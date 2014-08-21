<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html;charset=UTF-8"%>

<!doctype html>
<!-- The DOCTYPE declaration above will set the     -->
<!-- browser's rendering engine into                -->
<!-- "Standards Mode". Replacing this declaration   -->
<!-- with a "Quirks Mode" doctype is not supported. -->

<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">

<link rel="SHORTCUT ICON" href="favicon.ico">

<!-- Bootstrap core CSS -->
<jsp:include page="${initParam.bootstrap}bootstrap.css.jsp" />

<!-- Custom CSS for the '3 Col Portfolio' Template -->
<link href="${initParam.root_context}context.css" rel="stylesheet">

<!--                                           -->
<!-- Any title is fine                         -->
<!--                                           -->
<title>Web Application Starter Project</title>

<!--                                           -->
<!-- This script loads your compiled module.   -->
<!-- If you add any GWT meta tags, they must   -->
<!-- be added before this line.                -->
<!--                                           -->
<script type="text/javascript" language="javascript"
	src="context/context.nocache.js"></script>
</head>
<script type="text/javascript" src="${initParam.root_editor}js/tinymce/tinymce.min.js"></script>


<!--                                           -->
<!-- The body can have arbitrary html, or      -->
<!-- you can leave the body empty if you want  -->
<!-- to create a completely dynamic UI.        -->
<!--                                           -->
<body>

	<!--NAV BAR-->
	<nav class="navbar navbar-inverse navbar-static-top" role="navigation">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-ex1-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="${initParam.root_url}">${applicationScope.brand}</a>
			</div>

			<jsp:include page="/tmpl/auth.jsp" />

		</div>
		<!-- /.container -->
	</nav>

	<!-- OPTIONAL: include this if you want history support -->
	<iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1'
		style="position: absolute; width: 0; height: 0; border: 0"></iframe>

	<!-- RECOMMENDED if your web app will not function without JavaScript enabled -->
	<noscript>
		<div
			style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
			Your web browser must have JavaScript enabled in order for this
			application to display correctly.</div>
	</noscript>

	<div class="container">
		<div id="root"></div>
	</div>
	<!-- /container -->

	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->

	<jsp:include page="${initParam.bootstrap}bootstrap.js.jsp" />

	<script src="${initParam.root_context}context.js"></script>
</body>
</html>
