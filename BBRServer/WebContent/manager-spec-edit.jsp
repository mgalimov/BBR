<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-card-wrapper title="Edit specialist">
	<jsp:body>
		<t:card method="BBRSpecialists" gridPage="manager-spec-list.jsp" title="Edit specialist">
			<t:card-item label="Name" field="name" type="text" isRequired="required" />
			<t:card-item label="Point of service" type="reference" field="pos" referenceFieldTitle="title" referenceMethod="BBRPoSes"/>
			<t:card-item label="Position" field="position" type="text" isRequired="required" />
			<t:card-item label="Status" type="select" field="status" options="1:Active,2:Inactive"/>
		</t:card>
	</jsp:body>
</t:admin-card-wrapper>