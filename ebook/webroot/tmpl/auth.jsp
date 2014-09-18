<%@page contentType="text/html;charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- <div class="collapse navbar-collapse navbar-ex1-collapse"> -->


<ul class="nav navbar-nav navbar-right navbar-user">
	
	<c:choose>
		<c:when test="${empty sessionScope.user}">
			<li><a href="${initParam.login_url}"><i class="fa fa-user"></i> Войти</a></li>    	
	    </c:when>
	    <c:otherwise>
	    	<li class="dropdown user-dropdown">
				<a href="#" class="dropdown-toggle" data-toggle="dropdown">
					<i class="fa fa-user"></i> ${sessionScope.user.title} <b class="caret"></b>
				</a>
				<ul class="dropdown-menu">
				<!-- 	<li><a href="#"><i class="fa fa-user"></i> Profile</a></li>
					<li><a href="#"><i class="fa fa-envelope"></i> Inbox <span
							class="badge">7</span></a></li>
					<li><a href="#"><i class="fa fa-gear"></i> Settings</a></li>
					<li class="divider"></li> -->
					<li><a href="${initParam.logout_url}"><i class="fa fa-power-off"></i> Выйти</a></li>
				</ul>
			</li>
	    </c:otherwise>
	</c:choose>
	
			
</ul>

<!-- </div> -->