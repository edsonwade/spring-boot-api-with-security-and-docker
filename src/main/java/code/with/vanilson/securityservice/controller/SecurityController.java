package code.with.vanilson.securityservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "")
public class SecurityController {

    @GetMapping
    public String hello() {
        return "Hello World";
    }
}
