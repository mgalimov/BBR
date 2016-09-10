<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:wrapper title="LBL_SPEC_TITLE">
	<jsp:body>
		<t:grid method="BBRSpecialists" editPage="manager-spec-edit.jsp" createPage="manager-spec-create.jsp" title="LBL_SPEC_TITLE">
			<t:grid-item label="LBL_NAME" field="name" sort="asc"/>
			<t:grid-item label="LBL_POS" field="pos.title"/>
			<t:grid-item label="LBL_POSITION" field="position"/>
			<t:grid-item label="LBL_SPEC_STATUS" field="status" type="select" options="OPT_SPEC_STATUS"/>
			<t:grid-item label="LBL_START_WORKHOUR" field="startWorkHour" type="time"/>
			<t:grid-item label="LBL_END_WORKHOUR" field="endWorkHour" type="time"/>
		</t:grid>
	</jsp:body>
</t:wrapper>
