package org.adrianwalker.recipecosting.server;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.adrianwalker.recipecosting.common.entity.Recipe;
import org.adrianwalker.recipecosting.common.entity.RecipeIngredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/recipe")
public final class RecipeResource extends AbstractResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(RecipeResource.class);
  private static RecipeCostingUserEntityResourceDelegate<Recipe> recipesDelegate;
  private static RecipeCostingUserEntityResourceDelegate<RecipeIngredient> recipeIngredientsDelegate;

  static {
    EntityManagerFactory emf = PersistenceManager.INSTANCE.getEntityManagerFactory();
    recipesDelegate = new RecipeCostingUserEntityResourceDelegate<Recipe>(Recipe.class, emf);
    recipeIngredientsDelegate = new RecipeCostingUserEntityResourceDelegate<RecipeIngredient>(RecipeIngredient.class, emf);
  }

  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Recipe readRecipe(
          @PathParam("id")
          final long id) throws Exception {

    Recipe recipe = recipesDelegate.read(getSessionUser(), id);

    return recipe;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> readRecipes(
          @QueryParam("page")
          @DefaultValue("-1")
          final int page,
          @QueryParam("pageSize")
          @DefaultValue("-1")
          final int pageSize) throws Exception {

    LOGGER.info("page = " + page + " pageSize = " + pageSize);

    List<Recipe> recipes;
    if (page < 0 || pageSize < 0) {
      recipes = recipesDelegate.read(getSessionUser(), "name");
    } else {
      recipes = recipesDelegate.read(getSessionUser(), page, pageSize, "name");
    }

    for (Recipe recipe : recipes) {
      recipe.setRecipeIngredients(null);
    }

    Map<String, Object> response = response();

    if (page < 0 || pageSize < 0) {
      response.put("recipes", recipes);
    } else {
      response.put("recipes", recipes);
      response.put("pageCount", recipesDelegate.countPages(getSessionUser(), pageSize));
    }
    
    return response;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> updateRecipe(final Recipe recipe) throws Exception {

    Map<String, Object> response = response();

    List<RecipeIngredient> recipeIngredients = recipe.getRecipeIngredients();
    for (RecipeIngredient recipeIngredient : recipeIngredients) {
      recipeIngredient.setRecipe(recipe);
      recipeIngredient.setUser(getSessionUser());
    }
    
    response.put("recipe", recipesDelegate.update(getSessionUser(), recipe));
    response.put("message", "recipe saved");

    return response;
  }

  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> deleteRecipes(final List<Long> ids) throws Exception {

    recipesDelegate.delete(getSessionUser(), ids);

    return response("recipes deleted");
  }

  @DELETE
  @Path("/ingredient")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> deleteRecipeIngredients(final List<Long> recipeIngredientIds) throws Exception {

    for (Long recipeIngredientId : recipeIngredientIds) {

      RecipeIngredient recipeIngredient = recipeIngredientsDelegate.read(getSessionUser(), recipeIngredientId);
      Recipe recipe = recipeIngredient.getRecipe();
      recipe.getRecipeIngredients().remove(recipeIngredient);
      recipeIngredientsDelegate.delete(getSessionUser(), recipeIngredientId);
      recipesDelegate.update(getSessionUser(), recipe);
    }

    return response("recipe ingredients deleted");
  }
}
