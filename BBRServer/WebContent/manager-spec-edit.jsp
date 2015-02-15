<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-card-wrapper title="Edit user">
	<jsp:body>
		<t:card method="BBRSpecialists" gridPage="manager-spec-list.jsp" title="Edit specialist">
			<t:card-item label="Name" field="name" type="text" isRequired="required" />
			<t:card-item label="Position" field="position" type="text" isRequired="required" />
		</t:card>
	</jsp:body>
</t:admin-card-wrapper>