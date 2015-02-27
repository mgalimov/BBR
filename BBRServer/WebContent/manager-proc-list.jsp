<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-grid-wrapper title="Procedures">
	<jsp:body>
		<t:grid method="BBRProcedures" editPage="manager-proc-edit.jsp" createPage="manager-proc-create.jsp" title="Procedures">
			<t:grid-item label="Point of sale (service)" field="posTitle" sort="ascending"/>
			<t:grid-item label="Title" field="title" sort="ascending"/>
			<t:grid-item label="length, hrs" field="length"/>
			<t:grid-item label="Price" field="price"/>
			<t:grid-item label="Currency" field="currency"/>
		</t:grid>
	</jsp:body>
</t:admin-grid-wrapper>