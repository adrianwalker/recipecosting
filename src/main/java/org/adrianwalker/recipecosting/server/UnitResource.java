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
import org.adrianwalker.recipecosting.common.entity.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/unit")
public final class UnitResource extends AbstractResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(UnitResource.class);
  private static RecipeCostingUserEntityResourceDelegate<Unit> unitsDelegate;

  static {
    EntityManagerFactory emf = PersistenceManager.INSTANCE.getEntityManagerFactory();
    unitsDelegate = new RecipeCostingUserEntityResourceDelegate<Unit>(Unit.class, emf);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> readUnits(
          @QueryParam("page")
          @DefaultValue("-1")
          final int page,
          @QueryParam("pageSize")
          @DefaultValue("-1")
          final int pageSize) throws Exception {

    LOGGER.info("page = " + page + " pageSize = " + pageSize);

    Map<String, Object> response = response();
    if (page < 0 || pageSize < 0) {
      response.put("units", unitsDelegate.read(getSessionUser(), "name"));
    } else {
      response.put("units", unitsDelegate.read(getSessionUser(), page, pageSize, "name"));
      response.put("pageCount", unitsDelegate.countPages(getSessionUser(), pageSize));
    }

    return response;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> updateUnits(final List<Unit> units) throws Exception {

    unitsDelegate.update(getSessionUser(), units);

    return response("units saved");
  }

  @DELETE
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Map<String, Object> deleteUnits(final List<Long> ids) throws Exception {

    unitsDelegate.delete(getSessionUser(), ids);

    return response("units deleted");
  }
}
