package com.srikanth.clinica.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.print.Doc;

import com.srikanth.clinica.model.Doctor;
import com.srikanth.clinica.repos.DoctorRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//@CrossOrigin(origins = "http://localhost:9091", maxAge = 3600)
@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    
    @Autowired
    DoctorRepo doctorRepo;

    // @GetMapping("/doctors/{id}")
    // public ResponseEntity<Doctor> getDoctorById(@PathVariable("id") long id) {
    //     Optional<Doctor> doctor = doctorRepo.findById(id);
    //     if(doctor.isPresent())
    //         return new ResponseEntity<>(doctor.get(), HttpStatus.OK);
    //     else
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    // }

    @PostMapping
    public ResponseEntity<Doctor> create(@RequestBody Doctor doctor) {
        try {
            Doctor _doctor = doctorRepo.save(doctor);
            return new ResponseEntity<>(_doctor, HttpStatus.CREATED);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> update(@PathVariable("id") long id, @RequestBody Doctor doctor) {
        Optional<Doctor> foundDoctor = doctorRepo.findById(id);
        if(foundDoctor.isPresent()) {
            Doctor _doctor = foundDoctor.get();
            _doctor.setFirstName(doctor.getFirstName());
            _doctor.setLastName(doctor.getLastName());
            _doctor.setAddress(doctor.getAddress());
            _doctor.setCity(doctor.getCity());
            _doctor.setPincode(doctor.getPincode());
            return new ResponseEntity<>(doctorRepo.save(_doctor), HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") long id) {
        try {
            doctorRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> delete() {
        try {
            doctorRepo.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        try {
            List<Doctor> doctors = new ArrayList<Doctor>();
            doctorRepo.findAll().forEach(doctors::add);
            
            if(doctors.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            
            return new ResponseEntity<>(doctors, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{pincode}")
    public ResponseEntity<List<Doctor>> findByPincode(@PathVariable("pincode") String pincode) {
        try {
            List<Doctor> doctors = doctorRepo.findByPincode(pincode);
            if(doctors.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            else
                return new ResponseEntity<>(doctors, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}