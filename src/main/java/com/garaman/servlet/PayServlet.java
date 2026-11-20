package com.garaman.servlet;

import com.garaman.dao.BillDAO;
import com.garaman.dao.CustomerDAO;
import com.garaman.dao.DataAccessException;
import com.garaman.dao.OrderDAO;
import com.garaman.model.Bill;
import com.garaman.model.Customer;
import com.garaman.model.Order;
import com.garaman.util.CsrfUtil;
import com.garaman.util.DBUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Module "Thanh toan" (nhan vien ban hang).
 * GET  ?action=searchCustomer&keyword=...   -> searchCustomer()
 * GET  ?action=searchOrder&customerId=...   -> searchOrder()
 * GET  ?action=showBill&orderId=...         -> showBill()   (xem truoc hoa don, chua luu)
 * POST action=confirm&orderId=...&csrf=...  -> confirm()    (luu hoa don + chuyen trang thai "da thanh toan")
 */
@WebServlet("/pay")
public class PayServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final String FRM_SEARCH_CUSTOMER = "/sales/SearchCustomerFrm.jsp";
    private static final String FRM_ORDER_LIST = "/sales/CustomerOrderListFrm.jsp";
    private static final String FRM_BILL = "/sales/OrderBillDetailFrm.jsp";
    private static final ZoneId ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    private CustomerDAO customerDAO;
    private OrderDAO orderDAO;
    private BillDAO billDAO;

    @Override
    public void init() {
        customerDAO = new CustomerDAO();
        orderDAO = new OrderDAO();
        billDAO = new BillDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = trim(request.getParameter("action"));
        try {
            switch (action) {
                case "searchCustomer":
                    searchCustomer(request, response);
                    break;
                case "searchOrder":
                    searchOrder(request, response);
                    break;
                case "showBill":
                    showBill(request, response);
                    break;
                default:
                    forward(request, response, FRM_SEARCH_CUSTOMER);
            }
        } catch (DataAccessException e) {
            fail(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Hệ thống đang gặp sự cố, vui lòng thử lại sau.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!"confirm".equals(trim(request.getParameter("action")))) {
            fail(request, response, HttpServletResponse.SC_BAD_REQUEST, "Yêu cầu không hợp lệ.", null);
            return;
        }
        try {
            confirm(request, response);
        } catch (DataAccessException e) {
            fail(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Không thể xử lý thanh toán, hóa đơn chưa được lưu. Vui lòng thử lại.", e);
        }
    }

    /** Tim khach hang theo ten, chuyen danh sach len SearchCustomerFrm. */
    protected void searchCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = trim(request.getParameter("keyword"));
        List<Customer> customers = customerDAO.getCustomerByName(keyword);
        request.setAttribute("keyword", keyword);
        request.setAttribute("customers", customers);
        request.setAttribute("searched", Boolean.TRUE);
        forward(request, response, FRM_SEARCH_CUSTOMER);
    }

    /** Lay danh sach don hang cua khach hang da chon, chuyen len CustomerOrderListFrm. */
    protected void searchOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String customerId = trim(request.getParameter("customerId"));
        Customer customer = customerDAO.getCustomerById(customerId);
        if (customer == null) {
            fail(request, response, HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy khách hàng.", null);
            return;
        }
        List<Order> orders = orderDAO.getOrderByCustomer(customerId);
        request.setAttribute("customer", customer);
        request.setAttribute("orders", orders);
        forward(request, response, FRM_ORDER_LIST);
    }

    /**
     * Xem hoa don: don chua thanh toan -> tinh hoa don xem truoc (chua luu CSDL);
     * don da thanh toan -> hien hoa don da luu.
     */
    protected void showBill(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Order order = orderDAO.getOrderById(trim(request.getParameter("orderId")));
        if (order == null) {
            fail(request, response, HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy đơn hàng.", null);
            return;
        }
        Bill bill = null;
        if (order.isStatus()) {
            bill = billDAO.getBillByOrder(order.getId());
        }
        if (bill == null) {
            bill = buildBill(order);
        } else {
            bill.setOrder(order);
        }
        showBillForm(request, response, order, bill);
    }

    /**
     * Xac nhan thanh toan. Tinh lai hoa don o phia may chu (khong tin so tien tu trinh duyet), roi trong
     * MOT giao dich: chuyen trang thai don hang sang "da thanh toan" va luu hoa don. Hoac luu tron ven,
     * hoac khong luu gi (E4). Neu don hang vua duoc nhan vien khac xu ly thi bao loi va hoan tac (E2).
     */
    protected void confirm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!CsrfUtil.isValid(request)) {
            fail(request, response, HttpServletResponse.SC_FORBIDDEN,
                    "Phiên làm việc không hợp lệ, vui lòng tải lại trang và thử lại.", null);
            return;
        }
        String orderId = trim(request.getParameter("orderId"));
        Order order = orderDAO.getOrderById(orderId);
        if (order == null) {
            fail(request, response, HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy đơn hàng.", null);
            return;
        }
        if (order.isStatus()) {
            conflict(request, response, order);
            return;
        }

        Bill bill = buildBill(order);
        boolean saved;
        try (Connection c = DBUtil.getConnection()) {
            c.setAutoCommit(false);
            try {
                saved = orderDAO.updateOrderStatus(orderId, true, c) && billDAO.createBill(bill, c);
                if (saved) {
                    c.commit();
                } else {
                    c.rollback();
                }
            } catch (RuntimeException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Loi giao dich thanh toan", e);
        }

        if (!saved) {
            conflict(request, response, orderDAO.getOrderById(orderId));
            return;
        }
        order.setStatus(true);
        request.setAttribute("success", "Thanh toán thành công. Hóa đơn đang được in.");
        request.setAttribute("autoPrint", Boolean.TRUE);
        showBillForm(request, response, order, bill);
    }

    /** Dong goi hoa don tu don hang: tien dich vu, tien phu tung, giam gia theo diem, tong tien. */
    private Bill buildBill(Order order) {
        double servicesPrice = order.getServicesPrice();
        double partsPrice = order.getPartsPrice();
        double subtotal = servicesPrice + partsPrice;
        double discount = order.getGuest().getDiscountPrice(subtotal);
        Bill bill = new Bill();
        bill.setServicesPrice(servicesPrice);
        bill.setPartsPrice(partsPrice);
        bill.setDiscount(discount);
        bill.setTotal(subtotal - discount);
        bill.setPaymentDate(Date.valueOf(LocalDate.now(ZONE)));
        bill.setOrder(order);
        return bill;
    }

    private void showBillForm(HttpServletRequest request, HttpServletResponse response, Order order, Bill bill)
            throws ServletException, IOException {
        request.setAttribute("order", order);
        request.setAttribute("bill", bill);
        forward(request, response, FRM_BILL);
    }

    private void conflict(HttpServletRequest request, HttpServletResponse response, Order order)
            throws ServletException, IOException {
        Bill stored = order == null ? null : billDAO.getBillByOrder(order.getId());
        request.setAttribute("error",
                "Hóa đơn vừa được xử lý bởi nhân viên khác, vui lòng tải lại.");
        if (order == null) {
            fail(request, response, HttpServletResponse.SC_CONFLICT, "Đơn hàng không còn tồn tại.", null);
            return;
        }
        Bill bill = stored != null ? stored : buildBill(order);
        bill.setOrder(order);
        response.setStatus(HttpServletResponse.SC_CONFLICT);
        showBillForm(request, response, order, bill);
    }
}
