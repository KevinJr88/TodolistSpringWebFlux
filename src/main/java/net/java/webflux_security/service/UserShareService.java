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
public class UserShareService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodolistRepository todolistRepository;

    public Mono<Userdata> updateReadUserAccess(String fromUsername, String toUsername) {
        return userRepository.findByEmail(fromUsername)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(userFind -> {
                    if (userFind.isPresent()) {
                        Userdata user = userFind.get();
                        if (user.getReadAccess().contains(toUsername)) {
                            return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "Read access for this user already exists"));
                        }
                        user.addReadUserAccess(toUsername);
                        return userRepository.save(user);
                    }
                    return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "User not found"));
                });
    }

    public Mono<Userdata> updateEditorUserAccess(String fromUsername, String toUsername) {
        return userRepository.findByEmail(fromUsername).map(Optional::of).defaultIfEmpty(Optional.empty())
                .flatMap(userFind -> {
                    if(userFind.isPresent()) {
                        Userdata user = userFind.get();
                        if(user.getEditAccess().contains(toUsername)) {
                            return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "Editor access for this user already exists"));
                        }
                        if(user.getReadAccess().contains(toUsername)) {
                            user.removeReadUserAccess(toUsername);
                        }
                        userFind.get().addReadUserAccess(toUsername);
                        userFind.get().addEditorUserAccess(toUsername);
                        return userRepository.save(userFind.get());
                    }
                    return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "User not found"));
                });
    }

    public Mono<Userdata> deleteReadUserAccess(String fromUsername, String toUsername) {
        return userRepository.findByEmail(fromUsername)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(userFind -> {
                    if (userFind.isPresent()) {
                        Userdata user = userFind.get();
                        if (user.getReadAccess().contains(toUsername)) {
                            user.removeReadUserAccess(toUsername);
                            return userRepository.save(user);
                        }
                        return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "Read access for this user does not exist"));
                    }
                    return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "User not found"));
                });
    }

    public Mono<Userdata> deleteEditorUserAccess(String fromUsername, String toUsername) {
        return userRepository.findByEmail(fromUsername)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(userFind -> {
                    if (userFind.isPresent()) {
                        Userdata user = userFind.get();
                        if (user.getEditAccess().contains(toUsername)) {
                            user.removeEditorUserAccess(toUsername);
                            return userRepository.save(user);
                        }
                        return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "Editor access for this user does not exist"));
                    }
                    return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "User not found"));
                });
    }

    public Flux<Todolist> getUserReadAccess(String fromUsername, String toUsername, int offset, int pageSize) {
        return userRepository.findByEmail(toUsername)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMapMany(userFind -> {
                    if (userFind.isPresent()) {
                        return Flux.fromStream(userFind.get().getReadAccess().stream())
                                .filter(userAccess -> userAccess.equals(fromUsername))
                                .flatMap(userAccess -> todolistRepository.findAllPagination(toUsername, offset * pageSize, pageSize))
                                .switchIfEmpty(Flux.error(new CustomException(HttpStatus.FORBIDDEN, "Access Denied")));
                    } else {
                        return Flux.error(new CustomException(HttpStatus.FORBIDDEN, "User not found"));
                    }
                });
    }

    public Flux<Todolist> getUserReadAccessByStatus(String fromUsername, String toUsername, String status, int offset, int pageSize) {
        return userRepository.findByEmail(toUsername)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMapMany(userFind -> {
                    if (userFind.isPresent()) {
                        return Flux.fromStream(userFind.get().getReadAccess().stream())
                                .filter(userAccess -> userAccess.equals(fromUsername))
                                .flatMap(userAccess -> todolistRepository.findAllByStatus(toUsername, status, offset * pageSize, pageSize))
                                .switchIfEmpty(Flux.error(new CustomException(HttpStatus.FORBIDDEN, "Access Denied")));
                    } else {
                        return Flux.error(new CustomException(HttpStatus.FORBIDDEN, "User not found"));
                    }
                });


    }

    public Mono<Todolist> addTodolistEditorAccess(String fromUsername, String toUsername, Todolist todolist) {
        return userRepository.findByEmail(toUsername)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(userFind -> {
                    if (userFind.isPresent()) {
                        return Flux.fromStream(userFind.get().getEditAccess().stream())
                                .filter(userAccess -> userAccess.equals(fromUsername))
                                .next() // Get the first element from the Flux
                                .flatMap(userAccess -> todolistRepository.save(todolist))
                                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.FORBIDDEN, "Access Denied")));
                    } else {
                        return Mono.error(new CustomException(HttpStatus.NOT_FOUND, "User not found"));
                    }
                });
    }

    public Mono<Todolist> editTodolistEditorAccess(String fromUsername, String toUsername, Todolist todolist, Long id) {
        return userRepository.findByEmail(toUsername)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(userFind -> {
                    if (userFind.isPresent()) {
                        return Flux.fromStream(userFind.get().getEditAccess().stream())
                                .filter(userAccess -> userAccess.equals(fromUsername))
                                .next() // Get the first element from the Flux
                                .flatMap(userAccess -> {
                                    todolist.setId(id);
                                    todolist.setEmail(toUsername);
                                    return todolistRepository.save(new Todolist(todolist.getId(), todolist.getTask(), todolist.getNote(), todolist.getStatus(), toUsername));

                                })
                                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.FORBIDDEN, "Access Denied")));
                    } else {
                        return Mono.error(new CustomException(HttpStatus.NOT_FOUND, "User not found"));
                    }
                });

    }

    public Flux<Todolist> getTodolistContainsTask(String fromUsername, String toUsername, String task) {
        return userRepository.findByEmail(toUsername)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMapMany(userFind -> {
                    if (userFind.isPresent()) {
                        return Flux.fromStream(userFind.get().getReadAccess().stream())
                                .filter(userAccess -> userAccess.equals(fromUsername))
                                .flatMap(userAccess -> todolistRepository.findAllByTaskContaining(toUsername, task));
                    } else {
                        return Flux.error(new CustomException(HttpStatus.FORBIDDEN, "User not found"));
                    }
                });
    }

    public Flux<Todolist> getTodolistByStatusContainsTask(String fromUsername, String toUsername, String status, String task) {
        return userRepository.findByEmail(toUsername)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMapMany(userFind -> {
                    if (userFind.isPresent()) {
                        return Flux.fromStream(userFind.get().getReadAccess().stream())
                                .filter(userAccess -> userAccess.equals(fromUsername))
                                .flatMap(userAccess -> todolistRepository.findAllByTaskContainingAndStatus(toUsername, task, status));
                    } else {
                        return Flux.error(new CustomException(HttpStatus.FORBIDDEN, "User not found"));
                    }
                });
    }

    public Mono<Userdata> deleteTodolistEditorAccess(String fromUsername, String toUsername, Long id) {
        return userRepository.findByEmail(toUsername)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(userFind -> {
                    if (userFind.isPresent()) {
                        return Flux.fromStream(userFind.get().getEditAccess().stream())
                                .filter(userAccess -> userAccess.equals(fromUsername))
                                .next() // Get the first element from the Flux
                                .flatMap(userAccess -> {
                                    todolistRepository.deleteById(id);
                                    return Mono.just(userFind.get());
                                })
                                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.FORBIDDEN, "Access Denied")));
                    } else {
                        return Mono.error(new CustomException(HttpStatus.NOT_FOUND, "User not found"));
                    }
                });

    }

    public Mono<Todolist> getUserReadAccessById(String fromUsername, @NonNull String toUsername, Long id) {
        return userRepository.findByEmail(toUsername)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(userFind -> {
                    if (userFind.isPresent()) {
                        return Flux.fromStream(userFind.get().getReadAccess().stream())
                                .filter(userAccess -> userAccess.equals(fromUsername))
                                .next() // Get the first element from the Flux
                                .flatMap(userAccess -> todolistRepository.findByIdAndUsername(toUsername, id));
                    } else {
                        return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "User not found"));
                    }
                });
    }

    public Mono<Long> countByStatus(String fromUsername, String toUsername, String status) {
        return userRepository.findByEmail(toUsername)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(userFind -> {
                    if (userFind.isPresent()) {
                        return Flux.fromStream(userFind.get().getReadAccess().stream())
                                .filter(userAccess -> userAccess.equals(fromUsername))
                                .next() // Get the first element from the Flux
                                .flatMap(userAccess -> todolistRepository.countByStatus(toUsername, status))
                                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.FORBIDDEN, "Access Denied")));
                    } else {
                        return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "User not found"));
                    }
                });
    }

    public Mono<Long> countAll(String fromUsername, String toUsername) {
        return userRepository.findByEmail(toUsername)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(userFind -> {
                    if (userFind.isPresent()) {
                        return Flux.fromStream(userFind.get().getReadAccess().stream())
                                .filter(userAccess -> userAccess.equals(fromUsername))
                                .next() // Get the first element from the Flux
                                .flatMap(userAccess -> todolistRepository.countAll(toUsername))
                                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.FORBIDDEN, "Access Denied")));
                    } else {
                        return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "User not found"));
                    }
                });
    }
}
