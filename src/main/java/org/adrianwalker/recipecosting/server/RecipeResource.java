package org.adrianwalker.recipecosting.server;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.adrianwalker.recipecosting.common.entity.Recipe;
import org.adrianwalker.recipecosting.common.entity.RecipeIngredient;

@Path("/recipe")
public final class RecipeResource extends AbstractRecipeCostingUserEntityResource<Recipe> {

  private static RecipeCostingUserEntityResourceDelegate<Recipe> recipesDelegate;
  private static RecipeCostingUserEntityResourceDelegate<RecipeIngredient> recipeIngredientsDelegate;

  static {
    EntityManagerFactory emf = PersistenceManager.INSTANCE.getEntityManagerFactory();
    recipesDelegate = new RecipeCostingUserEntityResourceDelegate<Recipe>(Recipe.class, emf);
    recipeIngredientsDelegate = new RecipeCostingUserEntityResourceDelegate<RecipeIngredient>(RecipeIngredient.class, emf);
  }

  @Override
  public RecipeCostingUserEntityResourceDelegate<Recipe> getDelegate() {
    return recipesDelegate;
  }

  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Recipe read(
    @PathParam("id")
    final long id) throws Exception {

    return getDelegate().read(getSessionUser(), id);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> read() throws Exception {

    List<Recipe> recipes = getDelegate().read(getSessionUser(), "name");

    for (Recipe recipe : recipes) {
      recipe.setRecipeIngredients(null);
    }

    Map<String, Object> response = response();
    response.put("recipes", recipes);

    return response;
  }

  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> delete(final List<Long> ids) throws Exception {

    getDelegate().delete(getSessionUser(), ids);

    return response("recipes deleted");
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> save(final Map<String, Object> save) throws Exception {

    // save recipe
    Recipe recipe = (Recipe) save.get("recipe");
    List<RecipeIngredient> recipeIngredients = recipe.getRecipeIngredients();
    for (RecipeIngredient recipeIngredient : recipeIngredients) {
      recipeIngredient.setRecipe(recipe);
      recipeIngredient.setUser(getSessionUser());
    }

    // delete any ingredients
    List<Long> recipeIngredientIds = (List<Long>) save.get("ids");
    for (Long recipeIngredientId : recipeIngredientIds) {
      RecipeIngredient recipeIngredient = recipeIngredientsDelegate.read(getSessionUser(), recipeIngredientId);
      recipeIngredients.remove(recipeIngredient);
      recipeIngredientsDelegate.delete(getSessionUser(), recipeIngredientId);
    }

    recipe = getDelegate().update(getSessionUser(), recipe);

    Map<String, Object> response = response();
    response.put("recipe", recipe);
    response.put("message", "recipe saved");

    return response;
  }
}
