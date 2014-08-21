
document.setContent = (text) ->
	tinymce.activeEditor.setContent(text)
	return
    
	
document.getContent = ()->
	tinyMCE.activeEditor.getContent()
    
document.init = () ->
	tinymce.init
		language : 'ru'
		setup: "s"
		selector: "textarea"
		plugins: ["advlist autolink lists charmap print preview anchor",
		"searchreplace visualblocks code",
		"insertdatetime table contextmenu paste",
		"textcolor","autoresize"]
		paste_auto_cleanup_on_paste : true
		paste_remove_styles: true
		paste_remove_styles_if_webkit: true
		paste_strip_class_attributes: true
		toolbar: "insertfile undo redo | styleselect | bold italic | alignleft
		 aligncenter alignright alignjustify | bullist numlist outdent indent | forecolor backcolor"
		setup: (editor)->
			return  
	return

    	