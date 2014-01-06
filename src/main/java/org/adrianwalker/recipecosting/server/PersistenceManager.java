package org.adrianwalker.recipecosting.server;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public enum PersistenceManager {

  INSTANCE;
  private static final String PERSISTENCE_UNIT = "RecipeCostingPU";
  private EntityManagerFactory emf;

  private PersistenceManager() {
  }

  public EntityManagerFactory getEntityManagerFactory() {

    if (null == emf) {
      emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
    }
    return emf;
  }

  public void closeEntityManagerFactory() {

    if (null != emf) {
      emf.close();
    }
  }
}
