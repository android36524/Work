/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo.report;

import java.util.ResourceBundle;

import org.joda.time.DateTime;

import com.serotonin.mango.Common;
import com.serotonin.mango.util.DateUtils;
import com.serotonin.web.i18n.I18NUtils;
import com.serotonin.web.taglib.DateFunctions;

/**
 *  
 */
public class ReportInstance {
    public static final int STATE_NOT_STARTED = 1;
    public static final int STATE_STARTED = 2;
    public static final int STATE_FINISHED = 3;
    public static final int STATE_FAILED = 4;

    private int id = Common.NEW_ID;
    private int userId;
    private String name;
    private int includeEvents;
    private boolean includeUserComments;
    private long reportStartTime = -1;
    private long reportEndTime = -1;
    private long runStartTime = -1;
    private long runEndTime = -1;
    private int recordCount = -1;
    private boolean preventPurge;
    private int factoryId;
    
	private boolean intervalTime;
	private int timeType;
	private int timeNum;
    
    private ResourceBundle bundle;

    public ReportInstance() {
        // no op
    }

    public ReportInstance(ReportVO template) {
        userId = template.getUserId();
        name = template.getName();
        includeEvents = template.getIncludeEvents();
        includeUserComments = template.isIncludeUserComments();
        intervalTime=template.isIntervalTime();
        timeType=template.getTimeType();
        timeNum=template.getTimeNum();
        factoryId=template.getFactoryId();
        if (template.getDateRangeType() == ReportVO.DATE_RANGE_TYPE_RELATIVE) {
            if (template.getRelativeDateType() == ReportVO.RELATIVE_DATE_TYPE_PREVIOUS) {
                DateTime date = DateUtils.truncateDateTime(new DateTime(), template.getPreviousPeriodType());
                reportEndTime = date.getMillis();
                date = DateUtils.minus(date, template.getPreviousPeriodType(), template.getPreviousPeriodCount());
                reportStartTime = date.getMillis();
            }
            else {
                DateTime date = new DateTime();
                reportEndTime = date.getMillis();
                date = DateUtils.minus(date, template.getPastPeriodType(), template.getPastPeriodCount());
                reportStartTime = date.getMillis();
            }
        }
        else {
            if (!template.isFromNone()) {
                DateTime date = new DateTime(template.getFromYear(), template.getFromMonth(), template.getFromDay(),
                        template.getFromHour(), template.getFromMinute(), 0, 0);
                reportStartTime = date.getMillis();
            }

            if (!template.isToNone()) {
                DateTime date = new DateTime(template.getToYear(), template.getToMonth(), template.getToDay(), template
                        .getToHour(), template.getToMinute(), 0, 0);
                reportEndTime = date.getMillis();
            }
        }
    }

    public int getState() {
        if (runStartTime == -1)
            return STATE_NOT_STARTED;
        if (runEndTime == -1)
            return STATE_STARTED;
        if (recordCount == -1)
            return STATE_FAILED;
        return STATE_FINISHED;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public boolean isFromInception() {
        return reportStartTime == -1;
    }

    public boolean isToNow() {
        return reportEndTime == -1;
    }

    public String getPrettyReportStartTime() {
        if (reportStartTime == -1)
            return I18NUtils.getMessage(bundle, "common.inception");
        return DateFunctions.getFullMinuteTime(reportStartTime);
    }

    public String getPrettyReportEndTime() {
        if (reportEndTime == -1)
            return I18NUtils.getMessage(bundle, "reports.now");
        return DateFunctions.getFullMinuteTime(reportEndTime);
    }

    public String getPrettyRunStartTime() {
        if (runStartTime == -1)
            return I18NUtils.getMessage(bundle, "reports.notStarted");
        return DateFunctions.getFullMinuteTime(runStartTime);
    }

    public String getPrettyRunEndTime() {
        if (runStartTime == -1)
            return "";
        if (runEndTime == -1)
            return I18NUtils.getMessage(bundle, "reports.inProgress");
        return DateFunctions.getFullMinuteTime(runEndTime);
    }

    public String getPrettyRunDuration() {
        if (runStartTime == -1)
            return "";
        if (runEndTime == -1)
            return I18NUtils.getMessage(bundle, "reports.inProgress");
        return DateUtils.getDuration(runEndTime - runStartTime).getLocalizedMessage(bundle);
    }

    public String getPrettyRecordCount() {
        if (runStartTime == -1)
            return "";
        if (runEndTime == -1)
            return "";
        if (recordCount == -1)
            return I18NUtils.getMessage(bundle, "reports.failed");
        return Integer.toString(recordCount);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIncludeEvents() {
        return includeEvents;
    }

    public void setIncludeEvents(int includeEvents) {
        this.includeEvents = includeEvents;
    }

    public boolean isIncludeUserComments() {
        return includeUserComments;
    }

    public void setIncludeUserComments(boolean includeUserComments) {
        this.includeUserComments = includeUserComments;
    }

    public long getReportStartTime() {
        return reportStartTime;
    }

    public void setReportStartTime(long reportStartTime) {
        this.reportStartTime = reportStartTime;
    }

    public long getReportEndTime() {
        return reportEndTime;
    }

    public void setReportEndTime(long reportEndTime) {
        this.reportEndTime = reportEndTime;
    }

    public long getRunStartTime() {
        return runStartTime;
    }

    public void setRunStartTime(long runStartTime) {
        this.runStartTime = runStartTime;
    }

    public long getRunEndTime() {
        return runEndTime;
    }

    public void setRunEndTime(long runEndTime) {
        this.runEndTime = runEndTime;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public boolean isPreventPurge() {
        return preventPurge;
    }

    public void setPreventPurge(boolean preventPurge) {
        this.preventPurge = preventPurge;
    }

	public int getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(int factoryId) {
		this.factoryId = factoryId;
	}

	public boolean isIntervalTime() {
		return intervalTime;
	}

	public void setIntervalTime(boolean intervalTime) {
		this.intervalTime = intervalTime;
	}

	public int getTimeType() {
		return timeType;
	}

	public void setTimeType(int timeType) {
		this.timeType = timeType;
	}

	public int getTimeNum() {
		return timeNum;
	}

	public void setTimeNum(int timeNum) {
		this.timeNum = timeNum;
	}
}
