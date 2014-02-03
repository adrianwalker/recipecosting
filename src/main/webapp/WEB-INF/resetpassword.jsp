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
        <input id="password1" type="password"/>
      </div>
      <div>
        <input id="password2" type="password"/>
      </div>
      <div>
        <input id="reset" type="button" value="Reset"/>
      </div>
    </div>
    <script>
      $(function() {


        var params = getUrlParams();
        if (params.email !== undefined) {
          $.get("rest/user/forgotpassword", {
            email: params.email
          }).done(function() {
            // check email message
          }).fail(function() {
            error("Invalid email");
          });
        }


        $("#register").click(function() {
          if (params.uuid !== undefined) {
            $.post("rest/user/resetpassword", {
              uuid: uuid,
              password1: $("#password1").val(),
              password2: $("#password2").val()
            }).done(function() {
              window.location.replace("login.html");
            }).fail(function() {
              error();
            });
          }
        });
      });
    </script>
  </body>
</html>
