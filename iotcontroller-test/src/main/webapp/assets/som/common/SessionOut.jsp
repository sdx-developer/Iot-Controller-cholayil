<%@ page import="com.firstis.common.util.general.*" %>
<html>
<head>
    <script language="JavaScript" src="<%= request.getContextPath() %>/common/load.js"></script>
    <LINK REL="STYLESHEET" TYPE="text/css" HREF="common/ColorScheme.css">
</head>
<% String submitTarget = "main";
    String controls = Symbols.NO_FLAG;
    String nameOfApplication = Constants.getApplicationName();
    String message = (String) request.getAttribute("message");


    if (message != null && message.trim().length() > 0) {
        message = message.replace('~', ' ');
    }
    String destination = (String) request.getAttribute("destination");
    if (destination != null && destination.trim().length() > 0) {
        controls = Symbols.YES_FLAG;
    }
    String frameTarget = request.getParameter("frameTarget");
    String lFrom = request.getParameter("from");
    String lStandAloneCaledar = request.getParameter("STANDALONECALENDAR");
    if (request.getAttribute("Action") != null) {
        submitTarget = "_self";
        frameTarget = "_self";
    }
    /*String urlTop = "common/Error.jsp?destination="+destination1+"&message="+message1+"&frameTarget"+frameTarget;
 String urlBottom = "common/Blank.html.jsp";*/
%>
<%--<!--<frameset frameborder="0" border="false" framespacing="0" rows="50%,*">
<frame name="schtype" src="/<%= nameOfApplication %>/<%= urlTop %>" scrolling="auto" noresize>
<frame name="schmain" src="/<%= nameOfApplication %>/<%= urlBottom %>" scrolling="no">
</frameset><noframes></noframes>-->--%>
<body class="jui jennifer" LEFTMARGIN="0" TOPMARGIN="0" MARGINWIDTH="0" MARGINHEIGHT="0" onLoad="document.session.submit();"
      onLoad="loadProgressBar();" onUnload="unloadProgressBar();">
<form name="session" method="post" action="<%= request.getContextPath()%>/common/Error.jsp" target="mainFrame">
    <input type="hidden" name="message" value="<%= message %>">
    <input type="hidden" name="destination" value="<%= destination %>">
    <input type="hidden" name="frameTarget" value="<%= frameTarget %>">
    <input type="hidden" name="STANDALONECALENDAR" value="<%= lStandAloneCaledar %>">
    <input type="hidden" name="from" value="<%= lFrom %>">
    <input type="hidden" name="controls" value="<%= controls %>">
</form>
</body>
</html>