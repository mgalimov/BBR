<%@page import="BBRAcc.BBRService.BBRServiceStatus"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="BBRAcc.BBRService" %>
<%@ page import="BBRClientApp.BBRContext" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%
	String sId = "";
	String sTitle = "";
	BBRContext context = BBRContext.getContext(request); 
	BBRService service = (BBRService)context.get("service");
	
	if (service != null && service.getStatus() == BBRServiceStatus.SERVICE_ACTIVE) {
		sId = service.getId().toString();
		sTitle = service.getTitle();
	}
	
	request.setAttribute("serviceId", sId);
	request.setAttribute("serviceTitle", sTitle);
%>
<t:admin-card-wrapper title="LBL_SERVICE_PRICE_CREATE">
	<jsp:body>
		<t:card method="BBRServicePrices" gridPage="system-serviceprice-list.jsp" title="LBL_SERVICE_PRICE_CREATE">
			<t:card-item label="LBL_SERVICE" field="service" type="reference" 
						  referenceFieldTitle="title" referenceMethod="BBRServices" isRequired="required"
						  defaultValue="${serviceId}"
						  defaultDisplay="${serviceTitle}" />
			<t:card-item label="LBL_COUNTRY" field="country" type="text" />
			<t:card-item label="LBL_SPRICE_START_DATE" field="startDate" type="date" isRequired="required"/>
			<t:card-item label="LBL_SPRICE_END_DATE" field="endDate" type="date" />
			<t:card-item label="LBL_SPRICE_PRICE" field="price" type="money" currencyField="currency" isRequired="required"/>
			<t:card-item label="LBL_SPRICE_LIMIT" field="creditLimit" type="number" />
		</t:card>
	</jsp:body>
</t:admin-card-wrapper>