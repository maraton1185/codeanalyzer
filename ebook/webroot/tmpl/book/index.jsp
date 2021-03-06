<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html;charset=UTF-8"%>

<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">

<link rel="SHORTCUT ICON" href="favicon.ico">

<title>${model.activeSection}</title>

<!-- Bootstrap core CSS -->
 <jsp:include page="${initParam.bootstrap}bootstrap.css.jsp"/>

<script type="text/javascript" src="${initParam.root_context}highlight/highlight.pack.js"></script>
<link href="${initParam.root_context}highlight/styles/1c.css" rel="stylesheet">

<!-- Custom CSS for the '3 Col Portfolio' Template -->
<link href="${initParam.root_book}book.css" rel="stylesheet">

<link rel="SHORTCUT ICON" href="static/book.png">    

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

				<a class="navbar-brand" href="${model.url}">${model.title}</a>
	    						
			</div>

			
			<div class="collapse navbar-collapse navbar-ex1-collapse">
				
				<!-- AUTH -->
				<jsp:include page="/tmpl/auth.jsp"/>

				<!-- SEARCH -->
				<form class="navbar-form navbar-right hidden-print" role="search">
					<div class="form-group">
				    	<input type="hidden" name="book" value="${param.book}"</>
				    	<input type="text" class="form-control" placeholder="Поиск" name="search" value="${param.search}">
				    </div>					    
				</form>
		        
				<!-- LINKS -->
				<ul class="nav navbar-nav">
					<c:forEach var="section" items="${model.sections}">
						<li><a href="#${section.id}">${section.title}</a></li>
					</c:forEach>
				</ul>
			</div> 
			
		</div>
	</nav><!-- /NAV BAR -->


	<!-- BREADCUMPS -->
	<div class="container hidden-print">
		<div class="row">
			<div class="col-lg-12">
				<ol class="breadcrumb">
					<c:forEach var="parent" items="${model.parents}">
	                	<li>
							<a href="${parent.url}">${parent.title}</a>	    					
	                	</li>            		
	            	</c:forEach>
	            	<li class="active">${model.activeSection}</li>
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
						<a href="${section.url}">
							<h2 class="page-header1">${section.title}</h2>
						</a>								
    					

						<!-- UP link-->
						<div class="back-to-top hidden-print">
							<a href="#"><small>Наверх</small></a>

							<c:if test="${not empty section.context }">
								<a href="${section.context}" target="_blank"> 
									<span class="fa fa-pencil"></span> <small>Контекст</small>
								</a>
							</c:if>
                			
						</div>
					</c:if>

					<!-- BLOCK -->
					<c:if test="${not section.group}">

						<!-- UP link-->
						<div class="back-to-top hidden-print">
							<a href="#"><small>Наверх</small></a>

							<c:if test="${not empty section.context }">
								<a href="${section.context}" target="_blank"> 
									<span class="fa fa-pencil"></span> <small>Контекст</small>
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
								
							<div class="col-md-${section.bigImageCSS} hidden-print">
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
								<a id="image${image.id}" rel="group${section.id}" class="small-picture" href="#${section.id}" title="${image.title}"> <img
									class="img-responsive portfolio-item" src="${image.url}">
								</a>
							</c:if>
							<c:if test="${section.bigImageCSS==0}">	
								<a id="image${image.id}" rel="group${section.id}" class="fancy" href="${image.url}&.jpg" title="${image.title}"> <img
									class="img-responsive portfolio-item" src="${image.url}">
								</a>
							</c:if>
							<small>${image.title}</small>
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
