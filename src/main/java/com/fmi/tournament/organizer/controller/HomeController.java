package com.fmi.tournament.organizer.controller;

import com.fmi.tournament.organizer.security.model.BaseUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {
  @GetMapping
  public ResponseEntity<String> Home(){
    Object x = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (x instanceof User) {
      String name = ((User) x).getUsername();
      System.out.println("The fucking username is " + name);
    } else if (x instanceof BaseUser) {
      String name = ((BaseUser) x).getUsername();
      System.out.println("The fucking system username is " + name);
    } else {
      System.out.println("WTF we");
    }

    System.out.println("1) " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    System.out.println("2) " + SecurityContextHolder.getContext().getAuthentication().getCredentials());
    System.out.println("3) " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
    System.out.println("4) " + SecurityContextHolder.getContext().getAuthentication().getDetails());
    return ResponseEntity.ok("Welcome!");
  }
}