<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-grid-wrapper title="My visits">
	<jsp:body>
		<t:grid method="BBRVisits" editPage="general-edit-visit.jsp" createPage="general-plan-visit.jsp" title="Visits">
			<t:grid-item label="Date and Time" field="timeScheduled" />
			<t:grid-item label="Point of sale (service)" field="pos.title"/>
			<t:grid-item label="Name" field="userName"/>
			<t:grid-item label="Contacts" field="userContacts"/>
			<t:grid-item label="Specialist" field="spec.title"/>
			<t:grid-item label="Status" field="status"/>
			<t:grid-item label="Length, hours" field="length"/>
			<t:grid-item label="Pos start time" field="pos.startWorkHour" type="time"/>
			<t:grid-item label="Pos end time" field="pos.endWorkHour" type="time"/>
		</t:grid>
	</jsp:body>
</t:admin-grid-wrapper>
