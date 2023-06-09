package com.vula.license.service;

import com.vula.license.config.ServiceConfig;
import com.vula.license.model.License;
import com.vula.license.model.Organization;
import com.vula.license.repository.LicenseRepository;
import com.vula.license.service.client.OrganizationDiscoveryClient;
import com.vula.license.service.client.OrganizationFeignClient;
import com.vula.license.service.client.OrganizationRestTemplateClient;
import com.vula.license.utils.UserContextHolder;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.bulkhead.annotation.Bulkhead.Type;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

@Service
@AllArgsConstructor
public class LicenseService {

    private final ServiceConfig config;
    private final MessageSource messages;
    private final LicenseRepository licenseRepository;
    private final OrganizationFeignClient organizationFeignClient;
    private final OrganizationDiscoveryClient organizationDiscoveryClient;
    private final OrganizationRestTemplateClient organizationRestClient;

    private static final Logger logger = LoggerFactory.getLogger(LicenseService.class);

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

    @CircuitBreaker(name = "organizationService")
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

    @CircuitBreaker(name = "licenseService", fallbackMethod = "buildFallbackLicenseList")
    @RateLimiter(name = "licenseService", fallbackMethod = "buildFallbackLicenseList")
    @Retry(name = "retryLicenseService", fallbackMethod = "buildFallbackLicenseList")
    @Bulkhead(name = "bulkheadLicenseService", type=Type.THREADPOOL, fallbackMethod = "buildFallbackLicenseList")
    public CompletableFuture<List<License>> getLicensesByOrganization(String organizationId) throws TimeoutException {
        return CompletableFuture.supplyAsync(() -> {
            logger.debug(
                "getLicensesByOrganization Correlation id: {}",
                UserContextHolder.getContext().getCorrelationId()
            );
            try {
                randomlyRunLong();
            } catch (TimeoutException e) {
                throw new RuntimeException(e);
            }
            return licenseRepository.findByOrganizationId(organizationId);
        });
    }

    private void randomlyRunLong() throws TimeoutException {
        Random random = new Random();
        int randomNumber = random.nextInt(3) + 1;
        if (randomNumber != 3) {
            sleep();
        }
    }

    private void sleep() throws TimeoutException {
        try {
            Thread.sleep(5000);
            throw new TimeoutException();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }

    @SuppressWarnings("unused")
    private CompletableFuture<List<License>> buildFallbackLicenseList(String organizationId, Throwable t) {
        return CompletableFuture.supplyAsync(() -> {
            License license = new License();
            license.setLicenseId("0000000-00-00000");
            license.setOrganizationId(organizationId);
            license.setProductName("Sorry no licensing information currently available");
            return List.of(license);
        });
    }
}
