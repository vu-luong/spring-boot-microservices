package com.vula.organization.controller;

import com.vula.organization.model.Organization;
import com.vula.organization.service.OrganizationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "v1/organization")
@AllArgsConstructor
public class OrganizationController {
    
    private final OrganizationService organizationService;
    
    @GetMapping(value = "/{organizationId}")
    public ResponseEntity<Organization> getOrganization(
        @PathVariable("organizationId") String organizationId
    ) {
        return ResponseEntity.ok(organizationService.findById(organizationId));
    }
    
    @PutMapping(value = "/{organizationId}")
    public void updateOrganization(
        @PathVariable("organizationId") String organizationId,
        @RequestBody Organization organization
    ) {
        organizationService.update(organization);
    }
    
    @PostMapping
    public ResponseEntity<Organization> createOrganization(
        @RequestBody Organization organization
    ) {
        return ResponseEntity.ok(organizationService.create(organization));
    }
    
    @DeleteMapping(value = "/{organizationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrganization(
        @PathVariable("organizationId") String organizationId,
        @RequestBody Organization organization
    ) {
        organizationService.delete(organization);
    }
}
