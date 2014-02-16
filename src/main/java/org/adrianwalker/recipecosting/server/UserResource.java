package org.adrianwalker.recipecosting.server;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.adrianwalker.recipecosting.common.entity.Unit;
import org.adrianwalker.recipecosting.common.entity.UnitConversion;
import org.adrianwalker.recipecosting.common.entity.User;
import org.adrianwalker.recipecosting.server.controller.EmailController;
import org.adrianwalker.recipecosting.server.controller.LoginController;
import org.adrianwalker.recipecosting.server.util.Patterns;
import org.adrianwalker.recipecosting.server.util.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/user")
public final class UserResource extends AbstractResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);
  private static final String SESSION_USER_ATTRIBUTE = "user";
  private static final Pattern EMAIL_REGEX = Pattern.compile(Patterns.EMAIL);
  private static final int PASSWORD_LENGTH = 6;
  private static final int UUID_LENGTH = 36;
  private static LoginController loginController;
  private static EmailController emailController;
  private static RecipeCostingEntityResourceDelegate<User> userDelegate;
  private static RecipeCostingEntityResourceDelegate<Unit> unitsDelegate;
  private static RecipeCostingEntityResourceDelegate<UnitConversion> userConversionsDelegate;

  static {
    EntityManagerFactory emf = PersistenceManager.INSTANCE.getEntityManagerFactory();
    loginController = new LoginController(emf);
    emailController = new EmailController();
    userDelegate = new RecipeCostingEntityResourceDelegate<User>(User.class, emf);
    unitsDelegate = new RecipeCostingEntityResourceDelegate<Unit>(Unit.class, emf);
    userConversionsDelegate = new RecipeCostingEntityResourceDelegate<UnitConversion>(UnitConversion.class, emf);
  }

  @POST
  @Path("register")
  public Response register(
          @FormParam("email")
          final String email,
          @FormParam("password1")
          final String password1,
          @FormParam("password2")
          final String password2) throws Exception {

    String password;
    if (password1.equals(password2)) {
      password = password1;
    } else {
      throw new Exception("Passwords do not match");
    }

    LOGGER.info("email = " + email + " password = " + password);

    if (null == email || email.isEmpty()) {
      throw new Exception("Invalid email address");
    }

    if (null == password || password.isEmpty()) {
      throw new Exception("Invalid password");
    }

    if (password.length() < PASSWORD_LENGTH) {
      throw new Exception("Passwords must be atleast " + PASSWORD_LENGTH + " characters");
    }

    Matcher matcher = EMAIL_REGEX.matcher(email);
    if (!matcher.matches()) {
      throw new Exception("Invalid email address");
    }

    long count;
    try {
      count = loginController.countByUsername(email);
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
    user.setEnabled(false);
    user.setUuid(UUID.randomUUID().toString());

    try {
      user = userDelegate.update(user);
      emailController.send(email, "user registered", "http://localhost:9090/recipecosting/enable.html?uuid=" + user.getUuid());
    } catch (Exception e) {
      String message = "Error registering new user";
      LOGGER.error(message, e);
      throw new Exception(message, e);
    }

    return Response.ok().build();
  }

  @POST
  @Path("enable")
  public Response enable(
          @QueryParam("uuid")
          final String uuid) throws Exception {

    LOGGER.info("uuid = " + uuid);

    if (null == uuid || uuid.isEmpty()) {
      throw new Exception("Invalid uuid");
    }

    if (uuid.length() < UUID_LENGTH) {
      throw new Exception("Invalid uuid");
    }

    User user = loginController.findByUuid(uuid);

    if (null != user) {
      user.setEnabled(true);
      user.setUuid(UUID.randomUUID().toString());

      try {
        user = userDelegate.update(user);
      } catch (Exception e) {
        String message = "Error enabling user";
        LOGGER.error(message, e);
        throw new Exception(message, e);
      }

      UserData userData = new UserData(unitsDelegate, userConversionsDelegate);
      userData.createDefaultDataForUser(user);
    }

    return Response.ok().build();
  }

  @POST
  @Path("login")
  public Response login(
          @FormParam("username")
          final String username,
          @FormParam("password")
          final String password) throws Exception {

    LOGGER.info("username = " + username + ", password = " + password);

    User user;
    try {
      user = loginController.findByUsernamePassword(username, password);
    } catch (Exception e) {
      String message = "Unable to read login";
      LOGGER.error(message, e);
      throw new Exception(message, e);
    }

    if (null == user || !user.getEnabled()) {
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

  @POST
  @Path("changepassword")
  public Response changePassword(
          @FormParam("password1")
          final String password1,
          @FormParam("password2")
          final String password2) throws Exception {

    String password;
    if (password1.equals(password2)) {
      password = password1;
    } else {
      throw new Exception("Passwords do not match");
    }

    if (null == password || password.isEmpty()) {
      throw new Exception("Invalid password");
    }

    if (password.length() < PASSWORD_LENGTH) {
      throw new Exception("Passwords must be atleast " + PASSWORD_LENGTH + " characters");
    }

    User user = getSessionUser();
    user.setPassword(password);

    try {
      user = userDelegate.update(user);
    } catch (Exception e) {
      String message = "Error changing password";
      LOGGER.error(message, e);
      throw new Exception(message, e);
    }

    return Response.ok().build();
  }

  @POST
  @Path("forgotpassword")
  public Response forgotPassword(
          @FormParam("username")
          final String username) throws Exception {

    LOGGER.info("username = " + username);

    User user;
    try {
      user = loginController.findByUsername(username);
    } catch (Exception e) {
      String message = "Unable to read login";
      LOGGER.error(message, e);
      throw new Exception(message, e);
    }

    if (null == user || !user.getEnabled()) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    try {
      emailController.send(user.getEmail(), "password reset", "http://localhost:9090/recipecosting/resetpassword.html?uuid=" + user.getUuid());
    } catch (Exception e) {
      String message = "Error sending password reset email";
      LOGGER.error(message, e);
      throw new Exception(message, e);
    }

    return Response.ok().build();
  }

  @POST
  @Path("resetpassword")
  public Response resetPassword(
          @FormParam("uuid")
          final String uuid,
          @FormParam("password1")
          final String password1,
          @FormParam("password2")
          final String password2) throws Exception {

    String password;
    if (password1.equals(password2)) {
      password = password1;
    } else {
      throw new Exception("Passwords do not match");
    }

    if (null == password || password.isEmpty()) {
      throw new Exception("Invalid password");
    }

    if (password.length() < PASSWORD_LENGTH) {
      throw new Exception("Passwords must be atleast " + PASSWORD_LENGTH + " characters");
    }

    User user;
    try {
      user = loginController.findByUuid(uuid);
    } catch (Exception e) {
      String message = "Unable to read login";
      LOGGER.error(message, e);
      throw new Exception(message, e);
    }

    if (null == user || !user.getEnabled()) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    user.setPassword(password);
    user.setUuid(UUID.randomUUID().toString());

    try {
      user = userDelegate.update(user);
    } catch (Exception e) {
      String message = "Error changing password";
      LOGGER.error(message, e);
      throw new Exception(message, e);
    }

    return Response.ok().build();
  }
}
