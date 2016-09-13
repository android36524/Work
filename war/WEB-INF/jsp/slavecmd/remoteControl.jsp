<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp"%>
<%@page import="com.serotonin.modbus4j.code.RegisterRange"%>
<%@page import="com.serotonin.modbus4j.code.DataType"%>
<%@page import="com.serotonin.mango.Common"%>

<tag:page dwr="RemoteControlDwr" onload="init">
	<script>
		var currentPoint;

		function init() {

			if ($("scopeId").value == "")
				return;
       		writePointList();

		}
		function setPointList(response){
			var pointsList = $("pointsList");
			pointsList.innerHTML = "";
			var points = response.data.points;
			for (var i = 0; i < points.length; i++) {
				var point = points[i];
				var tr = document.createElement("tr"), tdPointName = document
						.createElement("td"), tdOffset = document
						.createElement("td"), tdtog = document
						.createElement("td"), tdEdit = document
						.createElement("td");
				tr.className = "row";
				tdPointName.innerHTML = point.pointName;
				tdOffset.innerHTML = point.offset;
				tdtog.innerHTML = '<img class="ptr" id="toggleImg'
						+ point.pointID
						+ '" src="images/brick_stop.png" onclick="togglePoint('
						+ point.pointID + ')">';
				tdEdit.innerHTML = '<a href="#" onclick="editPoint('
						+ point.pointID
						+ ')" class="editImg"><tag:img png="icon_ds_edit"
                         title="common.edit" id="editImg'+point.pointID+'"/></a>';
				tr.appendChild(tdPointName);
				tr.appendChild(tdOffset);
				tr.appendChild(tdtog);
				tr.appendChild(tdEdit);
				pointsList.appendChild(tr);
			}
		
		}

		function editPoint(pointId) {
			if (currentPoint)
				stopImageFader("editImg" + currentPoint.pointID);
			RemoteControlDwr.getPointDetailById(pointId, editPointCB);
			//hideContextualMessages("pointDetails");
		}

		function editPointCB(response) {
			currentPoint = response.data.point;
        	display("pointDeleteImg", currentPoint.pointID != <c:out value="<%= Common.NEW_ID %>"/>);
			$set("pointName", currentPoint.pointName);
			$set("offset", currentPoint.offset);
			$set("modbusDataType", currentPoint.dataType);			
       		jQuery("#dsid").val(currentPoint.dsid);		
       		jQuery("#modbusDataType").val(currentPoint.dataType);
			$set("bit", currentPoint.bit);
			$set("settingValue", currentPoint.settingValue);
			startImageFader("editImg"+ currentPoint.pointID);
			show("pointDetails");
			changeDataType('');
		}
		function togglePoint(pointId) {
		
        	$("toggleImg"+pointId).src="images/brick_go.png";
			startImageFader("toggleImg" + pointId, true);
			if (confirm("<fmt:message key="remote.control.Confirm"/>")) {
				RemoteControlDwr.rendRemoteCommand(pointId, togglePointCB);
			}else {
        		$("toggleImg"+pointId).src="images/brick_stop.png";		
				stopImageFader("toggleImg"+ pointId);
			}
		}
		
		function togglePointCB(response) {
		
			if (response.hasMessages) {
				alert(response.messages[0].genericMessage);
			} else {
				alert("<fmt:message key="remote.control.result"/>");
			}
        	$("toggleImg"+response.data.id).src="images/brick_stop.png";		
			stopImageFader("toggleImg"+ response.data.id);
		}
		
		
	    function deletePoint() {
	        if (confirm("<fmt:message key="dsEdit.deleteConfirm"/>")) {
	            RemoteControlDwr.deletePoint(currentPoint.pointID, deletePointCB);
	            startImageFader("pointDeleteImg", true);
	        }
	    }
	    
	    function deletePointCB(points) {
	        stopImageFader("pointDeleteImg");
	        hide("pointDetails");
	        currentPoint = null;
	        writePointList();
	    }
		
	    function writePointList() {
	        dwr.util.removeAllRows("pointsList");        
			RemoteControlDwr.getPointsByScopeId($("scopeId").value,setPointList);
	    }

    function savePoint() {
        startImageFader("pointSaveImg", true);
        hideContextualMessages("pointProperties");
        var locator = currentPoint;
        savePointImpl(locator);
    }
    
  function savePointImpl(locator) {
  
      locator.scopeid = $get("scopeId");
      locator.pointName = $get("pointName");
      locator.dataType = $get("modbusDataType");
      locator.offset = $get("offset");
      locator.settingValue= $get("settingValue");
      if(parseInt($get("offset"))>65535){
      	showMessage("pointMessage", "<fmt:message key='dsEdit.point.offset.validation'/>");
      	return;
      }      
      locator.dsid= $get("dsid");
      locator.bit = $get("bit");
      
      RemoteControlDwr.savePoint(locator.pointID, locator.pointName,locator.offset, locator.scopeid,locator.settingValue ,locator.dsid,locator.dataType
      ,locator.bit,savePointCB);
  }
  
    function savePointCB(response) {
        stopImageFader("pointSaveImg");
        if (response.hasMessages)
            showDwrMessages(response.messages);
        else {
            writePointList();
            if (response.data.id==<c:out value="<%= Common.NEW_ID %>"/>) { 
            	hide("pointDetails");            	
				stopImageFader("editImg" + response.data.id);
            }
            showMessage("pointMessage", "<fmt:message key="dsEdit.pointSaved"/>");
        }
    }
    
    
		
		function isBinary(prefix, dt) {
		 if (!dt)
		  dt = $get(prefix +"modbusDataType");
		    return dt == "<c:out value="<%= DataType.BINARY %>"/>";
		}  
		
		function isString(prefix) {
		 var dt = $get(prefix +"modbusDataType");
		    return dt == "<c:out value="<%= DataType.CHAR %>"/>" || dt == "<c:out value="<%= DataType.VARCHAR %>"/>";
		}

		function changeDataType(prefix) {
			if (isBinary(prefix)) {
				setDisabled("bit", false);
			} else if (isString(prefix)) {
				setDisabled("bit", true);
			} else {
				setDisabled("bit", true);
			}
		}
	</script>

	<table cellpadding="0" cellspacing="0" id="pointProperties">
		<tbody>
			<tr>
				<td valign="top">
					<div class="borderDiv marR marB">
						<table width="100%">
							<tbody>
								<tr>
									<td class="smallTitle"><fmt:message
											key="remote.control.title" /></td>
									<td align="right"><tag:img
											id="editImg${applicationScope['constants.Common.NEW_ID']}"
											png="icon_comp_add"
											onclick="editPoint(${applicationScope['constants.Common.NEW_ID']})" />
									</td>
								</tr>
							</tbody>
						</table>
						<table cellspacing="1">
							<tbody>
								<tr class="rowHeader" id="pointListHeaders">
									<td><fmt:message key="remote.control.setting.pointname" /></td>
									<td><fmt:message key="remote.control.setting.offset" /></td>
									<td><fmt:message key="remote.control" /></td>
									<td><fmt:message key="common.edit" /></td>
								</tr>
							</tbody>
							<tbody id="pointsList">
							</tbody>
						</table>
					</div>
				</td>
				<td valign="top">
					<div id="pointDetails" class="borderDiv marR marB"
						style="display: none;">
						<table>
							<tr>
								<td><span class="smallTitle"><fmt:message
											key="dsEdit.points.details" /></span> <tag:help id="${pointHelpId}" />
								</td>
								<td align="right"><tag:img id="pointSaveImg" png="save"
										onclick="savePoint()" title="common.save" /> <tag:img
										id="pointDeleteImg" png="delete" onclick="deletePoint()"
										title="common.delete" /></td>
							</tr>
						</table>
						<div id="pointMessage" class="ctxmsg formError"></div>

						<table>
							<tr>
								<td class="formLabelRequired"><fmt:message
										key="common.access.dataSource" /></td>
								<td class="formField"><select id="dsid">
										<c:forEach items="${dataSources}" var="dataSource">
											<option value="${dataSource.id}">${dataSource.name}-(<fmt:message
													key="${dataSource.type.key}" />)
											</option>
										</c:forEach>
								</select></td>
							</tr>
							<tr>
								<td class="formLabelRequired"><fmt:message
										key="dsEdit.points.name" /></td>
								<td class="formField"><input type="text" id="pointName" /></td>
							</tr>

							<tbody id="nonSlaveMonitor">

								<tr>
									<td class="formLabelRequired"><fmt:message
											key="dsEdit.modbus.modbusDataType" /></td>
									<td class="formField"><select id="modbusDataType"
										onchange="changeDataType('')">
											<option value="<c:out value="<%=DataType.BINARY%>"/>"><fmt:message
													key="dsEdit.modbus.modbusDataType.binary" /></option>
											<option
												value="<c:out value="<%=DataType.TWO_BYTE_INT_UNSIGNED%>"/>"><fmt:message
													key="dsEdit.modbus.modbusDataType.2bUnsigned" /></option>
											<option
												value="<c:out value="<%=DataType.TWO_BYTE_INT_SIGNED%>"/>"><fmt:message
													key="dsEdit.modbus.modbusDataType.2bSigned" /></option>
											<option value="<c:out value="<%=DataType.TWO_BYTE_BCD%>"/>"><fmt:message
													key="dsEdit.modbus.modbusDataType.2bBcd" /></option>
											<option
												value="<c:out value="<%=DataType.FOUR_BYTE_INT_UNSIGNED%>"/>"><fmt:message
													key="dsEdit.modbus.modbusDataType.4bUnsigned" /></option>
											<option
												value="<c:out value="<%=DataType.FOUR_BYTE_INT_SIGNED%>"/>"><fmt:message
													key="dsEdit.modbus.modbusDataType.4bSigned" /></option>
											<option
												value="<c:out value="<%=DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED%>"/>"><fmt:message
													key="dsEdit.modbus.modbusDataType.4bUnsignedSwapped" /></option>
											<option
												value="<c:out value="<%=DataType.FOUR_BYTE_INT_SIGNED_SWAPPED%>"/>"><fmt:message
													key="dsEdit.modbus.modbusDataType.4bSignedSwapped" /></option>
											<option
												value="<c:out value="<%=DataType.FOUR_BYTE_FLOAT%>"/>"><fmt:message
													key="dsEdit.modbus.modbusDataType.4bFloat" /></option>
											<option
												value="<c:out value="<%=DataType.FOUR_BYTE_FLOAT_SWAPPED%>"/>"><fmt:message
													key="dsEdit.modbus.modbusDataType.4bFloatSwapped" /></option>
											<option value="<c:out value="<%=DataType.FOUR_BYTE_BCD%>"/>"><fmt:message
													key="dsEdit.modbus.modbusDataType.4bBcd" /></option>
											<option
												value="<c:out value="<%=DataType.EIGHT_BYTE_INT_UNSIGNED%>"/>"><fmt:message
													key="dsEdit.modbus.modbusDataType.8bUnsigned" /></option>
											<option
												value="<c:out value="<%=DataType.EIGHT_BYTE_INT_SIGNED%>"/>"><fmt:message
													key="dsEdit.modbus.modbusDataType.8bSigned" /></option>
											<option
												value="<c:out value="<%=DataType.EIGHT_BYTE_INT_UNSIGNED_SWAPPED%>"/>"><fmt:message
													key="dsEdit.modbus.modbusDataType.8bUnsignedSwapped" /></option>
											<option
												value="<c:out value="<%=DataType.EIGHT_BYTE_INT_SIGNED_SWAPPED%>"/>"><fmt:message
													key="dsEdit.modbus.modbusDataType.8bSignedSwapped" /></option>
											<option
												value="<c:out value="<%=DataType.EIGHT_BYTE_FLOAT%>"/>"><fmt:message
													key="dsEdit.modbus.modbusDataType.8bFloat" /></option>
											<option
												value="<c:out value="<%=DataType.EIGHT_BYTE_FLOAT_SWAPPED%>"/>"><fmt:message
													key="dsEdit.modbus.modbusDataType.8bFloatSwapped" /></option>
											<option value="<c:out value="<%=DataType.CHAR%>"/>"><fmt:message
													key="dsEdit.modbus.modbusDataType.char" /></option>
											<option value="<c:out value="<%=DataType.VARCHAR%>"/>"><fmt:message
													key="dsEdit.modbus.modbusDataType.varchar" /></option>
									</select></td>
								</tr>

								<tr>
									<td class="formLabelRequired"><fmt:message
											key="remote.control.setting.settingValue" /></td>
									<td class="formField"><input id="settingValue" type="text" /></td>
								</tr>
								<tr>
									<td class="formLabelRequired"><fmt:message
											key="dsEdit.modbus.offset" /></td>
									<td class="formField"><input type="text" id="offset" /></td>
								</tr>

								<tr>
									<td class="formLabelRequired"><fmt:message
											key="dsEdit.modbus.bit" /></td>
									<td class="formField"><input id="bit" type="text" /></td>
								</tr>
							</tbody>
						</table>
				</td>
			</tr>
		</tbody>
	</table>
</tag:page>


<input type="hidden" id="scopeId" value="${scopeid}">