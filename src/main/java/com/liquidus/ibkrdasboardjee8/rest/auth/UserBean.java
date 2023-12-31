package com.liquidus.ibkrdasboardjee8.rest.auth;

import com.liquidus.ibkrdasboardjee8.rest.auth.enitity.User;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.Base64;
import java.util.logging.Logger;

import static com.liquidus.ibkrdasboardjee8.rest.auth.util.PasswordHashingUtil.hashPassword;


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

    public void addUser(User user) {
        if (user == null) {
            logger.warning("[userBean] Passed user is null");
            return;
        }
        if (!findUserByUsername(user.getUsername())) {
            // get array which contains hashed password, salt and iterations
            String[] password = hashPassword(user.getPassword());
            // set password
            user.setPassword(Base64.getEncoder().encodeToString(password[0].getBytes()));
            // set salt
            user.setSalt(password[1]);
            // set iterations
            user.setIterations(Integer.parseInt(password[2]));
            entityManager.merge(user);
        }
    }

    public boolean findUserByUsername(String username) {
        if (username.isEmpty()) {
            logger.warning("[userBean] Passed username is null");
            return false;
        }

        User user;
        try {
            user = entityManager.createQuery("select u from User u where u.username =:username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            logger.warning("No user with such username: " + username);
            return false;
        }
        return user != null;
    }
}
