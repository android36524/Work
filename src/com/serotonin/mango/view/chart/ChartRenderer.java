/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.view.chart;

import java.io.Serializable;
import java.util.Map;

import com.serotonin.mango.view.ImplDefinition;
import com.serotonin.mango.vo.DataPointVO;

public interface ChartRenderer extends Serializable {
    public static final int TYPE_NONE = 1;
    public static final int TYPE_TABLE = 2;
    public static final int TYPE_IMAGE = 3;
    public static final int TYPE_STATS = 4;

    public String getTypeName();

    public void addDataToModel(Map<String, Object> model, DataPointVO point);

    public ImplDefinition getDef();
}
