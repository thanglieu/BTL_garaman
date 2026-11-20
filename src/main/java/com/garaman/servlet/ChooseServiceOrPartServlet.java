package com.garaman.servlet;

import com.garaman.dao.DataAccessException;
import com.garaman.dao.PartTypeDAO;
import com.garaman.dao.ServiceTypeDAO;
import com.garaman.model.PartType;
import com.garaman.model.ServiceType;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Module "Tim kiem thong tin dich vu/phu tung" (khach hang).
 * GET ?action=search&keyword=...  -> searchServiceOrPart()
 * GET ?action=detail&kind=service|part&id=...  -> showDetail()
 */
@WebServlet("/choose-service-or-part")
public class ChooseServiceOrPartServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final String FRM_CHOOSE = "/customer/ChooseServiceOrPartFrm.jsp";
    private static final String FRM_DETAIL = "/customer/ServiceOrPartFrm.jsp";

    private ServiceTypeDAO serviceTypeDAO;
    private PartTypeDAO partTypeDAO;

    @Override
    public void init() {
        serviceTypeDAO = new ServiceTypeDAO();
        partTypeDAO = new PartTypeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = trim(request.getParameter("action"));
        try {
            if ("search".equals(action)) {
                searchServiceOrPart(request, response);
            } else if ("detail".equals(action)) {
                showDetail(request, response);
            } else {
                forward(request, response, FRM_CHOOSE);
            }
        } catch (DataAccessException e) {
            fail(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Hệ thống đang gặp sự cố, vui lòng thử lại sau.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /** Tim dich vu va phu tung co ten chua tu khoa, chuyen ca hai danh sach len ChooseServiceOrPartFrm. */
    protected void searchServiceOrPart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = trim(request.getParameter("keyword"));
        List<ServiceType> serviceList = serviceTypeDAO.getServiceByName(keyword);
        List<PartType> partList = partTypeDAO.getPartTypeByName(keyword);
        request.setAttribute("keyword", keyword);
        request.setAttribute("serviceList", serviceList);
        request.setAttribute("partList", partList);
        request.setAttribute("searched", Boolean.TRUE);
        forward(request, response, FRM_CHOOSE);
    }

    /** Lay chi tiet dich vu hoac phu tung theo id roi chuyen len ServiceOrPartFrm. */
    protected void showDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String kind = trim(request.getParameter("kind"));
        String id = trim(request.getParameter("id"));
        if ("service".equals(kind)) {
            ServiceType s = serviceTypeDAO.getServiceById(id);
            if (s == null) {
                fail(request, response, HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy dịch vụ.", null);
                return;
            }
            request.setAttribute("kind", "service");
            request.setAttribute("item", s);
        } else if ("part".equals(kind)) {
            PartType p = partTypeDAO.getPartTypeById(id);
            if (p == null) {
                fail(request, response, HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy phụ tùng.", null);
                return;
            }
            request.setAttribute("kind", "part");
            request.setAttribute("item", p);
        } else {
            fail(request, response, HttpServletResponse.SC_BAD_REQUEST, "Yêu cầu không hợp lệ.", null);
            return;
        }
        forward(request, response, FRM_DETAIL);
    }
}
