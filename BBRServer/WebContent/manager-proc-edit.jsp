<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<t:wrapper title="LBL_EDIT_PROCEDURE_TITLE">
<jsp:body>
		<t:card title="LBL_EDIT_PROCEDURE_TITLE" gridPage="manager-proc-list.jsp" method="BBRProcedures">
			<t:card-item label="LBL_TITLE" type="text" field="title" isRequired="required" />
			<t:card-item label="LBL_PROC_GROUP" type="reference" field="procedureGroup" referenceFieldTitle="title" referenceFieldExpr="data.title + ' (' + data.pos.title + ')'" referenceMethod="BBRProcedureGroups" isRequired="required" />
			<t:card-item label="LBL_POS" type="text" field="procedureGroup.pos.title" isDisabled="readonly" />
			<t:card-item label="LBL_PROC_LENGTH" type="text" field="length" isRequired="required" defaultValue="0.5"/>
			<t:card-item label="LBL_PROC_PRICE" type="text" field="price" />
			<t:card-item label="LBL_CURRENCY" type="text" field="procedureGroup.pos.currency" isDisabled="readonly"/>
			<t:card-item label="LBL_PROC_STATUS" type="select" field="status" options="OPT_PROC_STATUS"/>
		</t:card>
</jsp:body>
</t:wrapper>


<script>
	$(document).ready(function () {
		$("#procedureGroupinput").on("change", function() {
			$.ajax({
				url: 'BBRProcedureGroups',
	        	data: {
	        		operation: 'getdata',
	        		id: $(this).val()
	        	}
			}).success(function (d) {
				p = $.parseJSON(d);
				$("#procedureGroup_pos_currencyinput").val(p.pos.currency);
				$("#procedureGroup_pos_titleinput").val(p.pos.title);
			}).error(function () {
				$("#procedureGroup_pos_currencyinput").val("");
				$("#procedureGroup_pos_titleinput").val("");
			});
		})
	});
</script>