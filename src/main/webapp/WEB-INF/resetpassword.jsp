<!DOCTYPE html>
<html>
  <head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
    <script src="js/logging.js"></script>
    <script src="js/error.js"></script>
    <script src="js/url.js"></script>
  </head>
  <body>
    <div>
      <div>
        <label for="password1">Password:</label>
        <input id="password1" type="password"/>
      </div>
      <div>
        <label for="password2">Password Again:</label>
        <input id="password2" type="password"/>
      </div>
      <div>
        <input id="resetpassword" type="button" value="Reset Password"/>
      </div>
    </div>
    <script>
      $("#resetpassword").click(function() {

        var params = getUrlParams();
        if (params.uuid !== undefined) {
          $.post("rest/user/resetpassword", {
            uuid: params.uuid,
            password1: $("#password1").val(),
            password2: $("#password2").val()
          }).done(function() {
            window.location.replace("login.html");
          }).fail(function() {
            error();
          });
        }
      });
    </script>
  </body>
</html>
