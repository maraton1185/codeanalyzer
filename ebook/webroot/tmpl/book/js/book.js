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
    var img, src;
    img = $(this).parents('.container').find('.big-picture');
    src = $(this).find('img').attr('src');
    img.attr('src', src);
  });
});

changeStatusLine = function(status) {
  window.status = status;
  window.status = "done";
};
