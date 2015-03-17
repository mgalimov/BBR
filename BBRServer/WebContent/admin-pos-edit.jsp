<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-card-wrapper title="Point of Sale">
	<jsp:body>
		<t:card method="BBRPoSes" gridPage="admin-pos-list.jsp" title="Point of Sale">
			<t:card-item label="Shop" field="shop" type="reference" referenceMethod="BBRShops" referenceFieldTitle="title"/>
			<t:card-item label="Title" field="title" type="text" isRequired="required"/>
			<t:card-item label="Start Work Hour" field="startWorkHour" type="time" isRequired="required"/>
			<t:card-item label="End Work Hour" field="endWorkHour" type="time" isRequired="required"/>
			<t:card-item label="Location Description" field="locationDescription" type="text"/>
			<t:card-item label="Location lattitude" field="locationGPS.lat" type="text"/>
			<t:card-item label="Location longitude" field="locationGPS.lng" type="text"/>
		</t:card>
	</jsp:body>
</t:admin-card-wrapper>