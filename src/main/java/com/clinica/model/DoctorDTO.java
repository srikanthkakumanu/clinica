package com.clinica.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Doctor information retrieved from the doctor
 * service.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String pincode;
}