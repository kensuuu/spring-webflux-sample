package com.kensuuu.demo.todo;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@NoArgsConstructor
@Table("todos")
public class Todo {
    @Id
    private Long id;
    private String task;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
