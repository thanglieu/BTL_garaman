<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<c:set var="type" value="${param.type == 'service' || param.type == 'part' ? param.type : 'all'}"/>
<jsp:include page="/WEB-INF/jspf/header.jsp"><jsp:param name="title" value="Tìm kiếm dịch vụ/phụ tùng"/></jsp:include>
<h1>Tìm kiếm dịch vụ / phụ tùng</h1>
<form class="search" method="get" action="${pageContext.request.contextPath}/choose-service-or-part">
    <input type="hidden" name="action" value="search">
    <input id="inSearchTextField" type="text" name="keyword" maxlength="100"
           placeholder="Nhập tên dịch vụ/phụ tùng" value="<c:out value='${keyword}'/>" autofocus>
    <button id="inSearchButton" type="submit" class="btn">Tìm kiếm</button>
</form>

<c:if test="${searched}">

        <h2>Dịch vụ</h2>
        <table id="outServiceTableList">
            <thead><tr><th>Tên dịch vụ</th><th class="num">Giá (đ)</th><th></th></tr></thead>
            <tbody>
            <c:forEach var="s" items="${serviceList}">
                <tr>
                    <td><c:out value="${s.name}"/></td>
                    <td class="num"><fmt:formatNumber value="${s.price}" pattern="#,##0"/></td>
                    <td><c:url var="u" value="/choose-service-or-part">
                            <c:param name="action" value="detail"/><c:param name="kind" value="service"/><c:param name="id" value="${s.id}"/>
                        </c:url><a class="subServiceDetail" href="${u}">Xem chi tiết</a></td>
                </tr>
            </c:forEach>
            <c:if test="${empty serviceList}"><tr><td colspan="3" class="muted">Không có dịch vụ nào khớp từ khóa.</td></tr></c:if>
            </tbody>
        </table>
        <br>
        <h2>Phụ tùng</h2>
        <table id="outPartTypeTableList">
            <thead><tr><th>Tên phụ tùng</th><th class="num">Giá (đ)</th><th></th></tr></thead>
            <tbody>
            <c:forEach var="p" items="${partList}">
                <tr>
                    <td><c:out value="${p.name}"/></td>
                    <td class="num"><fmt:formatNumber value="${p.price}" pattern="#,##0"/></td>
                    <td><c:url var="u" value="/choose-service-or-part">
                            <c:param name="action" value="detail"/><c:param name="kind" value="part"/><c:param name="id" value="${p.id}"/>
                        </c:url><a class="subPartDetail" href="${u}">Xem chi tiết</a></td>
                </tr>
            </c:forEach>
            <c:if test="${empty partList}"><tr><td colspan="3" class="muted">Không có phụ tùng nào khớp từ khóa.</td></tr></c:if>
            </tbody>
        </table>
 
</c:if>
<p><a class="btn secondary" href="${pageContext.request.contextPath}/customer/CustomerHomeFrm.jsp">← Về trang chủ</a></p>
<jsp:include page="/WEB-INF/jspf/footer.jsp"/>
