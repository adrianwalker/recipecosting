package org.adrianwalker.recipecosting.server;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import org.adrianwalker.recipecosting.common.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractResource.class);
  private static final String SESSION_USER_ATTRIBUTE = "user";
  @Context
  private HttpServletRequest request;

  @GET
  @Path("test")
  @Produces("text/plain")
  public String test() {
    return "test";
  }

  public HttpSession getSession() {
    return request.getSession();
  }

  public User getSessionUser() {
    return (User) getSession().getAttribute(SESSION_USER_ATTRIBUTE);
  }

  public Map<String, Object> response(final String message) {

    Map<String, Object> response = response();
    response.put("message", message);

    return response;
  }

  public HashMap<String, Object> response() {
    return new HashMap<String, Object>();
  }
}
