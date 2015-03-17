<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-grid-wrapper title="Points of sale (service)">
	<jsp:body>
		<t:grid method="BBRPoSes" editPage="admin-pos-edit.jsp" createPage="admin-pos-create.jsp" title="Point of Sales">
			<t:grid-item label="Title" field="title" sort="ascending"/>
			<t:grid-item label="Location Description" field="locationDescription" sort="ascending"/>
			<t:grid-item label="Shop" field="shop.title"/>
			<t:card-item label="Start Work Hour" field="locationGPS.lat" type="time"/>
			<t:card-item label="End Work Hour" field="locationGPS.lng" type="time"/>
		</t:grid>
	</jsp:body>
</t:admin-grid-wrapper>
