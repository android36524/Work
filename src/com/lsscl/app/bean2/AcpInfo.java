package com.lsscl.app.bean2;

/**
 * 空压机信息
 * 
 * @author yxx
 * 
 */
public class AcpInfo {
	private String id;
	private String name;
	private String scopeId;
	private String scopeName;
	private String p1;//工况比功率
	private String p2;//风冷节能
	private String p3;//水冷节能
	private boolean run;// 运行状态 1运行 0：停止

	public String toJson() {
		String json = "{\"ID\":" + id + ",\"NAME\":\"" + name + "\",\"SID\":"
				+ scopeId + ",\"SNAME\":\"" + scopeName + "\",\"STATE\":"
				+ (run ? 1 : 0) + ",\"P1\":" + p1 + ",\"P2\":" + p2
				+ ",\"P3\":" + p3 + "}";
		return json;
	}

	public String getP1() {
		return p1;
	}

	public void setP1(String p1) {
		this.p1 = p1;
	}

	public String getP2() {
		return p2;
	}

	public void setP2(String p2) {
		this.p2 = p2;
	}

	public String getP3() {
		return p3;
	}

	public void setP3(String p3) {
		this.p3 = p3;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScopeId() {
		return scopeId;
	}

	public void setScopeId(String scopeId) {
		this.scopeId = scopeId;
	}

	public boolean isRun() {
		return run;
	}

	public void setRun(boolean run) {
		this.run = run;
	}

	public String getScopeName() {
		return scopeName;
	}

	public void setScopeName(String scopeName) {
		this.scopeName = scopeName;
	}

}
