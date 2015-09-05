<%@ tag language="java" pageEncoding="UTF-8" description="Dashboard Group" import="BBRClientApp.BBRContext"%>
<%@ attribute name="title" required="true" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% BBRContext context = BBRContext.getContext(request); %>

<div class="row">
	<h2 class="panel-title">${context.gs(title)}</h2>
	<jsp:doBody/>
</div>