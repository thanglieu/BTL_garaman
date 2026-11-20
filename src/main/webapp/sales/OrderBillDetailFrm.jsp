<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<jsp:include page="/WEB-INF/jspf/header.jsp"><jsp:param name="title" value="Hóa đơn"/></jsp:include>
<h1>Hóa đơn thanh toán</h1>
<c:if test="${not empty success}"><div class="msg ok no-print" role="status"><c:out value="${success}"/></div></c:if>
<c:if test="${not empty error}"><div class="msg err no-print" role="alert"><c:out value="${error}"/></div></c:if>
<c:choose>
<c:when test="${empty order}">
    <div class="msg err">Chưa chọn đơn hàng nào.</div>
</c:when>
<c:otherwise>
    <dl class="info">
        <dt>Khách hàng</dt><dd><c:out value="${order.guest.name}"/></dd>
        <dt>Đơn hàng</dt><dd><c:out value="${order.description}"/> (<c:out value="${order.id}"/>)</dd>
        <dt>Ghi chú</dt><dd><c:out value="${order.note}"/></dd>
        <dt>Trạng thái</dt><dd><c:out value="${order.status ? 'Đã thanh toán' : 'Chưa thanh toán'}"/></dd>
    </dl>

    <h2>Dịch vụ đã sử dụng</h2>
    <table id="servicesPriceListTable">
        <thead><tr><th>Dịch vụ</th><th>Kỹ thuật viên</th><th class="num">Thành tiền (đ)</th></tr></thead>
        <tbody>
        <c:forEach var="s" items="${order.serviceList}">
            <tr><td><c:out value="${s.type.name}"/></td>
                <td><c:out value="${s.engineer.name}"/></td>
                <td class="num"><fmt:formatNumber value="${s.servicePrice}" pattern="#,##0"/></td></tr>
        </c:forEach>
        <c:if test="${empty order.serviceList}"><tr><td colspan="3" class="muted">Không có dịch vụ.</td></tr></c:if>
        </tbody>
    </table>

    <h2>Phụ tùng đã sử dụng</h2>
    <table id="partsPriceListTable">
        <thead><tr><th>Phụ tùng</th><th class="num">Thành tiền (đ)</th></tr></thead>
        <tbody>
        <c:forEach var="p" items="${order.partList}">
            <tr><td><c:out value="${p.type.name}"/></td>
                <td class="num"><fmt:formatNumber value="${p.partPrice}" pattern="#,##0"/></td></tr>
        </c:forEach>
        <c:if test="${empty order.partList}"><tr><td colspan="2" class="muted">Không có phụ tùng.</td></tr></c:if>
        </tbody>
    </table>

    <table class="totals">
        <tr><td>Tiền dịch vụ</td><td class="num"><fmt:formatNumber value="${bill.servicesPrice}" pattern="#,##0"/> đ</td></tr>
        <tr><td>Tiền phụ tùng</td><td class="num"><fmt:formatNumber value="${bill.partsPrice}" pattern="#,##0"/> đ</td></tr>
        <tr><td>Khuyến mãi</td><td class="num" id="discountTextField">- <fmt:formatNumber value="${bill.discount}" pattern="#,##0"/> đ</td></tr>
        <tr class="total"><td>Tổng thanh toán</td><td class="num" id="totalPriceTextField"><fmt:formatNumber value="${bill.total}" pattern="#,##0"/> đ</td></tr>
        <tr><td>Ngày thanh toán</td><td class="num" id="paymentDateTextField"><fmt:formatDate value="${bill.paymentDate}" pattern="dd/MM/yyyy"/></td></tr>
    </table>

    <div class="actions">
        <c:choose>
            <c:when test="${order.status}">
                <button type="button" class="btn" onclick="window.print()">In hóa đơn</button>
            </c:when>
            <c:otherwise>
                <form method="post" action="${pageContext.request.contextPath}/pay">
                    <input type="hidden" name="action" value="confirm">
                    <input type="hidden" name="orderId" value="<c:out value='${order.id}'/>">
                    <input type="hidden" name="csrf" value="<c:out value='${csrf}'/>">
                    <button id="confirmButton" type="submit" class="btn primary">Xác nhận thanh toán</button>
                </form>
            </c:otherwise>
        </c:choose>
        <c:url var="back" value="/pay">
            <c:param name="action" value="searchOrder"/><c:param name="customerId" value="${order.guest.id}"/>
        </c:url>
        <a id="cancelButton" class="btn secondary" href="${back}"><c:out value="${order.status ? 'Quay lại' : 'Hủy'}"/></a>
    </div>
    <c:if test="${autoPrint}"><script>window.addEventListener('load', function () { window.print(); });</script></c:if>
</c:otherwise>
</c:choose>
<jsp:include page="/WEB-INF/jspf/footer.jsp"/>
