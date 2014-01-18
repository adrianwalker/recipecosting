package org.adrianwalker.recipecosting.server.controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.adrianwalker.recipecosting.common.entity.User;

public final class LoginController {

  private EntityManagerFactory emf = null;

  public LoginController(final EntityManagerFactory emf) {
    this.emf = emf;
  }

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

  public User findByUsername(final String username) {
    EntityManager em = getEntityManager();

    try {
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery cq = cb.createQuery();
      Root entity = cq.from(User.class);
      cq.select(entity);
      cq.where(cb.equal(entity.get("username"), username));

      Query query = em.createQuery(cq);
      return (User) query.getSingleResult();
    } catch (final NoResultException nre) {
      return null;
    } finally {
      em.close();
    }
  }

  public User findByUsernamePassword(final String username, final String password) {
    EntityManager em = getEntityManager();

    try {
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery cq = cb.createQuery();
      Root entity = cq.from(User.class);
      cq.select(entity);
      cq.where(
              cb.equal(entity.get("username"), username),
              cb.equal(entity.get("password"), password));

      Query query = em.createQuery(cq);
      return (User) query.getSingleResult();
    } catch (final NoResultException nre) {
      return null;
    } finally {
      em.close();
    }
  }

  public User findByUuid(final String uuid) {
    EntityManager em = getEntityManager();

    try {
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery cq = cb.createQuery();
      Root entity = cq.from(User.class);
      cq.select(entity);
      cq.where(cb.equal(entity.get("uuid"), uuid));

      Query query = em.createQuery(cq);
      return (User) query.getSingleResult();
    } catch (final NoResultException nre) {
      return null;
    } finally {
      em.close();
    }
  }

  public long countByUsername(final String username) {
    EntityManager em = getEntityManager();
    try {
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery cq = cb.createQuery();
      Root<User> entity = cq.from(User.class);
      cq.select(em.getCriteriaBuilder().count(entity));
      cq.where(cb.equal(entity.get("username"), username));

      Query query = em.createQuery(cq);
      return (Long) query.getSingleResult();
    } finally {
      em.close();
    }
  }
}
