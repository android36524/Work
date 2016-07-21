<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<%@page import="com.serotonin.modbus4j.code.RegisterRange"%>
<%@page import="com.serotonin.modbus4j.code.DataType"%>

<script type="text/javascript">
  function initImpl() {
      scanButtons(false);
      changeRange('test_');
  }
  
  function scan() {
      $set("scanMessage", "<fmt:message key="dsEdit.modbus.startScan"/>");
      dwr.util.removeAllOptions("scanNodes");
      scanButtons(true);
      scanImpl();
  }
  
  function scanCB(msg) {
      if (msg)
          alert(msg);
      else
          setTimeout(scanUpdate, 1000);
  }

  function scanUpdate() {
      DataSourceEditDwr.modbusScanUpdate(scanUpdateCB);
  }
  
  function scanUpdateCB(result) {
      if (result) {
          $set("scanMessage", result.message);
          dwr.util.removeAllOptions("scanNodes");
          dwr.util.addOptions("scanNodes", result.nodes);
          if (!result.finished)
              scanCB();
          else
              scanButtons(false);
      }
  }
  
  function scanCancel() {
      DataSourceEditDwr.cancelTestingUtility(scanButtons);
  }
  
  function scanButtons(scanning) {
      setDisabled("scanBtn", scanning);
      setDisabled("scanCancelBtn", !scanning);
  }
  
  function locatorTest() {
      setDisabled("locatorTestBtn", true);
      
      var locator = {};
      locator.slaveId = $get("test_slaveId");
      locator.range = $get("test_range");
      locator.modbusDataType = $get("test_modbusDataType");
      locator.offset = $get("test_offset");
      locator.bit = $get("test_bit");
      locator.registerCount = $get("test_registerCount");
      locator.charset = $get("test_charset");
      
      locatorTestImpl(locator);
  }
  
  function locatorTestCB(response) {
      hideContextualMessages("locatorTestDiv");
      hideGenericMessages("locatorTestGeneric");
      if (response.hasMessages) {
          // Add the prefix to the contextual messages.
          for (var i=0; i<response.messages.length; i++) {
              if (response.messages[i].contextKey)
                  response.messages[i].contextKey = "test_"+ response.messages[i].contextKey;
          }
          showDwrMessages(response.messages, "locatorTestGeneric");
          $set("locatorTestResult");
      }
      else
          $set("locatorTestResult", response.data.result);
      setDisabled("locatorTestBtn", false);
  }
  
  function dataTest() {
      setDisabled("dataTestBtn", true);
      dataTestImpl($get("dataTest_slaveId"), $get("dataTest_range"), $get("dataTest_offset"), $get("dataTest_length"));
      hideGenericMessages("dataTestGeneric");
  }
  
  function dataTestCB(response) {
	  if (response.data.length)
		  $set("dataTest_length", response.data.length);
	  
      if (response.hasMessages) {
          showDwrMessages(response.messages, "dataTestGeneric");
          hide("dataTestResults");
      }
      else {
    	  var results = "";
    	  for (var i=0; i<response.data.results.length; i++)
    		  results += response.data.results[i] +"<br/>";
          $set("dataTestResults", results);
          show("dataTestResults");
      }

      setDisabled("dataTestBtn", false);
  }
  
  function addPointImpl() {
	  DataSourceEditDwr.getPoint(-1, function(point) {
		  editPointCB(point);
	      $set("slaveId", $get("test_slaveId"));
	      $set("range", $get("test_range"));
	      $set("modbusDataType", $get("test_modbusDataType"));
	      $set("offset", $get("test_offset"));
	      $set("bit", $get("test_bit"));
	      $set("registerCount", $get("test_registerCount"));
	      $set("charset", $get("test_charset"));
          changeRange('');
	  });
  }
  
  function appendPointListColumnFunctions(pointListColumnHeaders, pointListColumnFunctions) {
      pointListColumnHeaders[pointListColumnHeaders.length] = "<fmt:message key="dsEdit.modbus.slave"/>";
      pointListColumnFunctions[pointListColumnFunctions.length] = function(p) { return p.pointLocator.slaveId; };
      
      pointListColumnHeaders[pointListColumnHeaders.length] = "<fmt:message key="dsEdit.modbus.range"/>";
      pointListColumnFunctions[pointListColumnFunctions.length] = function(p) {
          if (p.pointLocator.slaveMonitor)
              return "<fmt:message key="dsEdit.modbus.slaveMonitor"/>";
          return p.pointLocator.rangeMessage;
      };
      
      pointListColumnHeaders[pointListColumnHeaders.length] = "<fmt:message key="dsEdit.modbus.offset"/>";
      pointListColumnFunctions[pointListColumnFunctions.length] = function(p) {
    	  if (p.pointLocator.slaveMonitor)
    		  return "";
    	  if (isBinary('', p.pointLocator.modbusDataType) && !isBinaryRange('', p.pointLocator.range))
              return p.pointLocator.offset +'/'+ p.pointLocator.bit;
    	  return p.pointLocator.offset;
  	  };
  }
  
  function editPointCBImpl(locator) {
      $set("slaveId", locator.slaveId);
      $set("range", locator.range);
      $set("modbusDataType", locator.modbusDataType);
      $set("offset", locator.offset);
      $set("bit", locator.bit);
      $set("registerCount", locator.registerCount);
      $set("charset", locator.charset);
      $set("settableOverride", locator.settableOverride);
      $set("multiplier", locator.multiplier);
      $set("additive", locator.additive);

      if (locator.slaveMonitor) {
          hide("nonSlaveMonitor");
          setDisabled("slaveId", true);
      }
      else {
          setDisabled("slaveId", false);
          show("nonSlaveMonitor");
          changeRange('');
      }
  }
  
  function savePointImpl(locator) {
      delete locator.settable;
      delete locator.rangeMessage;
      delete locator.dataTypeId;
      delete locator.relinquishable;
      
      locator.slaveId = $get("slaveId");
      locator.range = $get("range");
      locator.modbusDataType = $get("modbusDataType");
      locator.offset = $get("offset");
      if(parseInt($get("offset"))>65535){
      	showMessage("pointMessage", "<fmt:message key='dsEdit.point.offset.validation'/>");
      	return;
      }      
      locator.bit = $get("bit");
      locator.registerCount = $get("registerCount");
      locator.charset = $get("charset");
      locator.settableOverride = $get("settableOverride");
      locator.multiplier = $get("multiplier");
      locator.additive = $get("additive");
      DataSourceEditDwr.saveModbusPointLocator(currentPoint.id, $get("xid"), $get("name"), locator, savePointCB);
  }
  
  function changeRange(prefix) {
      if (isBinaryRange(prefix)) {
          $set(prefix +"modbusDataType", "<c:out value="<%= DataType.BINARY %>"/>");
          setDisabled(prefix +"modbusDataType", true);
      }
      else
          setDisabled(prefix +"modbusDataType", false);
      changeDataType(prefix);
      
      var rangeId = $get(prefix +"range");
      if (rangeId == "<c:out value="<%= RegisterRange.COIL_STATUS %>"/>" || rangeId == "<c:out value="<%= RegisterRange.HOLDING_REGISTER %>"/>"|| rangeId == "<c:out value="<%= RegisterRange.HOLDING_REGISTER_88 %>"/>")
          maybeSetDisabled(prefix +"settableOverride", false);
      else {
    	  maybeSetDisabled(prefix +"settableOverride", true);
          $set(prefix +"settableOverride", false);
      }
  }
  
  function isBinary(prefix, dt) {
	  if (!dt)
		  dt = $get(prefix +"modbusDataType");
      return dt == "<c:out value="<%= DataType.BINARY %>"/>";
  }
  
  function isBinaryRange(prefix, r) {
	  if (!r)
	      r = $get(prefix +"range");
      return r == "<c:out value="<%= RegisterRange.COIL_STATUS %>"/>" || r == "<c:out value="<%= RegisterRange.INPUT_STATUS %>"/>";
  }
  
  function isString(prefix) {
	  var dt = $get(prefix +"modbusDataType");
      return dt == "<c:out value="<%= DataType.CHAR %>"/>" || dt == "<c:out value="<%= DataType.VARCHAR %>"/>";
  }
  
  function changeDataType(prefix) {
	  if (isBinary(prefix)) {
          setDisabled(prefix +"bit", isBinaryRange(prefix));
          setDisabled(prefix +"registerCount", true);
          setDisabled(prefix +"charset", true);
          maybeSetDisabled(prefix +"multiplier", true);
          maybeSetDisabled(prefix +"additive", true);
	  }
	  else if (isString(prefix)) {
          setDisabled(prefix +"bit", true);
          setDisabled(prefix +"registerCount", false);
          setDisabled(prefix +"charset", false);
          maybeSetDisabled(prefix +"multiplier", true);
          maybeSetDisabled(prefix +"additive", true);
	  }
	  else {
          setDisabled(prefix +"bit", true);
          setDisabled(prefix +"registerCount", true);
          setDisabled(prefix +"charset", true);
          maybeSetDisabled(prefix +"multiplier", false);
          maybeSetDisabled(prefix +"additive", false);
	  }
  }
  
  function maybeSetDisabled(nodeName, val) {
	  var node = $(nodeName);
	  if (node)
		  setDisabled(node, val);
  }
</script>


<%@ include file="/WEB-INF/jsp/dataSourceEdit/dsHead.jspf" %>
<%@ include file="/WEB-INF/jsp/dataSourceEdit/dsFoot.jspf" %>

<tag:pointList pointHelpId="modbusPP">
  <tr>
    <td class="formLabelRequired"><fmt:message key="dsEdit.modbus.slaveId"/></td>
    <td class="formField"><input type="text" id="slaveId"/></td>
  </tr>
  
  <tbody id="nonSlaveMonitor">
    <tr>
      <td class="formLabelRequired"><fmt:message key="dsEdit.modbus.registerRange"/></td>
      <td class="formField">
        <select id="range" onchange="changeRange('')">
          <option value="<c:out value="<%= RegisterRange.COIL_STATUS %>"/>"><fmt:message key="dsEdit.modbus.coilStatus"/></option>
          <option value="<c:out value="<%= RegisterRange.INPUT_STATUS %>"/>"><fmt:message key="dsEdit.modbus.inputStatus"/></option>
          <option value="<c:out value="<%= RegisterRange.HOLDING_REGISTER %>"/>"><fmt:message key="dsEdit.modbus.holdingRegister"/></option>
          <option value="<c:out value="<%= RegisterRange.INPUT_REGISTER %>"/>"><fmt:message key="dsEdit.modbus.inputRegister"/></option>
          <option value="<c:out value="<%= RegisterRange.HOLDING_REGISTER_88 %>"/>"><fmt:message key="dsEdit.modbus.holdingRegister_88"/></option>
        </select>
      </td>
    </tr>
    
    <tr>
      <td class="formLabelRequired"><fmt:message key="dsEdit.modbus.modbusDataType"/></td>
      <td class="formField">
        <select id="modbusDataType" onchange="changeDataType('')">
          <option value="<c:out value="<%= DataType.BINARY %>"/>"><fmt:message key="dsEdit.modbus.modbusDataType.binary"/></option>
          <option value="<c:out value="<%= DataType.TWO_BYTE_INT_UNSIGNED %>"/>"><fmt:message key="dsEdit.modbus.modbusDataType.2bUnsigned"/></option>
          <option value="<c:out value="<%= DataType.TWO_BYTE_INT_SIGNED %>"/>"><fmt:message key="dsEdit.modbus.modbusDataType.2bSigned"/></option>
          <option value="<c:out value="<%= DataType.TWO_BYTE_BCD %>"/>"><fmt:message key="dsEdit.modbus.modbusDataType.2bBcd"/></option>
          <option value="<c:out value="<%= DataType.FOUR_BYTE_INT_UNSIGNED %>"/>"><fmt:message key="dsEdit.modbus.modbusDataType.4bUnsigned"/></option>
          <option value="<c:out value="<%= DataType.FOUR_BYTE_INT_SIGNED %>"/>"><fmt:message key="dsEdit.modbus.modbusDataType.4bSigned"/></option>
          <option value="<c:out value="<%= DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED %>"/>"><fmt:message key="dsEdit.modbus.modbusDataType.4bUnsignedSwapped"/></option>
          <option value="<c:out value="<%= DataType.FOUR_BYTE_INT_SIGNED_SWAPPED %>"/>"><fmt:message key="dsEdit.modbus.modbusDataType.4bSignedSwapped"/></option>
          <option value="<c:out value="<%= DataType.FOUR_BYTE_FLOAT %>"/>"><fmt:message key="dsEdit.modbus.modbusDataType.4bFloat"/></option>
          <option value="<c:out value="<%= DataType.FOUR_BYTE_FLOAT_SWAPPED %>"/>"><fmt:message key="dsEdit.modbus.modbusDataType.4bFloatSwapped"/></option>
          <option value="<c:out value="<%= DataType.FOUR_BYTE_BCD %>"/>"><fmt:message key="dsEdit.modbus.modbusDataType.4bBcd"/></option>
          <option value="<c:out value="<%= DataType.EIGHT_BYTE_INT_UNSIGNED %>"/>"><fmt:message key="dsEdit.modbus.modbusDataType.8bUnsigned"/></option>
          <option value="<c:out value="<%= DataType.EIGHT_BYTE_INT_SIGNED %>"/>"><fmt:message key="dsEdit.modbus.modbusDataType.8bSigned"/></option>
          <option value="<c:out value="<%= DataType.EIGHT_BYTE_INT_UNSIGNED_SWAPPED %>"/>"><fmt:message key="dsEdit.modbus.modbusDataType.8bUnsignedSwapped"/></option>
          <option value="<c:out value="<%= DataType.EIGHT_BYTE_INT_SIGNED_SWAPPED %>"/>"><fmt:message key="dsEdit.modbus.modbusDataType.8bSignedSwapped"/></option>
          <option value="<c:out value="<%= DataType.EIGHT_BYTE_FLOAT %>"/>"><fmt:message key="dsEdit.modbus.modbusDataType.8bFloat"/></option>
          <option value="<c:out value="<%= DataType.EIGHT_BYTE_FLOAT_SWAPPED %>"/>"><fmt:message key="dsEdit.modbus.modbusDataType.8bFloatSwapped"/></option>
          <option value="<c:out value="<%= DataType.CHAR %>"/>"><fmt:message key="dsEdit.modbus.modbusDataType.char"/></option>
          <option value="<c:out value="<%= DataType.VARCHAR %>"/>"><fmt:message key="dsEdit.modbus.modbusDataType.varchar"/></option>
        </select>
      </td>
    </tr>
    
    <tr>
      <td class="formLabelRequired"><fmt:message key="dsEdit.modbus.offset"/></td>
      <td class="formField"><input type="text" id="offset"/></td>
    </tr>
    
    <tr>
      <td class="formLabelRequired"><fmt:message key="dsEdit.modbus.bit"/></td>
      <td class="formField"><input id="bit" type="text"/></td>
    </tr>
    
    <tr>
      <td class="formLabelRequired"><fmt:message key="dsEdit.modbus.registerCount"/></td>
      <td class="formField"><input id="registerCount" type="text"/></td>
    </tr>
    
    <tr>
      <td class="formLabelRequired"><fmt:message key="dsEdit.modbus.charset"/></td>
      <td class="formField"><input id="charset" type="text"/></td>
    </tr>
    
    <tr>
      <td class="formLabelRequired"><fmt:message key="dsEdit.modbus.settableOverride"/></td>
      <td class="formField"><input id="settableOverride" type="checkbox"/></td>
    </tr>
    
    <tr>
      <td class="formLabel"><fmt:message key="dsEdit.modbus.multiplier"/></td>
      <td class="formField"><input type="text" id="multiplier"/></td>
    </tr>
    
    <tr>
      <td class="formLabel"><fmt:message key="dsEdit.modbus.additive"/></td>
      <td class="formField"><input type="text" id="additive"/></td>
    </tr>
  </tbody>
</tag:pointList>