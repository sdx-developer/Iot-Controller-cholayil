<%@page contentType="text/html"%>
<%@ page import="java.util.*,com.firstis.common.util.general.*" %>

<%
    String pageStr           = (String) request.getAttribute(Symbols.PAGESTRING);
    Hashtable pagingHt       = (Hashtable) request.getAttribute(Symbols.PAGING);
    String pageNoStr         = (String) pagingHt.get(Symbols.PAGE_NO);
    String startRecordNo     = (String) pagingHt.get(Symbols.START_RECORD_NO);
    String endRecordNo       = (String) pagingHt.get(Symbols.END_RECORD_NO);
    String totalCount        = (String) pagingHt.get(Symbols.TOTAL_COUNT);
    String recordCountStr    = (String) pagingHt.get(Symbols.RECORD_COUNT);

    boolean isPrevPageExists = !(startRecordNo.equals("1")) && !(startRecordNo.equals("0"));
    boolean isNextPageExists = (Integer.parseInt(endRecordNo)  < Integer.parseInt(totalCount));

    int pageNo               = Integer.parseInt(pageNoStr);
    int recordCount          = Integer.parseInt(recordCountStr);
%>
