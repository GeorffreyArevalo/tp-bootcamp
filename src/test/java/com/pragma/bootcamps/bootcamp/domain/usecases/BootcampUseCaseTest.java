package com.pragma.bootcamps.bootcamp.domain.usecases;

import com.pragma.bootcamps.bootcamp.domain.clients.CapabilityAssociationClientPort;
import com.pragma.bootcamps.bootcamp.domain.clients.TechnologyClientPort;
import com.pragma.bootcamps.bootcamp.domain.enums.ExceptionMessages;
import com.pragma.bootcamps.bootcamp.domain.exceptions.BootcampAlreadyExistsException;
import com.pragma.bootcamps.bootcamp.domain.exceptions.BootcampCapabilitiesCountException;
import com.pragma.bootcamps.bootcamp.domain.exceptions.NotFoundException;
import com.pragma.bootcamps.bootcamp.domain.exceptions.SagaCompensationException;
import com.pragma.bootcamps.bootcamp.domain.models.Bootcamp;
import com.pragma.bootcamps.bootcamp.domain.models.CapabilitySummary;
import com.pragma.bootcamps.bootcamp.domain.models.TechnologySummary;
import com.pragma.bootcamps.bootcamp.domain.queue.BootcampMessage;
import com.pragma.bootcamps.bootcamp.domain.queue.port.QueuePublisherPort;
import com.pragma.bootcamps.bootcamp.domain.spi.BootcampPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BootcampUseCaseTest {

    @Mock
    private BootcampPersistencePort bootcampPersistencePort;

    @Mock
    private CapabilityAssociationClientPort capabilityAssociationClientPort;

    @Mock
    private TechnologyClientPort technologyClientPort;

    @Mock
    private QueuePublisherPort queuePublisher;

    private BootcampUseCase bootcampUseCase;

    @BeforeEach
    void setUp() {
        bootcampUseCase = new BootcampUseCase(
                bootcampPersistencePort,
                capabilityAssociationClientPort,
                technologyClientPort,
                queuePublisher
        );
    }

    @Test
    void saveBootcampShouldSaveAndAssociateCapabilitiesSuccessfully() {
        Bootcamp input = baseBootcamp().toBuilder().id(null).build();
        Bootcamp saved = input.toBuilder().id(1L).build();

        when(bootcampPersistencePort.findBootcampByName(input.getName())).thenReturn(Mono.empty());
        when(bootcampPersistencePort.saveBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(saved));
        when(capabilityAssociationClientPort.associateCapabilities(saved.getId(), input.getCapabilityIds()))
                .thenReturn(Mono.empty());
        when(queuePublisher.sendBootcampReportMessage(any(BootcampMessage.class))).thenReturn(Mono.empty());

        StepVerifier.create(bootcampUseCase.saveBootcamp(input))
                .assertNext(result -> {
                    assertEquals(1L, result.getId());
                    assertEquals(2, result.getCapabilityCount());
                })
                .verifyComplete();

        ArgumentCaptor<BootcampMessage> messageCaptor = ArgumentCaptor.forClass(BootcampMessage.class);
        verify(queuePublisher).sendBootcampReportMessage(messageCaptor.capture());
        assertEquals(saved.getId(), messageCaptor.getValue().getBootcampId());
        assertEquals(saved.getCapabilityCount(), messageCaptor.getValue().getCapabilityCount());
        verify(capabilityAssociationClientPort).associateCapabilities(saved.getId(), input.getCapabilityIds());
    }

    @Test
    void saveBootcampShouldFailWhenCapabilitiesCountIsInvalid() {
        Bootcamp input = baseBootcamp().toBuilder().capabilityIds(List.of()).build();

        StepVerifier.create(bootcampUseCase.saveBootcamp(input))
                .expectErrorSatisfies(error -> {
                    assertInstanceOf(BootcampCapabilitiesCountException.class, error);
                    assertEquals(ExceptionMessages.BOOTCAMP_CAPABILITIES_COUNT_INVALID.getMessage(), error.getMessage());
                })
                .verify();

        verify(bootcampPersistencePort, never()).findBootcampByName(anyString());
        verify(bootcampPersistencePort, never()).saveBootcamp(any(Bootcamp.class));
    }

    @Test
    void saveBootcampShouldFailWhenCapabilitiesAreRepeated() {
        Bootcamp input = baseBootcamp().toBuilder().capabilityIds(List.of(9L, 9L)).build();

        StepVerifier.create(bootcampUseCase.saveBootcamp(input))
                .expectErrorSatisfies(error -> {
                    assertInstanceOf(BootcampCapabilitiesCountException.class, error);
                    assertEquals(ExceptionMessages.BOOTCAMP_CAPABILITIES_REPEATED.getMessage(), error.getMessage());
                })
                .verify();

        verify(bootcampPersistencePort, never()).findBootcampByName(anyString());
        verify(bootcampPersistencePort, never()).saveBootcamp(any(Bootcamp.class));
    }

    @Test
    void saveBootcampShouldFailWhenNameAlreadyExists() {
        Bootcamp input = baseBootcamp();

        when(bootcampPersistencePort.findBootcampByName(input.getName()))
                .thenReturn(Mono.just(baseBootcamp().toBuilder().id(77L).build()));

        StepVerifier.create(bootcampUseCase.saveBootcamp(input))
                .expectErrorSatisfies(error -> {
                    assertInstanceOf(BootcampAlreadyExistsException.class, error);
                    assertEquals(ExceptionMessages.BOOTCAMP_ALREADY_EXISTS.format(input.getName()), error.getMessage());
                })
                .verify();

        verify(bootcampPersistencePort, never()).saveBootcamp(any(Bootcamp.class));
        verify(capabilityAssociationClientPort, never()).associateCapabilities(anyLong(), any());
    }

    @Test
    void saveBootcampShouldCompensateWhenAssociationFails() {
        Bootcamp input = baseBootcamp().toBuilder().id(null).build();
        Bootcamp saved = input.toBuilder().id(10L).build();

        when(bootcampPersistencePort.findBootcampByName(input.getName())).thenReturn(Mono.empty());
        when(bootcampPersistencePort.saveBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(saved));
        when(capabilityAssociationClientPort.associateCapabilities(saved.getId(), input.getCapabilityIds()))
                .thenReturn(Mono.error(new RuntimeException("association failure")));
        when(bootcampPersistencePort.deleteBootcamp(saved.getId())).thenReturn(Mono.empty());

        StepVerifier.create(bootcampUseCase.saveBootcamp(input))
                .expectErrorSatisfies(error -> {
                    assertInstanceOf(SagaCompensationException.class, error);
                    assertEquals(ExceptionMessages.SAGA_COMPENSATION_ASSOCIATION_FAILURE.getMessage(), error.getMessage());
                })
                .verify();

        verify(bootcampPersistencePort).deleteBootcamp(saved.getId());
        verify(queuePublisher, never()).sendBootcampReportMessage(any(BootcampMessage.class));
    }

    @Test
    void getBootcampsWithCapabilitiesShouldAggregateData() {
        Bootcamp bootcamp = baseBootcamp().toBuilder().id(3L).build();
        CapabilitySummary capabilityOne = CapabilitySummary.builder().id(1L).name("Backend").build();
        CapabilitySummary capabilityTwo = CapabilitySummary.builder().id(2L).name("DevOps").build();
        TechnologySummary javaTech = new TechnologySummary(100L, "Java");
        TechnologySummary springTech = new TechnologySummary(101L, "Spring");
        TechnologySummary dockerTech = new TechnologySummary(102L, "Docker");

        when(bootcampPersistencePort.findBootcampsPagedAndSorted(0, 10, "name", "asc"))
                .thenReturn(Flux.just(bootcamp));
        when(capabilityAssociationClientPort.getCapabilitiesByBootcampId(bootcamp.getId()))
                .thenReturn(Flux.just(capabilityOne, capabilityTwo));
        when(technologyClientPort.getTechnologiesByCapabilityId(1L))
                .thenReturn(Flux.just(javaTech, springTech));
        when(technologyClientPort.getTechnologiesByCapabilityId(2L))
                .thenReturn(Flux.just(dockerTech));

        StepVerifier.create(bootcampUseCase.getBootcampsWithCapabilities(0, 10, "name", "asc"))
                .assertNext(result -> {
                    assertEquals(bootcamp.getId(), result.getId());
                    assertEquals(2, result.getCapabilities().size());
                    assertEquals("Backend", result.getCapabilities().get(0).getName());
                    assertEquals(2, result.getCapabilities().get(0).getTechnologies().size());
                    assertEquals("DevOps", result.getCapabilities().get(1).getName());
                    assertEquals(1, result.getCapabilities().get(1).getTechnologies().size());
                })
                .verifyComplete();
    }

    @Test
    void deleteBootcampShouldDeleteAssociationsAndBootcamp() {
        Long bootcampId = 5L;
        when(bootcampPersistencePort.findBootcampById(bootcampId)).thenReturn(Mono.just(baseBootcamp().toBuilder().id(bootcampId).build()));
        when(capabilityAssociationClientPort.deleteAssociatedDataByBootcampId(bootcampId)).thenReturn(Mono.empty());
        when(bootcampPersistencePort.deleteBootcamp(bootcampId)).thenReturn(Mono.empty());

        StepVerifier.create(bootcampUseCase.deleteBootcamp(bootcampId))
                .verifyComplete();

        verify(capabilityAssociationClientPort).deleteAssociatedDataByBootcampId(bootcampId);
        verify(bootcampPersistencePort).deleteBootcamp(bootcampId);
    }

    @Test
    void deleteBootcampShouldFailWhenBootcampDoesNotExist() {
        Long bootcampId = 99L;
        when(bootcampPersistencePort.findBootcampById(bootcampId)).thenReturn(Mono.empty());

        StepVerifier.create(bootcampUseCase.deleteBootcamp(bootcampId))
                .expectErrorSatisfies(error -> {
                    assertInstanceOf(NotFoundException.class, error);
                    assertEquals(ExceptionMessages.BOOTCAMP_NOT_FOUND.format(bootcampId), error.getMessage());
                })
                .verify();

        verify(capabilityAssociationClientPort, never()).deleteAssociatedDataByBootcampId(anyLong());
        verify(bootcampPersistencePort, never()).deleteBootcamp(anyLong());
    }

    @Test
    void validateConflictsShouldReturnFalseWhenThereIsScheduleOverlap() {
        Long newBootcampId = 11L;
        Bootcamp candidate = baseBootcamp().toBuilder()
                .id(newBootcampId)
                .releaseDate(LocalDate.of(2026, 3, 10))
                .duration(5)
                .build();
        Bootcamp enrolled = baseBootcamp().toBuilder()
                .id(12L)
                .releaseDate(LocalDate.of(2026, 3, 8))
                .duration(4)
                .build();

        when(bootcampPersistencePort.findBootcampById(newBootcampId)).thenReturn(Mono.just(candidate));
        when(bootcampPersistencePort.findAllByIds(List.of(12L))).thenReturn(Flux.just(enrolled));

        StepVerifier.create(bootcampUseCase.validateConflicts(newBootcampId, List.of(12L)))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void validateConflictsShouldReturnTrueWhenThereIsNoScheduleOverlap() {
        Long newBootcampId = 21L;
        Bootcamp candidate = baseBootcamp().toBuilder()
                .id(newBootcampId)
                .releaseDate(LocalDate.of(2026, 3, 20))
                .duration(5)
                .build();
        Bootcamp enrolled = baseBootcamp().toBuilder()
                .id(22L)
                .releaseDate(LocalDate.of(2026, 3, 1))
                .duration(4)
                .build();

        when(bootcampPersistencePort.findBootcampById(newBootcampId)).thenReturn(Mono.just(candidate));
        when(bootcampPersistencePort.findAllByIds(List.of(22L))).thenReturn(Flux.just(enrolled));

        StepVerifier.create(bootcampUseCase.validateConflicts(newBootcampId, List.of(22L)))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void validateConflictsShouldFailWhenCandidateBootcampDoesNotExist() {
        Long newBootcampId = 404L;
        when(bootcampPersistencePort.findBootcampById(newBootcampId)).thenReturn(Mono.empty());

        StepVerifier.create(bootcampUseCase.validateConflicts(newBootcampId, List.of(1L, 2L)))
                .expectErrorSatisfies(error -> {
                    assertInstanceOf(NotFoundException.class, error);
                    assertEquals(ExceptionMessages.BOOTCAMP_NOT_FOUND.format(newBootcampId), error.getMessage());
                })
                .verify();

        verify(bootcampPersistencePort, never()).findAllByIds(any());
    }

    private Bootcamp baseBootcamp() {
        return Bootcamp.builder()
                .name("Bootcamp Reactive")
                .description("Reactive programming")
                .releaseDate(LocalDate.of(2026, 3, 1))
                .duration(10)
                .capabilityCount(2)
                .capabilityIds(List.of(1L, 2L))
                .build();
    }
}
