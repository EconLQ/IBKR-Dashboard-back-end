package com.liquidus.ibkrdasboardjee8.dao;

import com.liquidus.ibkrdasboardjee8.entity.CustomContract;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.util.logging.Logger;

@Singleton
public class CustomContractDao implements CustomContractLocal {
    Logger logger = Logger.getLogger(CustomContractDao.class.getName());
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void saveContract(CustomContract contract) {
        if (contract == null) {
            logger.warning("Passed contract is null");
            return;
        }

        if (!isContractExists(contract)) {
            try {
                logger.info("Adding contract: " + contract.getSymbol() + " with contract ID: " + contract.getContractId());
                // persist (add) unique contracts to the table
                this.entityManager.persist(contract);
            } catch (PersistenceException | ConstraintViolationException e) {
                logger.severe("Duplicate entry for contract: " + contract.getSymbol() + ". Error: " + e.getMessage());
            }
        }
    }

    public CustomContract findContractByPositionId(int positionId) {
        CustomContract existingContract = null;
        try {
            existingContract = entityManager.createQuery("select c from CustomContract c where c.contractId =:contractId", CustomContract.class)
                    .setParameter("contractId", positionId)
                    .getSingleResult();
        } catch (NoResultException e) {
            logger.warning("No contract found with such conId: " + positionId);
        }
        return existingContract;
    }

    @Override
    public boolean isContractExists(CustomContract contract) {
        if (contract == null) {
            logger.warning("Passed contract is null");
            return false;
        }
        // check if the contract exists in the table
        CustomContract managedContract = null;
        try {
            managedContract = entityManager
                    .createQuery("SELECT p from CustomContract p where p.contractId = :contractId", CustomContract.class)
                    .setParameter("contractId", contract.getContractId())
                    .getSingleResult();
        } catch (NoResultException e) {
            logger.info("Contract with contractId: " + contract.getContractId() + " hasn't been found");
            return false;
        }

        return managedContract != null;
    }
}
