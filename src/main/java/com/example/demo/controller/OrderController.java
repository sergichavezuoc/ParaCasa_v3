package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.mapping.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.demo.model.Menu;
import com.example.demo.model.Order;
import com.example.demo.model.User;
import com.example.demo.repository.MenuRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.OrderRepository;


@CrossOrigin(origins = "http://localhost:8081")
@Controller
@RequestMapping("/admin")



public class OrderController {


  @Autowired
  OrderRepository orderRepository;
  @Autowired
  MenuRepository menuRepository;
  @Autowired
  UserRepository userRepository;
  @GetMapping("/orderes")
  public String getAllOrders( @RequestParam(required = false) Long userid,Model model) {
 
    List<Order> orders = new ArrayList<Order>();
    if (userid == null){
      orderRepository.findAll().forEach(orders::add);
    }
    else {
      orderRepository.findAllByUser_id(userid).forEach(orders::add);
    }
    if (orders.isEmpty()) {
      return "orderes";
    }
    model.addAttribute("orders", orders);
    return "orderes";
}
@GetMapping("/orderes/add")
public String addOrder(Model model) {
  List<User> users = new ArrayList<User>();
  userRepository.findAll().forEach(users::add);
  model.addAttribute("users", users);
    return "nuevo_orderes";
}
@PostMapping("/orderes")
public String createOrder(@RequestParam long userid) {
  Optional<User> user = userRepository.findById(userid);
  if (user.isPresent()) {
    Order orderRequest = new Order(user.get());
    orderRepository.save(orderRequest);
  }
    return "creado";  
  
}
@PostMapping("/orderinmenu")
public String addMenuOrder(@RequestParam long id, @RequestParam long menuid,Model model) {
  Optional<Order> order = orderRepository.findById(id);
  Optional<Menu> menu = menuRepository.findById(menuid);
  if (order.isPresent() & menu.isPresent()) {
    Order orderRequest=order.get();
    Menu menuRequest=menu.get();
    orderRequest.addMenu(menuRequest);
    orderRepository.save(orderRequest);
    
  return "creado"; 
  }
  else{
    return "error";
  }
}
@GetMapping("/orderes/{id}/edit")
  public String getUser_MenuById(@PathVariable("id") long id, Model model) {
    Optional<Order> orderData = orderRepository.findById(id);

    if (orderData.isPresent()) {
      model.addAttribute("order", orderData.get());
      List<User> user = userRepository.findAll();
      List<Menu> menu = menuRepository.findAll();
      Order order = orderData.get();
      List<Menu> menusinorder = menuRepository.findMenusByorders(order);
      model.addAttribute("users", user);
      model.addAttribute("menus", menu);
      model.addAttribute("menusinorder", menusinorder);
      return "modificar_orderes";
    } else {
      return "noencontrado";
    }
  }
  @DeleteMapping("/orderes/del/{id}")
  public String deleteOrder(@PathVariable("id") long id) {
    try {
      orderRepository.deleteById(id);
      return "borrado";
    } catch (Exception e) {
      return "error";
    }
  }
  @DeleteMapping("/menuinorderes/del/{id}")
  public String deleteMenuinOrder(@PathVariable("id") long menuid,@RequestParam long id) {
    try {
      Optional<Menu> menu = menuRepository.findById(menuid);
      Optional<Order> order =orderRepository.findById(id);
     if (order.isPresent() & menu.isPresent()) {
      Order orderRequest =order.get();
      Menu menuRequest=menu.get();
    orderRequest.removeMenu(menuRequest);
    orderRepository.save(orderRequest);
      return "borrado";}
      else{
        return "error"; 
      }
    } catch (Exception e) {
      return "error";
    }
  }
  /*
  @GetMapping("/orders")
  public String getAllOrders( @RequestParam(required = false) Long customerid,Model model) {
 
      List<Customer_Menu> orders = new ArrayList<Customer_Menu>();
      if (customerid == null){
        customer_MenuRepository.findAllGrouped().forEach(orders::add);
      }
      else {
        customer_MenuRepository.findAllGrouped(customerid).forEach(orders::add);
      }
      if (orders.isEmpty()) {
        return "orders";
      }
      model.addAttribute("orders", orders);
      return "orders";
  }
 

  @GetMapping("/orders/add")
  public String addCustomer(Model model) {
    List<Menu> menus = new ArrayList<Menu>();
    menuRepository.findAll().forEach(menus::add);
    List<Customer> customers = new ArrayList<Customer>();
    customerRepository.findAll().forEach(customers::add);
    model.addAttribute("customers", customers);
    model.addAttribute("menus", menus);
      return "nuevo_order";
  }

   @PostMapping("/orders")
  public String createCustomer_Order(@RequestParam long customerid, @RequestParam long menuid,  @RequestParam int quantity,  @RequestParam long orderid) {
    Optional<Customer> customer = customerRepository.findById(customerid);
    Optional<Menu> menu = menuRepository.findById(menuid);
    if (menu.isPresent() && customer.isPresent()) {
      Customer_Menu customer_MenuRequest = new Customer_Menu(customer.get(), menu.get(), quantity, orderid);
      customer_MenuRepository.save(customer_MenuRequest);
    }
  

      return "creado";  
    
  }
  @GetMapping("/orders/{id}/edit")
  public String getCustomer_MenuById(@PathVariable("id") long id, Model model) {
    Optional<Customer_Menu> customer_MenuData = customer_MenuRepository.findById(id);

    if (customer_MenuData.isPresent()) {
      model.addAttribute("customer_Menu", customer_MenuData.get());
      List<Customer> customer = customerRepository.findAll();
      List<Menu> menu = menuRepository.findAll();
      Customer_Menu order = customer_MenuData.get();
      List<Customer_Menu> menusinorder = customer_MenuRepository.findByOrderid(order.getOrderid());
      model.addAttribute("customers", customer);
      model.addAttribute("menus", menu);
      model.addAttribute("menusinorder", menusinorder);
      return "modificar_order";
    } else {
      return "noencontrado";
    }
  }
  @DeleteMapping("/orderinmenu/del/{id}")
  public String deleteCustomer(@PathVariable("id") long id) {
    try {
      customer_MenuRepository.deleteById(id);
      return "borrado";
    } catch (Exception e) {
      return "error";
    }
  }
  @DeleteMapping("/order/del/{id}")
  public String deleteOrder(@PathVariable("id") long id) {
    try {
      customer_MenuRepository.deleteById(id);
      return "borrado";
    } catch (Exception e) {
      return "error";
    }
  }
   */
}