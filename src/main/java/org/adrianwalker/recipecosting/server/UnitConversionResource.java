package org.adrianwalker.recipecosting.server;

import java.math.BigDecimal;
import java.util.HashMap;
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
import org.adrianwalker.recipecosting.common.entity.UnitConversion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/unitconversion")
public final class UnitConversionResource extends AbstractResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(UnitConversionResource.class);
  private static RecipeCostingUserEntityResourceDelegate<UnitConversion> unitConversionsDelegate;

  static {
    EntityManagerFactory emf = PersistenceManager.INSTANCE.getEntityManagerFactory();
    unitConversionsDelegate = new RecipeCostingUserEntityResourceDelegate<UnitConversion>(UnitConversion.class, emf);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> readUnitConversions(
    @QueryParam("page")
    @DefaultValue("-1")
    final int page,
    @QueryParam("pageSize")
    @DefaultValue("-1")
    final int pageSize) throws Exception {

    LOGGER.info("page = " + page + " pageSize = " + pageSize);

    Map<String, Object> map = new HashMap<String, Object>();
    if (page < 0 || pageSize < 0) {
      map.put("unitConversions", unitConversionsDelegate.read(getSessionUser(), "unitFrom.name"));
    } else {
      map.put("unitConversions", unitConversionsDelegate.read(getSessionUser(), page, pageSize, "unitFrom.name"));
      map.put("pageCount", unitConversionsDelegate.countPages(getSessionUser(), pageSize));
    }

    return map;
  }

  @GET
  @Path("/lookup")
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> readUnitConversionsLookup() throws Exception {

    Map<String, Object> map = new HashMap<String, Object>();

    List<UnitConversion> unitConversions = unitConversionsDelegate.read(getSessionUser());

    for (UnitConversion unitConversion : unitConversions) {
      Long unitFromId = unitConversion.getUnitFrom().getId();
      Long unitToId = unitConversion.getUnitTo().getId();
      BigDecimal ratio = unitConversion.getRatio();

      map.put(unitFromId + ":" + unitToId, ratio);

      if (ratio.signum() != 0) {
        map.put(unitToId + ":" + unitFromId, BigDecimal.ONE.divide(ratio));
      }
    }

    return map;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> updateUnitConversions(final List<UnitConversion> unitConversions) throws Exception {

    unitConversionsDelegate.update(getSessionUser(), unitConversions);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("message", "unit conversions saved");

    return map;
  }

  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> deleteUnitConversions(final List<Long> ids) throws Exception {

    unitConversionsDelegate.delete(getSessionUser(), ids);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("message", "unit conversions deleted");

    return map;
  }
}
