package com.liquidus.ibkrdasboardjee8.rest.auth;

import com.liquidus.ibkrdasboardjee8.rest.auth.enitity.User;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.security.enterprise.CallerPrincipal;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class CustomIdentityStore implements IdentityStore {
    Logger logger = Logger.getLogger(CustomIdentityStore.class.getName());
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public CredentialValidationResult validate(Credential credential) {
        if (credential instanceof UsernamePasswordCredential) {
            UsernamePasswordCredential usernamePasswordCredential = (UsernamePasswordCredential) credential;
            String username = usernamePasswordCredential.getCaller();
            String password = usernamePasswordCredential.getPasswordAsString();

            if (isValidUser(username, password)) {
                // create CallerPrincipal with the validated username
                CallerPrincipal callerPrincipal = new CallerPrincipal(username);
                logger.info("CallerPrincipal object: " + callerPrincipal.getName());
                return new CredentialValidationResult(callerPrincipal, new HashSet<>(List.of("USER")));
            }
        }
        return CredentialValidationResult.INVALID_RESULT;
    }

    private boolean isValidUser(String username, String password) {
        if (username.isEmpty()) {
            logger.warning("Username can't be empty...");
            return false;
        }
        User user = null;
        try {
            user = this.entityManager
                    .createQuery("select u from User u where u.username =:username", User.class)
                    .setParameter("username", username).getSingleResult();
        } catch (NoResultException e) {
            logger.warning("No user with such username: " + username);
        }
        if (user != null && user.getPassword().equals(password)) {
            logger.info("User has been verified...");
            return true;
        }
        return false;
    }
}
