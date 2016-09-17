<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.*"%>
<%@ page import="BBRClientApp.BBRContext"%>
<%@ page import="BBR.BBRUtil"%>
<%@ page import="BBRAcc.BBRPoSManager"%>
<%@ page import="BBRAcc.BBRPoS"%>
<%@ page import="BBRAcc.BBRUser.*"%>
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
			context.set("visitsUserNC", userNC);
			context.set("visitsPosId", null);
			context.set("visitsDatePos", null);
			
			titleMod = BBRUtil.visualTitleDelimiter + userNC[0];
			if (userNC.length == 2)
				titleMod += ", " + userNC[1];
		}
		
		if (t.equals("datepos")) {
			String[] datePos = params.get("query").split(BBRUtil.recordDivider);
			context.set("visitsDatePos", datePos);
			context.set("visitsUserNC", null);
			context.set("visitsPosId", null);
			
			if (datePos.length > 0) {
				SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
				context.filterStartDate = df.parse(datePos[0]);
				context.filterEndDate = context.filterStartDate;
				titleMod = BBRUtil.visualTitleDelimiter + df.format(context.filterStartDate);
				if (datePos.length == 2 && datePos[1] != null) {
					BBRPoSManager pmgr = new BBRPoSManager();
					BBRPoS pos = pmgr.findById(Long.parseLong(datePos[1]));
					context.filterPoS = pos;
					titleMod += BBRUtil.visualTitleDelimiter + pos.getTitle();
				}
			}
		}
		
		if (t.equals("unapproved")) {
			context.set("visitsPosId", params.get("query"));
			context.set("visitsUserNC", null);
			context.set("visitsDatePos", null);
			titleMod += BBRUtil.visualTitleDelimiter + context.gs("LBL_UNAPPROVED_TITLE_MOD");
		}
		
		if (t.equals("all")) {
			context.set("visitsDatePos", null);
			context.set("visitsUserNC", null);
			context.set("visitsPosId", null);
		}
	} else {
		context.set("visitsUserNC", null);
		context.set("visitsPosId", null);
		context.set("visitsDatePos", null);
	}

	request.setAttribute("titleMod", titleMod);
%>

<t:wrapper title="LBL_USER_VISITS_TITLE" titleModifier="${titleMod}">
	<jsp:body>
		<t:grid method="BBRVisits" editPage="manager-visit-edit.jsp" createPage="manager-visit-edit.jsp" title="LBL_USER_VISITS_TITLE" titleModifier="${titleMod}">
			<t:grid-item label="LBL_DATE_TIME" field="timeScheduled" />
			<t:grid-item label="LBL_REAL_TIME" field="realTime" />
			<t:grid-item label="LBL_POS" field="pos.title"/>
			<t:grid-item label="LBL_USER_NAME" field="userName"/>
			<t:grid-item label="LBL_CONTACT_INFO" field="userContacts"/>
			<t:grid-item label="LBL_SPEC" field="spec.name"/>
			<t:grid-item label="LBL_VISIT_STATUS" field="status" type="select" options="OPT_VISIT_STATUS"/>
			<t:grid-item label="LBL_LENGTH" field="length"/>
			<t:grid-item label="LBL_FINAL_PRICE" field="finalPrice"/>
			<t:grid-item label="LBL_DISCOUNT_PERCENT" field="discountPercent"/>
			<t:grid-item label="LBL_DISCOUNT_AMOUNT" field="discountAmount"/>
			<t:grid-item label="LBL_PRICE_PAID" field="pricePaid"/>
			<t:grid-item label="LBL_AMOUNT_TO_SPECIALIST" field="amountToSpecialist"/>
			<t:grid-item label="LBL_AMOUNT_TO_MATERIALS" field="amountToMaterials"/>
			<t:grid-item label="LBL_BOOKING_CODE" field="bookingCode"/>
		</t:grid>
	</jsp:body>
</t:wrapper>
