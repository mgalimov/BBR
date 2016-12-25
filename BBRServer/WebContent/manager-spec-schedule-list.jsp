<%@page import="BBRClientApp.BBRParams"%>
<%@page import="BBR.BBRGPS"%>
<%@page import="BBRAcc.BBRPoSManager"%>
<%@page import="BBRAcc.BBRPoS"%>
<%@page import="BBR.BBRDataSet"%>
<%@page import="BBRClientApp.BBRContext"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<% 	
	BBRParams params = new BBRParams(request.getQueryString());
	request.setAttribute("posId", params.get("posId"));
	
	BBRContext context = BBRContext.getContext(request);
	context.set("newVisitMode", "manager-view");
%>

<script src="js/bbr-maps.js" type="text/javascript"></script>
<t:wrapper title="LBL_SPEC_SCHEDULE_TITLE">
	<jsp:body>
		<t:card title="LBL_SPEC_SCHEDULE" gridPage="manager-visit-create.jsp" method="BBRVisits" showFooter="false" showToolbar="true">
			<t:toolbar-group>
				<t:toolbar-item label="LBL_OPEN_VISITS_BTN" id="openVisits" icon="glyphicon-calendar"/>
<%-- 				<t:toolbar-item label="LBL_OPEN_ALL_UNAPPROVED_VISITS_BTN" id="openAllUnapprovedVisits" icon="glyphicon-question-sign"/> --%>
				<t:toolbar-item label="LBL_OPEN_ALL_VISITS_BTN" id="openAllVisits" icon="glyphicon-list-alt"/>
			</t:toolbar-group>
			<t:card-schedule-spec-proc posId="${posId}"/>
		</t:card>
	</jsp:body>
</t:wrapper>

<script>
	$(document).ready(function() {
		$("#openVisits").click(function(){
			dt = $("a[id^='sd'].btn-info").attr('id').substring(2, 12);
			pos = $("#shopposinput").val();
			if (pos.charAt(0) != "s")
				window.location.href = "manager-visit-list.jsp?t=datepos&query="+dt+"@@"+pos; 
		});

		$("#openAllUnapprovedVisits").click(function(){
			pos = $("#shopposinput").val();
			if (pos.charAt(0) != "s")
				window.location.href = "manager-visit-list.jsp?t=unapproved&query="+pos; 
		});

		$("#openAllVisits").click(function(){
			dt = $("a[id^='sd'].btn-info").attr('id').substring(2, 12);
			pos = $("#shopposinput").val();
			if (pos.charAt(0) != "s")
				window.location.href = "manager-visit-list.jsp?t=all&query="+dt+"@@"+pos; 
		});

	});
</script>
