<%@page import="ebook.module.book.servlets.BookServletModel"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html;charset=UTF-8"%>

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

<title>${model.section.title}</title>

<!-- Bootstrap core CSS -->
<link href="<%=root%>css/bootstrap.min.css" rel="stylesheet">

<!-- Custom CSS for the '3 Col Portfolio' Template -->
<link href="<%=root%>css/book.css" rel="stylesheet">

<link href="<%=root%>fancybox/jquery.fancybox.css?v=2.1.5" rel="stylesheet">

</head>

<body host="${model.host}">

	<!-- NAV BAR -->
	<nav class="navbar navbar-static-top navbar-inverse" role="navigation">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-ex1-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<!-- BRAND -->
				<a class="navbar-brand" href="#">${model.section.title}</a>
			</div>

			<!-- LINKS -->
			<div class="collapse navbar-collapse navbar-ex1-collapse">
				<ul class="nav navbar-nav">
					<c:forEach var="section" items="${model.sections}">
						<li><a href="#${section.id}">${section.title}</a></li>
					</c:forEach>
				</ul>
			</div>

		</div>
	</nav><!-- /NAV BAR -->


<!--
<a class="fancybox" href="http://localhost:80/img?book=2&id=8" title="Lorem ipsum dolor sit amet"><img src="http://localhost:80/img?book=2&id=8" alt="" /></a>
-->
	<c:forEach var="section" items="${model.sections}">

		<div class="container" id=${section.id}>

			<div class="row">

				<div class="col-lg-12">

					<!-- BLOCK -->
					<c:if test="${not section.group}">

						<h3 class="page-header">
							${section.title}
						</h3>

					</c:if>

					<!-- GROUP -->
					<c:if test="${section.group}">

						<!-- SWT-->
						<c:if test="${model.swtMode}">
							<a href="#" class="openSection">
								<h2 class="page-header1">${section.title}</h2>
							</a>
						</c:if>

						<!-- BROWSER-->
						<c:if test="${not model.swtMode}">
							<a href="#" class="openSectionBrowse">
								<h2 class="page-header1">${section.title}</h2>
							</a>
						</c:if>

						<!-- UP link-->
						<div class="back-to-top">
							<a href="#"><small>Наверх</small></a>
						</div>
					</c:if>

					<!-- BLOCK -->
					<c:if test="${not section.group}">

						<!-- UP link-->
						<div class="back-to-top">
							<a href="#"><small>Наверх</small></a>

							<!-- SWT edit link-->
							<c:if test="${model.swtMode}">
								<a href="#" class="openSection"> <span
									class="glyphicon glyphicon-edit"></span> <small>Изменить</small>
								</a>
							</c:if>
						</div>

					</c:if>
				</div>

			</div>

			<!-- BLOCK -->
			<c:if test="${not section.group}">
				
				<div class="row">

					<!-- THERE ARE SMALL IMAGES -->
					<c:if test="${not empty section.images}">

						<!-- BIG IMAGE -->
						<c:if test="${section.bigImageCSS!=0}">		
								
							<div class="col-md-${section.bigImageCSS}">
								<!--img class="big-picture img-responsive" src="${section.images.get(0).url}"-->
								<a class="fancy" href="${section.images.get(0).url}&.jpg" title="${section.images.get(0).title}">
									<img class="big-picture img-responsive" src="${section.images.get(0).url}">
								</a>
							</div>

						</c:if>

						<!-- TEXT -->
						<div class="col-md-${section.textCSS}">${section.text}</div>
					</c:if>

					<!-- THERE ARE NO SMALL IMAGES -->
					<c:if test="${empty section.images}">
						<!-- TEXT -->
						<div class="col-md-12">${section.text}</div>
					</c:if>
				</div>

				<!-- SMALL IMAGES -->
				<div class="row">

					<c:if test="${not empty section.images}">
						<div class="col-lg-12">
							<h4 class="page-header">Картинки</h4>
						</div>
					</c:if>

					<c:forEach var="image" items="${section.images}">
						<div class="col-sm-2 col-xs-6">
							<!--a class="small-picture" href="#${section.id}"> <img
								class="img-responsive portfolio-item" src="${image.url}">
							</a>-->
							<c:if test="${section.bigImageCSS!=0}">	
								<a rel="group${section.id}" class="small-picture" href="#${section.id}" title="${image.title}"> <img
									class="img-responsive portfolio-item" src="${image.url}">
								</a>
							</c:if>
							<c:if test="${section.bigImageCSS==0}">	
								<a rel="group${section.id}" class="fancy" href="${image.url}&.jpg" title="${image.title}"> <img
									class="img-responsive portfolio-item" src="${image.url}">
								</a>
							</c:if>
						</div>
					</c:forEach>

				</div>
			</c:if>
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
	<script src="<%=root%>js/jquery.js"></script>
	<script src="<%=root%>fancybox/jquery.mousewheel-3.0.6.pack.js"></script>

	<script src="<%=root%>fancybox/jquery.fancybox.pack.js?v=2.1.5"></script>

	<script src="<%=root%>js/bootstrap.min.js"></script>
	<script src="<%=root%>js/book.js"></script>
	<%-- 	<script src="<%=root%>js/jquery.cookie.js"></script> --%>
</body>

</html>
