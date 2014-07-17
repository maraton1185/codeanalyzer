
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
    <link href="${root}css/bootstrap.css" rel="stylesheet">

    <!-- Custom CSS for the 'Round About' Template -->
    <link href="${root}css/list.css" rel="stylesheet">

</head>

<body>

    <!-- NAV BAR -->
    <nav class="navbar navbar-static-top navbar-inverse" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="${model.url}">${model.brand}</a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse navbar-ex1-collapse">
                <ul class="nav navbar-nav">
                    <li><a href="${model.aboutUrl}">Как пользоваться</a>
                    </li>
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container -->
    </nav><!-- /NAV BAR -->


    <!-- BREADCUMPS -->
    <div class="container">
        <div class="row">
            <div class="col-lg-12">
                <ol class="breadcrumb">
                    <c:forEach var="parent" items="${model.parents}">
                        <li>
                            <a href="${parent.url}">${parent.title}</a>                            
                        </li>                   
                    </c:forEach>
                    <li class="active">${model.title}</li>
                </ol>
            </div>
        </div>
    </div>
    
    <!-- BOOKS -->
    <div class="container">

        <div class="row">
            
            <c:forEach var="book" items="${model.books}">
                <div class="col-lg-4 col-sm-6">
                    <a href="${book.url}">
                    <c:if test="${book.hasImage}">                 
                        <!--img class="img-circle img-responsive" src="${book.image}"-->
                        <img class="img-responsive" src="${book.image}">
                    </c:if>
                    <h3>${book.title}</h3>
                    </a>
                    <p>${book.description}</p>

                </div>
            </c:forEach>
         
        </div>

    </div>
    <!-- /container -->

    <c:import url="/tmpl/footer.jsp"/>

    <!-- JavaScript -->
    <script src="${root}js/jquery-1.10.2.js"></script>
    <script src="${root}js/bootstrap.js"></script>

</body>

</html>
