<%@ page import="com.firstis.common.util.general.*,com.firstis.telaweb.beans.*,java.util.*,com.firstis.common.util.dateformatter.*" %>
<html>
<head>
<title>Repeat Time</title>
<%
        response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
        response.setHeader("Pragma","no-cache"); //HTTP 1.0
        response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<script language="JavaScript" src="<%= request.getContextPath() %>/rotation/rotation.js"></script>
<script language="JavaScript">
function setTimeInCurrentWindow() {
	var	fromTimeHr	= document.calendarfrm.fromtimeHr.options[document.calendarfrm.fromtimeHr.selectedIndex].value;
	var	fromTimeMin	= document.calendarfrm.fromtimeMin.options[document.calendarfrm.fromtimeMin.selectedIndex].value;
	var	toTimeHr        = document.calendarfrm.totimeHr.options[document.calendarfrm.totimeHr.selectedIndex].value;
    var	toTimeMin       = document.calendarfrm.totimeMin.options[document.calendarfrm.totimeMin.selectedIndex].value;
    var	fromTime	= fromTimeHr + ":" + fromTimeMin;
    var	toTime		= toTimeHr   + ":" + toTimeMin;
	var timeRange	= document.calendarfrm.time.value;
	if (! isValidTimeRange(fromTime + '-' + toTime)) {
		alert('To Time should be greater than From Time');
		return;
	}
	if (timeRange != '') {
		timeRange = timeRange + ',';
	}
	timeRange = timeRange + fromTime + '-' + toTime;
	var tempStr = timeRange;
	var	timeValuesCount = 0;
	var commaIndex		= tempStr.indexOf(',');
	if (commaIndex == -1) {
		if (! isValidTimeRange(tempStr)) {
			alert('Invalid time format');
			return;
		}
		timeValuesCount++;
	} else {
		while ( commaIndex != -1) {
			timeValuesCount++;
			if (! isValidTimeRange(tempStr.substring(0, commaIndex))) {
				alert('Invalid time format');
				return;
			}
			tempStr = tempStr.substring(commaIndex + 1, tempStr.length);
			commaIndex = tempStr.indexOf(',');
		}
	}
	if (timeValuesCount > 3) {
		alert('Maximum limit of time values is 4');
		return;
	}
	document.calendarfrm.time.value = timeRange;
}

function clearTime() {
	document.calendarfrm.time.value = '';
}

function isValidTimeRange(timeRange) {
	if (timeRange.length == 11 && timeRange.charAt(2) == ':' && timeRange.charAt(5) == '-' && timeRange.charAt(8) == ':' ) {
		fromTime	= timeRange.substring(0,5);
		toTime		= timeRange.substring(6,11);
	} else {
		return false;
	}
	if ( (Number(toTime.substring(0,2))*60 + Number(toTime.substring(3,5)))
			> (Number(fromTime.substring(0,2))*60 + Number(fromTime.substring(3,5))) ) {
		return true;
	}
	return false;
}

</script>
<LINK REL="STYLESHEET" TYPE="text/css" HREF="common/ColorScheme.css">
</head>
</head>
<%
        String daysArray[] = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        DateConversion dateConversion = new DateConversion();
        String rotationdate = "";
        String lRotationDay = "";
        String noOfDaysStr = (String) request.getAttribute("NOOFDAYS");
        String servletName = (String) request.getAttribute("SERVLETNAME");
        String rotationStart = (String) request.getAttribute("ROTATIONSTART");
        String lActiveFlag   = (String) request.getAttribute(com.firstis.common.util.general.Symbols.ACTIVEFLAG);   //  Added for ActiveFlag Implementation.
        int    noOfDays    = Integer.parseInt(noOfDaysStr);
        String[] dayArray  = {"Day", "Second Day", "Third Day", "Fourth Day","Fifth Day",
                                "Sixth Day", "Seventh Day", "Eigth Day", "Ninth Day", "Tenth Day"};
        String lMenuItemId = (String) request.getAttribute("MENUITEMID");
%>
<body class="jui jennifer" LEFTMARGIN="0" TOPMARGIN="0" MARGINWIDTH="0" MARGINHEIGHT="0">
<form name="calendarfrm" method="post" action="<%= servletName %>">
<input type= "hidden" name="appName" value="<%= Constants.getApplication() %>">
<input type= "hidden" name="ActionType" value="CALENDARSAVE">
<input type= "hidden" name="menuItemId" value="<%= lMenuItemId %>">
<input type="hidden" name="servletName" value = "<%= servletName %>">
<input type="hidden" name="ActiveFlag" value = "<%= lActiveFlag %>">
<input type="hidden" name="selectedDay" value ="0">
    <tr>
      <td>
        <table border="1" align="center" cellpadding="2" cellspacing="1" width="100%">
          <tr>
            <td align="center" colspan='2' class="boldness">Timings</td>
          </tr>
          <tr>
            <td align="center" width="50%" class="normal">Day</td>
            <td width="50%">
              <select name="day" onchange="selectDay()">
                <%
                for (int k = 1; k <= noOfDays; k++) {
                    if (rotationStart != null && rotationStart.trim().length() > 0) {
                        if (k == 1) {
                            rotationdate = rotationStart;
                        } else {
                            rotationStart = DateFormatter.getOldDateFormat(rotationStart);
                            rotationStart = dateConversion.getNextOrPreviousDate(rotationStart, DateConversion.NEXT_DAY);
                            rotationdate = DateFormatter.getNewDateFormat(rotationStart);
                            rotationStart = rotationdate;
                        }
                        lRotationDay = daysArray[dateConversion.getDateDay(DateFormatter.getOldDateFormat(rotationdate))];
                        out.println("<option value='" + k +"'>" + lRotationDay + "</option>");
                    } else {
                        out.println("<option value='" + k +"'>Day " + k + "</option>");
                    }
                }
                %>
              </select>
            </td>
          </tr>
          <tr> 
            <td align="center" width="50%" class="normal">FromTime</td>
            <td align="center" width="50%" class="normal">ToTime</td>
          </tr>
          <tr class="normal">
        <td colspan='2' align="center" class="normal">
        <select name="fromtimeHr" size="1">
<%
    String timeHr = "";
	for(int x = 0; x < 24; x++) {
		timeHr = x + "";
		if(timeHr.length() != 2) {
			timeHr = "0" + timeHr;
        }
		out.println("<option value='" + timeHr +"' >" + timeHr + "</option>");
	}
%>
        </select>
        <select name="fromtimeMin" size="1">
<%
    String  timeMin  = "";
	for(int x = 0; x < 60; x++) {
		timeMin  = x + "";
		if(timeMin.length() != 2) {
			timeMin = "0" + timeMin;
        }
		out.println("<option value='" + timeMin + "' >" + timeMin + "</option>");
	}
%>
        </select>
        -
        <select name="totimeHr" size="1">
<%
    timeHr   = "";
	for (int x = 0; x < 24; x++) {
		timeHr  = x + "";
		if (timeHr.length() != 2) {
			timeHr = "0" + timeHr;
        }
		if (x == 23) {
			out.println("<option value='" + timeHr + "' selected>"+ timeHr +"</option>");
		} else {
			out.println("<option value='" + timeHr + "'>" + timeHr + "</option>");
		}
	}
%>
        </select>
        <select name="totimeMin" size="1">
<%
    timeMin = "";
	for(int x = 0; x < 60 ;x++) {
		timeMin = x + "";
		if(timeMin.length() != 2) {
			timeMin = "0" + timeMin;
        }
		if (x == 59) {
			out.println("<option value='" + timeMin + "' selected>" + timeMin + "</option>");
		} else {
			out.println("<option value='" + timeMin + "' >" + timeMin + "</option>");
		}
	}
%>
        </select>
      </td>
    </tr>
          <tr> 
            <td align="center"> 
              <input class="main_butts" type="button" name="timeadd" value=" Add Times " onClick="setTimeInCurrentWindow();">
            </td>
            <td align="center"> 
              <input class="main_butts" type="button" name="re" value=" Clear Time" onClick="clearTime();">
            </td>
          </tr>
          <tr> 
            <td colspan='2' align="center"> 
              <input type="text" name="time" value="" size="30" onFocus="blur();">
            </td>
          </tr>
          <tr> 
            <td nowrap class="normal">Repeat 
              Till*&nbsp; 
              <input type="text" size="5" name="lastday">
              &nbsp;days</td>
            <td nowrap class="normal">For 
              Every** 
              <select name="repeatcount">
                <%
                    for (int k = 1; k <= dayArray.length; k++) {
                        out.println("<option value='" + k +"'>" + dayArray[k-1] + "</option>");
                    }
%>
              </select>
            </td>
          </tr>
          <tr> 
            <td align="center"> 
              <input class="main_butts" type="button" name="timeadd" value="   OK   " onClick="checkValue()">
            </td>
            <td align="center"> 
              <input class="main_butts" type="button" name="canc" value="Cancel" onClick="window.close()">
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td class="note">*Schedule rotates till the number of days.<br>
        **Selected Timings is assigned For Every specified interval.
      </td>
    </tr>
  </table>
  </form>
</body>
</html>