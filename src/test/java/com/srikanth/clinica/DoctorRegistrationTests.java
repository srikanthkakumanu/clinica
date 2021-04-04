package com.srikanth.clinica;

import com.srikanth.clinica.controller.DoctorController;

import org.junit.jupiter.api.Test;

public class DoctorRegistrationTests {
    @Test
    void testRegister() {
        System.out.println(DoctorController.register("Srikanth"));
    }

}
