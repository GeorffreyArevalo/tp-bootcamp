package com.pragma.bootcamps.bootcamp.infrastructure.adapters.queue.rabbitmq.adapter;

import com.pragma.bootcamps.bootcamp.domain.queue.BootcampMessage;
import com.pragma.bootcamps.bootcamp.domain.queue.port.QueuePublisherPort;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
public class RabbitMqPublisherPortAdapter implements QueuePublisherPort {

    private final Queue queue;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public Mono<Void> sendBootcampReportMessage(BootcampMessage message) {
        return Mono.fromRunnable(() ->
            rabbitTemplate.convertAndSend(queue.getName(), message)
        ).subscribeOn(Schedulers.boundedElastic()).then();
    }

}
