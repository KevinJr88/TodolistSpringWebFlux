package net.java.webflux_security.service;

import net.java.webflux_security.model.Todolist;
import net.java.webflux_security.repository.TodolistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class TodolistService {
    @Autowired
    TodolistRepository todolistRepository;

    public Flux<Todolist> findAllByUsername(String username) {
        return todolistRepository.findAllByEmail(username);
    }

    public Flux<Todolist> findTodolistWithPagination(String username, int offset, int pageSize){
        int tempOffset = offset * pageSize;
        return todolistRepository.findAllPagination(username, tempOffset, pageSize);
    }

    public Mono<Todolist> save(Todolist todolistReq) {
        return todolistRepository.save(todolistReq);
    }

    public Flux<Todolist> findAllByStatus(String username, String status, int offset, int pageSize){
        int tempOffset = offset * pageSize;
        return todolistRepository.findAllByStatus(username, status, tempOffset, pageSize);
    }

    public Mono<Todolist> findById(String username, Long id) {
        return todolistRepository.findByIdAndUsername(username, id);
    }

    public Mono<Todolist> update(Long id, Todolist todolist) {
        return todolistRepository.findById(id).map(Optional::of).defaultIfEmpty(Optional.empty())
                .flatMap(optionalTodolist -> {
                    if (optionalTodolist.isPresent()) {
                        todolist.setId(id);
                        return todolistRepository.save(todolist);
                    }
                    return Mono.empty();
                });
    }

    public Mono<Void> deleteById(Long id) {
        return todolistRepository.deleteById(id);
    }

    public Flux<Todolist> findByTaskAndStatusContaining(String username, String task, String status) {
        return todolistRepository.findAllByTaskContainingAndStatus(username, task, status);
    }

    public Flux<Todolist> findByTaskContaining(String username, String task) {
        return todolistRepository.findAllByTaskContaining(username, task);
    }

    public Mono<Long> countByStatus(String username, String status){
        return  todolistRepository.countByStatus(username, status);
    }

    public Mono<Long> countAll(String username){
        return  todolistRepository.countAll(username);
    }

}
