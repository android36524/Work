package com.lsscl.app.bean2;

public class PointValue {
	private String pid;//点id
	private String pname;//点名称
	private String value;//点值
	private int type;//统计点类型 0：普通点
	private boolean run;//是否
	private String color;
	
	public String toJson(){
		String json = "{\"PID\":"+pid
				     +",\"PNAME\":\""+pname
				     +"\",\"VALUE\":\""+value+"\",\"STATE\":"+(run?1:0)
				     +",\"COLOR\":\""+color+"\""
				     +",\"TYPE\":"+type+"}";
		return json;
	}
	
	
	public String getColor() {
		return color;
	}


	public void setColor(String color) {
		this.color = color;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public boolean isRun() {
		return run;
	}

	public void setRun(boolean run) {
		this.run = run;
	}
	
}
