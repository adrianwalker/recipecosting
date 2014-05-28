<!DOCTYPE html>
<html>
  <head>
    <title>Log In</title>
    <%@ include file="head.jspf" %>
    <script src="js/login.min.js"></script>
  </head>
  <body>
    <%@ include file="dialog.jspf" %>
    <%@ include file="loginmenu.jspf" %>
    <div class="container">
      <form id="form1" name="form1" class="form330px" role="form">
        <h2>Log In</h2>
        <label for="username">Username:</label>
        <input id="username" name="username" class="form-control" type="email" required autofocus/>
        <br/>
        <label for="password">Password:</label>
        <input id="password" name="password" class="form-control" type="password"/>
        <br/>
        <button id="login" type="button" class="btn btn-primary">Log in</button>
        <button id="forgotpassword" type="button" class="btn btn-link">Forgotten your password?</button>
      </form>
    </div>
    <div class="container">
      <form id="form2" name="form2" class="form330px" role="form">
        <h2>Register - It's Free!</h2>
        <label for="email">Email Address:</label>
        <input id="email" name="email" class="form-control" type="email" required />
        <br/>
        <label for="password1">Password:</label>
        <input id="password1" class="form-control" name="password1" type="password" required/>
        <br/>
        <label for="password2">Password Again:</label>
        <input id="password2" class="form-control" name="password2" type="password" required/>
        <br/>
        <button id ="register" type="button" class="btn btn-primary">Register</button>
      </form>
    </div>
  </body>
</html>
