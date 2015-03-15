package lucel_updater.gui.misc;

import java.awt.Component;
import java.util.logging.Logger;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class LucelProgressRenderer implements TableCellRenderer{
	public JProgressBar prog  = new JProgressBar(0,100);
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		try{
			int progress = 0;
	        if (value instanceof Float) {
	            progress = Math.round(((Float) value) * 100f);
	        } else if (value instanceof Integer) {
	            progress = (int) value;
	        }
	        prog.setValue(progress/100);
	        prog.setString(String.valueOf(progress/100)+" %");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
        return prog;
	}

}
