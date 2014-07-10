 <!doctype html public "-//w3c//dtd html 4.0 transitional//en">
  
  <%@ page contentType="text/html; charset=windows-1251" %>
  <%@ page import="java.util.*, java.text.*" %>
  <%
  	String book = request.getParameter("book");
  	String section = request.getParameter("id");
  %>
  <html>
     <head> 
          <title>Ошибка</title>
          <meta http-equiv="Content-Type" content="text/html; charset=windows-1251">
     </head>
	 
     <body>
          <h1>Книга не найдена</h1>
          <table>
          	<tr>
          		<td>Код книги:</td>
          		<td><%= book%></td>
          	</tr>
          	<tr>
          		<td>Код раздела:</td>
          		<td><%= section%></td>
          	</tr>
          </table>
     </body>
  </html>
  
  <%! 
     String getFormattedDate () 
     { 
          SimpleDateFormat sdf = new SimpleDateFormat ("dd.MM.yyyy hh:mm:ss"); 
          return sdf.format (new Date ()); 
     } 
  %>
  