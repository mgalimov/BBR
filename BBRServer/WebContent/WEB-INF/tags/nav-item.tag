<%@ tag language="java" pageEncoding="UTF-8" description="Navigation Item"
		import="BBRClientApp.BBRContext"%>
<%@ attribute name="title" required="true" %>
<%@ attribute name="href" required="true" %>
<%@ attribute name="active" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<% BBRContext context = BBRContext.getContext(request); 
   request.setAttribute("allowed", context.isPageAllowed(href));%>

<c:if test="${allowed}">
	<c:choose>
	<c:when test="${active.equals('true')}">
		<li><a href="${href}" class="active"><c:out value="${title}"/></a></li>
	</c:when>
	<c:otherwise>
		<li><a href="${href}"><c:out value="${title}"/></a></li>
	</c:otherwise>
	</c:choose>
</c:if>