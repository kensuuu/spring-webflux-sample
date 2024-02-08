package com.kensuuu.demo.todo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Table("todos")
public class Todo {
    @Id
    private Long id;
    private String task;
    private int status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
