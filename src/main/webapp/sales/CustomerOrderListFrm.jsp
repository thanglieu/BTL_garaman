<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<jsp:include page="/WEB-INF/jspf/header.jsp"><jsp:param name="title" value="Đơn hàng của khách hàng"/></jsp:include>
<h1>Đơn hàng của khách hàng</h1>
<p>Khách hàng: <strong id="outCustomerName"><c:out value="${customer.name}"/></strong></p>
<table id="outOrderListTable">
    <thead><tr><th>Mã đơn hàng</th><th>Mô tả</th><th>Ngày tạo</th><th>Trạng thái</th><th>Hóa đơn</th><th>Ghi chú</th></tr></thead>
    <tbody>
    <c:forEach var="o" items="${orders}">
        <tr>
            <td><c:out value="${o.id}"/></td>
            <td><c:out value="${o.description}"/></td>
            <td><fmt:formatDate value="${o.createdDate}" pattern="dd/MM/yyyy"/></td>
            <td><c:out value="${o.status ? 'Đã thanh toán' : 'Chưa thanh toán'}"/></td>
            <td><c:url var="u" value="/pay">
                    <c:param name="action" value="showBill"/><c:param name="orderId" value="${o.id}"/>
                </c:url><a class="subBillOfOrder" href="${u}"><c:out value="${o.status ? 'Xem chi tiết' : 'Thanh toán'}"/></a></td>
            <td><c:out value="${o.note}"/></td>
        </tr>
    </c:forEach>
    <c:if test="${empty orders}"><tr><td colspan="6" class="muted">Khách hàng chưa có đơn hàng nào.</td></tr></c:if>
    </tbody>
</table>
<p><a class="btn secondary" href="${pageContext.request.contextPath}/sales/SearchCustomerFrm.jsp">← Tìm khách hàng khác</a></p>
<jsp:include page="/WEB-INF/jspf/footer.jsp"/>
