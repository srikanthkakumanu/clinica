package com.srikanth.clinica.repos;

import java.util.List;
import com.srikanth.clinica.model.Doctor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor, Long> {
    List<Doctor> findByFirstName(String firstName);
    List<Doctor> findByPincode(String pincode);
}