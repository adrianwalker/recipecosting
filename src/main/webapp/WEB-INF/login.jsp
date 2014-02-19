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
          $.post("rest/user/login", {
            username: $("#username").val(),
            password: $("#password").val()
          }).done(function() {
            window.location.replace("recipes.html");
          }).fail(function() {
            dialog("Invalid email/password");
          });
        });

        $("#register").click(function() {
          $.post("rest/user/register", {
            email: $("#email").val(),
            password1: $("#password1").val(),
            password2: $("#password2").val()
          }).done(function() {
            dialog("Check your email to enable your account");
          }).fail(function() {
            error();
          });
        });

        $("#forgotpassword").click(function() {
          var username = $("#username").val();

          if (username) {
            $.post("rest/user/forgotpassword", {
              username: username
            }).done(function() {
              dialog("Check your email to login to your account");
            }).fail(function() {
              dialog("Invalid username");
            });
          } else {
            dialog("Enter your username");
          }

          return false;
        });
      });
    </script>
  </body>
</html>
