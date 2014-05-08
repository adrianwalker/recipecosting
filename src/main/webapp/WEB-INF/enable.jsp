<!DOCTYPE html>
<html>
  <head>
    <title></title>
    <%@ include file="head.jspf" %>
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
          }).fail(function(xhr, status, error) {
            dialog("Error", error);
          });
        }
      });
    </script>
  </body>
</html>
