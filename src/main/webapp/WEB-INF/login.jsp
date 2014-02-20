<!DOCTYPE html>
<html>
  <head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script src="js/jquery-1.11.0.min.js"></script>
    <script src="js/jquery.validate.min.js"></script>
    <script src="js/logging.js"></script>
    <script src="js/error.js"></script>
    <script src="js/dialog.js"></script>
    <link rel="stylesheet" href="css/style.css" type="text/css" />
  </head>
  <body>
    <div>
      <%@ include file="dialog.jspf" %>
      <h2>Login</h2>
      <form id="form1">
        <div>
          <label for="username">Username:</label>
          <input id="username" name="username" type="text"/>
        </div>
        <div>
          <label for="password">Password:</label>
          <input id="password" name="password" type="password"/>
        </div>
        <div>
          <input id ="login" type="button" value="Log in"/>
          <a id="forgotpassword" href="#">Forgotten your password?</a>
        </div>
      </form>
    </div>
    <div>
      <h2>Register</h2>
      <form id="form2">
        <div>
          <label for="email">Email:</label>
          <input id="email" name="email" type="text"/>
        </div>
        <div>
          <label for="password1">Password:</label>
          <input id="password1" name="password1" type="password"/>
        </div>
        <div>
          <label for="password2">Password Again:</label>
          <input id="password2" name="password2" type="password"/>
        </div>
        <div>
          <input id ="register" type="button" value="Register"/>
        </div>
      </form>
    </div>
    <script>
      $(function() {

        $("#form1").validate({
          rules: {
            username: {
              required: true,
              email: true,
              minlength: 1,
              maxlength: 1000
            },
            password: {
              required: true,
              email: true,
              minlength: 1,
              maxlength: 1000
            }
          }
        });

        $("#form2").validate({
          rules: {
            email: {
              required: true,
              email: true,
              minlength: 1,
              maxlength: 1000
            },
            password1: {
              required: true,
              email: true,
              minlength: 1,
              maxlength: 1000
            },
            password2: {
              required: true,
              email: true,
              minlength: 1,
              maxlength: 1000
            }
          }
        });

        $("#login").click(function() {

          if (!$("#form1").valid()) {
            return false;
          }

          $.post("rest/user/login", {
            username: $("#username").val(),
            password: $("#password").val()
          }).done(function(data) {

            var message = data.message;
            if (message) {
              dialog(message);
            } else {
              window.location.replace("recipes.html");
            }

          }).fail(function() {
            error();
          });
        });

        $("#register").click(function() {

          if (!$("#form2").valid()) {
            return false;
          }

          $.post("rest/user/register", {
            email: $("#email").val(),
            password1: $("#password1").val(),
            password2: $("#password2").val()
          }).done(function(data) {

            var message = data.message;
            if (message) {
              dialog(message);
            }

          }).fail(function() {
            error();
          });
        });

        $("#forgotpassword").click(function() {

          $.post("rest/user/forgotpassword", {
            username: $("#username").val()
          }).done(function(data) {

            var message = data.message;
            if (message) {
              dialog(message);
            }

          }).fail(function() {
            error();
          });

          return false;
        });
      });
    </script>
  </body>
</html>
