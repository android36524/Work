package com.lsscl.app.bean2;

import com.lsscl.app.bean.MsgBody;

public class AcpStatistics extends MsgBody{
	
	private float unloadTime;//卸载时间
	private float loadTime;//加载时间
	private float runTime;//运行时间
	private float stopTime;//停机时间
	private float avgLoadPower;//平均加载功率
	private float avgUnLoadPower;//平均卸载功率
	public float getUnloadTime() {
		return unloadTime;
	}
	public void setUnloadTime(float unloadTime) {
		this.unloadTime = unloadTime;
	}
	public float getLoadTime() {
		return loadTime;
	}
	public void setLoadTime(float loadTime) {
		this.loadTime = loadTime;
	}
	public float getRunTime() {
		return runTime;
	}
	public void setRunTime(float runTime) {
		this.runTime = runTime;
	}
	public float getStopTime() {
		return stopTime;
	}
	public void setStopTime(float stopTime) {
		this.stopTime = stopTime;
	}
	public float getAvgLoadPower() {
		return avgLoadPower;
	}
	public void setAvgLoadPower(float avgLoadPower) {
		this.avgLoadPower = avgLoadPower;
	}
	public float getAvgUnLoadPower() {
		return avgUnLoadPower;
	}
	public void setAvgUnLoadPower(float avgUnLoadPower) {
		this.avgUnLoadPower = avgUnLoadPower;
	}
	
	@Override
	public String toJSON() {
		String json = "\"MSGBODY\":{";
				json += "\"LT\":"+loadTime+",";
				json += "\"ULT\":"+unloadTime+",";
				json += "\"RT\":"+runTime+",";
				json += "\"ST\":"+stopTime+",";
				json += "\"ALP\":"+avgLoadPower+",";
				json += "\"AUP\":"+avgUnLoadPower;
				json +="}";
		return json;
	}
}
