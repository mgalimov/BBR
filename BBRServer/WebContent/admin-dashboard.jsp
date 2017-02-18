<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"
		 import="BBRClientApp.BBRContext" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:wrapper title="LBL_DASHBOARD_TITLE">
	<jsp:body>
		<t:dashboard title="LBL_DASHBOARD_TITLE">
<%-- 	  		<t:dashboard-item type="pie" title="LBL_DASHBOARD_VISITS" method="BBRVisitCharts" indicator="visitsByPeriod"/> --%>
	  		<t:dashboard-item type="line" title="LBL_DASHBOARD_VISITS" method="BBRVisitCharts" indicator="visitsByPeriod"/>
<%-- 	  		<t:dashboard-item type="line" title="LBL_DASHBOARD_VISITS" method="BBRVisitCharts" indicator="visitsByPeriod"/> --%>
	  		<t:dashboard-item type="line" title="LBL_DASHBOARD_INCOME" method="BBRVisitCharts" indicator="incomeByPeriod"/>
	  		<t:dashboard-item type="bar" title="LBL_DASHBOARD_VISITS" method="BBRVisitCharts" indicator="visitsByWeekDays"/>
	  		<t:dashboard-item type="pie" title="LBL_DASHBOARD_VISITORS" method="BBRVisitorCharts" indicator="visitorsNewVsReturned"/>
	  		<t:dashboard-item type="pie" title="LBL_DASHBOARD_SOURCES" method="BBRVisitCharts" indicator="visitsBySources"/>
		</t:dashboard>
	</jsp:body>
</t:wrapper>
