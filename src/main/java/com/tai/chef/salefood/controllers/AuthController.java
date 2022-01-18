package com.tai.chef.salefood.controllers;

import com.tai.chef.salefood.common.CommonResource;
import com.tai.chef.salefood.models.ERole;
import com.tai.chef.salefood.models.Role;
import com.tai.chef.salefood.models.User;
import com.tai.chef.salefood.payload.request.LoginRequest;
import com.tai.chef.salefood.payload.request.OTPRequest;
import com.tai.chef.salefood.payload.request.SignupRequest;
import com.tai.chef.salefood.payload.response.AuthResponse;
import com.tai.chef.salefood.payload.response.MessageResponse;
import com.tai.chef.salefood.repository.UserRepository;
import com.tai.chef.salefood.security.services.UserDetailsImpl;
import com.tai.chef.salefood.security.token.TokenProvider;
import com.tai.chef.salefood.service.TotpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private static final int SECRET_SIZE = 10;

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final TotpService totpService;

    private final TokenProvider tokenProvider;

    private final CommonResource commonResource;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(commonResource.getTokenLength());
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        boolean saveTokenRedis = tokenProvider.saveTokenRedis(token, userDetails);
        if (!saveTokenRedis)
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(HttpStatus.UNAUTHORIZED);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new AuthResponse(token, userDetails, roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        try {
            if (userRepository.existsByUsername(signUpRequest.getUsername()))
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Username is already taken!"));

            if (userRepository.existsByEmail(signUpRequest.getEmail()))
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Email is already in use!"));

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
            user.setBalance(new BigDecimal(50000));
            String encodedSecret = new Base32().encodeToString(user.getUserSecret().getBytes());

            userRepository.save(user);

            // This Base32 encode may usually return a string with padding characters - '='.
            // QR generator which is user (zxing) does not recognize strings containing symbols other than alphanumeric
            // So just remove these redundant '=' padding symbols from resulting string
            return ResponseEntity.ok(new MessageResponse(
                    encodedSecret.replace("=", ""),
                    user.getUsername()));

        } catch (Exception ex) {
            log.error("FAILED while signup ", ex);
            return ResponseEntity.internalServerError().body(new MessageResponse("User registered failed!"));
        }
    }

    @RequestMapping("/verify-otp")
    public ResponseEntity<?> tokenCheck(@RequestBody OTPRequest otpRequest, HttpServletRequest request) {
        try {
            String token = tokenProvider.resolveToken(request);
            String username = tokenProvider.getUsernameFromToken(token);
            User user = userRepository.findByUsername(username).orElse(null);
            if (Objects.isNull(user))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("UNAUTHORIZED"));

            // check otp verified in time
            if (tokenProvider.isTOTPRedis(otpRequest.getTotp(), username))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("TOTP is verified"));

            if (!totpService.verifyCode(otpRequest.getTotp(), user.getUserSecret()))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("UNAUTHORIZED"));

            // save otp to redis
            tokenProvider.saveTOTPRedis(otpRequest.getTotp(), username);

            // update balance of user
            BigDecimal price = Objects.isNull(otpRequest.getPrice()) ?
                    BigDecimal.ZERO : BigDecimal.valueOf(otpRequest.getPrice());
            user.setBalance(user.getBalance().subtract(price));
            userRepository.save(user);

            return ResponseEntity.ok().body(new MessageResponse("Pay success"));
        } catch (Exception ex) {
            log.error("verify totp failed", ex);
            return ResponseEntity.internalServerError().body("pay error");
        }
    }
}
