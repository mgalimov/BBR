<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<t:wrapper title="LBL_PROMO_TITLE">
<jsp:body>
		<t:card title="LBL_PROMO_TITLE" gridPage="manager-promo-list.jsp" method="BBRPromos" showTabs="true">
			<t:card-tab label="LBL_PROMO_MAIN_TAB" id="mainTab" isActive="true">
				<t:card-item label="LBL_TITLE" type="text" field="title" isRequired="required" />
				<t:card-item label="LBL_SHOP" type="reference" field="shop" isRequired="required" referenceFieldTitle="title" referenceMethod="BBRShops" />
				<t:card-item label="LBL_POSES" type="reference" field="poses" isRequired="required" referenceFieldTitle="title" referenceMethod="BBRPoSes" multiple="true" />
				<t:card-item label="LBL_PROMO_TYPE" type="select" field="promoType" options="OPT_PROMO_TYPE"/>
				<t:card-item label="LBL_PROMO_START_DATE" field="startDate" type="date" isRequired="required"/>
				<t:card-item label="LBL_PROMO_END_DATE" field="endDate" type="date"/>
				<t:card-item label="LBL_PROMO_STATUS" type="select" field="status" options="OPT_PROMO_STATUS"/>
			</t:card-tab>
			<t:card-tab label="LBL_PROMO_PARAM_TAB" id="paramTab">
				<t:card-item label="LBL_PROCEDURE_COMBO" type="reference" field="procedures" referenceFieldTitle="title" referenceMethod="BBRProcedures" multiple="true" />
				<t:card-item label="LBL_SOURCES" type="select" field="sources" options="OPT_VISIT_SOURCE" multiple="true" />
				<t:card-item label="LBL_DISCOUNT_PERCENT" field="discount" type="text"/>
				<t:card-item label="LBL_VISITS_NUMBER" field="visitsNumber" type="text"/>
			</t:card-tab>
		</t:card>
</jsp:body>
</t:wrapper>

<script>
	$(document).ready(function () {
		$("#posinput").on("change", function() {
			$.ajax({
				url: 'BBRPoSes',
	        	data: {
	        		operation: 'getdata',
	        		id: $(this).val()
	        	}
			}).success(function (d) {
				p = $.parseJSON(d);
				$("#pos_currencyinput").val(p.currency);
			}).error(function () {
				$("#pos_currencyinput").val("---");
			});
		})
	});
</script>