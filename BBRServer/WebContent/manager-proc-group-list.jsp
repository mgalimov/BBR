<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:wrapper title="LBL_PROC_GROUPS_TITLE">
	<jsp:body>
		<t:grid method="BBRProcedureGroups" editPage="manager-proc-group-edit.jsp" createPage="manager-proc-group-edit.jsp" 
				title="LBL_PROC_GROUPS_TITLE" paging="false">
			<t:grid-item label="LBL_POS" field="pos.title"/>
			<t:grid-item label="LBL_TITLE" field="title" sort="asc"/>
			<t:grid-item label="LBL_DESCRIPTION" field="description"/>
		</t:grid>
	</jsp:body>
</t:wrapper>
