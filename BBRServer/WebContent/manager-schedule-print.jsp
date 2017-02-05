<%@page import="java.text.SimpleDateFormat"%>
<%@page import="BBRAcc.BBRUser.BBRUserRole"%>
<%@page import="BBRAcc.BBRPoSManager"%>
<%@page import="BBRClientApp.BBRContext"%>
<%@page import="BBR.BBRUtil"%>
<%@page import="BBRClientApp.BBRParams"%>
<%@page import="BBRAcc.BBRUser"%>
<%@page import="BBRAcc.BBRPoS"%>
<%@page import="BBRCust.BBRSpecialistManager"%>
<%@page import="BBRCust.BBRSpecialist"%>
<%@page import="BBR.BBRDataSet"%>
<%@page import="java.util.*"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%
	BBRContext context = BBRContext.getContext(request);
	BBRParams params = new BBRParams(request.getQueryString());

	String startDate = params.get("startDate");
	String posId = params.get("posId");
	String type = params.get("type");
	String title = "";
	String specsTable = "";
	int datesPerPage = 7;
	
	try {
		if (!type.equals("calendar") && !type.equals("table"))
			throw new Exception();
		BBRPoSManager mgr = new BBRPoSManager();
		BBRPoS pos = mgr.findById(Long.parseLong(posId));
		if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
			if (pos.getShop().getId() != context.user.getShop().getId())
				throw new Exception();
		if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN || 
			context.user.getRole() == BBRUserRole.ROLE_POS_SPECIALIST)
			if (pos.getId() != context.user.getPos().getId())
				throw new Exception();
		if (context.user.getRole() == BBRUserRole.ROLE_VISITOR)
			throw new Exception();

		title = pos.getTitle();

		if (type.equals("table")) {
			BBRSpecialistManager smgr = new BBRSpecialistManager();
			BBRDataSet<BBRSpecialist> slist = smgr.listAvailableSpecialists(pos);

			Calendar cs = Calendar.getInstance();
			Calendar ce = Calendar.getInstance();
			Calendar c = Calendar.getInstance();
			
			SimpleDateFormat df = new SimpleDateFormat(BBRUtil.fullDateFormat);
			Date sd = df.parse(startDate);
			cs.setTime(sd);
			ce.setTime(sd);
			ce.set(Calendar.DAY_OF_MONTH, cs.getActualMaximum(Calendar.DAY_OF_MONTH));
			
			while (!cs.after(ce)) {
				specsTable += "<table class='table table-bordered table-condensed table-striped small'>";
				specsTable += "<tr>";
				c.setTime(cs.getTime());
				int p = 1;
				specsTable += "<td></td>";
				if (datesPerPage == 7)
					while (p < c.get(Calendar.DAY_OF_WEEK) - 1) {
						specsTable += "<td style='width:83px;'></td>";
						p++;
					}
				while (p <= datesPerPage && !c.after(ce)) {
					specsTable += "<td data-top='t' data-date='" + df.format(c.getTime()) + "' style='width:83px;'></td>";
					c.add(Calendar.DATE, 1);
					p++;
				}
				while (p <= datesPerPage) {
					specsTable += "<td style='width:83px;'></td>";
					p++;
				}
				
				specsTable += "</tr>";
				
				for (BBRSpecialist spec : slist.data) {
					specsTable += "<tr>";
					specsTable += "<td>" + spec.getName() + ", " + spec.getPosition() + "</td>";
					String sid = spec.getId().toString();
					c.setTime(cs.getTime());
					p = 1;
					if (datesPerPage == 7)
						while (p < c.get(Calendar.DAY_OF_WEEK) - 1) {
							specsTable += "<td></td>";
							p++;
						}
					while (p <= datesPerPage && !c.after(ce)) {
						specsTable += "<td data-spec='" + sid + "' data-date='" + df.format(c.getTime()) + "' class='text-center nobr' nowrap='nowrap'></td>";
						c.add(Calendar.DATE, 1);
						p++;
					}
					while (p <= datesPerPage) {
						specsTable += "<td></td>";
						p++;
					}
					specsTable += "</tr>";
				}
				cs.setTime(c.getTime());
				specsTable += "</table>";
			}
			
		}
	} catch (Exception ex) {
		startDate = "";
	}
%>

<t:light-wrapper title="">
	<h1 id="title"></h1>
	<div id="printPanel">
	</div>
</t:light-wrapper>

<script>
	$(document).ready(function () {
		var sd = "<%=startDate%>";
		if (sd == "") return;
		moment.locale('${context.getLocaleString()}');
		
		sd = moment(sd).startOf('month');
		var ed = moment(sd).endOf('month');
		$.ajax({
			url: 'BBRTurns',
			method: 'get',
			data: {
				operation: 'getTurns',
				posId: <%=posId%>,
				startDate: sd.year() + "-" + (sd.month() + 1) + "-" + sd.date(),
				endDate: ed.year() + "-" + (ed.month() + 1) + "-" + ed.date()
			}
		}).done(function (data) {
			if (data == "")
				return;
			turns = $.parseJSON(data);
			$("#title").text("<%=title%>, " + sd.format("MMMM YYYY"));
			if ("<%=type%>" == "calendar") { 
				html = "<table class='table table-bordered table-condensed table-striped small'>";
				rowD = "";
				rowC = "";
				var d = moment(sd);
				var wd = 1;
				while (wd <= d.weekday()) {
					rowD += "<td></td>";
					rowC += "<td></td>";
					wd++;
				}
				while (!d.isAfter(ed, 'day')) {
					rowD += "<td><b>" + d.format("dd DD") + "</b></td>"
					rowC += "<td style='height: 80px;'>";
					for (i = 0; i < turns.data.length; i++) {
						if (moment(turns.data[i].date).isSame(d, 'day')) {
							rowC += turns.data[i].specialist.name + ": " + 
								    turns.data[i].startTime + " - " + turns.data[i].endTime + "<br/>";
						}; 
					}
					rowC += "</td>"
					d.add(1, "day");
					wd++;
					if (wd > 7) {
						wd = 1;
						html += "<tr>" + rowD + "</tr><tr>" + rowC + "</tr>";
						rowD = "";
						rowC = "";
					};
				}
				while (wd <= 7) {
					rowD += "<td></td>";
					rowC += "<td></td>";
					wd++;
				}
				html += "<tr>" + rowD + "</tr><tr>" + rowC + "</tr></table>";
				$("#printPanel").html(html);
			} else
			if ("<%=type%>" == "table") {
				html = "<%=specsTable%>";
				$("#printPanel").html(html);
				for (i = 0; i < turns.data.length; i++) {
					cell = turns.data[i].startTime + " - " + turns.data[i].endTime; 
					$("td[data-spec='" + turns.data[i].specialist.id + "'][data-date='" + turns.data[i].date + "']").html(cell);
				}
				$("td[data-top='t']").each(function () {
					el = $(this);
					d = moment(el.attr("data-date"));
					el.html("<b>" + d.format("dd DD") + "</b>");
				})
			}
			window.print();
		});
	});
</script>