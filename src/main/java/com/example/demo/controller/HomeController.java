package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@CrossOrigin(origins = "http://localhost:8081")
@Controller
@RequestMapping("/")
public class HomeController {

@Autowired
UserRepository userRepository;
  @GetMapping("/")
  public String home(Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String currentPrincipalName = auth.getName();
    User user = userRepository.getUserByUsername(currentPrincipalName);
    if (user!=null){
      model.addAttribute("user", user.getId());
    }
      return "home";
  }
  @GetMapping("/register")
  public String showRegistrationForm(Model model) {
    model.addAttribute("user", new User());
    return "signup_form";
}
}