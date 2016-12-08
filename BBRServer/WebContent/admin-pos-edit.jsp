<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:wrapper title="LBL_POS_TITLE">
	<jsp:body>
		<t:card method="BBRPoSes" gridPage="admin-pos-list.jsp" title="LBL_POS_TITLE" showTabs="true">
			<t:card-tab label="LBL_POS_TITLE" id="mainTab" combined="true" isActive="true">
				<t:card-item label="LBL_SHOP" field="shop" type="reference" referenceMethod="BBRShops" referenceFieldTitle="title"/>
				<t:card-item label="LBL_TITLE" field="title" type="text" isRequired="required"/>
				<t:card-item label="LBL_START_WORKHOUR" field="startWorkHour" type="time" isRequired="required"/>
				<t:card-item label="LBL_END_WORKHOUR" field="endWorkHour" type="time" isRequired="required"/>
				<t:card-item label="LBL_LOC_DESCRIPTION" field="locationDescription" type="text"/>
			</t:card-tab>
			<t:card-tab label="LBL_POS_INFO" id="extraTab" combined="true">
				<t:card-item label="LBL_LOC_LAT" field="locationGPS.lat" type="text"/>
				<t:card-item label="LBL_LOC_LONG" field="locationGPS.lng" type="text"/>
				<t:card-item label="LBL_CURRENCY" field="currency" type="text"/>
				<t:card-item label="LBL_TIMEZONE" field="timeZone" type="select" options="OPT_TIMEZONES"/>
				<t:card-item label="LBL_URLID" field="urlID" type="text"/>
				<t:card-item label="LBL_EMAIL_NOTIFICATION" field="email" type="text"/>
				<t:card-item label="LBL_SMS_NOTIFICATION" field="sms" type="text"/>
				<t:card-item label="LBL_PRIZE_VISIT_NUMBER" field="prizeVisitNumber" type="number"/>
			</t:card-tab>
		</t:card>
	</jsp:body>
</t:wrapper>

<script>
	$("#shopinput").on("change", function () {
		shopId = $("#shopinput").val();
		$.get("BBRShops",
				{	
					id: shopId,
					operation: "getdata"
				}
			).done(function (data) {
				shop = $.parseJSON(data);
				el = $("#timeZoneinput")[0].selectize;
				el.addItem(shop.timeZone);
			});
	});
</script>