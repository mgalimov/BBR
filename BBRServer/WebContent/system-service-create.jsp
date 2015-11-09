<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-card-wrapper title="LBL_SERVICE_CREATE">
	<jsp:body>
		<t:card method="BBRServices" gridPage="system-service-list.jsp" title="LBL_SERVICE_CREATE">
			<t:card-item label="LBL_TITLE" field="title" type="text" isRequired="required"/>
			<t:card-item label="LBL_SERVICE_STATUS" field="status" type="select" options="OPT_SERVICE_STATUS"/>
			<t:card-item label="LBL_SERVICE_DEMO" field="demo" type="boolean"/>
			<t:card-item label="LBL_SERVICE_BASIC" field="basic" type="boolean" />
		</t:card>
	</jsp:body>
</t:admin-card-wrapper>