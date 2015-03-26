<%@page import="BBRAcc.BBRPoSManager"%>
<%@page import="BBRAcc.BBRPoS"%>
<%@page import="BBR.BBRDataSet"%>
<%@page import="BBRClientApp.BBRContext"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<script src="//api-maps.yandex.ru/2.1/?lang=ru_RU" type="text/javascript"></script>
<script src="js/bbr-maps.js" type="text/javascript"></script>

<%
	BBRContext context = BBRContext.getContext(request);
	
	int visitStep = context.getLastVisitStep();
	if (visitStep > 3 || visitStep < 1)
		context.setLastVisitStep(1);
	request.setAttribute("visitStep", visitStep);
	
	Long visitId = Long.parseLong("0");
	if (context.planningVisit != null)
		visitId = context.planningVisit.getId();
	
	if (context.user != null)
		request.setAttribute("userName", context.user.getFirstName() + " " + context.user.getLastName());
	else
		request.setAttribute("userName", "");
	
	request.setAttribute("location", Double.toString(context.getLocation().lat) + ", " + Double.toString(context.getLocation().lng));
	
	String posCoords = "";
	String posIds = "";
	BBRPoSManager mgr = new BBRPoSManager();
	BBRDataSet<BBRPoS> poses = mgr.listLocal(context.getLocation(), 10.0);
	for (BBRPoS pos : poses.page_data) {
		posCoords = posCoords + "[" + pos.getLocationGPS().getLat() + "," + pos.getLocationGPS().getLng() + "], ";
		posIds = posIds + pos.getId().toString() + ", ";
	}
	request.setAttribute("posCoords", posCoords);
	request.setAttribute("posIds", posIds);
	
	if (visitStep == 4) {
		String outString = "<p>Your visit id is " + context.planningVisit.getId() + "</p>";
		outString += "<p>Place " + context.planningVisit.getPos().getTitle() + " at " + 
					  context.planningVisit.getPos().getLocationDescription() + "<a href='" + context.planningVisit.getPos().getMapHref() + "'>See map</a></p>";
		outString += "<p>Date and time " + context.planningVisit.getTimeScheduled() + "</p>";
		if (context.planningVisit.getSpec() != null) {
			outString += "<p>Your specialist is " + context.planningVisit.getSpec().getName() + "</p>";
		}
		if (context.planningVisit.getProcedure() != null) {
			outString += "<p>Your procedure is " + context.planningVisit.getProcedure().getTitle() + "</p>";
		}
		outString += "<p>It will take " + context.planningVisit.getLength() + " hours.</p>";
		
		request.setAttribute("outString", outString);
	}
	
%>
<t:general-wrapper title="Plan your visit">
<jsp:body>

<c:choose>
	<c:when test="${visitStep == 1}">
		<t:card title="Plan your visit. Step 1. Place to visit" gridPage="general-plan-visit.jsp" method="BBRVisits" buttonSave="Go to step 2" buttonCancel="Cancel">
			<div class="panel col-md-12" id="map" style="height: 300px"></div>
			<t:card-item label="Select place" type="reference" field="pos" isRequired="required" referenceFieldTitle="title" referenceMethod="BBRPoSes" defaultValue="${closestPoS}" defaultDisplay="${closestPoSName}"/>
		</t:card>
		
		<script>
			$(document).ready(function () {
				ymaps.ready(function () {
					initMap(${location});
					addPosesToMap([${posCoords}], [${posIds}], function(posId) {
						 $("#posinput")[0].selectize.addItem(posId);
						 $("#posinput")[0].selectize.refreshItems();
					});
				})
			});
		</script>
	</c:when>

	<c:when test="${visitStep == 2}">
		<t:card title="Plan your visit. Step 2. Date and time" gridPage="general-plan-visit.jsp" method="BBRVisits" buttonSave="Go to step 3" buttonCancel="Cancel">
			<t:card-schedule-spec-proc/>
		</t:card>
	</c:when>

	<c:when test="${visitStep == 3}">
		<t:card title="Plan your visit. Step 3. Contact information" gridPage="general-plan-visit.jsp" method="BBRVisits" buttonSave="Get it done" buttonCancel="Cancel">
			<t:card-item label="Your name" type="text" field="userName" isRequired="required" defaultValue="${userName}"/>
			<t:card-item label="Your phone" type="text" field="userContacts" isRequired="required" />
		</t:card>
	</c:when>
	
	<c:when test="${visitStep == 4}">
		<div>
			<h1>Your visit planned</h1>
			<p>You'll get approved soon. Please find your visit information below:</p>
			<c:out value="${outString}" escapeXml="false"/>
		</div>
	</c:when>
</c:choose>
</jsp:body>
</t:general-wrapper>
