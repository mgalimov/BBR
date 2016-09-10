<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ page import="BBRClientApp.BBRContext"%>
<%
	BBRContext context = BBRContext.getContext(request);
%>
<t:wrapper title="LBL_SUBSCR_EDIT">
	<jsp:body>
		<t:modal cancelButtonLabel="BTN_RETURN" processButtonLabel="BTN_CANCEL_SUBSCRIPTION" title="LBL_CANCEL_SUBSCRIPTION" id="modalCancel">
			${context.gs("LBL_REALLY_WANT_TO_CANCEL_SUBSCRIPTION")}
		</t:modal>
		<t:card method="BBRSubscriptions" gridPage="admin-subscription-list.jsp" title="LBL_SUBSCR_EDIT">
			<t:toolbar-item label="LBL_CANCEL_SUBSCR" id="cancelSubscrByManagerBtn" />
			<t:toolbar-item label="LBL_SEE_TRANS" id="seeSubscrTransBtn" />
			<t:card-item label="LBL_SHOP" field="shop" type="reference" referenceMethod="BBRShops" referenceFieldTitle="title" isDisabled="readonly"/>
			<t:card-item label="LBL_SERVICE" field="service" type="reference" referenceMethod="BBRServices" referenceFieldTitle="title"  isDisabled="readonly"/>
			<t:card-item label="LBL_SUBSCR_START_DATE" field="startDate" type="date" isDisabled="readonly"/>
			<t:card-item label="LBL_SUBSCR_END_DATE" field="endDate" type="date" isDisabled="readonly"/>
			<t:card-item label="LBL_SUBSCR_BALANCE" field="currency" type="text" isDisabled="readonly"/>
			<t:card-item label="LBL_SUBSCR_CURRENCY" field="currency" type="text" isDisabled="readonly"/>
			<t:card-item label="LBL_SUBSCR_LIMIT" field="creditLimit" type="text" isDisabled="readonly"/>
			<t:card-item label="LBL_SUBSCR_STATUS" field="status" type="select" options="OPT_SUBSCR_STATUS"  isDisabled="readonly"/>
		</t:card>
	</jsp:body>
</t:wrapper>

<script>
	$(document).ready(function () {
		$("#cancelSubscrByManagerBtn").click(function () {
			$('#modalCancel').modal();
		});

		$("#modalCancelprocess").click(function () {
			subscrId = getUrlParameter("id");
			if (subscrId)
				$.get(
						"BBRSubscriptions",
						{	
							subscrId: subscrId,
							operation: "cancelSubscriptionByShopAdmin"
						}
					).done(function () {
						window.location.href = window.location.href; 
					});
		});

		$("#seeSubscrTransBtn").click(function () {
			subscrId = getUrlParameter("id");
			if (subscrId)
				window.location.href = "admin-transaction-list.jsp?subscrId=" + subscrId;
		});

	});
</script>