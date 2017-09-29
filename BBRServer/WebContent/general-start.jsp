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

<t:login-wrapper title="LBL_SELECT_POS_TITLE">
<jsp:body>

<body class="hold-transition login-page" style="background-image: url(bks/${bk}); background-repeat: no-repeat; background-size: cover; background-position: center;">
    <div class="login-box">
		<div class="login-box-body" style="background: rgba(255,255,255,0.5);">
			<div style="text-align: center; padding-bottom: 10px;">
				<img src="images/barbiny50px.png" />
			</div>
		</div>
		<div class="container-fluid">
			<h3 id="selection"></h3>
			<h3 id="head">${context.gs("LBL_START")}</h3>
			<div class="alert alert-warning hide" id="alertMessage">
	    		<button type="button" class="close" aria-label="Close" id="alertCloseBtn"><span aria-hidden="true">&times;</span></button>
	    		<div id="alertText"></div>
			</div> 
			<div class="row">
				<div class="col-md-6 col-xs-12 col-sm-12 col-lg-6" id="main">
					<div class="form-group">
						<label for="nameInput">${context.gs("LBL_YOUR_NAME")}</label>
						<input type="text" id="nameInput" class="form-control" required/>
					</div>
					<div class="form-group">
						<label for="lastNameInput">${context.gs("LBL_YOUR_LAST_NAME")}</label>
						<input type="text" id="lastNameInput" class="form-control" required/>
					</div>
					<div class="form-group">
						<label for="emailInput">${context.gs("LBL_YOUR_EMAIL")}</label>
						<input type="text" id="emailInput" class="form-control" required/>
					</div>
					<div class="form-group">
						<label for="passwordInput">${context.gs("LBL_YOUR_PASSWORD")}</label>
						<input type="password" id="passwordInput" class="form-control" required/>
					</div>
					<div class="form-group">
						<label for="passwordRepeatInput">${context.gs("LBL_YOUR_PASSWORD_REPEAT")}</label>
						<input type="password" id="passwordRepeatInput" class="form-control" required/>
					</div>
					<a href="#" class="btn btn-primary" id="nextBtn">${context.gs("BTN_NEXT")}</a>
				</div>
				<div class="col-md-6 col-xs-12 col-sm-12 col-lg-6" id="shop">
					<div class="form-group">
						<label for="shopInput">${context.gs("LBL_YOUR_SHOP_NAME")}</label>
						<input type="text" id="shopInput" class="form-control" required/>
					</div>
					<div class="form-group">
						<label for="countryInput">${context.gs("LBL_YOUR_COUNTRY")}</label>
						<input type="text" id="countryInput" class="form-control" required/>
					</div>
					<div class="form-group">
						<label for="currencyInput">${context.gs("LBL_YOUR_CURRENCY")}</label>
						<input type="text" id="currencyInput" class="form-control" required/>
					</div>
					<div class="form-group">
						<label for="posInput">${context.gs("LBL_YOUR_POS_NAME")}</label>
						<input type="text" id="posInput" class="form-control" required/>
					</div>
					<div class="form-group">
						<label for="cityInput">${context.gs("LBL_YOUR_POS_CITY")}</label>
						<input type="text" id="cityInput" class="form-control" required/>
					</div>
					<div class="form-group">
						<label for="latInput">${context.gs("LBL_YOUR_POS_LOCATION_LAT")}</label>
						<input type="text" id="latInput" class="form-control" required/>
					</div>
					<div class="form-group">
						<label for="lonInput">${context.gs("LBL_YOUR_POS_LOCATION_LON")}</label>
						<input type="text" id="lonInput" class="form-control" required/>
					</div>
					<div class="form-group">
						<label for="tzInput">${context.gs("LBL_YOUR_POS_TZ")}</label>
						<input type="text" id="tzInput" class="form-control" required/>
					</div>
					<div class="form-group">
						<label for="urlIDInput">${context.gs("LBL_YOUR_URL_ID")}</label>
						<input type="text" id="urlIDInput" class="form-control" required/>
					</div>
					<a href="#" class="btn btn-primary hide" id="finishBtn">${context.gs("BTN_FINISH")}</a>
				</div>
				<div class="col-md-4 col-xs-12 col-sm-12 col-lg-4" id="map" style="height: 400px">
				</div>
			</div>
		</div>
	</div>
	</body>
	</jsp:body>
</t:login-wrapper>

<script async defer
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBmrSOV51WWVbmOZPe-nyc-jFfkgFmLyng&callback=initMap">
</script>

<script>
	var name, lastName, email, password, passwordRepeat;
	var shop, posName, posCity, posLat, posLon, posTZ, urlID, country, currency;
	var map;

	$(document).ready(function () {
 		window.addEventListener('popstate', function(e){
 			selector();
 		}, false);

 		$("#nextBtn").click(function() {
			$("#alertMessage").addClass("hide");
 			$("#nextBtn").addClass("disable");
 			name = $("#nameInput").val();
 			name = $("#lastNameInput").val();
 			email = $("#emailInput").val();
 			password = $("#passwordInput").val();
 			passwordRepeat = $("#passwordRepeatInput").val();
 			$.get("BBRSignUp", {
 				operation: "test",
 				email: email,
 				password: password,
 				passwordRepeat: passwordRepeat
 			}).done(function () {
 	 			go("#shop");
 			}).fail(function (data) {
				$("#alertMessage").html(data.responseText); 				
				$("#alertMessage").removeClass("hide");
				$("#nextBtn").removeClass("disable");
 			});
 		});
	
 		$("#finishBtn").click(function () {
 			$("#alertMessage").addClass("hide");
 			shop = $("#shopInput").val();
			country = $("#countryInput").val();
			currency = $("#currencyInput").val();
 			posName = $("#posInput").val();
 			posCity = $("#cityInput").val();
 			posLat = $("#latInput").val();
 			posLon = $("#lonInput").val();
 			posTZ = $("#tzInput").val();
			urlID = $("#urlIDInput").val();
 			$.get("BBRSignUp", {
 				operation: "create",
 				name: name,
 				lastName: name,
 				email: email,
 				password: password,
 				passwordRepeat: passwordRepeat,
 				shop: shop,
				country: country,
				currency: currency,
				urlID: urlID,
				pos: pos,
				city: city,
				lat: lat,
				lon: lon,
				tz: tz
 			}).done(function () {
 	 			go("general-signin.jsp");
 			}).fail(function (data) {
				$("#alertMessage").html(data.responseText); 				
				$("#alertMessage").removeClass("hide");
				$("#nextBtn").removeClass("disable");
 			});
 		});
 		
 		$("#posInput").change(function () {
 			if (!posName || urlID == convertUrlID(posName)) {
 				newUrlID = convertUrlID($("#posInput").val());
 				$("#urlIDInput").val(newUrlID);
 			}
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
        map = new google.maps.Map(document.getElementById('map'), {});
    }

</script>