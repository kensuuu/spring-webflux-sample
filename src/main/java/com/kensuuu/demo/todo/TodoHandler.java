package com.kensuuu.demo.todo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.kensuuu.demo.Handlers.handleQueryParam;

@Component
@Slf4j
public class TodoHandler {

    public RouterFunction<ServerResponse> routes() {
        return RouterFunctions.route()
                .GET("/todo", this::get)
                .build();
    }

    public Mono<ServerResponse> get(ServerRequest serverRequest) {
        return handleQueryParam(serverRequest, GetTodoRequest.class, request -> {
            log.info(request.toString());
            return Mono.empty();
        });
    }
}
