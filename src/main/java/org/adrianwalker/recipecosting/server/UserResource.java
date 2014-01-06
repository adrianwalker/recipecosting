package org.adrianwalker.recipecosting.server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.adrianwalker.recipecosting.common.entity.User;
import org.adrianwalker.recipecosting.server.controller.LoginController;
import org.adrianwalker.recipecosting.server.util.Patterns;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/user")
public final class UserResource  extends AbstractResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);
  private static final String SESSION_USER_ATTRIBUTE = "user";
  private static final Pattern EMAIL_REGEX = Pattern.compile(Patterns.EMAIL);
  private static final int MIN_PASSWORD_LENGTH = 6;
  private static LoginController loginController;
  private static RecipeCostingEntityResourceDelegate<User> userDelegate;

  static {
    EntityManagerFactory emf = PersistenceManager.INSTANCE.getEntityManagerFactory();
    loginController = new LoginController(emf);
    userDelegate = new RecipeCostingEntityResourceDelegate<User>(User.class, emf);
  }

  @POST
  @Path("register")
  public Response register(
          @QueryParam("email")
          final String email,
          @QueryParam("password")
          final String password) throws Exception {

    LOGGER.info("email = " + email + "password = " + password);

    if (null == email || email.isEmpty()) {
      throw new Exception("Invalid email address");
    }

    if (null == password || password.isEmpty()) {
      throw new Exception("Invalid password");
    }

    if (password.length() < MIN_PASSWORD_LENGTH) {
      throw new Exception("Passwords must be atleast " + MIN_PASSWORD_LENGTH + " characters");
    }

    Matcher matcher = EMAIL_REGEX.matcher(email);
    if (!matcher.matches()) {
      throw new Exception("Invalid email address");
    }

    long count;
    try {
      count = loginController.count(email);
    } catch (Exception e) {
      String message = "Error registering new user";
      LOGGER.error(message, e);
      throw new Exception(message, e);
    }

    if (count != 0) {
      throw new Exception("User already exists");
    }

    User user = new User();
    user.setEmail(email);
    user.setUsername(email);
    user.setPassword(password);

    try {
      userDelegate.update(user);
    } catch (Exception e) {
      String message = "Error registering new user";
      LOGGER.error(message, e);
      throw new Exception(message, e);
    }

    return Response.ok().build();
  }

  @GET
  @Path("login")
  public Response login(
          @QueryParam("username")
          final String username,
          @QueryParam("password")
          final String password) throws Exception {

    LOGGER.info("username = " + username + ", password = " + password);

    User user;
    try {
      user = loginController.find(username, password);
    } catch (Exception e) {
      String message = "Unable to read login";
      LOGGER.error(message, e);
      throw new Exception(message, e);
    }

    if (null == user) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    HttpSession session = getSession();
    session.setAttribute(SESSION_USER_ATTRIBUTE, user);

    return Response.ok().build();
  }

  @GET
  @Path("logout")
  public Response logout() throws Exception {

    HttpSession session = getSession();
    session.invalidate();

    return Response.ok().build();
  }
}
