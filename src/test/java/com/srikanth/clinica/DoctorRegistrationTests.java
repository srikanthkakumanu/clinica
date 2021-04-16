package com.srikanth.clinica;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import javax.print.attribute.standard.Media;

import com.srikanth.clinica.controller.DoctorController;
import com.srikanth.clinica.model.Doctor;
import com.srikanth.clinica.repos.DoctorRepo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import reactor.core.publisher.Mono;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = DoctorController.class)
@Import(Doctor.class)
public class DoctorRegistrationTests {

    @MockBean
    DoctorRepo repo;

    @Autowired
    private WebTestClient webclient;

    @Test
    void testCreateDoctor() {
        Doctor doctor = new Doctor("Srikanth", "Kakumanu", "Lakshmi Prasad Arcade", "Tenali", "522201");
        Mono<Doctor> doctorMono = Mono.just(doctor);

        Mockito.when(repo.save(doctor)).thenReturn(doctorMono.block());
        
        webclient.post()
                .uri("/api/doctors")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(doctor))
                .exchange()
                .expectStatus().isCreated();
        
        Mockito.verify(repo, times(1)).save(doctor);
    }
}
