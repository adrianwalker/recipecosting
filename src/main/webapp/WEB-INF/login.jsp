<!DOCTYPE html>
<html>
  <head>
    <title></title>
    <%@ include file="head.jspf" %>
    <script src="js/login.min.js"></script>
  </head>
  <body>
    <%@ include file="dialog.jspf" %>
    <%@ include file="loginmenu.jspf" %>
    <div class="container">
      <form id="form1" name="form1" class="form-signin" role="form">
        <h2>Log In</h2>
        <input id="username" name="username" class="form-control" type="email" placeholder="Username" required autofocus/>
        <input id="password" name="password" class="form-control" type="password" placeholder="Password" required/>
        <br/>
        <button id="login" type="submit" class="btn btn-primary">Log in</button>
        <button id="forgotpassword" type="submit" class="btn btn-link">Forgotten your password?</button>
      </form>
    </div>
    <div class="container">
      <form id="form2" name="form2" class="form-signin" role="form">
        <h2>Register - It's Free!</h2>
        <input id="email" name="email" class="form-control" type="email" placeholder="Email Address" required />
        <input id="password1" class="form-control" name="password1" type="password" placeholder="Password" required/>
        <input id="password2" class="form-control" name="password2" type="password" placeholder="Password Again" required/>
        <br/>
        <button id ="register" type="submit" class="btn btn-primary">Register</button>
      </form>
    </div>
  </body>
</html>
