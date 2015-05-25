<%@ tag language="java" pageEncoding="UTF-8" description="Navigation Group"
		import="BBRClientApp.BBRContext"%>
<%@ attribute name="title" required="true" %>
<%@ attribute name="level" required="true" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<% BBRContext context = BBRContext.getContext(request); 
   request.setAttribute("allowed", context.isPageAllowed(level));%>

<c:if test="${allowed}">
    <h5>${context.gs(title)}</h5>
	<ul class="nav nav-sidebar">
		<jsp:doBody/>
	</ul>
</c:if>