<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="BBRClientApp.BBRContext"%>
<%@ page import="BBR.BBRUtil"%>
<%@ page import="BBRClientApp.BBRParams"%>
<%@ page import="BBRAcc.BBRServiceSubscription"%>
<%@ page import="BBRAcc.BBRServiceSubscriptionManager"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<%
	BBRContext context = BBRContext.getContext(request);
	BBRParams params = new BBRParams(request.getQueryString());	
	String titleMod = "";
	context.set("subscription", null);
	String subscrId = params.get("subscrId");
	if (subscrId != null && !subscrId.isEmpty()) {
		BBRServiceSubscriptionManager smgr = new BBRServiceSubscriptionManager();
		BBRServiceSubscription subscription = smgr.findById(Long.parseLong(subscrId));
		if (subscription != null) {
			context.set("subscription", subscription);
			SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
			titleMod = BBRUtil.visualTitleDelimiter + subscription.getService().getTitle() + 
					   BBRUtil.visualTitleDelimiter + subscription.getShop().getTitle() + 
					   BBRUtil.visualTitleDelimiter + df.format(subscription.getStartDate());
		}
	}
	
	request.setAttribute("titleMod", titleMod);
%>

<t:admin-grid-wrapper title="LBL_TRAN_TITLE">
	<jsp:body>
		<t:grid method="BBRTransactions" editPage="" createPage="" 
				title="LBL_TRAN_TITLE" standardFilters="true" titleModifier="${titleMod}"
				customToolbar="true">
			<t:grid-item label="LBL_TRAN_ID" field="id" />
			<t:grid-item label="LBL_TRAN_DATE" field="date"/>
			<t:grid-item label="LBL_TRAN_SERVICE" field="service.title" />
			<t:grid-item label="LBL_TRAN_AMOUNT" field="amount" />
			<t:grid-item label="LBL_TRAN_CURRENCY" field="currency" />
		</t:grid>
	</jsp:body>
</t:admin-grid-wrapper>