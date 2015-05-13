<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-grid-wrapper title="Specialists">
	<jsp:body>
		<t:grid method="BBRSpecialists" editPage="manager-spec-edit.jsp" createPage="manager-spec-create.jsp" title="Specialists">
			<t:grid-item label="Name" field="name" sort="asc"/>
			<t:grid-item label="Point of service" field="pos.title"/>
			<t:grid-item label="Position" field="position"/>
			<t:grid-item label="Status" field="status" type="select" options="0:Unassigned,1:Active,2:Inactive"/>
		</t:grid>
	</jsp:body>
</t:admin-grid-wrapper>