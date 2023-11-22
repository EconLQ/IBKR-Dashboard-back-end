package com.liquidus.ibkrdasboardjee8.rest;

import javax.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/ibkr-dashboard/api")
@BasicAuthenticationMechanismDefinition(
        realmName = "local"
)
public class IBKRDashboardRestApp extends Application {
    public IBKRDashboardRestApp() {
    }
}
