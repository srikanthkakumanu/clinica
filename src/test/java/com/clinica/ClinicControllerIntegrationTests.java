package com.clinica;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.clinica.model.Clinic;
import com.clinica.repos.ClinicRepo;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClinicControllerIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ClinicRepo clinicRepo;

    @AfterEach
    void tearDown() {
        clinicRepo.deleteAll();
    }

    private HttpHeaders createHeaders(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);
        return headers;
    }

    @Test
    void whenCreateClinic_thenReturns201Created() {
        Clinic clinic = new Clinic("City Clinic", "Lakshmi Prasad Arcade", "Tenali", "522201", "9876543210", "clinic@city.com");
        HttpEntity<Clinic> request = new HttpEntity<>(clinic, createHeaders("admin", "admin123"));

        ResponseEntity<Clinic> response = restTemplate.postForEntity("/api/clinics", request, Clinic.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("City Clinic");
    }

    @Test
    void whenGetClinicById_withValidId_thenReturns200Ok() {
        Clinic clinic = new Clinic("City Clinic", "Lakshmi Prasad Arcade", "Tenali", "522201", "9876543210", "clinic@city.com");
        Clinic savedClinic = clinicRepo.save(clinic);
        HttpEntity<?> request = new HttpEntity<>(createHeaders("admin", "admin123"));

        ResponseEntity<Clinic> response = restTemplate.exchange("/api/clinics/" + savedClinic.getId(), HttpMethod.GET, request, Clinic.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(savedClinic.getId());
    }

    @Test
    void whenGetClinicById_withInvalidId_thenReturns404NotFound() {
        HttpEntity<?> request = new HttpEntity<>(createHeaders("admin", "admin123"));
        ResponseEntity<Clinic> response = restTemplate.exchange("/api/clinics/999", HttpMethod.GET, request, Clinic.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void whenUpdateClinic_thenReturns200Ok() {
        Clinic clinic = new Clinic("City Clinic", "Lakshmi Prasad Arcade", "Tenali", "522201", "9876543210", "clinic@city.com");
        Clinic savedClinic = clinicRepo.save(clinic);

        Clinic updatedDetails = new Clinic("New Clinic", "New Address", "New City", "123456", "1234567890", "new@clinic.com");
        HttpEntity<Clinic> requestUpdate = new HttpEntity<>(updatedDetails, createHeaders("admin", "admin123"));

        ResponseEntity<Clinic> response = restTemplate.exchange("/api/clinics/" + savedClinic.getId(), HttpMethod.PUT, requestUpdate, Clinic.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("New Clinic");
        assertThat(response.getBody().getAddress()).isEqualTo("New Address");
    }

    @Test
    void whenDeleteClinic_thenReturns204NoContent() {
        Clinic clinic = new Clinic("City Clinic", "Lakshmi Prasad Arcade", "Tenali", "522201", "9876543210", "clinic@city.com");
        Clinic savedClinic = clinicRepo.save(clinic);

        HttpEntity<?> request = new HttpEntity<>(createHeaders("admin", "admin123"));
        ResponseEntity<Void> response = restTemplate.exchange("/api/clinics/" + savedClinic.getId(), HttpMethod.DELETE, request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(clinicRepo.findById(savedClinic.getId())).isEmpty();
    }
}
