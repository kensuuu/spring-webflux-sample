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
                .build();
    }

    public Mono<ServerResponse> get(ServerRequest serverRequest) {
        var id = Long.parseLong(serverRequest.pathVariable("id"));
        return todoRepository.findById(id)
                .flatMap(todo -> ServerResponse.ok().bodyValue(todo))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
