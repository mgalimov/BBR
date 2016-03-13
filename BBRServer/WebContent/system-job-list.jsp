<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-grid-wrapper title="LBL_JOBS_TITLE">
	<jsp:body>
		<t:grid method="BBRJobs" editPage="system-job-edit.jsp" createPage="system-job-create.jsp" title="LBL_JOBS_TITLE">
			<t:grid-item label="LBL_TITLE" field="title" sort="asc"/>
		</t:grid>
	</jsp:body>
</t:admin-grid-wrapper>