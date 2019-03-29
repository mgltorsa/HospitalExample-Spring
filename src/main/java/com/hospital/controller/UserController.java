package com.hospital.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import com.hospital.model.Genre;
import com.hospital.model.User;
import com.hospital.model.UserType;
import com.hospital.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * AppController
 */
@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {

        // Patients
        User p1 = new User();

        p1.setName("Adella Haley");
        p1.setEmail("Adella.Ledner@hotmail.com");
        p1.setBirthdate(new Date("1999/08/02"));
        p1.setGenre(Genre.F);
        p1.setType(UserType.PATIENT);

        User p2 = new User();
        p2.setName("Mario Joaquie");
        p2.setEmail("Misty98@yahoo.com");
        p2.setBirthdate(new Date("1999/7/13"));
        p2.setGenre(Genre.M);
        p2.setType(UserType.PATIENT);

        User p3 = new User();
        p3.setName("Paul Mirage");
        p3.setEmail("Maud38@yahoo.com");
        p3.setBirthdate(new Date("1970/5/15"));
        p3.setGenre(Genre.M);
        p3.setType(UserType.PATIENT);

        userRepository.save(p1);
        userRepository.save(p2);
        userRepository.save(p3);

        // MEDICS
        User m1 = new User();

        m1.setName("David Nielse");
        m1.setEmail("Davin.Hansen68@gmail.com");
        m1.setBirthdate(new Date("1985/2/13"));
        m1.setGenre(Genre.M);
        m1.setType(UserType.DOCTOR);

        User m2 = new User();
        m2.setName("Maria Salera");
        m2.setEmail("Salma12@gmail.com");
        m2.setBirthdate(new Date("1980/11/24"));
        m2.setGenre(Genre.F);
        m2.setType(UserType.DOCTOR);

        User m3 = new User();
        m3.setName("Carlos Liason");
        m3.setEmail("Cali82@yahoo.com");
        m3.setBirthdate(new Date("1992/3/18"));
        m3.setGenre(Genre.M);
        m3.setType(UserType.DOCTOR);

        userRepository.save(m1);
        userRepository.save(m2);
        userRepository.save(m3);

    }

    @GetMapping("/")
    public String getIndex() {
        return "index";
    }

    @GetMapping("/create")
    public String saveUser(Model model) {

        model.addAttribute("user", new User());
        model.addAttribute("types", UserType.values());
        model.addAttribute("genres", Genre.values());
        return "user_templates/add";
    }

    @PostMapping("/create")
    public String saveUser(Model model, @Validated @ModelAttribute User user, BindingResult bindingResult) {
        if (bindingResult.getErrorCount() > 0) {
            System.out.println("has errors saving");
            model.addAttribute("types", UserType.values());
            model.addAttribute("genres", Genre.values());
            return "user_templates/add";
        } else {

            userRepository.save(user);
            return "redirect:users";
        }
    }

    @GetMapping(value = "/usersBy")
    public ModelAndView userList(@RequestParam String name) {
        List<User> users = userRepository.findByName(name);
        return new ModelAndView("user_templates/list", "users", users);
    }

    @GetMapping(value = "/users")
    public ModelAndView getUsers() {
        return new ModelAndView("user_templates/list", "users", userRepository.findAll());
    }

    @GetMapping(value = "/edit")
    public String editUser(Model model, @RequestParam Long id) {
        User user = userRepository.findById(id).get();
        if (user == null) {
            model.addAttribute("errMsg", "No se encontró usuario con id - " + id);
            return "index";
        }
        model.addAttribute("user", user);
        model.addAttribute("genres", Genre.values());
        return "user_templates/edit";

    }

    @PostMapping(value = "/edit")
    public String editUser(Model model, @Validated @ModelAttribute User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("genres", Genre.values());
            return "user_templates/edit";
        } else {
            userRepository.save(user);
            return "redirect:users";
        }
    }

    @GetMapping(value = "/remove")
    public String removeUser(Model model, @RequestParam Long id) {
        if (!userRepository.existsById(id)) {
            model.addAttribute("errMsg", "No se encontró usuario con id - " + id);
            return "index";
        }
        try {
            userRepository.deleteById(id);
            model.addAttribute("users", userRepository.findAll());
        } catch (Exception e) {
            model.addAttribute("errMsg", "Existen citas asociadas a el usuario, debe eliminar primero las citas");
            model.addAttribute("users",userRepository.findAll());
            return "user_templates/list";
        }

        return "redirect:users";
    }

}