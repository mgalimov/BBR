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
	String lastVisit = context.getLastVisitScheduled();
	request.setAttribute("lastVisit", lastVisit);
	if (context.user != null)
		request.setAttribute("userName", context.user.getFirstName() + " " + context.user.getLastName());
	else
		request.setAttribute("userName", "");
	request.setAttribute("location", Double.toString(context.getLocation().lat) + ", " + Double.toString(context.getLocation().lng));
	
	String posesCoords = "";
	String bbrIds = "";
	BBRPoSManager mgr = new BBRPoSManager();
	BBRDataSet<BBRPoS> poses = mgr.listLocal(context.getLocation(), 10.0);
	for (BBRPoS pos : poses.page_data) {
		posesCoords = posesCoords + "[" + pos.getLocationGPS().getLat() + "," + pos.getLocationGPS().getLng() + "], ";
		bbrIds = bbrIds + pos.getId().toString() + ", ";
	}
	request.setAttribute("posesCoords", posesCoords);
	request.setAttribute("bbrIds", bbrIds);
%>
<t:general-wrapper title="Plan your visit">
<jsp:body>
<c:out value="${location}"></c:out>

<c:choose>
	<c:when test="${lastVisit == null}">
		<t:card title="Plan your visit" gridPage="general-plan-visit.jsp" method="BBRVisits">
			<div class="panel col-md-12" id="map" style="height: 400px"></div>
			<t:card-item label="Select place" type="reference" field="pos" isRequired="required" referenceFieldTitle="title" referenceMethod="BBRPoSes" defaultValue="${closestPoS}" defaultDisplay="${closestPoSName}"/>
			<t:card-item label="Date and time YYYY-MM-DD HH-MM" type="text" field="timeScheduled" isRequired="required" />
			<t:card-item label="Your name" type="text" field="userName" isRequired="required" defaultValue="${userName}"/>
			<t:card-item label="Your phone" type="text" field="userContacts" isRequired="required" />
			<t:card-item label="Select procedure" type="reference" field="procedure" referenceFieldTitle="title" referenceMethod="BBRProcedures"/>
			<t:card-item label="Select specialist" type="reference" field="spec" referenceFieldTitle="name" referenceMethod="BBRSpecialists"/>
		</t:card>
		
		<script>
			$(document).ready(function () {
				ymaps.ready(function () {
					initMap(${location});
					addPosesToMap([${posesCoords}], [${bbrIds}]);
				})
			});
		</script>
	</c:when>
	
	<c:when test="${lastVisit != null}">
		<div>
			<p>Thanks for your visit! Your visit id is ${lastVisit}.
			</p>
		</div>
	</c:when>
</c:choose>
</jsp:body>
</t:general-wrapper>
