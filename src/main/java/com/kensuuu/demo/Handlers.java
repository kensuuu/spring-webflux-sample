package com.kensuuu.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class Handlers {

    public static <T, R extends Mono<ServerResponse>> Mono<ServerResponse> handleBody(ServerRequest serverRequest, Class<T> clazz, Function<T, R> function) {
        return serverRequest
                .bodyToMono(clazz)
                .flatMap(function)
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(WebExchangeBindException.class, e -> ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON).bodyValue(e.getBody()))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build())
                .flatMap(response -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(response));
    }
}
