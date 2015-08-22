<%@ page import="BBRClientApp.BBRContext"%>
<%@ page import="BBR.BBRUtil"%>
<%@ page import="BBRClientApp.BBRParams"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:admin-grid-wrapper title="LBL_SPEC_TURNS_TITLE">
	<jsp:body>
		<t:grid method="BBRTurns" editPage="manager-turn-edit.jsp" createPage="manager-turn-create.jsp" title="LBL_SPEC_TURNS_TITLE">
			<t:grid-item label="LBL_SPECIALIST" field="specialist.name"/>
			<t:grid-item label="LBL_START_TIME" field="startTime" />
			<t:grid-item label="LBL_END_TIME" field="endTime" />
		</t:grid>
	</jsp:body>
</t:admin-grid-wrapper>
