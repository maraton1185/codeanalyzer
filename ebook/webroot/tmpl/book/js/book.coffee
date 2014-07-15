$ ->
	#console.log 'hi'
	#$.cookie("tinyEditor", 1);

	#click on change block link
	$('.change-block').click (e)->
		id = $(this).parents('.container').attr('id')
		changeStatusLine 'event:changeBlock()='+id
		return

	#click on small images cause load it into big image
	$('.small-picture').click (e)->
		#e.preventDefault()
		img = $(this).parents('.container').find('.big-picture')
		src = $(this).find('img').attr 'src'
		img.attr 'src', src
		#console.log img, src
		return

	return

changeStatusLine = (status)->
	window.status = status
	window.status = "done"
	return