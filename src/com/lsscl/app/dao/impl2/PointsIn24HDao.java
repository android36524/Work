package com.lsscl.app.dao.impl2;

import java.util.Date;
import java.util.List;

import com.lsscl.app.bean.PointsWithin24MsgBody;
import com.lsscl.app.bean.QC;
import com.lsscl.app.bean.RSP;
import com.lsscl.app.dao.AppDao;
import com.mongodb.DBObject;
import com.serotonin.mango.rt.dataImage.PointValueTime;

public class PointsIn24HDao extends AppDao{

	@Override
	public RSP getRSP(QC qc) {
		RSP rsp = new RSP(qc.getMsgId());
		String stime = qc.getMsgBody().get("STIME");
		if(stime==null)return null;
		Date now = new Date(Long.parseLong(stime));
		long gt = now.getTime();
		long le = gt + 24 * 60 * 60 * 1000;
		String pid = qc.getMsgBody().get("PID");
		int pointId = Integer.parseInt(pid);
		long t1 = System.currentTimeMillis();
		List<PointValueTime> pvts = pointValueDao.getPointValuesBetween(pointId, gt, le);
		long t2 = System.currentTimeMillis();
		System.out.println("点查询时间："+(t2-t1));
		//String unit = getUnitByPid(pointId);
		//t2 = System.currentTimeMillis();
		//System.out.println("unit查询时间："+(t2-t1));
		PointsWithin24MsgBody msgBody = new PointsWithin24MsgBody();
		msgBody.setTitle("");
		msgBody.setStime(gt);
		//msgBody.setSubTitle(unit);

		msgBody.setPoints(pvts);
		msgBody.setImageType(qc.getMsgBody().get("IMGTYPE"));
		rsp.setMsgBody(msgBody);

		System.out.println("msgBody："+msgBody);
		return rsp;
	}
	
}
