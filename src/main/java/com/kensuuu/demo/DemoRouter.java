package com.kensuuu.demo;

import com.kensuuu.demo.todo.TodoHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class DemoRouter {

    @Bean
    public RouterFunction<ServerResponse> route(TodoHandler todoHandler) {
        return RouterFunctions.route()
                .add(todoHandler.routes())
                .build();
    }
}
