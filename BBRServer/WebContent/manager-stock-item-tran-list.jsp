<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:wrapper title="LBL_STOCK_ITEMS_TRANS_TITLE">
	<jsp:body>
		<t:grid method="BBRStockItemTrans" editPage="manager-stock-item-tran-edit.jsp" createPage="manager-stock-item-tran-edit.jsp" 
				title="LBL_STOCK_ITEMS_TRANS_TITLE" standardFilters="true">
			<t:grid-item label="LBL_DATE" field="date" sort="desc"/>
			<t:grid-item label="LBL_ITEM" field="item.title" sort="asc"/>
			<t:grid-item label="LBL_POS" field="pos.title"/>
			<t:grid-item label="LBL_SPECIALIST" field="specialist.name"/>
			<t:grid-item label="LBL_TYPE" field="type" type="select" options="OPT_STOCK_TRAN_TYPE"/>
			<t:grid-item label="LBL_QTY" field="qty"/>
		</t:grid>
	</jsp:body>
</t:wrapper>