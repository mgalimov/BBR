<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-grid-wrapper title="LBL_SERVICES_TITLE">
	<jsp:body>
		<t:grid method="BBRServices" editPage="system-service-edit.jsp" createPage="system-service-create.jsp"
		        title="LBL_SERVICES_TITLE" standardFilters="false">
			<t:grid-item label="LBL_TITLE" field="title" sort="asc"/>
			<t:grid-item label="LBL_SERVICE_STATUS" field="status" type="select" options="OPT_SERVICE_STATUS"/>
			<t:grid-item label="LBL_SERVICE_DEMO" field="demo" type="boolean"/>
			<t:grid-item label="LBL_SERVICE_BASIC" field="basic" type="boolean"/>
			<t:grid-item label="LBL_SERVICE_PRICE" field="currentPrice"/>
		</t:grid>
	</jsp:body>
</t:admin-grid-wrapper>