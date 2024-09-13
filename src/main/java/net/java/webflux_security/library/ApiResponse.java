package net.java.webflux_security.library;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class ApiResponse {
    private int status;
    private String message;
    private Object data;
}
