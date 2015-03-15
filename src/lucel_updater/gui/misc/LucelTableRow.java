package lucel_updater.gui.misc;

public class LucelTableRow {
	private String fromFilename;
	private String toFilename;
	private String size;
	private long status;
	
	public LucelTableRow(String from, String to, String size){
		this.fromFilename = from;
		this.toFilename = to;
		this.size = size;
	}
	public String getFromFilename() {
		return fromFilename;
	}
	public void setFromFilename(String fromFilename) {
		this.fromFilename = fromFilename;
	}
	public String getToFilename() {
		return toFilename;
	}
	public void setToFilename(String toFilename) {
		this.toFilename = toFilename;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public long getStatus() {
		return status;
	}
	public void setStatus(long status) {
		this.status = status;
	}
	
}
