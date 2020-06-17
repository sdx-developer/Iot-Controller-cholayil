<%@ page import="com.firstis.common.util.general.Symbols"%>
<%
       String lSearchTerm   = (String) request.getAttribute("SEARCH_TERM");
       String lSearchOption = (String) request.getAttribute("SEARCH_OPTION");
       String lSearchBy     = (String) request.getAttribute("SEARCH_BY");
       String lActiveFlag   = (String) request.getAttribute(com.firstis.common.util.general.Symbols.ACTIVEFLAG);
       String lAssignFlag   = (String) request.getAttribute(com.firstis.common.util.general.Symbols.ASSIGNFLAG);
       // Security Attributes
       Boolean newAccess = (Boolean) request.getAttribute("NEWACCESS");  
       Boolean deleteAccess = (Boolean) request.getAttribute("DELETEACCESS");  
       Boolean editAccess = (Boolean) request.getAttribute("EDITACCESS"); 

       
       //For Paging
       String lPageNo       = (String) request.getAttribute("PAGE_NO");
       String lPageStr           = (String) request.getAttribute("PAGE_STRING");
       if (lSearchTerm == null) {
            lSearchTerm = "";
       } else if (lSearchTerm.trim().length() == 0) {
            lSearchTerm = "";
       }

       if (lSearchOption == null) {
            lSearchOption = "";
       } else if (lSearchOption.trim().length() == 0) {
            lSearchOption = "";
       }

       if (lPageNo == null) {
            lPageNo = "";
       } else if (lPageNo.trim().length() == 0) {
            lPageNo = "";
       }

       if (lPageStr == null) {
            lPageStr = "";
       } else if (lPageStr.trim().length() == 0) {
            lPageStr = "";
       }

       if (lActiveFlag == null) {
            lActiveFlag = "";
       }

     if (lAssignFlag == null) {
          lAssignFlag = "";
       }
       if (lSearchBy == null) {
            lSearchBy = "";
       } else if (lSearchBy.trim().length() == 0) {
            lSearchBy = "";
       }
        lSearchTerm = lSearchTerm.replace(' ','~');
        lSearchOption = lSearchOption.replace(' ','~');
        lSearchBy = lSearchBy.replace(' ','~');
        
%>
<INPUT type="hidden" name="<%= com.firstis.common.util.general.Symbols.SEARCH_OPTION %>" value="<%= lSearchOption %>">
<input type="hidden" name="<%= com.firstis.common.util.general.Symbols.SEARCH_TERM %>" value="<%= lSearchTerm %>">
<input type="hidden" name="<%= com.firstis.common.util.general.Symbols.SEARCH_BY %>" value="<%= lSearchBy %>">
<input type="hidden" name="<%= com.firstis.common.util.general.Symbols.ACTIVEFLAG %>" value="<%= lActiveFlag %>">
<input type="hidden" name="<%= com.firstis.common.util.general.Symbols.ASSIGNFLAG %>" value="<%= lAssignFlag %>">
<input type="hidden" name="<%= com.firstis.common.util.general.Symbols.PAGENO %>" value="<%= lPageNo %>">
<%    if(!lPageStr.equalsIgnoreCase("") && lPageStr != null) { %>
<input type="hidden" name="<%= com.firstis.common.util.general.Symbols.PAGESTRING %>" value="<%= lPageStr %>">
<%    } %>



