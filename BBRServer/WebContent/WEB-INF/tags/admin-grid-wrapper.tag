<%@ tag language="java" pageEncoding="UTF-8" description="Admin Grid Wrapper"%>
<%@ attribute name="title" fragment="true" %>
<!--  http://stackoverflow.com/questions/1296235/jsp-tricks-to-make-templating-easier -->
<!DOCTYPE html>
<html>
	<head>
	    <meta charset="utf-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	    <meta name="viewport" content="width=device-width, initial-scale=1">
	    <title>
	    	<jsp:invoke fragment="title"/>
	    </title>
	
	    <!-- Bootstrap -->
	    <link href="css/bootstrap.min.css" rel="stylesheet">

		<!-- BBR Utils -->
	    <script src="js/bbr-utils.js"></script>
	
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
		
				<!--  PAGINATION plugin -->
		<link rel="stylesheet" type="text/css" href="css/jquery.bs_pagination.min.css">
		<script type="text/javascript" src="js/jquery.bs_pagination.min.js"></script>
		<script type="text/javascript" src="js/bs_pagination/localization/en.min.js"></script>
		 
		<!--  FILTERS plugin --> 
		<link rel="stylesheet" type="text/css" href="css/jquery.jui_filter_rules.bs.css">
		<script type="text/javascript" src="js/jquery.jui_filter_rules.js"></script>
		<script type="text/javascript" src="js/jui_filter_rules/localization/en.js"></script>
		<!--  required from filters plugin -->
		<script type="text/javascript" src="js/moment.min.js"></script>
		 
		<!--  DATAGRID plugin -->
		<link rel="stylesheet" type="text/css" href="css/jquery.bs_grid.min.css">
		<script type="text/javascript" src="js/jquery.bs_grid.js"></script>
		<script type="text/javascript" src="js/bs_grid/localization/en.js"></script>
  	</head>
  	<body>
  		<div class="containter">
  			<jsp:doBody/>
  		</div>
	</body>
</html>