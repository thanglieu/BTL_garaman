<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/jspf/header.jsp"><jsp:param name="title" value="Trang chủ"/></jsp:include>
<h1>Xin chào, <c:out value="${sessionScope.account.name}"/></h1>
<p class="muted">Vai trò <strong><c:out value="${sessionScope.role}"/></strong> chưa có chức năng nào được triển khai
    trong phạm vi bài này (chỉ có module Tìm kiếm dịch vụ/phụ tùng cho khách hàng và module Thanh toán cho nhân viên bán hàng).</p>
<jsp:include page="/WEB-INF/jspf/footer.jsp"/>
