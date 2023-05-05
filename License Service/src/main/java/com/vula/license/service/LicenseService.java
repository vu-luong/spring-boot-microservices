package com.vula.license.service;

import com.vula.license.model.License;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class LicenseService {

    public License getLicense(String licenseId, String organizationId) {
        License license = new License();
        license.setId(new Random().nextInt(1000));
        license.setLicenseId(licenseId);
        license.setOrganisationId(organizationId);
        license.setDescription("Software product");
        license.setProductName("VSoft");
        license.setLicenseType("full");
        return license;
    }

    public String createLicense(License license, String organizationId) {
        String responseMessage = null;
        if (license != null) {
            license.setOrganisationId(organizationId);
            responseMessage = String.format("This is the post and the object is: %s", license);
        }
        return responseMessage;
    }

    public String updateLicense(License license, String organizationId) {
        String responseMessage = null;
        if (license != null) {
            license.setOrganisationId(organizationId);
            responseMessage = String.format("THis is the put and the object is: %s", license);
        }
        return responseMessage;
    }

    public String deleteLicense(String licenseId, String organizationId) {
        String responseMessage = null;
        responseMessage = String.format(
            "Deleting license with id %s for the organization %s",
            licenseId,
            organizationId
        );
        return responseMessage;
    }
}
