package net.java.webflux_security.repository;

import net.java.webflux_security.model.Todolist;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.r2dbc.repository.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TodolistRepository extends R2dbcRepository<Todolist, Long> {
    @Query("SELECT * FROM todolist WHERE status = :status AND task LIKE '%' || :task || '%'")
    Flux<Todolist> findAllByTaskContainingAndStatus(String task, String status);
    @Query("SELECT * FROM todolist WHERE status = :status ORDER BY todolist.id DESC LIMIT :pageSize OFFSET :offset")
    Flux<Todolist> findAllByStatus(String status, int offset, int pageSize);
    @Query("SELECT COUNT(*) FROM todolist WHERE status = :status" )
    Mono<Long> countByStatus(String status);
    @Query("SELECT COUNT(*) FROM todolist" )
    Mono<Long> countAll();
    @Query("SELECT * FROM todolist ORDER BY todolist.id DESC LIMIT :pageSize OFFSET :offset")
    Flux<Todolist> findAllPagination(int offset, int pageSize);
}
