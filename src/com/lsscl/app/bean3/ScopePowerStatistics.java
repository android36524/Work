package com.lsscl.app.bean3;

import java.util.List;

import com.lsscl.app.bean.MsgBody;
import com.lsscl.app.util.StringUtil;
import com.serotonin.mango.rt.dataImage.PointValueTime;

public class ScopePowerStatistics extends MsgBody{
	private long duration;
	private long startTime;
	private long endTime;
	private List<PointValueTime> points;
	@Override
	public String toJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append("\"MSGBODY\":{");
		sb.append("\"STIME\":"+startTime+",");
		sb.append("\"DURATION\":"+duration+",");
		sb.append("\"POINTS\":"+buildPoints());
		sb.append("}");
		return sb.toString();
	}
	private String buildPoints() {
		StringBuilder sb = new StringBuilder("[");
		int length = (int) ((endTime-startTime)/duration);
		Double []values = new Double[length];
		for(PointValueTime pvt:points){
			int index = (int) ((pvt.getTime()-startTime)/duration);
			values[index] = pvt.getDoubleValue();
		}
		for(Double d:values){
			String s = d!=null?StringUtil.formatNumber(d, "0.0"):"-1";
			sb.append(s+",");
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append("]");
		return sb.toString();
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public void setPoints(List<PointValueTime> points) {
		this.points = points;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
}
