/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.impl.DefaultWebContextBuilder;

/**
 *  
 */
public class WebContextFilter implements Filter {
    private final DefaultWebContextBuilder builder = new DefaultWebContextBuilder();
    private ServletContext servletContext;

    @Override
    public void init(FilterConfig config) {
        servletContext = config.getServletContext();
    }

    @Override
    public void destroy() {
        // no op
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        try {
            builder.set((HttpServletRequest) request, (HttpServletResponse) response, null, servletContext, null);
            chain.doFilter(request, response);
        }
        finally {
            builder.unset();
        }
    }
}
