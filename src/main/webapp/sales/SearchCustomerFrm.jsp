<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/jspf/header.jsp"><jsp:param name="title" value="Tìm khách hàng"/></jsp:include>
<h1>Thanh toán — tìm khách hàng</h1>
<form class="search" method="get" action="${pageContext.request.contextPath}/pay">
    <input type="hidden" name="action" value="searchCustomer">
    <input id="inSearchTextField" type="text" name="keyword" maxlength="100"
           placeholder="Nhập tên khách hàng" value="<c:out value='${keyword}'/>" autofocus>
    <button id="inSearchButton" type="submit" class="btn">Tìm kiếm</button>
</form>
<c:if test="${searched}">
    <table id="outCustomersByNameTable">
        <thead><tr><th>Tên</th><th>SĐT</th><th></th></tr></thead>
        <tbody>
        <c:forEach var="cu" items="${customers}">
            <tr>
                <td><c:out value="${cu.name}"/></td>
                <td><c:out value="${cu.phoneNumber}"/></td>
                <td><c:url var="u" value="/pay">
                        <c:param name="action" value="searchOrder"/><c:param name="customerId" value="${cu.id}"/>
                    </c:url><a class="subOrderByCustomer" href="${u}">Chọn khách hàng</a></td>
            </tr>
        </c:forEach>
        <c:if test="${empty customers}"><tr><td colspan="3" class="muted">Không có khách hàng nào khớp tên.</td></tr></c:if>
        </tbody>
    </table>
</c:if>
<p><a class="btn secondary" href="${pageContext.request.contextPath}/sales/SalesStaffHomeFrm.jsp">← Về trang chủ</a></p>
<jsp:include page="/WEB-INF/jspf/footer.jsp"/>
