<%@ page import="com.firstis.common.util.general.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <LINK REL="STYLESHEET" TYPE="text/css" HREF="common/ColorScheme.css">
    <script language="JavaScript" src="<%= request.getContextPath() %>/common/load.js"></script>
</head>
<body class="jui jennifer" LEFTMARGIN="0" TOPMARGIN="0" MARGINWIDTH="0" MARGINHEIGHT="0"
      onLoad="loadProgressBar();document.session.submit();" onUnload="unloadProgressBar();">
<form name="session" method="post" action="common/Error.jsp" target="mainFrame">
    <html:messages id="title" message="true" property="TITLE">
        <%--<bean:write name="title"/>--%>
        <bean:define id="message" name="title" type="java.lang.String"/>
        <input type="hidden" name="message" value="<%= message %>">
    </html:messages>
    <input type="hidden" name="destination" value="/index1.jsp">
    <input type="hidden" name="frameTarget" value="main">
</form>
</body>
</html>