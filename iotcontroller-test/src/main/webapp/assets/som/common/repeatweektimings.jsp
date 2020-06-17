<%@ page import="com.firstis.common.util.general.*,com.firstis.telaweb.beans.*,com.firstis.telaweb.beans.section.*,java.util.*,com.firstis.common.util.dateformatter.*, com.firstis.telaweb.dao.teladom.SectKwdDom, com.firstis.common.util.validation.*" %>
<%
	String	selectString = "";
        String	elementIndex	= (String) request.getParameter("ELEMENT_INDEX");
	int		elementIndexVal	= 0;
	
	try {
		elementIndexVal = Integer.parseInt(elementIndex);
	} catch (Exception e) {
	}
        DefnEditBean[] lScheduleDefnArray = (DefnEditBean[]) request.getAttribute("BASICSCHEDULEBEAN");
%>

<html>
<head>
<title>Week-Time</title>
<script language="JavaScript" src="<%= request.getContextPath() %>/rotation/rotation.js"></script>
<script language="JavaScript">

<!--
        var arrayObjects = new Array(<%= lScheduleDefnArray.length %>)
        <%
            for (int i = 0; i < lScheduleDefnArray.length; i++) {
                DefnEditBean lScheduleBean = lScheduleDefnArray[i];
                DefnKwdEditBean[] lKwdArray = lScheduleBean.getKwdArray();
                %>var scheduleObjects = new Array(4)
                  scheduleObjects[0] = '<%= lScheduleBean.getDefnId() %>'
                  scheduleObjects[1] = '<%= lScheduleBean.getDefnName() %>'
                  var kwdValueObjects = new Array(<%= lKwdArray.length %>)
                  var kwdNameObjects = new Array(<%= lKwdArray.length %>)
                <%
                for (int j = 0; j < lKwdArray.length; j++) {
                    DefnKwdEditBean lKwdBean = lKwdArray[j];
                    %>kwdValueObjects[<%= j %>] = '<%= lKwdBean.getKwdValue() %>';
                    kwdNameObjects[<%= j %>] = '<%= lKwdBean.getKwdName() %>'; <%
                }
                %>
                scheduleObjects[2] = kwdNameObjects;
                scheduleObjects[3] = kwdValueObjects;
                arrayObjects[<%= i %>] = scheduleObjects;
                <%
            }
        %>

        function setDataForSelectedSchedule(dataSelected) {
            if (dataSelected == "") {
                    document.calendarForm.def_time.value = "";
                    document.calendarForm.sun_time.value = "";
                    document.calendarForm.mon_time.value = "";
                    document.calendarForm.tue_time.value = "";
                    document.calendarForm.wed_time.value = "";
                    document.calendarForm.thu_time.value = "";
                    document.calendarForm.fri_time.value = "";
                    document.calendarForm.sat_time.value = "";
                    document.calendarForm.scheduleId.value = "";
                    document.calendarForm.scheduleName.value = "";
            } else {
                var dataObject;
                for (var i = 0; i < arrayObjects.length; i++) {
                    var scheduleObjects = arrayObjects[i];
                    if (scheduleObjects[0] == dataSelected) {
                        dataObject = scheduleObjects;
                    }
                }
                document.calendarForm.scheduleId.value = dataObject[0];
                document.calendarForm.scheduleName.value = dataObject[1];
                var kwdNameObjects = dataObject[2];
                var kwdValueObjects = dataObject[3];
                for (var i = 0; i < kwdNameObjects.length; i++) {
                    var data = kwdValueObjects[i];

                    if ('<%= SectKwdDom.KWDNAME_DEFAULTDAY %>' == kwdNameObjects[i]) {
                        document.calendarForm.def_time.value = data;
                    } else if ('<%= SectKwdDom.KWDNAME_SUNDAY %>' == kwdNameObjects[i]) {
                        document.calendarForm.sun_time.value = data
                    } else if ('<%= SectKwdDom.KWDNAME_MONDAY %>' == kwdNameObjects[i]) {
                        document.calendarForm.mon_time.value = data;
                    } else if ('<%= SectKwdDom.KWDNAME_TUESDAY %>' == kwdNameObjects[i]) {
                        document.calendarForm.tue_time.value = data;
                    } else if ('<%= SectKwdDom.KWDNAME_WEDNESDAY %>' == kwdNameObjects[i]) {
                        document.calendarForm.wed_time.value = data;
                    } else if ('<%= SectKwdDom.KWDNAME_THURSDAY %>' == kwdNameObjects[i]) {
                        document.calendarForm.thu_time.value = data;
                    } else if ('<%= SectKwdDom.KWDNAME_FRIDAY %>' == kwdNameObjects[i]) {
                        document.calendarForm.fri_time.value = data;
                    } else if ('<%= SectKwdDom.KWDNAME_SATURDAY %>' == kwdNameObjects[i]) {
                    document.calendarForm.sat_time.value = data;
                    }
                }
                if (kwdNameObjects.length == 0) {
                    document.calendarForm.def_time.value = "";
                    document.calendarForm.sun_time.value = "";
                    document.calendarForm.mon_time.value = "";
                    document.calendarForm.tue_time.value = "";
                    document.calendarForm.wed_time.value = "";
                    document.calendarForm.thu_time.value = "";
                    document.calendarForm.fri_time.value = "";
                    document.calendarForm.sat_time.value = "";
                }
           }
        }

	function checkAll() {
		var args	= checkAll.arguments;
		if (args.length > 1) {
			var formObj		= args[0];
			var allBoxObj	= args[1];
			var	checkBoxName;
			var checkedVal	= allBoxObj.checked;
			if (args.length > 2) {
				checkBoxName = args[2];
			}
			for (var i=0; i<formObj.elements.length; i++) {
				if (formObj.elements[i].type == 'checkbox') {
					if (args.length == 2) {
						formObj.elements[i].checked = checkedVal;
					} else if (formObj.elements[i].name == checkBoxName) {
						formObj.elements[i].checked = checkedVal;
					}
				}
			}
		}
	}

	function setTimeInCurrentWindow() {
               if (document.calendarForm.schedule.options[document.calendarForm.schedule.selectedIndex].value != "") {
                       alert("Unselect Available Schedule to set the timings selected");
                } else {
                    var	fromTime	= document.calendarForm.fromhr.options[document.calendarForm.fromhr.selectedIndex].value + ':' + document.calendarForm.frommin.options[document.calendarForm.frommin.selectedIndex].value;
                    var	toTime		= document.calendarForm.tohr.options[document.calendarForm.tohr.selectedIndex].value + ':' + document.calendarForm.tomin.options[document.calendarForm.tomin.selectedIndex].value;
                    var timeRange	= '';
                    var errorDays	= '';
                    if (! isValidTimeRange(fromTime + '-' + toTime)) {
                       alert('To Time should be greater than From Time');
                       return;
                    }
                    timeRange = timeRange + fromTime + '-' + toTime;
                    if (document.calendarForm.def_sel.checked) {
			if (! setTimeForDay(document.calendarForm.def_time, timeRange)) {
				errorDays += 'Defaultday,';
    			}
                    }
                    if (document.calendarForm.sun_sel.checked) {
			if (! setTimeForDay(document.calendarForm.sun_time, timeRange)) {
				errorDays += ' Sunday,';
			}
                    }
                    if (document.calendarForm.mon_sel.checked) {
			if (! setTimeForDay(document.calendarForm.mon_time, timeRange)) {
				errorDays += ' Monday,';
			}
                    }
                    if (document.calendarForm.tue_sel.checked) {
			if (! setTimeForDay(document.calendarForm.tue_time, timeRange)) {
				errorDays += ' Tuesday,';
			}
                    }
                    if (document.calendarForm.wed_sel.checked) {
			if (! setTimeForDay(document.calendarForm.wed_time, timeRange)) {
				errorDays += ' Wednesday,';
			}
                    }
                    if (document.calendarForm.thu_sel.checked) {
			if (! setTimeForDay(document.calendarForm.thu_time, timeRange)) {
				errorDays += ' Thursday,';
			}
                    }
                    if (document.calendarForm.fri_sel.checked) {
			if (! setTimeForDay(document.calendarForm.fri_time, timeRange)) {
				errorDays += ' Friday,';
			}
                    }
                    if (document.calendarForm.sat_sel.checked) {
			if (! setTimeForDay(document.calendarForm.sat_time, timeRange)) {
				errorDays += ' Saturday,';
			}
                    }
		
                    if (errorDays != '') {
			errorDays = errorDays.substring(0, errorDays.length-1);
			alert(' Maximum number of Time range is 4 \n' + errorDays + ': truncated to 4 values');
                    }
                    document.calendarForm.scheduleId.value = "";
                    document.calendarForm.scheduleName.value = "";
                }
		
	}
	
	function setTimeForDay(timeValue, timeRange) {
		var tempStr = timeValue.value;
		var	timeValuesCount = 0;
		var commaIndex		= tempStr.indexOf(',');
			while ( commaIndex != -1) {
				timeValuesCount++;
				//alert('tempStr: ' + tempStr);
				//alert('timeValuesCount: ' + timeValuesCount);
				//alert('tempStr.substring(0, commaIndex): ' + tempStr.substring(0, commaIndex));
				tempStr = tempStr.substring(commaIndex + 1, tempStr.length);
				commaIndex = tempStr.indexOf(',');		
			}
		if (timeValuesCount < 3) {
			if (timeValue.value != '') {
				timeValue.value += ',';
			}
			timeValue.value += timeRange;
			return true;
		}
		return false;
	}
	
	function clearTime() {
                if (document.calendarForm.schedule.options[document.calendarForm.schedule.selectedIndex].value != "") {
                    document.calendarForm.schedule.options[0].selected = true
                }
                if (document.calendarForm.def_sel.checked) {
                    document.calendarForm.def_time.value = '';
                }
                if (document.calendarForm.sun_sel.checked) {
                    document.calendarForm.sun_time.value = '';
                }
                if (document.calendarForm.mon_sel.checked) {
                    document.calendarForm.mon_time.value = '';
                }
                if (document.calendarForm.tue_sel.checked) {
                    document.calendarForm.tue_time.value = '';
                }
                if (document.calendarForm.wed_sel.checked) {
                    document.calendarForm.wed_time.value = '';
                }
                if (document.calendarForm.thu_sel.checked) {
                    document.calendarForm.thu_time.value = '';
                }
                if (document.calendarForm.fri_sel.checked) {
                    document.calendarForm.fri_time.value = '';
                }
                if (document.calendarForm.sat_sel.checked) {
                    document.calendarForm.sat_time.value = '';
                }
                    document.calendarForm.scheduleId.value = "";
                    document.calendarForm.scheduleName.value = "";
	}
	
	function setTimeInParentWindow() {
		window.opener.document.forms[0].elements[<%= (elementIndexVal) %>].value = document.calendarForm.def_time.value;
		window.opener.document.forms[0].elements[<%= (elementIndexVal + 3) %>].value = document.calendarForm.sun_time.value;
		window.opener.document.forms[0].elements[<%= (elementIndexVal + 4) %>].value = document.calendarForm.mon_time.value;
		window.opener.document.forms[0].elements[<%= (elementIndexVal + 5) %>].value = document.calendarForm.tue_time.value;
		window.opener.document.forms[0].elements[<%= (elementIndexVal + 6) %>].value = document.calendarForm.wed_time.value;
		window.opener.document.forms[0].elements[<%= (elementIndexVal + 7) %>].value = document.calendarForm.thu_time.value;
		window.opener.document.forms[0].elements[<%= (elementIndexVal + 8) %>].value = document.calendarForm.fri_time.value;
		window.opener.document.forms[0].elements[<%= (elementIndexVal + 9) %>].value = document.calendarForm.sat_time.value;
		window.close();
	}
	
	function setValuesFromParentWindow() {
		document.calendarForm.def_time.value = window.opener.document.forms[0].elements[<%= elementIndexVal %>].value;
		document.calendarForm.sun_time.value = window.opener.document.forms[0].elements[<%= (elementIndexVal + 3) %>].value;
		document.calendarForm.mon_time.value = window.opener.document.forms[0].elements[<%= (elementIndexVal + 4) %>].value;
		document.calendarForm.tue_time.value = window.opener.document.forms[0].elements[<%= (elementIndexVal + 5) %>].value;
		document.calendarForm.wed_time.value = window.opener.document.forms[0].elements[<%= (elementIndexVal + 6) %>].value;
		document.calendarForm.thu_time.value = window.opener.document.forms[0].elements[<%= (elementIndexVal + 7) %>].value;
		document.calendarForm.fri_time.value = window.opener.document.forms[0].elements[<%= (elementIndexVal + 8) %>].value;
		document.calendarForm.sat_time.value = window.opener.document.forms[0].elements[<%= (elementIndexVal + 9) %>].value;
	}
	
	function isValidTimeRange(timeRange) {
		if (timeRange.length == 11 && timeRange.charAt(2) == ':' && timeRange.charAt(5) == '-' && timeRange.charAt(8) == ':' ) {
			fromTime	= timeRange.substring(0,5);
			toTime		= timeRange.substring(6,11);
		} else {
			return false;
		}
		if ( (Number(toTime.substring(0,2))*60 + Number(toTime.substring(3,5)))
				>= (Number(fromTime.substring(0,2))*60 + Number(fromTime.substring(3,5))) ) {
			return true;
		}
		return false;
	}
//-->
</script>
<LINK REL="STYLESHEET" TYPE="text/css" HREF="common/ColorScheme.css">
</head>
</head>
<%
        DateConversion dateConversion = new DateConversion();
        String rotationdate = "";
        String daysArray[] = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        String lRotationDay = "";
        String rotationStart = (String) request.getAttribute("ROTATIONSTART");
        String lActiveFlag   = (String) request.getAttribute(com.firstis.common.util.general.Symbols.ACTIVEFLAG);   //  Added for ActiveFlag Implementation.

        String noOfWeeksStr = (String) request.getAttribute("NOOFDAYS");
        String servletName = (String) request.getAttribute("SERVLETNAME");
        int    noOfWeeks    = Integer.parseInt(noOfWeeksStr);
        String[] dayArray  = {"Week", "Second Day", "Third Day", "Fourth Day","Fifth Day",
                                "Sixth Day", "Seventh Day", "Eigth Day", "Ninth Day", "Tenth Day"};
        String lMenuItemId = (String) request.getAttribute("MENUITEMID");

        
%>
<body class="jui jennifer" LEFTMARGIN="0" TOPMARGIN="0" MARGINWIDTH="0" MARGINHEIGHT="0">
<form name="calendarForm" method="post" action="<%= servletName %>" >
<input type= "hidden" name="appName" value="<%= com.firstis.common.util.general.Constants.getApplication() %>">
<input type="hidden" name="ELEMENT_INDEX" value="<%= elementIndex %>">
<input type="hidden" name="ActionType" value="CALENDARSAVE">
<input type= "hidden" name="menuItemId" value="<%= lMenuItemId %>">
<input type="hidden" name="servletName" value = "<%= servletName %>">
<input type="hidden" name="scheduleId" value = "">
<input type="hidden" name="scheduleName" value = "">
<input type="hidden" name="ActiveFlag" value = "<%= lActiveFlag %>">
  <table border="1" cellspacing="1" cellpadding="2" align="center">
    <tr> 
      <td align="center" nowrap colspan="2" class="boldness">Week 
        Timings</td>
    </tr>
    <tr> 
      <td align="center" nowrap colspan="2"><select name="week">
                <%
                for (int k = 0; k < noOfWeeks; k++) {
                    if ((rotationStart != null) && (rotationStart.trim().length() > 0) ) {
                        rotationStart = DateFormatter.getOldDateFormat(rotationStart);                        
                        String lRotationEnd = dateConversion.getDateWeekAfter(rotationStart);
                        rotationStart = dateConversion.getStartWeekDate(rotationStart);
                        rotationdate = DateFormatter.getNewDateFormat(rotationStart) + "-" + DateFormatter.getNewDateFormat(lRotationEnd);
                        rotationStart = dateConversion.getNextOrPreviousDate(lRotationEnd, DateConversion.NEXT_DAY);
                        out.println("<option value='" + k +"'>" + rotationdate + "</option>");
                    } else {
			int i = k + 1;
                        out.println("<option value='" + k +"'>Week " + i + "</option>");
                    }
                }
%>
              </select></td>
    </tr>
    <tr> 
      <td align="center" nowrap class="boldness">Available Schedule :</td>
      <td align="center" nowrap><select name="schedule" onChange="javascript:setDataForSelectedSchedule(document.calendarForm.schedule.options[document.calendarForm.schedule.selectedIndex].value)">
            <option value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
                <%
                for (int k = 0; k < lScheduleDefnArray.length; k++) {
                    DefnEditBean lScheduleDefn = lScheduleDefnArray[k];
                    %><option value="<%= lScheduleDefn.getDefnId() %>"><%= lScheduleDefn.getDefnName() %></option><%
                }
                %>
      </select></td>
    </tr>
    <tr> 
      <td align="center" nowrap colspan="2"> 
        <table border="0" cellspacing="0" cellpadding="0">
          <tr align="center"> 
            <td nowrap class="boldness">From 
              Time</td>
            <td nowrap><b></b></td>
            <td nowrap class="boldness">To 
              Time</td>
          </tr>
          <tr align="center"> 
            <td nowrap> 
              <select name="fromhr" size="1">
                <%
			for (int x = 0;x < 24;x++) {
				String time = Integer.toString(x);
				if (time.length() != 2) {
					time = "0" + time;
				}
				%>
                <option value="<%= time %>"><%= time %></option>
                <%
			}	
			%>
              </select>
              : 
              <%
		  	%>
              <select name="frommin" size="1">
                <%
			for (int x = 0; x < 60; x++) {
				String time = Integer.toString(x);
				if (time.length() != 2) {
					time = "0" + time;
				}
				%>
                <option value="<%= time %>"><%= time %></option>
                <%
			}	
			%>
              </select>
            </td>
            <td nowrap>-</td>
            <td nowrap> 
              <select name="tohr" size="1">
                <%
			for (int x = 0;x < 24;x++) {
				String time = Integer.toString(x);
				if (time.length() != 2) {
					time = "0" + time;
				}
				if (x == 23) {
					selectString = "selected";
				} else {
					selectString = "";
				}
				%>
                <option value="<%= time %>" <%= selectString %>><%= time %></option>
                <%
			}	
			%>
              </select>
              : 
              <%
		  	%>
              <select name="tomin" size="1">
                <%
			for (int x = 0; x < 60; x++) {
				String time = Integer.toString(x);
				if (time.length() != 2) {
					time = "0" + time;
				}
				if (x == 59) {
					selectString = "selected";
				} else {
					selectString = "";
				}
				%>
                <option value="<%= time %>" <%= selectString %>><%= time %></option>
                <%
			}	
			%>
              </select>
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr> 
      <td align="center" nowrap colspan="2"> 
        <input class="main_butts" type="button" name="add" value="  Set Time  " onClick="javascript:setTimeInCurrentWindow()">
        &nbsp;&nbsp;&nbsp;&nbsp; 
        <input class="main_butts" type="button" name="clear" value=" Clear Time " onClick="javascript:clearTime();">
      </td>
    </tr>
    <tr> 
      <td align="center" nowrap colspan="2"> 
        <table border="1" cellspacing="0" cellpadding="2">
          <tr> 
            <td nowrap class="boldness">
              <input type="checkbox" name="allCheck" value="checkbox" onClick="checkAll(document.calendarForm, document.calendarForm.allCheck)">
              </td>
            <td colspan="2" nowrap class="note">All 
              days<br>
              (<u>Select 
              checkbox and Set or Clear time</u>)</td>
          </tr>
          <tr> 
            <td nowrap>
              <input type="checkbox" name="def_sel" value="checkbox">
              </td>
            <td nowrap class="boldness">Defaultday&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
            <td align="center" nowrap> 
              <input type="text" name="def_time" size="12" onFocus="blur();">
            </td>
          </tr>
          <tr> 
            <td nowrap>
              <input type="checkbox" name="sun_sel" value="checkbox">
              </td>
            <td nowrap class="boldness">Sunday</td>
            <td align="center" nowrap> 
              <input type="text" name="sun_time" size="12" onFocus="blur();">
            </td>
          </tr>
          <tr> 
            <td nowrap>
              <input type="checkbox" name="mon_sel" value="checkbox">
              </td>
            <td nowrap class="boldness">Monday</td>
            <td align="center" nowrap> 
              <input type="text" name="mon_time" size="12" onFocus="blur();">
            </td>
          </tr>
          <tr> 
            <td nowrap>
              <input type="checkbox" name="tue_sel" value="checkbox">
              </td>
            <td nowrap class="boldness">Tuesday</td>
            <td align="center" nowrap> 
              <input type="text" name="tue_time" size="12" onFocus="blur();">
            </td>
          </tr>
          <tr> 
            <td nowrap>
              <input type="checkbox" name="wed_sel" value="checkbox">
              </td>
            <td nowrap class="boldness">Wednesday</td>
            <td align="center" nowrap> 
              <input type="text" name="wed_time" size="12" onFocus="blur();">
            </td>
          </tr>
          <tr> 
            <td nowrap>
              <input type="checkbox" name="thu_sel" value="checkbox">
              </td>
            <td nowrap class="boldness">Thursday</font></b></td>
            <td align="center" nowrap> 
              <input type="text" name="thu_time" size="12" onFocus="blur();">
            </td>
          </tr>
          <tr> 
            <td nowrap>
              <input type="checkbox" name="fri_sel" value="checkbox">
              </td>
            <td nowrap class="boldness">Friday</td>
            <td align="center" nowrap> 
              <input type="text" name="fri_time" size="12" onFocus="blur();">
            </td>
          </tr>
          <tr> 
            <td nowrap>
              <input type="checkbox" name="sat_sel" value="checkbox">
              </td>
            <td nowrap class="boldness">Saturday</td>
            <td align="center" nowrap> 
              <input type="text" name="sat_time" size="12" onFocus="blur();">
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <!--<tr> 
      <td><font size="2" face="Verdana, Arial, Helvetica, sans-serif">Repeat Till*&nbsp; 
        <input type="text" size="5" name="lastday">
        &nbsp;<br>
        days</font></td>
      <td><font size="2" face="Verdana, Arial, Helvetica, sans-serif">For Every</font>** 
        <select name="repeatcount">
          <%
                   /* for (int k = 1; k <= dayArray.length; k++) {
                        out.println("<option value='" + k +"'>" + dayArray[k-1] + "</option>");
                    }*/
%>
        </select>
      </td>
    </tr>
    <tr> -->
      <td align="center" nowrap colspan="2"> 
        <input class="main_butts" type="button" name="ok" value="   OK   " onClick="checkRepeatWeekTiming(this.form)">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
        <input class="main_butts" type="button" name="cancel" value=" Cancel " onClick="closeRepeatWeekTiming(this.form, form.servletName.value)">
      </td>
    </tr>
  </table>
  </form>
</body>
</html>