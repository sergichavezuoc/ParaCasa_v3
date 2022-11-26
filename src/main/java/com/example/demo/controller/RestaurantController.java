package com.example.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import com.example.demo.model.Restaurant;
import com.example.demo.repository.RestaurantRepository;

@CrossOrigin(origins = "http://localhost:8081")
@Controller
@RequestMapping("/admin")
public class RestaurantController {

  @Autowired
  RestaurantRepository restaurantRepository;

  @GetMapping("/restaurants")
  public String getAllRestaurants(@RequestParam(required = false) String title, Model model) {
    try {
      List<Restaurant> restaurants = new ArrayList<Restaurant>();

      if (title == null)
        restaurantRepository.findAll().forEach(restaurants::add);
      else
        restaurantRepository.findByNameContaining(title).forEach(restaurants::add);

      if (restaurants.isEmpty()) {
        return "restaurants";
      }
      model.addAttribute("restaurants", restaurants);
      return "restaurants";
    } catch (Exception e) {
      return "error";
    }
  }

  @GetMapping("/restaurants/{id}")
  public ResponseEntity<Restaurant> getRestaurantById(@PathVariable("id") long id) {
    Optional<Restaurant> restaurantData = restaurantRepository.findById(id);

    if (restaurantData.isPresent()) {
      return new ResponseEntity<>(restaurantData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
  @GetMapping("/restaurants/{id}/edit")
  public String getRestaurantById(@PathVariable("id") long id, Model model) {
    Optional<Restaurant> restaurantData = restaurantRepository.findById(id);

    if (restaurantData.isPresent()) {
      model.addAttribute("restaurant", restaurantData.get());
      return "modificar_restaurante";
    } else {
      return "noencontrado";
    }
  }
  @GetMapping("/restaurants/add")
  public String addRestaurant() {
      return "nuevo_restaurante";
  }

  @PostMapping("/restaurants")
  public String createRestaurant(@RequestParam String title, @RequestParam String description, @RequestParam("image") MultipartFile multipartFile) {
    try {
      String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());  
 
      String uploadDir = "src/main/resources/static/images/" ;
      String nameToSaveinDataBase =  '/' + fileName;      
      FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
      restaurantRepository.save(new Restaurant(title, description, false, nameToSaveinDataBase));
      return "creado";
    } catch (Exception e) {
      return "error";
    }
  }

  @PutMapping("/restaurants/{id}")
  public String updateRestaurant(@PathVariable("id") long id, @RequestParam String name, @RequestParam String description) {
    Optional<Restaurant> restaurantData = restaurantRepository.findById(id);

    if (restaurantData.isPresent()) {
      Restaurant _restaurant = restaurantData.get();
      _restaurant.setName(name);
      _restaurant.setDescription(description);
      restaurantRepository.save(_restaurant);
      return "modificado";
    } else {
      return "id"+id;
    }
  }

  @DeleteMapping("/restaurants/del/{id}")
  public String deleteRestaurant(@PathVariable("id") long id) {
    try {
      restaurantRepository.deleteById(id);
      return "borrado";
    } catch (Exception e) {
      return "error";
    }
  }

  @DeleteMapping("/restaurants")
  public ResponseEntity<HttpStatus> deleteAllRestaurants() {
    try {
      restaurantRepository.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

  

}