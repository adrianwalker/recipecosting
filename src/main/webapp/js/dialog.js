function dialog(message, messages) {

  var overlay = $("#overlay");
  var html = message;
  $(messages).each(function(index, value) {
    html = html + "<br>" + value;
  });

  $("#text").html(html);

  $("#ok").click(function() {
    overlay.hide();
  });

  overlay.show();
}

function error() {
  dialog("An unexpected error has occurred");
}