package com.tai.chef.salefood.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "index";
    }

    @GetMapping("/about")
    public String aboutUs() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/faqs")
    public String faqs() {
        return "faqs";
    }

    @GetMapping("/help")
    public String help() {
        return "help";
    }

    @GetMapping("/icon")
    public String icon() {
        return "icon";
    }

    @GetMapping("/payment")
    public String payment() {
        return "payment";
    }

    @GetMapping("/privacy")
    public String privacy() {
        return "privacy";
    }

    @GetMapping("/product")
    public String product() {
        return "product";
    }

    @GetMapping("/product2")
    public String product2() {
        return "products";
    }

    @GetMapping("/single")
    public String single() {
        return "single";
    }

    @GetMapping("/single2")
    public String single2() {
        return "single2";
    }

    @GetMapping("/terms")
    public String terms() {
        return "terms";
    }

    @GetMapping("/typography")
    public String typography() {
        return "typography";
    }

    @PostMapping("/checkout")
    public String checkout() {
        return "checkout";
    }
}
