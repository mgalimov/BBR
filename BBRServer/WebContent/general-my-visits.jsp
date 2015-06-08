<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-grid-wrapper title="LBL_MY_VISITS_TITLE">
	<jsp:body>
		<t:grid method="BBRVisits" editPage="general-edit-visit.jsp" createPage="general-plan-visit.jsp" title="LBL_MY_VISITS_TITLE">
			<t:grid-item label="LBL_DATE_TIME" field="timeScheduled" />
			<t:grid-item label="LBL_POS" field="pos.title"/>
			<t:grid-item label="LBL_USER_NAME" field="userName"/>
			<t:grid-item label="LBL_CONTACT_INFO" field="userContacts"/>
			<t:grid-item label="LBL_SPEC" field="spec.title"/>
			<t:grid-item label="LBL_VISIT_STATUS" field="status" type="select" options="OPT_VISIT_STATUS"/>
			<t:grid-item label="LBL_VISIT_LENGTH" field="length"/>
			<t:grid-item label="LBL_POS_START_WORKHOUR" field="pos.startWorkHour" type="time"/>
			<t:grid-item label="LBL_POS_END_WORKHOUR" field="pos.endWorkHour" type="time"/>
		</t:grid>
	</jsp:body>
</t:admin-grid-wrapper>
