package com.lsscl.app.dao.impl3;

import java.util.List;

import com.lsscl.app.bean.QC;
import com.lsscl.app.bean.RSP;
import com.lsscl.app.bean3.AcpPowerStatistics;
import com.lsscl.app.bean3.BasePoint;
import com.lsscl.app.dao.AppDao;
import com.serotonin.mango.rt.dataImage.PointValueTime;

/**
 * 空压机功率统计
 * 
 * @author yxx
 * 
 */
public class AcpPowerStatisticsDao extends AppDao {

	@Override
	public RSP getRSP(QC qc) {
		RSP rsp = new RSP(qc.getMsgId());
		String acpId = qc.getMsgBody().get("AID");
		String stime = qc.getMsgBody().get("STIME");
		String etime = qc.getMsgBody().get("ETIME");
		long from = Long.valueOf(stime);
		long to = Long.valueOf(etime);
		List<PointValueTime> powers = getBasePoints(acpId, BasePoint.Power,
				from, to);
		List<PointValueTime> loads = getBasePoints(acpId,
				BasePoint.LoadingState, from, to);
		List<PointValueTime> runs = getBasePoints(acpId,
				BasePoint.RunningState, from, to);
		AcpPowerStatistics msgBody = new AcpPowerStatistics();
		msgBody.setPowerPoints(powers);
		msgBody.setRunningStatePoints(runs);
		msgBody.setLoadingStatePoints(loads);
		msgBody.setStartTime(from);
		msgBody.setEndTime(to);
		rsp.setMsgBody(msgBody);
		return rsp;
	}
}
