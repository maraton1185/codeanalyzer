$ ->

 	#Custom JavaScript for the Menu Toggle
	$('#menu-toggle').click (e)->
		e.preventDefault()
		$('#wrapper').toggleClass 'active'
		return

	return

changeStatusLine = (status)->
	window.status = status
	window.status = "done"
	return