<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:admin-card-wrapper title="LBL_SERVICE_PRICE_EDIT">
	<jsp:body>
		<t:card method="BBRServicePrices" gridPage="system-serviceprice-list.jsp" title="LBL_SERVICE_PRICE_EDIT">
			<t:card-item label="LBL_SERVICE" field="service" type="reference" 
						  referenceFieldTitle="title" referenceMethod="BBRServices" isDisabled="readonly"/>
			<t:card-item label="LBL_COUNTRY" field="country" type="text" isDisabled="readonly" />
			<t:card-item label="LBL_SPRICE_START_DATE" field="startDate" type="date" isDisabled="readonly"/>
			<t:card-item label="LBL_SPRICE_END_DATE" field="endDate" type="date" />
			<t:card-item label="LBL_SPRICE_PRICE" field="price" type="money" currencyField="currency"/>
			<t:card-item label="LBL_SPRICE_LIMIT" field="creditLimit" type="text" isDisabled="readonly"/>
		</t:card>
	</jsp:body>
</t:admin-card-wrapper>