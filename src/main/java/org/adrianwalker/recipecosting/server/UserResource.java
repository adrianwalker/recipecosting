package org.adrianwalker.recipecosting.server;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> register(
          @FormParam("email")
          final String email,
          @FormParam("password1")
          final String password1,
          @FormParam("password2")
          final String password2) throws Exception {

    LOGGER.info("email = " + email + " password1 = " + password1 + " password2 = " + password2);


    if (null == email || email.isEmpty()) {
      return response("Invalid email address");
    }

    if (null == password1 || password1.isEmpty()
            || null == password2 || password2.isEmpty()) {

      return response("Invalid passwords");
    }

    String password;
    if (password1.equals(password2)) {
      password = password1;
    } else {
      return response("Passwords do not match");
    }


    if (password.length() < PASSWORD_LENGTH) {
      return response("Passwords must be at least " + PASSWORD_LENGTH + " characters");
    }

    Matcher matcher = EMAIL_REGEX.matcher(email);
    if (!matcher.matches()) {
      return response("Invalid email address");
    }

    long count;
    try {
      count = loginController.countByUsername(email);
    } catch (Exception e) {
      String message = "error registering new user";
      LOGGER.error(message, e);
      return response(message);
    }

    if (count != 0) {
      return response("User already exists");
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
      String message = "error registering new user";
      LOGGER.error(message, e);
      return response(message);
    }

    return response("Check your email to enable your account");
  }

  @POST
  @Path("enable")
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> enable(
          @FormParam("uuid")
          final String uuid) throws Exception {

    LOGGER.info("uuid = " + uuid);

    if (null == uuid || uuid.length() < UUID_LENGTH) {
      return response("Invalid identifier");
    }

    User user = loginController.findByUuid(uuid);

    if (null != user) {
      user.setEnabled(true);
      user.setUuid(UUID.randomUUID().toString());

      try {
        user = userDelegate.update(user);
      } catch (Exception e) {
        String message = "error enabling user";
        LOGGER.error(message, e);
        return response(message);
      }

      UserData userData = new UserData(unitsDelegate, userConversionsDelegate);
      userData.createDefaultDataForUser(user);
    }

    return response();
  }

  @POST
  @Path("login")
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> login(
          @FormParam("username")
          final String username,
          @FormParam("password")
          final String password) throws Exception {

    LOGGER.info("username = " + username + ", password = " + password);

    if (null == username || username.isEmpty()
            || null == password || password.isEmpty()) {

      return response("Invalid username/password");
    }

    Matcher matcher = EMAIL_REGEX.matcher(username);
    if (!matcher.matches()) {
      return response("Invalid username/password");
    }

    User user;
    try {
      user = loginController.findByUsernamePassword(username, password);
    } catch (Exception e) {
      String message = "error logging in";
      LOGGER.error(message, e);
      return response(message);
    }

    if (null == user || !user.getEnabled()) {
      return response("Invalid username/password");
    }

    HttpSession session = getSession();
    session.setAttribute(SESSION_USER_ATTRIBUTE, user);

    return response();
  }

  @GET
  @Path("logout")
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> logout() throws Exception {

    HttpSession session = getSession();
    session.invalidate();

    return response();
  }

  @POST
  @Path("changepassword")
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> changePassword(
          @FormParam("password1")
          final String password1,
          @FormParam("password2")
          final String password2) throws Exception {

    String password;
    if (password1.equals(password2)) {
      password = password1;
    } else {
      return response("Passwords do not match");
    }

    if (null == password || password.length() < PASSWORD_LENGTH) {
      return response("Passwords must be atleast " + PASSWORD_LENGTH + " characters");
    }

    User user = getSessionUser();
    user.setPassword(password);

    try {
      user = userDelegate.update(user);
    } catch (Exception e) {
      String message = "error changing password";
      LOGGER.error(message, e);
      return response(message);
    }

    return response("Password changed");
  }

  @POST
  @Path("forgotpassword")
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> forgotPassword(
          @FormParam("username")
          final String username) throws Exception {

    LOGGER.info("username = " + username);

    if (null == username || username.isEmpty()) {
      return response("Invalid username");
    }

    User user;
    try {
      user = loginController.findByUsername(username);
    } catch (Exception e) {
      String message = "error finding user";
      LOGGER.error(message, e);
      return response(message);
    }

    if (null == user || !user.getEnabled()) {
      return response("User not found");
    }

    try {
      emailController.send(user.getEmail(), "password reset", "http://localhost:9090/recipecosting/resetpassword.html?uuid=" + user.getUuid());
    } catch (Exception e) {
      String message = "error sending password reset email";
      LOGGER.error(message, e);
      return response(message);
    }

    return response();
  }

  @POST
  @Path("resetpassword")
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> resetPassword(
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
      return response("Passwords do not match");
    }

    if (null == password || password.length() < PASSWORD_LENGTH) {
      return response("Passwords must be atleast " + PASSWORD_LENGTH + " characters");
    }

    User user;
    try {
      user = loginController.findByUuid(uuid);
    } catch (Exception e) {
      String message = "error resetting password";
      LOGGER.error(message, e);
      return response(message);
    }

    if (null == user || !user.getEnabled()) {
      return response("User not found");
    }

    user.setPassword(password);
    user.setUuid(UUID.randomUUID().toString());

    try {
      user = userDelegate.update(user);
    } catch (Exception e) {
      String message = "error resetting password";
      LOGGER.error(message, e);
      return response(message);
    }

    return response();
  }
}
