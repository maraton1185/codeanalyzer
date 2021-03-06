<%@page contentType="text/html;charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr">
<head>
<title>StyledEditor</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8"/>

<script type="text/javascript" src="${initParam.root_editor}js/tinymce/tinymce.min.js"></script>



<!--script>
tinymce.init({
	language : 'ru',
    selector: "textarea",
//     mode : "exact",
//     elements : "content",
//     width: '100%',
//     height: 400,
//     autoresize_min_height: 400,
//     autoresize_max_height: 800,
	//inline: true,
	
    plugins: [
        "advlist autolink lists charmap print preview anchor",
        "searchreplace visualblocks code fullscreen",
        "insertdatetime table contextmenu paste",
		"textcolor","autoresize"
    ],
    toolbar: "insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | forecolor backcolor",
	 //menubar: false,
	setup: function(editor) {
        editor.on('init', function(e) {
            changeStatusLine('tinymce:onInit()');
			tinymce.activeEditor.execCommand('mceFullScreen');
        });
		/*editor.on('ExecCommand', function(e) {
            console.log('ExecCommand event', e);
        });*/
		editor.on('change', function(e) {
            changeStatusLine('tinymce:onChange()');
        });
    }
});

	function setContent(text) {
        tinymce.activeEditor.setContent(text);
    };
	
	function getContent() {
        return tinyMCE.activeEditor.getContent();
    };
	
	function changeStatusLine(status){
		window.status = status;
		window.status = "done";
	}
	
/*$(document).ready(function(){

	$("#set").click(function(){
		//alert("The paragraph was clicked.");
		//tinymce.get('content').setContent('<span>some</span> html');
		//setContent('<span>some</span> html');
		changeStatusLine('fromWebApp:handleInit()');
	});
	
});*/
</script-->

</head>
<body>

   <textarea id="content" name="content" style="width:100%; height:100% !important"></textarea>
	<!-- <div id="set">
	set content
	</div> -->
    <script src="${initParam.bootstrap}jquery-1.11.1.min.js"></script>
    <script src="${initParam.root_editor}editor.js"></script>
</body>
</html>



