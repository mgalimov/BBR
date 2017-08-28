<%@page import="BBR.BBRDataSet"%>
<%@page import="BBRAcc.BBRUser.BBRUserRole"%>
<%@page import="BBRCust.BBRVisit.BBRVisitStatus"%>
<%@page import="BBR.BBRUtil"%>
<%@page import="BBRAcc.BBRPoSManager"%>
<%@page import="BBRAcc.BBRShopManager"%>
<%@page import="BBRAcc.BBRPoS"%>
<%@page import="BBRAcc.BBRShop"%>
<%@page import="BBRCust.BBRVisit"%>
<%@page import="BBRCust.BBRSpecialist"%>
<%@page import="BBRClientApp.BBRContext"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% 
	BBRContext context = BBRContext.getContext(request);
%> 

<t:light-wrapper title="LBL_SELECT_POS_TITLE">
<jsp:body>
	<div class="container-fluid">
		<h2 id="head">${context.gs("LBL_SELECT_CITY")}</h2> 
		<h2 id="poses">${context.gs("LBL_SELECT_POS")}</h2> 
		<div class="row">
			<div class="col-md-6 col-xs-12 col-sm-12 col-lg-6" id="main">
			</div>
			<div class="col-md-4 col-xs-12 col-sm-12 col-lg-4" id="map">
				<iframe id="gmap" width="100%" height="400" style="border:0" ></iframe>
			</div>
		</div>
	</div>
</jsp:body>
</t:light-wrapper>

<script>
	$(document).ready(function () {
 		window.addEventListener('popstate', function(e){
 			selector();
 		}, false);

		selector();
	});
	
	function selector() {
		var hash = location.hash;
		if (hash == "" || hash == "#")
			fillCities();
		else if (hash.substr(0, 6) == "#poses")
			fillPoses(hash.substr(12, hash.length-12));		
	}

	function fillCities() {
		$("#head").removeClass("hide");
		$("#poses").addClass("hide");
		$("#map").addClass("hide");
		$.ajax({
			url: "BBRPoSes",
			data: {
				operation: "cityList"
			}
		}).done(function (data) {
			d = $.parseJSON(data);
			var html = "";
			var letter = "";
			for (i = 0; i < d.length; i++) {
				city = d[i];
				if (city.substr(0, 1) != letter) {
					letter = city.substr(0, 1);
					html += "<h1>" + letter + "</h1>";
				}
				html += "<a href='#' class='list-group-item' data-city='city'>" + city + "</a>";
			}
			$("#main").html(html);
			$("[data-city$=city]").click(function (e) {
				e.preventDefault();
				go("#poses?city=" + this.text);
			});
		});
	}

	function go(hash) {
		history.pushState({}, null, hash);
		selector();
	}
	
	function fillPoses(city) {
		$("#head").addClass("hide");
		$("#poses").removeClass("hide");
		$("#map").removeClass("hide");
		$("#gmap").attr("src", "https://www.google.com/maps/embed/v1/place?key=AIzaSyBmrSOV51WWVbmOZPe-nyc-jFfkgFmLyng&q=" + city); 
		$.ajax({
			url: "BBRPoSes",
			data: {
				operation: "posList",
				city: city
			}
		}).done(function (data) {
			d = $.parseJSON(data);
			var html = "";
			var letter = "";
			for (i = 0; i < d.length; i++) {
				pos = d[i];
				if (city.substr(0, 1) != letter) {
					letter = pos.title.substr(0, 1);
					html += "<h1>" + letter + "</h1>";
				}
				html += "<a href='#' class='list-group-item' data-type='pos' data-pos='" + pos.id + "'>" + pos.title + "</a>";
			}
			$("#main").html(html);
			$("[data-type$=pos]").click(function () {
				window.location.href = "general-plan-visit.jsp?pos=" + $(this).attr("data-pos");
			});
		});
	}

</script>