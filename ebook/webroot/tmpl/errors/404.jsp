<%@page contentType="text/html;charset=UTF-8"%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>${applicationScope.brand}</title>

    
    <jsp:include page="${initParam.bootstrap}bootstrap.css.jsp"/>
    <!-- Add custom CSS here -->
    <link href="${initParam.root_404}errors.css" rel="stylesheet">
    
</head>

<body>

    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                 <c:when test="${not empty swtMode}">
                    <a class="navbar-brand" href="#">${applicationScope.brand}</a>
                </c:when>
                <c:otherwise>
                    <a class="navbar-brand" href="${initParam.root_url}">${applicationScope.brand}</a>
                </c:otherwise>
                
            </div>

            <!-- LINKS -->
            <div class="collapse navbar-collapse navbar-ex1-collapse">
                <jsp:include page="/tmpl/auth.jsp" />
            </div>
        </div>
        <!-- /.container -->
    </nav>

    <div class="container">

        <div class="row">

            <div class="col-lg-12">
                <h1 class="page-header">404
                    <small>Page Not Found</small>
                </h1>
                <ol class="breadcrumb">
                    <li><a href="${initParam.root_url}">Домашняя</a>
                    </li>
                    <li class="active">404</li>
                </ol>
            </div>

        </div>

        <div class="row">

            <div class="col-lg-12">
                <p class="error-404">404</p>
                <p class="lead">Запрашиваемая страница не найдена.</p>
            </div>

        </div>

    </div>
    <!-- /.container -->

    <jsp:include page="/tmpl/footer.jsp"/>

    <!-- JavaScript -->
    <jsp:include page="${initParam.bootstrap}bootstrap.js.jsp"/>
    <script src="${initParam.root_404}errors.js"></script>

</body>

</html>
