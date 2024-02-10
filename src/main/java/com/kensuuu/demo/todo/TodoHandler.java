package com.kensuuu.demo.todo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
@Slf4j
public class TodoHandler {

    private TodoRepository todoRepository;

    public RouterFunction<ServerResponse> routes() {
        return RouterFunctions.route()
                .GET("/todo/{id}", this::get)
                .POST("/todo", this::create)
                .PUT("/todo/{id}", this::update)
                .DELETE("/todo/{id}", this::delete)
                .build();
    }

    public Mono<ServerResponse> get(ServerRequest serverRequest) {
        var id = Long.parseLong(serverRequest.pathVariable("id"));
        return todoRepository.findById(id)
                .flatMap(todo -> ServerResponse.ok().bodyValue(todo))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> create(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Todo.class)
                .flatMap(todoRepository::save)
                .flatMap(response -> ServerResponse.ok().bodyValue(response));
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        var id = Long.parseLong(serverRequest.pathVariable("id"));
        return serverRequest.bodyToMono(Todo.class)
                .flatMap(todo -> todoRepository.findById(id)
                        .flatMap(existingTodo -> todoRepository.save(todo)))
                .flatMap(response -> ServerResponse.ok().bodyValue(response))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        var id = Long.parseLong(serverRequest.pathVariable("id"));
        return todoRepository.findById(id)
                .flatMap(todo -> todoRepository.delete(todo)
                        .then(ServerResponse.ok().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
