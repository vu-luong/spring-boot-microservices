package com.vula.license.service;

import com.vula.license.config.ServiceConfig;
import com.vula.license.model.License;
import com.vula.license.model.Organization;
import com.vula.license.repository.LicenseRepository;
import com.vula.license.service.client.OrganizationDiscoveryClient;
import com.vula.license.service.client.OrganizationFeignClient;
import com.vula.license.service.client.OrganizationRestTemplateClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LicenseService {

    @Autowired
    private MessageSource messages;

    @Autowired
    private LicenseRepository licenseRepository;
    
    @Autowired
    private OrganizationFeignClient organizationFeignClient;
    
    @Autowired
    private OrganizationDiscoveryClient organizationDiscoveryClient;
    
    @Autowired
    private OrganizationRestTemplateClient organizationRestClient;

    @Autowired
    private ServiceConfig config;

    public License getLicense(String licenseId, String organizationId, String clientType) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(
            organizationId, licenseId
        );
        if (license == null) {
            throw new IllegalArgumentException(
                String.format(
                    messages.getMessage("license.search.error.message", null, null),
                    licenseId,
                    organizationId
                )
            );
        }
        
        Organization organization = retrieveOrganizationInfo(organizationId, clientType);
        if (organization != null) {
            license.setOrganizationName(organization.getName());
            license.setContactName(organization.getContactName());
            license.setContactEmail(organization.getContactEmail());
            license.setContactPhone(organization.getContactPhone());
        }
        return license.withComment(config.getProperty());
    }

    private Organization retrieveOrganizationInfo(String organizationId, String clientType) {
        Organization organization = null;

        switch (clientType) {
            case "feign":
                System.out.println("I am using the feign client");
                organization = organizationFeignClient.getOrganization(organizationId);
                break;
            case "rest":
                System.out.println("I am using the rest client");
                organization = organizationRestClient.getOrganization(organizationId);
                break;
            case "discovery":
                System.out.println("I am using the discovery client");
                organization = organizationDiscoveryClient.getOrganization(organizationId);
                break;
            default:
                organization = organizationRestClient.getOrganization(organizationId);
        }

        return organization;
    }

    public License createLicense(License license) {
        license.setLicenseId(UUID.randomUUID().toString());
        licenseRepository.save(license);
        return license.withComment(config.getProperty());
    }

    public License updateLicense(License license) {
        licenseRepository.save(license);
        return license.withComment(config.getProperty());
    }

    public String deleteLicense(String licenseId) {
        String responseMessage;
        License license = new License();
        license.setLicenseId(licenseId);
        licenseRepository.delete(license);
        responseMessage = String.format(
            messages.getMessage("license.delete.message", null, null),
            licenseId
        );
        return responseMessage;
    }
}
