<!DOCTYPE html>
<html>
  <head>
    <title></title>
    <%@ include file="head.jspf" %>
  </head>
  <body>
    <%@ include file="dialog.jspf" %>
    <%@ include file="loginmenu.jspf" %>
    <div class="container">
      <form id="form1" name="form1" class="form-signin" role="form">
        <h2>Log In</h2>
        <input id="username" name="username" class="form-control" type="email" placeholder="Username" required autofocus/>
        <input id="password" name="password" class="form-control" type="password" placeholder="Password" required/>
        <br/>
        <button id="login" type="submit" class="btn btn-primary btn-block">Log in</button>
        <button id="forgotpassword" type="submit" class="btn btn-link">Forgotten your password?</button>
      </form>
    </div>
    <div class="container">
      <form id="form2" name="form2" class="form-signin" role="form">
        <h2>Register - It's Free!</h2>
        <input id="email" name="email" class="form-control" type="email" placeholder="Email Address" required />
        <input id="password1" class="form-control" name="password1" type="password" placeholder="Password" required/>
        <input id="password2" class="form-control" name="password2" type="password" placeholder="Password Again" required/>
        <br/>
        <button id ="register" type="submit" class="btn btn-primary btn-block">Register</button>
      </form>
    </div>
    <script>
      function login() {
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
      }

      function register() {
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
      }

      function forgotPassword() {
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
      }

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
                minlength: 1,
                maxlength: 1000
              }
            }, submitHandler: function(form) {
              login();
            }
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
            },
            submitHandler: function(form) {
              register();
            }
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
              },
              password: {
                required: false,
                minlength: 1,
                maxlength: 1000
              }
            },
            submitHandler: function(form) {
              login();
            }
          });
        });
      });
    </script>
  </body>
</html>
