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
	    	request.setAttribute("posAddress", pos.getLocationDescription());
	    }
	} else
		request.setAttribute("posId", 1);
	
%>
<t:light-wrapper title="LBL_PLAN_VISIT_TITLE">
<jsp:body>
	<div class="container">
		<t:modal  cancelButtonLabel="LBL_GRID_CONFIRM_DELETION_CANCEL_BTN" 
				  processButtonLabel="LBL_GRID_CONFIRM_DELETION_CONFIRM_BTN" 
				  title="LBL_GRID_CONFIRM_DELETION_TITLE" 
				  id="sureToDelete">
			${context.gs('MSG_GRID_CONFIRM_DELETION')} 
		</t:modal>
		<div class="row">
			<h2>${posTitle}</h2>
			<p>${posAddress}</p>
			<p/>
		</div>
		<div class="row">
		  <a href="#" class="btn btn-default" id="specBtn">${context.gs("LBL_GET_BY_SPEC")}</a>
		  <a href="#" class="btn btn-default" id="procBtn">${context.gs("LBL_GET_BY_PROC")}</a>
		  <a href="#" class="btn btn-default" id="checkBtn">${context.gs("LBL_CHECK_BOOKING")}</a>
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
			<div class="form-group hide" id="bookingGroup">
				<label for="nameInput">${context.gs("LBL_YOUR_VISIT_CODE")}</label>
				<div class="input-group">
					<input type="text" id="bookingCodeInput" class="form-control" required/>
				    <span class="input-group-btn">
	        			<button class="btn btn-default" type="button" id="bookingCodeBtn"><span class="glyphicon glyphicon-search" aria-hidden="true"></span>&nbsp;</button>
	      			</span>
      			</div>
			</div>
			<a href="#" class="btn btn-primary hide" id="finishBtn">${context.gs("BTN_FINISH_BOOKING")}</a>
			<div id="mainTab">
			</div>
			<a href="#" class="btn btn-primary hide" id="closeBtn">${context.gs("BTN_CLOSE_BOOKING")}</a>
			<a href="#" class="btn btn-danger hide" id="cancelBtn">${context.gs("BTN_CANCEL_BOOKING")}</a>
			<a href="#" class="btn btn-danger hide" id="cancelBookingBtn">${context.gs("BTN_CANCEL_BOOKING")}</a>
		</div>
	</div>
</jsp:body>
</t:light-wrapper>

<script>
	timerCodeInput = null;
	visitGlobal = null;

	$(document).ready(function () {
		specId = 0;
		dateSelected = new Date();
		timeSelected = "";
		
		$("#specBtn").click(fillSpec);
		$("#procBtn").click(fillProc);
		$("#checkBtn").click(fillCheck);
		
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
		
		$("#bookingCodeInput").keyup(function () {
			if (timerCodeInput)
				clearTimeout(timerCodeInput);
			timerCodeInput = setTimeout(checkBookingCode, 1500);
		}).keydown(function (e) {
			if (e.which == 13)
				checkBookingCode();
		});
		$("#bookingCodeBtn").click(checkBookingCode);
		
		$("#cancelBookingBtn").click(function () {
			$('#sureToDelete').modal();
		});

		$('#sureToDeleteprocess').click(function () {
			if (visitGlobal && visitGlobal.id)
				$.ajax({
					url: "BBRVisits",
					operation: "cancelVisit",
					visitId: visitGlobal.id
				}).done(function () {
					$('#sureToDelete').hide();
				}).fail(function () {
					$('#sureToDelete').hide();
				});
		});

	});
	
	function fillSpec() {
		procId = "";
		procName = "";
		$("#specBtn").removeClass("btn-default").addClass("btn-primary");
		$("#procBtn").removeClass("btn-primary").addClass("btn-default");
		$("#checkBtn").removeClass("btn-primary").addClass("btn-default");
		$("#dateInputDiv").addClass("hide");
		$("#nameGroup").addClass("hide");
		$("#contactGroup").addClass("hide");
		$("#bookingGroup").addClass("hide");
		$("#finishBtn").addClass("hide");
		$("#closeBtn").addClass("hide");
		$("#cancelBtn").addClass("hide");
		$("#cancelBookingBtn").addClass("hide");
		
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
		$("#procBtn").removeClass("btn-default").addClass("btn-primary");
		$("#specBtn").removeClass("btn-primary").addClass("btn-default");
		$("#checkBtn").removeClass("btn-primary").addClass("btn-default");
		$("#dateInputDiv").addClass("hide");
		$("#nameGroup").addClass("hide");
		$("#contactGroup").addClass("hide");
		$("#bookingGroup").addClass("hide");
		$("#finishBtn").addClass("hide");
		$("#closeBtn").addClass("hide");
		$("#cancelBtn").addClass("hide");
		$("#cancelBookingBtn").addClass("hide");
		
		$.ajax({
			url: "BBRProcedures",
			data: {
				operation: "limitedreference",
				constrains: ${posId}
			}
		}).done(function (data) {
			d = $.parseJSON(data);
			var html = "";
			n = 0;
			for (i = 0; i < d.recordsTotal; i++) {
				proc = d.data[i];
				if (proc.status == 1) {
					media = "<div class='media'><div class='media-left pull-left media-middle' style='padding-right: 10px;'><img class='media-object' src='images/tool.png' alt='"+proc.title+"'></div><div class='media-body'><h4 class='media-heading'>" + proc.title + "</h4>" + procLength(proc.length) + ", " + proc.price + proc.pos.currency + "</div></div>";
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
		if (!visit) return;
		visitGlobal = visit;
		
		$("#dateInputDiv").addClass("hide");
		$("#nameGroup").addClass("hide");
		$("#contactGroup").addClass("hide");
		$("#bookingGroup").addClass("hide");
		$("#finishBtn").addClass("hide");
		$("#closeBtn").removeClass("hide");
		$("#cancelBtn").removeClass("hide");
		$("#cancelBookingBtn").addClass("hide");
		
		var html = "<h2>" + $("#nameInput").val() + "${context.gs('LBL_THANKS_FOR_BOOKING')}!</h2>"
		html = displayVisit(visit);
	
		$("#mainTab").html(html);
		
		$("#cancelBtn").click(function() {
			fillCheck(visit.bookingCode);
		})

	}
	
	function fillCheck(bookingCode) {
		$("#checkBtn").removeClass("btn-default").addClass("btn-primary");
		$("#specBtn").removeClass("btn-primary").addClass("btn-default");
		$("#procBtn").removeClass("btn-primary").addClass("btn-default");
		$("#dateInputDiv").addClass("hide");
		$("#nameGroup").addClass("hide");
		$("#contactGroup").addClass("hide");
		$("#bookingGroup").removeClass("hide");
		$("#finishBtn").addClass("hide");
		$("#closeBtn").addClass("hide");
		$("#cancelBtn").addClass("hide");
		$("#cancelBookingBtn").addClass("hide");
		$("#mainTab").html("");
		
		if (bookingCode != null && typeof bookingCode == "string")
			$("#bookingCodeInput").val(bookingCode);
		
		checkBookingCode();
	}
	
	function checkBookingCode() {
		$.ajax({
			url: "BBRVisits",
			data: {
				operation: "checkBookingCode",
				code: $("#bookingCodeInput").val()
			}
		}).done(function (visit) {
			if (visit != "") {
				visitGlobal = $.parseJSON(visit);
				$("#mainTab").html(displayVisit(visitGlobal));
				$("#cancelBookingBtn").removeClass("hide");
			}
			else
				$("#mainTab").html("${context.gs('MSG_NO_VISIT_WITH_CODE')}");
		});
	}
	
	function displayVisit(visit) {
		var html = "";
		if (visit.spec)
			html += "<p>${context.gs('LBL_YOUR_SPEC_IS')}</p><h4>" + visit.spec.name + "</h4><p/>";
		if (visit.procedure) {
			html += "<p>${context.gs('LBL_YOU_BOOKED_PROC')}</p><h4>" + visit.procedure.title + "</h4><p/>";
		}
				
		html += "<p>${context.gs('LBL_YOUR_VISIT_CODE')}</p><h4>" + visit.bookingCode + "</h4><p/>";
		html += "<p>${context.gs('LBL_TO_DATE_TIME')}</p><h4>" + visit.timeScheduled + "</h4><p/>";
		html += "<p>${context.gs('LBL_IN_POS')}</p><h4>" + visit.pos.title + "</h4><p/>";
		html += "<p>" + visit.pos.locationDescription + "<p/>";
		return html;
	}
	
	function procLength(length) {
		hour = Math.round(Math.floor(length));
		minutes = Math.round((length - hour) * 60);
		
		if (minutes == 1)
			mins = minutes + " ${context.gs('SYS_MINUTE')}";
		else
			if (minutes > 0)
				mins = minutes + " ${context.gs('SYS_MINUTES')}";
			else
				mins = "";
		
		if (hour == 1)
			hrs = hour + " ${context.gs('SYS_HOUR')}";
		else
			if (hour > 0)
				hrs = hour + " ${context.gs('SYS_HOURS')}";
			else
				hrs = "";
				
		if (hrs == "" && mins == "")
			return "--";
		else
			if (hrs == "")
				return mins;
			else
				if (mins == "")
					return hrs;
				else
					return hrs + " " + mins;
	}

	
</script>