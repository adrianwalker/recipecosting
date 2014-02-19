<!DOCTYPE html>
<html>
  <head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
    <script src="js/logging.js"></script>
    <script src="js/url.js"></script>
    <script src="js/error.js"></script>
    <script src="js/dialog.js"></script>
    <link rel="stylesheet" href="css/style.css" type="text/css" />
  </head>
  <body>
    <div>
      <%@ include file="dialog.jspf" %>
    </div>
    <script>

      $(function() {

        var params = getUrlParams();
        if (params.uuid !== undefined) {
          $.post("rest/user/enable", {
            uuid: params.uuid
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
