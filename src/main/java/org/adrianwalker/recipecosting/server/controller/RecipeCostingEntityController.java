package org.adrianwalker.recipecosting.server.controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.adrianwalker.recipecosting.common.entity.RecipeCostingEntity;
import org.adrianwalker.recipecosting.server.controller.exceptions.NonexistentEntityException;

public class RecipeCostingEntityController<T extends RecipeCostingEntity> {

  private EntityManagerFactory emf = null;
  private final Class<T> clazz;

  public RecipeCostingEntityController(final Class<T> clazz, final EntityManagerFactory emf) {
    this.clazz = clazz;
    this.emf = emf;
  }

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public void create(final T entity) {
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

  public void edit(T entity) throws NonexistentEntityException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      entity = em.merge(entity);
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

  public void destroy(Long id) throws NonexistentEntityException {
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

  public T find(Long id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(clazz, id);
    } finally {
      em.close();
    }
  }

  public long count() {
    EntityManager em = getEntityManager();
    try {
      CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
      Root<T> rt = cq.from(clazz);
      cq.select(em.getCriteriaBuilder().count(rt));
      Query q = em.createQuery(cq);
      return (Long) q.getSingleResult();
    } finally {
      em.close();
    }
  }
}
