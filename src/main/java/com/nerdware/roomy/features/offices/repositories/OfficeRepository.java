package com.nerdware.roomy.features.offices.repositories;

import com.nerdware.roomy.domain.entities.Office;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OfficeRepository extends CrudRepository<Office, Integer> {

}
