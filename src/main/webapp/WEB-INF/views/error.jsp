<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/jspf/header.jsp"><jsp:param name="title" value="Thông báo"/></jsp:include>
<h1>Không thể hoàn thành yêu cầu</h1>
<div class="msg err" role="alert">
    <c:choose>
        <c:when test="${not empty errorMessage}"><c:out value="${errorMessage}"/></c:when>
        <c:otherwise>Trang không tồn tại hoặc bạn không có quyền truy cập.</c:otherwise>
    </c:choose>
</div>
<p><a class="btn secondary" href="${pageContext.request.contextPath}/login">Về trang chủ</a></p>
<jsp:include page="/WEB-INF/jspf/footer.jsp"/>
