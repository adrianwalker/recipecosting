package org.adrianwalker.recipecosting.server;

import javax.persistence.EntityManagerFactory;
import org.adrianwalker.recipecosting.common.entity.RecipeCostingEntity;
import org.adrianwalker.recipecosting.server.controller.RecipeCostingEntityController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RecipeCostingEntityResourceDelegate<T extends RecipeCostingEntity> {

  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = LoggerFactory.getLogger(RecipeCostingEntityResourceDelegate.class);
  private RecipeCostingEntityController<T> controller;

  public RecipeCostingEntityResourceDelegate(final Class<T> clazz, final EntityManagerFactory emf) {

    LOGGER.info("clazz = " + clazz);

    controller = new RecipeCostingEntityController<T>(clazz, emf);
  }

  public T read(final long id) throws Exception {

    LOGGER.info("id = " + id);

    try {
      return controller.find(id);
    } catch (Exception e) {
      String message = "Unable to read entity";
      LOGGER.error(message, e);
      throw new Exception(message, e);
    }
  }

  public T update(final T entity) throws Exception {

    LOGGER.info("entity = " + entity);

    Long id = entity.getId();

    if (id == null) {
      create(entity);
      id = entity.getId();
    } else {
      edit(entity);
    }

    return read(id);
  }

  public void delete(final long id) throws Exception {

    LOGGER.info("id = " + id);

    destroy(id);
  }

  private void create(final T entity) throws Exception {

    LOGGER.info("entity = " + entity);

    try {
      controller.create(entity);
    } catch (Exception e) {
      String message = "Unable to create entity";
      LOGGER.error(message, e);
      throw new Exception(message, e);
    }
  }

  private void edit(final T entity) throws Exception {

    LOGGER.info("entity = " + entity);

    try {
      controller.edit(entity);
    } catch (Exception e) {
      String message = "Unable to save entity";
      LOGGER.error(message, e);
      throw new Exception(message, e);
    }
  }

  private void destroy(final long id) throws Exception {

    LOGGER.info("id = " + id);

    try {
      controller.destroy(id);
    } catch (Exception e) {
      String message = "Unable to delete entity";
      LOGGER.error(message, e);
      throw new Exception(message, e);
    }
  }
}
