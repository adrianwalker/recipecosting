<!DOCTYPE html>
<html>
  <head>
    <title></title>
    <%@ include file="head.jspf" %>
    <script src="js/changepassword.min.js"></script>
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
  </body>
</html>
