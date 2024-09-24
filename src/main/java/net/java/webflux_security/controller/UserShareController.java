package net.java.webflux_security.controller;


import lombok.extern.slf4j.Slf4j;
import net.java.webflux_security.dto.TodolistDto;
import net.java.webflux_security.dto.UserShareDto;
import net.java.webflux_security.exception.CustomException;
import net.java.webflux_security.model.Todolist;
import net.java.webflux_security.model.Userdata;
import net.java.webflux_security.service.UserService;
import net.java.webflux_security.service.UserShareService;
import net.java.webflux_security.service.UtilService;
import net.java.webflux_security.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/share")
public class UserShareController {
    @Autowired
    UserShareService userShareService;

    @Autowired
    UtilService util;

    @Autowired
    JWTUtil jwtUtil;

    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping("/read")
    public Mono<Userdata> addUserReadAccess(@RequestHeader("Authorization") String token, @RequestBody UserShareDto userShareDto) {
        String jwtToken = token.substring(7);
        String fromUsername = jwtUtil.getUsernameFromToken(jwtToken);

        return userShareService.updateReadUserAccess(fromUsername, userShareDto.getToUsername());
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping("/read/permission")
    public Mono<Userdata> addUserReadPermission(@RequestHeader("Authorization") String token, @RequestBody UserShareDto userShareDto) {
        String jwtToken = token.substring(7);
        String fromUsername = jwtUtil.getUsernameFromToken(jwtToken);

        return userShareService.updateReadUserPermission(fromUsername, userShareDto.getToUsername());
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/read/permission")
    public Mono<List<String>> getUserReadPermission(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        String username = jwtUtil.getUsernameFromToken(jwtToken);
        return userShareService.getReadUserPermission(username);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/read/access")
    public Mono<List<String>> getUserReadAccess(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        String username = jwtUtil.getUsernameFromToken(jwtToken);
        return userShareService.getReadUserAccess(username);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping("/editor")
    public Mono<Userdata> addUserEditorAccess(@RequestHeader("Authorization") String token, @RequestBody UserShareDto userShareDto) {
        String jwtToken = token.substring(7);
        String fromUsername = jwtUtil.getUsernameFromToken(jwtToken);

        return userShareService.updateEditorUserAccess(fromUsername, userShareDto.getToUsername());
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/editor/permission")
    public Mono<List<String>> getUserEditorPermission(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        String username = jwtUtil.getUsernameFromToken(jwtToken);
        return userShareService.getEditorUserPermission(username);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping("/editor/permission")
    public Mono<Userdata> addUserEditorPermission(@RequestHeader("Authorization") String token, @RequestBody UserShareDto userShareDto) {
        String jwtToken = token.substring(7);
        String fromUsername = jwtUtil.getUsernameFromToken(jwtToken);

        return userShareService.updateEditorUserPermission(fromUsername, userShareDto.getToUsername());
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/editor/access")
    public Mono<List<String>> getUserEditorAccess(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        String username = jwtUtil.getUsernameFromToken(jwtToken);
        return userShareService.getEditorUserAccess(username);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @DeleteMapping("/read/delete/{username}")
    public Mono<Userdata> deleteUserReadAccess(@RequestHeader("Authorization") String token, @PathVariable("username") String toUsername) {
        String jwtToken = token.substring(7);
        String fromUsername = jwtUtil.getUsernameFromToken(jwtToken);

        return userShareService.deleteReadUserAccess(fromUsername, toUsername);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @DeleteMapping("/read/permission/delete/{username}")
    public Mono<Userdata> deleteUserReadPermission(@RequestHeader("Authorization") String token, @PathVariable("username") String toUsername) {
        String jwtToken = token.substring(7);
        String fromUsername = jwtUtil.getUsernameFromToken(jwtToken);

        return userShareService.deleteReadUserPermission(fromUsername, toUsername);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @DeleteMapping("/editor/delete/{username}")
    public Mono<Userdata> deleteUserEditorAccess(@RequestHeader("Authorization") String token, @PathVariable("username") String toUsername) {
        String jwtToken = token.substring(7);
        String fromUsername = jwtUtil.getUsernameFromToken(jwtToken);

        return userShareService.deleteEditorUserAccess(fromUsername, toUsername);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @DeleteMapping("/editor/permission/delete/{username}")
    public Mono<Userdata> deleteUserEditorPermission(@RequestHeader("Authorization") String token, @PathVariable("username") String toUsername) {
        String jwtToken = token.substring(7);
        String fromUsername = jwtUtil.getUsernameFromToken(jwtToken);

        return userShareService.deleteEditorUserPermission(fromUsername, toUsername);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/read/{offset}/{pageSize}")
    public Flux<Todolist> getTodolistReadAccess(@RequestHeader("Authorization") String token, @RequestBody UserShareDto userShareDto, @PathVariable("offset") int offset, @PathVariable("pageSize") int pageSize) {
        String jwtToken = token.substring(7);
        String fromUsername = jwtUtil.getUsernameFromToken(jwtToken);

        return userShareService.getUserReadAccess(fromUsername, userShareDto.getToUsername(), offset, pageSize);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/read/{status}/{offset}/{pageSize}")
    public Flux<Todolist> getTodolistReadAccessByStatus(@RequestHeader("Authorization") String token, @RequestBody UserShareDto userShareDto, @PathVariable("status") String status, @PathVariable("offset") int offset, @PathVariable("pageSize") int pageSize) {
        String jwtToken = token.substring(7);
        String fromUsername = jwtUtil.getUsernameFromToken(jwtToken);

        return userShareService.getUserReadAccessByStatus(fromUsername, userShareDto.getToUsername(), status, offset, pageSize);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/read/{id}")
    public Mono<Todolist> getTodolistReadAccessById(@RequestHeader("Authorization") String token, @RequestBody UserShareDto userShareDto, @PathVariable("id") Long id) {
        String jwtToken = token.substring(7);
        String fromUsername = jwtUtil.getUsernameFromToken(jwtToken);

        return userShareService.getUserReadAccessById(fromUsername, userShareDto.getToUsername(), id);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/read/search")
    public Flux<Todolist> getTodolistReadAccessContainsTask(@RequestHeader("Authorization") String token, @RequestBody UserShareDto userShareDto) {
        String jwtToken = token.substring(7);
        String fromUsername = jwtUtil.getUsernameFromToken(jwtToken);

        return userShareService.getTodolistContainsTask(fromUsername, userShareDto.getToUsername(), userShareDto.getTask());
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/read/search/{status}")
    public Flux<Todolist> getTodolistReadAccessContainsTask(@RequestHeader("Authorization") String token, @PathVariable("status") String status, @RequestBody UserShareDto userShareDto) {
        String jwtToken = token.substring(7);
        String fromUsername = jwtUtil.getUsernameFromToken(jwtToken);

        return userShareService.getTodolistByStatusContainsTask(fromUsername, userShareDto.getToUsername(), status, userShareDto.getTask());
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/create/{username}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Todolist> addTodolistEditorAccess(@RequestHeader("Authorization") String token, @PathVariable("username") String toUsername, @RequestBody TodolistDto todolistRequest) {
        String jwtToken = token.substring(7);
        String fromUsername = jwtUtil.getUsernameFromToken(jwtToken);
        String message = util.validation(todolistRequest);
        if (message.isEmpty()) {
            return userShareService.addTodolistEditorAccess(fromUsername, toUsername, new Todolist(todolistRequest.getTask(), todolistRequest.getNote(), todolistRequest.getStatus(), toUsername));
        } else {
            return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, message));
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping("/edit/{username}/{id}")
    public Mono<Todolist> editTodolistEditorAccess(@RequestHeader("Authorization") String token, @PathVariable("username") String toUsername, @PathVariable("id") Long id, @RequestBody TodolistDto todolistRequest) {
        String jwtToken = token.substring(7);
        String fromUsername = jwtUtil.getUsernameFromToken(jwtToken);
        String message = util.validation(todolistRequest);
        if (message.isEmpty()) {
            return userShareService.editTodolistEditorAccess(fromUsername, toUsername, new Todolist(todolistRequest.getTask(), todolistRequest.getNote(), todolistRequest.getStatus(), toUsername), id);
        } else {
            return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, message));
        }


    }


    @CrossOrigin(origins = "http://localhost:5173")
    @DeleteMapping("/delete/{username}/{id}")
    public Mono<Userdata> deleteTodolistEditorAccess(@RequestHeader("Authorization") String token, @PathVariable("username") String toUsername, @PathVariable("id") Long id) {
        String jwtToken = token.substring(7);
        String fromUsername = jwtUtil.getUsernameFromToken(jwtToken);

        return userShareService.deleteTodolistEditorAccess(fromUsername, toUsername, id);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/count/{username}/{status}")
    private Mono<Long> getCountData(@RequestHeader("Authorization") String token, @PathVariable("status") String status, @PathVariable("username") String toUsername){
        String jwtToken = token.substring(7);
        String fromUsername = jwtUtil.getUsernameFromToken(jwtToken);
        return  userShareService.countByStatus(fromUsername, toUsername, status);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/count/{username}")
    private Mono<Long> getCountAll(@RequestHeader("Authorization") String token, @PathVariable("username") String toUsername){
        String jwtToken = token.substring(7);
        String fromUsername = jwtUtil.getUsernameFromToken(jwtToken);
        return  userShareService.countAll(fromUsername, toUsername);
    }


}
