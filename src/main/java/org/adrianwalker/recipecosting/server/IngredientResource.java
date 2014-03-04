package org.adrianwalker.recipecosting.server;

import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.adrianwalker.recipecosting.common.entity.Ingredient;

@Path("/ingredient")
public final class IngredientResource extends AbstractRecipeCostingUserEntityResource<Ingredient> {

  private static RecipeCostingUserEntityResourceDelegate<Ingredient> ingredientsDelegate;
  static {
    EntityManagerFactory emf = PersistenceManager.INSTANCE.getEntityManagerFactory();
    ingredientsDelegate = new RecipeCostingUserEntityResourceDelegate<Ingredient>(Ingredient.class, emf);
  }

  @Override
  public RecipeCostingUserEntityResourceDelegate<Ingredient> getDelegate() {
    return ingredientsDelegate;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> read() throws Exception {

    return read("ingredients", "name");
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> save(final Map<String, Object> save) throws Exception {

    return save(save, "ingredients saved");
  }
}
