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
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

import static com.liquidus.ibkrdasboardjee8.rest.auth.util.PasswordHashingUtil.getUserPassword;
import static com.liquidus.ibkrdasboardjee8.rest.auth.util.PasswordHashingUtil.isMatched;

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
                logger.info("CallerPrincipal name: " + callerPrincipal.getName());
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

        if (user != null) {
            // get user's decoded password, salt and iterations
            String[] pwd = getUserPassword(username);
            assert pwd != null;
            // validate password from request with the one stored in DB
            if (isMatched(pwd[0], password, Base64.getDecoder().decode(pwd[1]), Integer.parseInt(pwd[2]))) {
                logger.info("User [" + username + "] has been verified...");
                return true;
            } else {
                logger.warning("Validation failed. Password doesn't match.");
                return false;
            }
        }
        return false;
    }
}
