<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/jspf/header.jsp"><jsp:param name="title" value="Trang chủ nhân viên bán hàng"/></jsp:include>
<h1>Xin chào, <c:out value="${sessionScope.account.name}"/></h1>
<div class="menu">
    <a id="subCreateOrder" class="btn" href="#" aria-disabled="true" title="Chưa triển khai trong phạm vi bài này">Tạo đơn hàng</a>
    <a id="subPayment" class="btn" href="${pageContext.request.contextPath}/sales/SearchCustomerFrm.jsp">Nhận thanh toán</a>
    <a id="subImportPart" class="btn" href="#" aria-disabled="true" title="Chưa triển khai trong phạm vi bài này">Nhập phụ tùng</a>
    <a id="subManageWareHouse" class="btn" href="#" aria-disabled="true" title="Chưa triển khai trong phạm vi bài này">Quản lý kho</a>
</div>
<jsp:include page="/WEB-INF/jspf/footer.jsp"/>
