package com.example.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Menu;
import com.example.demo.model.MenuType;
import com.example.demo.model.Restaurant;
import com.example.demo.repository.MenuRepository;
import com.example.demo.repository.MenuTypeRepository;
import com.example.demo.repository.RestaurantRepository;

import org.springframework.util.StringUtils;


@CrossOrigin(origins = "http://localhost:8081")
@Controller
@RequestMapping("/admin")



public class MenuController {

  @Autowired
  MenuRepository menuRepository;
  @Autowired
  RestaurantRepository restaurantRepository;
  @Autowired
  MenuTypeRepository menuTypeRepository;
  @GetMapping("/menus")
  public String getAllMenus(@RequestParam(required = false) String title, Model model) {
    try {
      List<Menu> menus = new ArrayList<Menu>();

      if (title == null)
        menuRepository.findAll().forEach(menus::add);
      else
        menuRepository.findByNameContaining(title).forEach(menus::add);

      if (menus.isEmpty()) {
        return "menus";
      }
      model.addAttribute("menus", menus);
      return "menus";
    } catch (Exception e) {
      return "error";
    }
  }
  @GetMapping("/restaurants/{restaurantId}/menus")
  public String getAllCommentsByMenuId(@PathVariable(value = "restaurantId") Long restaurantId, Model model) {
 
    List<Menu> menus = menuRepository.findByRestaurantId(restaurantId);
    model.addAttribute("menus", menus);
    return "menus";
  }
  @GetMapping("/menus/{id}")
  public ResponseEntity<Menu> getMenuById(@PathVariable("id") long id) {
    Optional<Menu> menuData = menuRepository.findById(id);

    if (menuData.isPresent()) {
      return new ResponseEntity<>(menuData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
  @GetMapping("/menus/add")
  public String addMenu(Model model) {
    List<Restaurant> restaurants = new ArrayList<Restaurant>();
    restaurantRepository.findAll().forEach(restaurants::add);
    List<MenuType> menuTypes = new ArrayList<MenuType>();
    menuTypeRepository.findAll().forEach(menuTypes::add);
    model.addAttribute("restaurants", restaurants);
    model.addAttribute("types", menuTypes);
      return "nuevo_menu";
  }

  @PostMapping("/menus")
  public String createMenu(@RequestParam String name, @RequestParam String description, @RequestParam long restaurantsid, @RequestParam long menutypeid,  @RequestParam("image") MultipartFile multipartFile) throws IOException {
    System.out.println("identificador de menu");

    Optional<Restaurant> menu = restaurantRepository.findById(restaurantsid);
    Optional<MenuType> menuType = menuTypeRepository.findById(menutypeid);
    if (menu.isPresent() && menuType.isPresent()) {
      String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());  
 
      String uploadDir = "src/main/resources/static/images/" ;
      String nameToSaveinDataBase =  '/' + fileName;      
 
      FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
      Menu menuRequest = new Menu(name, description, menu.get(), nameToSaveinDataBase, menuType.get());
      menuRepository.save(menuRequest);
      return "creado";
    } else {
      return "error";
    }
    
    
  }
  @GetMapping("/menus/{id}/edit")
  public String getMenuById(@PathVariable("id") long id, Model model) {
    Optional<Menu> menuData = menuRepository.findById(id);
    
    if (menuData.isPresent()) {
      List<Restaurant> restaurants = restaurantRepository.findAll();
      List<MenuType> types = menuTypeRepository.findAll();
      model.addAttribute("types", types);
      model.addAttribute("restaurants", restaurants);
      model.addAttribute("menu", menuData.get());
      return "modificar_menu";
    } else {
      return "noencontrado";
    }
  }

  @PutMapping("/menus/{id}")
  public String updateMenu(@PathVariable("id") long id, @RequestParam String name, @RequestParam String description, @RequestParam long restaurant, @RequestParam long type) {
    Optional<Menu> menuData = menuRepository.findById(id);
Optional<Restaurant> restaurantData =restaurantRepository.findById(restaurant);
Optional<MenuType> menuTypeData =menuTypeRepository.findById(type);
    if (menuData.isPresent() && restaurantData.isPresent() && menuTypeData.isPresent()) {
      Menu _menu = menuData.get();
      MenuType _menuType =menuTypeData.get();
      _menu.setName(name);
      _menu.setDescription(description);
      _menu.setMenu(restaurantData.get());
      _menu.setType(_menuType);
      menuRepository.save(_menu);
      menuTypeRepository.save(_menuType);
      return "modificado";
    } else {
      return "id"+id;
    }
  }
  

  @DeleteMapping("/menus/del/{id}")
  public String deleteMenu(@PathVariable("id") long id) {
    try {
      menuRepository.deleteById(id);
      return "borrado";
    } catch (Exception e) {
      return "error";
    }
  }

  @DeleteMapping("/menus")
  public ResponseEntity<HttpStatus> deleteAllMenus() {
    try {
      menuRepository.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

}