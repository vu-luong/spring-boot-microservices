package com.vula.organization.repository;

import com.vula.organization.model.Organization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository
    extends CrudRepository<Organization, String> {
}
