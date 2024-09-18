package net.java.webflux_security.service;

import lombok.NonNull;
import net.java.webflux_security.exception.CustomException;
import net.java.webflux_security.model.Todolist;
import net.java.webflux_security.model.Userdata;
import net.java.webflux_security.repository.TodolistRepository;
import net.java.webflux_security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public Mono<Userdata> findByUsername(String username) {
        Mono<Userdata> data = userRepository.findByEmail(username);
        return data.switchIfEmpty(Mono.empty());
    }

    public Flux<Userdata> getAllUser() {
        return userRepository.findAll();
    }

    public Mono<Userdata> addUpdateUser(Userdata userdata) {
        return userRepository.save(userdata);
    }

}
