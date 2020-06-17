<%   String lColorScheme2 = (String) session.getAttribute("colorScheme");
	String	selectString = "";
    String	elementIndex	= (String) request.getParameter("ELEMENT_INDEX");
	int		elementIndexVal	= 0;
	
	try {
		elementIndexVal = Integer.parseInt(elementIndex);
    } catch (Exception e) {
	}
%>

<html>
<head>
<title>Week-Time</title>
<LINK REL="STYLESHEET" TYPE="text/css" HREF="common/<%=lColorScheme2%>/<%=lColorScheme2%>_scheme.css">
<script language="JavaScript" src="section/section.js"></script>
<script language="JavaScript">
<!--
    function clearAllDays(){
     for (var i=0; i<document.calendarForm.elements.length; i++) {
		if (document.calendarForm.elements[i].type == 'checkbox') {
            if (document.calendarForm.elements[i].name == 'allCheck'){
               if (document.calendarForm.elements[i].checked) {
                  document.calendarForm.elements[i].checked = false;
               }
            }
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
		var	fromTime	= document.calendarForm.fromhr.options[document.calendarForm.fromhr.selectedIndex].value + ':' + document.calendarForm.frommin.options[document.calendarForm.frommin.selectedIndex].value;
		var	toTime		= document.calendarForm.tohr.options[document.calendarForm.tohr.selectedIndex].value + ':' + document.calendarForm.tomin.options[document.calendarForm.tomin.selectedIndex].value;
		var timeRange	= '';
		var errorDays	= '';
		if (! isValidTimeRange(fromTime + '-' + toTime)) {
			alert('To Time should be greater than From Time');
			return;
		}
		timeRange = timeRange + fromTime + '-' + toTime;
		/*if (document.calendarForm.def_sel.checked) {
       		if (! setTimeForDay(document.calendarForm.def_time, timeRange)) {
				errorDays += 'Defaultday,';
			}
		}*/
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
		/*if (document.calendarForm.def_sel.checked) {
			document.calendarForm.def_time.value = '';
		}*/
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
	}

	function setTimeInParentWindow() {
	   if(window.opener.document.modsection.tabName.value == "Standard Schedules") {
		window.opener.document.forms[0].elements[<%= (elementIndexVal) %>].value = document.calendarForm.sun_time.value;;
		window.opener.document.forms[0].elements[<%= (elementIndexVal + 3) %>].value = document.calendarForm.mon_time.value;
		window.opener.document.forms[0].elements[<%= (elementIndexVal + 4) %>].value = document.calendarForm.tue_time.value;
		window.opener.document.forms[0].elements[<%= (elementIndexVal + 5) %>].value = document.calendarForm.wed_time.value;
		window.opener.document.forms[0].elements[<%= (elementIndexVal + 6) %>].value = document.calendarForm.thu_time.value;
		window.opener.document.forms[0].elements[<%= (elementIndexVal + 7) %>].value = document.calendarForm.fri_time.value;
		window.opener.document.forms[0].elements[<%= (elementIndexVal + 8) %>].value = document.calendarForm.sat_time.value;
		/*window.opener.document.forms[0].elements[<%= (elementIndexVal + 9) %>].value = document.calendarForm.sat_time.value;*/
	  }
		window.close();
	}

	function setValuesFromParentWindow() {
		document.calendarForm.sun_time.value = window.opener.document.forms[0].elements[<%= elementIndexVal  %>].value;
		document.calendarForm.mon_time.value = window.opener.document.forms[0].elements[<%= (elementIndexVal + 3) %>].value;
		document.calendarForm.tue_time.value = window.opener.document.forms[0].elements[<%= (elementIndexVal + 4) %>].value;
		document.calendarForm.wed_time.value = window.opener.document.forms[0].elements[<%= (elementIndexVal + 5) %>].value;
		document.calendarForm.thu_time.value = window.opener.document.forms[0].elements[<%= (elementIndexVal + 6) %>].value;
		document.calendarForm.fri_time.value = window.opener.document.forms[0].elements[<%= (elementIndexVal + 7) %>].value;
		document.calendarForm.sat_time.value = window.opener.document.forms[0].elements[<%= (elementIndexVal + 8) %>].value;
		/*document.calendarForm.sat_time.value = window.opener.document.forms[0].elements[<%= (elementIndexVal + 9) %>].value;*/
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
</head>
<body class="jui jennifer" LEFTMARGIN="0" TOPMARGIN="0" MARGINWIDTH="0" MARGINHEIGHT="0" onLoad="setValuesFromParentWindow();">
<form name="calendarForm" method="post" action="">
<input type= "hidden" name="appName" value="<%= com.firstis.common.util.general.Constants.getApplication() %>">
<input type="hidden" name="ELEMENT_INDEX" value="<%= elementIndex %>">
  <table border="1" cellspacing="1" cellpadding="2" align="center">
    <tr class="CalendarHead"> 
      <td align="center" nowrap class="CalendarHead">Week Timings</td>
    </tr>
    <tr class="CalendarRow"> 
      <td align="center" nowrap class="CalendarRow"> 
        <table border="0" cellspacing="0" cellpadding="0">
          <tr align="center"> 
            <td nowrap class="note">From Time</td>
            <td nowrap>&nbsp;</td>
            <td nowrap class="note">To Time</td>
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
    <tr class="CalendarRow"> 
      <td align="center" nowrap class="CalendarRow"> 
        <input class="btn focus mini" type="button" name="add" value="  Set Time  " onClick="javascript:setTimeInCurrentWindow()">
        &nbsp;&nbsp;&nbsp;&nbsp; 
        <input class="btn focus mini" type="button" name="clear" value=" Clear Time " onClick="javascript:clearTime();">
      </td>
    </tr>
    <tr class="CalendarRow"> 
      <td align="center" nowrap class="CalendarRow"> 
        <table border="0" cellspacing="0" cellpadding="2">
          <tr> 
            <td nowrap>
                <input class="CalendarRow" type="checkbox" name="allCheck" value="checkbox" onClick="checkAll(document.calendarForm, document.calendarForm.allCheck)">
              </td>
            <td colspan="2" nowrap class="boldness">All days<br>
             <font class="normal" size="1">(<u>Select 
              checkbox and Set or Clear time</u>)</font></td>
          </tr>
          <%--<tr>
            <td nowrap><input class="CalendarRow" type="checkbox" name="def_sel" value="checkbox" onClick="clearAllDays()"></td>
            <td nowrap class="boldness">Defaultday&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
            <td align="center" nowrap>
              <input type="text" name="def_time"  size="12" onFocus="blur();">
            </td>
          </tr>--%>
          <tr>
            <td nowrap><input class="CalendarRow" type="checkbox" name="sun_sel" value="checkbox" onClick="clearAllDays()"></td>
            <td nowrap class="boldness">Sunday</td>
            <td align="center" nowrap> 
              <input type="text" name="sun_time" size="12" onFocus="blur();">
            </td>
          </tr>
          <tr> 
            <td nowrap><input class="CalendarRow" type="checkbox" name="mon_sel" value="checkbox" onClick="clearAllDays()"></td>
            <td nowrap class="boldness">Monday</td>
            <td align="center" nowrap> 
              <input type="text" name="mon_time" size="12" onFocus="blur();">
            </td>
          </tr>
          <tr> 
            <td nowrap><input class="CalendarRow" type="checkbox" name="tue_sel" value="checkbox" onClick="clearAllDays()"></td>
            <td nowrap class="boldness">Tuesday</td>
            <td align="center" nowrap> 
              <input type="text" name="tue_time" size="12" onFocus="blur();">
            </td>
          </tr>
          <tr> 
            <td nowrap><input class="CalendarRow" type="checkbox" name="wed_sel" value="checkbox" onClick="clearAllDays()"></td>
            <td nowrap class="boldness">Wednesday</td>
            <td align="center" nowrap> 
              <input type="text" name="wed_time" size="12" onFocus="blur();">
            </td>
          </tr>
          <tr> 
            <td nowrap><input class="CalendarRow" type="checkbox" name="thu_sel" value="checkbox" onClick="clearAllDays()"></td>
            <td nowrap class="boldness">Thursday</td>
            <td align="center" nowrap> 
              <input type="text" name="thu_time" size="12" onFocus="blur();">
            </td>
          </tr>
          <tr> 
            <td nowrap><input class="CalendarRow" type="checkbox" name="fri_sel" value="checkbox" onClick="clearAllDays()"></td>
            <td nowrap class="boldness">Friday</td>
            <td align="center" nowrap> 
              <input type="text" name="fri_time" size="12" onFocus="blur();">
            </td>
          </tr>
          <tr> 
            <td nowrap><input class="CalendarRow" type="checkbox" name="sat_sel" value="checkbox" onClick="clearAllDays()"></td>
            <td nowrap class="boldness">Saturday</td>
            <td align="center" nowrap> 
              <input type="text" name="sat_time" size="12" onFocus="blur();">
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr class="CalendarHead"> 
      <td align="center" nowrap class="CalendarHead"> 
        <input class="btn focus mini" type="button" name="ok" value="   OK   " onClick="javascript:setTimeInParentWindow();">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
        <input class="btn focus mini" type="button" name="cancel" value=" Cancel " onClick="window.close();">
      </td>
    </tr>
  </table>
  </form>
</body>
</html>