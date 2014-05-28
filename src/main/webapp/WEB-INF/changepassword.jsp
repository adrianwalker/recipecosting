<!DOCTYPE html>
<html>
  <head>
    <title>Change Password</title>
    <%@ include file="head.jspf" %>
    <script src="js/changepassword.min.js"></script>
  </head>
  <body>
    <%@ include file="dialog.jspf" %>
    <%@ include file="menu.jspf" %>
    <div class="container">
      <form id="form" name="form" class="form330px" role="form">
        <h2>Change Password</h2>
        <label for="password1">Password:</label>
        <input id="password1" name="password1" class="form-control" type="password" required autofocus/>
        <br/>
        <label for="password2">Password Again:</label>
        <input id="password2" name="password2" class="form-control" type="password" required/>
        <br/>
        <input id="changepassword" type="button" value="Change Password" class="btn btn-primary"/>
      </form>
    </div>
  </body>
</html>
