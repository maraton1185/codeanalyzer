<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html;charset=UTF-8"%>

<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">

<title>${model.title}</title>

<!-- Bootstrap core CSS -->
 <jsp:include page="${initParam.bootstrap}bootstrap.css.jsp"/>

<!-- Custom CSS for the '3 Col Portfolio' Template -->
<link href="${initParam.root_book}book.css" rel="stylesheet">

</head>

<body>

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
				<c:choose>
					<c:when test="${model.swtMode}">
	            		<a class="navbar-brand" href="#">${model.title}</a>
	                </c:when>
	                <c:otherwise>
	        			<a class="navbar-brand" href="${model.url}">${model.title}</a>
	    			</c:otherwise>
				</c:choose>
				
			</div>

			<!-- LINKS -->
			<div class="collapse navbar-collapse navbar-ex1-collapse">
				<ul class="nav navbar-nav">
					<c:forEach var="section" items="${model.sections}">
						<li><a href="#${section.id}">${section.title}</a></li>
					</c:forEach>
				</ul>

				<jsp:include page="/tmpl/auth.jsp"/>
			</div>
			
		</div>
	</nav><!-- /NAV BAR -->

	<!-- BREADCUMPS -->
	<div class="container">
		<div class="row">
			<div class="col-lg-12">
				<ol class="breadcrumb">
					<c:forEach var="parent" items="${model.parents}">
	                	<li>
							<c:choose>
								<c:when test="${model.swtMode}">
	                				<a class="openSection" data="${parent.id}" href="${parent.url}">${parent.title}</a>
	                			</c:when>
	                			 <c:otherwise>
	        						<a href="${parent.url}">${parent.title}</a>
	    						</c:otherwise>
	                		</c:choose>
	                	</li>            		
	            	</c:forEach>
	            	<li class="active">${model.section.title}</li>
	            </ol>
			</div>
		</div>
	</div>

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

						<!-- TITLE-->
						<c:choose>
							<c:when test="${model.swtMode}">
                				<a href="#" class="openSection">
									<h2 class="page-header1">${section.title}</h2>
								</a>
                			</c:when>
                			<c:otherwise>
        						<a href="${section.url}">
									<h2 class="page-header1">${section.title}</h2>
								</a>
    						</c:otherwise>
                		</c:choose>

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

	<jsp:include page="/tmpl/footer.jsp"/>

	<!-- JavaScript -->
	<jsp:include page="${initParam.bootstrap}bootstrap.js.jsp"/>

	<script src="${initParam.root_book}book.js"></script>

</body>

</html>
