<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-card-wrapper title="LBL_JOB_TITLE">
	<jsp:body>
		<t:card method="BBRJobs" gridPage="system-job-list.jsp" title="LBL_JOB_TITLE">
			<t:card-item label="LBL_TITLE" field="title" type="text" isRequired="required"/>
			<t:card-item label="LBL_NEXT_RUN" field="nextRun" type="datetime" timeStepping="1"/>
			<t:card-item label="LBL_RUN_CONDITIONS" field="runConditions" type="text"/>
			<t:card-item label="LBL_RUN_METHOD" field="runMethod" type="text" isRequired="required"/>
		</t:card>
	</jsp:body>
</t:admin-card-wrapper>