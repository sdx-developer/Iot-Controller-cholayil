<%@ page import="com.firstis.common.util.general.*" %>
<html>
<head>
<script language="JavaScript" src="<%= request.getContextPath() %>/common/load.js"></script>
<title>Error Page</title>
<LINK REL="STYLESHEET" TYPE="text/css" HREF="../../<%= Constants.getApplication() %>/common/ColorScheme.css"> 
</head>
<body class="jui jennifer" LEFTMARGIN="0" TOPMARGIN="0" MARGINWIDTH="0" MARGINHEIGHT="0" onLoad="loadProgressBar();" onUnload="unloadProgressBar();"> 
<form name="errorfrm" method="post" action="">
<table width="100%" border="0" cellspacing="1" cellpadding="2" class="border_title_table">
    <tr class="bodycolor"> 
        <td width="100%" class="bodycolor">
            <table width="100%" border="0" cellspacing="1" cellpadding="2" class="bodycolor">
                <tr class="bodycolor"> 
                <td width="100%" align="center" colspan="2" class="save_error"><br><br>Error in the requested page due to Data Missing<br><br><br></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</form>
</body>
</html>
 