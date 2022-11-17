package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.User;
import com.example.demo.repository.MenuRepository;
import com.example.demo.repository.UserRepository;


@CrossOrigin(origins = "http://localhost:8081")
@Controller
@RequestMapping("/admin")



public class UserController {


  @Autowired
  UserRepository userRepository;
  @Autowired
  MenuRepository menuRepository;
  @GetMapping("/users")
  public String getAllUsers(@RequestParam(required = false) String title, Model model) {
    try {
      List<User> users = new ArrayList<User>();

      if (title == null)
        userRepository.findAll().forEach(users::add);
      else
       userRepository.findByName(title).forEach(users::add);

      if (users.isEmpty()) {
        return "users";
      }
      model.addAttribute("users", users);
      return "users";
    } catch (Exception e) {
      return "error";
    }
  }
 
  @GetMapping("/users/{id}")
  public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
    Optional<User> userData = userRepository.findById(id);

    if (userData.isPresent()) {
      return new ResponseEntity<>(userData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
  @GetMapping("/users/add")
  public String addUser(Model model) {
 
      return "nuevo_user";
  }


  @PostMapping("/users")
  public String createUser(@RequestParam String name, @RequestParam String surname, @RequestParam String username, @RequestParam String email, @RequestParam String password) {
    System.out.println("identificador de user");
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String encodedPassword = encoder.encode(password);
  
      User userRequest = new User(name, surname, username, email, encodedPassword);
      userRepository.save(userRequest);
      return "creado";  
    
  }
  @GetMapping("/users/{id}/edit")
  public String getUserById(@PathVariable("id") long id, Model model) {
    Optional<User> userData = userRepository.findById(id);

    if (userData.isPresent()) {
      model.addAttribute("user", userData.get());
      return "modificar_user";
    } else {
      return "noencontrado";
    }
  }

  @PutMapping("/users/{id}")
  public String updateUser(@PathVariable("id") long id, @RequestParam String name, @RequestParam String surname, @RequestParam String email) {
    Optional<User> userData = userRepository.findById(id);

    if (userData.isPresent()) {
      User _user = userData.get();
      _user.setName(name);
      _user.setSurname(surname);
      _user.setEmail(email);
      userRepository.save(_user);
      return "modificado";
    } else {
      return "id"+id;
    }
  }
  

  @DeleteMapping("/users/del/{id}")
  public String deleteUser(@PathVariable("id") long id) {
    try {
      userRepository.deleteById(id);
      return "borrado";
    } catch (Exception e) {
      return "error";
    }
  }

  @DeleteMapping("/users")
  public ResponseEntity<HttpStatus> deleteAllUsers() {
    try {
      userRepository.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }


}