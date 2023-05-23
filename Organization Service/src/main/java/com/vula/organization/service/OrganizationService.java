package com.vula.organization.service;

import com.vula.organization.model.Organization;
import com.vula.organization.repository.OrganizationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrganizationService {
 
    private final OrganizationRepository organizationRepository;
    
    public Organization findById(String organizationId) {
        Optional<Organization> opt = organizationRepository.findById(organizationId);
        return opt.orElse(null);
    }
    
    public Organization create(Organization organization) {
        organization.setId(UUID.randomUUID().toString());
        organization = organizationRepository.save(organization);
        return organization;
    }
    
    public void update(Organization organization) {
        organizationRepository.save(organization);
    }
    
    public void delete(Organization organization) {
        organizationRepository.deleteById(organization.getId());
    }
}
