
document.setContent = (text) ->
	tinymce.activeEditor.setContent(text)
	$('pre code', window.parent.frames[2].document).each (i, block)->
    	hljs.highlightBlock block
    	return
  
	return
    
	
document.getContent = ()->
	tinyMCE.activeEditor.getContent()
    
document.init = () ->
	tinymce.init
		language: 'ru'
		setup: "s"
		selector: "textarea"
		readonly: 1
		plugins: ["advlist autolink lists charmap print preview anchor",
		"searchreplace visualblocks code",
		"insertdatetime table contextmenu paste",
		"textcolor","autoresize"]		
		toolbar: ""
		toolbar: "false"
		menubar: "false"
		content_css: "/tmpl/context/highlight/styles/1c.css"
		setup: (editor)->
			return  
	return

    	