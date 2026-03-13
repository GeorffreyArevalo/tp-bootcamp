package com.pragma.bootcamps.bootcamp.domain.usecases;

import com.pragma.bootcamps.bootcamp.domain.api.BootcampServicePort;
import com.pragma.bootcamps.bootcamp.domain.clients.CapabilityAssociationClientPort;
import com.pragma.bootcamps.bootcamp.domain.clients.TechnologyClientPort;
import com.pragma.bootcamps.bootcamp.domain.enums.ExceptionMessages;
import com.pragma.bootcamps.bootcamp.domain.exceptions.BootcampAlreadyExistsException;
import com.pragma.bootcamps.bootcamp.domain.exceptions.BootcampCapabilitiesCountException;
import com.pragma.bootcamps.bootcamp.domain.exceptions.NotFoundException;
import com.pragma.bootcamps.bootcamp.domain.exceptions.SagaCompensationException;
import com.pragma.bootcamps.bootcamp.domain.models.Bootcamp;
import com.pragma.bootcamps.bootcamp.domain.models.BootcampWithCapabilities;
import com.pragma.bootcamps.bootcamp.domain.spi.BootcampPersistencePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.pragma.bootcamps.bootcamp.domain.constants.BootcampConstants.MAX_CAPS;
import static com.pragma.bootcamps.bootcamp.domain.constants.BootcampConstants.MIN_CAPS;
import static com.pragma.bootcamps.bootcamp.domain.utils.BootcampUtils.buildBootcampWithCapabilities;
import static com.pragma.bootcamps.bootcamp.domain.utils.BootcampUtils.buildCapabilitySummaryWithTechnologies;

@RequiredArgsConstructor
public class BootcampUseCase implements BootcampServicePort {

    private final BootcampPersistencePort bootcampPersistencePort;
    private final CapabilityAssociationClientPort capabilityAssociationClientPort;
    private final TechnologyClientPort technologyClientPort;

    public Mono<Bootcamp> saveBootcamp(Bootcamp bootcamp) {
        bootcamp.setCapabilityCount(bootcamp.getCapabilityIds().size());
        return Mono.just(bootcamp)
                .filter(bc -> isValidCapabilitiesCount(bc.getCapabilityIds(), MIN_CAPS, MAX_CAPS))
                .switchIfEmpty(Mono.error(new BootcampCapabilitiesCountException(
                        ExceptionMessages.BOOTCAMP_CAPABILITIES_COUNT_INVALID.format())))
                .filter(bc -> hasNoRepeatedCapabilities(bc.getCapabilityIds()))
                .switchIfEmpty(Mono.error(new BootcampCapabilitiesCountException(
                        ExceptionMessages.BOOTCAMP_CAPABILITIES_REPEATED.getMessage())))
                .flatMap(this::validateUniqueName)
                .flatMap(this::saveAndAssociateCapabilities);
    }

    public Flux<BootcampWithCapabilities> getBootcampsWithCapabilities(int page, int size, String sortBy, String order) {
        return bootcampPersistencePort.findBootcampsPagedAndSorted(page, size, sortBy, order)
                .flatMapSequential(bootcamp -> capabilityAssociationClientPort.getCapabilitiesByBootcampId(bootcamp.getId())
                        .flatMapSequential(capability -> technologyClientPort.getTechnologiesByCapabilityId(capability.getId())
                                .collectList()
                                .map(technologies -> buildCapabilitySummaryWithTechnologies(capability, technologies))
                        )
                        .collectList()
                        .map(capabilities -> buildBootcampWithCapabilities(bootcamp, capabilities))
                );
    }

    public Mono<Void> deleteBootcamp(Long bootcampId) {
        return bootcampPersistencePort.findBootcampById(bootcampId)
                .switchIfEmpty(Mono.error(new NotFoundException(ExceptionMessages.BOOTCAMP_NOT_FOUND.format(bootcampId))))
                .flatMap(bootcamp -> capabilityAssociationClientPort.deleteAssociatedDataByBootcampId(bootcampId)
                        .then(bootcampPersistencePort.deleteBootcamp(bootcampId)));
    }

    private Mono<Bootcamp> validateUniqueName(Bootcamp bootcamp) {
        return bootcampPersistencePort.findBootcampByName(bootcamp.getName())
                .flatMap(existing -> Mono.<Bootcamp>error(new BootcampAlreadyExistsException(
                        ExceptionMessages.BOOTCAMP_ALREADY_EXISTS.format(bootcamp.getName())))
                )
                .switchIfEmpty(Mono.just(bootcamp));
    }

    private Mono<Bootcamp> saveAndAssociateCapabilities(Bootcamp bootcamp) {
        var capabilityIds = bootcamp.getCapabilityIds();
        return bootcampPersistencePort.saveBootcamp(bootcamp)
                .flatMap(savedBootcamp -> capabilityAssociationClientPort
                        .associateCapabilities(savedBootcamp.getId(), capabilityIds)
                        .thenReturn(savedBootcamp)
                        .onErrorResume(e -> bootcampPersistencePort.deleteBootcamp(savedBootcamp.getId())
                                .then(Mono.error(new SagaCompensationException(
                                        ExceptionMessages.SAGA_COMPENSATION_ASSOCIATION_FAILURE.getMessage()))
                                ))
                );
    }

    private static boolean isValidCapabilitiesCount(List<Long> capabilityIds, int min, int max) {
        return capabilityIds != null && capabilityIds.size() >= min && capabilityIds.size() <= max;
    }

    private static boolean hasNoRepeatedCapabilities(List<Long> capabilityIds) {
        return capabilityIds != null && capabilityIds.stream().distinct().count() == capabilityIds.size();
    }
}
