package org.adrianwalker.recipecosting.server;

import java.util.ArrayList;
import java.util.List;
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

    List<String> messages = new ArrayList<String>();

    List<Long> ids = save.getIds();
    for (Long id : ids) {
      try {
        delegate.delete(sessionUser, id);
      } catch (Exception ex) {
        messages.add(ex.getMessage());
      }
    }

    List<T> changed = save.getChanged();
    for (T change : changed) {
      try {
        delegate.update(sessionUser, change);
      } catch (Exception ex) {
        messages.add(ex.getMessage());
      }
    }
    
    Map<String, Object> response = response();
    response.put("message", message);
    if(!messages.isEmpty()) {
      response.put("messages", messages);
    }

    return response;
  }
}
