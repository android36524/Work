package com.serotonin.mango.web.dwr;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lsscl.app.dao.AppsettingDao;
import com.serotonin.mango.Common;
import com.serotonin.mango.DataTypes;
import com.serotonin.mango.db.dao.DataSourceDao;
import com.serotonin.mango.db.dao.RemoteControlDao;
import com.serotonin.mango.rt.dataImage.DataPointRT;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.rt.dataImage.SetPointSource;
import com.serotonin.mango.rt.dataSource.modbus.ModbusPointLocatorRT;
import com.serotonin.mango.vo.AppPoints;
import com.serotonin.mango.vo.RemoteControlVO;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.dataSource.DataSourceVO;
import com.serotonin.mango.vo.dataSource.DataSourceVO.Type;
import com.serotonin.mango.vo.dataSource.modbus.ModbusDataSourceVO;
import com.serotonin.mango.vo.dataSource.modbus.ModbusIpDataSourceVO;
import com.serotonin.mango.vo.dataSource.modbus.ModbusIpDataSourceVO.TransportType;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.web.dwr.MethodFilter;
import com.serotonin.web.i18n.LocalizableMessage;

public class RemoteControlDwr extends BaseDwr {

	private static final Log LOG = LogFactory.getLog(RemoteControlDwr.class);
	private RemoteControlDao dao;

	public DwrResponseI18n init() {
		DwrResponseI18n response = new DwrResponseI18n();

		return response;

	}

	@MethodFilter
	public DwrResponseI18n getPointDetailById(String pointId) {
		dao = new RemoteControlDao();
		DwrResponseI18n response = new DwrResponseI18n();
		RemoteControlVO pointDetail = new RemoteControlVO();
		if (Integer.parseInt(pointId) != Common.NEW_ID) {
			pointDetail = dao.getPointDetailByID(Integer.valueOf(pointId));
		}
		response.addData("point", pointDetail);
		return response;
	}

	@MethodFilter
	public DwrResponseI18n getPointsByScopeId(String scopeId) {
		dao = new RemoteControlDao();
		DwrResponseI18n response = new DwrResponseI18n();
		List<RemoteControlVO> points = dao.getControlPointListByScopeID(Integer
				.valueOf(scopeId));
		response.addData("points", points);
		return response;
	}

	@MethodFilter
	public DwrResponseI18n deletePoint(String pointID) {
		dao = new RemoteControlDao();
		DwrResponseI18n response = new DwrResponseI18n();
		boolean ret = true;

		ret = dao.deleteDataPointByID(pointID);
		if (!ret)
			response.addMessage(new LocalizableMessage("remote.Error"));

		return response;
	}

	@SuppressWarnings("null")
	@MethodFilter
	public DwrResponseI18n rendRemoteCommand(String pointId) {
		DwrResponseI18n response = new DwrResponseI18n();
		dao = new RemoteControlDao();
		RemoteControlVO rc = dao.getPointDetailByID(Integer.parseInt(pointId));
		DataSourceVO<?> vo = Common.ctx.getRuntimeManager().getDataSource(
				rc.getDsid());

		if (vo.getType() == Type.MODBUS_IP) {
			ModbusIpDataSourceVO mv = (ModbusIpDataSourceVO) vo;
	        IpParameters params = new IpParameters();
	        params.setHost(mv.getHost());
	        params.setPort(mv.getPort());
	        params.setEncapsulated(mv.isEncapsulated());
	        ModbusMaster modbusMaster;
	        if (mv.getTransportType() == TransportType.UDP)
	            modbusMaster = new ModbusFactory().createUdpMaster(params);
	        else
	            modbusMaster = new ModbusFactory().createTcpMaster(params,
	            		mv.getTransportType() == TransportType.TCP_KEEP_ALIVE);
	        
			modbusMaster.setTimeout(mv.getTimeout());
			modbusMaster.setRetries(mv.getRetries());
			modbusMaster.setMaxReadBitCount(mv.getMaxReadBitCount());
			modbusMaster.setMaxReadRegisterCount(mv.getMaxReadRegisterCount());
			modbusMaster
					.setMaxWriteRegisterCount(mv.getMaxWriteRegisterCount());
			response.addData("id", rc.getPointID());
			try {
				modbusMaster.init();
				BaseLocator<?> ml = BaseLocator.createLocator(1, 6,
						rc.getOffset(), System.currentTimeMillis(),
						rc.getDataType(), rc.getBit(), 100,
						Charset.forName("ASCII"));
				modbusMaster.setValue(ml, rc.getSettingValue());
			} catch (ModbusInitException e) {
				response.addMessage(new LocalizableMessage("remote.Error"));
				return response;
			} catch (ModbusTransportException e) {
				response.addMessage(new LocalizableMessage("remote.Error"));
				return response;
			} catch (ErrorResponseException e) {
				response.addMessage(new LocalizableMessage("remote.Error"));
				return response;
			}
		}
		return response;
	}

	@MethodFilter
	public DwrResponseI18n savePoint(String pointID, String pointName,
			String offset, String scopeid, String settingValue, String dsid,
			String dataType, String bit) {
		dao = new RemoteControlDao();
		boolean ret = true;
		DwrResponseI18n response = new DwrResponseI18n();
		RemoteControlVO rc = new RemoteControlVO();
		rc.setPointID(Integer.parseInt(pointID));
		rc.setPointName(pointName);
		rc.setBit(Integer.parseInt(bit));
		rc.setDataType(Integer.parseInt(dataType));
		rc.setDsid(Integer.parseInt(dsid));
		rc.setOffset(Integer.parseInt(offset));
		rc.setScopeid(Integer.parseInt(scopeid));
		rc.setSettingValue(Integer.parseInt(settingValue));
		ret = dao.savePoint(rc);

		if (!ret)
			response.addMessage(new LocalizableMessage("remote.Error"));

		response.addData("id", rc.getPointID());
		return response;
	}

}
