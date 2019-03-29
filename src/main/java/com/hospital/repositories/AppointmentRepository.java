package com.hospital.repositories;

import java.util.List;

import com.hospital.model.Appointment;
import com.hospital.model.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * AppointmentRepository
 * 
 * 
 */
@Repository
public interface AppointmentRepository extends CrudRepository<Appointment, Long> {
      
    List<Appointment> findByPatient(User patient);
    List<Appointment> findByMedic(User medic);
}