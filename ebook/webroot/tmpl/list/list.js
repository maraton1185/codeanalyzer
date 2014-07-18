var changeStatusLine;

$(function() {
  $('#menu-toggle').click(function(e) {
    e.preventDefault();
    $('#wrapper').toggleClass('active');
    alert("hi");
  });
});

changeStatusLine = function(status) {
  window.status = status;
  window.status = "done";
};
