package com.lsscl.app.dao.impl3;

import java.util.Random;

import com.lsscl.app.bean.QC;
import com.lsscl.app.bean.RSP;
import com.lsscl.app.bean2.AcpStatistics;
import com.lsscl.app.dao.AppDao;

/**
 * 
 * @author yxx
 * 单机统计
 */
public class AcpStatisticsDao extends AppDao{

	@Override
	public RSP getRSP(QC qc) {
		RSP rsp = new RSP(qc.getMsgId());
		String acpId = qc.getMsgBody().get("AID");
		String stime = qc.getMsgBody().get("STIME");
		String etime = qc.getMsgBody().get("ETIME");
		AcpStatistics acpStatistics = getAcpStatistics(acpId,stime,etime);
		rsp.setMsgBody(acpStatistics);
		return rsp;
	}

	private AcpStatistics getAcpStatistics(String acpId, String stime,
			String etime) {
		Random random = new Random();
		float totaltime = 10000;
		AcpStatistics acpStatistics = new AcpStatistics();
		float f1 = random.nextFloat();
		float f2 = random.nextFloat();
		acpStatistics.setLoadTime(totaltime * f1);
		acpStatistics.setUnloadTime(totaltime * (1-f1));
		acpStatistics.setRunTime(totaltime * f2);
		acpStatistics.setStopTime(totaltime * (1-f2));
		acpStatistics.setAvgLoadPower(random.nextFloat()*50000);
		acpStatistics.setAvgUnLoadPower(random.nextFloat()*10000);
		return acpStatistics;
	}
}
