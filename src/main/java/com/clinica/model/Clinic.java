package com.clinica.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

/**
 * Represents a Clinic entity in the system.
 * This entity is mapped to the "Clinic" table in the database.
 */
@Entity
@Table(name = "Clinic")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Relation(collectionRelation = "clinics")
public class Clinic extends RepresentationModel<Clinic> {
    /**
     * The unique identifier for the clinic.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The name of the clinic.
     */
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    /**
     * The address of the clinic.
     */
    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;

    /**
     * The city where the clinic is located.
     */
    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    /**
     * The pincode of the clinic's location.
     */
    @NotBlank(message = "Pincode is required")
    @Pattern(regexp = "\\d{5,6}", message = "Pincode must be 5 or 6 digits")
    private String pincode;

    /**
     * The phone number of the clinic.
     */
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "\\d{10}", message = "Phone must be 10 digits")
    private String phone;

    /**
     * The email address of the clinic.
     */
    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email must be valid")
    private String email;

    /**
     * List of doctor IDs associated with this clinic.
     */
    @ElementCollection
    private List<Long> doctorIds;

    /**
     * Constructor for creating a new Clinic without ID.
     *
     * @param name      the name of the clinic
     * @param address   the address of the clinic
     * @param city      the city of the clinic
     * @param pincode   the pincode of the clinic
     * @param phone     the phone number of the clinic
     * @param email     the email of the clinic
     * @param doctorIds list of doctor IDs
     */
    public Clinic(String name, String address, String city, String pincode, String phone, String email,
            List<Long> doctorIds) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.pincode = pincode;
        this.phone = phone;
        this.email = email;
        this.doctorIds = doctorIds;
    }

    /**
     * Constructor for creating a new Clinic without ID and doctors.
     *
     * @param name    the name of the clinic
     * @param address the address of the clinic
     * @param city    the city of the clinic
     * @param pincode the pincode of the clinic
     * @param phone   the phone number of the clinic
     * @param email   the email of the clinic
     */
    public Clinic(String name, String address, String city, String pincode, String phone, String email) {
        this(name, address, city, pincode, phone, email, null);
    }
}
