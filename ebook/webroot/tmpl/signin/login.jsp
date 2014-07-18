<%@page contentType="text/html;charset=UTF-8"%>

<!DOCTYPE html>
<!-- saved from url=(0040)http://getbootstrap.com/examples/signin/ -->
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<link rel="icon" href="http://getbootstrap.com/favicon.ico">

<title>${applicationScope.brand}</title>

<!-- Bootstrap core CSS -->
<jsp:include page="${initParam.bootstrap}bootstrap.css.jsp" />

<!-- Custom styles for this template -->
<link href="${initParam.root_sign}login.css" rel="stylesheet">


<!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
<!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
<script src="${initParam.root_sign}js/ie-emulation-modes-warning.js"></script>
<style></style>
<style type="text/css"></style>

<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<script src="${initParam.root_sign}js/ie10-viewport-bug-workaround.js"></script>

<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>

	<!--NAV BAR-->
	<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
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

	<div class="container">

		<form class="form-signin" role="form" action="login" method="POST">
			<h2 class="form-signin-heading">Вход</h2>
			<input name="username" type="text" class="form-control" placeholder="Имя" required=""
				autofocus=""> 
      <input name="password" type="password" class="form-control"
				placeholder="Пароль" required="">
			<div class="checkbox">
				<label> 
          <input name="remember-me" type="checkbox" value="remember-me">
					Запомнить меня
				</label>
			</div>
			<button class="btn btn-lg btn-primary btn-block" type="submit">Войти</button>
		</form>

	</div>
	<!-- /container -->


	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->

	<jsp:include page="/tmpl/footer.jsp" />

	<jsp:include page="${initParam.bootstrap}bootstrap.js.jsp" />

</body>
</html>