<%@ tag import="java.time.LocalDate" %>

<%
    LocalDate tm = LocalDate.now();
    out.println(tm.plusYears(1));
%>