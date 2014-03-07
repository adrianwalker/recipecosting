function dialog(message, messages) {

  var overlay = $("#overlay");

  var html = $("<p>" + message + "</p>");
  if (messages && messages.length > 0) {
    html.append("<ul>");
    $(messages).each(function(index, value) {
      html.append("<li>" + value + "</li>");
    });
    html.append("</ul>");
  }

  $("#text").html(html);

  $("#ok").click(function() {
    overlay.hide();
  });

  overlay.show();
}

function error() {
  dialog("An unexpected error has occurred");
}