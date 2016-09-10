<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="BBR.BBRUtil"  %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<%
	SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
	request.setAttribute("now", df.format(new Date()));
%>

<t:wrapper title="LBL_SUBSCR_CREATE">
	<jsp:body>
		<t:card method="BBRSubscriptions" gridPage="admin-subscription-list.jsp" title="LBL_SUBSCR_CREATE">
			<t:card-item label="LBL_SHOP" field="shop" type="reference" referenceMethod="BBRShops" referenceFieldTitle="title"/>
			<t:card-item label="LBL_SERVICE" field="service" type="reference" referenceMethod="BBRServices" referenceFieldTitle="title"/>
			<t:card-item label="LBL_SUBSCR_START_DATE" field="startDate" type="date" defaultValue="${now}"/>
			<t:card-item label="LBL_SUBSCR_END_DATE" field="endDate" type="date" isDisabled="readonly"/>
			<t:card-item label="LBL_SUBSCR_BALANCE" field="balance" type="text" isDisabled="readonly"/>
			<t:card-item label="LBL_SUBSCR_CURRENCY" field="currency" type="text" isDisabled="readonly"/>
			<t:card-item label="LBL_SUBSCR_LIMIT" field="creditLimit" type="text" isDisabled="readonly"/>
			<t:card-item label="LBL_SUBSCR_STATUS" field="status" type="select" options="OPT_SUBSCR_STATUS" isDisabled="readonly"/>
		</t:card>
	</jsp:body>
</t:wrapper>

<script>
var serviceId; 

$(document).ready(function() {
	serviceId = $("#serviceinput").val();
	
	$("#serviceinput").on("change", function () {
		newServiceId = $("#serviceinput").val();
		if (newServiceId != serviceId) {
			serviceId = newServiceId;
			$.get("BBRServices", {id: serviceId, operation: "getdata"}, function(responseText) {
				service = $.parseJSON(responseText);
				$("#currencyinput").val(service.currency);
				$("#creditLimitinput").val(service.creditLimit);
			});
		}
	});
});

</script>