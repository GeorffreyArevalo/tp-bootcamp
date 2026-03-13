package com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.routes;


import com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.docs.BootcampOpenApi;
import com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.handlers.BootcampHandler;
import com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.routes.paths.BootcampPath;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;

@Configuration
@RequiredArgsConstructor
public class BootcampRouter {

    private final BootcampPath bootcampPath;

    @Bean
    public RouterFunction<ServerResponse> routerFunction(BootcampHandler handler) {
        return route()
                .POST(bootcampPath.getBootcamps(), handler::listenSaveBootcamp, BootcampOpenApi::saveBootcamp)
                .GET(bootcampPath.getBootcampsList(), handler::listenListBootcamps, BootcampOpenApi::listBootcamps)
                .GET(bootcampPath.getValidateConflicts(), handler::listenValidateConflicts, BootcampOpenApi::validateConflicts)
                .DELETE(bootcampPath.getDeleteBootcampById(), handler::listenDeleteBootcamp, BootcampOpenApi::deleteBootcamp)
                .build();
    }

}
