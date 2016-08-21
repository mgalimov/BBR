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
	    	<c:out value="${context.gs(title)}"/>
	    </title>

		<link rel="icon" href="favicon.ico" type="image/x-icon">
		<link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
		 
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
	    <script src="js/jquery.cookie.js"></script>
	    <script src="js/bootstrap.min.js"></script>
		  
		<!-- SELECTIZE -->
		<script type="text/javascript" src="js/selectize.min.js"></script>
		<link rel="stylesheet" type="text/css" href="css/selectize.css" />

		<!-- DateTime Picker -->
		<link rel="stylesheet" type="text/css" href="css/bootstrap-datetimepicker.min.css" />
		<script type="text/javascript" src="js/moment-with-locales.min.js"></script>
		<script type="text/javascript" src="js/bootstrap-datetimepicker.min.js" ></script>
		
  		<!-- BBR Utils -->
	    <script src="js/bbr-utils.js"></script>
	    <link href="css/bbr-admin.css" rel="stylesheet">	
  	</head>
  	<body>
  		<div class="container-fluid">
  			<div class="row">
	  			<div class="col-sm-9 col-md-10 main">
	  				<jsp:doBody/>
	  			</div>
  			</div>
  		</div>
	</body>
</html>