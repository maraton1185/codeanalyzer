 <!doctype html public "-//w3c//dtd html 4.0 transitional//en">
  
  <%@ page contentType="text/html; charset=windows-1251" %>
  <%@ page import="java.util.*, java.text.*" %>
 <%
  String id = (String) request.getAttribute("data");

%> 
  <html>
      <head> 
          <title>Список книг</title>
          <meta http-equiv="Content-Type" content="text/html; charset=windows-1251">
     </head>
	 
     <body>
     
     <h1>Код книги: <%=id%></h1>

     </body>
  </html>
 
 
