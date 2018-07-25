package com.ldapauth.demo.controllers;


import com.ldapauth.demo.entity.Role;
import com.ldapauth.demo.entity.User;
import com.ldapauth.demo.mail.EmailService;
import com.ldapauth.demo.mail.Mail;
import com.ldapauth.demo.repository.RoleRepository;
import com.ldapauth.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/")
public class LoginController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EmailService emailService;
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public ModelAndView Login() {
        return new ModelAndView("login");
    }

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public ModelAndView register() {

        return new ModelAndView("register","newUser",new User());
    }
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public ModelAndView addUser(@Valid @ModelAttribute("newGroup") User newUser, BindingResult bindingResult,Model model) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("register","newUser",new User());
        }

        Role role = roleRepository.getOne("USER");
        newUser.setCreated(new Date());
        newUser.setUpdated(new Date());
        newUser.setProfilePicture("/home/marwen/Bureau/uploads/user.jpg");
        newUser.setEnabled(false);
        newUser.setBiography("No Biography :(");
        newUser.setRole(role);
        userRepository.save(newUser);
        Mail mail = new Mail();
        mail.setFrom("marwenjaffel@gmail.com");
        mail.setTo(newUser.getEmail());
        mail.setSubject("Confirmation Mail");
        mail.setContent("http://localhost:8999/confirm/"+newUser.getUsername());
        emailService.sendSimpleMessage(mail);
        model.addAttribute("title","Confirmation email sent");
        model.addAttribute("message","Please check your email and confirm your account");
        ModelAndView modelAndView = new ModelAndView("login");
        return modelAndView;
    }
    @RequestMapping(value = "confirm/{username}", method = RequestMethod.GET)
    public String confirm(@PathVariable("username") String userName) {
        User user = userRepository.findByUsername(userName);
        user.setEnabled(true);
        user.setUpdated(new Date());
        userRepository.save(user);
        return "redirect:/login";
    }
  /*  @RequestMapping("/user")
    public Principal user(Principal principal){
        return principal;
    }*/
}