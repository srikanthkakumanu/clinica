package com.clinica.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.clinica.model.Clinic;
import com.clinica.model.ClinicDTO;
import com.clinica.model.DoctorDTO;
import com.clinica.repos.ClinicRepo;
import com.clinica.service.DoctorService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for managing Clinic entities with HATEOAS support.
 * Provides secure endpoints for CRUD operations on clinics, including:
 * - Pagination and filtering capabilities
 * - Doctor association management via REST integration
 * - Hypermedia links for API discoverability
 * - Comprehensive input validation
 * - HTTP Basic Authentication required for all operations
 *
 * All endpoints return HATEOAS-enabled responses with navigation links.
 * Input validation is enforced using Bean Validation annotations.
 *
 * @author Clinica Development Team
 * @version 1.0
 * @since 2026-01-31
 */
@RestController
@RequestMapping("/api/clinics")
@RequiredArgsConstructor
@Tag(name = "Clinic API", description = "Secure API for managing clinics with HATEOAS support")
public class ClinicController {

    private final ClinicRepo clinicRepo;
    private final DoctorService doctorService;

    /**
     * Creates a new clinic.
     *
     * @param clinic the clinic data to create
     * @return ResponseEntity with the created clinic and HTTP status 201
     */
    @Operation(summary = "Create a new clinic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Clinic created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<Clinic> createClinic(@Valid @RequestBody Clinic clinic) {
        try {
            Clinic newClinic = new Clinic(clinic.getName(), clinic.getAddress(), clinic.getCity(), clinic.getPincode(),
                    clinic.getPhone(), clinic.getEmail());
            Clinic savedClinic = clinicRepo.save(newClinic);
            Clinic clinicWithLinks = addLinks(savedClinic);
            return new ResponseEntity<>(clinicWithLinks, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves clinics, optionally filtered by pincode, with pagination.
     *
     * @param pincode  optional pincode filter
     * @param pageable pagination parameters
     * @return ResponseEntity with a page of clinics
     */
    @Operation(summary = "Find clinics by pincode or get all clinics with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved clinics"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<Clinic>> findClinics(@RequestParam(required = false) String pincode, Pageable pageable) {
        try {
            Page<Clinic> clinics;
            if (pincode == null) {
                clinics = clinicRepo.findAll(pageable);
            } else {
                clinics = clinicRepo.findByPincode(pincode, pageable);
            }
            return new ResponseEntity<>(clinics, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves a clinic by its ID.
     *
     * @param id the ID of the clinic
     * @return ResponseEntity with the clinic if found, or 404 if not
     */
    @Operation(summary = "Get a clinic by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved clinic"),
            @ApiResponse(responseCode = "404", description = "Clinic not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Clinic> getClinicById(@PathVariable("id") long id) {
        return clinicRepo.findById(id)
                .map(this::addLinks)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Updates an existing clinic's details.
     *
     * @param id            the ID of the clinic to update
     * @param clinicDetails the updated clinic data
     * @return ResponseEntity with the updated clinic or 404 if not found
     */
    @Operation(summary = "Update a clinic's details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clinic updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Clinic not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Clinic> updateClinic(@PathVariable("id") long id, @Valid @RequestBody Clinic clinicDetails) {
        return clinicRepo.findById(id)
                .map(clinic -> {
                    clinic.setName(clinicDetails.getName());
                    clinic.setAddress(clinicDetails.getAddress());
                    clinic.setCity(clinicDetails.getCity());
                    clinic.setPincode(clinicDetails.getPincode());
                    clinic.setPhone(clinicDetails.getPhone());
                    clinic.setEmail(clinicDetails.getEmail());
                    Clinic updatedClinic = clinicRepo.save(clinic);
                    return new ResponseEntity<>(addLinks(updatedClinic), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Adds a doctor to a clinic.
     *
     * @param clinicId the ID of the clinic
     * @param doctorId the ID of the doctor to add
     * @return ResponseEntity with the updated clinic or 404 if not found
     */
    @Operation(summary = "Add a doctor to a clinic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor added successfully"),
            @ApiResponse(responseCode = "404", description = "Clinic or doctor not found")
    })
    @PostMapping("/{clinicId}/doctors/{doctorId}")
    public ResponseEntity<Clinic> addDoctorToClinic(@PathVariable("clinicId") long clinicId,
            @PathVariable("doctorId") long doctorId) {
        Clinic clinic = clinicRepo.findById(clinicId).orElse(null);
        if (clinic == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Check if doctor exists
        DoctorDTO doctor = doctorService.getDoctorById(doctorId);
        if (doctor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (clinic.getDoctorIds() == null) {
            clinic.setDoctorIds(List.of());
        }
        if (!clinic.getDoctorIds().contains(doctorId)) {
            clinic.getDoctorIds().add(doctorId);
            clinicRepo.save(clinic);
        }
        return ResponseEntity.ok(addLinks(clinic));
    }

    /**
     * Removes a doctor from a clinic.
     *
     * @param clinicId the ID of the clinic
     * @param doctorId the ID of the doctor to remove
     * @return ResponseEntity with the updated clinic or 404 if not found
     */
    @Operation(summary = "Remove a doctor from a clinic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor removed successfully"),
            @ApiResponse(responseCode = "404", description = "Clinic not found")
    })
    @DeleteMapping("/{clinicId}/doctors/{doctorId}")
    public ResponseEntity<Clinic> removeDoctorFromClinic(@PathVariable("clinicId") long clinicId,
            @PathVariable("doctorId") long doctorId) {
        Clinic clinic = clinicRepo.findById(clinicId).orElse(null);
        if (clinic == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (clinic.getDoctorIds() != null) {
            clinic.getDoctorIds().remove(doctorId);
            clinicRepo.save(clinic);
        }
        return ResponseEntity.ok(addLinks(clinic));
    }

    /**
     * Deletes a clinic by its ID.
     *
     * @param id the ID of the clinic to delete
     * @return ResponseEntity with HTTP status 204 or 404 if not found
     */
    @Operation(summary = "Delete a clinic by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Clinic deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Clinic not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteClinic(@PathVariable("id") long id) {
        try {
            if (!clinicRepo.existsById(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            clinicRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes all clinics.
     *
     * @return ResponseEntity with HTTP status 204
     */
    @Operation(summary = "Delete all clinics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "All clinics deleted successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAllClinics() {
        try {
            clinicRepo.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Adds HATEOAS (Hypermedia As The Engine Of Application State) links to a
     * Clinic entity.
     * This method enhances the clinic resource with navigational links for API
     * discoverability.
     *
     * Generated Links:
     * - self: Link to retrieve the current clinic
     * - update: Link to update the clinic (PUT operation)
     * - delete: Link to delete the clinic (DELETE operation)
     * - all-clinics: Link to retrieve all clinics
     * - remove-doctor-{id}: Dynamic links to remove associated doctors (one per
     * doctor)
     *
     * These links enable clients to discover available operations without prior
     * knowledge
     * of the API structure, following RESTful HATEOAS principles.
     *
     * @param clinic the clinic entity to enhance with links
     * @return Clinic entity with HATEOAS links added
     */
    private Clinic addLinks(Clinic clinic) {
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClinicController.class)
                .getClinicById(clinic.getId())).withSelfRel();
        Link updateLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClinicController.class)
                .updateClinic(clinic.getId(), clinic)).withRel("update");
        Link deleteLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClinicController.class)
                .deleteClinic(clinic.getId())).withRel("delete");
        Link allClinicsLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClinicController.class)
                .findClinics(null, null)).withRel("all-clinics");

        clinic.add(selfLink, updateLink, deleteLink, allClinicsLink);

        // Add doctor-related links if clinic has doctors
        if (clinic.getDoctorIds() != null && !clinic.getDoctorIds().isEmpty()) {
            for (Long doctorId : clinic.getDoctorIds()) {
                Link removeDoctorLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClinicController.class)
                        .removeDoctorFromClinic(clinic.getId(), doctorId)).withRel("remove-doctor-" + doctorId);
                clinic.add(removeDoctorLink);
            }
        }

        return clinic;
    }

    /**
     * Converts a Clinic entity to ClinicDTO with associated doctor details.
     * This method fetches doctor information from the external doctor service
     * for each doctor ID associated with the clinic.
     *
     * The conversion includes:
     * - Basic clinic information (id, name, address, etc.)
     * - Complete doctor details fetched via REST calls to doctor service
     * - Filtering of null/invalid doctor responses
     *
     * Note: This method makes external service calls and should be used
     * judiciously to avoid performance issues with large datasets.
     *
     * @param clinic the clinic entity to convert
     * @return ClinicDTO with populated doctor details
     */
    private ClinicDTO convertToDTO(Clinic clinic) {
        List<DoctorDTO> doctors = clinic.getDoctorIds() != null ? clinic.getDoctorIds().stream()
                .map(doctorService::getDoctorById)
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList()) : List.of();
        return new ClinicDTO(clinic.getId(), clinic.getName(), clinic.getAddress(),
                clinic.getCity(), clinic.getPincode(), clinic.getPhone(), clinic.getEmail(), doctors);
    }
}
