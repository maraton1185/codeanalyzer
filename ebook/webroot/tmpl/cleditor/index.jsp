<%@page contentType="text/html;charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr">
<head>
<title>StyledEditor</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8"/>


 <link rel="stylesheet" type="text/css" href="${initParam.root_editor}js/jquery.cleditor.css" />

</head>
<body>

	<textarea cols="80" id="editor" name="editor" rows="10">
	</textarea>


   <!--textarea id="content" name="content" style="width:100%; height:100% !important"></textarea-->
	<!-- <div id="set">
	set content
	</div> -->
    <script src="${initParam.bootstrap}jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="${initParam.root_editor}js/jquery.cleditor.min.js"></script>
    
    
    <script src="${initParam.root_editor}editor.js"></script>
</body>
</html>



