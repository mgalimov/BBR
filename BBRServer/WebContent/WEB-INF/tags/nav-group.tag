<%@ tag language="java" pageEncoding="UTF-8" description="Navigation Group"
		import="BBRClientApp.BBRContext"%>
<%@ attribute name="title" required="true" %>
<%@ attribute name="level" required="true" %>
<%@ attribute name="faicon" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<% BBRContext context = BBRContext.getContext(request); 
   request.setAttribute("allowed", context.isPageAllowed(level));%>

<c:if test="${allowed}">
	<li class="treeview">
        <a href="#"><i class="fa fa-${faicon}"></i><span class="small">${context.gs(title)}</span> <i class="fa fa-angle-left pull-right"></i></a>
        <ul class="treeview-menu">
		    <jsp:doBody/>
		</ul>
	</li>
</c:if>