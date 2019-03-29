package com.hospital.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.hospital.model.Appointment;
import com.hospital.model.User;
import com.hospital.model.UserType;
import com.hospital.repositories.AppointmentRepository;
import com.hospital.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * AppointmentController
 */
@Controller
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/create_appointment")
    public String createAppointment(Model model) {

        List<User> patients = userRepository.findByType(UserType.PATIENT);
        List<User> medics = userRepository.findByType(UserType.DOCTOR);

        if (!patients.isEmpty() && !medics.isEmpty()) {
            model.addAttribute("appointment", new Appointment());
            model.addAttribute("patients", patients);
            model.addAttribute("medics", medics);
            return "appointment_templates/add";

        }

        model.addAttribute("errMsg", "Deben existir pacientes y medicos en la base de datos");
        return "index";

    }

    @PostMapping(value = "/create_appointment")
    public String createAppointment(Model model, @Validated @ModelAttribute Appointment appointment,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            System.out.println("error adding");
            model.addAttribute("patients", userRepository.findByType(UserType.PATIENT));
            model.addAttribute("medics", userRepository.findByType(UserType.DOCTOR));
            return "appointment_templates/add";

        }
        appointmentRepository.save(appointment);
        return "redirect:/appointments";

    }

    @GetMapping(value = "/appointments")
    public String appointments(Model model) {
        model.addAttribute("appointments", appointmentRepository.findAll());
        return "appointment_templates/list";
    }

    @GetMapping(value = "/edit_appointment")
    public String editAppointment(Model model, @RequestParam Long id) {
        Appointment found = appointmentRepository.findById(id).get();
        if (found == null) {
            model.addAttribute("errMsg", "No se encontró cita con id - " + id);
            return "index";
        }
        model.addAttribute("appointment", found);
        List<User> patients = userRepository.findByType(UserType.PATIENT);
        List<User> medics = userRepository.findByType(UserType.DOCTOR);

        if (!patients.isEmpty() && !medics.isEmpty()) {
            model.addAttribute("appointment", found);
            model.addAttribute("patients", patients);
            model.addAttribute("medics", medics);
            return "appointment_templates/edit";

        }

        model.addAttribute("errMsg", "Deben existir pacientes y medicos en la base de datos");
        return "index";
    }

    @PostMapping(value = "/edit_appointment")
    public String postMethodName(Model model, @Validated @ModelAttribute Appointment appointment,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("patients", userRepository.findByType(UserType.PATIENT));
            model.addAttribute("medics", userRepository.findByType(UserType.DOCTOR));
            System.out.println("error editing");
            return "appointment_templates/edit";
        } else {
            return "redirect:appointments";
        }

    }

    @GetMapping(value = "/remove_appointment")
    public String removeAppointment(Model model, @RequestParam Long id) {

        if (!appointmentRepository.existsById(id)) {
            model.addAttribute("errMsg", "No se encontró cita con id - " + id);
            return "index";
        }
        appointmentRepository.deleteById(id);
        return "redirect:appointments";
    }

    @GetMapping(value = "/appointmentByUser")
    public String getAppointmentByUser(Model model, @RequestParam Long id) {

        User user = userRepository.findById(id).get();

        List<Appointment> appointments = new ArrayList<Appointment>();

        if (user.getType() == UserType.DOCTOR) {
            appointments = appointmentRepository.findByMedic(user);
        } else if (user.getType() == UserType.PATIENT) {
            appointments = appointmentRepository.findByPatient(user);
        }

        if (appointments.size() == 0) {
            model.addAttribute("errMsg", "No se encontraron citas para el usuario con id - " + id);
            return "index";
        }

        Comparator<Appointment> comparator = new Comparator<Appointment>() {

            @Override
            public int compare(Appointment o1, Appointment o2) {
                int dateComparisson = o1.getDate().compareTo(o2.getDate());
                if (dateComparisson == 0) {
                    return o1.getTime().compareTo(o2.getTime());
                }
                return dateComparisson;
            }
        };

        appointments.sort(comparator);
        model.addAttribute("appointments", appointments);

        return "appointment_templates/list";
    }

}