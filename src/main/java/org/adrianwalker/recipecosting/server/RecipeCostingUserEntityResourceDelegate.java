package org.adrianwalker.recipecosting.server;

import java.util.List;
import javax.persistence.EntityManagerFactory;
import org.adrianwalker.recipecosting.common.entity.RecipeCostingUserEntity;
import org.adrianwalker.recipecosting.common.entity.User;
import org.adrianwalker.recipecosting.server.controller.RecipeCostingUserEntityController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RecipeCostingUserEntityResourceDelegate<T extends RecipeCostingUserEntity> {

  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = LoggerFactory.getLogger(RecipeCostingUserEntityResourceDelegate.class);
  private RecipeCostingUserEntityController<T> controller;
  private Class<T> clazz;

  public RecipeCostingUserEntityResourceDelegate(final Class<T> clazz, final EntityManagerFactory emf) {

    this.clazz = clazz;

    LOGGER.info("clazz = " + clazz);

    controller = new RecipeCostingUserEntityController<T>(clazz, emf);
  }

  public T read(final User user, final long id) throws Exception {

    LOGGER.info("user = " + user.getUsername() + ", id = " + id);

    try {
      return controller.find(user, id);
    } catch (Exception e) {
      String message = "Unable to read " + getClassName();
      LOGGER.error(message, e);
      throw new Exception(message, e);
    }
  }

  public List<T> read(final User user, final String orderBy) throws Exception {
    return read(user, true, -1, -1, orderBy);
  }

  public List<T> read(final User user, final int page, final int pageSize, final String orderBy) throws Exception {
    return read(user, false, page, pageSize, orderBy);
  }

  private List<T> read(final User user, final boolean all, final int page, final int pageSize, final String orderBy) throws Exception {

    LOGGER.info("user = " + user.getUsername() + ", page = " + page + ", pageSize = " + pageSize + ", orderBy = " + orderBy);

    List<T> entities;
    if (!all) {

      int firstResult = (page - 1) * pageSize;
      int maxResults = pageSize;

      LOGGER.info("maxResults = " + maxResults + ", firstResult = " + firstResult);

      try {
        entities = controller.find(user, maxResults, firstResult, orderBy);
      } catch (Exception e) {
        String message = "Unable to read " + getClassName();
        LOGGER.error(message, e);
        throw new Exception(message, e);
      }
    } else {

      try {
        entities = controller.find(user, orderBy);
      } catch (Exception e) {
        String message = "Unable to read " + getClassName();
        LOGGER.error(message, e);
        throw new Exception(message, e);
      }
    }

    LOGGER.info("entities.size() = " + entities.size());

    return entities;

  }

  public T update(final User user, final T entity) throws Exception {

    LOGGER.info("user = " + user.getUsername() + ", entity = " + entity);

    entity.setUser(user);
    
    Long id = entity.getId();

    if (id == null) {
      create(user, entity);
      id = entity.getId();
    } else {
      read(user, id);
      edit(entity);
    }

    return read(user, id);
  }

  public void update(final User user, final List<T> entities) throws Exception {

    LOGGER.info("user = " + user.getUsername() + ", entities = " + entities);

    for (T entity : entities) {
      
      entity.setUser(user);
      
      Long id = entity.getId();
      if (id == null) {
        create(user, entity);
      } else {
        read(user, id);
        edit(entity);
      }
    }
  }

  public void delete(final User user, final List<Long> ids) throws Exception {

    LOGGER.info("user = " + user.getUsername() + ", ids = " + ids);

    for (long id : ids) {
      read(user, id);
      destroy(id);
    }
  }

  public void delete(final User user, final long id) throws Exception {

    LOGGER.info("user = " + user.getUsername() + ", id = " + id);

    read(user, id);
    destroy(id);
  }

  public long count(final User user) throws Exception {

    LOGGER.info("user = " + user.getUsername());

    try {
      return controller.count(user);
    } catch (Exception e) {
      String message = "Unable to count " + getClassName();
      LOGGER.error(message, e);
      throw new Exception(message, e);
    }
  }

  public int countPages(final User user, final int pageSize) throws Exception {

    try {
      long count = controller.count(user);
      int pageCount = (int) ((count + pageSize - 1) / pageSize);

      LOGGER.info("user = " + user.getUsername() + ", count = " + count + ", pageCount = " + pageCount);

      return pageCount;
    } catch (Exception e) {
      String message = "Unable to count pages for " + getClassName();
      LOGGER.error(message, e);
      throw new Exception(message, e);
    }
  }

  private void create(final User user, final T entity) throws Exception {

    LOGGER.info("user = " + user.getUsername() + ", entity = " + entity);

    try {
      entity.setUser(user);
      controller.create(entity);
    } catch (Exception e) {
      String message = "Unable to create " + getClassName();
      LOGGER.error(message, e);
      throw new Exception(message, e);
    }
  }

  private void edit(final T entity) throws Exception {

    LOGGER.info("entity = " + entity);

    try {
      controller.edit(entity);
    } catch (Exception e) {
      String message = "Unable to save " + getClassName();
      LOGGER.error(message, e);
      throw new Exception(message, e);
    }
  }

  private void destroy(final long id) throws Exception {

    LOGGER.info("id = " + id);

    try {
      controller.destroy(id);
    } catch (Exception e) {
      String message = "Unable to delete " + getClassName();
      LOGGER.error(message, e);
      throw new Exception(message, e);
    }
  }

  private String getClassName() {
    return clazz.getName().substring(clazz.getName().lastIndexOf('.') + 1).toLowerCase();
  }
}
