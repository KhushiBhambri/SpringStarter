package com.kaybee.init.new.demo.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class projectController {

    @GetMapping("/get")
        public String get(){

        return "HI";
        }
}


