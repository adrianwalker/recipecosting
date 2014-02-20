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
      <%@ include file="menu.jspf" %>
      <div>
        <label for="password1">Password:</label>
        <input id="password1" type="password"/>
      </div>
      <div>
        <label for="password2">Password Again:</label>
        <input id="password2" type="password"/>
      </div>
      <div>
        <input id="changepassword" type="button" value="Change Password"/>
      </div>
    </div>
    <script>
      $("#changepassword").click(function() {

        $.post("rest/user/changepassword", {
          password1: $("#password1").val(),
          password2: $("#password2").val()
        }).done(function() {
          dialog("Password changed");
        }).fail(function() {
          error();
        });
      });
    </script>
  </body>
</html>
