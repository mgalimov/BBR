<%@page import="BBRClientApp.BBRContext"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:admin-card-wrapper title="LBL_EDIT_TURN_TITLE">
<jsp:body>
		<t:card title="LBL_EDIT_TURN_TITLE" gridPage="manager-turn-list.jsp" method="BBRTurns">
			<t:card-item label="LBL_SPECIALIST" field="specialist" type="reference" isDisabled="readonly" referenceFieldTitle="name" referenceMethod="BBRSpecialists" />
			<t:card-item label="LBL_START_TIME" field="startTime" type="datetime"/>
			<t:card-item label="LBL_END_TIME" field="endTime" type="datetime"/>
		</t:card>
</jsp:body>
</t:admin-card-wrapper>
