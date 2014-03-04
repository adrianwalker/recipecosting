package org.adrianwalker.recipecosting.server;

import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.adrianwalker.recipecosting.common.entity.UnitConversion;

@Path("/unitconversion")
public final class UnitConversionResource extends AbstractRecipeCostingUserEntityResource<UnitConversion> {

  private static RecipeCostingUserEntityResourceDelegate<UnitConversion> unitConversionDelegate;

  static {
    EntityManagerFactory emf = PersistenceManager.INSTANCE.getEntityManagerFactory();
    unitConversionDelegate = new RecipeCostingUserEntityResourceDelegate<UnitConversion>(UnitConversion.class, emf);
  }

  @Override
  public RecipeCostingUserEntityResourceDelegate<UnitConversion> getDelegate() {
    return unitConversionDelegate;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> read() throws Exception {

    return read("unitConversions", "unitFrom.name");
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> save(final Map<String, Object> save) throws Exception {

    return save(save, "unit conversions saved");
  }
}
