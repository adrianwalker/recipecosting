<!DOCTYPE html>
<html>
  <head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
    <script src="js/logging.js"></script>
    <script src="js/url.js"></script>
    <script src="js/error.js"></script>
  </head>
  <body>
    <div>
    </div>
    <script>

      $(function() {

        var params = getUrlParams();
        if (params.email !== undefined) {
          $.get("rest/user/forgotpassword", {
            uuid: params.uuid
          }).done(function() {
          }).fail(function() {
            error("Invalid uuid");
          });
        }
      });
    </script>
  </body>
</html>
