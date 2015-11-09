<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="BBRClientApp.BBRContext"%>
<%@ page import="BBR.BBRUtil"%>
<%@ page import="BBRClientApp.BBRParams"%>
<%@ page import="BBRAcc.BBRService"%>
<%@ page import="BBRAcc.BBRServiceManager"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<%
	BBRContext context = BBRContext.getContext(request);
	BBRParams params = new BBRParams(request.getQueryString());	
	String titleMod = "";
	context.set("service", null);
	String serviceId = params.get("serviceId");
	if (serviceId != null && !serviceId.isEmpty()) {
		BBRServiceManager smgr = new BBRServiceManager();
		BBRService service = smgr.findById(Long.parseLong(serviceId));
		if (service != null) {
			context.set("service", service);
			titleMod = BBRUtil.visualTitleDelimiter + service.getTitle();
		}
	}
	
	request.setAttribute("titleMod", titleMod);
	
	if (context.get("turnsList") == null)
		request.setAttribute("turnsListBtn", "#toggleFutureTurnsBtn");
	else
		request.setAttribute("turnsListBtn", "#toggleAllTurnsBtn");
%>

<t:admin-grid-wrapper title="LBL_SERVICE_PRICE_TITLE">
	<jsp:body>
		<t:grid method="BBRServicePrices" editPage="system-serviceprice-edit.jsp" createPage="system-serviceprice-create.jsp" 
				title="LBL_SERVICE_PRICE_TITLE" standardFilters="false" titleModifier="${titleMod}">
			<t:grid-item label="LBL_SERVICE" field="service.title" />
			<t:grid-item label="LBL_COUNTRY" field="country" />
			<t:grid-item label="LBL_SPRICE_START_DATE" field="startDate" />
			<t:grid-item label="LBL_SPRICE_END_DATE" field="endDate" />
			<t:grid-item label="LBL_SPRICE_CURRENCY" field="currency" />
			<t:grid-item label="LBL_SPRICE_LIMIT" field="creditLimit" />
		</t:grid>
	</jsp:body>
</t:admin-grid-wrapper>