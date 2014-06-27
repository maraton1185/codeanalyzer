﻿<%! 
    String root = "rinfo/"; 
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Привет медвед!</title>

    <!-- Bootstrap Core CSS-->
    <link href="<%= root%>css/bootstrap.min.css" rel="stylesheet" type="text/css">
 
    <!-- Custom CSS -->
    <link href="<%= root%>css/scrolling-nav.css" rel="stylesheet" type="text/css">
	
</head>

<!-- The #page-top ID is part of the scrolling feature - the data-spy and data-target are part of the built-in Bootstrap scrollspy function -->
<body id="page-top" data-spy="scroll" data-target=".navbar-fixed-top">

    <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
        <div class="container">
            <div class="navbar-header page-scroll">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#page-top">Start Bootstrap</a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse navbar-ex1-collapse">
                <ul class="nav navbar-nav">
                    <!-- Hidden li included to remove active class from about link when scrolled up past about section -->
                    <li class="hidden">
                        <a href="#page-top"></a>
                    </li>
                    <li class="page-scroll">
                        <a href="#about">About</a>
                    </li>
                    <li class="page-scroll">
                        <a href="#services">Services</a>
                    </li>
                    <li class="page-scroll">
                        <a href="#contact">Contact</a>
                    </li>
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container -->
    </nav>

    <section id="intro" class="intro-section">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <h1>Scrolling Nav</h1>
                    <div class="page-scroll">
                        <a class="btn btn-default" href="#about">Click me!</a>
                    </div>
                    <br>
                    <p>This demo is the same sort of scrolling navigation menu we use in our <a href="http://startbootstrap.com/grayscale.php">Grayscale theme</a>. Whenever you want to use the page scrolling feature, make sure the link points to an ID (<code>#about</code> for example), and that the parent of the link has the class <code>.page-scroll</code>. See how the button above and the menu bar links are formatted for a working example.</p>
                </div>
            </div>
        </div>
    </section>

    <section id="about" class="about-section">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <h1>About Section</h1>
                </div>
            </div>
        </div>
    </section>

    <section id="services" class="services-section">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <h1>Services Section</h1>
                </div>
            </div>
        </div>
    </section>

    <section id="contact" class="contact-section">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <h1>Contact Section</h1>
                </div>
            </div>
        </div>
    </section>

    <!-- Core JavaScript Files -->
    <script src="<%= root%>js/jquery-1.10.2.js"></script>
    <script src="<%= root%>js/bootstrap.min.js"></script>
    <script src="<%= root%>js/jquery.easing.min.js"></script>

    <!-- Custom Theme JavaScript -->
    <script src="<%= root%>js/scrolling-nav.js"></script>
 	
</body>

</html>
