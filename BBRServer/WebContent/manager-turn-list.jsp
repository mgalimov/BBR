<%@page import="BBR.BBRDataSet"%>
<%@page import="BBRAcc.BBRPoSManager"%>
<%@page import="BBRAcc.BBRPoS"%>
<%@page import="BBRAcc.BBRUser.BBRUserRole"%>
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
	String posId = params.get("posId");

	request.setAttribute("show", "grid");
	
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
					request.setAttribute("show", "list");
					if (posId == null || posId.trim().isEmpty())
						posId = spec.getPos().getId().toString();
				}
			}
		}
	}
	
	if (posId == null || posId.trim().isEmpty() || posId.startsWith("s")) {
		if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN)
			posId = context.user.getPos().getId().toString();
		else {
			BBRDataSet<BBRPoS> d = null;
			BBRPoSManager pmgr = new BBRPoSManager();
			if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN) 
				d = pmgr.list("", "id", "shop.id = " + context.user.getShop().getId().toString());
			else
				if (context.user.getRole() == BBRUserRole.ROLE_BBR_OWNER)
					if (posId != null && posId.startsWith("s"))
						d = pmgr.list("", "id", "shop.id = " + posId.substring(1));
					else
						d = pmgr.list();
			if (d != null && d.data.size() > 0) {
				BBRPoS pos = (BBRPoS)(d.data.get(0));
				posId = pos.getId().toString();
			} else
				posId = "";

		}
	}
	
	request.setAttribute("posId", posId);
	
	request.setAttribute("titleMod", titleMod);
	
	if (context.get("turnsList") == null)
		request.setAttribute("turnsListBtn", "#toggleFutureTurnsBtn");
	else
		request.setAttribute("turnsListBtn", "#toggleAllTurnsBtn");
%>

<t:wrapper title="LBL_SPEC_TURNS_TITLE" titleModifier="${titleMod}">
	<jsp:body>
		<ul class="nav nav-pills">
		  <li role="presentation" id="showGridBtn"><a href="#">${context.gs("LBL_TURNS_GRID_TITLE")}</a></li>
		  <li role="presentation" id="showListBtn"><a href="#">${context.gs("LBL_TURNS_LIST_TITLE")}</a></li>
		</ul>
		<div id="gridDiv">
			<h3>${context.gs("LBL_SPEC_TURNS_TITLE").concat(titleMod)}</h3>
			<t:card-schedule-spec-turns posId="${posId}"/>
		</div>
		<div class="hide" id="listDiv">
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
		</div>
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
	
	$("#showGridBtn").click(function () {
		$("#gridDiv").removeClass("hide");
		$("#listDiv").addClass("hide");
		$("#showGridBtn").addClass("active");
		$("#showListBtn").removeClass("active");
	})

	$("#showListBtn").click(function () {
		$("#listDiv").removeClass("hide");
		$("#gridDiv").addClass("hide");
		$("#showListBtn").addClass("active");
		$("#showGridBtn").removeClass("active");
	})
	
	if ("${show}" == "grid")
		$("#showGridBtn").click();
	else
		$("#showListBtn").click();

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