package com.clinica;

import com.clinica.controller.ClinicController;
import com.clinica.model.Clinic;
import com.clinica.repos.ClinicRepo;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ClinicRegistrationTests {

    private MockMvc mockMvc;

    @Mock
    private ClinicRepo clinicRepo;

    @InjectMocks
    private ClinicController clinicController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clinicController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void whenCreateClinic_thenReturns201Created() throws Exception {
        Clinic clinic = new Clinic("City Clinic", "Lakshmi Prasad Arcade", "Tenali", "522201", "9876543210",
                "clinic@city.com");
        Clinic savedClinic = new Clinic(1L, "City Clinic", "Lakshmi Prasad Arcade", "Tenali", "522201", "9876543210",
                "clinic@city.com", null);

        when(clinicRepo.save(ArgumentMatchers.any(Clinic.class))).thenReturn(savedClinic);

        mockMvc.perform(post("/api/clinics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clinic)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("City Clinic")))
                .andExpect(jsonPath("$.address", is("Lakshmi Prasad Arcade")));
    }

    @Test
    void whenGetClinicById_withValidId_thenReturns200Ok() throws Exception {
        Clinic clinic = new Clinic(1L, "City Clinic", "Lakshmi Prasad Arcade", "Tenali", "522201", "9876543210",
                "clinic@city.com", null);
        when(clinicRepo.findById(1L)).thenReturn(Optional.of(clinic));

        mockMvc.perform(get("/api/clinics/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("City Clinic")));
    }

    @Test
    void whenGetClinicById_withInvalidId_thenReturns404NotFound() throws Exception {
        when(clinicRepo.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clinics/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenFindAllWithPagination_thenReturnsPaginatedResponse() throws Exception {
        Clinic clinic1 = new Clinic(1L, "Clinic 1", "Address 1", "City 1", "11111", "1111111111", "c1@clinic.com",
                null);
        Clinic clinic2 = new Clinic(2L, "Clinic 2", "Address 2", "City 2", "22222", "2222222222", "c2@clinic.com",
                null);
        List<Clinic> clinics = List.of(clinic1, clinic2);
        Pageable pageable = PageRequest.of(0, 2);
        Page<Clinic> clinicPage = new PageImpl<>(clinics, pageable, clinics.size());

        when(clinicRepo.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(clinicPage);

        mockMvc.perform(get("/api/clinics?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalElements", is(2)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.size", is(2)))
                .andExpect(jsonPath("$.number", is(0)));
    }
}
