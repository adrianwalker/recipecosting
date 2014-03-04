package org.adrianwalker.recipecosting.server;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.adrianwalker.recipecosting.common.entity.UnitConversion;
import org.adrianwalker.recipecosting.common.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/unitconversion")
public final class UnitConversionResource extends AbstractResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(UnitConversionResource.class);
  private static RecipeCostingUserEntityResourceDelegate<UnitConversion> delegate;

  static {
    EntityManagerFactory emf = PersistenceManager.INSTANCE.getEntityManagerFactory();
    delegate = new RecipeCostingUserEntityResourceDelegate<UnitConversion>(UnitConversion.class, emf);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Override
  public Map<String, Object> read() throws Exception {

    Map<String, Object> response = response();
    response.put("unitConversions", delegate.read(getSessionUser(), "unitFrom.name"));

    return response;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Override
  public Map<String, Object> save(final Map<String, Object> save) throws Exception {

    User sessionUser = getSessionUser();

    delegate.update(sessionUser, (List<UnitConversion>) save.get("changed"));
    delegate.delete(sessionUser, (List<Long>) save.get("ids"));

    Map<String, Object> response = response("unit conversions saved");
    response.putAll(read());

    return response;
  }
}
