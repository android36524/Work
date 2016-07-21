package com.lsscl.app.dao.impl3;

import java.util.Random;

import com.lsscl.app.bean.QC;
import com.lsscl.app.bean.RSP;
import com.lsscl.app.bean3.ScopeBasePointStatistics;
import com.lsscl.app.dao.AppDao;

public class ScopeBasePointStatisticsDao extends AppDao{
	@Override
	public RSP getRSP(QC qc) {
		RSP rsp  = new RSP(qc.getMsgId());
		long stime = Long.valueOf(qc.getMsgBody().get("STIME"));
		long etime = Long.valueOf(qc.getMsgBody().get("ETIME"));
		String scopeId = qc.getMsgBody().get("SCOPEID"); 
		ScopeBasePointStatistics msgBody = getStatistics(stime,etime,scopeId);
		rsp.setMsgBody(msgBody);
		return rsp;
	}

	private ScopeBasePointStatistics getStatistics(long stime, long etime,String scopeId) {
		long interval = (etime-stime)/(1000*60*60);
		Random random = new Random();
		float f1 = random.nextFloat();
		float f2 = random.nextFloat();
		ScopeBasePointStatistics s = new ScopeBasePointStatistics();
		s.setRunTime(f1*interval*1.5f);
		s.setStopTime((1-f1)*interval*1.5f);
		s.setLoadTime(f2*interval*1.5f);
		s.setUnloadTime((1-f2)*interval*1.5f);
		s.setAvgLoadPower(f1*6000);
		s.setAvgUnloadPower(f2*5000);
		s.setAcpCount(random.nextInt(500));
		return s;
	}
}
