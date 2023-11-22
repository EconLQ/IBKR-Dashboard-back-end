package com.liquidus.ibkrdasboardjee8.rest.auth;

import com.liquidus.ibkrdasboardjee8.rest.auth.enitity.User;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.logging.Logger;


@RequestScoped
public class UserBean implements Serializable {
    Logger logger = Logger.getLogger(UserBean.class.getName());
    @PersistenceContext
    private EntityManager entityManager;

    public String getAccountId(String username) {
        if (username == null) {
            logger.warning("Username is null");
            return null;
        }
        String accountId = null;
        try {
            accountId = entityManager.createQuery("select u from User u  where u.username =:username", User.class)
                    .setParameter("username", username)
                    .getSingleResult().getAccountId();
        } catch (NoResultException e) {
            logger.warning("No accountId found for such user: " + username);
        }

        if (accountId == null) {
            logger.warning("Failed to find this account id. Account Id is null...");
            return null;
        }
        return accountId;
    }

}
