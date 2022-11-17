package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.demo.model.Menu;
import com.example.demo.model.Order;
import com.example.demo.model.Restaurant;
import com.example.demo.model.User;
import com.example.demo.repository.MenuRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.RestaurantRepository;
import com.example.demo.repository.UserRepository;

import org.springframework.ui.Model;
@CrossOrigin(origins = "http://localhost:8081")
@Controller
@RequestMapping("/")
public class PublicController {
    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderRepository orderRepository;
    @GetMapping("/user/restaurants")
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
@PostMapping("/user/addorder")
public String addorder(@RequestParam Long iduser, Long idmenu,Model model) {
  Optional<Menu> menu= menuRepository.findById(idmenu);
  Optional<User> user= userRepository.findById(iduser);
  
  if (menu.isPresent() && user.isPresent()) {
    Order orderRequest = new Order(user.get());
    orderRepository.save(orderRequest);
    Menu menuRequest=menu.get();
    orderRequest.addMenu(menuRequest);
    Order order = orderRepository.save(orderRequest);
    List<Menu> menus = menuRepository.findAll();
    List<Menu> menusinorder = menuRepository.findMenusByorders(order);
    model.addAttribute("order", order);
    model.addAttribute("users", user);
    model.addAttribute("menus", menus);
    model.addAttribute("menusinorder", menusinorder);
    return "user_order";
  }
  else {
    return "aerror";
  }
 
 }
@PostMapping("/process_register")
public String saveUser(@RequestParam String name, String surname, String username, String password, String email,Model model) {
  BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
  String encodedPassword = encoder.encode(password);
  User userRequest = new User(name, surname, username, email, encodedPassword);
  userRepository.save(userRequest);
  return "creado";
}
@GetMapping("/user/myprofile")
public String getProfile(Model model) {
  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
  String currentPrincipalName = auth.getName();
  User uservalidated = userRepository.getUserByUsername(currentPrincipalName);
  if (uservalidated!=null){
    model.addAttribute("user", uservalidated);
    return "myprofile";
  }
  else {
    return "aerror";
  }
}
@GetMapping("/user/orders")
public String getOrders(Model model) {
  List<Order> orders = new ArrayList<Order>();
  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
  String currentPrincipalName = auth.getName();
  User uservalidated = userRepository.getUserByUsername(currentPrincipalName);
  if (uservalidated!=null){
   
    orderRepository.findAllByUser_id(uservalidated.getId()).forEach(orders::add);
  if (orders.isEmpty()) {
    return "orderes";
  }
  model.addAttribute("orders", orders);
  return "orderes";
  }
  else {
    return "aerror";
  }
}
@GetMapping("/user/restaurants/{restaurantId}/menus")
public String getAllCommentsByMenuId(@PathVariable(value = "restaurantId") Long restaurantId, Model model) {
  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
  String currentPrincipalName = auth.getName();
  User uservalidated = userRepository.getUserByUsername(currentPrincipalName);
  if (uservalidated!=null){
    model.addAttribute("user", uservalidated.getId());
    System.out.println(currentPrincipalName);
  System.out.println(uservalidated.getId());
  }

  List<Menu> menus = menuRepository.findByRestaurantId(restaurantId);
  model.addAttribute("menus", menus);
  return "menus";
}
@RequestMapping("/login")
public String login() {
  return "login";
}
}
