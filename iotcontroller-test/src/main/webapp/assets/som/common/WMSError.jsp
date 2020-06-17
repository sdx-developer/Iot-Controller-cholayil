<%@ page import="com.firstis.common.util.general.*" %>
<html>
<head>
<script language="JavaScript" src="<%= request.getContextPath() %>/common/load.js"></script>

<title>Error Page</title>
</head>
<%
   String cookieTrail = (String) request.getAttribute("COOKIETRIAL");
   String error = (String) request.getAttribute("ERROR");
   String headingLabel = (String) request.getAttribute("HEADINGLABEL");
%>
<body topmargin="5" leftmargin="5" marginheight="5" marginwidth="5" onLoad="loadProgressBar();" onUnload="unloadProgressBar();"> 
<form name="errorfrm" method="post" action="">
<table width="100%" border="0" cellpadding="0" border="0" bgcolor="#CCCCCC" height="10">
   <tr height="10">  
       <td height="10"><%= cookieTrail %></td>
   </tr>
</table>
<table width="100%" border="0" cellspacing="1" cellpadding="1" bgcolor="#000000">
    <tr> 
        <td width="100%">
            <table width="100%" border="0" cellspacing="1" cellpadding="3" bgcolor="#000000">
                <tr bgcolor="#CCCC99"> 
                    <td width="100%" align="center" colspan="2"><font size="2" face="Verdana, Arial, Helvetica, sans-serif"><b><%= headingLabel %></b></font></td>
                </tr>
                <tr bgcolor="#CCCC99"> 
                    <td width="100%" align="center" colspan="2"><br><br><font size="2" face="Verdana, Arial, Helvetica, sans-serif" color="red"><%= error %></font></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</form>
</body>
</html>
