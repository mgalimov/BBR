<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:wrapper title="LBL_POSES_TITLE">
	<jsp:body>
		<t:grid method="BBRPoSes" editPage="admin-pos-edit.jsp" createPage="admin-pos-create.jsp" 
		 		title="LBL_POSES_TITLE" standardFiltersShopsOnly="true">
			<t:grid-item label="LBL_TITLE" field="title" sort="asc"/>
			<t:grid-item label="LBL_CITY" field="city" type="text"/>
			<t:grid-item label="LBL_SHOP" field="shop.title"/>
			<t:grid-item label="LBL_START_WORKHOUR" field="startWorkHour" type="time"/>
			<t:grid-item label="LBL_END_WORKHOUR" field="endWorkHour" type="time"/>
			<t:grid-item label="LBL_CURRENCY" field="currency" type="text"/>
			<t:grid-item label="LBL_TIMEZONE" field="timeZone" type="text"/>
			<t:grid-item label="LBL_URLID" field="urlID" type="text"/>
			<t:grid-item label="LBL_POS_STATUS" field="status" type="select" options="OPT_POS_STATUS"/>
		</t:grid>
	</jsp:body>
</t:wrapper>
