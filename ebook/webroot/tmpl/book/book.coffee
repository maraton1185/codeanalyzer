$ ->
	#console.log 'hi'
	#$.cookie("tinyEditor", 1);
	$(".fancy").fancybox()

	#click on change block link
	$('.openSection').click (e)->
		e.preventDefault()
		id = $(this).attr('data')
		if id==undefined 
			id = $(this).parents('.container').attr('id')
		changeStatusLine 'event:openSection()='+id
		return

	###click on section
	$('.openSectionBrowse').click (e)->
		e.preventDefault()
		id = $(this).parents('.container').attr('id')
		host = $('body').attr('host');
		window.location.href = host + '&id=' + id
		return
	###
	
	#click on small images cause load it into big image
	$('.small-picture').click (e)->
		#e.preventDefault()
		big = $(this).parents('.container').find('.big-picture')
		small = $(this).find('img')
		src = small.attr 'src'
		title = small.parent().attr 'title'
		
		big.attr 'src', src
		fancy = big.parent()
		fancy.attr 'href', src+'&.jpg'
		fancy.attr 'title', title
		#console.log img, src
		return

	#links in text
	$('.picture-link').click (e)->
		e.preventDefault()
		id = $(this).attr('class').split(' ')[1]
		$('#'+id).trigger('click')
		#alert id
		return

	return

changeStatusLine = (status)->
	window.status = status
	window.status = "done"
	return