package com.garaman.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Tien ich dung chung cho cac servlet: forward JSP va bao loi. */
public abstract class BaseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void forward(HttpServletRequest req, HttpServletResponse resp, String jsp)
            throws ServletException, IOException {
        req.getRequestDispatcher(jsp).forward(req, resp);
    }

    /** Hien trang loi voi thong diep than thien; chi ghi chi tiet ngoai le vao log may chu. */
    protected void fail(HttpServletRequest req, HttpServletResponse resp, int status, String message, Exception e)
            throws ServletException, IOException {
        if (e != null) {
            getServletContext().log(message, e);
        }
        resp.setStatus(status);
        req.setAttribute("errorMessage", message);
        forward(req, resp, "/WEB-INF/views/error.jsp");
    }

    protected static String trim(String s) {
        return s == null ? "" : s.trim();
    }
}
