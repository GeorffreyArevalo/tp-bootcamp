package com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ValidatorUtil {

    private final Validator validator;

    public <T> Mono<T> validate(T target){
        return Mono.fromCallable(() -> {
            Set<ConstraintViolation<T>> violations = validator.validate(target);
            if(!violations.isEmpty()){
                throw new ConstraintViolationException(violations);
            }
            return target;
        }).subscribeOn(Schedulers.boundedElastic());
    }

}
