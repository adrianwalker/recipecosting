<!DOCTYPE html>
<html>
  <head>
    <title>Reset Password</title>
    <%@ include file="head.jspf" %>
    <script src="js/resetpassword.min.js"></script>
  </head>
  <body>
    <%@ include file="dialog.jspf" %>
    <%@ include file="menu.jspf" %>
    <div class="container">
      <form id="form" name="form" class="form330px" role="form">
        <h2>Reset Password</h2>
        <label for="password1">Password:</label>
        <input id="password1" name="password1" class="form-control" type="password" required autofocus/>
        <br/>
        <label for="password2">Password Again:</label>
        <input id="password2" name="password2" class="form-control" type="password" required/>
        <br/>
        <input id="resetpassword" type="button" value="Reset Password" class="btn btn-primary"/>
      </form>
    </div>
  </body>
</html>
