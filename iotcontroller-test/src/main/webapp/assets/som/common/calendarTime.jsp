<%@ page import="java.util.*" %>
<html>
<head>
<title>Time</title>
<LINK REL="STYLESHEET" TYPE="text/css" HREF="ColorScheme.css"> 
</head>
<%        
        String fromTime = request.getParameter("fromDate");
        String toTime   = request.getParameter("toDate");
        StringTokenizer stok;
        String fromHour = "00";
        String fromMin  = "00";
        String toHour   = "23";
        String toMin    = "59";
        if ((fromTime != null) && (fromTime.trim().length() > 0)) {
            stok = new StringTokenizer(fromTime, ":");
            fromHour = stok.nextToken();
            fromMin  = stok.nextToken();
        } else {
            fromTime = "00:00";
        }
        if ((toTime != null) && (toTime.trim().length() > 0)) {
            stok = new StringTokenizer(toTime, ":");
            toHour = stok.nextToken();
            toMin  = stok.nextToken();
        } else {
            toTime = "23:59";
        }

%>
<body class="jui jennifer" LEFTMARGIN="0" TOPMARGIN="0" MARGINWIDTH="0" MARGINHEIGHT="0">
<form name="calendarfrm" method="post">
  <table border="1" align="center" cellpadding="2" cellspacing="1">
    <tr> 
      <td align="center" colspan='2' class="boldness">Timings</td>
    </tr>
    <tr> 
      <td>
        &nbsp;
      </td>
      <td>Hour&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp;Min</td>
    </tr>
    <tr> 
      <td>
        <select name="type" size="1" onChange="changeTime(this.form)">
            <option value="from">From</option>
            <option value="to">To</option>
        </select>
     </td>
      <td nowrap> 
        <select name="hour" size="1" onChange="setTime(this.form, this, this.form.min)">
        <%
            String time = null;
            for(int x = 0; x < 24; x++) {
		time = x + "";
		if(time.length() != 2) {
			time = "0" + time;
                }
		if (time.equalsIgnoreCase(fromHour)) {
			out.println("<option value='"+time+"' selected>"+time+"</option>");
		} else {
			out.println("<option value='"+time+"'>"+time+"</option>");
		}
            }	
        %>
        </select>&nbsp;
        :
        &nbsp;
        <select name="min" size="1" onChange="setTime(this.form, this.form.hour, this)">
        <%
            for(int x = 0; x < 60; x++) {
		time = x + "";
		if(time.length() != 2) {
			time = "0" + time;
                }
		if (time.equalsIgnoreCase(fromMin)) {
			out.println("<option value='"+time+"' selected>"+time+"</option>");
		} else {
			out.println("<option value='"+time+"'>"+time+"</option>");
		}
            }	
        %>
        </select>
     </td>
    </tr>
    <tr>
        <td>From Time</td>
        <td>To Time</td>
    </tr>
    <tr>
        <td><input type="text" size="8" name="fromTime" value="<%= fromTime %>" onfocus="blur()"></td>
        <td><input type="text" size="8" name="toTime" value="<%= toTime %>" onfocus="blur()"></td>
    </tr>
    <tr>
        <td colspan="2" align="center">
        <input type="button" value=" OK " onClick="addTime(this.form)">&nbsp;&nbsp;
        <input type="button" value="Clear" onClick="clearTime(this.form)">&nbsp;&nbsp;
        <input type="button" value="Cancel" onclick="window.close()">
        </td>
    </tr>
  </table>
</form>
<script language="Javascript">
function clearTime(form) {    
    form.type.options.selectedIndex = 0;
    form.fromTime.value = "00:00";
    form.toTime.value = "23:59";
    setDefault(form);
}
function addTime(form) {
    window.opener.document.forms[0].plannedStartDTM.value = form.fromTime.value;
    window.opener.document.forms[0].plannedEndDTM.value = form.toTime.value;
    window.close();    
}
function setTime(form, srcHr, srcMin) {
    var type = form.type.options[form.type.options.selectedIndex].value;
    if (type == "from") {
        form.fromTime.value = srcHr.options[srcHr.options.selectedIndex].value + ":" + srcMin.options[srcMin.options.selectedIndex].value;
    } else {
        form.toTime.value = srcHr.options[srcHr.options.selectedIndex].value + ":" + srcMin.options[srcMin.options.selectedIndex].value;
    }
}
function changeTime(form) {
    setDefault(form);
}
function setDefault(form) {
    var type = form.type.options[form.type.options.selectedIndex].value;
    if (type == "from") {
        form.hour.selectedIndex = 0;
        form.min.selectedIndex = 0;
    } else {
        form.hour.selectedIndex = (form.hour.options.length) - 1;
        form.min.selectedIndex = (form.min.options.length) - 1;
    }
}
</script>
</body>
</html>