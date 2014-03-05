package org.adrianwalker.recipecosting.server;

import java.util.Map;
import org.adrianwalker.recipecosting.common.entity.RecipeCostingUserEntity;
import org.adrianwalker.recipecosting.common.entity.Save;
import org.adrianwalker.recipecosting.common.entity.User;

public abstract class AbstractRecipeCostingUserEntityResource<T extends RecipeCostingUserEntity> extends AbstractResource {

  public abstract RecipeCostingUserEntityResourceDelegate<T> getDelegate();

  public Map<String, Object> read(final String entities, final String orderBy) throws Exception {

    Map<String, Object> response = response();

    RecipeCostingUserEntityResourceDelegate<T> delegate = getDelegate();
    response.put(entities, delegate.read(getSessionUser(), orderBy));

    return response;
  }

  public Map<String, Object> save(final Save<T> save, final String message) throws Exception {

    User sessionUser = getSessionUser();

    RecipeCostingUserEntityResourceDelegate<T> delegate = getDelegate();
    delegate.update(sessionUser, save.getChanged());
    delegate.delete(sessionUser, save.getIds());

    return response(message);
  }
}
