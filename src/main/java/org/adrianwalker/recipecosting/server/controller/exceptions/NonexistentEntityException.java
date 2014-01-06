package org.adrianwalker.recipecosting.server.controller.exceptions;

public final class NonexistentEntityException extends Exception {

  public NonexistentEntityException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public NonexistentEntityException(final String message) {
    super(message);
  }
}
