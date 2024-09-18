package net.java.webflux_security.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class UserShareDto {
    @NotBlank(message = "Username should not be empty")
    String toUsername;
    String task;
}
