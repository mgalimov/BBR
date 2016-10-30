<%@ tag language="java" pageEncoding="UTF-8" description="Grid"%>
<%@ attribute name="title" required="true"%>
<%@ attribute name="titleModifier" %>
<%@ attribute name="method" required="true"%>
<%@ attribute name="editPage" required="true"%>
<%@ attribute name="createPage" required="true"%>
<%@ attribute name="customToolbar" %>
<%@ attribute name="standardFilters" %>
<%@ attribute name="standardFiltersShopsOnly" %>
<%@ attribute name="paging"%>

<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<c:set var="items" scope="request" value="{data: \"id\", visible: false}"/>
<c:set var="itemsHF" scope="request" value="<th>ID</th>"/>
<c:set var="sorting" scope="request" value=""/>
<c:set var="index" scope="request" value="1"/>
<c:set var="itemToolbar" scope="request" value="${''}"/>
<c:set var="filterStartDate" scope="request" value="${context.getFilterStartDate()}" />
<c:set var="filterEndDate" scope="request" value="${context.getFilterEndDate()}" />

<c:if test="${!paging.equals('false')}">
	<c:set var="paging" value="true"/>
</c:if>



<!-- http://www.onjava.com/pub/a/onjava/excerpt/jserverpages3_ch11/ -->

<h3>${context.gs(title).concat(titleModifier)}</h3>
		
<t:modal  cancelButtonLabel="LBL_GRID_CONFIRM_DELETION_CANCEL_BTN" 
		  processButtonLabel="LBL_GRID_CONFIRM_DELETION_CONFIRM_BTN" 
		  title="LBL_GRID_CONFIRM_DELETION_TITLE" 
		  id="sureToDelete">
	${context.gs('MSG_GRID_CONFIRM_DELETION')} 
</t:modal>

<div class="alert alert-warning alert-dismissable hide" id="alertMessage">
    <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <div id="alertText"></div>
</div>

<div class="panel">
  <div class="panel-heading" id="toolbar">
  	<div class="btn-toolbar" role="toolbar">
		  <span id="toolbarpanel">
		  	  <c:if test="${customToolbar != true}">
		  	  	<div class='btn-group' role='group'>
					<button type="button" class="btn btn-default" id="create">
					  <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> ${context.gs('LBL_GRID_CREATE_RECORD_BTN')}
					</button>
					<button type="button" class="btn btn-info" id="edit">
					  <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span> ${context.gs('LBL_GRID_EDIT_RECORD_BTN')}
					</button>
					<button type="button" class="btn btn-warning" id="delete">
					  <span class="glyphicon glyphicon-trash" aria-hidden="true"></span> ${context.gs('LBL_GRID_DELETE_RECORD_BTN')}
					</button>
				</div>
			  </c:if>
		  </span>
    	  <c:if test="${standardFilters != false}">
    	  <span class="pull-right">
			   	<button type="button" class="btn btn-default" data-toggle="collapse" data-target="#filterPanel" aria-collapsed="true">
				    <span class="glyphicon glyphicon-filter" aria-hidden="true"></span> <span class="caret"></span>
	  			</button>
		  </span>
		  </c:if>

	  </div>
  </div>
  
 <c:if test="${standardFilters != false}">
  <div class="collapse filterPanel" id="filterPanel">
  	<div class="pull-right">
	  	<form class="form-inline">
	  		<t:select-shop-pos field="shoppos" isShopsOnly="${standardFiltersShopsOnly}"/>&nbsp;
	  		<input type='text' class='form-control' id='periodPicker'/>
	  		<button type="button" class="btn btn-primary" id="applyFilters">
			  	${context.gs('LBL_DATERANGE_APPLY_BTN')}
			</button>
	  	</form>
	 </div>
  </div>
 </c:if>
  
  <div class="panel-body">
	  <table id="grid" class="table table-stripped table-bordered no-footer noselection">
			<jsp:doBody/>
			<thead>
				<tr>
					<c:out value="${itemsHF}" escapeXml="false"/>
				</tr>
			</thead>
	  </table>
  </div>
</div>

<c:if test="${customToolbar == true}">
<script>
	$(document).ready(function() {
		$('#toolbarpanel').html("${itemToolbar}");
	});
</script>
</c:if>

<script>
	$(document).ready(function() {
		$('#create').click(
				function(event) {
					window.location.href = '${createPage}?id=new';
				});
		
		$('#edit').click(
				function(event) {
					var row = table.row('.success');
					if (row.length > 0)
						window.location.href = '${editPage}?id=' + row.data().id;
				});

		$('#delete').click(
				function(event) {
					var row = table.row('.success');
					if (row.length > 0)
						$('#sureToDelete').modal();
				});

		$('#sureToDeleteprocess').click(
				function(event) {
					$('#sureToDeleteprocess').prop('disabled', true);
					$('#sureToDeletecancel').prop('disabled', true);
					var row = table.row('.success');
					if (row.length > 0) {
						$.get('${method}', 
								{id:row.data().id,
							     operation:'delete'})
					     .done(function(data) {
								$('#sureToDelete').modal('hide');
								$('#sureToDeleteprocess').prop('disabled', false);
								$('#sureToDeletecancel').prop('disabled', false);
								table.draw();
						  		 })
						 .fail(function(data) {
								$('#sureToDelete').modal('hide');
								$('#sureToDeleteprocess').prop('disabled', false);
								$('#sureToDeletecancel').prop('disabled', false);
								$('#alertText').text(data.responseText);
								$('#alertMessage').removeClass('hide');
							    $('html body').animate({
							        scrollTop: 0 
							    }, 200);
							   });
					}
				});
		var p = window.location.href.split("?")[0];
		var j = getCookie(p + "-order");
		
		if (j && j != "undefined")
			o = $.parseJSON(j);
		else
			o = [${sorting}];
		
		var table = $('#grid').DataTable({
		 			ajax: {
		 				url: '${method}',
		 				type: 'POST'
		 			},
		 			columns: [${items}],
		 	    	order: o,
		 	    	serverSide: true,
		 	    	lengthChange: false,
		 	    	searching: false,
		 	    	paging: ${paging},
		 	    	pageLength: 25,
		 	    	language: {
		 	    		url: 'js/localization/grid_${context.getLocaleString()}.json'
		 	    	}
			  	});
	
	    $('#grid').on( 'click', 'tbody tr', function () {
            table.$('tr.success').removeClass('success');
            $(this).addClass('success');
	    });
	    
	    $('#grid').on( 'dblclick', 'tbody tr', function (e) {
	    	$("#edit").click();
	    	e.stopPropagation();
	    });
	    
	    $('#grid').on( 'draw.dt', function (e) {
	    	o = $('#grid').DataTable().order();
	    	p = window.location.href.split("?")[0];
	    	j = $.toJSON(o);
	    	setCookie(p + "-order", j, {expires: 7*24*60});
	    });
	    
<c:if test="${standardFilters != false}">	    
		moment.locale('${context.getLocaleString()}');

		locale = {
				"format": "YYYY-MM-DD",
		        "separator": " — ",
		        "applyLabel": "${context.gs('LBL_DATERANGE_APPLY_BTN')}",
		        "cancelLabel": "${context.gs('LBL_DATERANGE_CANCEL_BTN')}",
		        "fromLabel": "${context.gs('LBL_DATERANGE_FROM')}",
		        "toLabel": "${context.gs('LBL_DATERANGE_TO')}",
		        "customRangeLabel": "${context.gs('LBL_DATERANGE_CUSTOM')}",
		        "daysOfWeek": moment.weekdaysShort(),
		        "monthNames": moment.months(),
		        "firstDay": 1
		    };

		var startDate = moment('${filterStartDate}');
		var endDate = moment('${filterEndDate}');
		
		$("#periodPicker").daterangepicker({
				autoApply: true,
				locale: locale,
				opens: 'left',
				startDate: startDate,
				endDate: endDate
		});
		
		$shopposfirstLoad = true;
		var el = $("#shopposinput")[0].selectize;
		el.load(shopposLoadData);
		el.on("load", function () {
			if ($shopposfirstLoad) {
				var el = $("#shopposinput")[0].selectize;
				var defaultId = "${context.getFilterShopPosId()}";
				if (defaultId != "") {
					var firstOptionIndex = Object.keys(el.options)[0];
					for (i = 0; i < Object.keys(el.options).length; i++) {
						var s = Object.keys(el.options)[i];
						if (s.charAt(0) == "s") {
							firstOptionIndex = Object.keys(el.options)[i];
							break;
						}
					}
		    		el.addItem(el.options[firstOptionIndex].id);
				} else
					el.addItem(defaultId);
	    		el.refreshItems();
	    		$shopposfirstLoad = false;
			}
		});
		
		$("#applyFilters").click(function() {
			var dtp = $("#periodPicker").data("daterangepicker");
			var spId = $("#shopposinput").val();
			var shopId = null;
			var posId = null;
			if (spId.charAt(0) == "s")
				shopId = spId.substr(1);
			else
				posId = spId;
			var startDate = dtp.startDate.format("YYYY-MM-DD");
			var endDate = dtp.endDate.format("YYYY-MM-DD");
			
			$.ajax({
				url: 'BBRBase',
	        	data: {
	        		operation: 'setFilters',
	        		filterStartDate: startDate,
	        		filterEndDate: endDate,
	        		filterShop: shopId,
	        		filterPoS: posId 
	        	}
			}).success(function (d) {
				table.draw();
			}).error(function () {
			});
		});
</c:if>
	});
</script>

