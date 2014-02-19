<!DOCTYPE html>
<html>
  <head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
    <script src="js/logging.js"></script>
    <script src="js/error.js"></script>
    <script src="js/dialog.js"></script>
    <link rel="stylesheet" href="css/style.css" type="text/css" />
  </head>
  <body>
    <div>
      <%@ include file="dialog.jspf" %>
      <h2>Login</h2>
      <div>
        <label for="username">Username:</label>
        <input id="username" type="text"/>
      </div>
      <div>
        <label for="password">Password:</label>
        <input id="password" type="password"/>
      </div>
      <div>
        <input id ="login" type="button" value="Log in"/>
        <a id="forgotpassword" href="#">Forgotten your password?</a>
      </div>
    </div>
    <div>
      <h2>Register</h2>
      <div>
        <label for="email">Email:</label>
        <input id="email" type="text"/>
      </div>
      <div>
        <label for="password1">Password:</label>
        <input id="password1" type="password"/>
      </div>
      <div>
        <label for="password2">Password Again:</label>
        <input id="password2" type="password"/>
      </div>
      <div>
        <input id ="register" type="button" value="Register"/>
      </div>
    </div>
    <script>
      $(function() {
        $("#login").click(function() {

          var username = $("#username").val();
          var password = $("#password").val();

          if (!username) {
            dialog("Enter your username");
            return false;
          }

          if (!password) {
            dialog("Enter your password");
            return false;
          }

          $.post("rest/user/login", {
            username: username,
            password: password
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
          var email = $("#email").val();
          var password1 = $("#password1").val();
          var password2 = $("#password2").val();

          if (!email) {
            dialog("Enter an email address");
            return false;
          }

          if (!password1) {
            dialog("Enter a password");
            return false;
          }

          if (!password2) {
            dialog("Enter a password again");
            return false;
          }

          if (password1 !== password2) {
            dialog("Passwords do not match");
            return false;
          }


          $.post("rest/user/register", {
            email: email,
            password1: password1,
            password2: password2
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
          var username = $("#username").val();

          if (!username) {
            dialog("Enter your username");
            return false;
          }

          $.post("rest/user/forgotpassword", {
            username: username
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
