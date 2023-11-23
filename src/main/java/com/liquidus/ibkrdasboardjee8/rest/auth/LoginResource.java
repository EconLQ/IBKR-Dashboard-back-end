package com.liquidus.ibkrdasboardjee8.rest.auth;

import com.liquidus.ibkrdasboardjee8.rest.auth.enitity.User;
import com.liquidus.ibkrdasboardjee8.tws.OrderDataRetrieval;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.annotation.security.DeclareRoles;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Logger;

@Path("/login")
@DeclareRoles("USER")
public class LoginResource {
    Logger logger = Logger.getLogger(LoginResource.class.getName());
    @Inject
    private IdentityStore identityStore;
    @Inject
    private OrderDataRetrieval app;
    @Inject
    private UserBean userBean;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(final User loginRequest) {
        if (loginRequest == null) {
            logger.warning("Accepted user is null...");
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }

        CredentialValidationResult result = identityStore.validate(
                new UsernamePasswordCredential(loginRequest.getUsername(), loginRequest.getPassword()));

        if (result.getStatus() == CredentialValidationResult.Status.VALID) {
            logger.info("User: [" + loginRequest.getUsername() + "] has been authorized");

            // set IB's accountCode for that user
            app.setAccountCode(userBean.getAccountId(result.getCallerPrincipal().getName()));

            // generate JWT token to pass to client
            String token = generateJWT(result.getCallerPrincipal().getName());
            JsonObject responseJson = Json.createObjectBuilder()
                    .add("token", token)
                    .build();
            logger.info("Response JSON: " + responseJson);
            return Response.ok(responseJson).build();
        } else {
            logger.warning("User: [" + loginRequest.getUsername() + "] has NOT been authorized");
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    private String generateJWT(String username) {
        if (username.isEmpty()) {
            logger.warning("[generateJWT] Username is null");
            return null;
        }
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + ((24 * 60 * 60 + 1) * 1000))) // one-year + 1s for leap years
                .signWith(SignatureAlgorithm.HS512, generateSecureKey())
                .compact();
    }

    /**
     * Generate random secure key for JWT
     *
     * @return secret key that will be sign with SHA-512 algorithm during JWT generation
     */
    private String generateSecureKey() {
        String secretKey = "";
        try {
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            byte[] key = new byte[32]; // 256 bits
            secureRandom.nextBytes(key);
            secretKey = Base64.getEncoder().encodeToString(key);
        } catch (NoSuchAlgorithmException e) {
            logger.warning("Failed to generate Secret Key: " + e.getMessage());
        }
        if (secretKey.isEmpty()) {
            logger.warning("Empty secure key...");
        }
        return secretKey;
    }

}
