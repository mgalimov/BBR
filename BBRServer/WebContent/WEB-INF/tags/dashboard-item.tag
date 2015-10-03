<%@ tag language="java" pageEncoding="UTF-8" description="Dashboard Item" import="BBRClientApp.BBRContext"%>
<%@ attribute name="title" required="true" %>
<%@ attribute name="type" required="true" %>
<%@ attribute name="method" required="true" %>
<%@ attribute name="indicator" required="true" %>
<%@ attribute name="options" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<% BBRContext context = BBRContext.getContext(request); %>

<c:set var="items" scope="request" value="${items}${indicator}${type}chart();
        "/>
<div id="${indicator}_${type}_chart" class="dashboard col-md-6"></div>
<c:set var="chartoptions" scope="page" value=""/>
<c:set var="chartpackage" scope="page" value=""/>

<c:choose>
	<c:when test="${type.equals('pie')}">
		<c:set var="chartpackage" scope="page" value=""/>
		<c:set var="typefunc" scope="page" value="PieChart"/>
	</c:when>
	<c:when test="${type.equals('bar')}">
		<c:set var="chartpackage" scope="page" value="'bar'"/>
		<c:set var="typefunc" scope="page" value="ColumnChart"/>
		<c:set var="chartoptions" scope="page" value="bars: 'vertical'"/>
	</c:when>
	<c:when test="${type.equals('line')}">
		<c:set var="chartpackage" scope="page" value=""/>
		<c:set var="typefunc" scope="page" value="LineChart"/>
	</c:when>
</c:choose>

<c:if test="${!chartpackage.equals('')}">
	<c:if test="${not fn:contains(chartpackages, chartpackage)}">
		<c:set var="chartpackages" scope="request" value="${chartpackages}, ${chartpackage}"/>
	</c:if>
</c:if>

<script>
	function ${indicator}${type}chart () {
		spId = $("#shopposinput").val();
		shopId = null;
		posId = null;
		if (spId.charAt(0) == "s")
			shopId = spId.substr(1);
		else
			posId = spId;
		
		$.ajax({
			url: "${method}",
			data: {
				type: "${type}",
				indicator: "${indicator}",
				periods: periods,
				options: "${options}",
				shopId: shopId,
				posId: posId
			}
		}).done(function (data) {
			var dt = $.parseJSON(data);
			//var gdt = google.visualization.arrayToDataTable(dt, false);
			var gdt = new google.visualization.DataTable(dt);
			var chart = new google.visualization.${typefunc}(document.getElementById("${indicator}_${type}_chart"));
			var options = {
					title: '${context.gs(title)}',
					${chartoptions}
			};
	        chart.draw(gdt, options);
		});
	};
</script>