package com.garaman.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/** Token CSRF luu trong session, dung cho moi form POST. */
public final class CsrfUtil {
    public static final String ATTR = "csrfToken";
    public static final String PARAM = "csrf";
    private static final SecureRandom RANDOM = new SecureRandom();

    private CsrfUtil() {
    }

    public static String token(HttpSession session) {
        String t = (String) session.getAttribute(ATTR);
        if (t == null) {
            byte[] b = new byte[24];
            RANDOM.nextBytes(b);
            t = Base64.getUrlEncoder().withoutPadding().encodeToString(b);
            session.setAttribute(ATTR, t);
        }
        return t;
    }

    public static boolean isValid(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        if (s == null) {
            return false;
        }
        String expected = (String) s.getAttribute(ATTR);
        String actual = req.getParameter(PARAM);
        return expected != null && actual != null && MessageDigest.isEqual(
                expected.getBytes(StandardCharsets.UTF_8), actual.getBytes(StandardCharsets.UTF_8));
    }
}
