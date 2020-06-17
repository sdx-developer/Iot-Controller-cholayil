<%@ page import="com.firstis.common.util.general.*" %>
<%@ page import="java.util.StringTokenizer" %>
<%
    String nameOfApplication = Constants.getApplication();
    String imgBase = null;
    String message = request.getParameter("message");
    String controlMode = Symbols.NO_FLAG;
    if (request.getParameter("controls") != null) {
        controlMode = request.getParameter("controls");
    }
    boolean lMsgFlag = true;
    String validate = "";

    if (message != null && message.trim().length() > 0) {
        if (message.trim().equalsIgnoreCase("DB Down")) {
            imgBase = "Database-down";
        }
        message = message.replace('~', ' ');

        if (message.startsWith("License Expired")) {
            validate = "Expired";
        }
        String[] lSpecial = {"`", "!", "#", "$", "%", "^", "&", "*", "(", ")", "+", "-", "=", "|", ";", "@", "\'", "\"", "<", ">"};
        for (int x = 0; x < lSpecial.length; x++) {
            StringTokenizer lStok = new StringTokenizer(message.trim(), lSpecial[x].trim());
            if (lStok.countTokens() > 2) {
                lMsgFlag = false;
            }
        }
    }
    if (!lMsgFlag) {
        message = "";
    }
    String destination = request.getParameter("destination");
    boolean lDestFlag = true;
    if (destination != null && destination.trim().length() > 0) {
        String[] lSpecial = {"`", "!", "#", "$", "%", "^", "&", "*", "(", ")", "+", "-", "=", "|", ";", "@", "\'", "\"", "<", ">"};
        for (int x = 0; x < lSpecial.length; x++) {
            StringTokenizer lStok = new StringTokenizer(destination.trim(), lSpecial[x].trim());
            if (lStok.countTokens() > 2) {
                lDestFlag = false;
            }
        }
    }
    if (!lDestFlag) {
        destination = "";
    }
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

    String lStandAloneCaledar = request.getParameter("STANDALONECALENDAR");
    boolean lCalendarFlag = true;
    if (lStandAloneCaledar != null && lStandAloneCaledar.trim().length() > 0) {
        String[] lSpecial = {"`", "!", "#", "$", "%", "^", "&", "*", "(", ")", "+", "-", "=", "|", ";", "@", "\'", "\"", "<", ">"};
        for (int x = 0; x < lSpecial.length; x++) {
            StringTokenizer lStok = new StringTokenizer(lStandAloneCaledar.trim(), lSpecial[x].trim());
            if (lStok.countTokens() > 2) {
                lCalendarFlag = false;
            }
        }
    }
    if (!lCalendarFlag) {
        lStandAloneCaledar = "";
    }
%>
<html>
<head>
    <title>Error Page</title>
    <script language="JavaScript" src="<%= request.getContextPath() %>/common/load.js"></script>
    <LINK REL="STYLESHEET" TYPE="text/css" HREF="../../<%= nameOfApplication %>/common/ColorScheme.css">
</head>
<script language="JavaScript" src="../../<%= nameOfApplication %>/common/global.js"></script>
<body class="jui jennifer" LEFTMARGIN="0" TOPMARGIN="0" MARGINWIDTH="0" MARGINHEIGHT="0" onLoad="loadProgressBar();"
      onUnload="unloadProgressBar();">
<%
    if (lStandAloneCaledar != null && lStandAloneCaledar.equalsIgnoreCase("yes")) {
%>
<form name="errorfrm" method="post" action="<%= request.getContextPath()  %>/calendar/index.html">
    <%

} else  if (frameTarget != null) {
    %>
    <form name="errorfrm" method="post" action="/<%= nameOfApplication %>/<%= destination %>"
          target="<%= frameTarget %>">
        <input type="hidden" name="from" value="out">
        <input type="hidden" name="errmsg" value=<%=validate%>>
        <% } else {    %>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td>&nbsp;</td>
            </tr>
        </table>
        <form name="errorfrm" method="post" action="<%= destination %>">
            <% } %>
            <table width="100%" height="52" border="0" align="center" cellpadding="0" cellspacing="1"
                   class="border_title_table">
                <tr valign="top" class="border_title_tr">
                    <td width="100%" class="border_title_td">
                        <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                            <tr align="center" class="column_title_tr">
                                <td height="17" class="column_title_td">ERRORS/WARNINGS</td>
                            </tr>
                            <tr align="center" class="bodycolor">
                                <td class="bodycolor"><br> <font class="save_error">
                                    <% if (imgBase != null) { %>
                                    <img src="<%= request.getContextPath() %>/images/<%=imgBase%>.gif" width="95"
                                         height="90" align="absmiddle">
                                    <% } else { %>
                                    <img src="<%= request.getContextPath() %>/images/Errorandwarning.gif" width="95"
                                         height="90" align="absmiddle">
                                    <% } %>
                                    <% if (message != null && message.trim().length() > 0) { %>
                                    <img src="<%= request.getContextPath() %>/images/10x10.gif" width="10" height="10"
                                         align="absmiddle"><%=message%>
                                </font><img src="<%= request.getContextPath() %>/images/10x10.gif" width="110"
                                            height="1" align="absmiddle"><br>
                                    <% } %>
                                </td>
                            </tr>
                            <tr class="bodycolor">
                                <% if (controlMode.trim().equals(Symbols.YES_FLAG)) { %>
                                <% if (lStandAloneCaledar != null && lStandAloneCaledar.equalsIgnoreCase("yes")) { %>
                                <td width="100%" align="center" height="14" class="bodycolor"><font class="normal"><a
                                        href="javascript:relogin('OK')">OK</a></font></td>
                                <% } else if (frameTarget != null) { %>
                                <td width="100%" align="center" height="14" class="bodycolor"><font class="normal"><a
                                        href="javascript:relogin('CLOSE')">OK</a></font></td>
                                <% } else { %>
                                <td width="100%" align="center" height="14" class="bodycolor"><font class="normal"><a
                                        href="javascript:relogin('OK')">OK</a></font></td>
                                <% } %>
                                <% } else {
                                    if (message != null && message.trim().length() > 0) {
                                        if (message.equalsIgnoreCase("Vulnerable input found") || message.equalsIgnoreCase("Error in Processing") || message.equalsIgnoreCase("DB~Down") || message.equalsIgnoreCase("Session~Expired") || message.equalsIgnoreCase("Access~Denied")) {
                                %>
                                <td width="100%" align="center" height="14" class="bodycolor"><font class="normal"><a
                                        href="javascript:relogin1()">OK</a></font></td>
                                <% }
                                }
                                } %>
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
<script language="JavaScript">
    function relogin(to) {
        if (to == "CLOSE") {
            // closePopUps();
        }
        document.errorfrm.target = "main";
        window.document.errorfrm.submit();
    }
    function relogin1() {
        var url = '<%= request.getContextPath()%>/LoginServlet?from=out';
        document.errorfrm.action = url;
        document.errorfrm.target = "main";
        document.errorfrm.submit();

    }

</script>
</html>
