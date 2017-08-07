package BBR;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BBRChartData extends BBRDataElement {
	ArrayList<BBRChartCol> cols = new ArrayList<BBRChartCol>();
	ArrayList<BBRChartRow> rows = new ArrayList<BBRChartRow>();
	
	public BBRChartCol addCol(String id, String label, String type) {
		BBRChartCol col = new BBRChartCol();
		col.id = id;
		col.label = label;
		col.type = type;
		cols.add(col);
		
		return col;
	}
	
	public BBRChartCol addCol(String label, String type) {
		return addCol(null, label, type);
	}

	public BBRChartCol addCol(String label) {
		return addCol(null, label, BBRChartDataTypes.BBR_CHART_STRING);
	}

	public void addCols(String... labels) {
		for(int i = 0; i < labels.length; i++){
			addCol(labels[i]);
		}
	}
	
	public void addCols(String[]... labels) {
		for(int i = 0; i < labels.length; i++){
			if (labels[i].length > 1)
				addCol(labels[i][0], labels[i][1]);
			else
				addCol(labels[i][0]);
		}
	}

	public BBRChartRow addRow(BBRChartCell... cells) {
		BBRChartRow r = new BBRChartRow();
		
		for(int i = 0; i < cells.length; i++){
			r.c.add(cells[i]);
	    }
		
		rows.add(r);
		return r;
	}
	
	public BBRChartRow addRow(Object[]... cells) {
		BBRChartRow r = new BBRChartRow();
		
		for(int i = 0; i < cells.length; i++){
			BBRChartCell cell;
			if (cells[i].length > 1)
				cell = new BBRChartCell(cells[i][0], (String)cells[i][1]);
			else
				cell = new BBRChartCell(cells[i][0]);
			
			r.c.add(cell);
	    }
		rows.add(r);
		
		return r;
	}

	public BBRChartRow addRow(Object... cells) {
		BBRChartRow r = new BBRChartRow();
		
		for(int i = 0; i < cells.length; i++){
			BBRChartCell cell;
			cell = new BBRChartCell(cells[i]);
			r.c.add(cell);
	    }
		rows.add(r);
		
		return r;
	}

	
	public void addRows(Object[][]... rows) {
		for(int i = 0; i < rows.length; i++){
			if (rows[i] != null)
				addRow(rows[i]);
	    }
	}
	
	public BBRChartData(String[][] cols, Object[][][] rows) {
		addCols(cols);
		addRows(rows);
	}
	
	public BBRChartData() {
	}
	
	public class BBRChartDataTypes {
		public static final String BBR_CHART_STRING = "string";
		public static final String BBR_CHART_NUMBER = "number";
		public static final String BBR_CHART_DATETIME = "datetime";
		public static final String BBR_CHART_DATE = "date";
		public static final String BBR_CHART_TIME = "timeofday";
		public static final String BBR_CHART_BOOLEAN = "boolean";
	}
	
	public void importList(List<Object[]> list, List<Object[]> listComp, BBRChartPeriods period) {
		for(int i = 0; i < list.size(); i++) {
			Object[] line = list.get(i);
			if (period.compareToEndDate == null) {
				this.addRow(line);
			} else {
				Object[] lineCompD;
				if (listComp != null && i < listComp.size()) {
					Object[] lineComp = listComp.get(i);
					line[0] = line[0] + "\n" + lineComp[0];
					lineCompD = new Object[lineComp.length-1];
					for (int j = 1; j < lineComp.length; j++)
						lineCompD[j-1] = lineComp[j];
				} else {
					lineCompD = new Object[line.length-1];
					for (int j = 1; j < line.length; j++)
						lineCompD[j-1] = 0;					
				}
				
				BBRChartCell[] cells = new BBRChartCell[(line.length-1)*2 + 1];
				
				for (int j = 0; j < line.length; j++)
					cells[j] = new BBRChartCell(line[j]);

				for (int j = 0; j < lineCompD.length; j++)					
					cells[line.length + j] = new BBRChartCell(lineCompD[j]);
				
				this.addRow(cells);
			}
		}
	}
	
	public static List<Object[]> enrichDateList(List<Object[]> list, Date startDate, Date endDate, int detail) throws Exception {
		if (list == null) return null;
		
		String infmt = BBRChartPeriods.dateInFormat(detail);
		String outfmt = BBRChartPeriods.dateOutFormat(detail);
		int delta = BBRChartPeriods.getDelta(detail);
       
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		SimpleDateFormat idf = new SimpleDateFormat(infmt);
		SimpleDateFormat odf = new SimpleDateFormat(outfmt);
	
		List<Object[]> rlist = new ArrayList<Object[]>();
		
		int i = 0;
		while (calendar.getTime().before(endDate) || calendar.getTime().equals(endDate)) {
			Date dt = null;

			Calendar cdr = Calendar.getInstance();
			if (i < list.size()) {
				try {
					dt = idf.parse((list.get(i)[0]).toString());
				} catch (Exception ex) {
					dt = startDate;
				}
				cdr.setTime(dt);
			}

			if (dt == null || calendar.before(cdr)) {
				Object[] line = null;
				if (list.size() > 0)
					line = list.get(0);
				if (line != null) {
					Object[] line2 = new Object[line.length];
					line2[0] = odf.format(calendar.getTime());
					for (int j = 1; j < line.length; j++)
						line2[j] = 0;
					rlist.add(line2);
				} else {
					Object[] line2 = {odf.format(calendar.getTime()), 0};
					rlist.add(line2);
				}
				calendar.add(delta, 1);
			} else {
				Object[] line = list.get(i);
				Object[] line2 = new Object[line.length];
				line2[0] = odf.format(calendar.getTime());
				for (int j = 1; j < line.length; j++)
					line2[j] = line[j];
				
				rlist.add(line2);
				calendar.add(delta, 1);
				i++;
			}
		}
		
		return rlist;
	}
}