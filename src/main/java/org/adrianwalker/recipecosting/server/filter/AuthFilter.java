package org.adrianwalker.recipecosting.server.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AuthFilter implements Filter {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthFilter.class);
  private static final String REDIRECT_URL = "/login.html";
  private static final String[] PUBLIC_URLS = {
    REDIRECT_URL,
    "/enable.html",
    "/resetpassword.html",
    "/rest/user/login",
    "/rest/user/register",
    "/rest/user/enable",
    "/rest/user/forgotpassword",
    "/rest/user/resetpassword"
  };
  private static final String SESSION_USER_ATTRIBUTE = "user";
  
  @Override
  public void init(final FilterConfig fc) throws ServletException {
  }
  
  @Override
  public void destroy() {
  }
  
  private boolean isPublicUrl(final HttpServletRequest request) {
    
    String url = request.getRequestURI();
    
    LOGGER.debug("url = " + url);
    
    for (String publicUrl : PUBLIC_URLS) {
      if (url.contains(publicUrl)) {
        return true;
      }
    }
    
    return false;
  }
  
  private boolean isLoggedIn(final HttpServletRequest request) {
    
    Object user = request.getSession().getAttribute(SESSION_USER_ATTRIBUTE);
    
    return null != user;
  }
  
  @Override
  public void doFilter(
          final ServletRequest req,
          final ServletResponse res,
          final FilterChain chain) throws IOException, ServletException {
    
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    
    boolean publicUrl = isPublicUrl(request);
    boolean loggedIn = isLoggedIn(request);
    
    LOGGER.debug("publicUrl = " + publicUrl + " loggedIn = " + loggedIn);
    
    if (publicUrl || loggedIn) {
      chain.doFilter(req, res);
    } else {
      response.sendRedirect(request.getContextPath() + REDIRECT_URL);
    }
  }
}
