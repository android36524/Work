/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo.dataSource.spinwave;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.serotonin.json.JsonRemoteProperty;
import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.mango.rt.dataSource.PointLocatorRT;
import com.serotonin.mango.rt.dataSource.spinwave.SpinwavePointLocatorRT;
import com.serotonin.mango.rt.event.type.AuditEventType;
import com.serotonin.mango.vo.dataSource.AbstractPointLocatorVO;
import com.serotonin.spinwave.SwMessage;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.web.i18n.LocalizableMessage;

/**
 *  
 */
abstract public class BaseSpinwavePointLocatorVO extends AbstractPointLocatorVO {
    public boolean isSettable() {
        return false;
    }

    public PointLocatorRT createRuntime() {
        return new SpinwavePointLocatorRT(this);
    }

    public LocalizableMessage getConfigurationDescription() {
        return new LocalizableMessage("dsEdit.spinwave.dpconn", sensorAddress, new LocalizableMessage(
                getAttributeDescription()));
    }

    abstract public MangoValue getValue(SwMessage msg);

    abstract public String getAttributeDescription();

    @JsonRemoteProperty
    private int sensorAddress;
    @JsonRemoteProperty
    private int attributeId;
    @JsonRemoteProperty
    private boolean convertToCelsius;

    public int getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(int attributeId) {
        this.attributeId = attributeId;
    }

    public boolean isConvertToCelsius() {
        return convertToCelsius;
    }

    public void setConvertToCelsius(boolean convertToCelsius) {
        this.convertToCelsius = convertToCelsius;
    }

    public int getSensorAddress() {
        return sensorAddress;
    }

    public void setSensorAddress(int sensorAddress) {
        this.sensorAddress = sensorAddress;
    }

    public void validate(DwrResponseI18n response) {
        if (sensorAddress <= 1)
            response.addContextualMessage("sensorAddress", "validate.required");
    }

    @Override
    public void addProperties(List<LocalizableMessage> list) {
        AuditEventType.addPropertyMessage(list, "dsEdit.spinwave.sensorAddress", sensorAddress);
        AuditEventType.addPropertyMessage(list, "dsEdit.spinwave.attribute", attributeId);
        AuditEventType.addPropertyMessage(list, "dsEdit.spinwave.convert", convertToCelsius);
    }

    @Override
    public void addPropertyChanges(List<LocalizableMessage> list, Object o) {
        BaseSpinwavePointLocatorVO from = (BaseSpinwavePointLocatorVO) o;
        AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.spinwave.sensorAddress", from.sensorAddress,
                sensorAddress);
        AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.spinwave.attribute", from.attributeId, attributeId);
        AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.spinwave.convert", from.convertToCelsius,
                convertToCelsius);
    }

    //
    // /
    // / Serialization
    // /
    //
    private static final long serialVersionUID = -1;
    private static final int version = 1;

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(version);
        out.writeInt(sensorAddress);
        out.writeInt(attributeId);
        out.writeBoolean(convertToCelsius);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1) {
            sensorAddress = in.readInt();
            attributeId = in.readInt();
            convertToCelsius = in.readBoolean();
        }
    }
}
