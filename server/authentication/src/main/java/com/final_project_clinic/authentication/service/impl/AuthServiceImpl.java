package com.final_project_clinic.authentication.service.impl;

import com.final_project_clinic.authentication.data.model.Patient;
import com.final_project_clinic.authentication.data.model.User;
import com.final_project_clinic.authentication.data.repository.PatientRepository;
import com.final_project_clinic.authentication.data.repository.UserRepository;
import com.final_project_clinic.authentication.service.AuthService;
import com.final_project_clinic.authentication.utils.PasswordUtils;
import com.final_project_clinic.authentication.exception.AuthException;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, PatientRepository patientRepository) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void login(String email, String password) throws AuthException {
        // Find the user from the database
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new AuthException("User not found, please input valid username");
        }

        // Compare the password
        if (!PasswordUtils.verifyPassword(password, user.getPassword())) {
            throw new AuthException("Password wrong, please try again");
        }
    }

    @Override
    public void register(User user) {
        user.setPassword(PasswordUtils.hashPassword(user.getPassword())); // Hash the password before saving
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());
        user.setCreatedBy(user.getEmail());
        User savedUser = userRepository.save(user);

        // Create a corresponding patient record
        if ("ADMIN".equals(savedUser.getRole())) {
            return; // Exit the method early if the user is an admin
        }        
        Patient patient = new Patient();
        patient.setNik(3174123411234L);
        patient.setUser_id(savedUser.getId());
        patient.setCreatedBy(savedUser.getEmail());
        patient.setCreatedTime(new Date());
        patientRepository.save(patient);
    }
}
