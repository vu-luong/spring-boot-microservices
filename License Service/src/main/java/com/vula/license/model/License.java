package com.vula.license.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class License {
    private int id;
    private String licenseId;
    private String description;
    private String organisationId;
    private String productName;
    private String licenseType;
}
