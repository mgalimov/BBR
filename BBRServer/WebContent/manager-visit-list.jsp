<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="BBRClientApp.BBRContext"%>
<%@ page import="BBR.BBRUtil"%>
<%@ page import="BBRAcc.BBRPoSManager"%>
<%@ page import="BBRAcc.BBRPoS"%>
<%@ page import="BBRClientApp.BBRParams"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<%
	BBRContext context = BBRContext.getContext(request);
	BBRParams params = new BBRParams(request.getQueryString());
	
	String titleMod = "";
	
	String t = params.get("t");
	if (t != null && !t.isEmpty()) {
		if (t.equals("user")) {
			String[] userNC = params.get("query").split(BBRUtil.recordDivider);
			context.set("userNC", userNC);
			context.set("pos", null);
			context.set("datePos", null);
			
			titleMod = BBRUtil.visualTitleDelimiter + userNC[0];
			if (userNC.length == 2)
				titleMod += ", " + userNC[1];
		}
		
		if (t.equals("datepos")) {
			String[] datePos = params.get("query").split(BBRUtil.recordDivider);
			context.set("datePos", datePos);
			context.set("userNC", null);
			context.set("pos", null);
			
			if (datePos.length > 0) {
				SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
				titleMod = BBRUtil.visualTitleDelimiter + df.format(df.parse(datePos[0]));			
				if (datePos.length == 2 && datePos[1] != null) {
					BBRPoSManager pmgr = new BBRPoSManager();
					BBRPoS pos = pmgr.findById(Long.parseLong(datePos[1]));
					titleMod += BBRUtil.visualTitleDelimiter + pos.getTitle();
				}
			}
		}
		
		if (t.equals("unapproved")) {
			context.set("pos", params.get("query"));
			context.set("userNC", null);
			context.set("datePos", null);
			titleMod += BBRUtil.visualTitleDelimiter + context.gs("LBL_UNAPPROVED_TITLE_MOD");
		}
	}
	

	request.setAttribute("titleMod", titleMod);

%>

<t:admin-grid-wrapper title="LBL_USER_VISITS_TITLE" titleModifier="${titleMod}">
	<jsp:body>
		<t:grid method="BBRVisits" editPage="manager-visit-edit.jsp" createPage="" title="LBL_USER_VISITS_TITLE" titleModifier="${titleMod}">
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
