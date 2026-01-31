package com.clinica.service;

import com.clinica.model.DoctorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Base64;
import java.util.List;

/**
 * Service for interacting with the Doctor microservice.
 */
@Service
@RequiredArgsConstructor
public class DoctorService {

    private final RestTemplate restTemplate;

    @Value("${doctor.service.url:http://localhost:9092}")
    private String doctorServiceUrl;

    @Value("${doctor.service.username:admin}")
    private String username;

    @Value("${doctor.service.password:password}")
    private String password;

    /**
     * Creates HTTP headers with Basic Authentication.
     *
     * @return HttpHeaders with auth
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);
        headers.set("Authorization", authHeader);
        return headers;
    }

    /**
     * Retrieves a doctor by ID from the doctor service.
     *
     * @param doctorId the ID of the doctor
     * @return the DoctorDTO if found, null otherwise
     */
    public DoctorDTO getDoctorById(Long doctorId) {
        try {
            String url = doctorServiceUrl + "/api/doctors/" + doctorId;
            HttpHeaders headers = createHeaders();
            RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
            ResponseEntity<DoctorDTO> response = restTemplate.exchange(requestEntity, DoctorDTO.class);
            return response.getBody();
        } catch (Exception e) {
            // Handle exceptions (e.g., doctor not found, service unavailable)
            return null;
        }
    }

    /**
     * Retrieves all doctors from the doctor service.
     *
     * @return list of all doctors
     */
    public List<DoctorDTO> getAllDoctors() {
        try {
            String url = doctorServiceUrl + "/api/doctors";
            HttpHeaders headers = createHeaders();
            RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
            ResponseEntity<List<DoctorDTO>> response = restTemplate.exchange(
                    requestEntity,
                    new ParameterizedTypeReference<List<DoctorDTO>>() {
                    });
            return response.getBody();
        } catch (Exception e) {
            return List.of();
        }
    }
}