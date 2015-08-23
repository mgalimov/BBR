<%@page import="BBRClientApp.BBRContext"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:admin-card-wrapper title="LBL_EDIT_TURN_TITLE">
<jsp:body>
		<t:card title="LBL_EDIT_TURN_TITLE" gridPage="manager-turn-list.jsp" method="BBRTurns">
			<t:toolbar-item label="LBL_GUESS_TURN_BTN" id="guessTurnBtn" />
			<t:card-item label="LBL_SPECIALIST" field="specialist" type="reference" isDisabled="readonly" referenceFieldTitle="name" referenceMethod="BBRSpecialists" />
			<t:card-item label="LBL_TURN_DATE" field="date" type="date"/>
			<t:card-item label="LBL_START_TIME" field="startTime" type="time"/>
			<t:card-item label="LBL_END_TIME" field="endTime" type="time"/>
		</t:card>
</jsp:body>
</t:admin-card-wrapper>

<script>
	$(document).ready(function () {
		$("#guessTurnBtn").click(function () {
			specId = $("#specialistinput").val();
			$.get(
				"BBRTurns",
				{	
					specialist: specId,
					operation: "guessturn"
				}
			).done(function (data) {
				s = $.parseJSON(data);
				$("#dateinput").val(s[0]);
				$("#startTimeinput").val(s[1]);
				$("#endTimeinput").val(s[2]);
			});
		});
	});
</script>