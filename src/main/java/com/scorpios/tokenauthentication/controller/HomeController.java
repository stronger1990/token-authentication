package com.scorpios.tokenauthentication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

    @RequestMapping(value ={ "/login3"}, method = RequestMethod.GET)
    public String getLoginPage() {
        return "login2";
    }
}
