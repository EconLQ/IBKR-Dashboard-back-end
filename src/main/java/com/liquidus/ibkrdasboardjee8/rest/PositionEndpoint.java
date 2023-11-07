package com.liquidus.ibkrdasboardjee8.rest;


import com.liquidus.ibkrdasboardjee8.dao.PositionLocal;
import com.liquidus.ibkrdasboardjee8.entity.Position;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.List;
import java.util.logging.Logger;

@RequestScoped
@Path("/positions")
@Produces("application/json")
@Consumes("application/json")
public class PositionEndpoint {
    @PersistenceContext
    EntityManager entityManager;
    @Inject
    PositionLocal positionBean;
    private Logger logger = Logger.getLogger(PositionEndpoint.class.getName());

    @Transactional
    @POST
    @Consumes("application/json")
    public Response createPosition(final Position position) {
        if (position == null) {
            logger.warning("[createPosition] Position can't be added as it's null");
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        this.positionBean.addPosition(position);

        UriBuilder uriBuilder = UriBuilder.fromPath("positions/{id}")
                .resolveTemplate("id", position.getContractId());

        return Response.created(uriBuilder.build()).build();
    }

    @GET
    @Path("/{cid:[0-9]+}")
    public Response findPositionByContractId(@PathParam("cid") final int cid) {
        Position position = this.positionBean.findPositionById(cid);

        if (position == null) {
            logger.warning("No position with such id: " + cid);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(position).build();
    }

    @GET
    public List<Position> listAllPositions(@QueryParam("start") final Integer startPosition, @QueryParam("max") final Integer maxResult) {
        final List<Position> positions = this.positionBean.getAllPositions();

        if (positions == null) {
            logger.warning("No positions found in this portfolio");
            return null;
        }
        return positions;
    }

    @Transactional
    @PUT
    @Path("/{cid:[0-9]+}")
    @Consumes("application/json")
    public Response updatePosition(@PathParam("cid") final int cid, final Position position) {
        if (position == null) {
            logger.severe("Failed to update position via [updatePosition] endpoint. Position is null");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        try {
            this.positionBean.updatePosition(position);
            logger.info("Updated: " + position.getTicker() + " position in the table");
            return Response.noContent().build();
        } catch (NoResultException e) {
            logger.warning("[updatePosition] No position found with such id: " + cid);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Transactional
    @DELETE
    @Path("/{cid:[0-9]+}")
    public Response deletePositionByContractId(@PathParam("cid") final int cid) {
        try {
            Position position = this.entityManager.createQuery("select p from Position p where p.contractId =:contractId", Position.class)
                    .setParameter("contractId", cid)
                    .getSingleResult();
            this.positionBean.deletePosition(position);
            logger.info("Position: " + position.getTicker() + " was deleted from the table");
            return Response.noContent().build();
        } catch (NoResultException e) {
            logger.warning("[deletePosition] No position found with such id: " + cid);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
