package net.java.webflux_security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class TodolistDto {
    @NotBlank(message = "Task should not be empty")
    String task;

    String note;

    @NotBlank(message = "Status should not be empty")
    String status;

    String toUsername;
}
