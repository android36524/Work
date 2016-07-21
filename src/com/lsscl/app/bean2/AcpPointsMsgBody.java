package com.lsscl.app.bean2;

import java.util.ArrayList;
import java.util.List;

import com.lsscl.app.bean.MsgBody;

/**
 * 空压机点详情
 * @author yxx
 *
 */
public class AcpPointsMsgBody extends MsgBody{
	private List<PointValue> points = new ArrayList<PointValue>();
	private String p1;
	private String v1;
	private String p2;
	private String v2;
	private String p3;
	private String v3;
	private AcpInfo acp;
	
	@Override
	public String toJSON() {
		String json = "\"MSGBODY\":{";
		if(acp!=null){
			json += "\"ACP\":"+acp.toJson()+",";
		}
		if(p1!=null&&v1!=null){
			json += "\"P1\":"+p1+",\"V1\":\""+v1+"\",";
		}
		if(p2!=null&&v2!=null){
			json += "\"P2\":"+p2+",\"V2\":\""+v2+"\",";
		}
		if(p3!=null&&v3!=null){
			json += "\"P3\":"+p3+",\"V3\":\""+v3+"\",";
		}
		json += "\"POINTS\":[";
		for(PointValue pv:points){
			json += pv.toJson()+",";
		}
		if(points.size()>0)json = json.substring(0, json.lastIndexOf(","));
		json += "]}";
		return json;
	}

	public List<PointValue> getPoints() {
		return points;
	}

	public void setPoints(List<PointValue> points) {
		this.points = points;
	}


	public String getP1() {
		return p1;
	}

	public void setP1(String p1) {
		this.p1 = p1;
	}

	public String getV1() {
		return v1;
	}

	public void setV1(String v1) {
		this.v1 = v1;
	}

	public String getP2() {
		return p2;
	}

	public void setP2(String p2) {
		this.p2 = p2;
	}

	public String getV2() {
		return v2;
	}

	public void setV2(String v2) {
		this.v2 = v2;
	}

	public String getP3() {
		return p3;
	}

	public void setP3(String p3) {
		this.p3 = p3;
	}

	public String getV3() {
		return v3;
	}

	public void setV3(String v3) {
		this.v3 = v3;
	}

	public AcpInfo getAcp() {
		return acp;
	}

	public void setAcp(AcpInfo acp) {
		this.acp = acp;
	}
	
}
