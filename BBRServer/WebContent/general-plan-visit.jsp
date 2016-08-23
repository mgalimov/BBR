<%@page import="BBR.BBRUtil"%>
<%@page import="BBRAcc.BBRPoSManager"%>
<%@page import="BBRAcc.BBRPoS"%>
<%@page import="BBRCust.BBRVisit"%>
<%@page import="BBRCust.BBRSpecialist"%>
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

	String posId = params.get("pos");
	if (posId != null && !posId.isEmpty()) {
		BBRPoSManager pmgr = new BBRPoSManager();
		BBRPoS pos = pmgr.findById(Long.parseLong(posId));
	    if (pos != null) {
	    	request.setAttribute("posId", posId);
	    	request.setAttribute("posTitle", pos.getTitle());
	    }
	} else
		request.setAttribute("posId", 1);
	
%>
<t:light-wrapper title="LBL_PLAN_VISIT_TITLE">
<jsp:body>
	<div class="container">
		<div class="row">
		  <a href="#" class="btn btn-primary" id="specBtn">${context.gs("LBL_GET_BY_SPEC")}</a>
		  <a href="#" class="btn btn-primary" id="procBtn">${context.gs("LBL_GET_BY_PROC")}</a>
		</div>
		<p/>
		<div class="row">
			<div class='input-group date col-md-3 hide' id='dateInputDiv'>	
				<input id='dateInput' type='text' class='form-control' />
				<span class='input-group-addon'>
					<span class='glyphicon glyphicon-calendar'/></span>
				</span>
			</div>
			<p/>
			<div class="form-group hide" id="nameGroup">
				<label for="nameInput">${context.gs("LBL_YOUR_NAME")}</label>
				<input type="text" id="nameInput" class="form-control" required/>
			</div>
			<div class="form-group hide" id="contactGroup">
				<label for="contactInput">${context.gs("LBL_YOUR_PHONE")}</label>
				<input type="text" id="contactInput" class="form-control" required/>
			</div>
			<a href="#" class="btn btn-primary hide" id="finishBtn">${context.gs("BTN_FINISH_BOOKING")}</a>
			<div id="mainTab">
			</div>
			<a href="#" class="btn btn-primary hide" id="closeBtn">${context.gs("BTN_CLOSE_BOOKING")}</a>
		</div>
	</div>
</jsp:body>
</t:light-wrapper>

<script>
	$(document).ready(function () {
		specId = 0;
		dateSelected = new Date();
		timeSelected = "";
		
		$("#specBtn").click(fillSpec);
		$("#procBtn").click(fillProc);
		
		fillSpec();

		moment.locale('<%=context.getLocaleString()%>');
		var m = moment();
		$("#dateInputDiv").datetimepicker({
			format: "YYYY-MM-DD",
			locale: "${context.getLocaleString()}",
			defaultDate: dateSelected,
			minDate: m.format("YYYY-MM-DD HH:mm")
		});
		$("#dateInputDiv").on("dp.change", fillTime);
		
		$("#finishBtn").click(function() {
			$.ajax({
				url: "BBRVisits",
				data: {
					operation: "createWizard",
					userName: $("#nameInput").val(),
					userContacts: $("#contactsInput").val(),
					timeScheduled: $("#dateInput").val() + " " + timeSelected,
					pos: ${posId},
					spec: specId,
					proc: procId
				}
			}).done(function (data) {
				fillFinish($.parseJSON(data));
			}).fail(function () {
				
			});
		});
		
		$("#closeBtn").click(function() {
			window.location.reload();
		});

	});
	
	function fillSpec() {
		procId = "";
		procName = "";
		$("#dateInputDiv").addClass("hide");
		$("#nameGroup").addClass("hide");
		$("#contactGroup").addClass("hide");
		$("#finishBtn").addClass("hide");
		$("#closeBtn").addClass("hide");
		$.ajax({
			url: "BBRSpecialists",
			data: {
				operation: "reference",
				constrains: ${posId}
			}
		}).done(function (data) {
			d = $.parseJSON(data);
			var html = "";
			n = 0;
			for (i = 0; i < d.recordsTotal; i++) {
				spec = d.data[i];
				if (spec.status == 1) {
					media = "<div class='media'><div class='media-left pull-left media-middle' style='padding-right: 10px;'><img class='media-object' src='images/barb.png' alt='"+spec.name+"'></div><div class='media-body'><h4 class='media-heading'>"+spec.name+"</h4>"+spec.position+"</div></div>";
					html += "<a href='#' class='list-group-item' id='specA" + spec.id + "' data-type='specialist' data-id='" + spec.id + "' data-name='" + spec.name + "'>" + media + "</a>";
				}
			}
			$("#mainTab").html(html);
			$("[data-type$=specialist]").click(function () {
				specId = $(this).attr('data-id');
				specName = $(this).attr('data-name');
				$("#dateInputDiv").removeClass("hide");
				$("#mainTab").html("");
				fillTime();
			})
		});
	}
		
	function fillProc() {
		specId = "";
		specName = "";
		$("#dateInputDiv").addClass("hide");
		$("#nameGroup").addClass("hide");
		$("#contactGroup").addClass("hide");
		$("#finishBtn").addClass("hide");
		$("#closeBtn").addClass("hide");
		$.ajax({
			url: "BBRProcedures",
			data: {
				operation: "reference",
				constrains: ${posId}
			}
		}).done(function (data) {
			d = $.parseJSON(data);
			var html = "";
			n = 0;
			for (i = 0; i < d.recordsTotal; i++) {
				proc = d.data[i];
				if (proc.status == 1) {
					media = "<div class='media'><div class='media-left pull-left media-middle' style='padding-right: 10px;'><img class='media-object' src='images/barb.png' alt='"+proc.title+"'></div><div class='media-body'><h4 class='media-heading'>"+proc.title+"</h4>" + proc.length + ", " + proc.price + "</div></div>";
					html += "<a href='#' class='list-group-item' id='procA" + proc.id + "' data-type='procedure' data-id='" + proc.id + "' data-name='" + proc.title + "'>" + media + "</a>";
				}
			}
			$("#mainTab").html(html);
			$("[data-type$=procedure]").click(function () {
				procId = $(this).attr('data-id');
				procName = $(this).attr('data-name');
				$("#dateInputDiv").removeClass("hide");
				$("#mainTab").html("");
				fillTime();
			})
		});
	}

	function fillTime() {
		dateSelected = $("#dateInput").val();

		$.ajax({
			url: "BBRSchedule",
			data: {
				operation: "freetimes",
				date: dateSelected,
				spec: specId,
				proc: procId,
				pos: ${posId}
			}
		}).done(function (data) {
			var a = data.split("*");
			var n = 0;
			var html = "";
			if (data == "") 
				html += "${context.gs('LBL_NO_FREE_TIMES')}";
			else {
				for (i = 0; i < a.length; i++) {
					time = a[i];
	                html += "<a href='#' class='list-group-item' data-type='time' data-time='" + time + "'>" + time + "</a>";
				}
			}
			$("#mainTab").html(html);
			$("[data-type$=time]").click(function () {
				$("#mainTab").html("");
				dateSelected = $("#dateInput").val();
				timeSelected = $(this).attr('data-time');
				if (timeSelected != "") {
					$("#selectSpecBody").html("");
					$("#dateInputDiv").addClass("hide");
					$("#nameGroup").removeClass("hide");
					$("#contactGroup").removeClass("hide");
					$("#finishBtn").removeClass("hide");
					$("#nameInput").focus();
				}
			})
		}).fail(function () {
			$("#mainTab").html("${context.gs('LBL_NO_FREE_TIMES')}");
		});		
	}
	
	function fillFinish(visit) {
		$("#dateInputDiv").addClass("hide");
		$("#nameGroup").addClass("hide");
		$("#contactGroup").addClass("hide");
		$("#finishBtn").addClass("hide");
		$("#closeBtn").removeClass("hide");
		
		var html = "<h2>" + $("#nameInput").val() + "${context.gs('LBL_THANKS_FOR_BOOKING')}!</h2>"
		if (specId > 0)
			html += "<p>${context.gs('LBL_YOUR_SPEC_IS')}</p><h4>" + specName + "</h4><p/>";
			else 
				if (procId > 0) {
					html += "<p>${context.gs('LBL_YOU_BOOKED_PROC')}</p><h4>" + procName + "</h4><p/>";
					html += "<p>${context.gs('LBL_AT_SPEC')}</p><h4>" + visit.spec.name + "</h4><p/>";
				}
				
		html += "<p>${context.gs('LBL_TO_DATE_TIME')}</p><h4>" + dateSelected + " " + timeSelected + "</h4><p/>";
		html += "<p>${context.gs('LBL_IN_POS')}</p><h4>${posTitle}</h4><p/>";
	
		$("#mainTab").html(html);

	}

	
</script>