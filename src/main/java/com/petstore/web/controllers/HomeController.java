package com.petstore.web.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/welcome")
    public @ResponseBody  String welcomeMessage(){
        return "pet store Application Running! yeah!!";

    }
    @GetMapping("/page")
    public String displayWelcomePage(){
        return "welcome";
    }

}
