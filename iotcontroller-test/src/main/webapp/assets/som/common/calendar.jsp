<%@ page import ="java.util.Calendar" %>
<%@ page import ="java.util.Date" %>
<html>
<head>
<title>Calender</title>
    <% String lColorScheme = (String) session.getAttribute("colorScheme");%>
<LINK REL="STYLESHEET" TYPE="text/css" HREF="<%=request.getContextPath()%>/common/<%=lColorScheme%>/nskin_<%=lColorScheme%>_scheme.css">
<link rel="stylesheet" TYPE="text/css" href="<%=request.getContextPath()%>/common/ui.min.css" />
<link rel="stylesheet" TYPE="text/css" href="<%=request.getContextPath()%>/common/ui-jennifer.min.css" />
<link rel="stylesheet" TYPE="text/css" href="<%=request.getContextPath()%>/common/grid.min.css" />
<link rel="stylesheet" TYPE="text/css" href="<%=request.getContextPath()%>/common/grid-jennifer.min.css" />
</head>
</head>
<%
    String	elementIndex	= (String) request.getParameter("ELEMENT_INDEX");
    String	calendarType	= (String) request.getParameter("CALENDAR_TYPE");
    String	dateMode	= (String) request.getParameter("DATE_MODE");
    String	toValue		= (String) request.getAttribute("TO_VALUE");
    String	fromValue	= (String) request.getAttribute("FROM_VALUE");
    String	timeValue	= (String) request.getAttribute("TIME_VALUE");
    String	appName		= com.firstis.common.util.general.Constants.getApplication();

	if (calendarType == null) {
		calendarType = "";
	}
    if (toValue == null) {
		toValue = "";
    }
    
    if (fromValue == null) {
    	fromValue = "";
    }
    
    if (timeValue == null) {
    	timeValue = "";
    }

    String	selectString = "";
%>
<body class="jui jennifer" LEFTMARGIN="0" TOPMARGIN="0" MARGINWIDTH="0" MARGINHEIGHT="0">
<%
    String  month   = "";
    String  from    = "",holi = "",exduty = "",vac = "",alldates = "";
    String  year1   = "",lastdate1 = "",firstday1 = "";
    String  time1   = "00";

	Calendar c;
	int m,date,year=0,firstday=0,lastdate=0,lastday,index;
	int firstweek,gg,g1,g2,tweek;
	int oo = 0;
	from = (String) request.getParameter("from");
	if (from == null) {
		from = "";
	}
	String mon[] = {"January","February","March","April","May","June","July","August","September","October","November","December"};
	int a[]={31,28,31,30,31,30,31,31,30,31,30,31};

	if (from.equalsIgnoreCase("main")) {
		c = Calendar.getInstance();
		date =c.get(Calendar.DATE);
		m = c.get(Calendar.MONTH);
		year = c.get(Calendar.YEAR);
		int leap = year%4;
		if (leap==0) {
			a[1]=29;
		}
		month=mon[m];
		Date d=new Date();
		d.setDate(1);
		firstday=d.getDay();
		lastdate=a[m];
		d.setDate(lastdate);
		lastday=d.getDay();
		alldates="";
		firstweek=7-firstday;
		gg=lastdate-firstweek;
		g1=gg/7;
		g2=gg%7;
		if (g2!=0) {
			tweek=g1+2;
		} else {
			tweek=g1+1;
		}
	} else {
		month = request.getParameter("month");
		if (month==null) {
			month = "";
		}
		year=Integer.parseInt((String) request.getParameter("year"));
		firstday1=request.getParameter("firstday");
		firstday=Integer.parseInt(firstday1);
		lastdate1=request.getParameter("lastdate");
		if (lastdate1==null) {
			lastdate1="0";
		}
		lastdate=Integer.parseInt(lastdate1);
	}
%>
<form name="calendarfrm" method="post" action="CalendarServlet?SourceType=CALENDAR">
<input type="hidden" name="ActionType" value="View">
<input type="hidden" name="ELEMENT_INDEX" value="<%= elementIndex %>">
<input type="hidden" name="FROM" value="<%= from %>">
<input type="hidden" name="calendarType" value="<%= calendarType %>">
  <table border="1" align="center" cellpadding="2" cellspacing="1">
    <tr align="center" class="CalendarHead"> 
      <td nowrap class="CalendarHead">Date Calendar</td>
	</tr>
    <tr align="center" class="CalendarRow"> 
      <td class="CalendarRow"> 
        <select name="month" size="1" onChange="timeOnChange()">
          <% 
			for(int i=0;i<mon.length;i++) {
				if (month.equalsIgnoreCase(mon[i])) {
					out.println("<option value='"+i+"' selected>"+mon[i]+"</option>");
				} else {
					out.println("<option value='"+i+"'>"+mon[i]+"</option>");
				}
			}

	%>
        </select>
        <select name="year" size="1" onChange="timeOnChange()">
          <%
		 for (int i=2001;i<=2100;i++) {
			if (year==i) {
				out.println("<option value="+i+" selected>"+i+"</option>");
			} else {
				out.println("<option value="+i+">"+i+"</option>");
			}
		}
		%>
        </select>
		<%
		if (calendarType.equals("DT")) {
		%>
        <br>
        <select name="date_mode" onChange="dateModeChange();">
          <option value="from">From</option><%
			if ((dateMode != null) && (dateMode.equals("to"))) {
				selectString = "selected";
			} else {
				selectString = "";
			}
		  %><option value="to" <%= selectString %>>To</option>
		</select>
        <select name="hour" size="1">
          <%
		for(int x = 0;x < 24;x++) {
			String time = x+"";
			if(time.length() != 2)
				time = "0"+time;
			else
				time = time;
			out.println("<option value='"+time+"'>"+time+"</option>");
		}	
	%>
        </select>
        : 
        <select name="min" size="1">
          <%
		for(int x = 0;x < 60;x++) {
			String time = x+"";
			if(time.length() != 2)
				time = "0"+time;
			else
				time = time;
			out.println("<option value='"+time+"'>"+time+"</option>");
		}	
	%>
        </select>
		<%
		}
		%>
      </td>
    </tr>
    <tr class="CalendarRow"> 
      <td class="CalendarRow"> 
        <table align="center" border="1" cellspacing="0" cellpadding="1" class="CalendarRow">
          <tr class="CalendarRow"> 
            <td align='center' class="sunday_color">S</td>
            <td align='center' class="day_color">M</td>
            <td align='center' class="day_color">T</td>
            <td align='center' class="day_color">W</td>
            <td align='center' class="day_color">T</td>
            <td align='center' class="day_color">F</td>
            <td align='center' class="day_color">S</td>
          </tr>
          <%
	int firstDayVal [] = {0,1,2,3,4,5,6};
	int yValue [] = {1,7,6,5,4,3,2};
	int ddEnd [] = {0,6,5,4,3,2,0};
	String tdSpace = "";
	for(int i=0;i<firstDayVal.length;i++) {
		if (firstday == firstDayVal[i]) {
			for(int j=0;j<i;j++) {
				tdSpace = tdSpace+"<td>&nbsp;</td>";
			}
			if ((!(tdSpace.equals(""))) && (!(firstday == 6))) {
				out.println("<tr class=\"CalendarRow\">"+tdSpace);
				for(int dd=1;dd<=ddEnd[i];dd++) {
					String n1="but"+dd;
					out.println("<td class=\"CalendarRow\"><input class=\"main_butts\" type='button' name="+n1+" value='0"+dd+"' onClick='setTimeInCurrentWindow("+dd+")'></td>");
				}
			} else if (firstday == 6) {
				out.println("<tr class=\"CalendarRow\">"+tdSpace);
				out.println("<td class=\"CalendarRow\"><input class=\"main_butts\" type='button' name='but1' value='01' onClick='setTimeInCurrentWindow(1)'></td></tr>");
			}
			for(int y=yValue[i];y<=lastdate;) {
				out.println("<tr class=\"CalendarRow\">");
				for(int r=1;r<=7;r++) {
					String n1="but";
					String n=n1+y;
					if(y<=lastdate) {
						if(y<=9) {
							out.println("<td class=\"CalendarRow\"><input class=\"main_butts\" type='button' name="+n+" value='0"+y+"' onClick='setTimeInCurrentWindow("+y+")'></td>");
						} else
							out.println("<td class=\"CalendarRow\"><input class=\"main_butts\" type='button' name="+n+" value="+y+" onClick='setTimeInCurrentWindow("+y+")'></td>");
					}
					y++;
				}
				out.println("</tr>");
			}
		}
	}%>
        </table>
      </td>
    </tr>
	<%
	if (calendarType.equals("DT")) {
	%>
    <tr class="CalendarRow"> 
      <td nowrap align="center" class="CalendarRow"> 
        <table border="0" cellspacing="0" cellpadding="0" class="CalendarRow">
          <tr class="CalendarRow"> 
            <td align="center" height="8" class="CalendarRow">From</td>
            <td height="8" class="CalendarRow"><b></b></td>
            <td align="center" height="8" class="CalendarRow">To</td>
            <td align="center" height="8" class="CalendarRow">&nbsp;</td>
          </tr>
          <tr class="CalendarRow"> 
            <td class="CalendarRow"> 
              <input type="text" name="from_date" size="16" value="<%= fromValue %>" onFocus="blur();">
            </td>
            <td class="CalendarRow">-</td>
            <td class="CalendarRow"> 
              <input type="text" name="to_date" size="16" value="<%= toValue %>" onFocus="blur();">
            </td>
            <td class="CalendarRow"> 
              <input class="btn focus mini" type="button" name="Add" value="Add" onClick="javascript:addTime();">
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr class="CalendarRow"> 
      <td nowrap align="center" class="CalendarRow"> 
        <textarea name="time" cols="35" rows="3" wrap="VIRTUAL" onFocus="blur();"><%= timeValue %></textarea>
      </td>
    </tr>
    <tr class="CalendarHead"> 
      <td align="center" class="CalendarHead"> 
        <input class="btn focus mini" type=button name="ok" value="  OK  " onClick="setTimeInParentWindow()">
        &nbsp;&nbsp; 
        <input class="btn focus mini" type="button" name="Clear" value="Clear" onClick="clearTime();">
        &nbsp;&nbsp; 
        <input class="btn focus mini" type="button" name="Cancel" value="Cancel" onClick="window.close();">
      </td>
    </tr>
	<%
	}
	%>
  </table>
  </form>

<script language=JavaScript>
<!--

function clearTime() {
    document.calendarfrm.to_date.value = '';
    document.calendarfrm.from_date.value = '';
    document.calendarfrm.time.value = '';
    document.calendarfrm.date_mode.selectedIndex = 0;
}

function addTime() {
    var d = new Date();
    var t_date = d.getDate();      // Returns the day of the month
    var t_mon = d.getMonth();      // Returns the month as a digit
    var t_year = d.getFullYear();  // Returns 4 digit year
    var t_hour = d.getHours();     // Returns hours
    var t_min = d.getMinutes();    // Returns minutes
    var t_sec = d.getSeconds();    // Returns seocnds
    var t_mil = d.getMilliseconds;  // Returns Milliseconds
    if (t_hour <= "9"){
        t_hour = "0"+t_hour;
    }
    if (t_min <= "9"){
        t_min = "0"+t_min;
    }
    if (t_date <= "9"){
        t_date = "0"+t_date;
    }
    t_mon = parseInt(t_mon)+1;
    if (t_mon <= "9"){
        t_mon = "0"+t_mon;
    }
    var currentDateTime = t_mon+"/"+t_date+"/"+t_year+"@"+t_hour+":"+t_min;
    var fromTime    = document.calendarfrm.from_date.value;
    var toTime      = document.calendarfrm.to_date.value;
    var time        = document.calendarfrm.time.value;
    if (fromTime == '' || toTime == '') {
    	alert('Set from time and to time and then ADD');
    	return;
    }
    if (fromTime < currentDateTime) {
        alert("Invalid Date");
        document.calendarfrm.from_date.value = '';
        document.calendarfrm.to_date.value = '';
        document.calendarfrm.date_mode.selectedIndex = 0;
        document.calendarfrm.time.value = time;
	    dateModeChange();
    } else if (toTime < fromTime) {
        alert("Invalid Date");
        document.calendarfrm.from_date.value = '';
        document.calendarfrm.to_date.value = '';
        document.calendarfrm.date_mode.selectedIndex = 0;
        document.calendarfrm.time.value = time;
	    dateModeChange();
    } else {
    if (time != '') {
    	time = time + ', ';
    }
    time = time + fromTime + '-' + toTime;
    document.calendarfrm.from_date.value = '';
    document.calendarfrm.to_date.value = '';
    document.calendarfrm.date_mode.selectedIndex = 0;
    document.calendarfrm.time.value = time;
	dateModeChange();
}
}

function setTimeInCurrentWindow(value) {
    var date    = dateProcessing(value);
	<%
	if (calendarType.equals("DA")) {
	%>
	    var d = new Date();
        var t_date = d.getDate();      // Returns the day of the month
        var t_mon = d.getMonth();      // Returns the month as a digit
        var t_year = d.getFullYear();  // Returns 4 digit year
        if (t_date <= "9"){
            t_date = "0"+t_date;
        }
        t_mon = parseInt(t_mon)+1;
        if (t_mon <= "9"){
            t_mon = "0"+t_mon;
        }
        var currentDate = t_mon+"/"+t_date+"/"+t_year;
	    if (date < currentDate) {
            alert("Invalid Date");
        } else {
        window.opener.document.forms[0].elements[document.calendarfrm.ELEMENT_INDEX.value].value = date;
		window.close();
		}
        <% } else if (calendarType.equals("CAL")) { %>
            window.opener.document.forms[0].elements[document.calendarfrm.ELEMENT_INDEX.value].value = date;
            if (window.opener.document.forms[0].elements[document.calendarfrm.ELEMENT_INDEX.value].name == 'startDate') {
                var fromDateString = window.opener.document.forms[0].fromDate.value.split("/");
                var fromMonth = fromDateString[0];
                if (fromMonth.charAt(0) == "0") {
                    fromMonth  =  fromMonth.substring(1);
                }
                var fromDate = fromDateString[1];
                if (fromDate.charAt(0) == "0") {
                    fromDate  =  fromDate.substring(1);
                }
                var fromYear = fromDateString[2];
                var toDateString = window.opener.document.forms[0].toDate.value.split("/");
                var toMonth = toDateString[0];
                if (toMonth.charAt(0) == "0") {
                    toMonth  =  toMonth.substring(1);
                }
                var toDate = toDateString[1];
                if (toDate.charAt(0) == "0") {
                    toDate  =  toDate.substring(1);
                }
                var toYear = toDateString[2];

                var fromDateObj = new Date(fromYear, fromMonth, fromDate, 0, 0, 0, 0);
                var toDateObj = new Date(toYear, toMonth, toDate, 0, 0, 0, 0);

                    // Calculate the difference in milliseconds
                    var difference_ms = Math.ceil((toDateObj.getTime()-fromDateObj.getTime())/(1000 * 60 * 60 * 24));

                    var startDateString = date.split("/");
                    var startMonth = startDateString[0];
                    if (startMonth.charAt(0) == "0") {
                        startMonth  =  startMonth.substring(1);
                    }
                    var startDate = startDateString[1];
                    if (startDate.charAt(0) == "0") {
                        startDate  =  startDate.substring(1);
                    }
                    var startYear = startDateString[2];
                    startDate = parseInt(startDate) + parseInt(difference_ms);
                    startMonth = parseInt(startMonth)-1;
                    var endDateObj = new Date(startYear, startMonth, startDate);
                    var endMonth = endDateObj.getMonth()+1;
                    if (endMonth <= 9) {
    	                endMonth = "0" + endMonth;
	                }
	                var endDate = endDateObj.getDate();
	                if (endDate <= 9) {
		                endDate = "0" + endDate ;
	                }
                    var endDateString = endMonth + "/" + endDate + "/" + endDateObj.getYear();
                    window.opener.document.forms[0].endDate.value = endDateString;
            } else if (window.opener.document.forms[0].elements[document.calendarfrm.ELEMENT_INDEX.value].name == 'toDate') {
                window.opener.document.forms[0].startDate.value = "";
                window.opener.document.forms[0].endDate.value = "";
            }
		window.close();
	<%
	} else {
	%>

		var timeVal = document.calendarfrm.hour.options[document.calendarfrm.hour.selectedIndex].value + ":" + document.calendarfrm.min.options[document.calendarfrm.min.selectedIndex].value;
		var timeObj = null;
			
		var tempStr         = document.calendarfrm.time.value;
		var timeValuesCount = 0;
		var commaIndex      = tempStr.indexOf(',');
	
		while ( commaIndex != -1) {
			timeValuesCount++;
			tempStr = tempStr.substring(commaIndex + 1, tempStr.length);
			commaIndex = tempStr.indexOf(',');		
		}
		if (timeValuesCount >= 15) {
			alert('Maximum limit of time values is 16');
			return;
		}
	
		if (document.calendarfrm.date_mode.options[document.calendarfrm.date_mode.selectedIndex].value == 'from') {
			timeObj = document.calendarfrm.from_date;
			document.calendarfrm.date_mode.selectedIndex = 1;
			dateModeChange();
		} else {
			timeObj = document.calendarfrm.to_date;
		}
		
		timeObj.value = date + "@" + timeVal;
	<%
	}
	%>
}

function setTimeInParentWindow() {
    window.opener.document.forms[0].elements[document.calendarfrm.ELEMENT_INDEX.value].value = document.calendarfrm.time.value;
    window.close();
}

function timeOnChange() {
    var month = document.calendarfrm.month.options[document.calendarfrm.month.selectedIndex].value
    var year = document.calendarfrm.year.options[document.calendarfrm.year.selectedIndex].value
    var elementIndex	= document.calendarfrm.ELEMENT_INDEX.value;
    var from			= document.calendarfrm.FROM.value;
	var firstDayInstance = new Date(year, month, 1);
	var firstDay = firstDayInstance.getDay();
	var months=new Array("January","February","March","April","May","June","July","August","September","October","November","December")
	firstDayInstance = null;
	var days = getDays(month, year);

	if (from == "main") {
		document.calendarfrm.action = "CalendarServlet?SourceType=CALENDAR&month="+months[month]+"&year="+year+"&firstday="+firstDay+"&lastdate="+days+"&ELEMENT_INDEX="+elementIndex;
	} else {
		document.calendarfrm.action = "CalendarServlet?SourceType=CALENDAR&month="+months[month]+"&year="+year+"&firstday="+firstDay+"&lastdate="+days+"&ELEMENT_INDEX="+elementIndex;
	}
	document.calendarfrm.ActionType.value = 'CHANGE_DATE';
	document.calendarfrm.submit();
}

function dateModeChange() {
	if (document.calendarfrm.date_mode.selectedIndex == 0) {
		// setting first element selected
		document.calendarfrm.hour.selectedIndex	= 0;
		document.calendarfrm.min.selectedIndex	= 0;
	} else if (document.calendarfrm.date_mode.selectedIndex == 1) {
		// setting last element selected
		document.calendarfrm.hour.selectedIndex	= document.calendarfrm.hour.options.length - 1;
		document.calendarfrm.min.selectedIndex	= document.calendarfrm.min.options.length - 1;
	}
}

function dateProcessing(value) {

	var month = parseInt(document.calendarfrm.month.options[document.calendarfrm.month.selectedIndex].value)
    var year = document.calendarfrm.year.options[document.calendarfrm.year.selectedIndex].value

	month=month+1
	if (month <= 9) {
    	month = "0" + month;
	}
	var date = parseInt(value);
	if (date <= 9) {
		date = "0" + date ;
	}
	var caldate = month + "/" + date + "/" + year;
        return caldate;
}

function getDays(month, year) {
	// create array to hold number of days in each month
	var ar = new Array(12)
	ar[0] = 31 // January
	ar[1] = (leapYear(year)) ? 29 : 28 // February
	ar[2] = 31 // March
	ar[3] = 30 // April
	ar[4] = 31 // May
	ar[5] = 30 // June
	ar[6] = 31 // July
	ar[7] = 31 // August
	ar[8] = 30 // September
	ar[9] = 31 // October
	ar[10] = 30 // November
	ar[11] = 31 // December

	// return number of days in the specified month (parameter)
	return ar[month]
}

function leapYear(year) {
	if (year % 4 == 0) // basic rule
		return true // is leap year
	/* else */ // else not needed when statement is "return"
		return false // is not leap year
}

	<%
	if (calendarType.equals("DT")) {
	%>

	if (document.calendarfrm.FROM.value == 'main') {
		document.calendarfrm.time.value = window.opener.document.forms[0].elements[document.calendarfrm.ELEMENT_INDEX.value].value;
	}

	<%
	}
	%>
//-->
</script>
</body>
</html>