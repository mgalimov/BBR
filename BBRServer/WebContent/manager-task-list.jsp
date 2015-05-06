<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-grid-wrapper title="Tasks">
	<jsp:body>
		<t:grid method="BBRTasks" editPage="manager-task-edit.jsp" createPage="manager-task-create.jsp" title="Tasks">
			<t:grid-item label="Title" field="title" sort="asc"/>
			<t:grid-item label="Point of Service" field="pos.title"/>
			<t:grid-item label="Performer" field="performer"/>
			<t:grid-item label="Text" field="text"/>
			<t:grid-item label="Deadline" field="deadline"/>
			<t:grid-item label="State" field="state"/>
		</t:grid>
	</jsp:body>
</t:admin-grid-wrapper>