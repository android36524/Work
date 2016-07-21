package com.lsscl.app.dao.impl2;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lsscl.app.bean.QC;
import com.lsscl.app.bean.RSP;
import com.lsscl.app.bean2.AcpInfo;
import com.lsscl.app.bean2.AcpPointsMsgBody;
import com.lsscl.app.bean2.PointValue;
import com.lsscl.app.dao.AppDao;
import com.lsscl.app.util.StringUtil;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.db.dao.PointValueDao;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.web.dwr.AppDatasourceSettingDwr;
import com.serotonin.mango.web.taglib.Functions;

public class AcpPointsDao extends AppDao {
	private PointValueDao pointValueDao = new PointValueDao();
	private DataPointDao dataPointDao = new DataPointDao();

    private static final Log LOG = LogFactory.getLog(AcpPointsDao.class);
	@Override
	public RSP getRSP(QC qc) {
		RSP rsp = new RSP(qc.getMsgId());
		String acpId = qc.getMsgBody().get("AID");
		rsp = new RSP(qc.getMsgId());

		Map<String, String> acpInfo = getAcpInfoById(acpId);
		List<Map<String, Object>> list = ejt
				.query(acp_attr2, new Object[] { acpId },
						new com.serotonin.mango.vo.ResultData());
		System.out.println("查询语句："+acp_attr2);
		AcpPointsMsgBody msg = new AcpPointsMsgBody();
		AcpInfo acp = new AcpInfo();
		acp.setId(acpId);
		acp.setName(acpInfo.get("name"));
		long now = new Date().getTime();
		/**
		 * 读取点值
		 */
		for (Map<String, Object> m : list) {
			String name = (String) m.get("name");
			
			Integer type = (Integer) m.get("type");
			if (isBasicPointName(type))
				continue;
			PointValue acpPoint = new PointValue();
			Object pid = m.get("pointId");
			acpPoint.setPid(pid+"");
			acpPoint.setPname(name);
			Integer dpid = (Integer) pid;
//			if(!isTableExist("pointValues_"+dpid))continue;
			DataPointVO dpv = dataPointDao.getDataPoint(dpid);
			PointValueTime pvt = pointValueDao.getLatestPointValue(dpid);
			if(pvt==null)continue;
			acpPoint.setRun((now-pvt.getTime())<2*60*1000);
			acpPoint.setValue(Functions.getRenderedText(dpv, pvt));
			acpPoint.setColor(dpv.getTextRenderer().getColour(pvt.getValue()));
			msg.getPoints().add(acpPoint);
		}
		// 基本属性点：电流、压力、温度
		int p1 = getPidByAcpType(acpId, 1);
		int p2 = getPidByAcpType(acpId, 2);
		int p3 = getPidByAcpType(acpId, 3);
		if(p1!=0){
			msg.setP1(p1+"");
			msg.setV1(getValueByPid(p1));
		}
		if(p2!=0){
			msg.setP2(p2+"");
			msg.setV2(getValueByPid(p2));
		}
		if(p3!=0){
			msg.setP3(p3+"");
			msg.setV3(getValueByPid(p3));
		}
		rsp.setMsgBody(msg);
		return rsp;
	}

	
	
	private String getValueByPid(int pid){
		if(pid==0)return "";
		PointValueTime pvt = pointValueDao.getLatestPointValue(pid);
		if(pvt==null)return "";
		return StringUtil.formatNumber(pvt.getDoubleValue(), "0.0");
	}
	
}
