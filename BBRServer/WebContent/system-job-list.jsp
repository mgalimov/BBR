<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:wrapper title="LBL_JOBS_TITLE">
	<jsp:body>
		<t:grid method="BBRJobs" editPage="system-job-edit.jsp" createPage="system-job-create.jsp" 
				title="LBL_JOBS_TITLE" standardFilters="false">
			<t:grid-item label="LBL_TITLE" field="title" />
			<t:grid-item label="LBL_LAST_RUN" field="lastRun" sort="desc"/>
			<t:grid-item label="LBL_NEXT_RUN" field="nextRun"/>
			<t:grid-item label="LBL_RUN_CONDITIONS" field="runConditions"/>
			<t:grid-item label="LBL_RUN_METHOD" field="runMethod"/>
			<t:grid-item label="LBL_LAST_RUN_STATUS" field="lastRunStatus"/>
		</t:grid>
	</jsp:body>
</t:wrapper>