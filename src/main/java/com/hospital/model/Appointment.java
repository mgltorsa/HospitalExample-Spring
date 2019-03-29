package com.hospital.model;

import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

/**
 * Appointment
 */
@Data
@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @NotNull(message="{user.error.null}")
    private User patient;

    @ManyToOne
    @NotNull(message="{user.error.null}")
    private User medic;

    @DateTimeFormat(pattern = "yyyy-MM-dd")  
    @NotNull(message="{date.error.null}")
    private Date date;


    @DateTimeFormat(pattern="HH:mm")
    @NotNull(message = "{time.error.null}")
    private LocalTime time;
   

}