package com.lsscl.app.dao.impl3;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lsscl.app.bean.QC;
import com.lsscl.app.bean.RSP;
import com.lsscl.app.bean2.AcpInfo;
import com.lsscl.app.bean2.AcpPointsMsgBody;
import com.lsscl.app.bean2.PointValue;
import com.lsscl.app.dao.AppDao;
import com.lsscl.app.util.AcpConfig;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.db.dao.PointValueDao;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.web.taglib.Functions;

/**
 * 报警时刻机器的点数据
 * 
 * @author yxx
 * 
 */
public class AlarmAcpPointsDao extends AppDao {
	private PointValueDao pointValueDao = new PointValueDao();
	private DataPointDao dataPointDao = new DataPointDao();

	@Override
	public RSP getRSP(QC qc) {
		RSP rsp = new RSP(qc.getMsgId());
		String acpId = qc.getMsgBody().get("AID");
		String atime = qc.getMsgBody().get("TIME");
		String alarmPointId = qc.getMsgBody().get("PID");
		long time = Long.valueOf(atime);
		rsp = new RSP(qc.getMsgId());

		Map<String, String> acpInfo = getAcpInfoById(acpId);
		List<Map<String, Object>> list = ejt
				.query(acp_attr2, new Object[] { acpId },
						new com.serotonin.mango.vo.ResultData());
		AcpPointsMsgBody msg = new AcpPointsMsgBody();
		AcpInfo acp = new AcpInfo();
		acp.setId(acpId);
		acp.setName(acpInfo.get("name"));
		/**
		 * 读取点值
		 */
		for (Map<String, Object> m : list) {
			String name = (String) m.get("name");
			int type = (Integer) m.get("type");
			PointValue acpPoint = new PointValue();
			Object pid = m.get("pointId");
			acpPoint.setPid(pid + "");
			acpPoint.setPname(name);
			acpPoint.setType(type);
			Integer dpid = (Integer) pid;
			// if(!isTableExist("pointValues_"+dpid))continue;
			DataPointVO dpv = dataPointDao.getDataPoint(dpid);
			PointValueTime pvt = pointValueDao.getPointValueAt(dpid, time);
			if (pvt == null)
				continue;
			acpPoint.setValue(Functions.getRenderedText(dpv, pvt));
			acpPoint.setColor(dpv.getTextRenderer().getColour(pvt.getValue()));
			if ((dpid + "").equals(alarmPointId)) {//如果是报警点排在最前
				msg.getPoints().add(0, acpPoint);
			}else{
				msg.getPoints().add(acpPoint);
			}
		}
		// 基本属性点：电流、压力、温度
		rsp.setMsgBody(msg);

		return rsp;
	}
}
