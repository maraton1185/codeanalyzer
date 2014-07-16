$ ->
	#console.log 'hi'
	#$.cookie("tinyEditor", 1);
	$(".fancy").fancybox()

	#click on change block link
	$('.openSection').click (e)->
		e.preventDefault()
		id = $(this).parents('.container').attr('id')
		changeStatusLine 'event:openSection()='+id
		return

	#click on section
	$('.openSectionBrowse').click (e)->
		e.preventDefault()
		id = $(this).parents('.container').attr('id')
		host = $('body').attr('host');
		window.location.href = host + '&id=' + id
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