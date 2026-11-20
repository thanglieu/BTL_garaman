package com.garaman.filter;

import com.garaman.util.CsrfUtil;
import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Kiem tra dang nhap va phan quyen theo duong dan:
 * /customer/* va /choose-service-or-part -> CUSTOMER; /sales/* va /pay -> SALES;
 * /other-home.jsp -> bat ky tai khoan da dang nhap.
 */
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        String role = session == null ? null : (String) session.getAttribute("role");
        if (role == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String path = req.getServletPath();
        String required = requiredRole(path);
        if (required != null && !required.equals(role)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Ban khong co quyen truy cap chuc nang nay");
            return;
        }
        req.setAttribute("csrf", CsrfUtil.token(session));
        chain.doFilter(request, response);
    }

    private String requiredRole(String path) {
        if (path.startsWith("/customer/") || path.equals("/choose-service-or-part")) {
            return "CUSTOMER";
        }
        if (path.startsWith("/sales/") || path.equals("/pay")) {
            return "SALES";
        }
        return null;
    }
}
