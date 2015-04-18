<%@tag import="org.eclipse.jdt.internal.compiler.ast.ThisReference"%>
<%@tag import="javax.servlet.jsp.tagext.Tag"%>
<%@ tag language="java" pageEncoding="UTF-8" description="Navigation Item"
		import="BBRClientApp.BBRContext"%>
<%@ attribute name="title" required="true" %>
<%@ attribute name="href" required="true" %>
<%@ attribute name="badge" %>
<%@ attribute name="badgeMethod" %>
<%@ attribute name="active" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<% BBRContext context = BBRContext.getContext(request); 
   request.setAttribute("allowed", context.isPageAllowed(href));
   request.setAttribute("itemId", this.hashCode());%>

<c:if test="${allowed}">
	<c:if test="${active.equals('true')}">
		<c:set var="aclass" value="active"/>
	</c:if>
	<li>
		<a href="${href}" class="${aclass}">
		<c:if test="${badge.equals('true')}">
			<span class="badge pull-right" id="${itemId}">0</span>
		</c:if>
		<c:out value="${title}"/></a>
	</li>
</c:if>

<c:if test="${badge.equals('true')}">
<script>
	$.ajax({url: "${badgeMethod}", 
			data: {operation: "badge"}})
	 	.done(function(data){
	 		$("#${itemId}").text(data);
	 	});
</script>
</c:if>