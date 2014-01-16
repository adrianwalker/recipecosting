package org.adrianwalker.recipecosting.server.controller;

import java.math.BigDecimal;
import java.util.UUID;
import javax.persistence.EntityManagerFactory;
import org.adrianwalker.recipecosting.common.entity.Ingredient;
import org.adrianwalker.recipecosting.common.entity.Recipe;
import org.adrianwalker.recipecosting.common.entity.RecipeIngredient;
import org.adrianwalker.recipecosting.common.entity.Unit;
import org.adrianwalker.recipecosting.common.entity.UnitConversion;
import org.adrianwalker.recipecosting.common.entity.User;
import org.adrianwalker.recipecosting.server.PersistenceManager;
import org.adrianwalker.recipecosting.server.RecipeCostingUserEntityResourceDelegate;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public final class RecipeEntityControllerTest {

  private static EntityManagerFactory emf;
  private RecipeCostingEntityController<User> userController;
  private RecipeCostingUserEntityResourceDelegate<Unit> unitsDelegate;
  private RecipeCostingUserEntityResourceDelegate<UnitConversion> unitConversionsDelegate;
  private RecipeCostingUserEntityResourceDelegate<Ingredient> ingredientsDelegate;
  private RecipeCostingUserEntityResourceDelegate<Recipe> recipesDelegate;

  public RecipeEntityControllerTest() {
  }

  @BeforeClass
  public static void setUpClass() {
    emf = PersistenceManager.INSTANCE.getEntityManagerFactory();
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() {
    userController = new RecipeCostingEntityController<User>(User.class, emf);
    unitsDelegate = new RecipeCostingUserEntityResourceDelegate<Unit>(Unit.class, emf);
    unitConversionsDelegate = new RecipeCostingUserEntityResourceDelegate<UnitConversion>(UnitConversion.class, emf);
    ingredientsDelegate = new RecipeCostingUserEntityResourceDelegate<Ingredient>(Ingredient.class, emf);
    recipesDelegate = new RecipeCostingUserEntityResourceDelegate<Recipe>(Recipe.class, emf);
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testCreateDelete() throws Exception {

    String username = "user";

    User user = new User();
    user.setEmail(username + "@" + username + ".com");
    user.setUsername(username);
    user.setPassword(username);
    user.setEnabled(true);
    user.setUuid(UUID.randomUUID().toString());
    userController.create(user);

    Unit unit = new Unit();
    unit.setName("unit");
    unit.setUser(user);
    unitsDelegate.update(user, unit);

    assertEquals(1, unitsDelegate.count(user));

    Ingredient ingredient = new Ingredient();
    ingredient.setName("ingredient");
    ingredient.setAmount(BigDecimal.ONE);
    ingredient.setCost(BigDecimal.ONE);
    ingredient.setUnit(unit);
    ingredient.setUser(user);
    ingredientsDelegate.update(user, ingredient);

    assertEquals(1, ingredientsDelegate.count(user));

    Recipe recipe = new Recipe();
    recipe.setName("recipe");
    recipe.setServes(1);
    recipe.setUser(user);
    RecipeIngredient recipeIngredient = new RecipeIngredient();
    recipeIngredient.setAmount(BigDecimal.ONE);
    recipeIngredient.setRecipe(recipe);
    recipeIngredient.setIngredient(ingredient);
    recipeIngredient.setUnit(unit);
    recipeIngredient.setUser(user);
    recipe.getRecipeIngredients().add(recipeIngredient);

    recipesDelegate.update(user, recipe);

    assertEquals(1, recipesDelegate.count(user));

    System.out.println(recipe);

    recipesDelegate.delete(user, recipe.getId());
    ingredientsDelegate.delete(user, ingredient.getId());
    unitsDelegate.delete(user, unit.getId());
    userController.destroy(user.getId());
  }
}