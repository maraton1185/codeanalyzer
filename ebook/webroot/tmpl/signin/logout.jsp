<%@page contentType="text/html;charset=UTF-8"%>

<%
  	session.removeAttribute("User");
	session.invalidate();
%>

