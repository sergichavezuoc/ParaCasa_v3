package com.example.demo.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.demo.model.MenuType;
import com.example.demo.repository.MenuTypeRepository;


@CrossOrigin(origins = "http://localhost:8081")
@Controller
@RequestMapping("/admin")



public class MenuTypeController {

  @Autowired
  MenuTypeRepository menuTypeRepository;
  @GetMapping("/menustype/add")
  public String addMenu(Model model) {
      return "nuevo_tipo";
  }

  @PostMapping("/menustype")
  public String createType(@RequestParam String type) throws IOException {
   MenuType menuTypeRequest =new MenuType(type);
   menuTypeRepository.save(menuTypeRequest);
  return "creado";
      
  }

}