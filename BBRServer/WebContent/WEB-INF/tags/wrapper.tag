<%@ tag language="java" pageEncoding="UTF-8" description="Admin Grid Wrapper"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ attribute name="title" %>
<%@ attribute name="titleModifier" %>

<!DOCTYPE html>
<html>
	<head>
	    <meta charset="utf-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	    <title>
	    	<c:out value="${context.gs(title)}"/>
	    	<c:out value="${titleModifier}"/>
	    </title>

		<link rel="icon" href="favicon.ico" type="image/x-icon">
		<link rel="shortcut icon" href="favicon.ico" type="image/x-icon">

	    <!-- Bootstrap -->
	    <link href="css/bootstrap.min.css" rel="stylesheet">
  	    <!-- Font Awesome -->
	    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
	    <!-- Ionicons -->
	    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
	
	    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	    <script src="js/jquery-1.11.2.min.js"></script>
	    <script src="js/jquery.cookie.js"></script>
	    <script src="js/bootstrap.min.js"></script>
	    <script src="js/jquery.json.min.js"></script>
	    
		<!-- SELECTIZE -->
		<script type="text/javascript" src="js/selectize.js"></script>
		<link rel="stylesheet" type="text/css" href="css/selectize.css" />

		<!-- DateTime Picker -->
		<link rel="stylesheet" type="text/css" href="css/bootstrap-datetimepicker.min.css" />
		<script type="text/javascript" src="js/moment-with-locales.min.js"></script>
		<script type="text/javascript" src="js/bootstrap-datetimepicker.min.js" ></script>

		<!--  DateRange Picker -->
		<script type="text/javascript" src="js/daterangepicker.js"></script>
		<link rel="stylesheet" type="text/css" href="css/daterangepicker.css" />
	  
		<!-- BBR Utils -->
	    <script src="js/bbr-utils.js"></script>
	    <link href="css/bbr-admin.css" type="text/css" rel="stylesheet">
		 
		<!-- Datagrid -->
		<link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap.css">
		<script type="text/javascript" src="js/jquery.dataTables.js"></script>
		<script type="text/javascript" src="js/dataTables.bootstrap.js"></script>
		
		<!-- Google Charts -->
		<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
		
		<!-- Nav Menu -->
		<link rel="stylesheet" href="css/jasny-bootstrap.min.css">
		<script src="js/jasny-bootstrap.min.js"></script>
		
		<!-- File Upload -->
		<link rel="stylesheet" href="css/fileinput.min.css">
		<script src="js/fileinput.min.js"></script>
		
		<!-- Admin LTE -->
		<link rel="stylesheet" href="css/AdminLTE.css">
		<link rel="stylesheet" href="css/_all-skins.min.css">
		<script src="js/app.js"></script>
		<script src="js/jquery.slimscroll.min.js"></script>
  	</head>
  	<body class="skin-green fixed">
   		<div class="wrapper">
	 		<t:top-menu  title="${context.gs('LBL_CONTROL_PANEL')} ${context.gs(title)} ${titleModifier}"/>
 			<t:side-menu /> 
  			<div class="content-wrapper">
	  			<div class="main">
	  				<jsp:doBody/>
	  			</div>
  			</div>
  		</div>
	</body>
</html>