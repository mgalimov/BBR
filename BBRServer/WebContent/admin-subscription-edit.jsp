<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-card-wrapper title="LBL_SUBSCR_EDIT">
	<jsp:body>
		<t:card method="BBRSubscriptions" gridPage="admin-subscription-list.jsp" title="LBL_SUBSCR_EDIT">
			<t:card-item label="LBL_SHOP" field="shop" type="reference" referenceMethod="BBRShops" referenceFieldTitle="title" isDisabled="readonly"/>
			<t:card-item label="LBL_SERVICE" field="service" type="reference" referenceMethod="BBRServices" referenceFieldTitle="title"  isDisabled="readonly"/>
			<t:card-item label="LBL_SUBSCR_START_DATE" field="startDate" type="date" isDisabled="readonly"/>
			<t:card-item label="LBL_SUBSCR_END_DATE" field="endDate" type="date" isDisabled="readonly"/>
			<t:card-item label="LBL_SUBSCR_BALANCE" field="currency" type="text" isDisabled="readonly"/>
			<t:card-item label="LBL_SUBSCR_CURRENCY" field="currency" type="text" isDisabled="readonly"/>
			<t:card-item label="LBL_SUBSCR_LIMIT" field="creditLimit" type="text" />
			<t:card-item label="LBL_SUBSCR_STATUS" field="status" type="select" options="OPT_SUBSCR_STATUS"/>
		</t:card>
	</jsp:body>
</t:admin-card-wrapper>