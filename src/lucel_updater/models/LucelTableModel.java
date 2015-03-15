package lucel_updater.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import javax.swing.JProgressBar;
import javax.swing.table.AbstractTableModel;

import lucel_updater.gui.misc.LucelTableRow;

public class LucelTableModel extends AbstractTableModel {
	int rowCount;
	ArrayList<LucelTableRow> data;
	String[] columnNames;

	public LucelTableModel(String[] strings) {
		this.rowCount = 0;
		columnNames = strings;
		this.data = new ArrayList<LucelTableRow>();
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.size();
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		LucelTableRow a = data.get(row);
		if (col == 3) {
			return new Float(a.getStatus());
		} else if (col == 0)
			return a.getToFilename();
		else if (col == 1)
			return a.getFromFilename();
		else if (col == 2)
			return a.getSize();
		return "error.";
	}

	@SuppressWarnings("unchecked")
	public Class getColumnClass(int c) {
		if (c == 3) // progressbar column
			return JProgressBar.class;
		return String.class;
	}

	@SuppressWarnings("unchecked")
	public void setValueAt(Object value, int row, int col) {
		try{
			LucelTableRow r = data.get(row);
	
			switch (col) {
			case 3:
				r.setStatus((long) value);
				break;
			}
	
			fireTableCellUpdated(row, col);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public int addRow(LucelTableRow row) {

		try {
			this.data.add(row);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// fireTableDataChanged();
		fireTableRowsInserted(data.size() - 1, data.size() - 1);
		return getRowCount() - 1;
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}


}
