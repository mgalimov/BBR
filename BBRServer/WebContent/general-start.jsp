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
<%@page import="java.util.TimeZone"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% 
	BBRContext context = BBRContext.getContext(request);
	int picNum = (int)(Math.ceil(Math.random() * 16) + 1);
	String[] timeZones = TimeZone.getAvailableIDs();
	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < timeZones.length; i++) {
		sb.append("{text: '" + timeZones[i] +"', value: '" + timeZones[i] +"'},\n");
	}
	sb.deleteCharAt(sb.length()-2);
	
	request.setAttribute("bk", String.format("%03d", picNum) + ".jpg");
	request.setAttribute("timeZones", sb.toString());
%>

<t:login-wrapper title="LBL_SELECT_POS_TITLE">
<jsp:body>
<!-- SELECTIZE -->
<script type="text/javascript" src="js/selectize.js"></script>
<link rel="stylesheet" type="text/css" href="css/selectize.css" />

<body class="hold-transition login-page" style="background-image: url(bks/${bk}); background-repeat: no-repeat; background-size: cover; background-position: center;">
    <div class="center-box">
		<div class="login-box-body" style="background: rgba(255,255,255,0.7);">
			<div style="text-align: center; padding-bottom: 10px;">
				<img src="images/barbiny50px.png" />
			</div>
			<h3 id="selection"></h3>
			<h3 id="head">${context.gs("LBL_START")}</h3>
			<div class="alert alert-warning hide" id="alertMessage">
	    		<button type="button" class="close" aria-label="Close" id="alertCloseBtn"><span aria-hidden="true">&times;</span></button>
	    		<div id="alertText"></div>
			</div> 
			<div id="main">
				<div class="form-group">
<%-- 					<label for="nameInput">${context.gs("LBL_YOUR_NAME")}</label> --%>
					<input type="text" id="nameInput" class="form-control" placeholder='${context.gs("LBL_YOUR_NAME")}' required/>
				</div>
				<div class="form-group">
<%-- 					<label for="lastNameInput">${context.gs("LBL_YOUR_LAST_NAME")}</label> --%>
					<input type="text" id="lastNameInput" class="form-control" placeholder='${context.gs("LBL_YOUR_LAST_NAME")}' required/>
				</div>
				<div class="form-group">
<%-- 					<label for="emailInput">${context.gs("LBL_YOUR_EMAIL")}</label> --%>
					<input type="email" id="emailInput" class="form-control" placeholder='${context.gs("LBL_YOUR_EMAIL")}' required/>
				</div>
				<div class="form-group">
<%-- 					<label for="passwordInput">${context.gs("LBL_YOUR_PASSWORD")}</label> --%>
					<input type="password" id="passwordInput" class="form-control" placeholder='${context.gs("LBL_YOUR_PASSWORD")}' required/>
				</div>
				<div class="form-group">
<%-- 					<label for="passwordRepeatInput">${context.gs("LBL_YOUR_PASSWORD_REPEAT")}</label> --%>
					<input type="password" id="passwordRepeatInput" class="form-control" placeholder='${context.gs("LBL_YOUR_PASSWORD_REPEAT")}'required/>
				</div>
				<a href="#" class="btn btn-primary pull-right" id="nextBtn">${context.gs("BTN_NEXT")}</a>
				<div class="row">
				</div>
			</div>
			<div id="shop">
				<div class="form-group">
<%-- 					<label for="shopInput">${context.gs("LBL_YOUR_SHOP_NAME")}</label> --%>
					<input type="text" id="shopInput" class="form-control" placeholder='${context.gs("LBL_YOUR_SHOP_NAME")}' required/>
				</div>
				<div class="form-group">
<%-- 					<label for="countryInput">${context.gs("LBL_YOUR_COUNTRY")}</label> --%>
					<input type="text" id="countryInput" class="form-control" placeholder='${context.gs("LBL_YOUR_COUNTRY")}' required/>
				</div>
				<div class="form-group">
<%-- 					<label for="currencyInput">${context.gs("LBL_YOUR_CURRENCY")}</label> --%>
					<input type="text" id="currencyInput" class="form-control" placeholder='${context.gs("LBL_YOUR_CURRENCY")}' required/>
				</div>
				<div class="form-group">
<%-- 					<label for="posInput">${context.gs("LBL_YOUR_POS_NAME")}</label> --%>
					<input type="text" id="posInput" class="form-control" placeholder='${context.gs("LBL_YOUR_POS_NAME")}' required/>
				</div>
				<div class="form-group">
<%-- 					<label for="cityInput">${context.gs("LBL_YOUR_POS_CITY")}</label> --%>
					<input type="text" id="cityInput" class="form-control" placeholder='${context.gs("LBL_YOUR_POS_CITY")}' required/>
				</div>
				<div class="form-group">
<%-- 					<label for="tzInput">${context.gs("LBL_YOUR_POS_TZ")}</label> --%>
					<select id="tzInput" class="selectize" required></select>
				</div>
				<div class="form-group">
<%-- 					<label for="urlIDInput">${context.gs("LBL_YOUR_URL_ID")}</label> --%>
					<input type="text" id="urlIDInput" class="form-control" placeholder='${context.gs("LBL_YOUR_URL_ID")}' required/>
				</div>
				<div class="alert alert-info" id="alertUrlID">
	    			<div id="alertUrlIDText">www.barbiny.ru/book/URL</div>
				</div> 
<%-- 				<label for="latInput">${context.gs("LBL_YOUR_POS_LOCATION")}</label> --%>
				<form role="form" class="form-inline">
					<div class="form-group">
						<input type="text" id="latInput" class="form-control" style="width: 200px" placeholder='${context.gs("LBL_LOC_LAT")}' required/>
					</div>
					<div class="form-group">
						<input type="text" id="lonInput" class="form-control" style="width: 200px" placeholder='${context.gs("LBL_LOC_LONG")}' required/>
					</div>
					<a href="#map" class="btn btn-info pull-right" id="mapBtn">${context.gs("BTN_MAP")}</a>
				</form>
				<div>&nbsp;</div>
				<a href="#finish" class="btn btn-primary pull-right hide" id="finishBtn">${context.gs("BTN_FINISH")}</a>
				</div>
				<div class="row">
				</div>
			</div>
	</div>
	<div class="center-box-map" style="display: none; z-index: 1000" id="mapbox">
		<div id="mapCtrl">
			<a href="#shop" class="btn btn-default pull-right" id="closeMapBtn" style="z-index: 1">
				<span class="glyphicon glyphicon-remove"></span> ${context.gs("BTN_CLOSE_MAP")}
			</a>
			&nbsp;&nbsp;
		</div>
		<div style="height: 100%; width: 100%;" id="map"></div>
	</div>
</body>

</jsp:body>
</t:login-wrapper>

<script defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBmrSOV51WWVbmOZPe-nyc-jFfkgFmLyng&callback=initMap" >
</script>

<script>
	var timeZones = [${timeZones}];
	var name, lastName, email, password, passwordRepeat;
	var shop, posName, posCity, posLat, posLon, posTZ, urlID, country, currency;
	var map;
	var ll;

	$(document).ready(function () {		
 		window.addEventListener('popstate', function(e){
 			selector();
 		}, false);
 		
 		$("#nextBtn").click(function() {
			$("#alertMessage").addClass("hide");
 			$("#nextBtn").addClass("disable");
 			name = $("#nameInput").val();
 			lastName = $("#lastNameInput").val();
 			email = $("#emailInput").val();
 			password = $("#passwordInput").val();
 			passwordRepeat = $("#passwordRepeatInput").val();
 			$.get("BBRSignUp", {
 				operation: "test",
 				email: email,
 				name: name,
 				lastName: lastName,
 				password: password,
 				passwordRepeat: passwordRepeat
 			}, function () {
 	 			go("#shop");
 			}).fail(function (data) {
				$("#alertMessage").html(data.responseText); 				
				$("#alertMessage").removeClass("hide");
				$("#nextBtn").removeClass("disable");
 			});
 		});
	
 		$("#finishBtn").click(function () {
 			$("#alertMessage").addClass("hide");
 			name = $("#nameInput").val();
 			lastName = $("#lastNameInput").val();
 			email = $("#emailInput").val();
 			password = $("#passwordInput").val();
 			passwordRepeat = $("#passwordRepeatInput").val();

 			shop = $("#shopInput").val();
			country = $("#countryInput").val();
			currency = $("#currencyInput").val();
 			posName = $("#posInput").val();
 			posCity = $("#cityInput").val();
 			posLat = $("#latInput").val();
 			posLon = $("#lonInput").val();
 			posCity = $("#cityInput").val();
 			posTZ = $("#tzInput").val();
			urlID = $("#urlIDInput").val();
 			$.get("BBRSignUp", {
 				operation: "create",
 				name: name,
 				lastName: lastName,
 				email: email,
 				password: password,
 				passwordRepeat: passwordRepeat,
 				shop: shop,
				country: country,
				currency: currency,
				urlID: urlID,
				pos: posName,
				city: posCity,
				lat: posLat,
				lon: posLon,
				tz: posTZ
 			}, function () {
 				$.get("BBRSignIn", {
 					email: email, 
 					password: password
 				}, function() {
 	 	 			window.location.href = "general-signin.jsp";
 				}).fail(function () {
 					window.location.href = "general-signin.jsp";
 				})
 			}).fail(function (data) {
				$("#alertMessage").html(data.responseText); 				
				$("#alertMessage").removeClass("hide");
				$("#nextBtn").removeClass("disable");
 			});
 		});
 		
 		$("#mapBtn").click(function () {
 			$("#mapbox").css("display", "block");
			$(document).keyup(function (e) {
 				if (e.keyCode == 27)
 					$("#closeMapBtn").click();
 			});
 			
 			$.get("http://maps.googleapis.com/maps/api/geocode/json?address="+$("#countryInput").val()+","+$("#cityInput").val(), function (r) {
 					if (r && r.results && r.results[0]) {
 						lat = r.results[0].geometry.location.lat * 1;
 						lon = r.results[0].geometry.location.lng * 1;
 						ll = new google.maps.LatLng(lat, lon);
 	 		 			map.panTo(ll);
 	 		 			map.setZoom(8);
 					}
 		 			google.maps.event.trigger(map, 'resize');
 				})
 				.fail(function () {
 					google.maps.event.trigger(map, 'resize');
 				});
 		});
 		
 		$("#closeMapBtn").click(function () {
 			$("#mapbox").css("display", "none");
			$(document).keyup(null);
 		});
 		
 		$("#posInput").change(function () {
 			if (!posName || urlID == convertUrlID(posName)) {
 				newUrlID = convertUrlID($("#posInput").val());
 				$("#urlIDInput").val(newUrlID);
 				$("#urlIDInput").change();
 			}
 		});
 		
 		$("#urlIDInput").change(function () {
 			var h = "www.barbiny.ru/book/"+$("#urlIDInput").val();
 			$("#alertUrlIDText").html("<a href='http://" + h + "'>" + h + "</a>");
 		});
 		
 		$("#cityInput").change(function () {
 			if ($("#tzInput").val() == "") {
 				$.get("http://maps.googleapis.com/maps/api/geocode/json?address="+$("#countryInput").val()+","+$("#cityInput").val(), function (r) {
 					if (r && r.results && r.results[0]) {
 						lat = r.results[0].geometry.location.lat * 1;
 						lon = r.results[0].geometry.location.lng * 1;
		 	 			findTZ(lat, lon);
		 	 		}
 				});
 			}
 		});
 		
 		$("#tzInput").selectize({
 			options: timeZones,
 			placeholder: '${context.gs("LBL_YOUR_POS_TZ")}'
 		});
 		
		selector();
	});
	
	function selector() {
		var hash = location.hash;
		if (hash == "" || hash == "#" || email == null || email == "")
			fillAccountInfo();
		else if (hash.substr(0, 6) == "#shop")
			fillShopInfo();	
	}

	function go(hash) {
		history.pushState({}, null, hash);
		selector();
	}

	function fillAccountInfo() {
		$("#shop").addClass("hide");
		$("#map").addClass("hide");
		$("#main").removeClass("hide");
		$("#nextBtn").removeClass("disable");
		$("#nextBtn").removeClass("hide");
		$("#finishBtn").addClass("hide");
	}
	
	function fillShopInfo() {
		$("#main").addClass("hide");
		$("#shop").removeClass("hide");
		$("#map").removeClass("hide");
		$("#finishBtn").removeClass("disable");
		$("#nextBtn").addClass("hide");
		$("#finishBtn").removeClass("hide");
	}
	
	function initMap() {
		if (!map) {
			if (!ll)
				ll = new google.maps.LatLng(0, 0);
			
	        map = new google.maps.Map(document.getElementById('map'), {
	            center: ll,
	            zoom: 8
	        });
	        
	        marker = new google.maps.Marker({
	            position: ll,
	            map: map
	          });
	    	
	    	map.addListener("click", function (event) {
	    	    var lat = event.latLng.lat();
	    	    var lon = event.latLng.lng();
	    	    ll = new google.maps.LatLng(lat,lon);
	    	    marker.setPosition(ll);
	    	    $("#latInput").val(lat);
	    	    $("#lonInput").val(lon);
				findTZ(lat, lon);
	    	});
			
	    	map.controls[google.maps.ControlPosition.RIGHT_TOP].push(document.getElementById("mapCtrl"));
		}
	}		

	function findTZ(lat, lon) {
	    var ds = new Date();
	    var timestamp = ds.getTime() / 1000;
	    $.get("https://maps.googleapis.com/maps/api/timezone/json?location="+lat+","+lon+"&timestamp="+timestamp+"&key=AIzaSyBmrSOV51WWVbmOZPe-nyc-jFfkgFmLyng", function (data) {
	    	var tzi = $("#tzInput")[0].selectize;
	    	tzi.clear();
	    	tzi.addItem(data.timeZoneId);
	    	tzi.refreshItems();
	    });
	}
</script>