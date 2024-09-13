package net.java.webflux_security.controller;


import lombok.extern.slf4j.Slf4j;
import net.java.webflux_security.library.ApiResponse;
import net.java.webflux_security.model.LoginRequest;
import net.java.webflux_security.model.LoginResponse;
import net.java.webflux_security.model.Userdata;
import net.java.webflux_security.security.CustomEncoder;
import net.java.webflux_security.service.UserService;
import net.java.webflux_security.utils.JWTUtil;
import net.java.webflux_security.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@Validated
@RequestMapping("/api")
public class LoginSignup {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private CustomEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private UtilService util;

	/**
	 * Login request endpoint
	 * @param request
	 * @return
	 */

    @CrossOrigin(origins = "http://localhost:5173")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Mono<ResponseEntity<?>> login(@RequestBody LoginRequest request) {
        return userService.findByUsername(request.getUsername()).map((userDetails) -> {
            log.info("username found");

            if (passwordEncoder.encode(request.getPassword()).equals(userDetails.getPassword())) {

                log.info("Sukses login");
                return ResponseEntity.ok(new LoginResponse(jwtUtil.generateToken(userDetails), userDetails.getEmail()));
            } else {
                log.info("password not matching");
                ApiResponse response = new ApiResponse(HttpStatus.UNAUTHORIZED.value(), "Password not matching", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(HttpStatus.UNAUTHORIZED.value(), "Invalid Email!", null)));
    }

	/**
	 * Signup endpoint
	 * @param user
     */
    @CrossOrigin(origins = "http://localhost:5173")
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public Mono<ResponseEntity<ApiResponse>> createPerson(@RequestBody Userdata user) {
        return userService.findByUsername(user.getEmail())
                .flatMap(userDetails -> {
                    log.info("email found");
                    return Mono.just(ResponseEntity.badRequest().body(new ApiResponse(HttpStatus.BAD_REQUEST.value(), "Email already in use", null)));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    String message = util.validation(user);
                    if (message.isEmpty()) {
                        log.info("Masuk sini");
                        user.setPassword(passwordEncoder.encode(user.getPassword()));
                        return userService.addUpdateUser(user)
                                .map(savedUser -> ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "User created successfully", savedUser)));
                    } else {
                        log.info("Masuk sini oi");
                        return Mono.just(ResponseEntity.badRequest().body(new ApiResponse(HttpStatus.BAD_REQUEST.value(), message, null)));
                    }
                }));
    }



}


