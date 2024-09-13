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

    public Flux<Todolist> findAll() {
        return todolistRepository.findAll();
    }

    public Flux<Todolist> findByTaskAndStatusContaining(String task, String status) {
        return todolistRepository.findAllByTaskContainingAndStatus(task, status);
    }

    public Flux<Todolist> findByTaskContaining(String task) {
        return todolistRepository.findAllByTaskContaining(task);
    }


    public Flux<Todolist> findAllByStatus(String status, int offset, int pageSize){
//        PageRequest pageRequest = PageRequest.of(offset, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        return todolistRepository.findAllByStatus(status, offset, pageSize);
    }

    public Mono<Long> countByStatus(String status){
        return  todolistRepository.countByStatus(status);
    }

    public Mono<Long> countAll(){
        return  todolistRepository.countAll();
    }

    public Mono<Todolist> findById(Long id) {
        return todolistRepository.findById(id);
    }

    public Mono<Todolist> save(Todolist todolistReq) {
        return todolistRepository.save(todolistReq);
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

    public Flux<Todolist> findTodolistWithPagination(int offset, int pageSize){

        return todolistRepository.findAllPagination(offset, pageSize);
    }

//    public PageImpl<Mono<Todolist>> findTodolistWithPagination(int offset, int pageSize){
//
//        Flux<Todolist> todolist = todolistRepository.findAllPagination(offset, pageSize);
//        return todolist;
//    }


}
