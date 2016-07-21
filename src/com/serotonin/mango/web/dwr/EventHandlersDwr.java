/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.dwr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.WebContextFactory;
import com.serotonin.mango.db.dao.power.UserEventLimtDao;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.CompoundEventDetectorDao;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.db.dao.DataSourceDao;
import com.serotonin.mango.db.dao.EventDao;
import com.serotonin.mango.db.dao.MailingListDao;
import com.serotonin.mango.db.dao.MaintenanceEventDao;
import com.serotonin.mango.db.dao.PublisherDao;
import com.serotonin.mango.db.dao.ScheduledEventDao;
import com.serotonin.mango.db.dao.UserDao;
import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.mango.rt.event.type.AuditEventType;
import com.serotonin.mango.rt.event.type.SystemEventType;
import com.serotonin.mango.rt.maint.work.ProcessWorkItem;
import com.serotonin.mango.view.text.TextRenderer;
import com.serotonin.mango.vo.DataPointExtendedNameComparator;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.dataSource.DataSourceVO;
import com.serotonin.mango.vo.event.CompoundEventDetectorVO;
import com.serotonin.mango.vo.event.EventHandlerVO;
import com.serotonin.mango.vo.event.EventTypeVO;
import com.serotonin.mango.vo.event.MaintenanceEventVO;
import com.serotonin.mango.vo.event.PointEventDetectorVO;
import com.serotonin.mango.vo.event.ScheduledEventVO;
import com.serotonin.mango.vo.permission.Permissions;
import com.serotonin.mango.vo.publish.PublishedPointVO;
import com.serotonin.mango.vo.publish.PublisherVO;
import com.serotonin.mango.web.dwr.beans.DataPointBean;
import com.serotonin.mango.web.dwr.beans.EventSourceBean;
import com.serotonin.mango.web.dwr.beans.RecipientListEntryBean;
import com.serotonin.util.StringUtils;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.web.i18n.LocalizableMessage;

public class EventHandlersDwr extends BaseDwr {
    private static final Log LOG = LogFactory.getLog(EventHandlersDwr.class);

    private final ResourceBundle setPointSnippetMap = ResourceBundle.getBundle("setPointSnippetMap");

    public Map<String, Object> getInitData() {
        User user = Common.getUser();
        Permissions.ensureDataSourcePermission(user);
        int factoryId=user.getCurrentScope().getId();
        EventDao eventDao = new EventDao();
        Map<String, Object> model = new HashMap<String, Object>();

        // Get the data points
        List<DataPointBean> allPoints = new ArrayList<DataPointBean>();
        List<EventSourceBean> dataPoints = new ArrayList<EventSourceBean>();
        List<DataPointVO> dps = new DataPointDao().getDataPoints(factoryId,DataPointExtendedNameComparator.instance, true);
        for (DataPointVO dp : dps) {
            if (!Permissions.hasDataSourcePermission(user, dp.getDataSourceId()))
                continue;

            allPoints.add(new DataPointBean(dp));

            if (dp.getEventDetectors().size() > 0) {
                EventSourceBean source = new EventSourceBean();
                source.setId(dp.getId());
                source.setName(dp.getExtendedName());

                for (PointEventDetectorVO ped : dp.getEventDetectors()) {
                    EventTypeVO dpet = ped.getEventType();
                    dpet.setHandlers(eventDao.getEventHandlers(dpet));
                    source.getEventTypes().add(dpet);
                }

                dataPoints.add(source);
            }
        }

        // Get the scheduled events
        List<EventTypeVO> scheduledEvents = new ArrayList<EventTypeVO>();
        List<ScheduledEventVO> ses = new ScheduledEventDao().getScheduledEvents(factoryId);
        for (ScheduledEventVO se : ses) {
            EventTypeVO et = se.getEventType();
            et.setHandlers(eventDao.getEventHandlers(et));
            scheduledEvents.add(et);
        }
        model.put("scheduledEvents", scheduledEvents);

        // Get the compound event detectors
        List<EventTypeVO> compoundEvents = new ArrayList<EventTypeVO>();
        List<CompoundEventDetectorVO> ceds = new CompoundEventDetectorDao().getCompoundEventDetectors(factoryId);
        for (CompoundEventDetectorVO ced : ceds) {
            EventTypeVO et = ced.getEventType();
            et.setHandlers(eventDao.getEventHandlers(et));
            compoundEvents.add(et);
        }
        model.put("compoundEvents", compoundEvents);

        // Get the data sources
        List<EventSourceBean> dataSources = new ArrayList<EventSourceBean>();
        for (DataSourceVO<?> ds : new DataSourceDao().getDataSources()) {
            if (!Permissions.hasDataSourcePermission(user, ds.getId()))
                continue;

            if (ds.getEventTypes().size() > 0) {
                EventSourceBean source = new EventSourceBean();
                source.setId(ds.getId());
                source.setName(ds.getName());

                for (EventTypeVO dset : ds.getEventTypes()) {
                    dset.setHandlers(eventDao.getEventHandlers(dset));
                    source.getEventTypes().add(dset);
                }
                dataSources.add(source);
            }
        }

        if (Permissions.hasAdmin(user)) {
        	/**
            // Get the publishers
            List<EventSourceBean> publishers = new ArrayList<EventSourceBean>();
            for (PublisherVO<? extends PublishedPointVO> p : new PublisherDao()
                    .getPublishers(new PublisherDao.PublisherNameComparator())) {
                if (p.getEventTypes().size() > 0) {
                    EventSourceBean source = new EventSourceBean();
                    source.setId(p.getId());
                    source.setName(p.getName());

                    for (EventTypeVO pet : p.getEventTypes()) {
                        pet.setHandlers(eventDao.getEventHandlers(pet));
                        source.getEventTypes().add(pet);
                    }

                    publishers.add(source);
                }
            }
            model.put("publishers", publishers);

            // Get the maintenance events
            List<EventTypeVO> maintenanceEvents = new ArrayList<EventTypeVO>();
            List<MaintenanceEventVO> mes = new MaintenanceEventDao().getMaintenanceEvents();
            for (MaintenanceEventVO me : mes) {
                EventTypeVO et = me.getEventType();
                et.setHandlers(eventDao.getEventHandlers(et));
                maintenanceEvents.add(et);
            }
            model.put("maintenanceEvents", maintenanceEvents);
 * 
 */
            // Get the system events
            List<EventTypeVO> systemEvents = new ArrayList<EventTypeVO>();
            for (EventTypeVO sets : SystemEventType.getSystemEventTypes()) {
                sets.setHandlers(eventDao.getEventHandlers(sets));
                systemEvents.add(sets);
            }
            model.put("systemEvents", systemEvents);
/**
            // Get the audit events
            List<EventTypeVO> auditEvents = new ArrayList<EventTypeVO>();
            for (EventTypeVO aets : AuditEventType.getAuditEventTypes()) {
                aets.setHandlers(eventDao.getEventHandlers(aets));
                auditEvents.add(aets);
            }
            model.put("auditEvents", auditEvents);
*/
        }

        // Get the mailing lists.
        model.put("mailingLists", new MailingListDao().getMailingLists(factoryId));

        // Get the users.
        //FIXME 更改
		model.put("users", new UserDao().getUsersAndParentUsers(factoryId));

        model.put("allPoints", allPoints);
        model.put("dataPoints", dataPoints);
        model.put("dataSources", dataSources);

        return model;
    }

    public String createSetValueContent(int pointId, String valueStr, String idSuffix) {
        DataPointVO pointVO = new DataPointDao().getDataPoint(pointId);
        Permissions.ensureDataSourcePermission(Common.getUser(), pointVO.getDataSourceId());

        MangoValue value = MangoValue.stringToValue(valueStr, pointVO.getPointLocator().getDataTypeId());

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("point", pointVO);
        model.put("idSuffix", idSuffix);
        model.put("text", pointVO.getTextRenderer().getText(value, TextRenderer.HINT_FULL));
        model.put("rawText", pointVO.getTextRenderer().getText(value, TextRenderer.HINT_RAW));
        String snippet = setPointSnippetMap.getString("com.serotonin.mango.view.text.AnalogRenderer");
        return generateContent(WebContextFactory.get().getHttpServletRequest(), snippet, model);
    }

    public DwrResponseI18n saveSetPointEventHandler(int scopeId, int eventSourceId, int eventTypeRef1, int eventTypeRef2,
            int handlerId, String xid, String alias, boolean disabled,boolean disabledChange, int targetPointId, int activeAction,
            String activeValueToSet, int activePointId, int inactiveAction, String inactiveValueToSet,
            int inactivePointId) {
        EventHandlerVO handler = new EventHandlerVO();
        handler.setDisableChange(disabledChange);
        handler.setHandlerType(EventHandlerVO.TYPE_SET_POINT);
        handler.setTargetPointId(targetPointId);
        handler.setActiveAction(activeAction);
        handler.setActiveValueToSet(activeValueToSet);
        handler.setActivePointId(activePointId);
        handler.setInactiveAction(inactiveAction);
        handler.setInactiveValueToSet(inactiveValueToSet);
        handler.setInactivePointId(inactivePointId);
        return save(scopeId,eventSourceId, eventTypeRef1, eventTypeRef2, handler, handlerId, xid, alias, disabled);
    }
/**
 * 保存一个事件处理器
 * @param eventSourceId
 * @param eventTypeRef1
 * @param eventTypeRef2
 * @param handlerId
 * @param xid
 * @param alias
 * @param disabled
 * @param activeRecipients
 * @param sendEscalation
 * @param escalationDelayType
 * @param escalationDelay
 * @param escalationRecipients
 * @param sendInactive
 * @param inactiveOverride
 * @param inactiveRecipients
 * @return
 */
    public DwrResponseI18n saveEmailEventHandler(int scopeId,int eventSourceId, int eventTypeRef1, int eventTypeRef2,
            int handlerId, String xid, String alias, boolean disabled,boolean disabledChange, List<RecipientListEntryBean> activeRecipients,
            boolean useSMS,boolean sendEscalation, int escalationDelayType, int escalationDelay,
            List<RecipientListEntryBean> escalationRecipients,boolean sendEscalation2, int escalationDelayType2, int escalationDelay2,
            List<RecipientListEntryBean> escalationRecipients2,  boolean sendInactive, boolean inactiveOverride,
            List<RecipientListEntryBean> inactiveRecipients) {
        EventHandlerVO handler = new EventHandlerVO();
        handler.setDisableChange(disabledChange);
        handler.setHandlerType(EventHandlerVO.TYPE_EMAIL);
        handler.setActiveRecipients(activeRecipients);
        handler.setUseSMS(useSMS);
        handler.setSendEscalation(sendEscalation);
        handler.setEscalationDelayType(escalationDelayType);
        handler.setEscalationDelay(escalationDelay);
        handler.setEscalationRecipients(escalationRecipients);
        
        handler.setSendEscalation2(sendEscalation2);
        handler.setEscalationDelayType2(escalationDelayType2);
        handler.setEscalationDelay2(escalationDelay2);
        handler.setEscalationRecipients2(escalationRecipients2);
        
        handler.setSendInactive(sendInactive);
        handler.setInactiveOverride(inactiveOverride);
        handler.setInactiveRecipients(inactiveRecipients);
        return save(scopeId,eventSourceId, eventTypeRef1, eventTypeRef2, handler, handlerId, xid, alias, disabled);
    }

    public DwrResponseI18n saveProcessEventHandler(int scopeId,int eventSourceId, int eventTypeRef1, int eventTypeRef2,
            int handlerId, String xid, String alias, boolean disabled,boolean disabledChange, String activeProcessCommand,
            String inactiveProcessCommand) {
        EventHandlerVO handler = new EventHandlerVO();
        handler.setDisableChange(disabledChange);
        handler.setHandlerType(EventHandlerVO.TYPE_PROCESS);
        handler.setActiveProcessCommand(activeProcessCommand);
        handler.setInactiveProcessCommand(inactiveProcessCommand);
        return save(scopeId,eventSourceId, eventTypeRef1, eventTypeRef2, handler, handlerId, xid, alias, disabled);
    }

    private DwrResponseI18n save(int scopeId,int eventSourceId, int eventTypeRef1, int eventTypeRef2, EventHandlerVO vo,
            int handlerId, String xid, String alias, boolean disabled) {
        DwrResponseI18n response = new DwrResponseI18n();
        EventTypeVO type = new EventTypeVO(eventSourceId, eventTypeRef1, eventTypeRef2);
        Permissions.ensureEventTypePermission(Common.getUser(), type);
        EventDao eventDao = new EventDao();
        User user = Common.getUser();
        //这里验证用户是否可以修改
        	if(user.getDefaultRole().getId()==7){
        		if(vo.isDisableChange()){
        			response.addMessage(new LocalizableMessage("common.disableChange"));
        	    }
        		if(new UserEventLimtDao().getUserLimit(scopeId)<=0&&handlerId== Common.NEW_ID){
        			response.addMessage(new LocalizableMessage("users.handlerLimit.overLimit"));
        		}
        }
        vo.setId(handlerId);
        vo.setXid(StringUtils.isEmpty(xid) ? eventDao.generateUniqueXid() : xid);
        vo.setAlias(alias);
        vo.setDisabled(disabled);
        vo.validate(response);
        
        if (!response.getHasMessages()) {
            eventDao.saveEventHandler(scopeId,type, vo);
            if(handlerId== Common.NEW_ID){//&&user.getDefaultRole().getId()>=5
            	//这里执行修改用户保存数量
            	new UserEventLimtDao().updateUserEventHandlersCount(scopeId);
            }
            response.addData("handler", vo);
        }
        return response;
    }

    public void deleteEventHandler(int handlerId) {
        EventDao eventDao = new EventDao();
        Permissions.ensureEventTypePermission(Common.getUser(), eventDao.getEventHandlerType(handlerId));
        eventDao.deleteEventHandler(handlerId);
        //减少工厂事件处理器的一个Count
        new UserEventLimtDao().deleteCount(Common.getUser().getCurrentScope().getId());
    }

    public LocalizableMessage testProcessCommand(String command) {
        if (StringUtils.isEmpty(command))
            return null;

        try {
            ProcessWorkItem.executeProcessCommand(command);
            return new LocalizableMessage("eventHandlers.commandTest.result");
        }
        catch (IOException e) {
            LOG.warn("Process error", e);
            return new LocalizableMessage("common.default", e.getMessage());
        }
    }
}
