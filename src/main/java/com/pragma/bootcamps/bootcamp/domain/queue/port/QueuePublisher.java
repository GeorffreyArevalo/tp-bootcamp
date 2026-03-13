package com.pragma.bootcamps.bootcamp.domain.queue.port;

import com.pragma.bootcamps.bootcamp.domain.queue.BootcampMessage;
import reactor.core.publisher.Mono;

public interface QueuePublisher {
    Mono<Void> sendBootcampReportMessage(BootcampMessage message);
}
