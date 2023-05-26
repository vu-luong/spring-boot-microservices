package com.vula.license.controller;

import com.vula.license.model.License;
import com.vula.license.service.LicenseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "v1/organization/{organizationId}/license")
@AllArgsConstructor
public class LicenseController {

    private final LicenseService licenseService;

    @GetMapping(value = "/{licenseId}")
    public ResponseEntity<License> getLicense(
        @PathVariable("organizationId") String organizationId,
        @PathVariable("licenseId") String licenseId,
        @RequestParam("clientType") String clientType
    ) {
        License license = licenseService.getLicense(licenseId, organizationId, clientType);
        return ResponseEntity.ok(license);
    }

    @PutMapping
    public ResponseEntity<License> updateLicense(
        @RequestBody License request
    ) {
        return ResponseEntity.ok(
            licenseService.updateLicense(request)
        );
    }

    @PostMapping
    public ResponseEntity<License> createLicense(
        @RequestBody License request
    ) {
        return ResponseEntity.ok(
            licenseService.createLicense(request)
        );
    }

    @DeleteMapping(value = "/{licenseId}")
    public ResponseEntity<String> deleteLicense(
        @PathVariable("licenseId") String licenseId
    ) {
        return ResponseEntity.ok(
            licenseService.deleteLicense(licenseId)
        );
    }
}
