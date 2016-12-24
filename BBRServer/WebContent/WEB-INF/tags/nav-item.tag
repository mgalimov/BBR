<%@tag import="org.eclipse.jdt.internal.compiler.ast.ThisReference"%>
<%@tag import="javax.servlet.jsp.tagext.Tag"%>
<%@ tag language="java" pageEncoding="UTF-8" description="Navigation Item"
		import="BBRClientApp.BBRContext"%>
<%@ attribute name="title" required="true" %>
<%@ attribute name="href" required="true" %>
<%@ attribute name="badge" %>
<%@ attribute name="badgeMethod" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<% BBRContext context = BBRContext.getContext(request); 
   request.setAttribute("allowed", context.isPageAllowed(href));
   String aclass = "";
   if (request.getRequestURI().contains(href))
	   aclass = "active";
   request.setAttribute("aclass", aclass);
   request.setAttribute("itemId", badgeMethod + "_badge");%>

<c:if test="${allowed}">
	<li class="${aclass}">
		<a href="${href}"><c:if test="${badge.equals('true')}"><span class="badge pull-right" id="${itemId}"></span></c:if>
			${context.gs(title)}
		</a>
	</li>
</c:if>

<c:if test="${badge.equals('true')}">
<script>
	function ${badgeMethod}TimerFunc() {
		$.ajax({url: "${badgeMethod}", 
			data: {operation: "badge"}})
	 	.done(function(data){
	 		if (data == "")
	 			$("[id^=${itemId}]").addClass("hidden");
	 		else
	 			$("[id^=${itemId}]").text(data);
	 		setTimeout(${badgeMethod}TimerFunc, 60000);
	 	});		
	}

	setTimeout(${badgeMethod}TimerFunc, 60000);
	${badgeMethod}TimerFunc();
	
</script>
</c:if>