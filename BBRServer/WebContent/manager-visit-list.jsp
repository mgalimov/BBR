<%@ page import="BBRClientApp.BBRContext"%>
<%@ page import="BBR.BBRUtil"%>
<%@ page import="BBRClientApp.BBRParams"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<%
	BBRContext context = BBRContext.getContext(request);
	BBRParams params = new BBRParams(request.getQueryString());	
	String t = params.get("t");
	if (t != null && !t.isEmpty()) {
		if (t.equals("user")) {
			String[] userNC = params.get("query").split(BBRUtil.recordDivider);
			context.set("userNC", userNC);
		}
		
		if (t.equals("datepos")) {
			String[] datePos = params.get("query").split(BBRUtil.recordDivider);
			context.set("datePos", datePos);
		}
	}
%>

<t:admin-grid-wrapper title="LBL_USER_VISITS_TITLE">
	<jsp:body>
		<t:grid method="BBRVisits" editPage="manager-visit-edit.jsp" createPage="" title="LBL_USER_VISITS_TITLE">
			<t:grid-item label="LBL_DATE_TIME" field="timeScheduled" />
			<t:grid-item label="LBL_POS" field="pos.title"/>
			<t:grid-item label="LBL_USER_NAME" field="userName"/>
			<t:grid-item label="LBL_CONTACT_INFO" field="userContacts"/>
			<t:grid-item label="LBL_SPEC" field="spec.title"/>
			<t:grid-item label="LBL_VISIT_STATUS" field="status" type="select" options="OPT_VISIT_STATUS"/>
			<t:grid-item label="LBL_FINAL_PRICE" field="finalPrice"/>
		</t:grid>
	</jsp:body>
</t:admin-grid-wrapper>