$ ->
	#console.log 'hi'
	$.cookie("tinyEditor", 1);
	$('.small-picture').click (e)->
		#e.preventDefault()
		img = $('.small-picture').parents('.container').find('.big-picture')
		src = $(this).find('img').attr 'src'
		img.attr 'src', src
		#console.log img, src
		return
	return