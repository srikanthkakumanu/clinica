package com.clinica.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinica.model.Clinic;

/**
 * Repository interface for Clinic entities.
 * Provides CRUD operations and custom queries for Clinic management.
 */
@Repository
public interface ClinicRepo extends JpaRepository<Clinic, Long> {
    /**
     * Finds clinics by pincode with pagination support.
     *
     * @param pincode  the pincode to search for
     * @param pageable pagination information
     * @return a page of clinics matching the pincode
     */
    Page<Clinic> findByPincode(String pincode, Pageable pageable);
}
