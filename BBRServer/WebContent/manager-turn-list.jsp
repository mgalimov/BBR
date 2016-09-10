<%@ page import="BBRClientApp.BBRContext"%>
<%@ page import="BBR.BBRUtil"%>
<%@ page import="BBRClientApp.BBRParams"%>
<%@ page import="BBRClientApp.BBRContext"%>
<%@ page import="BBRCust.BBRSpecialistManager"%>
<%@ page import="BBRCust.BBRSpecialist"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<%
	BBRContext context = BBRContext.getContext(request);
	BBRParams params = new BBRParams(request.getQueryString());	
	String titleMod = "";
	String t = params.get("t");

	context.set("spec", null);
	if (t != null && !t.isEmpty()) {
		if (t.equals("spec")) {
			String specId = params.get("query");
			context.set("spec", specId);
			
			if (specId != null && !specId.isEmpty()) {
				BBRSpecialistManager smgr = new BBRSpecialistManager();
				BBRSpecialist spec = smgr.findById(Long.parseLong(specId));
				if (spec != null) {
					titleMod = BBRUtil.visualTitleDelimiter + spec.getName();
				}
			}
		}
	}
	
	request.setAttribute("titleMod", titleMod);
	
	if (context.get("turnsList") == null)
		request.setAttribute("turnsListBtn", "#toggleFutureTurnsBtn");
	else
		request.setAttribute("turnsListBtn", "#toggleAllTurnsBtn");
%>

<t:wrapper title="LBL_SPEC_TURNS_TITLE" titleModifier="${titleMod}">
	<jsp:body>
		<t:grid method="BBRTurns" editPage="manager-turn-edit.jsp" createPage="manager-turn-create.jsp" 
				title="LBL_SPEC_TURNS_TITLE" titleModifier="${titleMod}" customToolbar="true">
			<t:toolbar-group>
				<t:toolbar-item label="LBL_GRID_CREATE_RECORD_BTN" id="create" icon="glyphicon-plus"></t:toolbar-item>	
				<t:toolbar-item label="LBL_GRID_EDIT_RECORD_BTN" id="edit" icon="glyphicon-pencil" accent="btn-info"></t:toolbar-item>	
				<t:toolbar-item label="LBL_GRID_DELETE_RECORD_BTN" id="delete" icon="glyphicon-trash" accent="btn-warning"></t:toolbar-item>
			</t:toolbar-group>
			<t:toolbar-group>	
				<t:toolbar-item label="LBL_TOGGLE_ALL_TURNS" id="toggleAllTurnsBtn" />
				<t:toolbar-item label="LBL_TOGGLE_FUTURE_TURNS" id="toggleFutureTurnsBtn" />
			</t:toolbar-group>
			<t:grid-item label="LBL_SPECIALIST" field="specialist.name" sort="asc"/>
			<t:grid-item label="LBL_DATE" field="date" sort="desc"/>
			<t:grid-item label="LBL_START_TIME" field="startTime" type="time" />
			<t:grid-item label="LBL_END_TIME" field="endTime" type="time" />
		</t:grid>
	</jsp:body>
</t:wrapper>

<script>
$(document).ready(function (){
	$("${turnsListBtn}").addClass("active");
	
	$("#toggleAllTurnsBtn").click(function () {
		clickHandler("toggleAllTurns", "#toggleAllTurnsBtn");
	})

	$("#toggleFutureTurnsBtn").click(function () {
		clickHandler("toggleFutureTurns", "#toggleFutureTurnsBtn");
	})
});  

function clickHandler(operation, btn) {
	$.ajax({
    	url: 'BBRTurns',
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

</script>