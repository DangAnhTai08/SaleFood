package com.tai.chef.salefood.controllers;

import com.tai.chef.salefood.models.ERole;
import com.tai.chef.salefood.models.Role;
import com.tai.chef.salefood.models.User;
import com.tai.chef.salefood.payload.request.SignupRequest;
import com.tai.chef.salefood.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private static final int SECRET_SIZE = 10;

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/singup")
    public String singup(@ModelAttribute("user") SignupRequest signUpRequest, ModelMap modelMap) {
        try {
            if (userRepository.existsByUsername(signUpRequest.getUsername()))
                return "index";

            if (userRepository.existsByEmail(signUpRequest.getEmail()))
                return "index";

            // Create new user's account
            User user = new User(signUpRequest.getUsername(),
                    signUpRequest.getEmail(),
                    encoder.encode(signUpRequest.getPassword()));
            Set<Role> roles = new HashSet<>();
            roles.add(new Role(1, ERole.ROLE_USER));
            user.setRoles(roles);
            user.setPhone(signUpRequest.getPhone());
            user.setAddress(signUpRequest.getAddress());
            user.setUserSecret(RandomStringUtils.random(SECRET_SIZE, true, true).toUpperCase());
            String encodedSecret = new Base32().encodeToString(user.getUserSecret().getBytes());

            userRepository.save(user);

            // This Base32 encode may usually return a string with padding characters - '='.
            // QR generator which is user (zxing) does not recognize strings containing symbols other than alphanumeric
            // So just remove these redundant '=' padding symbols from resulting string
            modelMap.addAttribute("secret", encodedSecret.replace("=", ""));

            return "register";
        } catch (Exception ex) {
            log.error("FAILED while singup ", ex);
            return "index";
        }
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
