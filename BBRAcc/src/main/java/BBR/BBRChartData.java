package BBR;

import java.util.ArrayList;

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
}