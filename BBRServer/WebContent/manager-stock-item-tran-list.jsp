<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:wrapper title="LBL_STOCK_ITEMS_TRANS_TITLE">
	<jsp:body>
		<t:grid method="BBRStockItemTrans" editPage="manager-stock-item-tran-edit.jsp" createPage="manager-stock-item-tran-edit.jsp" 
				title="LBL_STOCK_ITEMS_TRANS_TITLE" standardFilters="true" customToolbar="true">
			<t:toolbar-group>
				<t:toolbar-item label="LBL_GRID_CREATE_ADD_BTN" id="add" accent="btn-default" icon="glyphicon-plus"/>
				<t:toolbar-item label="LBL_GRID_CREATE_SUBTRACT_BTN" id="subtract" accent="btn-default" icon="glyphicon-minus"/>
				<t:toolbar-item label="LBL_GRID_EDIT_BTN" id="edit" accent="btn-default" icon="glyphicon-pencil"/>
				<t:toolbar-item label="LBL_GRID_DELETE_BTN" id="delete" accent="btn-default" icon="glyphicon-trash"/>
			</t:toolbar-group>
				
			<t:grid-item label="LBL_DATE" field="date" sort="desc"/>
			<t:grid-item label="LBL_ITEM" field="item.title" sort="asc"/>
			<t:grid-item label="LBL_POS" field="pos.title"/>
			<t:grid-item label="LBL_SPECIALIST" field="specialist.name"/>
			<t:grid-item label="LBL_TYPE" field="type" type="select" options="OPT_STOCK_TRAN_TYPE"/>
			<t:grid-item label="LBL_QTY" field="qty"/>
			<t:grid-item label="LBL_PARTY" field="party.title"/>
		</t:grid>
	</jsp:body>
</t:wrapper>

<script>
		$(document).ready(function() {
			$("#add").click(function () {
				window.location.href = 'manager-stock-item-tran-add-create.jsp?id=new';
			})		

			$("#subtract").click(function () {
				window.location.href = 'manager-stock-item-tran-subtract-create.jsp?id=new';
			})		
		});
</script>