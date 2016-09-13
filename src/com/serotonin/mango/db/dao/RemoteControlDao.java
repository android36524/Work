package com.serotonin.mango.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import com.serotonin.db.spring.GenericRowMapper;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.DataSourceDao.DataSourceRowMapper;
import com.serotonin.mango.vo.RemoteControlVO;

public class RemoteControlDao extends BaseDao {

	public List<RemoteControlVO> getControlPointListByScopeID(int scopeId) {
		List<RemoteControlVO> result = query(
				"select id,pointName,offset,settingValue,scopeid,dsid,dataType,bit from remoteControl where scopeid = ?  ",
				new Object[] { scopeId }, new DataPointRowMapper());
		return result;
	}

	public RemoteControlVO getPointDetailByID(int pointId) {
		return queryForObject(
				" select id,pointName,offset,settingValue,scopeid,dsid,dataType,bit from remoteControl  where id=?",
				new Object[] { pointId }, new DataPointRowMapper(), null);
	}	
	
	public boolean savePoint(RemoteControlVO dp) {
		if (dp.getPointID() == Common.NEW_ID) {
			insertDataPoint(dp);
		} else {
			updateRemotePoint(dp);
		}
		return true;
	}

	public boolean deleteDataPointByID(String id) {
		ejt.update("delete from remoteControl where id =? ",
				new Object[] { id });
		return true;
	}

	public void updateRemotePoint(final RemoteControlVO dp) {
		ejt.update(
				"update remoteControl set pointName=?, offset=?, scopeid=?, settingValue=?, dsid=?, dataType=?, bit=? where id=?",
				new Object[] { dp.getPointName(), dp.getOffset(),
						dp.getScopeid(), dp.getSettingValue(), dp.getDsid(),
						dp.getDataType(), dp.getBit(), dp.getPointID() },
				new int[] { Types.VARCHAR, Types.INTEGER, Types.INTEGER,
						Types.INTEGER, Types.INTEGER, Types.INTEGER,
						Types.INTEGER, Types.INTEGER });
	}

	void insertDataPoint(final RemoteControlVO dp) {
		// Insert the main data point record.
		doInsert(
				"insert into remoteControl ( pointName, offset,settingValue,scopeid,dsid,dataType,bit) values (?,?,?,?,?,?,?)",
				new Object[] { dp.getPointName(), dp.getOffset(),
						dp.getSettingValue(), dp.getScopeid(), dp.getDsid(),
						dp.getDataType(), dp.getBit() }, new int[] {
						Types.VARCHAR, Types.INTEGER, Types.INTEGER,
						Types.INTEGER, Types.INTEGER, Types.INTEGER,
						Types.INTEGER });

	}

	class DataPointRowMapper implements GenericRowMapper<RemoteControlVO> {
		public RemoteControlVO mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			RemoteControlVO rc = new RemoteControlVO(rs.getInt(1),
					rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),
					rs.getInt(6), rs.getInt(7), rs.getInt(8));
			return rc;
		}
	}
}
