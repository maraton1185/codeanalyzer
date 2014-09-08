var changeStatusLine, getContent, setContent;

changeStatusLine = function(status) {
  window.status = status;
  window.status = "done";
};

setContent = function(text) {
  var oEditor;
  oEditor = CKEDITOR.instances.editor1;
  oEditor.insertHtml(text);
};

window.onerror = function(msg, url, linenumber) {
  alert('Error message: ' + msg + '\nURL: ' + url + '\nLine Number: ' + linenumber);
  return true;
};

getContent = function() {
  return CKEDITOR.instances.editor1.getData();
};

CKEDITOR.on({
  'instanceCreated': function(e) {
    e.editor.on('change', function() {
      changeStatusLine("editor:onChange()");
    });
  }
});

CKEDITOR.replace('editor1', {
  extraPlugins: 'autogrow,onchange',
  removePlugins: 'resize',
  toolbar: 'MyToolbar',
  on: {
    'instanceReady': function(ev) {
      changeStatusLine("editor:onInit()");
    },
    'key': function(ev) {
      changeStatusLine("editor:onChange()");
    }
  }
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
