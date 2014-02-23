<!DOCTYPE html>
<html>
  <head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script src="js/jquery-1.11.0.min.js"></script>
    <script src="js/logging.js"></script>
    <script src="js/dialog.js"></script>
    <link rel="stylesheet" href="css/style.css" type="text/css" />
  </head>
  <body>
    <div>
      <%@ include file="dialog.jspf" %>
    </div>
    <script>

      $(function() {

        $.get("rest/user/logout", {
        }).done(function() {
          window.location.replace("login.html");
        }).fail(function() {
          error();
        });
      });
    </script>
  </body>
</html>
