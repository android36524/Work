package com.lsscl.app.bean3;

public enum BasePoint {
	Current(1), // 电流
	ExhaustPressure(2), // 排气压力
	ExhaustTemperature(3), // 排气温度
	Power(4), // 功率
	RunningState(5), // 运行状态
	LoadingState(6), // 加卸载状态
	LoadingTime(7), // 加载时间
	RunTime(8), // 运行时间
	StopTime(9);// 停机时间

	private int type;

	private BasePoint(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public static BasePoint valueOf(int type) {
		switch (type) {
		case 1:
			return BasePoint.Current;
		case 2:
			return BasePoint.ExhaustPressure;
		case 3:
			return BasePoint.ExhaustTemperature;
		case 4:
			return BasePoint.Power;
		case 5:
			return BasePoint.RunningState;
		case 6:
			return BasePoint.LoadingState;
		case 7:
			return BasePoint.LoadingTime;
		case 8:
			return BasePoint.RunTime;
		case 9:
			return BasePoint.StopTime;
		default:
			return null;
		}
	}
}
