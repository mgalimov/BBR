<%@ tag language="java" pageEncoding="UTF-8" description="Actions"
		import="BBRClientApp.BBRContext"%>

<% BBRContext context = BBRContext.getContext(request); %>
<div class="action-group">
	<div class="dropdown">
		<div class="btn-group" role="group" id="actionDiv">
		    <button type="button" class="btn btn-primary dropdown-toggle" 
		    		data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" id="actionBtnDropdown">
		      <%=context.gs("LBL_ACTION_CREATE_BTN") %>
		      <span class="caret"></span>
		    </button>
		    <ul class="dropdown-menu" aria-labbeledby="actionBtnDropdown" id="actionDropdown">
		   		<jsp:doBody/>
		    </ul>
		</div>
		<script>
			$(document).ready(function() {
				if ($("#actionDropdown").children().length == 0) {
					$("#actionDiv").addClass("hidden");
				}
			});
		</script>
	</div>
</div>