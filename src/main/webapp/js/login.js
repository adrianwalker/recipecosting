function login() {
  $.post("rest/user/login", {
    username: $("#username").val(),
    password: $("#password").val()
  }).done(function(data) {

    var message = data.message;
    if (message) {
      dialog("Login", message);
    } else {
      window.location.replace("recipes.html");
    }

  }).fail(function(xhr, status, error) {
    dialog("Error", error);
  });
}

function register() {
  $.post("rest/user/register", {
    email: $("#email").val(),
    password1: $("#password1").val(),
    password2: $("#password2").val()
  }).done(function(data) {

    var message = data.message;
    if (message) {
      dialog("Login", message);
    }

  }).fail(function(xhr, status, error) {
    dialog("Error", error);
  });
}

function forgotPassword() {
  $.post("rest/user/forgotpassword", {
    username: $("#username").val()
  }).done(function(data) {

    var message = data.message;
    if (message) {
      dialog("Login", message);
    }

  }).fail(function(xhr, status, error) {
    dialog("Error", error);
  });
}

$(function() {

  $("#login").click(function() {

    var form = $("#form1");

    form.data("validator", false);
    form.validate({
      rules: {
        username: {
          required: true,
          email: true,
          minlength: 1,
          maxlength: 1000
        },
        password: {
          required: true,
          minlength: 1,
          maxlength: 1000
        }
      }
    });

    if (form.valid()) {
      login();
    }
  });

  $("#register").click(function() {

    var form = $("#form2");

    form.validate({
      rules: {
        email: {
          required: true,
          email: true,
          minlength: 1,
          maxlength: 1000
        },
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

    if (form.valid()) {
      register();
    }
  });

  $("#forgotpassword").click(function() {

    var form = $("#form1");

    form.data("validator", false);
    form.validate({
      rules: {
        username: {
          required: true,
          email: true,
          minlength: 1,
          maxlength: 1000
        },
        password: {
          required: false,
          minlength: 1,
          maxlength: 1000
        }
      }
    });

    if (form.valid()) {
      forgotPassword();
    }
  });
});
