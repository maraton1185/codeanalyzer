var changeStatusLine;

$(function() {
  $(".fancy").fancybox();
  $('.openSection').click(function(e) {
    var id;
    e.preventDefault();
    id = $(this).attr('data');
    if (id === void 0) {
      id = $(this).parents('.container').attr('id');
    }
    changeStatusLine('event:openSection()=' + id);
  });

  /*click on section
  	$('.openSectionBrowse').click (e)->
  		e.preventDefault()
  		id = $(this).parents('.container').attr('id')
  		host = $('body').attr('host');
  		window.location.href = host + '&id=' + id
  		return
   */
  $('.small-picture').click(function(e) {
    var big, fancy, small, src, title;
    big = $(this).parents('.container').find('.big-picture');
    small = $(this).find('img');
    src = small.attr('src');
    title = small.parent().attr('title');
    big.attr('src', src);
    fancy = big.parent();
    fancy.attr('href', src + '&.jpg');
    fancy.attr('title', title);
  });
  $('.picture-link').click(function(e) {
    var id;
    e.preventDefault();
    id = $(this).attr('class').split(' ')[1];
    $('#' + id).trigger('click');
  });
});

changeStatusLine = function(status) {
  window.status = status;
  window.status = "done";
};
