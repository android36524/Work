package com.lsscl.app.bean3;

import com.lsscl.app.bean.MsgBody;

public class ScopeBasePointStatistics extends MsgBody{
	private float runTime;
	private float stopTime;
	private float loadTime;
	private float unloadTime;
	private int acpCount;
	private float avgLoadPower;
	private float avgUnloadPower;
	
	@Override
	public String toJSON() {
		String json = "\"MSGBODY\":{";
		json += "\"RT\":"+runTime+",";
		json += "\"ST\":"+stopTime+",";
		json += "\"LT\":"+loadTime+",";
		json += "\"ULT\":"+unloadTime+",";
		json += "\"COUNT\":"+acpCount+",";
		json += "\"LP\":"+avgLoadPower+",";
		json +="\"AUP\":"+avgUnloadPower+"}";
		return json;
	}

	public void setRunTime(float runTime) {
		this.runTime = runTime;
	}

	public void setStopTime(float stopTime) {
		this.stopTime = stopTime;
	}

	public void setLoadTime(float loadTime) {
		this.loadTime = loadTime;
	}

	public void setUnloadTime(float unloadTime) {
		this.unloadTime = unloadTime;
	}

	public void setAcpCount(int acpCount) {
		this.acpCount = acpCount;
	}

	public void setAvgLoadPower(float avgLoadPower) {
		this.avgLoadPower = avgLoadPower;
	}

	public void setAvgUnloadPower(float avgUnloadPower) {
		this.avgUnloadPower = avgUnloadPower;
	}
	
}
