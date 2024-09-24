package net.java.webflux_security.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
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

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@Slf4j
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
                        if(toUsername.equals(fromUsername)) {
                            return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "Cannot add self as read access"));
                        }
                        if (user.getReadAccess().contains(toUsername)) {
                            return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "Read access for this user already exists"));
                        }
                        user.addReadUserAccess(toUsername);
                        return userRepository.save(user);
                    }
                    return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "User not found"));
                });
    }

    public Mono<List<String>> getReadUserAccess(String username) {
        return userRepository.findByEmail(username)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(userFind -> {
                    if (userFind.isPresent()) {
                        return Mono.just(userFind.get().getReadAccess());
                    } else {
                        return Mono.error(new CustomException(HttpStatus.NOT_FOUND, "User not found"));
                    }
                });
    }

    public Mono<List<String>> getEditorUserAccess(String username) {
        return userRepository.findByEmail(username)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(userFind -> {
                    if (userFind.isPresent()) {
                        return Mono.just(userFind.get().getEditAccess());
                    } else {
                        return Mono.error(new CustomException(HttpStatus.NOT_FOUND, "User not found"));
                    }
                });
    }


    public Mono<Userdata> updateReadUserPermission(String fromUsername,  String toUsername) {
        return userRepository.findByEmail(toUsername)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(userFind -> {
                    if (userFind.isPresent()) {
                        Userdata user = userFind.get();
                        if(toUsername.equals(fromUsername)) {
                            return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "Cannot add self as read access"));
                        }
                        if (user.getReadPermission().contains(fromUsername)) {
                            return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "Read permission for this user already exists"));
                        }
                        user.addReadUserPermission(fromUsername);
                        return userRepository.save(user);
                    }
                    return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "User not found"));
                });
    }

    public Mono<List<String>> getReadUserPermission(String username) {
        return userRepository.findByEmail(username)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(userFind -> {
                    if (userFind.isPresent()) {
                        return Mono.just(userFind.get().getReadPermission());
                    } else {
                        return Mono.error(new CustomException(HttpStatus.NOT_FOUND, "User not found"));
                    }
                });
    }

    public Mono<Userdata> updateEditorUserAccess(String fromUsername, String toUsername) {
        return userRepository.findByEmail(fromUsername).map(Optional::of).defaultIfEmpty(Optional.empty())
                .flatMap(userFind -> {
                    if(userFind.isPresent()) {
                        Userdata user = userFind.get();
                        if(toUsername.equals(fromUsername)) {
                            return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "Cannot add self as read access"));
                        }
                        if(user.getEditAccess().contains(toUsername)) {
                            return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "Editor access for this user already exists"));
                        }
                        if(user.getReadAccess().contains(toUsername)) {
                            user.removeReadUserAccess(toUsername);
                        }
                        user.addReadUserAccess(toUsername);
                        user.addEditorUserAccess(toUsername);
                        return userRepository.save(user);
                    }
                    return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "User not found"));
                });
    }

    public Mono<Userdata> updateEditorUserPermission(String fromUsername,  String toUsername) {
        return userRepository.findByEmail(toUsername)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(userFind -> {
                    if (userFind.isPresent()) {
                        Userdata user = userFind.get();
                        if(toUsername.equals(fromUsername)) {
                            return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "Cannot add self as read access"));
                        }
                        if (user.getEditPermission().contains(fromUsername)) {
                            return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "Read permission for this user already exists"));
                        }
                        if(user.getReadPermission().contains(fromUsername)) {
                            user.removeReadUserPermission(fromUsername);
                        }
                        user.addReadUserPermission(fromUsername);
                        user.addEditorUserPermission(fromUsername);
                        return userRepository.save(user);
                    }
                    return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "User not found"));
                });
    }

    public Mono<List<String>> getEditorUserPermission(String username) {
        return userRepository.findByEmail(username)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(userFind -> {
                    if (userFind.isPresent()) {
                        return Mono.just(userFind.get().getEditPermission());
                    } else {
                        return Mono.error(new CustomException(HttpStatus.NOT_FOUND, "User not found"));
                    }
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
                                .collectList()
                                .flatMapMany(userAccessList -> {
                                    if (userAccessList.isEmpty()) {
                                        return Flux.error(new CustomException(HttpStatus.FORBIDDEN, "Access Denied"));
                                    }
                                    return todolistRepository.findAllPagination(toUsername, offset * pageSize, pageSize);
                                });
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
                                .collectList()
                                .flatMapMany(userAccessList -> {
                                    if (userAccessList.isEmpty()) {
                                        return Flux.error(new CustomException(HttpStatus.FORBIDDEN, "Access Denied"));
                                    }
                                    return todolistRepository.findAllByStatus(toUsername, status, offset * pageSize, pageSize);
                                });

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
                                    return todolistRepository.findByIdAndUsername(toUsername, id)
                                            .flatMap(todolist -> todolistRepository.deleteById(todolist.getId()))
                                            .then(Mono.just(userFind.get()));
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


    public Mono<Userdata> deleteReadUserPermission(String fromUsername, String toUsername) {
        return userRepository.findByEmail(toUsername)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(userFind -> {
                    if (userFind.isPresent()) {
                        Userdata user = userFind.get();
                        if (user.getReadPermission().contains(fromUsername)) {
                            user.removeReadUserPermission(fromUsername);
                            return userRepository.save(user);
                        }
                        return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "Read permission for this user does not exist"));
                    }
                    return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "User not found"));
                });
    }

    public Mono<Userdata> deleteEditorUserPermission(String fromUsername, String toUsername) {
        return userRepository.findByEmail(toUsername)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(userFind -> {
                    if (userFind.isPresent()) {
                        Userdata user = userFind.get();
                        if (user.getEditPermission().contains(fromUsername)) {
                            user.removeEditorUserPermission(fromUsername);
                            return userRepository.save(user);
                        }
                        return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "Editor permission for this user does not exist"));
                    }
                    return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "User not found"));
                });
    }
}
