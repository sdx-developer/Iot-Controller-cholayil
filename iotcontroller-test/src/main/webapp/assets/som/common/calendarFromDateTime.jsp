<%@ page import ="java.util.Calendar" %>
<%@ page import ="java.util.Date" %>
<html>
<head>
<title>Calender</title>
<LINK REL="STYLESHEET" TYPE="text/css" HREF="ColorScheme.css"> 
</head>
<%
    String	appName		= com.firstis.common.util.general.Constants.getApplication();
    String	selectString = "";
%>
<body class="jui jennifer" LEFTMARGIN="0" TOPMARGIN="0" MARGINWIDTH="0" MARGINHEIGHT="0" onLoad = "document.calendarfrm.fromDate.focus()">
<%    
    String  month   = "";
    String  from    = "",holi = "",exduty = "",vac = "",alldates = "";
    String  year1   = "",lastdate1 = "",firstday1 = "";
    String  time1   = "00";
    String  dateMode = "";
    String  selectedString = "";
    String fromDate = "";
    String toDate = "";

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
                dateMode = "from";
                fromDate = request.getParameter("fromDate");
                //toDate = request.getParameter("toDate");
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
                //dateMode = request.getParameter("date_mode");
                fromDate = request.getParameter("fromDate");
                //toDate = request.getParameter("toDate");
	}
%>
<form name="calendarfrm" method="post">
<script language="JavaScript" src="global.js"></script>
<input type="hidden" name="FROM" value="<%= from %>">

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
		 for (int i=2001;i<=2100;i++) {
			if (year==i) {
				out.println("<option value="+i+" selected>"+i+"</option>");
			} else {
				out.println("<option value="+i+">"+i+"</option>");
			}
		}
		%>
        </select>
        <br>
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
      </td>
    </tr>
    <tr class="CalendarRow"> 
      <td class="CalendarRow"> 
        <table align="center" border="1" cellspacing="0" cellpadding="1">
          <tr> 
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
        <table border="0" cellspacing="0" cellpadding="0">          
          <tr> 
            <td> 
              <input type="text" name="fromDate" size="16" value="<%= fromDate %>" onFocus="blur();">
            </td>        
          </tr>
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
function timeOnChange() {
    var month = document.calendarfrm.month.options[document.calendarfrm.month.selectedIndex].value
    var year = document.calendarfrm.year.options[document.calendarfrm.year.selectedIndex].value
    //var dateMode = document.calendarfrm.date_mode.options[document.calendarfrm.date_mode.selectedIndex].value
    var from			= document.calendarfrm.FROM.value;    
    var firstDayInstance = new Date(year, month, 1);
    var firstDay = firstDayInstance.getDay();
    var months=new Array("January","February","March","April","May","June","July","August","September","October","November","December")
    firstDayInstance = null;
    var days = getDays(month, year);
    document.calendarfrm.action = "calendarFromDateTime.jsp?month="+months[month]+"&year="+year+"&firstday="+firstDay+"&lastdate="+days;
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
    //var dateMode = document.calendarfrm.date_mode.options[document.calendarfrm.date_mode.selectedIndex].value
    //if (dateMode == "from") {
        document.calendarfrm.fromDate.value = timeObj;
        //document.calendarfrm.date_mode.selectedIndex = 1;
    /*} else {
        document.calendarfrm.toDate.value = timeObj;
    }*/
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
    //if (validate()) {
        var fromDate        = document.calendarfrm.fromDate.value;
        //var toDate          = document.calendarfrm.toDate.value;
         //if (txtSrc == "planned") {
                window.opener.document.utilities.dateTime.value = fromDate;
                //window.opener.document.maintenance.plannedEndDTM.value  = toDate;
        /*} else {
            window.opener.document.maintenance.actualStartDTM.value = fromDate;
            window.opener.document.maintenance.actualEndDTM.value  = toDate;
        }*/ 
        window.close(); 
    //}
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

function clearTime() {
	document.calendarfrm.fromDate.value = "";
}
</script>
</body>
</html>