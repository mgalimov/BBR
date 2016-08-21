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
	    }
	} else
		request.setAttribute("posId", 1);
	
%>
<t:light-wrapper title="LBL_PLAN_VISIT_TITLE">
<jsp:body>
	<div class="container">
		<div class="row">
			<ul class="nav nav-pills">
			    <li role="presentation" class="active"><a href="#selectSpec" aria-controls="selectSpec" role="tab" data-toggle="tab">Хочу к специалисту</a></li>
			    <li role="presentation"><a href="#selectProc" aria-controls="selectProc" role="tab" data-toggle="tab">Хочу услугу</a></li>
			</ul>
		</div>
		<p/>
		<div class="row">
			<div class="tab-content">
				<div role="tabpanel" class="tab-pane active" id="selectSpec">
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
					<div id="selectSpecBody">
					</div>
				</div>
				<div role="tabpanel" class="tab-pane" id="selectProc" >				
				</div>
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
		
		fillSpec();
		fillProc();
		
		$("#dateInputDiv").datetimepicker({
			format: "YYYY-MM-DD",
			locale: "${context.getLocaleString()}",
			defaultDate: dateSelected
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
				alert("OK!");
			});
		});
	});
	
	function fillSpec() {
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
					html += "<a href='#' class='list-group-item' id='specA" + spec.id + "' data-type='specialist' data-id='" + spec.id + "'>" + media + "</a>";
				}
			}
			$("#selectSpecBody").html(html);
			$("[data-type$=specialist]").click(function () {
				specId = $(this).attr('data-id');
				$("#dateInputDiv").removeClass("hide");
				fillTime();
			})
		});
	}
		
	function fillProc() {
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
					html += "<a href='#' class='list-group-item' id='procA" + proc.id + "' data-type='procedure' data-id='" + proc.id + "'>" + media + "</a>";
				}
			}
			$("#selectProc").html(html);
			$("[data-type$=procedure]").click(function () {
				alert($(this).attr('data-id'));
			})
		});
	}

	function fillTime() {
		var html = "";
		dateSelected = $("#dateInput").val();

		$.ajax({
			url: "BBRSchedule",
			data: {
				operation: "freetimes",
				date: dateSelected,
				spec: specId,
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
			div = "selectSpecBody";
			$("#"+div).html(html);
			$("[data-type$=time]").click(function () {
				timeSelected = $(this).attr('data-time');
				$("#selectSpecBody").html("");
				$("#dateInputDiv").addClass("hide");
				$("#nameGroup").removeClass("hide");
				$("#contactGroup").removeClass("hide");
				$("#finishBtn").removeClass("hide");
				$("#nameInput").focus();
			})
		});

	}

	
</script>