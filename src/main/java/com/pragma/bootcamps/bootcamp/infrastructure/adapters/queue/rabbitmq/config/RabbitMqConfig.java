package com.pragma.bootcamps.bootcamp.infrastructure.adapters.queue.rabbitmq.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${queue.rabbitmq.name-queue}")
    private String nameQueue;

    @Bean
    public Queue queue() {
        return new Queue(nameQueue, true);
    }

}
