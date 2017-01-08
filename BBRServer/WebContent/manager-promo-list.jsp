<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:wrapper title="LBL_PROMOS_TITLE">
	<jsp:body>
		<t:grid method="BBRPromos" editPage="manager-promo-edit.jsp" createPage="manager-promo-edit.jsp" 
				title="LBL_PROMOS_TITLE" customToolbar="true">
			<t:toolbar-group>
				<t:toolbar-item label="LBL_GRID_CREATE_RECORD_BTN" id="create" icon="glyphicon-plus"></t:toolbar-item>	
				<t:toolbar-item label="LBL_GRID_EDIT_RECORD_BTN" id="edit" icon="glyphicon-pencil" accent="btn-info"></t:toolbar-item>	
				<t:toolbar-item label="LBL_GRID_DELETE_RECORD_BTN" id="delete" icon="glyphicon-trash" accent="btn-warning"></t:toolbar-item>
			</t:toolbar-group>
			<t:toolbar-group>
				<t:toolbar-item label="BTN_ACTIVE_PROMOS" id="viewActive" icon="glyphicon-flash"/>
				<t:toolbar-item label="BTN_ALL_PROMOS" id="viewAll" icon="glyphicon-star"/>
			</t:toolbar-group>
			<t:grid-item label="LBL_TITLE" field="title" sort="asc"/>
			<t:grid-item label="LBL_SHOP" field="shop.title"/>
			<t:grid-item label="LBL_POSES" field="poses" type="multiple" referenceTitleField="title"/>
			<t:grid-item label="LBL_PROMO_TYPE" field="promoType" type="select" options="OPT_PROMO_TYPE"/>
			<t:grid-item label="LBL_PROMO_START_DATE" field="startDate"/>
			<t:grid-item label="LBL_PROMO_END_DATE" field="endDate"/>
			<t:grid-item label="LBL_PROMO_STATUS" field="status" type="select" options="OPT_PROMO_STATUS"/>
		</t:grid>
	</jsp:body>
</t:wrapper>

<script>
	$(document).ready(function () {
		$("#viewActive").addClass("active");
		
		$("#viewActive").click(function () {
			clickHandler("toggleActivePromos", "#viewActive");
		})

		$("#viewAll").click(function () {
			clickHandler("toggleAllPromos", "#viewAll");
		})
		
		function clickHandler(operation, btn) {
			$.ajax({
		    	url: 'BBRPromos',
		    	data: {
		    		operation: operation
		    	}
		    }).success(function (d) {
		    	table = $("#grid").DataTable();
		    	table.ajax.reload();
		    	table.draw();
		    	$(".active").removeClass("active");
		    	$(btn).addClass("active");			
		    });			
}
	})
</script>