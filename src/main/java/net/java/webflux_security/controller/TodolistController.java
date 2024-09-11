package net.java.webflux_security.controller;


import net.java.webflux_security.model.Todolist;
import net.java.webflux_security.service.TodolistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/todolist")
public class TodolistController {
    @Autowired
    TodolistService todolistService;

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Todolist> addTodolist(@RequestBody Todolist todolistRequest) {
        return todolistService.save(new Todolist(todolistRequest.getTask(), todolistRequest.getNote(), todolistRequest.getStatus()));
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping ("/search")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Todolist> getTodolistByTask(@RequestBody Todolist todolistRequest) {
         return todolistService.findByTaskAndStatusContaining(todolistRequest.getTask(), todolistRequest.getStatus());

    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/filter/{status}")
    public Flux<Todolist> getTodolistByStatus(@PathVariable("status") String status) {
        return todolistService.findAllByStatus(status, 0, 10);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/{status}/{offset}/{pagesize}")
    public Flux<Todolist> getTodolistByStatusPagination(@PathVariable("status") String status, @PathVariable("offset") int offset, @PathVariable("pagesize") int pagesize ) {
        return todolistService.findAllByStatus(status, offset, pagesize);
    }

    @GetMapping
    public Flux<Todolist> getTodolist() {
        return  todolistService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Todolist> getTodolistById(@PathVariable("id") Long id) {
        return  todolistService.findById(id);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping("/{id}")
    public Mono<Todolist> updateTodolist(@PathVariable("id") Long id, @RequestBody Todolist todolistRequest) {
       return todolistService.update(id,todolistRequest);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @DeleteMapping("/{id}")
    public Mono<Void> deleteTodolist(@PathVariable("id") Long id) {
        return todolistService.deleteById(id);
    }

//    @GetMapping("/sort/{field}")
//    private Flux<Todolist> getProductsWithSort(@PathVariable String field) {
//        List<Todolist> allProducts = todolistService.findTodolistWithSorting(field);
//        return new APIResponse<>(allProducts.size(), allProducts);
//    }


//    @GetMapping("/paginationAndSort/{offset}/{pageSize}/{field}")
//    private APIResponse<Page<Todolist>> getProductsWithPaginationAndSort(@PathVariable int offset, @PathVariable int pageSize,@PathVariable String field) {
//        Page<Todolist> todolistWithPagination = todolistService.findTodolistWithPaginationAndSorting(offset, pageSize, field);
//        return new APIResponse<>(todolistWithPagination.getSize(), todolistWithPagination);
//    }

    @GetMapping("/{offset}/{pageSize}")
    private Flux<Todolist> getTodolistWithPagination(@PathVariable int offset, @PathVariable int pageSize) {
        return todolistService.findTodolistWithPagination(offset, pageSize);
    }

    @GetMapping("/count/{status}")
    private Mono<Long> getCountData(@PathVariable String status){
        return  todolistService.countByStatus(status);

    }
    @GetMapping("/count")
    private Mono<Long> getCountAll(){
        return  todolistService.countAll();

    }

}
