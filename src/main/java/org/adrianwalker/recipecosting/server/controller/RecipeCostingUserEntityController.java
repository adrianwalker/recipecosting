package org.adrianwalker.recipecosting.server.controller;

import java.util.List;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import org.adrianwalker.recipecosting.common.entity.RecipeCostingEntity;
import org.adrianwalker.recipecosting.common.entity.RecipeCostingUserEntity;
import org.adrianwalker.recipecosting.common.entity.User;
import org.adrianwalker.recipecosting.server.controller.exceptions.NonexistentEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RecipeCostingUserEntityController<T extends RecipeCostingUserEntity> {

  private static final Logger LOGGER = LoggerFactory.getLogger(RecipeCostingUserEntityController.class);
  private static final Pattern PATH_SEPARATOR = Pattern.compile("\\.");
  private Class<T> clazz;
  private EntityManagerFactory emf;

  public RecipeCostingUserEntityController(final Class<T> clazz, final EntityManagerFactory emf) {

    LOGGER.info("clazz = " + clazz);

    this.clazz = clazz;
    this.emf = emf;
  }

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public void create(final T entity) {

    LOGGER.info("entity = " + entity);

    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      em.persist(entity);
      em.getTransaction().commit();
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void edit(final T entity) throws NonexistentEntityException, Exception {

    LOGGER.info("entity = " + entity);

    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      em.merge(entity);
      em.getTransaction().commit();
    } catch (Exception ex) {
      String msg = ex.getLocalizedMessage();
      if (msg == null || msg.length() == 0) {
        Long id = entity.getId();
        if (find(id) == null) {
          throw new NonexistentEntityException("The entity with id " + id + " no longer exists.");
        }
      }
      throw ex;
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public void destroy(final long id) throws NonexistentEntityException {

    LOGGER.info("id = " + id);

    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      RecipeCostingEntity entity;
      try {
        entity = em.getReference(clazz, id);
        entity.getId();
      } catch (EntityNotFoundException enfe) {
        throw new NonexistentEntityException("The entity with id " + id + " no longer exists.", enfe);
      }
      em.remove(entity);
      em.getTransaction().commit();
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  public List<T> find(final User user, final String orderBy) {
    return find(user, true, -1, -1, orderBy);
  }

  public List<T> find(final User user, final int maxResults, final int firstResult, final String orderBy) {
    return find(user, false, maxResults, firstResult, orderBy);
  }

  private List<T> find(final User user, final boolean all, final int maxResults, final int firstResult, final String orderBy) {

    LOGGER.info("user = " + user.getUsername() + ", all = " + all + ", maxResults = " + maxResults + ", firstResult = " + firstResult + ", orderBy = " + orderBy);

    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery cq = cb.createQuery();
      Root entity = cq.from(clazz);
      cq.select(entity);
      cq.where(cb.equal(entity.get("user"), user));

      Path path = null;
      for (String part : PATH_SEPARATOR.split(orderBy)) {
        if (null == path) {
          path = entity.get(part);
        } else {
          path = path.get(part);
        }
      }

      if (null != path) {
        cq.orderBy(cb.asc(path));
      }

      Query q = em.createQuery(cq);

      if (!all) {
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
      }

      return q.getResultList();
    } finally {
      em.close();
    }
  }

  public T find(final User user, long id) {
    LOGGER.info("user = " + user.getUsername() + ", id = " + id);

    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery cq = cb.createQuery();
      Root entity = cq.from(clazz);
      cq.select(entity);
      cq.where(
              cb.equal(entity.get("id"), id),
              cb.equal(entity.get("user"), user));

      Query q = em.createQuery(cq);
      return (T) q.getSingleResult();
    } finally {
      em.close();
    }
  }

  private T find(final long id) {

    LOGGER.info("id = " + id);

    EntityManager em = getEntityManager();
    try {
      return em.find(clazz, id);
    } finally {
      em.close();
    }
  }

  public long count(final User user) {

    LOGGER.info("user = " + user.getUsername());

    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery cq = cb.createQuery();
      Root<RecipeCostingEntity> entity = cq.from(clazz);
      cq.select(em.getCriteriaBuilder().count(entity));
      cq.where(cb.equal(entity.get("user"), user));
      Query q = em.createQuery(cq);
      return (Long) q.getSingleResult();
    } finally {
      em.close();
    }
  }
}
