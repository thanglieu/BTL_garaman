package com.garaman.filter;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

/** Dat UTF-8 cho request/response (de hien thi dung tieng Viet) va them vai header bao mat co ban. */
public class EncodingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        if (resp instanceof HttpServletResponse) {
            HttpServletResponse r = (HttpServletResponse) resp;
            r.setHeader("X-Content-Type-Options", "nosniff");
            r.setHeader("X-Frame-Options", "DENY");
            r.setHeader("Cache-Control", "no-store");
        }
        chain.doFilter(req, resp);
    }
}
