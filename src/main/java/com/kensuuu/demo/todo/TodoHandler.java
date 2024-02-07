package com.kensuuu.demo.todo;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class TodoHandler {

    public RouterFunction<ServerResponse> routes() {
        return RouterFunctions.route()
                .GET("/todo/{id}", this::get)
                .build();
    }

    public Mono<ServerResponse> get(ServerRequest serverRequest) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).build();
    }
}
