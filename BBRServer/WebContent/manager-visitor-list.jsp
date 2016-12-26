<%@ page import="BBRClientApp.BBRContext"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:wrapper title="LBL_VISITORS_TITLE">
	<jsp:body>
		<t:grid method="BBRVisitors" editPage="manager-visitor-edit.jsp" 
				createPage="" title="LBL_VISITORS_TITLE" 
				customToolbar="true">
			<t:toolbar-group>
				<t:toolbar-item label="LBL_OPEN_BTN" id="edit" accent="btn-info" icon="glyphicon-open"/>
			</t:toolbar-group>
			<t:toolbar-group>
				<t:toolbar-item label="LBL_LAST_30_DAYS" id="view30Days" icon="glyphicon-flash"/>
				<t:toolbar-item label="LBL_LAST_120_DAYS" id="view120Days" icon="glyphicon-star"/>
				<t:toolbar-item label="LBL_LAST_360_DAYS" id="view360Days" icon="glyphicon-time"/>
				<t:toolbar-item label="LBL_VIEW_ALL_VISITORS" id="viewAll" icon="glyphicon-tasks"/>
				<div class='form-inline'>
					<div class="input-group">
						<input class="form-control" type="text" id="visitorContacts" placeholder='${context.gs("LBL_CONTACT_INFO")}'/>
						<span class="input-group-btn">
							<button class="btn btn-default" id="searchContactsBtn"><span class='glyphicon glyphicon-search' aria-hidden='true'></span></button>
						</span>
					</div>
				</div>
			</t:toolbar-group>
			<t:grid-item label="LBL_USER_NAME" field="userName"/>
			<t:grid-item label="LBL_CONTACT_INFO" field="userContacts"/>
			<t:grid-item label="LBL_VISIT_COUNT" field="visitCount" sort="desc"/>
		</t:grid>
		
		<script>
		$(document).ready(function() {
			$("#view30Days").on("click", function() {
				$("#visitorContacts").val("");
				clickHandler('toggle30Days', "#view30Days");
			});

			$("#view120Days").on("click", function() {
				$("#visitorContacts").val("");
				clickHandler('toggle120Days', "#view120Days");
			});

			$("#view360Days").on("click", function() {
				$("#visitorContacts").val("");
				clickHandler('toggle360Days', "#view360Days");
			});

			$("#viewAll").on("click", function() {
				$("#visitorContacts").val("");
				clickHandler('toggleAll', "#viewAll");
			});
			
			$("#searchContactsBtn").on("click", function() {
				searchContact();
			});
			
			$("#visitorContacts").keypress(function(e) {
				if (e.which == 13)
					searchContact();
			});
			
			$("#view30Days").click();
		});
		
		function clickHandler(operation, btn) {
			$.ajax({
	        	url: 'BBRVisitors',
	        	data: {
	        		operation: operation
	        	}
	        }).success(function (d) {
	        	table = $("#grid").DataTable();
	        	table.ajax.reload();
	        	table.draw();
	        	$(".active").removeClass("active");
	        	$(btn).addClass("active");			
	        });			
		}
		
		function searchContact() {
			var contacts = $("#visitorContacts").val();
			if (contacts.trim() != "")
				$.ajax({
		        	url: 'BBRVisitors',
		        	data: {
		        		operation: 'searchContacts',
		        		contacts: contacts
		        	}
		        }).success(function (d) {
		        	table = $("#grid").DataTable();
		        	table.ajax.reload();
		        	table.draw();
		        	$(".active").removeClass("active");
		        	$(btn).addClass("active");			
		        });			

		}

		</script>
	</jsp:body>
</t:wrapper>
