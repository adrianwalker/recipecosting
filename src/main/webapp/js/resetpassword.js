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

  $("#resetpassword").click(function() {

    if (!form.valid()) {
      return false;
    }

    var params = getUrlParams();
    if (params.uuid !== undefined) {
      $.post("rest/user/resetpassword", {
        uuid: params.uuid,
        password1: $("#password1").val(),
        password2: $("#password2").val()
      }).done(function() {
        window.location.replace("login.html");
      }).fail(function(xhr, status, error) {
        dialog("Error", error);
      });
    }
  });
});
