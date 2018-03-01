<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:wrapper title="LBL_EDIT_STOCK_ITEM_TRANS_TITLE">
	<jsp:body>
		<t:card method="BBRStockItemTrans" gridPage="manager-stock-item-tran-list.jsp" title="LBL_EDIT_STOCK_ITEM_TRANS_TITLE">
			<t:card-item label="LBL_DATE" field="date" type="datetime" isRequired="true" defaultValue="now" />
			<t:card-item label="LBL_ITEM" type="reference" field="item" referenceFieldTitle="title" referenceMethod="BBRStockItems" isRequired="true"/>
			<t:card-item label="LBL_POS" type="reference" field="pos" referenceFieldTitle="title" referenceMethod="BBRPoSes" isRequired="true"/>
			<t:card-item label="LBL_SPECIALIST" type="reference" field="specialist" referenceFieldTitle="name" referenceMethod="BBRSpecialists" />
			<t:card-item label="LBL_TYPE" field="type" type="select" options="OPT_STOCK_TRAN_TYPE" isRequired="true" defaultValue="S"/>
			<t:card-item label="LBL_QTY" field="qty" type="number" isRequired="true"/>
			<t:card-item label="LBL_PARTY" field="party" type="reference" referenceFieldTitle="title" referenceMethod="BBRStockItemParties" isRequired="true"/>
		</t:card>
	</jsp:body>
</t:wrapper>


<script>
	var posId; 

	$(document).ready(function() {
		posId = $("#posinput").val();
		
		$("#posinput").on("change", function () {
			newPosId = $("#posinput").val();
			if (newPosId != posId) {
				posId = newPosId;
				inp = $("#specialistinput")[0].selectize;
				inp.clear();
				inp.clearOptions();
				inp.load(specialistLoadInitialData);
			}
		});
	});
	
	specialistSetConstrains = function () {
		return $("#posinput").val();
	}
</script>