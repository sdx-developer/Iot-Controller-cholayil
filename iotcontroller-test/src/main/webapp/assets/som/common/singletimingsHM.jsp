

<html>
<head>
<title>Schedule Time</title>
    <%String lColorScheme = (String) session.getAttribute("colorScheme");%>
<LINK REL="STYLESHEET" TYPE="text/css" href="<%= request.getContextPath() %>/common/<%=lColorScheme%>/<%=lColorScheme%>_scheme.css">

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

function setTimeInParentWindow() {
	window.opener.document.forms[0].elements[document.calendarfrm.ELEMENT_INDEX.value].value = document.calendarfrm.time.value;
	window.close();
}
//------------------PORTIA----------
function setTimeInCalendar(){
    parent.window.opener.document.calendarForm.TimePeriod.value = document.calendarfrm.time.value;
    window.close();
}
//----------------PORTIA--------------
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
</head>

<%
    String      setInterval         = (String) request.getAttribute("SETINTERVAL");
    String      elementIndex        = (String) request.getParameter("ELEMENT_INDEX");
    String      lActiveFlag         = (String) request.getAttribute(com.firstis.common.util.general.Symbols.ACTIVEFLAG);   //  Added for ActiveFlag Implementation.
%>
<body class="jui jennifer" LEFTMARGIN="0" TOPMARGIN="0" MARGINWIDTH="0" MARGINHEIGHT="0">

<script language="JavaScript" src="section/section.js"></script>
<script language="JavaScript" src="<%= request.getContextPath() %>/onCall/OnCall.js"></script>
<form name="calendarfrm" method="post" action="/<%= com.firstis.common.util.general.Constants.getApplication() %>/SectionServlet">
<input type= "hidden" name="appName" value="<%= com.firstis.common.util.general.Constants.getApplication() %>">
<input type="hidden" name="CALENDAR_TYPE" value="WT">
<input type="hidden" name="alltimes">
<input type="hidden" name="ELEMENT_INDEX" value="<%= elementIndex %>">
<input type="hidden" name="ActionType" value="">
<input type="hidden" name="SourceType" value="">
<input type="hidden" name="ActiveFlag" value = "<%= lActiveFlag %>">
<input type="hidden" name="setInterval" value = "<%= setInterval %>">

  <table border="1" align="center" cellpadding="2" cellspacing="1">
    <tr class="column_title_tr">
      <td align="center" colspan='2' class="column_title_td">Schedule Time</td>
    </tr>
    <tr class="column_title_tr">
      <td align="center" class="column_title_td">From Time</td>
      <td align="center" class="column_title_td">To Time</td>
    </tr>
    <tr class="column_title_tr">
      <td colspan='2' align="center" class="column_title_td">
        <select name="fromtimeHr" size="1">
<%
        String          timeHr          = "";
	for(int x = 0; x < 24; x++) {
		timeHr                  = x + "";
		if(timeHr.length() != 2) {
			timeHr          = "0" + timeHr;
                }
		out.println("<option value='" + timeHr +"' >" + timeHr + "</option>");
	}	
%>
        </select>
        <select name="fromtimeMin" size="1">
<%
        String          timeMin         = "";
	for(int x = 0; x < 60; x++) {
		timeMin                 = x + "";
		if(timeMin.length() != 2) {
			timeMin         = "0" + timeMin;
                }
		out.println("<option value='" + timeMin + "' >" + timeMin + "</option>");
	}	
%>
        </select>
        - 
        <select name="totimeHr" size="1">
<%
        timeHr                          = "";
	for (int x = 0; x < 24; x++) {
		timeHr                  = x + "";
		if (timeHr.length() != 2) {
			timeHr          = "0" + timeHr;
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
        timeMin                         = "";
	for(int x = 0; x < 60 ;x++) {
		timeMin                 = x + "";
		if(timeMin.length() != 2) {
			timeMin         = "0" + timeMin;
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
    <tr class="column_title_tr">
      <td align="center" class="column_title_td">
        <input class="btn focus mini" type="button" name="timeadd" value=" Add Times " onClick="setTimeInCurrentWindow();">
      </td>
      <td align="center" class="column_title_td">
        <input class="btn focus mini" type="button" name="re" value=" Clear Time" onClick="clearTime();">
      </td>
    </tr>
    <tr class="column_title_tr">
      <td colspan='2' align="center" class="column_title_td">
        <input type="text" name="time" value="" onFocus="blur();">
      </td>
    </tr>
    <tr class="column_title_tr">
      <td align="center" class="column_title_td" colspan="2"> 
        <input class="btn focus mini" type="button" name="timeadd" value="   OK   " onClick="
<%
   if(request.getParameter("SourceType") != null)
         { if(request.getParameter("SourceType").equals("OnCallMainCalendar")){
         out.print("setTimeInOnCallMainCalendarParentWindow()");
         }else{
         out.print("setTimeInOnCallSlotsParentWindow()");
         }
         //-----PORTIA-----------
         }else  if(request.getAttribute("TIME")!=null){
            out.print("setTimeInCalendar()");
         }
          //---------PORTIA------
          else {out.print("setTimeInParentWindow()");
         }
%>
;">
      &nbsp;&nbsp;
        <input class="btn focus mini" type="button" name="canc" value="Cancel" onclick=cancelfn(this.form)>
      </td>
    </tr>
  </table>
</form>


<script language=JavaScript>
<%if(request.getParameter("SourceType") == null) {%>
document.calendarfrm.time.value = window.opener.document.forms[0].elements[document.calendarfrm.ELEMENT_INDEX.value].value;
<%}%>

</script>
</body>
</html>