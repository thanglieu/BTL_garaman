<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<jsp:include page="/WEB-INF/jspf/header.jsp"><jsp:param name="title" value="Chi tiết dịch vụ/phụ tùng"/></jsp:include>
<c:choose>
    <c:when test="${empty item}">
        <div class="msg err">Chưa chọn dịch vụ hoặc phụ tùng nào.</div>
    </c:when>
    <c:otherwise>
        <h1>Chi tiết <c:out value="${kind == 'service' ? 'dịch vụ' : 'phụ tùng'}"/></h1>
        <dl class="info">
            <dt>Mã</dt><dd id="outID"><c:out value="${item.id}"/></dd>
            <dt>Tên</dt><dd id="outName"><c:out value="${item.name}"/></dd>
            <dt>Mô tả</dt><dd id="outDescription"><c:out value="${item.description}"/></dd>
            <dt>Giá</dt><dd id="outPrice"><fmt:formatNumber value="${item.price}" pattern="#,##0"/> đ</dd>
        </dl>
    </c:otherwise>
</c:choose>
<p><a class="btn secondary" href="javascript:history.back()">← Quay lại kết quả</a>
   <a class="btn secondary" href="${pageContext.request.contextPath}/customer/CustomerHomeFrm.jsp">Trang chủ</a></p>
<jsp:include page="/WEB-INF/jspf/footer.jsp"/>
