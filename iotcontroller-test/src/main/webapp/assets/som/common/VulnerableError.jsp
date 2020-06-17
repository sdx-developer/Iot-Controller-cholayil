<%@ page import="com.firstis.common.util.general.*" %>
<%@ page import="java.util.StringTokenizer" %>
<%
    String nameOfApplication = Constants.getApplication();
    String imgBase = null;
    String message = request.getParameter("message");
    String controlMode = Symbols.NO_FLAG;
    String frameTarget = request.getParameter("frameTarget");
    boolean lFrameFlag = true;
    if (frameTarget != null && frameTarget.trim().length() > 0) {
        String[] lSpecial = {"`", "!", "#", "$", "%", "^", "&", "*", "(", ")", "+", "-", "=", "|", ";", "@", "\'", "\"", "<", ">"};
        for (int x = 0; x < lSpecial.length; x++) {
            StringTokenizer lStok = new StringTokenizer(frameTarget.trim(), lSpecial[x].trim());
            if (lStok.countTokens() > 2) {
                lFrameFlag = false;
            }
        }
    }
    if (!lFrameFlag) {
        frameTarget = "";
    }



%>
<html>
<head>
<title>Error Page</title>
<script language="JavaScript" src="<%= request.getContextPath() %>/common/load.js"></script>
<LINK REL="STYLESHEET" TYPE="text/css" HREF="../../<%= nameOfApplication %>/common/ColorScheme.css">
</head>
<script language="JavaScript" src="../../<%= nameOfApplication %>/common/global.js"></script>
<body LEFTMARGIN = "0" TOPMARGIN = "0" MARGINWIDTH = "0" MARGINHEIGHT = "0" onLoad="loadProgressBar();" onUnload="unloadProgressBar();">

<form name="errorfrm" method="post" action=""  target="<%= frameTarget %>">

<table width="100%" height="52" border="0" align="center" cellpadding="0" cellspacing="1" class="border_title_table">
 <tr valign="top" class="border_title_tr">
   <td width="100%" class="border_title_td">
      <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr align="center" class="column_title_tr">
            <td height="17" class="column_title_td">ERRORS/WARNINGS</td>
              <td align="center" height="19" class="bodycolor">&nbsp;</td>
          </tr>
          <tr align="center" class="bodycolor">
            <td class="bodycolor"><br> <font class="save_error">
                <% if (imgBase !=null) { %>
                <img src="<%= request.getContextPath() %>/images/<%=imgBase%>.gif" width="95" height="90" align="absmiddle">
                <% } else { %>
                <img src="<%= request.getContextPath() %>/images/Errorandwarning.gif" width="95" height="90" align="absmiddle">
                <% } %>
                <% if (message !=null && message.trim().length()>0) { %>
                <img src="<%= request.getContextPath() %>/images/10x10.gif" width="10" height="10" align="absmiddle"><%=message%></font><img src="<%= request.getContextPath() %>/images/10x10.gif" width="110" height="1" align="absmiddle"><br>
                <% } %>
                </td>
              <td align="center" height="19" class="bodycolor">&nbsp;</td>
          </tr>
          <tr class="bodycolor">
          <td align="center" height="19" class="bodycolor">&nbsp;</td>
            <td align="center" height="19" class="bodycolor">&nbsp;</td>
          </tr>
          <tr class="bodycolor">
            <td align="center" height="19" class="bodycolor">&nbsp;</td>
            <td align="center" height="19" class="bodycolor">&nbsp;</td>
          </tr>
        </table>
    </td>
  </tr>
</table>
</form>

</body>
</html>
