<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:wrapper title="LBL_EDIT_SPEC_TITLE">
	<jsp:body>
		<t:card method="BBRSpecialists" gridPage="manager-spec-list.jsp" title="LBL_EDIT_SPEC_TITLE" showTabs="true">
			<t:toolbar-item label="LBL_OPEN_TURNS_BTN" id="openTurnsBtn" />
			<t:card-tab label="LBL_SPEC" id="generalTab" combined="true" isActive="true">
				<t:card-item label="LBL_NAME" field="name" type="text" isRequired="required" />
				<t:card-item label="LBL_POS" type="reference" field="pos" referenceFieldTitle="title" referenceMethod="BBRPoSes"/>
				<t:card-item label="LBL_POSITION" field="position" type="text" isRequired="required" />
				<t:card-item label="LBL_DAILY_AMOUNT" field="dailyAmount" type="text" isRequired="required" defaultValue="0"/>
				<t:card-item label="LBL_PROCEDURE_PERCENT" field="procedurePercent" type="text" isRequired="required" defaultValue="0"/>
				<t:card-item label="LBL_SPEC_STATUS" type="select" field="status" options="OPT_SPEC_STATUS"/>
				<t:card-item label="LBL_START_WORKHOUR" field="startWorkHour" type="time"/>
				<t:card-item label="LBL_END_WORKHOUR" field="endWorkHour" type="time"/>
				<t:card-item label="LBL_AVAILABLE_PROCEDURES" field="procedures" type="reference" referenceFieldTitle="title" referenceMethod="BBRProcedures" multiple="true" isRequired="required"/>
			</t:card-tab>
			<t:card-tab label="LBL_PHOTO" id="photoTab" combined="true">
				<t:card-item label="LBL_PHOTO" field="photoPath" type="picture" />
			</t:card-tab>
		</t:card>
	</jsp:body>
</t:wrapper>


<script>
	var posId; 

	$(document).ready(function() {
		posId = $("#posinput").val();
		
		$("#posinput").on("change", function () {
			newPosId = $("#posinput").val();
			if (newPosId != posId) {
				posId = newPosId;
				inp = $("#proceduresinput")[0].selectize;
				inp.clear();
				inp.clearOptions();
				inp.load(proceduresLoadInitialData);
			}
		});
	});
	
	proceduresSetConstrains = function () {
		return $("#posinput").val();
	}
	
	$(document).ready(function() {
		$("#openTurnsBtn").click(function () {
			specId = getUrlParameter("id");
			if (specId)
				window.location.href = "manager-turn-list.jsp?t=spec&query=" + specId;
		});
	});
</script>