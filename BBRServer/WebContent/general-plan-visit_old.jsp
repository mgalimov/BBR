<%@page import="BBR.BBRUtil"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="BBR.BBRGPS"%>
<%@page import="BBRAcc.BBRPoSManager"%>
<%@page import="BBRAcc.BBRPoS"%>
<%@page import="BBRCust.BBRVisit"%>
<%@page import="BBR.BBRDataSet"%>
<%@page import="BBRClientApp.BBRContext"%>
<%@ page import="BBRClientApp.BBRParams"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<script src="//api-maps.yandex.ru/2.1/?lang=ru_RU" type="text/javascript"></script>
<script src="js/bbr-maps.js" type="text/javascript"></script>

<%
	BBRContext context = BBRContext.getContext(request);
	BBRParams params = new BBRParams(request.getQueryString());

	String titleMod = "";

	String posid = params.get("pos");
	int visitStep = context.getLastVisitStep();
	if (visitStep > 3 || visitStep < 1)
		context.setLastVisitStep(1);

	if (visitStep == 1 && posid != null && !posid.isEmpty()) {
		BBRPoSManager pmgr = new BBRPoSManager();
		BBRPoS pos = pmgr.findById(Long.parseLong(posid));
		context.set("pos", pos);
	    if (pos != null) {
	    	context.planningVisit = new BBRVisit();
	    	context.planningVisit.setPos(pos);
	    	String country = pos.getShop().getCountry();
	    	if (context.user == null && country != null && country.equals("RU"))
	    		context.setLocale("ru_RU");
	    	visitStep++;
	    	context.setLastVisitStep(visitStep);
	    }
	} else
		context.set("pos", null);

	request.setAttribute("visitStep", visitStep);
	
	Long visitId = Long.parseLong("0");
	if (context.planningVisit != null) {
		visitId = context.planningVisit.getId();
		if (context.planningVisit.getPos() != null)
			request.setAttribute("posTitle", context.planningVisit.getPos().getTitle() + ", " + context.planningVisit.getPos().getLocationDescription());
		if (context.planningVisit.getSpec() != null)
			request.setAttribute("specName", context.planningVisit.getSpec().getName());
		if (context.planningVisit.getProcedure() != null) {
			request.setAttribute("procTitle", context.planningVisit.getProcedure().getTitle());
			request.setAttribute("procDuration", context.getDurationString(context.planningVisit.getProcedure().getLength()));
		}
		if (context.planningVisit.getTimeScheduled() != null) {
			SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateTimeFormat);
			request.setAttribute("visitTime", df.format(context.planningVisit.getTimeScheduled()));
		}
		request.setAttribute("visitorName", context.planningVisit.getUserName());
		request.setAttribute("visitorContacts", context.planningVisit.getUserContacts());
	}
	
	if (context.user != null)
		request.setAttribute("userName", context.user.getFirstName() + " " + context.user.getLastName());
	else
		request.setAttribute("userName", "");
	
	BBRGPS loc = context.getLocation(); 
	if (loc != null)
		request.setAttribute("location", Double.toString(loc.lat) + ", " + Double.toString(loc.lng));
	else
		request.setAttribute("location", "");
	
	String posCoords = "";
	String posIds = "";
	BBRPoSManager mgr = new BBRPoSManager();
	BBRDataSet<BBRPoS> poses = mgr.listLocal(context.getLocation(), 10.0);

	for (BBRPoS pos : poses.data) {
		if (pos.getLocationGPS() != null)
			posCoords = posCoords + "[" + pos.getLocationGPS().getLat() + "," + pos.getLocationGPS().getLng() + "], ";
		else
			posCoords = posCoords + "[0,0], ";
		posIds = posIds + pos.getId().toString() + ", ";
	}
	request.setAttribute("posCoords", posCoords);
	request.setAttribute("posIds", posIds);
	
	if (visitStep == 4) {
		String outString = "<p>" + context.gs("LBL_YOUR_VISIT_ID", context.planningVisit.getId()) + "</p>";
		BBRPoS pos = context.planningVisit.getPos(); 
		if (pos != null) {
			outString += "<p>" + context.gs("LBL_YOUR_VISIT_AT", pos.getTitle(), pos.getLocationDescription());
			outString += "<a href='" + context.planningVisit.getPos().getMapHref() + "' target='_blank'>"+context.gs("LBL_YOUR_VISIT_SEE_MAP")+"</a></p>";
		}
		outString += "<p>" + context.gs("LBL_YOUR_VISIT_TIME", context.planningVisit.getTimeScheduled()) + "</p>";
		if (context.planningVisit.getSpec() != null) {
			outString += "<p>" + context.gs("LBL_YOUR_VISIT_SPEC", context.planningVisit.getSpec().getName()) + "</p>";
		}
		if (context.planningVisit.getProcedure() != null) {
			outString += "<p>" + context.gs("LBL_YOUR_VISIT_PROC", context.planningVisit.getProcedure().getTitle()) + "</p>";
		}
		outString += "<p>" + context.gs("LBL_YOUR_VISIT_LENGTH", context.planningVisit.getLength()) + "</p>";
		
		request.setAttribute("outString", outString);
	}
	
	context.set("newVisitMode", "general-edit");
	
%>
<t:wrapper title="LBL_PLAN_VISIT_TITLE">
<jsp:body>

<c:choose>
	<c:when test="${visitStep == 1}">
		<t:card title="LBL_PLAN_VISIT_STEP_1" gridPage="general-plan-visit.jsp" method="BBRVisits" buttonSave="LBL_GOTO_STEP2_BTN" buttonCancel="LBL_CANCEL_VISIT_BTN">
			<div class="panel col-md-12" id="map" style="height: 300px"></div>
			<t:card-item label="LBL_SELECT_PLACE" type="reference" field="pos" isRequired="required" referenceFieldTitle="title" referenceMethod="BBRPoSes" defaultValue="${closestPoS}" defaultDisplay="${closestPoSName}"/>
		</t:card>
		
		<script>
			$(document).ready(function () {
				ymaps.ready(function () {
					initMap(${location});
					addPosesToMap([${posCoords}], [${posIds}], function(posId) {
						 el = $("#posinput")[0].selectize;
						 el.addItem(posId);
						 el.refreshItems();
					});
				})
			});
		</script>
	</c:when>

	<c:when test="${visitStep == 2}">
		<div class="container-fluid">
			<h4>${context.gs("LBL_PLAN_VISIT_STEP_1")} <i>${posTitle}</i></h4>
		</div>
		
		<t:card title="LBL_PLAN_VISIT_STEP_2" gridPage="general-plan-visit.jsp" method="BBRVisits" buttonSave="LBL_GOTO_STEP3_BTN" buttonCancel="LBL_CANCEL_VISIT_BTN">
			<t:card-schedule-spec-proc/>
		</t:card>
	</c:when>

	<c:when test="${visitStep == 3}">
		<div class="container-fluid">
			<h4>${context.gs("LBL_PLAN_VISIT_STEP_1")} <i>${posTitle}</i></h4>
			<h4>${context.gs("LBL_PLAN_VISIT_STEP_2")} <i>${specName}, ${procTitle}, ${visitTime}</i></h4>
		</div>

		<t:card title="LBL_PLAN_VISIT_STEP_3" gridPage="general-plan-visit.jsp" method="BBRVisits" buttonSave="LBL_GET_IT_DONE_BTN" buttonCancel="LBL_CANCEL_VISIT_BTN">
			<t:card-item label="LBL_YOUR_NAME" type="text" field="userName" isRequired="required" defaultValue="${userName}"/>
			<t:card-item label="LBL_YOUR_PHONE" type="text" field="userContacts" isRequired="required" />
		</t:card>
	</c:when>
	
	<c:when test="${visitStep == 4}">
		<div class="container-fluid">
			<h1>${context.gs('LBL_VISIT_CREATED')}</h1>
			<h4>${context.gs("LBL_PLAN_VISIT_STEP_1")} <i>${posTitle}</i></h4>
			<h4>${context.gs("LBL_PLAN_VISIT_STEP_2")} <i>${specName}, ${procTitle}, ${visitTime}, ${procDuration}</i></h4>
			<h4>${context.gs("LBL_PLAN_VISIT_STEP_3")} <i>${visitorName}, ${visitorContacts}</i></h4>
		</div>
		<!-- p>${context.gs('LBL_YOULL_GET_APPROVED_SOON')}</p-->
		<!-- c:out value="${outString}" escapeXml="false"/-->
	</c:when>
</c:choose>
</jsp:body>
</t:wrapper>
