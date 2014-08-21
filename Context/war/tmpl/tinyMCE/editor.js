var changeStatusLine, getContent, setContent;

changeStatusLine = function(status) {
  window.status = status;
  window.status = "done";
};

setContent = function(text) {
  tinymce.activeEditor.setContent(text);
};

getContent = function() {
  return tinyMCE.activeEditor.getContent();
};

tinymce.init({
  language: 'ru',
  setup: "s",
  selector: "textarea",
  plugins: ["advlist autolink lists charmap print preview anchor", "searchreplace visualblocks code fullscreen", "insertdatetime table contextmenu paste", "textcolor", "autoresize"],
  paste_auto_cleanup_on_paste: true,
  paste_remove_styles: true,
  paste_remove_styles_if_webkit: true,
  paste_strip_class_attributes: true,
  toolbar: "insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | forecolor backcolor",
  setup: function(editor) {
    editor.on('init', function(e) {
      changeStatusLine('tinymce:onInit()');
      tinymce.activeEditor.execCommand('mceFullScreen');
    });
    editor.on('change', function(e) {
      changeStatusLine('tinymce:onChange()');
    });
  }
});
