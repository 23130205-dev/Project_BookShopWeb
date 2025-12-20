<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="contextPath" content="${pageContext.request.contextPath}" />
<c:if test="${not empty sessionScope.currentUser}">
	<meta name="currentUserId" content="${sessionScope.currentUser.id}" />
</c:if>
<c:if test="${not empty requestScope.product}">
	<meta name="productId" content="${requestScope.product.id}" />
</c:if>

<link href="${pageContext.request.contextPath}/img/favicon.ico"
	rel="shortcut icon" type="image/x-icon">