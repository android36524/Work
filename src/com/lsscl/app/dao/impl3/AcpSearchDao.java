package com.lsscl.app.dao.impl3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lsscl.app.bean.QC;
import com.lsscl.app.bean.RSP;
import com.lsscl.app.bean2.AcpInfo;
import com.lsscl.app.bean2.AcpListMsgBody;
import com.lsscl.app.dao.AppDao;
import com.lsscl.app.dao.impl.MapResultData;

public class AcpSearchDao extends AppDao {
	private static final String SQL_PAGE = " select * from ("
			+ "select row_number()over(order by a.scopeId,a.id)rn, a.id,a.name,a.type,a.scopeId,s.scopename "
			+ "from appAcps a left join ScopeTree s on s.id = a.scopeId where s.scopetype=3 and (s.scopename like ? or a.name like ? or a.serialNumber like ?))t where rn>? and rn <= ?";
	public static final String ACP_LIST_BY_SCOPEID_SQL = SCOPE_TREE_BY_PARENTID_SQL
			+ SQL_PAGE;
	public static final String ACP_LIST_BAY_PHONE_SQL = SCOPE_TREE_BY_PHONENO
			+ SQL_PAGE;

	@Override
	public RSP getRSP(QC qc) {
		RSP rsp = new RSP(qc.getMsgId());
		AcpListMsgBody msgBody = new AcpListMsgBody();
		rsp.setMsgBody(msgBody);
		String scopeId = qc.getMsgBody().get("SCOPEID");
		String page = qc.getMsgBody().get("PAGE");
		String pageSize = qc.getMsgBody().get("PAGESIZE");
		String phone = qc.getMsgBody().get("PHONENO");
		String keyword = qc.getMsgBody().get("KEYWORD");
		int startIndex = 0;
		int endIndex = 0;
		if (page != null && pageSize != null) {
			int p = Integer.valueOf(page);
			int size = Integer.valueOf(pageSize);
			startIndex = (p - 1) * size;
			endIndex = p * size;
		}
		String likeKeyword = "%" + keyword + "%";
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		if (isRootScope(phone, scopeId) && isNotAdmin(phone)) {
			result = ejt.query(ACP_LIST_BAY_PHONE_SQL,
					new Object[] { phone, likeKeyword, likeKeyword,
							likeKeyword, startIndex, endIndex },
					new MapResultData());
		} else {
			result = ejt.query(ACP_LIST_BY_SCOPEID_SQL,
					new Object[] { scopeId, likeKeyword, likeKeyword,
							likeKeyword, startIndex, endIndex },
					new MapResultData());
		}
		for (Map<String, String> m : result) {
			AcpInfo info = new AcpInfo();
			String id = m.get("id");
			info.setId(id);
			info.setScopeId(m.get("scopeId"));
			info.setName(m.get("name"));
			info.setScopeName(m.get("scopename"));
			info.setRun(isRun(Integer.valueOf(id)));
			msgBody.getAcps().add(info);
		}
		return rsp;
	}
}
