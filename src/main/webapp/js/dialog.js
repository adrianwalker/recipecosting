function dialog(message) {
  var overlay = $("#overlay");

  $("#message").text(message);

  $("#ok").click(function() {
    overlay.hide();
  });

  overlay.show();
}

function error() {
  dialog("An unexpected error has occurred");
}