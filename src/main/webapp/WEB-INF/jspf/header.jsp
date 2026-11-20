<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${param.title}"/> - GaraMan</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header class="topbar">
    <div class="brand">Hệ thống quản lý Gara</div>
    <c:if test="${not empty sessionScope.account}">
        <form class="logout" method="post" action="${pageContext.request.contextPath}/logout">
            <span><c:out value="${sessionScope.account.name}"/></span>
            <input type="hidden" name="csrf" value="<c:out value='${csrf}'/>">
            <button type="submit" class="btn-link">Đăng xuất</button>
        </form>
    </c:if>
</header>
<main class="container">
