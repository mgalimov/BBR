<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:wrapper title="LBL_PROCEDURES_TITLE">
	<jsp:body>
		<t:grid method="BBRProcedures" editPage="manager-proc-edit.jsp" createPage="manager-proc-edit.jsp" 
				title="LBL_PROCEDURES_TITLE" paging="false">
			<t:grid-item label="LBL_TITLE" field="title" sort="asc"/>
			<t:grid-item label="LBL_POS" field="procedureGroup.pos.title"/>
			<t:grid-item label="LBL_PROC_GROUP" field="procedureGroup.title"/>
			<t:grid-item label="LBL_PROC_LENGTH" field="length"/>
			<t:grid-item label="LBL_PROC_PRICE" field="price"/>
			<t:grid-item label="LBL_CURRENCY" field="procedureGroup.pos.currency"/>
			<t:grid-item label="LBL_PROC_STATUS" field="status" type="select" options="OPT_PROC_STATUS"/>
		</t:grid>
	</jsp:body>
</t:wrapper>
