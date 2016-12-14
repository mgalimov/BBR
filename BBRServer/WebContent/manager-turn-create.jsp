<%@ page import="BBRClientApp.BBRContext"%>
<%@ page import="BBRCust.BBRTurnManager"%>
<%@ page import="BBRCust.BBRTurn"%>
<%@ page import="BBRCust.BBRSpecialistManager"%>
<%@ page import="BBRCust.BBRSpecialist"%>
<%@ page import="BBRClientApp.BBRParams"%>
<%@ page import="BBR.BBRUtil"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.*"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%
	BBRContext context = BBRContext.getContext(request);
	BBRParams params = new BBRParams(request.getQueryString());

	String defDate = "";
	String defStartTime = "";
	String defEndTime = "";
	String specId = (String)context.get("spec");
	String specName = "";
	
	if (specId != null && !specId.isEmpty()) {
		BBRSpecialistManager smgr = new BBRSpecialistManager();
		BBRSpecialist specialist = smgr.findById(Long.parseLong(specId));
		
		if (specialist != null) {
			specName = specialist.getName();
			BBRTurnManager tmgr = new BBRTurnManager();
			BBRTurn turn = tmgr.guessNextTurn(specialist);
			if (turn != null) {
				SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
				SimpleDateFormat tf = new SimpleDateFormat(BBRUtil.fullTimeFormat);
				defDate = df.format(turn.getDate());
				defStartTime = tf.format(turn.getStartTime());
				defEndTime = tf.format(turn.getEndTime());
			}
		}
	}

	request.setAttribute("specId", specId);
	request.setAttribute("specName", specName);
	request.setAttribute("defDate", defDate);
	request.setAttribute("defStartTime", defStartTime);
	request.setAttribute("defEndTime", defEndTime);
%>

<t:wrapper title="LBL_EDIT_TURN_TITLE">
<jsp:body>
		<t:card title="LBL_EDIT_TURN_TITLE" gridPage="manager-turn-list.jsp" method="BBRTurns" showTabs="false">
			<t:toolbar-item label="LBL_GUESS_TURN_BTN" id="guessTurnBtn" />
			<t:card-tab label="LBL_EDIT_TURN_TITLE" id="mainTab" isActive="true" combined="true">
				<t:card-item label="LBL_POS" field="pos" type="reference" 
							 referenceFieldTitle="title" referenceMethod="BBRPoSes"/>
				<t:card-item label="LBL_SPECIALIST" field="specialist" type="reference" 
							 referenceFieldTitle="name" referenceMethod="BBRSpecialists"
							 defaultValue="${specId}" defaultDisplay="${specName}"/>
				<t:card-item label="LBL_TURN_DATE" field="date" type="date" 
							 defaultValue="${defDate}"/>
				<t:card-item label="LBL_START_TIME" field="startTime" type="time" defaultValue="${defStartTime}"/>
				<t:card-item label="LBL_END_TIME" field="endTime" type="time" defaultValue="${defEndTime}"/>
			</t:card-tab>
<%-- 			<t:card-tab label="LBL_EDIT_TURN_TITLE" id="scheduleTab" isActive="false" combined="true"> --%>
<%-- 				<t:card-schedule-spec-turns posId="1"/> --%>
<%-- 			</t:card-tab> --%>
		</t:card>
</jsp:body>
</t:wrapper>

<script>
	$(document).ready(function () {
		specialistSetConstrains = function () {
			return $("#posinput").val();
		}
		
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
		
		posId = $("#posinput").val();
		
		$("#posinput")[0].selectize.on("load", function () {
			var inp = $("#posinput")[0].selectize;
			var inp2 = $("#specialistinput")[0].selectize;

			firstOptionIndex = Object.keys(inp.options)[0];
			inp.setValue(inp.options[firstOptionIndex].id);
		});
		
		firstTimeSpecLoad = true;
		
		$("#specialistinput")[0].selectize.on("load", function () {
			var inp = $("#specialistinput")[0].selectize;
			
			if (firstTimeSpecLoad) {
				firstTimeSpecLoad = false;
				inp.clear();
				inp.clearOptions();
				inp.load(specialistLoadInitialData);
			}
		});
		
		$("#posinput").on("change", posChange);
		
		function posChange() {
			newPosId = $("#posinput").val();
			if (newPosId != posId) {
				posId = newPosId;
				var inp = $("#specialistinput")[0].selectize;
				if (inp.loading == 0) {
					inp.clear();
					inp.clearOptions();
					inp.load(specialistLoadInitialData);
				}
			}
		}
		
		$("#specialistinput")[0].selectize.on("change", function () {
			$("#guessTurnBtn").click();
		});
	});
</script>