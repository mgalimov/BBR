<%@ tag language="java" pageEncoding="UTF-8" description="General Wrapper"%>
<%@ attribute name="title" required="true" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
	<head>
	    <meta charset="utf-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	    <meta name="viewport" content="width=device-width, initial-scale=1">
	    <title>
	    	<c:out value="${title}"/>
	    </title>

	    <!-- Bootstrap -->
	    <link href="css/bootstrap.min.css" rel="stylesheet">
	
	    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
	    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
	    <!--[if lt IE 9]>
	      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
	      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
	    <![endif]-->
	
	    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	    <script src="js/jquery-1.11.2.min.js"></script>
	    <!-- Include all compiled plugins (below), or include individual files as needed -->
	    <script src="js/bootstrap.min.js"></script>
	  
		<!--  JQUERY-UI (only sortable and datepicker is needed) -->
		<link rel="stylesheet" type="text/css" href="css/jquery-ui.min.css">
		<script type="text/javascript" src="js/jquery-ui.min.js"></script>
		<!--  if timepicker is used in filters -->
		
		<link rel="stylesheet" type="text/css" href="css/jquery-ui-timepicker-addon.min.css"/>
		<script type="text/javascript" src="js/jquery-ui-timepicker-addon.min.js"></script>
		<!--  if touch event support is needed (mobile devices) -->
		<script type="text/javascript" src="js/jquery.ui.touch-punch.min.js"></script>

		<!-- SELECTIZE -->
		<script type="text/javascript" src="js/selectize.js"></script>
		<link rel="stylesheet" type="text/css" href="css/selectize.css" />
		  
  		<!-- BBR Utils -->
	    <script src="js/bbr-utils.js"></script>
	    <link href="css/bbr-admin.css" rel="stylesheet">	
  	</head>
  	<body>
		<t:top-menu title="${title}"/>
  		<div class="container-fluid">
  			<div class="row">
	  	  		<t:side-menu />
	  			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
	  				<jsp:doBody/>
	  			</div>
  			</div>
  		</div>
	</body>
</html>