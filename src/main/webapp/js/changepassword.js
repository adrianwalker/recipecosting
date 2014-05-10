$(function() {

  var form = $("#form");

  form.validate({
    rules: {
      password1: {
        required: true,
        minlength: 1,
        maxlength: 1000
      },
      password2: {
        required: true,
        minlength: 1,
        maxlength: 1000,
        equalTo: '#password1'
      }
    }
  });

  $("#changepassword").click(function() {

    if (!form.valid()) {
      return false;
    }

    $.post("rest/user/changepassword", {
      password1: $("#password1").val(),
      password2: $("#password2").val()
    }).done(function() {
      dialog("Change Password", "Password changed");
    }).fail(function(xhr, status, error) {
      dialog("Error", error);
    });
  });
});
