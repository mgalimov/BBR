<%@ tag language="java" pageEncoding="UTF-8" description="Dashboard Item" import="BBRClientApp.BBRContext"%>
<%@ attribute name="title" required="true" %>
<%@ attribute name="type" required="true" %>
<%@ attribute name="method" required="true" %>
<%@ attribute name="indicator" required="true" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<% BBRContext context = BBRContext.getContext(request); %>

<c:set var="items" scope="request" value="${items}${indicator}${type}chart();
"/>
<div id="${indicator}_${type}_chart" class="dashboard"></div>
	
<script>
	function ${indicator}${type}chart () {
		$.ajax({
			url: "${method}",
			data: {
				type: "${type}",
				indicator: "${indicator}"
			}
		}).done(function (data) {
			var dt = $.parseJSON(data);
			var gdt = google.visualization.arrayToDataTable(dt, false);
			var chart = new google.visualization.PieChart(document.getElementById("${indicator}_${type}_chart"));
	        chart.draw(gdt);
		});
	};
</script>