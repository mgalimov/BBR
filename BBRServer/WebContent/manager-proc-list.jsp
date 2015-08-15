<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-grid-wrapper title="LBL_PROCEDURES_TITLE">
	<jsp:body>
		<t:grid method="BBRProcedures" editPage="manager-proc-edit.jsp" createPage="manager-proc-create.jsp" title="LBL_PROCEDURES_TITLE">
			<t:grid-item label="LBL_POS" field="pos.title"/>
			<t:grid-item label="LBL_TITLE" field="title" sort="asc"/>
			<t:grid-item label="LBL_PROC_LENGTH" field="length"/>
			<t:grid-item label="LBL_PROC_PRICE" field="price"/>
			<t:grid-item label="LBL_CURRENCY" field="pos.currency"/>
			<t:grid-item label="LBL_PROC_STATUS" field="status" type="select" options="OPT_PROC_STATUS"/>
		</t:grid>
	</jsp:body>
</t:admin-grid-wrapper>
