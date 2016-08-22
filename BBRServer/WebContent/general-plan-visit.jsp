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
		  <a href="#" class="btn btn-primary" id="specBtn">Хочу к специалисту</a>
		  <a href="#" class="btn btn-primary" id="procBtn">Хочу услугу</a>
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
				<label for="nameInput">Имя</label>
				<input type="text" id="nameInput" class="form-control" required/>
			</div>
			<div class="form-group hide" id="contactGroup">
				<label for="contactInput">Контактные данные</label>
				<input type="text" id="contactInput" class="form-control" required/>
			</div>
			<a href="#" class="btn btn-primary form-control hide" id="finishBtn">Finish</a>
			<div id="mainTab">
			</div>
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
					operation: "createWizardSpecTime",
					userName: $("#nameInput").val(),
					userContacts: $("#contactsInput").val(),
					timeScheduled: $("#dateInput").val() + " " + timeSelected,
					pos: ${posId},
					spec: specId
				}
			}).done(function () {
				fillFinish();
			}).fail(function () {
				
			});
		});
	});
	
	function fillSpec() {
		procId = "";
		procName = "";
		$("#dateInputDiv").addClass("hide");
		$("#nameGroup").addClass("hide");
		$("#contactGroup").addClass("hide");
		$("#finishBtn").addClass("hide");
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
			for (i = 0; i < a.length; i++) {
				time = a[i];
                html += "<a href='#' class='list-group-item' data-type='time' data-time='" + time + "'>" + time + "</a>";
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
		});		
	}
	
	function fillFinish() {
		$("#dateInputDiv").addClass("hide");
		$("#nameGroup").addClass("hide");
		$("#contactGroup").addClass("hide");
		$("#finishBtn").addClass("hide");

		var html = "<h2>" + $("#nameInput").val() + ", спасибо за бронирование!</h2>"
		if (specId > 0)
			html += "<p>Вы забронировали посещение к мастеру </p><h4>" + specName + "</h4><p/>";
			else 
				if (procId > 0)
					html += "<p>Вы забронировали услугу</p><h4>" + procName + "</h4><p/>";
				
		html += "<p>на дату и время</p><h4>" + dateSelected + " " + timeSelected + "</h4><p/>";
		html += "<p>в салоне </p><h4>${posTitle}</h4><p/>";
	
		$("#mainTab").html(html);

	}

	
</script>