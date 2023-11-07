package com.liquidus.ibkrdasboardjee8.dao;

import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.liquidus.ibkrdasboardjee8.entity.Position;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.logging.Logger;

/**
 * PositionDao has a local view as it directly implements an interface which has {@link javax.ejb.Local} annotation
 */
@Singleton
public class PositionDao implements PositionLocal {
    private Logger logger = Logger.getLogger(PositionDao.class.getName());

    @PersistenceContext
    private EntityManager entityManager;


    public PositionDao() {
    }

    @Override
    public void savePosition(Position position) {
        if (position == null) {
            logger.warning("Passed position is null.");
            return;
        }
        logger.info("Saving modified position...");
        this.entityManager.merge(position);
    }

    /**
     * Adds position to the table before checking if it exists and updates quantity, unrealized and realized PnL.
     * Method is synchronized to avoid stacked calls from one thread to MySQL
     *
     * @param position that we get for {@link com.liquidus.ibkrdasboardjee8.tws.EWrapperImpl#position(String, Contract, Decimal, double)}
     */
    @Override
    public synchronized void addPosition(Position position) {
        if (position == null) {
            logger.warning("Passed position is null");
            return;
        }

        if (!updatePosition(position)) {
            try {
                logger.info("Adding position: " + position.getTicker() + " with contract ID: " + position.getContractId());
                // persist (add) unique position to the table
                this.entityManager.persist(position);
            } catch (PersistenceException | ConstraintViolationException e) {
                logger.severe("Duplicate entry for position: " + position.getTicker() + ". Error: " + e.getMessage());
            }
        }
    }

    @Override
    public Position findPositionById(int positionId) {
        try {
            return this.entityManager
                    .createQuery("select p from Position p where p.contractId =:contractId", Position.class)
                    .setParameter("contractId", positionId).getSingleResult();
        } catch (NoResultException e) {
            logger.warning("No entity with contractId as: " + positionId + " found for query");
            return null;
        }
    }

    @Override
    public List<Position> getAllPositions() {
        return this.entityManager
                .createQuery("select p from Position p", Position.class)
                .getResultList();
    }

    @Override
    public void deletePosition(Position position) {
        this.entityManager.remove(
                this.entityManager.contains(position)
                        ? position
                        : this.entityManager.merge(position)
        );
    }

    @Override
    public boolean updatePosition(Position position) {
        if (position == null) {
            logger.warning("Passed position is null");
            return false;
        }
        // check if the position exists in the table
        Position managedPosition;
        try {
            managedPosition = entityManager
                    .createQuery("SELECT p from Position p where p.contractId = :contractId", Position.class)
                    .setParameter("contractId", position.getContractId())
                    .getSingleResult();
        } catch (NoResultException e) {
            logger.info("Position with contractId: " + position.getContractId() + " hasn't been found");
            return false;
        }

        // Update existing position
        logger.info("Position exists. Updating: " + position.getTicker() + " with contractId: " + position.getContractId());
        managedPosition.setPosition(position.getPosition());
        managedPosition.setUnrealizedPnL(position.getUnrealizedPnL());
        managedPosition.setRealizedPnL(position.getRealizedPnL());
        managedPosition.setLastMarketPrice(position.getLastMarketPrice());
        entityManager.merge(managedPosition);

        return true;
    }
}