package net.java.webflux_security.repository;

import lombok.NonNull;
import net.java.webflux_security.model.Todolist;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.r2dbc.repository.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TodolistRepository extends R2dbcRepository<Todolist, Long> {

    @Query("SELECT * FROM todolist WHERE email = :username")
    Flux<Todolist> findAllByEmail(String username);

    @Query("SELECT * FROM todolist WHERE email = :username ORDER BY todolist.id DESC LIMIT :pageSize OFFSET :offset")
    Flux<Todolist> findAllPagination(String username, int offset, int pageSize);

    @Query("SELECT * FROM todolist WHERE status = :status AND email = :username ORDER BY todolist.id DESC LIMIT :pageSize OFFSET :offset")
    Flux<Todolist> findAllByStatus(String username, String status, int offset, int pageSize);

    @Query("SELECT * FROM todolist WHERE email = :username AND id = :id LIMIT 1")
    Mono<Todolist> findByIdAndUsername(String username, Long id);

    @Query("SELECT * FROM todolist WHERE email = :username AND status = :status AND task ILIKE '%' || :task || '%'")
    Flux<Todolist> findAllByTaskContainingAndStatus(String username, String task, String status);

    @Query("SELECT * FROM todolist WHERE email = :username AND task ILIKE '%' || :task || '%'")
    Flux<Todolist> findAllByTaskContaining(String username, String task);

    @Query("SELECT COUNT(*) FROM todolist WHERE email = :username AND status = :status" )
    Mono<Long> countByStatus(String username, String status);

    @Query("SELECT COUNT(*) FROM todolist WHERE email = :username" )
    Mono<Long> countAll(String username);

    @Query("SELECT * FROM todolist")
    Flux<Todolist> findAllData();
}
