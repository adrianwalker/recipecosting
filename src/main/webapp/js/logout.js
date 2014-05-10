$(function() {

  $.get("rest/user/logout", {
  }).done(function() {
    window.location.replace("login.html");
  }).fail(function(xhr, status, error) {
    dialog("Error", error);
  });
});
