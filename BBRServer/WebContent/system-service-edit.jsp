<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:wrapper title="LBL_SERVICE_EDIT">
	<jsp:body>
		<t:card method="BBRServices" gridPage="system-service-list.jsp" title="LBL_SERVICE_EDIT">
			<t:toolbar-item label="LBL_OPEN_PRICES_BTN" id="openPricesBtn" condition="obj.status==1"/>
			<t:card-item label="LBL_TITLE" field="title" type="text" isRequired="required"/>
			<t:card-item label="LBL_SERVICE_STATUS" field="status" type="select" options="OPT_SERVICE_STATUS"/>
			<t:card-item label="LBL_SERVICE_DEMO" field="demo" type="boolean"/>
			<t:card-item label="LBL_SERVICE_BASIC" field="basic" type="boolean" />
			<t:card-item label="LBL_SERVICE_PRICE" field="currentPrice" type="text" isDisabled="readonly" />
		</t:card>
	</jsp:body>
</t:wrapper>

<script>
	$(document).ready(function() {
		$("#openPricesBtn").click(function () {
			serviceId = getUrlParameter("id");
			if (serviceId)
				window.location.href = "system-serviceprice-list.jsp?serviceId=" + serviceId;
		});
	});
</script>