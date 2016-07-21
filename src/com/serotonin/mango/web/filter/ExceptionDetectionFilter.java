/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.filter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.AjaxFilter;
import org.directwebremoting.AjaxFilterChain;

import com.serotonin.mango.rt.RTException;
import com.serotonin.mango.vo.permission.PermissionException;

/**
 *  
 */
public class ExceptionDetectionFilter implements AjaxFilter {
    private static final Log LOG = LogFactory.getLog(ExceptionDetectionFilter.class);

    public Object doFilter(Object obj, Method method, Object[] params, AjaxFilterChain chain) throws Exception {
        try {
            return chain.doFilter(obj, method, params);
        }
        catch (PermissionException e) {
            throw e;
        }
        catch (RTException e) {
            throw e;
        }
        catch (Exception e) {
            Throwable e2 = e;
            if (e2 instanceof InvocationTargetException)
                e2 = ((InvocationTargetException) e).getTargetException();
            LOG.error("DWR invocation exception", e2);

            throw e;
        }
    }
}