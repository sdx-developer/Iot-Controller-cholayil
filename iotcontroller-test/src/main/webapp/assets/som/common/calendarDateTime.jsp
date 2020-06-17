<%@ page import ="java.util.Calendar" %>
<%@ page import ="java.util.Date" %>
<html>
<head>
<title>Calendar</title>
<%String lColorScheme = (String) session.getAttribute("colorScheme");%>
<LINK REL="STYLESHEET" TYPE="text/css" HREF="<%=request.getContextPath()%>/common/<%=lColorScheme%>/nskin_<%=lColorScheme%>_scheme.css">
<link rel="stylesheet" TYPE="text/css" href="<%=request.getContextPath()%>/common/ui.min.css" />
<link rel="stylesheet" TYPE="text/css" href="<%=request.getContextPath()%>/common/ui-jennifer.min.css" />
<link rel="stylesheet" TYPE="text/css" href="<%=request.getContextPath()%>/common/grid.min.css" />
<link rel="stylesheet" TYPE="text/css" href="<%=request.getContextPath()%>/common/grid-jennifer.min.css" />
</head>
<%
    String	appName		= com.firstis.common.util.general.Constants.getApplication();
    String	selectString = "";
%>
<body class="jui jennifer" LEFTMARGIN="0" TOPMARGIN="0" MARGINWIDTH="0" MARGINHEIGHT="0">
<%
    String txtSrc = request.getParameter("txtSrc");
    Calendar c = Calendar.getInstance();
    String  month   = "";
    String  from    = "",holi = "",exduty = "",vac = "",alldates = "";
    String  year1   = "",lastdate1 = "",firstday1 = "";
    String  time1   = "00";
    String  dateMode = "";
    String  selectedString = "";
    String fromDate = "";
    String toDate = "";
    String hour = "";
    String min = "";
    String isAll = "";
    int currentYear = 0;

	int m,date,year=0,firstday=0,lastdate=0,lastday,index;
	int firstweek,gg,g1,g2,tweek;
	int oo = 0;
	from = (String) request.getParameter("from");
	if (from == null) {
		from = "";
	}
	String mon[] = {"January","February","March","April","May","June","July","August","September","October","November","December"};
	int a[]={31,28,31,30,31,30,31,31,30,31,30,31};

        currentYear = c.get(Calendar.YEAR);

	if (from.equalsIgnoreCase("main")) {
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
                dateMode = "from";
                fromDate = request.getParameter("fromDate");
                toDate = request.getParameter("toDate");
                isAll = request.getParameter("isAll");
                if (isAll.equalsIgnoreCase("true")) {
                    hour = "23";
                    min  = "59";
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
                dateMode = request.getParameter("date_mode");
                fromDate = request.getParameter("fromDate");
                toDate = request.getParameter("toDate");
                hour = request.getParameter("hour");
                min = request.getParameter("min");
                isAll = request.getParameter("isAll");
	}
        String agent=request.getHeader("User-Agent");
        boolean isNetscape = false;
        int textFieldSize = 18;
        if(agent.startsWith("Mozilla/4.7")) {
            isNetscape = true;
            textFieldSize = 10;
        }
        if (fromDate == null) {
            fromDate = "";
        }
        if (toDate == null) {
            toDate = "";
        }
%>
<form name="calendarfrm" method="post">
<script language="JavaScript" src="global.js"></script>
<input type="hidden" name="FROM" value="<%= from %>">
<input type="hidden" name="txtSrc" value="<%= txtSrc %>">
<input type="hidden" name="isAll" value="<%= isAll %>">


  <table border="1" align="center" cellpadding="2" cellspacing="1">
    <tr align="center" class="CalendarHead"> 
      <td nowrap class="CalendarHead">Date/Time Calendar</td>
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
		 for (int i=currentYear;i<=2100;i++) {
			if (year==i) {
				out.println("<option value="+i+" selected>"+i+"</option>");
			} else {
				out.println("<option value="+i+">"+i+"</option>");
			}
		}
		%>
        </select>
        <br>
        <select name="date_mode" size="1" onChange="dateModeChange();"><%
            if ((dateMode != null) && (dateMode.equalsIgnoreCase("to"))) {
                selectedString = "selected";
            } else {
               selectedString = "";
            }
            if (isAll.equalsIgnoreCase("false")) {
        %>
            <option value='from'>From</option><%
            }
        %><option value='to' <%= selectedString %>>To</option>
        </select>
        <select name="hour" size="1">
          <%
		for(int x = 0;x < 24;x++) {
			String time = x+"";
			if(time.length() != 2)
				time = "0"+time;
			else
				time = time;
                        if (hour.equalsIgnoreCase(time)) {
                            out.println("<option value='"+time+"' selected>"+time+"</option>");
                        } else {
                            out.println("<option value='"+time+"'>"+time+"</option>");
                        }
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
                        if (min.equalsIgnoreCase(time)) {
                            out.println("<option value='"+time+"' selected>"+time+"</option>");
                        } else {
                            out.println("<option value='"+time+"'>"+time+"</option>");
                        }
			
		}	
	%>
        </select>
      </td>
    </tr>
    <tr class="CalendarRow"> 
      <td class="CalendarRow"> 
        <table align="center" border="1" cellspacing="0" cellpadding="1">
          <tr> 
            <td align='center'><font size="3" color="Red" face="Arial, Helvetica, sans-serif"><b><font color="#CC0000">S</font></b></font></td>
            <td align='center'><font size="3" color="#006633" face="Arial, Helvetica, sans-serif"><b>M</b></font></td>
            <td align='center'><font size="3" color="#006633" face="Arial, Helvetica, sans-serif"><b>T</b></font></td>
            <td align='center'><font size="3" color="#006633" face="Arial, Helvetica, sans-serif"><b>W</b></font></td>
            <td align='center'><font size="3" color="#006633" face="Arial, Helvetica, sans-serif"><b>T</b></font></td>
            <td align='center'><font size="3" color="#006633" face="Arial, Helvetica, sans-serif"><b>F</b></font></td>
            <td align='center'><font size="3" color="#006633" face="Arial, Helvetica, sans-serif"><b>S</b></font></td>
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
				out.println("<tr>"+tdSpace);
				for(int dd=1;dd<=ddEnd[i];dd++) {
					String n1="but"+dd;
					out.println("<td><input class=\"main_butts\" type='button' name="+n1+" value='0"+dd+"' onClick='setTimeInWindow("+dd+")'></td>");
				}
			} else if (firstday == 6) {
				out.println("<tr>"+tdSpace);
				out.println("<td><input class=\"main_butts\" type='button' name='but1' value='01' onClick='setTimeInWindow(1)'></td></tr>");
			}
			for(int y=yValue[i];y<=lastdate;) {
				out.println("<tr>");
				for(int r=1;r<=7;r++) {
					String n1="but";
					String n=n1+y;
					if(y<=lastdate) {
						if(y<=9) {
							out.println("<td><input class=\"main_butts\" type='button' name="+n+" value='0"+y+"' onClick='setTimeInWindow("+y+")'></td>");
						} else
							out.println("<td><input class=\"main_butts\" type='button' name="+n+" value="+y+" onClick='setTimeInWindow("+y+")'></td>");
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
    <tr class="CalendarRow"> 
      <td nowrap align="center" class="CalendarRow"> 
        <table border="0" cellspacing="0" cellpadding="0"><%
            if (isAll.equalsIgnoreCase("false")) {
            %><tr> 
            <td align="center" height="8"><b><font size="1" face="Verdana, Arial, Helvetica, sans-serif">From</font></b></td>
            <td height="8"><b></b></td>
            <td align="center" height="8"><b><font size="1" face="Verdana, Arial, Helvetica, sans-serif">To</font></b></td>
            <td align="center" height="8">&nbsp;</td>
          </tr><%
            } else {
            %><tr> 
                <td align="center" colspan="4" height="8"><b><font size="1" face="Verdana, Arial, Helvetica, sans-serif">To</font></b></td>
            </tr><%
            }
            if (isAll.equalsIgnoreCase("false")) {
            %>
          <tr> 
            <td> 
              <input type="text" name="fromDate" size="<%= textFieldSize %>" value="<%= fromDate %>" onFocus="blur();">
            </td>
            <td>-</td>
            <td> 
              <input type="text" name="toDate" size="<%= textFieldSize %>" value="<%= toDate %>" onFocus="blur();">
            </td>
          </tr><%
            } else {
            %><tr> 
            <td colspan="4" align="center"> 
              <input type="hidden" name="fromDate" value="<%= fromDate %>">
              <input type="text" name="toDate" size="16" value="<%= toDate %>" onFocus="blur();">
            </td>
          </tr><%
            }
            %>
          
        </table>
      </td>
    </tr>
    <tr class="CalendarHead"> 
      <td align="center" class="CalendarHead"> 
        <input class="main_butts" type=button name="ok" value="  OK  " onClick="setDateTimeInParentWindow()">
        &nbsp;&nbsp; 
        <input class="main_butts" type="button" name="Clear" value="Clear" onClick="clearTime();">
        &nbsp;&nbsp; 
        <input class="main_butts" type="button" name="Cancel" value="Cancel" onClick="window.close();">
      </td>
    </tr>
  </table>
  </form>

<script language=JavaScript>
var isAll = '<%= isAll %>'
function clearTime() {
    document.calendarfrm.toDate.value = '';
    if (isAll == "false") {
        document.calendarfrm.fromDate.value = '';
    }
    document.calendarfrm.date_mode.selectedIndex = 0;
    dateModeChange();
}

function timeOnChange() {
    var month       = document.calendarfrm.month.options[document.calendarfrm.month.selectedIndex].value
    var year        = document.calendarfrm.year.options[document.calendarfrm.year.selectedIndex].value
    var firstDayInstance = new Date(year, month, 1);
    var firstDay = firstDayInstance.getDay();
    var months=new Array("January","February","March","April","May","June","July","August","September","October","November","December")
    firstDayInstance = null;
    var days = getDays(month, year);
    document.calendarfrm.action = "calendarDateTime.jsp?month="+months[month]+"&year="+year+"&firstday="+firstDay+"&lastdate="+days;
    document.calendarfrm.submit();
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
function setTimeInWindow(value) {
    var date    = dateProcessing(value);
    var timeVal = document.calendarfrm.hour.options[document.calendarfrm.hour.selectedIndex].value + ":" + document.calendarfrm.min.options[document.calendarfrm.min.selectedIndex].value;
    var timeObj = date + "@" + timeVal;
    var dateMode = document.calendarfrm.date_mode.options[document.calendarfrm.date_mode.selectedIndex].value
    if (dateMode == "from") {
        document.calendarfrm.fromDate.value = timeObj;
        document.calendarfrm.date_mode.selectedIndex = 1;
        dateModeChange();
    } else {
        document.calendarfrm.toDate.value = timeObj;
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
function setDateTimeInParentWindow() {
    if (validate()) {
        var txtSrc          = document.calendarfrm.txtSrc.value;
        var fromDate        = "";
        if (isAll == "false") {
            fromDate = document.calendarfrm.fromDate.value;
        } else {
            fromDate = '<%= fromDate %>'
        }
        var toDate          = document.calendarfrm.toDate.value;
         if (txtSrc == "planned") {
               window.opener.document.maintenance.plannedStartDTM.value = fromDate;
               window.opener.document.maintenance.plannedEndDTM.value  = toDate;
        } else {
              window.opener.document.maintenance.actualStartDTM.value = fromDate;
              window.opener.document.maintenance.actualEndDTM.value  = toDate;
        } 
        window.close(); 
    }
}
function validate() {
    var fromDate            = "";
    if (isAll == "false") {
         fromDate = document.calendarfrm.fromDate.value;
    } else {
         fromDate = '<%= fromDate %>'
    }
    var toDate              = document.calendarfrm.toDate.value;
    var flag                = true;
    if ((isSpace(fromDate)) && (isSpace(toDate))) {
        flag = true;
    } else if((isAll == "false") && (isSpace(fromDate))) {
        flag = false;
        alert("From Date/Time cannot be empty");
    } else if ((isAll == "false") && (isSpace(toDate))) {
        flag = false;
        alert("To Date/Time cannot be empty");
    } else if (isAll == "false") {
        var fromDateObj         = getDateObj(fromDate);
        var fromTimeInMilli     = fromDateObj.getTime();
        if (!isSpace(toDate)) {
            var toDateObj           = getDateObj(toDate);
            var toTimeInMilli       = toDateObj.getTime();
            if (toTimeInMilli <= fromTimeInMilli) {
                flag = false;
                alert("To Date/Time should be greater than From Date/Time");
            }
        }
    } 
    return flag;
}

function getDateObj(dateVar) {
    var dateTimeArray = dateVar.split("@");
    var date = dateTimeArray[0];
    var time = dateTimeArray[1];
    var dateArray = date.split("/");
    var timeArray = time.split(":");
    var month = dateArray[0] - 1;
    var day   = dateArray[1];
    var year  = dateArray[2];
    var hour  = timeArray[0];
    var min   = timeArray[1];
    var sec   = 0;
    var ms    = 0;
    var dateObj = new Date(year, month, day, hour, min, sec, ms);
    return dateObj;
}
function dateModeChange() {
	//alert('mode change');
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
</script>
</body>
</html>