<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"
		 import="BBRClientApp.BBRContext" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:admin-card-wrapper title="LBL_DASHBOARD_TITLE">
	<jsp:body>
		<t:dashboard>
		  	<t:dashboard-group title="LBL_DASHBOARD_TITLE">
		  		<t:dashboard-item type="pie" title="LBL_DASHBOARD_TASKS" method="BBRVisitCharts" indicator="xyz"/>
		  	</t:dashboard-group>
		</t:dashboard>
	</jsp:body>
</t:admin-card-wrapper>
