document.setContent = function(text) {
  tinymce.activeEditor.setContent(text);
  $('pre code', window.parent.frames[2].document).each(function(i, block) {
    hljs.highlightBlock(block);
  });
};

document.getContent = function() {
  return tinyMCE.activeEditor.getContent();
};

document.init = function() {
  tinymce.init({
    language: 'ru',
    setup: "s",
    selector: "textarea",
    readonly: 1,
    plugins: ["advlist autolink lists charmap print preview anchor", "searchreplace visualblocks code", "insertdatetime table contextmenu paste", "textcolor", "autoresize"],
    toolbar: "",
    toolbar: "false",
    menubar: "false",
    content_css: "/tmpl/context/highlight/styles/1c.css",
    setup: function(editor) {}
  });
};
