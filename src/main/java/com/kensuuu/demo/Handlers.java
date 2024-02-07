package com.kensuuu.demo;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.function.Function;

public class Handlers {

    private static final ConversionService conversionService = new DefaultConversionService();

    public static <T, R extends Mono<ServerResponse>> Mono<ServerResponse> handleBody(ServerRequest serverRequest, Class<T> clazz, Function<T, R> function) {
        return serverRequest
                .bodyToMono(clazz)
                .flatMap(function)
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(WebExchangeBindException.class, e -> ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON).bodyValue(e.getBody()))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build())
                .flatMap(response -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(response));
    }

    public static <T, R extends Mono<ServerResponse>> Mono<ServerResponse> handleQueryParam(ServerRequest serverRequest, Class<T> clazz, Function<T, R> function) {
        return Mono.fromCallable(() -> {
                    T instance = clazz.getDeclaredConstructor().newInstance();
                    PropertyDescriptor[] propertyDescriptors = org.springframework.beans.BeanUtils.getPropertyDescriptors(clazz);

                    for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                        Method writeMethod = propertyDescriptor.getWriteMethod();
                        if (writeMethod != null) {
                            Class<?> propertyType = propertyDescriptor.getPropertyType();
                            String propertyName = propertyDescriptor.getName();
                            serverRequest.queryParam(propertyName).ifPresent(value -> {
                                if (conversionService.canConvert(String.class, propertyType)) {
                                    Object convertedValue = conversionService.convert(value, propertyType);
                                    try {
                                        writeMethod.invoke(instance, convertedValue);
                                    } catch (Exception e) {
                                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid request parameter " + propertyName + " with value " + value, e);
                                    }
                                }
                            });
                        }
                    }
                    return instance;
                })
                .flatMap(function)
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(WebExchangeBindException.class, e -> ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON).bodyValue(e.getBody()))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build())
                .flatMap(response -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(response));
    }
}
