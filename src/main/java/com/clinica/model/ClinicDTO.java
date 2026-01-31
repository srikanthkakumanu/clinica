package com.clinica.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for Clinic with associated doctors.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClinicDTO {
    private Long id;
    private String name;
    private String address;
    private String city;
    private String pincode;
    private String phone;
    private String email;
    private List<DoctorDTO> doctors;
}