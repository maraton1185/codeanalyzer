var changeStatusLine, getContent, setContent;

changeStatusLine = function(status) {
  window.status = status;
  window.status = "done";
};

setContent = function(text) {
  var editor;
  editor = $("#editor").cleditor()[0];
  editor.$area.val(text);
  editor.updateFrame();
};

window.onerror = function(msg, url, linenumber) {
  alert('Error message: ' + msg + '\nURL: ' + url + '\nLine Number: ' + linenumber);
  return true;
};

getContent = function() {
  var editor;
  editor = $("#editor").cleditor()[0];
  return editor.$area.val(text);
};

$(function() {
  var editor;
  editor = $("#editor").cleditor();
  changeStatusLine("editor:onInit()");
  editor.change(function() {
    return changeStatusLine("editor:onChange()");
  });
});


/*
tinymce.init
	language : 'ru'
	setup: "s"
	selector: "textarea"
	plugins: ["advlist autolink lists charmap print preview anchor",
	"searchreplace visualblocks code fullscreen",
	"insertdatetime table contextmenu paste",
	"textcolor","autoresize"]
	paste_auto_cleanup_on_paste : true
	paste_remove_styles: true
	paste_remove_styles_if_webkit: true
	paste_strip_class_attributes: true
	content_css: "/tmpl/context/highlight/styles/1c.css"
	toolbar: "insertfile undo redo | styleselect | bold italic | alignleft
	 aligncenter alignright alignjustify | bullist numlist outdent indent | forecolor backcolor | 1code code"
	formats:
		code_format: {block : 'code', wrapper: 'pre'}
		pre_format: {block : 'pre'}
	setup: (editor)->
		editor.addButton '1code', {
				type: 'button'
				text: '1c'
				icon: false
				onclick: ()->
					editor.formatter.apply('pre_format')
					editor.formatter.apply('code_format')
					return
			}	
		editor.on 'init', (e)->
			changeStatusLine('tinymce:onInit()')
			tinymce.activeEditor.execCommand('mceFullScreen')
			return
		editor.on 'change', (e)->
			changeStatusLine('tinymce:onChange()')
			return

		return
 */
