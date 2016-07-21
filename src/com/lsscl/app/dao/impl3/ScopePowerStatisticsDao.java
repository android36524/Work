package com.lsscl.app.dao.impl3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.lsscl.app.bean.QC;
import com.lsscl.app.bean.RSP;
import com.lsscl.app.bean3.ScopePowerStatistics;
import com.lsscl.app.dao.AppDao;
import com.serotonin.mango.rt.dataImage.PointValueTime;

/**
 * 区域功率统计
 * @author yxx
 *
 */
public class ScopePowerStatisticsDao extends AppDao{

	@Override
	public RSP getRSP(QC qc) {
		RSP rsp = new RSP(qc.getMsgId());
		String stime = qc.getMsgBody().get("STIME");
		String etime = qc.getMsgBody().get("ETIME");
		String duration = qc.getMsgBody().get("DURATION");
		long lstime = Long.valueOf(stime);
		long letime = Long.valueOf(etime);
		long lduration = Long.valueOf(duration);
		List<PointValueTime> pvts = getAvgPowerList(lstime,letime,lduration);
		ScopePowerStatistics msgBody = new ScopePowerStatistics();
		msgBody.setStartTime(lstime);
		msgBody.setEndTime(letime);
		msgBody.setDuration(lduration);
		msgBody.setPoints(pvts);
		rsp.setMsgBody(msgBody);
		return rsp;
	}

	private List<PointValueTime> getAvgPowerList(long lstime, long letime,
			long lduration) {
		Random random = new Random();
		int length = (int) ((letime-lstime)/lduration);
		List<PointValueTime> pvts = new ArrayList<PointValueTime>();
		for(int i=0;i<length;i++){
			long time = lstime+lduration*i;
			double value = random.nextDouble()*2000;
			PointValueTime pvt = new PointValueTime(value,time);
			pvts.add(pvt);
		}
		return pvts;
	}

	
}
