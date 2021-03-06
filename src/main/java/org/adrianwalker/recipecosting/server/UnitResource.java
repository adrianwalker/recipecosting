package org.adrianwalker.recipecosting.server;

import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.adrianwalker.recipecosting.common.entity.Save;
import org.adrianwalker.recipecosting.common.entity.Unit;

@Path("/unit")
public final class UnitResource extends AbstractRecipeCostingUserEntityResource<Unit> {

  private static RecipeCostingUserEntityResourceDelegate<Unit> unitDelegate;
  static {
    EntityManagerFactory emf = PersistenceManager.INSTANCE.getEntityManagerFactory();
    unitDelegate = new RecipeCostingUserEntityResourceDelegate<Unit>(Unit.class, emf);
  }

  @Override
  public RecipeCostingUserEntityResourceDelegate<Unit> getDelegate() {
    return unitDelegate;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> read() throws Exception {

    return read("units", "name");
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> save(final Save<Unit> save) throws Exception {
    
    Map<String, Object> response = response();
    response.putAll(save(save, "Units saved"));
    response.putAll(read());

    return response;
  }
}
