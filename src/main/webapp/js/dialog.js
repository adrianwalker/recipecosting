function dialog(title, message) {

  $("#title").html(title);
  $("#text").html(message);
  $('#dialog').modal('show');
}

function error() {
  dialog("Error", "An unexpected error has occurred");
}