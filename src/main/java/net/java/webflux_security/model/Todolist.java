package net.java.webflux_security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor

public class Todolist {
    @Id
    private Long id;

    private String task;
    private String note;
    private String status;

    public Todolist(String task, String note, String status) {
        this.task = task;
        this.note = note;
        this.status = status;
    }
}
