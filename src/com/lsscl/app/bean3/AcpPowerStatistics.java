package com.lsscl.app.bean3;

import java.util.List;

import com.lsscl.app.bean.MsgBody;
import com.serotonin.mango.rt.dataImage.PointValueTime;

public class AcpPowerStatistics extends MsgBody {
	
	private static final long duration = 20000;// 时间间隔
	private List<PointValueTime> powerPoints;
	private List<PointValueTime> runningStatePoints;
	private List<PointValueTime> loadingStatePoints;
	private long startTime;
	private long endTime;

	@Override
	public String toJSON() {
		int length = (int) ((endTime - startTime) / duration)+1;
		StringBuilder sb = new StringBuilder();
		sb.append("\"MSGBODY\":{");
		sb.append("\"STIME\":"+startTime+",");
		sb.append("\"DURATION\":"+duration+",");
		sb.append(buildPoints(powerPoints,length,"POWER"));
		sb.append(",");
		sb.append(buildPoints(runningStatePoints,length, "RS"));
		sb.append(",");
		sb.append(buildPoints(loadingStatePoints,length, "LS"));
		sb.append("}");
		return sb.toString();
	}

	public String buildPoints(List<PointValueTime> pvts, int length,String key) {
		StringBuilder sb = new StringBuilder();
		Object[]array = new Object[length];
		for(PointValueTime p:pvts){
			int index = (int) ((p.getTime()-startTime)/duration);
			array[index] = p.getDoubleValue();
		}
		sb.append("\"" + key + "\":[");
		for(Object obj:array){
			sb.append((obj!=null?obj:"-1")+",");
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append("]");
		return sb.toString();
	}

	public List<PointValueTime> getPowerPoints() {
		return powerPoints;
	}

	public void setPowerPoints(List<PointValueTime> powerPoints) {
		this.powerPoints = powerPoints;
	}

	public List<PointValueTime> getRunningStatePoints() {
		return runningStatePoints;
	}

	public void setRunningStatePoints(List<PointValueTime> runningStatePoints) {
		this.runningStatePoints = runningStatePoints;
	}

	public List<PointValueTime> getLoadingStatePoints() {
		return loadingStatePoints;
	}

	public void setLoadingStatePoints(List<PointValueTime> loadingStatePoints) {
		this.loadingStatePoints = loadingStatePoints;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

}
