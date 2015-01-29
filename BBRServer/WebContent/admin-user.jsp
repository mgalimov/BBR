<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-card-wrapper>
	<jsp:attribute name="title">
      User
    </jsp:attribute>
	<jsp:body>

		<!-- Modal -->
		<div class="modal fade" id="sureToCancelChanges" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="myModalLabel">Cancel changes confirmation</h4>
		      </div>
		      <div class="modal-body">
		      	 Are you sure to cancel changes? All your changes will be lost. 
	   		  </div>
		    <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">Keep editing</button>
		        <button type="button" class="btn btn-warning" id="cancelChanges">Cancel changes</button>
		    </div>
		    </div>
		  </div>
		</div>
	
		<div class="container"  id="editForm">
			<form class="form-horizontal">
		       	<div class="form-group">
					<label for="inputEmailSignUp" class="col-sm-4 control-label">Email address</label>
					<p class="col-sm-6" id="inputEmail">
						<!--input type="email" id="inputEmail" class="form-control-static" placeholder="Email address" required>-->
					</p>
				</div>
				<div class="form-group">	
					<label for="inputFirstName" class="col-sm-4 control-label">First Name</label>
					<div class="col-sm-6">
						<input type="text" id="inputFirstName" class="form-control"
					placeholder="First Name" required>
					</div>
				</div>
				<div class="form-group">
					<label for="inputLastName" class="col-sm-4 control-label">Last Name</label>
					<div class="col-sm-6">
						<input type="text" id="inputLastName" class="form-control"
					placeholder="Last Name" required>
					</div>
				</div>
				<div class="form-group">
					<label for="inputApprovedStatus" class="col-sm-4 control-label">Status</label>
					<div class="col-sm-6">
						<select class="form-control" id="inputApproved">
						  <option value="true">Approved</option>
						  <option value="false">Not yet approved</option>
						</select>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-6">
						<button type="button" class="btn btn-default" data-toggle="modal" data-target="#sureToCancelChanges">Cancel changes</button>
						<button type="submit" class="btn btn-primary" id="saveChanges">Save changes</button>
					</div>
				</div>
			</form>
		</div>

			<script>
			 	$(document).ready(function () {
			 		idParam = getUrlParameter('id');
					if (!idParam) {
						goBackOrTo('admin-users.jsp');
					} else
					{
						$.get('BBRUserUpdate', {id : idParam, operation : 'getdata'}, function(responseText) {
							obj = $.parseJSON(responseText);
							if (obj) {
								$('#inputEmail').text(obj.email);
								$('#inputFirstName').val(obj.firstName);
								$('#inputLastName').val(obj.lastName);
								$('#inputApproved').val(obj.approved.toString());
							};
						});
			 		}

					$('#saveChanges').click(function(event) { 
				 		idParam = getUrlParameter('id');
		                var firstNameString=$('#inputFirstName').val();
		                var lastNameString=$('#inputLastName').val();
		                var approvedString=$('#inputApproved').val();
		             	$.get('BBRUserUpdate',{id:idParam,firstName:firstNameString,lastName:lastNameString,approved:approvedString,operation:'update'},function(responseText) { });
						goBackOrTo('admin-users.jsp');
		            });			 		

					$('#cancelChanges').click(function(event) {
						goBackOrTo('admin-users.jsp');
					});
			 	});
			</script>			      
		</jsp:body>
</t:admin-card-wrapper>