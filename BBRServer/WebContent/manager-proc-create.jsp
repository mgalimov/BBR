<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<t:general-wrapper title="LBL_CREATE_PROCEDURE_TITLE">
<jsp:body>
		<t:card title="LBL_CREATE_PROCEDURE_TITLE" gridPage="manager-proc-list.jsp" method="BBRProcedures">
			<t:card-item label="LBL_POS" type="reference" field="pos" isRequired="required" referenceFieldTitle="title" referenceMethod="BBRPoSes" />
			<t:card-item label="LBL_TITLE" type="text" field="title" isRequired="required" />
			<t:card-item label="LBL_PROC_LENGTH" type="text" field="length" isRequired="required" defaultValue="0.5"/>
			<t:card-item label="LBL_PROC_PRICE" type="text" field="price" />
			<t:card-item label="LBL_CURRENCY" type="text" field="currency" />
			<t:card-item label="LBL_PROC_STATUS" type="select" field="status" options="OPT_PROC_STATUS"/>
		</t:card>
</jsp:body>
</t:general-wrapper>
