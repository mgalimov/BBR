<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<t:wrapper title="LBL_EDIT_PROC_GROUP_TITLE">
<jsp:body>
		<t:card title="LBL_EDIT_PROC_GROUP_TITLE" gridPage="manager-proc-list.jsp" method="BBRProcedureGroups">
			<t:card-item label="LBL_POS" type="reference" field="pos" isRequired="required" referenceFieldTitle="title" referenceMethod="BBRPoSes" />
			<t:card-item label="LBL_TITLE" type="text" field="title" isRequired="required" />
			<t:card-item label="LBL_DESCRIPTION" type="text" field="description" />
		</t:card>
</jsp:body>
</t:wrapper>


<script>
	$(document).ready(function () {
		$("#posinput").on("change", function() {
			$.ajax({
				url: 'BBRPoSes',
	        	data: {
	        		operation: 'getdata',
	        		id: $(this).val()
	        	}
			}).success(function (d) {
				p = $.parseJSON(d);
				$("#pos_currencyinput").val(p.currency);
			}).error(function () {
				$("#pos_currencyinput").val("---");
			});
		})
	});
</script>