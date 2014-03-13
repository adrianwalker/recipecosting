<!DOCTYPE html>
<html>
  <head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script src="js/jquery-1.11.0.min.js"></script>
    <script src="js/jquery.validate.min.js"></script>
    <script src="js/logging.js"></script>
    <script src="js/dialog.js"></script>
    <link rel="stylesheet" href="css/style.css" type="text/css" />
    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <script src="js/bootstrap.min.js"></script>
  </head>
  <body>
    <%@ include file="dialog.jspf" %>
    <div class="container">
      <form id="form1" class="form-signin" role="form">
        <h2 class="form-signin-heading">Login</h2>
        <input id="username" name="username" type="email" class="form-control" placeholder="Username" required autofocus/>
        <input id="password" name="password" type="password" class="form-control" placeholder="Password" required/>
        <input id ="login" type="button" value="Log in" class="btn btn-lg btn-primary btn-block"/>
        <a id="forgotpassword" href="#">Forgotten your password?</a>
      </form>
    </div>
    <div class="container">
      <form id="form2"class="form-signin" role="form">
        <h2 class="form-signin-heading">Register</h2>
        <input id="email" name="email" type="email" class="form-control" placeholder="Username" required autofocus/>
        <input id="password1" name="password1" type="password" class="form-control" placeholder="Password" required/>
        <input id="password2" name="password2" type="password" class="form-control" placeholder="Password Again" required/>
        <input id ="register" type="button" value="Register" class="btn btn-lg btn-primary btn-block"/>
      </form>
    </div>
    <script>
      $(function() {

        $("#login").click(function() {

          var form = $("#form1");

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
                email: true,
                minlength: 1,
                maxlength: 1000
              }
            }
          });

          if (!form.valid()) {
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

          }).fail(function(xhr, status, error) {
            dialog(error);
          });
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

          if (!form.valid()) {
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

          }).fail(function(xhr, status, error) {
            dialog(error);
          });
        });

        $("#forgotpassword").click(function() {

          var form = $("#form1");

          form.validate({
            rules: {
              username: {
                required: true,
                email: true,
                minlength: 1,
                maxlength: 1000
              }
            }
          });

          if (!form.valid()) {
            return false;
          }

          $.post("rest/user/forgotpassword", {
            username: $("#username").val()
          }).done(function(data) {

            var message = data.message;
            if (message) {
              dialog(message);
            }

          }).fail(function(xhr, status, error) {
            dialog(error);
          });

          return false;
        });
      });
    </script>
  </body>
</html>
