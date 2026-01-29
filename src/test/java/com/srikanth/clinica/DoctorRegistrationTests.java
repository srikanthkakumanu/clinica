package com.srikanth.clinica;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srikanth.clinica.controller.DoctorController;
import com.srikanth.clinica.model.Doctor;
import com.srikanth.clinica.repos.DoctorRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class DoctorRegistrationTests {

    private MockMvc mockMvc;

    @Mock
    private DoctorRepo doctorRepo;

    @InjectMocks
    private DoctorController doctorController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(doctorController).build();
    }

    @Test
    void whenCreateDoctor_thenReturns201Created() throws Exception {
        Doctor doctor = new Doctor("Srikanth", "Kakumanu", "Lakshmi Prasad Arcade", "Tenali", "522201");
        Doctor savedDoctor = new Doctor(1L, "Srikanth", "Kakumanu", "Lakshmi Prasad Arcade", "Tenali", "522201");

        when(doctorRepo.save(ArgumentMatchers.any(Doctor.class))).thenReturn(savedDoctor);

        mockMvc.perform(post("/api/doctors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Srikanth")))
                .andExpect(jsonPath("$.lastName", is("Kakumanu")));
    }

    @Test
    void whenGetDoctorById_withValidId_thenReturns200Ok() throws Exception {
        Doctor doctor = new Doctor(1L, "Srikanth", "Kakumanu", "Lakshmi Prasad Arcade", "Tenali", "522201");
        when(doctorRepo.findById(1L)).thenReturn(Optional.of(doctor));

        mockMvc.perform(get("/api/doctors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Srikanth")));
    }

    @Test
    void whenGetDoctorById_withInvalidId_thenReturns404NotFound() throws Exception {
        when(doctorRepo.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/doctors/1"))
                .andExpect(status().isNotFound());
    }
}
