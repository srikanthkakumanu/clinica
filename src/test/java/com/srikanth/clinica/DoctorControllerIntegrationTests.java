package com.srikanth.clinica;

import com.srikanth.clinica.model.Doctor;
import com.srikanth.clinica.repos.DoctorRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DoctorControllerIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DoctorRepo doctorRepo;

    @AfterEach
    void tearDown() {
        doctorRepo.deleteAll();
    }

    @Test
    void whenCreateDoctor_thenReturns201Created() {
        Doctor doctor = new Doctor("Srikanth", "Kakumanu", "Lakshmi Prasad Arcade", "Tenali", "522201");

        ResponseEntity<Doctor> response = restTemplate.postForEntity("/api/doctors", doctor, Doctor.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getFirstName()).isEqualTo("Srikanth");
    }

    @Test
    void whenGetDoctorById_withValidId_thenReturns200Ok() {
        Doctor doctor = new Doctor("Srikanth", "Kakumanu", "Lakshmi Prasad Arcade", "Tenali", "522201");
        Doctor savedDoctor = doctorRepo.save(doctor);

        ResponseEntity<Doctor> response = restTemplate.getForEntity("/api/doctors/" + savedDoctor.getId(), Doctor.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(savedDoctor.getId());
    }

    @Test
    void whenGetDoctorById_withInvalidId_thenReturns404NotFound() {
        ResponseEntity<Doctor> response = restTemplate.getForEntity("/api/doctors/999", Doctor.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void whenUpdateDoctor_thenReturns200Ok() {
        Doctor doctor = new Doctor("Srikanth", "Kakumanu", "Lakshmi Prasad Arcade", "Tenali", "522201");
        Doctor savedDoctor = doctorRepo.save(doctor);

        Doctor updatedDetails = new Doctor("S.K", "K", "New Address", "New City", "123456");
        HttpEntity<Doctor> requestUpdate = new HttpEntity<>(updatedDetails);

        ResponseEntity<Doctor> response = restTemplate.exchange("/api/doctors/" + savedDoctor.getId(), HttpMethod.PUT, requestUpdate, Doctor.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getFirstName()).isEqualTo("S.K");
        assertThat(response.getBody().getAddress()).isEqualTo("New Address");
    }

    @Test
    void whenDeleteDoctor_thenReturns204NoContent() {
        Doctor doctor = new Doctor("Srikanth", "Kakumanu", "Lakshmi Prasad Arcade", "Tenali", "522201");
        Doctor savedDoctor = doctorRepo.save(doctor);

        ResponseEntity<Void> response = restTemplate.exchange("/api/doctors/" + savedDoctor.getId(), HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(doctorRepo.findById(savedDoctor.getId())).isEmpty();
    }
}
