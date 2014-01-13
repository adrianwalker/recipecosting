<!DOCTYPE html>
<html>
  <head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
    <script src="js/logging.js"></script>
    <script src="js/error.js"></script>
  </head>
  <body>
    <div>
      <div>        
        <input id="username" type="text"/>
      </div>
      <div>
        <input id="password1" type="password"/>
      </div>
      <div>
        <input id="password2" type="password"/>
      </div>
      <div>
        <input id ="register" type="button" value="Register"/>
      </div>
    </div>
    <script>
      $(function() {
        $("#register").click(function() {
          $.get("rest/user/register", {
            username: $("#username").val(),
            password1: $("#password1").val(),
            password2: $("#password2").val()
          }).done(function() {
            window.location.replace("recipes.html");
          }).fail(function() {
            error();
          });
        });
      });
    </script>
  </body>
</html>
