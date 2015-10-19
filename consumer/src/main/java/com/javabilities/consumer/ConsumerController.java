package com.javabilities.consumer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class ConsumerController {
    @RequestMapping(value="/", method=RequestMethod.GET)
    public String home() {
        return "index";
    }
}
