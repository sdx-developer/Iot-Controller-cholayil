<%@ page import ="java.util.*,com.firstis.wms.dao.maintenance.*,com.firstis.wms.dao.config.WeekDaysDom" %>
<html>
<head>
<title>Calendar</title>
<LINK REL="STYLESHEET" TYPE="text/css" HREF="ColorScheme.css"> 
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
    String fromTime = "";
    String toTime = "";
    String hour = "";
    String min = "";
    String isAll = ""; 
    int currentYear = 0;
    StringTokenizer stok            = null;
    StringTokenizer stok1           = null;

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
                fromDate = request.getParameter("fromDate1");
                toDate = request.getParameter("toDate1");
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
                fromDate = request.getParameter("fromDate1");
                toDate = request.getParameter("toDate1");
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
       
        String dateSetting = request.getParameter("dateSetting");
        if (dateSetting == null) {
            dateSetting = "from";
        }

         // Dhana: Added for the frequency selection

        String[] repeatPeriodDisplayArray = WeekDaysDom.REPEAT_PERIOD_DISPLAY_ARRAY;
        String[] repeatPeriodValueArray = WeekDaysDom.REPEAT_PERIOD_VALUE_ARRAY;
        String[] repeatFrequencyDisplayArray = WeekDaysDom.REPEAT_FREQUENCY_DISPLAY_ARRAY;
        String[] repeatFrequencyValueArray = WeekDaysDom.REPEAT_FREQUENCY_VALUE_ARRAY;
        String[] repeatOnPeriodDisplayArray = WeekDaysDom.REPEAT_ON_PERIOD_DISPLAY_ARRAY;
        String[] repeatOnPeriodValueArray = WeekDaysDom.REPEAT_ON_PERIOD_VALUE_ARRAY;
        String[] dayArray = WeekDaysDom.DAY_ARRAY;
        String[] repeatOnFrequencyDisplayArray = WeekDaysDom.REPEAT_ON_FREQUENCY_DISPLAY_ARRAY;
        String[]  repeatOnFrequencyValueArray = WeekDaysDom.REPEAT_ON_FREQUENCY_VALUE_ARRAY;
        String repeat = request.getParameter("repeat");
        String repeatOn = "";
        String frequencyUnit = "";
        String frequencyPeriod = "";
        String frequencyUnitOn = "";
        String frequencyPeriodOn = "";
        String frequencyUnitPeriodOn = "";
        String repeatChecked = "checked";
        String repeatOnChecked = "";
        if (repeat != null) {
            frequencyUnit = request.getParameter("frequencyUnit");
            frequencyPeriod = request.getParameter("frequencyPeriod");           
        } else {
            frequencyUnitOn = request.getParameter("frequencyUnitOn");
            frequencyPeriodOn = request.getParameter("frequencyPeriodOn");
            frequencyUnitPeriodOn = request.getParameter("frequencyUnitPeriodOn");
            repeatOnChecked = "checked";
            repeatChecked = "";
        }
        if (frequencyUnit == null) {
            frequencyUnit = "";
        }
        if (frequencyPeriod == null) {
            frequencyPeriod = "";
        }
        if (frequencyUnitOn == null) {
            frequencyUnitOn = "";
        }
        if (frequencyPeriodOn == null) {
            frequencyPeriodOn = "";
        }
        if (frequencyUnitPeriodOn == null) {
            frequencyUnitPeriodOn = "";
        }
%>
<form name="calendarfrm" method="post">
<script language="JavaScript" src="global.js"></script>
<input type="hidden" name="FROM" value="<%= from %>">
<input type="hidden" name="txtSrc" value="<%= txtSrc %>">
<input type="hidden" name="isAll" value="<%= isAll %>">
<input type="hidden" name="dateSetting" value="<%= dateSetting %>">
<input type="hidden" name="fromDateH" value="<%= fromDate %>">
<input type="hidden" name="toDateH" value="<%= toDate %>">

  <table border="1" align="center" cellpadding="2" cellspacing="1">
    <tr align="center"> 
      <td nowrap>Date/Time/Frequency Calendar</td>
    <tr align="center"> 
      <td> <%
        if (fromDate != null) {
            stok = new StringTokenizer(fromDate, "-"); 
            fromDate = "";
            if (toDate != null) {
                stok1 = new StringTokenizer(toDate, "-");
                toDate = "";
            }
            if (stok.countTokens() == 2) {
                fromDate = stok.nextToken();
                toDate = stok.nextToken();
            } else if (stok.countTokens() == 1) {
                fromDate = stok.nextToken();
            }
        }
        if (toDate != null) {
            if (stok1.countTokens() == 2) {
                fromTime = stok1.nextToken();
                toTime = stok1.nextToken();
            } else if (stok1.countTokens() == 1) {
                fromTime = stok1.nextToken();
            }
        }


      %>
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
      </td>
    </tr>
    <tr> 
      <td> 
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
    <tr> 
      <td nowrap align="center"> 
        <table border="0" cellspacing="0" cellpadding="0">
          <%
            if (isAll.equalsIgnoreCase("false")) {
            %>
          <tr> 
            <td align="center" height="8"><b><font size="1" face="Verdana, Arial, Helvetica, sans-serif">From 
              Date </font></b></td>
            <td height="8"><b></b></td>
            <td align="center" height="8"><b><font size="1" face="Verdana, Arial, Helvetica, sans-serif">To 
              Date </font></b></td>
            <td align="center" height="8">&nbsp;</td>
          </tr>
          <%
            } else {
            %>
          <tr> 
            <td align="center" colspan="4" height="8"><b><font size="1" face="Verdana, Arial, Helvetica, sans-serif">To</font></b></td>
          </tr>
          <%
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
          </tr>
          <%
            } else {
            %>
          <tr> 
            <td colspan="4" align="center"> 
              <input type="hidden" name="fromDate" value="<%= fromDate %>">
              <input type="text" name="toDate" size="16" value="<%= toDate %>" onFocus="blur();">
            </td>
          </tr>
          <tr> 
            <td colspan="4" align="center">&nbsp;</td>
          </tr>
          <%
            }
            %>
        </table>
      </td>
    </tr>
    <tr> 
      <td></td>
    </tr>
    <tr> 
      <td align="center"> 
        <select name="date_mode" size="1" onChange="dateModeChange();">
          <%
            if ((dateMode != null) && (dateMode.equalsIgnoreCase("to"))) {
                selectedString = "selected";
            } else {
               selectedString = "";
            }
            if (isAll.equalsIgnoreCase("false")) {
        %>
          <option value='from'>From</option>
          <%
            }
        %>
          <option value='to' <%= selectedString %>>To</option>
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
        </select>&nbsp;
        <input class="main_butts" type=button name="time" value="Set Time" onClick="setTime()">
      </td>
    </tr>
    <tr>
      <td align="center">
        <table border="0" cellspacing="0" cellpadding="0">
          <%
            if (isAll.equalsIgnoreCase("false")) {
            %>
          <tr> 
            <td align="center" height="8"><b><font size="1" face="Verdana, Arial, Helvetica, sans-serif">From 
              Time </font></b></td>
            <td height="8"><b></b></td>
            <td align="center" height="8"><b><font size="1" face="Verdana, Arial, Helvetica, sans-serif">To 
              Time </font></b></td>
            <td align="center" height="8">&nbsp;</td>
          </tr>
          <%
            } else {
            %>
          <tr> 
            <td align="center" colspan="4" height="8"><b><font size="1" face="Verdana, Arial, Helvetica, sans-serif">To</font></b></td>
          </tr>
          <%
            }
            if (isAll.equalsIgnoreCase("false")) {
            %>
          <tr> 
            <td> 
              <input type="text" name="fromTime" size="<%= textFieldSize %>" value="<%= fromTime %>" onFocus="blur();">
            </td>
            <td>-</td>
            <td> 
              <input type="text" name="toTime" size="<%= textFieldSize %>" value="<%= toTime %>" onFocus="blur();">
            </td>
          </tr>
          <%
            } else {
            %>
          <tr> 
            <td colspan="4" align="center"> 
              <input type="hidden" name="fromTime" value="<%= fromDate %>">
              <input type="text" name="toTime" size="16" value="<%= toDate %>" onFocus="blur();">
            </td>
          </tr>
          <tr> 
            <td colspan="4" align="center">&nbsp;</td>
          </tr>
          <%
            }
            %>            
        </table>
      </td>
    </tr>
        <tr> 
        <td colspan="4" align="center">
        <table border="1" align="center" cellpadding="2" cellspacing="1">
            <tr><td colspan="6"><b>Repeating:</b></td>
            </tr>
            <tr>
                <td width="5%"><input type="radio" value="repeat" name="repeat" <%= repeatChecked %> ></td>
                <td width="25%"><b>Repeat&nbsp;</b></td>
                <td width="15%">
                    <select name="frequencyPeriod">
                    <% for (int i = 0; i < repeatPeriodDisplayArray.length; i++) { 
                            if (frequencyPeriod.equals(repeatPeriodValueArray[i])) { %>
                                <option value="<%= repeatPeriodValueArray[i] %>" selected ><%= repeatPeriodDisplayArray[i] %></option>
                            <% } else { %>                        
                                <option value="<%= repeatPeriodValueArray[i] %>"><%= repeatPeriodDisplayArray[i] %></option>
                     <% 
                               }
                        } %>                              
                    </select>
                </td>
                <td colspan="3" width="55%">
                    <select name="frequencyUnit">
                        <% for (int i = 0; i < repeatFrequencyDisplayArray.length; i++) { 
                                if (frequencyUnit.equals(repeatFrequencyValueArray[i])) { %>
                                    <option value="<%= repeatFrequencyValueArray[i] %>" selected><%= repeatFrequencyDisplayArray[i] %></option>
                                <% } else { %>   
                                    <option value="<%= repeatFrequencyValueArray[i] %>"><%= repeatFrequencyDisplayArray[i] %></option>
                        <% 
                                } 
                           } %>
                    </select>
                </td>
            </tr>
          <tr>
                <td width="5%"><input type="radio" value="repeatOn" name="repeat" <%= repeatOnChecked %> ></td>
                <td width="25%"><b>Repeat On</b></td>
                <td width="15%">
                    <select name="frequencyPeriodOn">
                    <% for (int i = 0; i < repeatOnPeriodDisplayArray.length; i++) {
                            if (frequencyPeriodOn.equals(repeatOnPeriodValueArray[i])) { %>
                                <option value="<%= repeatOnPeriodValueArray[i] %>" selected><%= repeatOnPeriodDisplayArray[i] %></option>
                            <% } else { %>
                                <option value="<%= repeatOnPeriodValueArray[i] %>"><%= repeatOnPeriodDisplayArray[i] %></option>
                     <%         
                            }
                     } %>
                    </select>
                </td>
                <td width="15%">
                    <select name="frequencyUnitOn">
                        <% for (int i = 0; i < dayArray.length; i++) { 
                            if (frequencyUnitOn.equals(dayArray[i])) { %>
                                <option value="<%= dayArray[i] %>" selected><%= dayArray[i] %></option>
                            <% } else { %>
                                <option value="<%= dayArray[i] %>"><%= dayArray[i] %></option>
                            <% } 
                          }
                        %>                            
                    </select>
                </td>
                <td width="25%"><b>of every</b></td>
                <td width="15%">
                    <select name="frequencyUnitPeriodOn">
                        <% for (int i = 0; i < repeatOnFrequencyDisplayArray.length; i++) { 
                             if (frequencyUnitPeriodOn.equals(repeatOnFrequencyValueArray[i])) { %>
                                <option value="<%= repeatOnFrequencyValueArray[i] %>" selected><%= repeatOnFrequencyDisplayArray[i] %></option>
                            <% } else { %>
                                <option value="<%= repeatOnFrequencyValueArray[i] %>"><%= repeatOnFrequencyDisplayArray[i] %></option>
                        <%      }
                           }
                        %>
                    </select>
                </td>
            </tr>
          </table>
        </td>
    </tr>
 
    <tr> 
      <td align="center"> 
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
    document.calendarfrm.toTime.value = '';
    if (isAll == "false") {
        document.calendarfrm.fromDate.value = '';
        document.calendarfrm.fromTime.value = '';
    }
    document.calendarfrm.date_mode.selectedIndex = 0;
    document.calendarfrm.dateSetting.value = "from"
    document.calendarfrm.frequencyPeriod.selectedIndex = 0;
    document.calendarfrm.frequencyUnit.selectedIndex = 0;
    document.calendarfrm.frequencyPeriodOn.selectedIndex = 0;
    document.calendarfrm.frequencyUnitOn.selectedIndex = 0;
    document.calendarfrm.frequencyUnitPeriodOn.selectedIndex = 0;    
    dateModeChange();
}

function timeOnChange() {
    var month       = document.calendarfrm.month.options[document.calendarfrm.month.selectedIndex].value
    var year        = document.calendarfrm.year.options[document.calendarfrm.year.selectedIndex].value
    var firstDayInstance = new Date(year, month, 1);
    var firstDay = firstDayInstance.getDay();
    var months = new Array("January","February","March","April","May","June","July","August","September","October","November","December")
    firstDayInstance = null;
    var days = getDays(month, year);
    var fromDate = document.calendarfrm.fromDate.value+"-"+document.calendarfrm.toDate.value;
    var toDate = document.calendarfrm.fromTime.value+"-"+document.calendarfrm.toTime.value;
    var dateSetting = document.calendarfrm.dateSetting.value;
    if (!document.calendarfrm.fromDate.value == "") {
        dateSetting = "to"
    }
    if (fromDate == "-") {
        fromDate = ""
    }
    if (toDate == "-") {
        toDate = ""
    }
    var url = "calendarForBlockSch.jsp?month="+months[month]+"&year="+year+"&firstday="+firstDay+"&lastdate="+days+"&fromDate1="+fromDate+"&toDate1="+toDate+"&dateSetting="+dateSetting;
    document.calendarfrm.action = url;
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
    var timeObj = date;
    dateModeChange()
    var dateSetting1 = document.calendarfrm.dateSetting.value;
    if (dateSetting1 == "from") {
        document.calendarfrm.fromDate.value = timeObj;
        document.calendarfrm.dateSetting.value = "to";
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
        var fromToDate      = fromDate + "-" + toDate;
        var fromTime        = "";
        if (isAll == "false") {
            fromTime = document.calendarfrm.fromTime.value;
        } else {
            fromTime = '<%= fromTime %>'
        }
        var toTime          = document.calendarfrm.toTime.value;
        var fromToTime      = fromTime + "-" + toTime;
        if (fromToTime == "-") {
            fromToTime = ""
        }
        if (fromToDate == "-") {
            fromToDate = ""
        }
         if (txtSrc == "planned") {
               var repeat = "N";
               var repeatOn = "N";
               var textDisplay = "The Schedule ";
               var frequencyPeriod = document.calendarfrm.frequencyPeriod.options[document.calendarfrm.frequencyPeriod.selectedIndex].text;
               var frequencyUnit = document.calendarfrm.frequencyUnit.options[document.calendarfrm.frequencyUnit.selectedIndex].text;
               var frequencyPeriodOn = document.calendarfrm.frequencyPeriodOn.options[document.calendarfrm.frequencyPeriodOn.selectedIndex].text;
               var frequencyUnitOn = document.calendarfrm.frequencyUnitOn.options[document.calendarfrm.frequencyUnitOn.selectedIndex].text;
               var frequencyUnitPeriodOn = document.calendarfrm.frequencyUnitPeriodOn.options[document.calendarfrm.frequencyUnitPeriodOn.selectedIndex].text;
               if (document.calendarfrm.repeat && document.calendarfrm.repeat[0].checked) {
                    repeat = "Y";
                    textDisplay = textDisplay + "repeats " + frequencyPeriod + " " +  frequencyUnit;
               } else {
                    repeatOn = "Y";
                    textDisplay = textDisplay + "repeats on " + frequencyPeriodOn + " " +  frequencyUnitOn + " of every " + frequencyUnitPeriodOn;
               }               
               window.opener.document.maintenance.plannedStartDTM.value = fromToDate;
               window.opener.document.maintenance.plannedEndDTM.value  = fromToTime
               window.opener.document.maintenance.PlannedStartDTMForRepeat.value  = fromDate + "@" + fromTime;
               window.opener.document.maintenance.PlannedEndDTMForRepeat.value  = toDate + "@" + toTime;
               window.opener.document.maintenance.frequencyPeriod.value  = document.calendarfrm.frequencyPeriod.options[document.calendarfrm.frequencyPeriod.selectedIndex].value;
               window.opener.document.maintenance.frequencyUnit.value  = document.calendarfrm.frequencyUnit.options[document.calendarfrm.frequencyUnit.selectedIndex].value;
               window.opener.document.maintenance.frequencyPeriodOn.value  = document.calendarfrm.frequencyPeriodOn.options[document.calendarfrm.frequencyPeriodOn.selectedIndex].value;
               window.opener.document.maintenance.frequencyUnitOn.value  = document.calendarfrm.frequencyUnitOn.options[document.calendarfrm.frequencyUnitOn.selectedIndex].value;
               window.opener.document.maintenance.frequencyUnitPeriodOn.value  = document.calendarfrm.frequencyUnitPeriodOn.options[document.calendarfrm.frequencyUnitPeriodOn.selectedIndex].value;
               window.opener.document.maintenance.repeat.value = repeat;
               window.opener.document.maintenance.repeatOn.value = repeatOn;
               window.opener.document.maintenance.displayText.value = textDisplay;
        } 
        window.close(); 
    }
}
function validate() {
    var fromDate            = "";
    var fromTime            = "";
    if (isAll == "false") {
         fromDate = document.calendarfrm.fromDate.value;
         fromTime = document.calendarfrm.fromTime.value;
    } else {
         fromTime = '<%= fromTime %>'
    }
    var toDate              = document.calendarfrm.toDate.value;
    var toTime              = document.calendarfrm.toTime.value;
    //var frequencyPeriod     = document.calendarfrm.frequencyPeriod.value;
    var flag                = true;
    if ((isSpace(fromDate)) && (isSpace(toDate)) && (isSpace(fromTime)) && (isSpace(toTime))) {
        flag = true;
    } else if(isSpace(fromDate)) {
        flag = false;
        alert("From Date cannot be empty");
    } else if ((isAll == "false") && (isSpace(toDate))) {
        flag = false;
        alert("To Date cannot be empty");
    } else if(isSpace(fromTime)) {
        flag = false;
        alert("From Time cannot be empty");
    } else if(isSpace(toTime)) {
        flag = false;
        alert("To Time cannot be empty");
    } else if ((isAll == "false") && (isSpace(fromTime))) {
        flag = false;
        alert("To Time cannot be empty");
    } /* else if (isSpace(frequencyPeriod)) {
        flag = false
        alert("Every cannot be empty")
    }  else if (isNaN(frequencyPeriod)) {
        flag = false
        alert("Every should be a Number")
    } */ else {
        var fromDateObj         = getDateObj(fromDate, fromTime);
        var fromTimeInMilli     = fromDateObj.getTime();
        if (!isSpace(toDate)) {
            var toDateObj           = getDateObj(toDate, toTime);
            var toTimeInMilli       = toDateObj.getTime();
            if (toTimeInMilli <= fromTimeInMilli) {
                flag = false;
                alert("To Date/Time should be greater than From Date/Time");
            }
        }
    } 
    return flag;
}

function getDateObj(dateVar, timeVar) {
    var dateArray = dateVar.split("/");
    var timeArray = timeVar.split(":");
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
	//alert('mode change'+document.calendarfrm.date_mode.selectedIndex);
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
function setTime() {
    var timeVal = document.calendarfrm.hour.options[document.calendarfrm.hour.selectedIndex].value + ":" + document.calendarfrm.min.options[document.calendarfrm.min.selectedIndex].value;
    var dateMode = document.calendarfrm.date_mode.options[document.calendarfrm.date_mode.selectedIndex].value
    if (dateMode == "from") {
        document.calendarfrm.fromTime.value = timeVal;
        document.calendarfrm.date_mode.selectedIndex = 1;
        dateModeChange();
    } else {
        document.calendarfrm.toTime.value = timeVal;
    }
}
</script>
</body>
</html>