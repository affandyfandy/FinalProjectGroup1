package com.final_project_clinic.doctor.service;

import com.final_project_clinic.doctor.data.model.Doctor;
import com.final_project_clinic.doctor.data.repository.DoctorRepository;
import com.final_project_clinic.doctor.dto.DoctorDTO;
import com.final_project_clinic.doctor.exception.DuplicateIdentificationNumberException;
import com.final_project_clinic.doctor.mapper.DoctorMapper;
import com.final_project_clinic.doctor.service.impl.DoctorServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceImplTest {

    @InjectMocks
    private DoctorServiceImpl doctorService;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorMapper doctorMapper;

    @Test
    void getAllDoctors_ReturnsDoctorsPageTest() {
        Doctor doctor = new Doctor();
        DoctorDTO doctorDTO = new DoctorDTO();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Doctor> doctors = new PageImpl<>(Collections.singletonList(doctor));

        when(doctorRepository.findAll(pageable)).thenReturn(doctors);
        when(doctorMapper.toDTO(doctor)).thenReturn(doctorDTO);

        Page<DoctorDTO> result = doctorService.getAllDoctors(pageable);

        assertEquals(1, result.getTotalElements());

        verify(doctorRepository, times(1)).findAll(pageable);
        verify(doctorMapper, times(1)).toDTO(doctor);
    }

    @Test
    void getAllDoctorsList_ReturnsDoctorsListTest() {
        Doctor doctor = new Doctor();
        DoctorDTO doctorDTO = new DoctorDTO();

        when(doctorRepository.findAll()).thenReturn(Collections.singletonList(doctor));
        when(doctorMapper.toDTO(doctor)).thenReturn(doctorDTO);

        var result = doctorService.getAllDoctorsList();

        assertEquals(1, result.size());

        verify(doctorRepository, times(1)).findAll();
        verify(doctorMapper, times(1)).toDTO(doctor);
    }

    @Test
    void getDoctorById_ReturnsDoctorTest() {
        UUID id = UUID.randomUUID();
        Doctor doctor = new Doctor();
        DoctorDTO doctorDTO = new DoctorDTO();

        when(doctorRepository.findById(id)).thenReturn(Optional.of(doctor));
        when(doctorMapper.toDTO(doctor)).thenReturn(doctorDTO);

        Optional<DoctorDTO> result = doctorService.getDoctorById(id);

        assertTrue(result.isPresent());

        verify(doctorRepository, times(1)).findById(id);
        verify(doctorMapper, times(1)).toDTO(doctor);
    }

    @Test
    void getDoctorById_ReturnsEmptyWhenNotFoundTest() {
        UUID id = UUID.randomUUID();

        when(doctorRepository.findById(id)).thenReturn(Optional.empty());

        Optional<DoctorDTO> result = doctorService.getDoctorById(id);

        assertFalse(result.isPresent());

        verify(doctorRepository, times(1)).findById(id);
    }

    @Test
    void createDoctor_SavesAndReturnsDoctorTest() {
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setIdentificationNumber("123456");
        doctorDTO.setPhoneNumber("+6283929288327");
        Doctor doctor = new Doctor();

        when(doctorRepository.findByIdentificationNumber(doctorDTO.getIdentificationNumber())).thenReturn(null);
        when(doctorMapper.toEntity(doctorDTO)).thenReturn(doctor);
        when(doctorRepository.save(doctor)).thenReturn(doctor);
        when(doctorMapper.toDTO(doctor)).thenReturn(doctorDTO);

        DoctorDTO result = doctorService.createDoctor(doctorDTO);

        assertEquals(doctorDTO, result);

        verify(doctorRepository, times(1)).findByIdentificationNumber(doctorDTO.getIdentificationNumber());
        verify(doctorMapper, times(1)).toEntity(doctorDTO);
        verify(doctorRepository, times(1)).save(doctor);
        verify(doctorMapper, times(1)).toDTO(doctor);
    }

    @Test
    void createDoctor_ThrowsExceptionWhenIdentificationNumberExistsTest() {
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setIdentificationNumber("123456");
        doctorDTO.setPhoneNumber("+6283929288327");
        Doctor existingDoctor = new Doctor();

        when(doctorRepository.findByIdentificationNumber(doctorDTO.getIdentificationNumber())).thenReturn(existingDoctor);

        assertThrows(DuplicateIdentificationNumberException.class, () -> doctorService.createDoctor(doctorDTO));

        verify(doctorRepository, times(1)).findByIdentificationNumber(doctorDTO.getIdentificationNumber());
        verify(doctorRepository, never()).save(any());
    }

    @Test
    void editDoctor_UpdatesAndReturnsDoctorTest() {
        UUID id = UUID.randomUUID();
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setName("Updated Name");
        doctorDTO.setPhoneNumber("+628392918372");
        Doctor doctor = new Doctor();

        when(doctorRepository.findById(id)).thenReturn(Optional.of(doctor));
        when(doctorMapper.toDTO(doctor)).thenReturn(doctorDTO);

        Optional<DoctorDTO> result = doctorService.editDoctor(id, doctorDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", doctor.getName());

        verify(doctorRepository, times(1)).findById(id);
        verify(doctorRepository, times(1)).save(doctor);
        verify(doctorMapper, times(1)).toDTO(doctor);
    }

    @Test
    void editDoctor_ThrowsExceptionWhenIdentificationNumberExistsTest() {
        UUID id = UUID.randomUUID();
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setIdentificationNumber("12345");
        Doctor existingDoctor = new Doctor();
        existingDoctor.setId(UUID.randomUUID());
        Doctor doctor = new Doctor();
        doctor.setId(id);

        when(doctorRepository.findById(id)).thenReturn(Optional.of(doctor));
        when(doctorRepository.findByIdentificationNumber(doctorDTO.getIdentificationNumber())).thenReturn(existingDoctor);

        assertThrows(DuplicateIdentificationNumberException.class, () -> doctorService.editDoctor(id, doctorDTO));

        verify(doctorRepository, times(1)).findById(id);
        verify(doctorRepository, times(1)).findByIdentificationNumber(doctorDTO.getIdentificationNumber());
        verify(doctorRepository, never()).save(any());
    }

    @Test
    void deleteDoctor_DeletesDoctorTest() {
        UUID id = UUID.randomUUID();

        doNothing().when(doctorRepository).deleteById(id);

        doctorService.deleteDoctor(id);

        verify(doctorRepository, times(1)).deleteById(id);
    }
}
