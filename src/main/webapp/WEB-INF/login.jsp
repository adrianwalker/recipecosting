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
        <input id="password" type="password"/>
      </div>
      <div>
        <input id ="signin" type="button" value="Sign In"/>
      </div>
    </div>
    <script>
      $(function() {
        $("#signin").click(function() {
          $.get("rest/user/login", {
            username: $("#username").val(),
            password: $("#password").val()
          }).done(function() {
            window.location.replace("recipes.html");
          }).fail(function() {
            error("Invalid username/password");
          });
        });
      });
    </script>
  </body>
</html>
