package cn.itcast.mobilesafe.domain;

public class LockAppInfo {
	private String packname;
	private boolean flagcanstart=false;
	public String getPackname() {
		return packname;
	}
	public void setPackname(String packname) {
		this.packname = packname;
	}
	public boolean isFlagcanstart() {
		return flagcanstart;
	}
	public void setFlagcanstart(boolean flagcanstart) {
		this.flagcanstart = flagcanstart;
	}
	
}
