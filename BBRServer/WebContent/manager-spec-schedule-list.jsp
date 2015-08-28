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
%>

<script src="js/bbr-maps.js" type="text/javascript"></script>
<t:general-wrapper title="LBL_SPEC_SCHEDULE_TITLE">
	<jsp:body>
		<t:card title="LBL_SPEC_SCHEDULE" gridPage="manager-visit-create.jsp" method="BBRVisits" showFooter="false">
			<t:card-schedule-spec-proc mode="manager-view" posId="${posId}"/>
		</t:card>
	</jsp:body>
</t:general-wrapper>
