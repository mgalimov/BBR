<%@ tag language="java" pageEncoding="UTF-8" description="Dashboard" import="BBRClientApp.BBRContext"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="titleModifier" %>

<% BBRContext context = BBRContext.getContext(request); %>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<c:set var="items" scope="request" value=""/>
<c:set var="chartpackages" scope="request" value=""/>

<div class="row">
	<h3>${context.gs(title).concat(titleModifier)}</h3>
	<jsp:doBody/>
</div> 

<script>
	google.load('visualization', '1.0', {'packages':[${chartpackages}]});
	google.setOnLoadCallback(drawCharts);
	function drawCharts() {
		${items}
	}
</script>
