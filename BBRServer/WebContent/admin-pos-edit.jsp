<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-card-wrapper title="LBL_POS_TITLE">
	<jsp:body>
		<t:card method="BBRPoSes" gridPage="admin-pos-list.jsp" title="LBL_POS_TITLE">
			<t:card-item label="LBL_SHOP" field="shop" type="reference" referenceMethod="BBRShops" referenceFieldTitle="title"/>
			<t:card-item label="LBL_TITLE" field="title" type="text" isRequired="required"/>
			<t:card-item label="LBL_START_WORKHOUR" field="startWorkHour" type="time" isRequired="required"/>
			<t:card-item label="LBL_END_WORKHOUR" field="endWorkHour" type="time" isRequired="required"/>
			<t:card-item label="LBL_LOC_DESCRIPTION" field="locationDescription" type="text"/>
			<t:card-item label="LBL_LOC_LAT" field="locationGPS.lat" type="text"/>
			<t:card-item label="LBL_LOC_LONG" field="locationGPS.lng" type="text"/>
			<t:card-item label="LBL_CURRENCY" field="currency" type="text"/>
			<t:card-item label="LBL_TIMEZONE" field="timeZone" type="select" options="OPT_TIMEZONES"/>
		</t:card>
	</jsp:body>
</t:admin-card-wrapper>