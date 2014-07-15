<%@ page import="ebook.module.book.servlets.BookServletModel"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8"%>

<%
	String root = "tmpl/book/";

	BookServletModel model = (BookServletModel) request
			.getAttribute("model");
	
// 	boolean editMode = false;
	
// 	Cookie[] ck = request.getCookies();
// 	for (int i = 0; i < ck.length; i++) {
// 		if(ck[i].getName().equalsIgnoreCase("tinyEditor"))
// 			editMode = true;
// 	}
	
%>
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">

<title>Single Portfolio Item Template for Bootstrap 3</title>

<!-- Bootstrap core CSS -->
<link href="<%=root%>css/bootstrap.css" rel="stylesheet">

<!-- Custom CSS for the '3 Col Portfolio' Template -->
<link href="<%=root%>css/book.css" rel="stylesheet">
</head>

<body>

	<!-- 	<table> -->

	<%-- 		<c:if test="${model.section.group}"> --%>
	<!-- 			<p>группа -->
	<!-- 			<p> -->
	<%-- 		</c:if> --%>

	<!-- 		<tr> -->
	<%-- 			<th><c:out value="${model.section.title}">guest</c:out></th> --%>
	<!-- 		</tr> -->

	<%-- 		<c:forEach var="section" items="${model.sections}"> --%>
	<!-- 			<tr> -->
	<%-- 				<td><c:out value="${section.title}" default="guest" /></td> --%>
	<!-- 			</tr> -->
	<%-- 		</c:forEach> --%>
	<!-- 	</table> -->

	<!-- 
<c:forEach var="item" begin="1" end="6">
      <p>Item ${item}</p> 
   </c:forEach>
    -->
	<nav class="navbar navbar-static-top navbar-inverse" role="navigation">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-ex1-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="http://startbootstrap.com">${model.section.title}</a>
			</div>

			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse navbar-ex1-collapse">
				<ul class="nav navbar-nav">
					<c:forEach var="section" items="${model.sections}">
						<li><a href="#${section.id}">${section.title}</a></li>
					</c:forEach>
				</ul>
			</div>
			<!-- /.navbar-collapse -->
		</div>
		<!-- /.container -->
	</nav>


	<c:forEach var="section" items="${model.sections}">

		<div class="container" id=${section.id}>

			<div class="row">

				<div class="col-lg-12">
					<h1 class="page-header">${section.title}</h1>

					<div class="back-to-top">
						<a href="#"><small>Наверх</small></a>

						<c:if test="${model.swtMode}">
							<a href="#" class="change-block"> <span
								class="glyphicon glyphicon-edit"></span> <small>Изменить</small>
							</a>
						</c:if>
					</div>
				</div>

			</div>

			<div class="row">

				<div class="col-md-8">
					<!-- <img class="img-responsive" src="http://placehold.it/750x500"> -->
					<img class="big-picture img-responsive" src="/img?book=2&id=5">
				</div>

				<div class="col-md-4">${section.text}</div>

			</div>

			<div class="row">

				<c:if test="${not empty section.images}">
					<div class="col-lg-12">
						<h3 class="page-header">Картинки</h3>
					</div>
				</c:if>

				<c:forEach var="image" items="${section.images}">
					<div class="col-sm-3 col-xs-6">
						<a class="small-picture" href="#${section.id}"> <img class="img-responsive portfolio-item"
							src="${image.url}">
						</a>
					</div>
				</c:forEach>

			</div>

		</div>
		<!-- /.container -->

	</c:forEach>

	<div class="container">

		<hr>

		<footer>
			<div class="row">
				<div class="col-lg-12">
					<p>Copyright &copy; Company 2013</p>
				</div>
			</div>
		</footer>

	</div>
	<!-- /.container -->

	<!-- JavaScript -->
	<script src="<%=root%>js/jquery-1.10.2.js"></script>
	<script src="<%=root%>js/bootstrap.js"></script>
	<script src="<%=root%>js/book.js"></script>
<%-- 	<script src="<%=root%>js/jquery.cookie.js"></script> --%>
</body>

</html>
