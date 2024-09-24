package net.java.webflux_security.controller;

import net.java.webflux_security.dto.TodolistDto;
import net.java.webflux_security.exception.CustomException;
import net.java.webflux_security.model.Todolist;
import net.java.webflux_security.service.TodolistService;
import net.java.webflux_security.service.UtilService;
import net.java.webflux_security.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.http.MediaType;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/todolist")
public class TodolistController {
    @Autowired
    TodolistService todolistService;

    @Autowired
    private UtilService util;

    @Autowired
    JWTUtil jwtUtil;

//    @CrossOrigin(origins = "http://localhost:5173")
//    @GetMapping()
//    public Flux<Todolist> getTodolistNoAuth() {
//        return todolistService.findAll();
//    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping
    public Flux<Todolist> getTodolist(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        String username = jwtUtil.getUsernameFromToken(jwtToken);
        return todolistService.findAllByUsername(username);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/{offset}/{pageSize}")
    private Flux<Todolist> getTodolistWithPagination(@RequestHeader("Authorization") String token, @PathVariable int offset, @PathVariable int pageSize) {
        String jwtToken = token.substring(7);
        String username = jwtUtil.getUsernameFromToken(jwtToken);

        return todolistService.findTodolistWithPagination(username, offset, pageSize);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Todolist> addTodolist(@RequestHeader("Authorization") String token, @RequestBody TodolistDto todolistRequest) {
        String jwtToken = token.substring(7);
        String username = jwtUtil.getUsernameFromToken(jwtToken);
        String message = util.validation(todolistRequest);
        if (message.isEmpty()) {
            return todolistService.save(new Todolist(todolistRequest.getTask(), todolistRequest.getNote(), todolistRequest.getStatus(), username));
        } else {
            return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, message));
        }

    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/{status}/{offset}/{pagesize}")
    public Flux<Todolist> getTodolistByStatusPagination(@RequestHeader("Authorization") String token, @PathVariable("status") String status, @PathVariable("offset") int offset, @PathVariable("pagesize") int pagesize ) {
        String jwtToken = token.substring(7);
        String username = jwtUtil.getUsernameFromToken(jwtToken);
        return todolistService.findAllByStatus(username, status, offset, pagesize);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/{id}")
    public Mono<Todolist> getTodolistById(@RequestHeader("Authorization") String token, @PathVariable("id") Long id) {
        String jwtToken = token.substring(7);
        String username = jwtUtil.getUsernameFromToken(jwtToken);
        return  todolistService.findById(username, id);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping ("/search")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Todolist> getTodolistByTaskAndStatus(@RequestHeader("Authorization") String token, @RequestBody Todolist todolistRequest) {
        String jwtToken = token.substring(7);
        String username = jwtUtil.getUsernameFromToken(jwtToken);

        return todolistService.findByTaskAndStatusContaining(username, todolistRequest.getTask(), todolistRequest.getStatus());
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping ("/search/all")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Todolist> getTodolistByTask(@RequestHeader("Authorization") String token, @RequestBody Todolist todolistRequest) {
        String jwtToken = token.substring(7);
        String username = jwtUtil.getUsernameFromToken(jwtToken);

        return todolistService.findByTaskContaining(username, todolistRequest.getTask());
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping("/{id}")
    public Mono<Todolist> updateTodolist(@RequestHeader("Authorization") String token, @PathVariable("id") Long id, @RequestBody TodolistDto todolistRequest) {
        String jwtToken = token.substring(7);
        String username = jwtUtil.getUsernameFromToken(jwtToken);
        String message = util.validation(todolistRequest);
        if (message.isEmpty()) {
            return todolistService.update(id,new Todolist(todolistRequest.getTask(), todolistRequest.getNote(), todolistRequest.getStatus(), username));
        } else {
            return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, message));
        }



    }

    @CrossOrigin(origins = "http://localhost:5173")
    @DeleteMapping("/{id}")
    public Mono<Void> deleteTodolist(@PathVariable("id") Long id) {
        return todolistService.deleteById(id);
    }


    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/count/{status}")
    private Mono<Long> getCountData(@RequestHeader("Authorization") String token, @PathVariable String status){
        String jwtToken = token.substring(7);
        String username = jwtUtil.getUsernameFromToken(jwtToken);
        return  todolistService.countByStatus(username, status);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/count")
    private Mono<Long> getCountAll(@RequestHeader("Authorization") String token){
        String jwtToken = token.substring(7);
        String username = jwtUtil.getUsernameFromToken(jwtToken);
        return  todolistService.countAll(username);
    }

}
