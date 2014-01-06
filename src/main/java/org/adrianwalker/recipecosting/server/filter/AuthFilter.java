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
import javax.servlet.http.HttpSession;

public final class AuthFilter implements Filter {

  private static final String REDIRECT_URL = "/login.html";
  private static final String[] PUBLIC_URLS = {
    REDIRECT_URL,
    "/register.html",
    "/rest/user/login",
    "/rest/user/register"
  };

  @Override
  public void init(final FilterConfig fc) throws ServletException {
  }

  @Override
  public void destroy() {
  }

  private boolean isPublicUrl(final HttpServletRequest request) {

    String url = request.getServletPath();

    for (String publicUrl : PUBLIC_URLS) {
      if (url.contains(publicUrl)) {
        return true;
      }
    }

    return false;
  }

  private boolean isLoggedIn(final HttpServletRequest request) {

    HttpSession session = request.getSession(false);

    return null != session;
  }

  @Override
  public void doFilter(
          final ServletRequest req,
          final ServletResponse res,
          final FilterChain chain) throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    if (isPublicUrl(request) || isLoggedIn(request)) {
      chain.doFilter(req, res);
    } else {
      response.sendRedirect(request.getContextPath() + REDIRECT_URL);
    }
  }
}
