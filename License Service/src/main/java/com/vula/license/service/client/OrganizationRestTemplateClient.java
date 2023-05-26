package com.vula.license.service.client;

import com.vula.license.model.Organization;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@AllArgsConstructor
public class OrganizationRestTemplateClient {
    
    private final RestTemplate restTemplate;

    public Organization getOrganization(String organizationId){
        ResponseEntity<Organization> restExchange =
            restTemplate.exchange(
                "http://organization-service/v1/organization/{organizationId}",
                HttpMethod.GET,
                null, Organization.class, organizationId);

        return restExchange.getBody();
    }
}
