package com.hospital.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;


/**
 * User
 */
@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Size(min = 5, message = "{name.error.size}")
    @NotBlank(message="{name.error.blank}")
    private String name;
    
    @NotBlank(message = "{email.error}")
    private String email;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @NotNull(message="{date.error.null}")
    private Date birthdate;

    @NotNull(message = "genre.error")
    private Genre genre;

    @NotNull(message="type.error")
    private UserType type;
}