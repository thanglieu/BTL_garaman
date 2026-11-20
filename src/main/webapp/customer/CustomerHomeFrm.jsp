<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/jspf/header.jsp"><jsp:param name="title" value="Trang chủ khách hàng"/></jsp:include>
<h1>Xin chào, <c:out value="${sessionScope.account.name}"/></h1>
<div class="menu">
    <a id="subSearchService" class="btn" href="${pageContext.request.contextPath}/choose-service-or-part?type=service">Tìm kiếm dịch vụ/phụ tùng</a>
    <a id="subSchedule" class="btn" href="#" aria-disabled="true" title="Chưa triển khai trong phạm vi bài này">Đặt lịch hẹn</a>
</div>
<jsp:include page="/WEB-INF/jspf/footer.jsp"/>
