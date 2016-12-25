<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"
		 import="BBRClientApp.BBRContext" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%
	BBRContext context = BBRContext.getContext(request);
	request.setAttribute("userName", context.user.getFirstName() + " " + context.user.getLastName());
%>
<t:wrapper title="LBL_CONTROL_PANEL">
	<jsp:body>
  		<t:dashboard title="LBL_CONTROL_PANEL" hidePanel="true">
		  		<t:dashboard-item type="singleValue" title="LBL_NEW_VISITS" method="BBRVisitCharts" indicator="newVisits" color="green" icon="dashboard"/>
		  		<t:dashboard-item type="singleValue" title="LBL_TODAY_VISITS" method="BBRVisitCharts" indicator="todayVisits" color="red" icon="dashboard"/>
		  		<t:dashboard-item type="singleValue" title="LBL_TOMORROW_VISITS" method="BBRVisitCharts" indicator="tomorrowVisits" color="blue" icon="dashboard"/>
		</t:dashboard>
	</jsp:body>
</t:wrapper>