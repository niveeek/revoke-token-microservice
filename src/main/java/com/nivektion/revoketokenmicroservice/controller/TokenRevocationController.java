package com.nivektion.revoketokenmicroservice.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/tokens")
public class TokenRevocationController {

    @Value("${keycloak.auth-server-url}")
    private String keycloakAuthServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @PostMapping("/revoke")
    public ResponseEntity<String> revokeToken(@RequestParam String token) {
        String revokeUrl = keycloakAuthServerUrl + "/realms/" + realm + "/protocol/openid-connect/revoke";
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = new HashMap<>();
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        params.put("token", token);

        try {
            restTemplate.postForEntity(revokeUrl, params, String.class);
            return new ResponseEntity<>("Token revoked successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to revoke token: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
