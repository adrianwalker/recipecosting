function dialog(title, message, messages) {

  $("#title").html(title);

  var html = message;
  if (messages != null && messages.length !== 0) {
    html += "<ul>"
    $.each(messages, function(index, message) {
      html += "<li>" + message + "</li>";
    });
    html += "</ul>"
  }

  $("#text").html(html);

  $('#dialog').modal('show');
}

function error() {
  dialog("Error", "An unexpected error has occurred");
}