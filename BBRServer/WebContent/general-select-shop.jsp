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
		<h3 id="selection"></h3>
		<h3 id="head">${context.gs("LBL_SELECT_CITY")}</h3> 
		<div class="row">
			<div class="col-md-6 col-xs-12 col-sm-12 col-lg-6" id="main">
			</div>
			<div class="col-md-4 col-xs-12 col-sm-12 col-lg-4" id="map" style="height: 400px">
			</div>
		</div>
	</div>
</jsp:body>
</t:light-wrapper>

<script async defer
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBmrSOV51WWVbmOZPe-nyc-jFfkgFmLyng&callback=initMap">
</script>

<script>
	var cityName, posObj;
	var poses = [];
	var map;

	$(document).ready(function () {
 		window.addEventListener('popstate', function(e){
 			selector();
 		}, false);

		selector();
	});
	
	function selector() {
		var hash = location.hash;
		if (hash == "" || hash == "#" || !cityName)
			fillCities();
		else if (hash.substr(0, 6) == "#poses")
			fillPoses();	
		else if (hash.substr(0, 4) == "#pos")
			fillPos();
			
	}

	function go(hash) {
		history.pushState({}, null, hash);
		selector();
	}

	function fillCities() {
		$("#head").removeClass("hide");
		$("#map").addClass("hide");
		$("#pos").addClass("hide");
		$("#selection").html("");
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
					html += "<h2>" + letter + "</h2>";
				}
				html += "<a href='#' class='list-group-item' data-type='city'>" + city + "</a>";
			}
			$("#main").html(html);
			$("[data-type$=city]").click(function (e) {
				e.preventDefault();
				cityName = this.text;
				go("#poses");
			});
		});
	}
	
	function fillPoses() {
		$("#head").addClass("hide");
		$("#map").removeClass("hide");
		$("#pos").addClass("hide");
		$("#selection").html("<a href='#'>" + cityName + "</a>");
		$.ajax({
			url: "BBRPoSes",
			data: {
				operation: "posList",
				city: cityName
			}
		}).done(function (data) {
			d = $.parseJSON(data);
			var html = "";
			var letter = "";
			bounds = new google.maps.LatLngBounds();
			poses = [];
			for (i = 0; i < d.length; i++) {
				pos = d[i];
				poses.push(pos);
				if (pos.title.substr(0, 1) != letter) {
					letter = pos.title.substr(0, 1);
					html += "<h2>" + letter + "</h2>";
				}
				html += "<a href='#pos' class='list-group-item' data-type='pos' data-id='" + pos.id + "'>" + pos.title + "</a>";
		        marker = new google.maps.Marker({
		            position: {lat: pos.lat*1, lng: pos.lng*1},
		            map: map,
		            title: pos.title
		          });
		        marker.metadata = {posObj: pos};
		        marker.addListener('click', markerClick);
		        bounds.extend(marker.position);
			}
			
			if (d.length > 0) {
				google.maps.event.trigger(map, 'resize');
				if (d.length == 1) {
					pos = d[0];
					map.setCenter({lat: pos.lat*1, lng: pos.lng*1});
					map.setZoom(15);
				} else
					map.fitBounds(bounds);
				if (map.zoom > 15)
					map.setZoom(15);
				
			}
				
			html += "<br/><br/>";
			$("#main").html(html);
			$("[data-type$=pos]").click(function (e) {
				e.preventDefault();
				posId = $(this).attr("data-id");
				posObj = null;
				for (i = 0; i <= poses.length; i++)
					if (poses[i].id == posId) {
						posObj = poses[i];
						break;
					}
				go("#pos");
			});
		});
	}
	
	function markerClick() {
		posObj = this.metadata.posObj;
		fillPos();
	}
	
	function fillPos() {
		$("#head").addClass("hide");
		$("#map").removeClass("hide");
		$("#pos").removeClass("hide");
		$("#selection").html("<a href='#'>" + cityName + "</a> / <a href='#poses'>" + posObj.title + "</a>");
		html = posObj.locationDescription.replace("[b]", "<b>")
   			 .replace("[/b]","</b>")
   			 .replace("[u]", "<u>")
   			 .replace("[/u]","</u>")
   			 .replace("[red]", "<font color='red'>")
   			 .replace("[/red]","</font>")
   			 .replace("[green]", "<font color='green'>")
   			 .replace("[/green]","</font>")
   			 .replace("[i]", "<i>")
   			 .replace("[/i]","</i>")
   			 .replace("[br]", "<br/>");
		html += "<br/>" + posObj.startWorkHour + " &#151; " + posObj.endWorkHour;
		html += "<br/><br/>";
		html += "<a href='#' class='btn btn-default'>${context.gs("BTN_START_OVER")}</a>&nbsp;";
		html += "<a href='general-plan-visit.jsp?pos=" + posObj.id + "' class='btn btn-primary'>${context.gs("BTN_GO_ON")}</a>";
		html += "<br/><br/>";
		$("#main").html(html);
		google.maps.event.trigger(map, 'resize');
		map.setCenter({lat: posObj.lat*1, lng: posObj.lng*1});
		map.setZoom(15);
	}
	
	function initMap() {
        map = new google.maps.Map(document.getElementById('map'), {});
    }

</script>