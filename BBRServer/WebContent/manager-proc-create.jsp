<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<t:general-wrapper title="Create procedure">
<jsp:body>
		<t:card title="Create procedure" gridPage="manager-proc-list.jsp" method="BBRProcedures">
			<t:card-item label="Select place" type="reference" field="pos" isRequired="required" referenceFieldTitle="title" referenceMethod="BBRPoSes" />
			<t:card-item label="Title" type="text" field="title" isRequired="required" />
			<t:card-item label="Length, hrs" type="text" field="length" isRequired="required" defaultValue="0.5"/>
			<t:card-item label="Price" type="text" field="price" />
			<t:card-item label="Currency" type="text" field="currency" />
			<t:card-item label="Status" type="select" field="status" options="0:Initialized,1:Approved,2:Inactive"/>
		</t:card>
</jsp:body>
</t:general-wrapper>
