<!DOCTYPE html>
<html>
  <head>
    <title></title>
    <%@ include file="head.jspf" %>
  </head>
  <body>
    <%@ include file="dialog.jspf" %>
    <%@ include file="menu.jspf" %>
    <div class="container">
      <form id="form" name="form" class="form-signin" role="form">
        <h2>Change Password</h2>
        <input id="password1" name="password1" class="form-control" type="password" placeholder="Password" required autofocus/>
        <input id="password2" name="password2" class="form-control" type="password" placeholder="Password Again" required/>
        <br/>
        <input id="changepassword" type="button" value="Change Password" class="btn btn-primary"/>
      </form>
    </div>
    <script>
      $(function() {

        var form = $("#form");

        form.validate({
          rules: {
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
          }
        });

        $("#changepassword").click(function() {

          if (!form.valid()) {
            return false;
          }

          $.post("rest/user/changepassword", {
            password1: $("#password1").val(),
            password2: $("#password2").val()
          }).done(function() {
            dialog("Change Password", "Password changed");
          }).fail(function(xhr, status, error) {
            dialog("Error", error);
          });
        });
      });
    </script>
  </body>
</html>
