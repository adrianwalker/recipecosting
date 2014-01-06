package org.adrianwalker.recipecosting.common.entity;

public interface RecipeCostingUserEntity extends RecipeCostingEntity {
  
  User getUser();
  
  void setUser(User user);
}
