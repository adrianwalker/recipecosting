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
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.adrianwalker.recipecosting.common.entity.Ingredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/ingredient")
public final class IngredientResource extends AbstractResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(IngredientResource.class);
  private static RecipeCostingUserEntityResourceDelegate<Ingredient> ingredientsDelegate;

  static {
    EntityManagerFactory emf = PersistenceManager.INSTANCE.getEntityManagerFactory();
    ingredientsDelegate = new RecipeCostingUserEntityResourceDelegate<Ingredient>(Ingredient.class, emf);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> readIngredients(
          @QueryParam("page")
          @DefaultValue("-1")
          final int page,
          @QueryParam("pageSize")
          @DefaultValue("-1")
          final int pageSize) throws Exception {

    LOGGER.info("page = " + page + " pageSize = " + pageSize);

    Map<String, Object> response = response();

    if (page < 0 || pageSize < 0) {
      response.put("ingredients", ingredientsDelegate.read(getSessionUser(), "name"));
    } else {
      response.put("ingredients", ingredientsDelegate.read(getSessionUser(), page, pageSize, "name"));
      response.put("pageCount", ingredientsDelegate.countPages(getSessionUser(), pageSize));
    }

    return response;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> updateIngredients(final List<Ingredient> ingredients) throws Exception {

    ingredientsDelegate.update(getSessionUser(), ingredients);

    return response("ingredients saved");
  }

  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> deleteIngredients(final List<Long> ids) throws Exception {

    ingredientsDelegate.delete(getSessionUser(), ids);

    return response("ingredients deleted");
  }
}
