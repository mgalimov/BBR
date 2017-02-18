<%@ tag language="java" pageEncoding="UTF-8" description="Dashboard Item" import="BBRClientApp.BBRContext"%>
<%@ attribute name="title" required="true" %>
<%@ attribute name="type" required="true" %>
<%@ attribute name="method" required="true" %>
<%@ attribute name="indicator" required="true" %>
<%@ attribute name="icon" %>
<%@ attribute name="color" %>
<%@ attribute name="options" %>
<%@ attribute name="href" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<% BBRContext context = BBRContext.getContext(request); %>

<c:set var="items" scope="request" value="${items}${indicator}${type}chart();
        "/>
<c:set var="infoboxmargin" value="style='margin-left: 0px;'" />

<c:if test="${href!=null&&!href.isEmpty()}">
	<a href="${href}">
</c:if>
	<div class="info-box">
<c:if test="${icon!=null&&!icon.isEmpty()}">
		<span class="info-box-icon bg-${color}">
			<span class="glyphicon glyphicon-${icon}" aria-hidden="true"></span>
		</span>
	<c:set var="infoboxmargin" value="" />
</c:if>
		<div class="info-box-content" ${infoboxmargin}>
	 		<span class="info-box-text">${context.gs(title)}</span>
	 		<span class="info-box-number" id="${indicator}_${type}_chart"></span>
		</div>
	</div>
<c:if test="${href!=null&&!href.isEmpty()}">
	</a>
</c:if>

<c:set var="chartoptions" scope="page" value=""/>
<c:set var="chartpackage" scope="page" value=""/>

<c:choose>
	<c:when test="${type.equals('singleValue')}">
		<c:set var="chartpackage" scope="page" value=""/>
		<c:set var="typefunc" scope="page" value="Text"/>
		<c:set var="chartoptions" scope="page" value=""/>
	</c:when>
	<c:when test="${type.equals('pie')}">
		<c:set var="chartpackage" scope="page" value=""/>
		<c:set var="typefunc" scope="page" value="PieChart"/>
		<c:set var="chartoptions" scope="page" value="is3D: true"/>
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
		if (!google.visualization) return;
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
			var dt;
			if ("${type}" == "singleValue") {
				if (data == "")
					data = "${context.gs('MSG_NO_NEW_DATA')}";
				$("#${indicator}_${type}_chart").html(data);
			} else {
				if (data == "")
					dt = {cols:[{label:"X",type:"string"},{label:"Y",type:"number"}], rows:[]};
				else
					dt = $.parseJSON(data);
				var gdt = new google.visualization.DataTable(dt);
				var el = document.getElementById("${indicator}_${type}_chart");
				$(el).addClass("dashboard");
				var chart = new google.visualization.${typefunc}(el);
				var options = {
						legend: "right",
						${chartoptions}
				};
		        chart.draw(gdt, options);
			}
		});
	};
</script>