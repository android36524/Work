package com.serotonin.mango.vo;

import com.serotonin.mango.Common;

public class RemoteControlVO {

	private int pointID = Common.NEW_ID;
	private String pointName;
	private int offset;
	private int scopeid;
	private int settingValue;
	private int dsid;
	private int dataType;
	private int bit;

	public int getPointID() {
		return pointID;
	}

	public void setPointID(int pointID) {
		this.pointID = pointID;
	}

	public String getPointName() {
		return pointName;
	}

	public void setPointName(String pointName) {
		this.pointName = pointName;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getScopeid() {
		return scopeid;
	}

	public void setScopeid(int scopeid) {
		this.scopeid = scopeid;
	}

	public int getSettingValue() {
		return settingValue;
	}

	public void setSettingValue(int settingValue) {
		this.settingValue = settingValue;
	}

	public int getDsid() {
		return dsid;
	}

	public void setDsid(int dsid) {
		this.dsid = dsid;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public int getBit() {
		return bit;
	}

	public void setBit(int bit) {
		this.bit = bit;
	}

	public RemoteControlVO() {

	}

	public RemoteControlVO(Integer id, String pointName, Integer offset,
			Integer settingValue, Integer scopeid, Integer dsid,
			Integer dataType, Integer bit) {

		this.pointID = id;
		this.pointName = pointName;
		this.offset = offset;
		this.scopeid = scopeid;
		this.settingValue = settingValue;
		this.dsid = dsid;
		this.dataType = dataType;
		this.bit = bit;
	}

}
