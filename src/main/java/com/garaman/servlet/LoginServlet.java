package com.garaman.servlet;

import com.garaman.dao.AccountDAO;
import com.garaman.dao.DataAccessException;
import com.garaman.model.Account;
import com.garaman.util.CsrfUtil;
import com.garaman.util.PasswordUtil;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/** Dang nhap dung chung cho moi tac nhan; sau do chuyen den trang chu theo vai tro. */
@WebServlet("/login")
public class LoginServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    /** Dung de van thuc hien phep so sanh khi khong tim thay tai khoan (han che do thoi gian). */
    private static final String DUMMY_HASH = PasswordUtil.sha256Hex("dummy");

    private AccountDAO accountDAO;

    @Override
    public void init() {
        accountDAO = new AccountDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);
        String role = (String) session.getAttribute("role");
        if (role != null) {
            resp.sendRedirect(req.getContextPath() + homePath(role));
            return;
        }
        req.setAttribute("csrf", CsrfUtil.token(session));
        forward(req, resp, "/WEB-INF/views/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!CsrfUtil.isValid(req)) {
            fail(req, resp, HttpServletResponse.SC_FORBIDDEN, "Phien lam viec khong hop le, vui long tai lai trang.", null);
            return;
        }
        String username = trim(req.getParameter("username"));
        String password = req.getParameter("password");
        try {
            Account acc = accountDAO.getAccountByUsername(username);
            boolean ok = PasswordUtil.matches(password, acc == null ? DUMMY_HASH : acc.getPassword()) && acc != null;
            String role = ok ? accountDAO.getRole(acc.getId()) : null;
            if (!ok || role == null) {
                req.setAttribute("error", "Sai tên đăng nhập hoặc mật khẩu.");
                req.setAttribute("username", username);
                req.setAttribute("csrf", CsrfUtil.token(req.getSession(true)));
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                forward(req, resp, "/WEB-INF/views/login.jsp");
                return;
            }
            acc.setPassword(null); // khong giu mat khau trong session
            req.changeSessionId(); // chong session fixation
            HttpSession session = req.getSession();
            session.removeAttribute(CsrfUtil.ATTR);
            session.setAttribute("account", acc);
            session.setAttribute("role", role);
            resp.sendRedirect(req.getContextPath() + homePath(role));
        } catch (DataAccessException e) {
            fail(req, resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Hệ thống đang gặp sự cố, vui lòng thử lại sau.", e);
        }
    }

    static String homePath(String role) {
        switch (role) {
            case "CUSTOMER":
                return "/customer/CustomerHomeFrm.jsp";
            case "SALES":
                return "/sales/SalesStaffHomeFrm.jsp";
            default:
                return "/other-home.jsp";
        }
    }
}
