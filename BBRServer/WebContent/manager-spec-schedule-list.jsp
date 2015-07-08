<%@page import="BBR.BBRGPS"%>
<%@page import="BBRAcc.BBRPoSManager"%>
<%@page import="BBRAcc.BBRPoS"%>
<%@page import="BBR.BBRDataSet"%>
<%@page import="BBRClientApp.BBRContext"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<script src="//api-maps.yandex.ru/2.1/?lang=ru_RU" type="text/javascript"></script>
<script src="js/bbr-maps.js" type="text/javascript"></script>
<t:general-wrapper title="LBL_PLAN_VISIT_TITLE">
	<jsp:body>
		<t:card title="LBL_PLAN_VISIT_STEP_2" gridPage="general-plan-visit.jsp" method="BBRVisits" buttonSave="LBL_GOTO_STEP3_BTN" buttonCancel="LBL_CANCEL_VISIT_BTN">
			<t:card-schedule-spec-proc mode="manager-view"/>
		</t:card>
	</jsp:body>
</t:general-wrapper>
