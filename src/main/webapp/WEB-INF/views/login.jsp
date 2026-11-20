<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/jspf/header.jsp"><jsp:param name="title" value="Đăng nhập"/></jsp:include>
<h1>Đăng nhập</h1>
<c:if test="${not empty error}"><div class="msg err" role="alert"><c:out value="${error}"/></div></c:if>
<form class="login" method="post" action="${pageContext.request.contextPath}/login">
    <input type="hidden" name="csrf" value="<c:out value='${csrf}'/>">
    <label>Tên đăng nhập
        <input type="text" name="username" value="<c:out value='${username}'/>" maxlength="50" required autofocus autocomplete="username">
    </label>
    <label>Mật khẩu
        <input type="password" name="password" maxlength="100" required autocomplete="current-password">
    </label>
    <button type="submit" class="btn primary">Đăng nhập</button>
</form>
<jsp:include page="/WEB-INF/jspf/footer.jsp"/>
