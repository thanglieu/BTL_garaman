package com.garaman.servlet;

import com.garaman.util.CsrfUtil;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (CsrfUtil.isValid(req)) {
            HttpSession s = req.getSession(false);
            if (s != null) {
                s.invalidate();
            }
        }
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
