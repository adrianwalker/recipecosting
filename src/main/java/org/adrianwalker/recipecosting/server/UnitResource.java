package org.adrianwalker.recipecosting.server;

import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.adrianwalker.recipecosting.common.entity.Unit;

@Path("/unit")
public final class UnitResource extends AbstractRecipeCostingUserEntityResource<Unit> {

  private static RecipeCostingUserEntityResourceDelegate<Unit> unitDelegate = new RecipeCostingUserEntityResourceDelegate<Unit>(Unit.class, PersistenceManager.INSTANCE.getEntityManagerFactory());

  @Override
  public RecipeCostingUserEntityResourceDelegate getDelegate() {
    return unitDelegate;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Override
  public Map<String, Object> read() throws Exception {

    return read("units", "name");
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Override
  public Map<String, Object> save(final Map<String, Object> save) throws Exception {

    return save(save, "units saved");
  }
}
