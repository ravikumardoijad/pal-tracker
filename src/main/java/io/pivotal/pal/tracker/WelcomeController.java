package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {


  //  @Value("${welcome.message}")
    public String a_welcome_message;

    /*public WelcomeController() {

    }*/

    public WelcomeController(
            @Value("${welcome.message}")String a_welcome_message
    ) {
        this.a_welcome_message = a_welcome_message;
    }

    @GetMapping("/")
    public String sayHello() {
        return  a_welcome_message;
    }
}