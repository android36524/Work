/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo.dataSource.modbus;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.serotonin.json.JsonException;
import com.serotonin.json.JsonObject;
import com.serotonin.json.JsonReader;
import com.serotonin.json.JsonRemoteEntity;
import com.serotonin.json.JsonRemoteProperty;
import com.serotonin.mango.rt.dataSource.DataSourceRT;
import com.serotonin.mango.rt.dataSource.modbus.ModbusIpDataSource;
import com.serotonin.mango.rt.event.type.AuditEventType;
import com.serotonin.mango.util.LocalizableJsonException;
import com.serotonin.modbus4j.base.ModbusUtils;
import com.serotonin.util.SerializationHelper;
import com.serotonin.util.StringUtils;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.web.i18n.LocalizableMessage;
import com.serotonin.mango.db.dao.DataSourceDao;
import com.serotonin.mango.vo.dataSource.DataSourceVO;

@JsonRemoteEntity
public class ModbusIpDataSourceVO extends ModbusDataSourceVO<ModbusIpDataSourceVO> {
    public static final Type TYPE = Type.MODBUS_IP;

    public enum TransportType {
        TCP("dsEdit.modbusIp.transportType.tcp"), TCP_KEEP_ALIVE("dsEdit.modbusIp.transportType.tcpKA"), UDP(
                "dsEdit.modbusIp.transportType.udp");

        private final String key;

        TransportType(String key) {
            this.key = key;
        }

        public static TransportType valueOfIgnoreCase(String text) {
            for (TransportType type : values()) {
                if (type.name().equalsIgnoreCase(text))
                    return type;
            }
            return null;
        }

        public static List<String> getTypeList() {
            List<String> result = new ArrayList<String>();
            for (TransportType type : values())
                result.add(type.name());
            return result;
        }

        public String getKey() {
            return key;
        }
    }

    @Override
    public LocalizableMessage getConnectionDescription() {
        return new LocalizableMessage("common.default", host + ":" + port);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

    @Override
    public DataSourceRT createDataSourceRT() {
        return new ModbusIpDataSource(this);
    }

    private TransportType transportType;
    @JsonRemoteProperty
    private String host;
    @JsonRemoteProperty
    private int port = ModbusUtils.TCP_PORT;
    @JsonRemoteProperty
    private boolean encapsulated=true;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isEncapsulated() {
        return encapsulated;
    }

    public void setEncapsulated(boolean encapsulated) {
        this.encapsulated = encapsulated;
    }

    public TransportType getTransportType() {
        return transportType;
    }

    public void setTransportType(TransportType transportType) {
        this.transportType = transportType;
    }

    public String getTransportTypeStr() {
        if (transportType == null)
            return null;
        return transportType.toString();
    }

    public void setTransportTypeStr(String transportType) {
        if (transportType != null)
            this.transportType = TransportType.valueOf(transportType);
    }

    @Override
    public void validate(DwrResponseI18n response) {
        super.validate(response);
        if (transportType == null)
            response.addContextualMessage("transportType", "validate.required");
        if (StringUtils.isEmpty(host))
            response.addContextualMessage("host", "validate.required");
        if (port <= 0 || port > 0xffff)
            response.addContextualMessage("port", "validate.invalidValue");
        for (DataSourceVO<?> dsVo : new DataSourceDao().getDataSourcesForPort(3)){//MODBUS_IP
        	 ModbusIpDataSourceVO ds = (ModbusIpDataSourceVO) dsVo;
        	if(ds.getPort()==port&&super.getId()!=ds.getId()){
        		response.addContextualMessage("port", "validate.portUsed");
        	}
    	}
    }

    @Override
    protected void addPropertiesImpl(List<LocalizableMessage> list) {
        super.addPropertiesImpl(list);
        AuditEventType.addPropertyMessage(list, "dsEdit.modbusIp.transportType", transportType.getKey());
        AuditEventType.addPropertyMessage(list, "dsEdit.modbusIp.host", host);
        AuditEventType.addPropertyMessage(list, "dsEdit.modbusIp.port", port);
        AuditEventType.addPropertyMessage(list, "dsEdit.modbusIp.encapsulated", encapsulated);
    }

    @Override
    protected void addPropertyChangesImpl(List<LocalizableMessage> list, ModbusIpDataSourceVO from) {
        super.addPropertyChangesImpl(list, from);
        if (from.transportType != transportType)
            AuditEventType.addPropertyChangeMessage(list, "dsEdit.modbusIp.transportType", from.transportType.getKey(),
                    transportType.getKey());
        AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.modbusIp.host", from.host, host);
        AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.modbusIp.port", from.port, port);
        AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.modbusIp.encapsulated", from.encapsulated,
                encapsulated);
    }

    //
    // /
    // / Serialization
    // /
    //
    private static final long serialVersionUID = -1;
    private static final int version = 2;

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(version);
        out.writeObject(transportType);
        SerializationHelper.writeSafeUTF(out, host);
        out.writeInt(port);
        out.writeBoolean(encapsulated);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1) {
            transportType = (TransportType) in.readObject();
            host = SerializationHelper.readSafeUTF(in);
            port = in.readInt();
            encapsulated = false;
        }
        else if (ver == 2) {
            transportType = (TransportType) in.readObject();
            host = SerializationHelper.readSafeUTF(in);
            port = in.readInt();
            encapsulated = in.readBoolean();
        }
    }

    @Override
    public void jsonDeserialize(JsonReader reader, JsonObject json) throws JsonException {
        super.jsonDeserialize(reader, json);
        String text = json.getString("transportType");
        if (text != null) {
            transportType = TransportType.valueOfIgnoreCase(text);
            if (transportType == null)
                throw new LocalizableJsonException("emport.error.invalid", "transportType", text,
                        TransportType.getTypeList());
        }
    }

    @Override
    public void jsonSerialize(Map<String, Object> map) {
        super.jsonSerialize(map);
        map.put("transportType", transportType);
    }
}