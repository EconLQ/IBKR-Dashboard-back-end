package com.liquidus.ibkrdasboardjee8.dao;

import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.liquidus.ibkrdasboardjee8.entity.Position;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Singleton
@LocalBean
public class PositionDao implements PositionLocal {
    private Logger logger = Logger.getLogger(PositionDao.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    private List<Position> positions = new ArrayList<>();


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

        // check if the position exists in the table
        Position managedPosition = entityManager
                .createQuery("SELECT p from Position p where p.contractId = :contractId", Position.class)
                .setParameter("contractId", position.getContractId())
                .getSingleResult();

        if (managedPosition != null) {
            logger.info("Position with such ID: " + position.getContractId() + " already exists");
            // Update existing position
            managedPosition.setPosition(position.getPosition());
            managedPosition.setUnrealizedPnL(position.getUnrealizedPnL());
            managedPosition.setRealizedPnL(position.getRealizedPnL());
            entityManager.merge(managedPosition);
        } else {
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
        return this.entityManager.find(Position.class, positionId);
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
    public void updatePosition(Position position) {
        if (position == null) {
            logger.warning("Passed position is null");
            return;
        }
        List<Position> positions = getAllPositions();
    }
}