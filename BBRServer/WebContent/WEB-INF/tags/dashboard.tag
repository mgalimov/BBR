<%@ tag language="java" pageEncoding="UTF-8" description="Dashboard" import="BBRClientApp.BBRContext"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<c:set var="items" scope="request" value="${''}"/>

<jsp:doBody/>
 
<script>
	google.load('visualization', '1.0', {'packages':['corechart']});
	google.setOnLoadCallback(drawCharts);
	function drawCharts() {
		${items}
	}
</script>
