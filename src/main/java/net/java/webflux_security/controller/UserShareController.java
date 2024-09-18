package net.java.webflux_security.controller;


import net.java.webflux_security.dto.UserShareDto;
import net.java.webflux_security.model.Todolist;
import net.java.webflux_security.model.Userdata;
import net.java.webflux_security.service.UserService;
import net.java.webflux_security.service.UserShareService;
import net.java.webflux_security.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/share")
public class UserShareController {
    @Autowired
    UserShareService userShareService;

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
    @DeleteMapping("/read/delete/{username}")
    public Mono<Userdata> deleteUserReadAccess(@RequestHeader("Authorization") String token, @PathVariable("username") String toUsername) {
        String jwtToken = token.substring(7);
        String fromUsername = jwtUtil.getUsernameFromToken(jwtToken);

        return userShareService.deleteReadUserAccess(fromUsername, toUsername);
    }




    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping("/editor")
    public Mono<Userdata> addUserEditorAccess(@RequestHeader("Authorization") String token, @RequestBody UserShareDto userShareDto) {
        String jwtToken = token.substring(7);
        String fromUsername = jwtUtil.getUsernameFromToken(jwtToken);

        return userShareService.updateEditorUserAccess(fromUsername, userShareDto.getToUsername());
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @DeleteMapping("/editor/delete/{username}")
    public Mono<Userdata> deleteUserEditorAccess(@RequestHeader("Authorization") String token, @PathVariable("username") String toUsername) {
        String jwtToken = token.substring(7);
        String fromUsername = jwtUtil.getUsernameFromToken(jwtToken);

        return userShareService.deleteEditorUserAccess(fromUsername, toUsername);
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
    public Mono<Todolist> addTodolistEditorAccess(@RequestHeader("Authorization") String token, @PathVariable("username") String toUsername, @RequestBody Todolist todolist) {
        String jwtToken = token.substring(7);
        String fromUsername = jwtUtil.getUsernameFromToken(jwtToken);

        return userShareService.addTodolistEditorAccess(fromUsername, toUsername, new Todolist(todolist.getTask(), todolist.getNote(), todolist.getStatus(), toUsername));
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping("/edit/{username}/{id}")
    public Mono<Todolist> editTodolistEditorAccess(@RequestHeader("Authorization") String token, @PathVariable("username") String toUsername, @PathVariable("id") Long id, @RequestBody Todolist todolist) {
        String jwtToken = token.substring(7);
        String fromUsername = jwtUtil.getUsernameFromToken(jwtToken);

        return userShareService.editTodolistEditorAccess(fromUsername, toUsername, todolist, id);
    }


    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping("/delete/{username}/{id}")
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
