/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataImage.types;

import com.serotonin.mango.DataTypes;
import com.serotonin.util.StringUtils;

/**
 *  
 */
public class AlphanumericValue extends MangoValue implements Comparable<AlphanumericValue> {
    private final String value;

    public AlphanumericValue(String value) {
        this.value = StringUtils.escapeLT(value);
    }

    @Override
    public boolean hasDoubleRepresentation() {
        return false;
    }

    @Override
    public double getDoubleValue() {
        throw new RuntimeException(
                "AlphanumericValue has no double value. Use hasDoubleRepresentation() to check before calling this method");
    }

    @Override
    public String getStringValue() {
        return value;
    }

    @Override
    public boolean getBooleanValue() {
        throw new RuntimeException("AlphanumericValue has no boolean value.");
    }

    @Override
    public Object getObjectValue() {
        return value;
    }

    @Override
    public int getIntegerValue() {
        throw new RuntimeException("AlphanumericValue has no int value.");
    }

    @Override
    public int getDataType() {
        return DataTypes.ALPHANUMERIC;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public Number numberValue() {
        throw new RuntimeException("AlphanumericValue has no Number value.");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final AlphanumericValue other = (AlphanumericValue) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        }
        else if (!value.equals(other.value))
            return false;
        return true;
    }

    @Override
    public int compareTo(AlphanumericValue that) {
        if (value == null || that.value == null)
            return 0;
        if (value == null)
            return -1;
        if (that.value == null)
            return 1;
        return value.compareTo(that.value);
    }

    @Override
    public <T extends MangoValue> int compareTo(T that) {
        return compareTo((AlphanumericValue) that);
    }
}
