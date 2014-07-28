
changeStatusLine = (status)->
	window.status = status
	window.status = "done"
	return

setContent = (text) ->
	tinymce.activeEditor.setContent(text)
	return
    
	
getContent = ()->
	tinyMCE.activeEditor.getContent()
    

tinymce.init
	language : 'ru'
	setup: "s"
	selector: "textarea"
	plugins: ["advlist autolink lists charmap print preview anchor",
	"searchreplace visualblocks code fullscreen",
	"insertdatetime table contextmenu paste",
	"textcolor","autoresize"]
	toolbar: "insertfile undo redo | styleselect | bold italic | alignleft
	 aligncenter alignright alignjustify | bullist numlist outdent indent | forecolor backcolor"
	setup: (editor)->
		editor.on 'init', (e)->
			changeStatusLine('tinymce:onInit()')
			tinymce.activeEditor.execCommand('mceFullScreen')
			return
		editor.on 'change', (e)->
			changeStatusLine('tinymce:onChange()')
			return

		return  

    	