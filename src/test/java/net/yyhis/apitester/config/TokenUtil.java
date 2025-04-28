package net.yyhis.apitester.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import org.springframework.security.oauth2.jwt.Jwt;

public class TokenUtil {
    public static Jwt generateDummyKeycloakToken() {
        return Jwt.withTokenValue("token")
            .header("alg", "none")
            .claim("sub", UUID.randomUUID().toString())
            .claim("scope","openid profile email")
            .claim("realm_access", Collections.singletonMap("roles", Arrays.asList("offline_access", "default-roles-dev-realm", "uma_authorization", "TEST")))
            .claim("preferred_username", "test")
            .claim("name", "John Doe")  
            .claim("email", "test@example.com")  
            .build();
    }
}
