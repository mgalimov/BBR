<%@ tag language="java" pageEncoding="UTF-8" description="Action Item"
		import="BBRClientApp.BBRContext"%>
<%@ attribute name="title" required="true" %>
<%@ attribute name="href" required="true" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<% BBRContext context = BBRContext.getContext(request); 
   request.setAttribute("allowed", context.isPageAllowed(href));
%>

<c:if test="${allowed}">
    <li><a href="${href}"><c:out value="${context.gs(title)}"/></a></li>
</c:if>