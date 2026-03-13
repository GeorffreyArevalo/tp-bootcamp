package com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.docs;

import com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.dtos.requests.BootcampRequest;
import com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.dtos.responses.BusinessResponse;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.experimental.UtilityClass;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;


@UtilityClass
public class BootcampOpenApi {

    private static final String TAG = "Bootcamp";

    private static final String CREATED_CODE = String.valueOf(HttpStatus.CREATED.value());
    private static final String BAD_REQUEST_CODE = String.valueOf(HttpStatus.BAD_REQUEST.value());
    private static final String CONFLICT_CODE = String.valueOf(HttpStatus.CONFLICT.value());
    private static final String OK_CODE = String.valueOf(HttpStatus.OK.value());
    private static final String NO_CONTENT_CODE = String.valueOf(HttpStatus.NO_CONTENT.value());
    private static final String INTERNAL_ERROR_CODE = String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());
    private static final String NOT_FOUND_CODE = String.valueOf(HttpStatus.NOT_FOUND.value());

    private static final String CREATED_DESC = "Bootcamp created successfully";
    private static final String BAD_REQUEST_DESC = "Invalid request data";
    private static final String CONFLICT_DESC = "Bootcamp name already exists";
    private static final String OK_DESC = "Bootcamps listed successfully";
    private static final String DELETE_204_DESC = "No content. Bootcamp and all exclusive associations were deleted successfully. Response body is empty.";
    private static final String DELETE_500_DESC = "Internal server error. The bootcamp or its associations could not be deleted. Response body contains an error message.";
    private static final String DELETE_404_DESC = "Not Found. The bootcamp with the specified ID does not exist. Response body contains an error message.";

    private static final String OPERATION_SAVE = "saveBootcamp";
    private static final String OPERATION_DESC = "Creates a new bootcamp";
    private static final String OPERATION_LIST = "listBootcamps";
    private static final String OPERATION_LIST_DESC = "Lists all bootcamps with pagination and sorting";
    private static final String OPERATION_DELETE = "deleteBootcamp";

    private static final String PARAM_PAGE = "page";
    private static final String PARAM_SIZE = "size";
    private static final String PARAM_SORT_BY = "sortBy";
    private static final String PARAM_ORDER = "order";

    private static final String DEFAULT_PAGE_DESC = "Page number for pagination (default: 0)";
    private static final String DEFAULT_SIZE_DESC = "Page size for pagination (default: 10)";
    private static final String SORT_BY_DESC = "Field to sort by (name or capabilityCount)";
    private static final String ORDER_DESC = "Sort order (asc or desc)";

    private static final String DELETE_DESCRIPTION = "Deletes a bootcamp by its ID. This operation is transactional: first, all associations and orphan dependencies are deleted in the capability microservice (capabilities and technologies exclusive to the bootcamp), and only if successful, the bootcamp record is deleted in this service.";
    private static final String DELETE_PARAM_NAME = "bootcampId";
    private static final String DELETE_PARAM_DESC = "Unique identifier of the bootcamp to delete";


    public void saveBootcamp(Builder builder) {
        builder
                .operationId(OPERATION_SAVE)
                .description(OPERATION_DESC)
                .tag(TAG)
                .requestBody(requestBodyBuilder()
                        .required(true)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(BootcampRequest.class))))

                .response(responseBuilder()
                        .responseCode(CREATED_CODE)
                        .description(CREATED_DESC)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(BusinessResponse.class))))

                .response(responseBuilder()
                        .responseCode(BAD_REQUEST_CODE)
                        .description(BAD_REQUEST_DESC)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(BusinessResponse.class))))

                .response(responseBuilder()
                        .responseCode(CONFLICT_CODE)
                        .description(CONFLICT_DESC)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(BusinessResponse.class))));
    }


    public void listBootcamps(Builder builder) {
        builder
                .operationId(OPERATION_LIST)
                .description(OPERATION_LIST_DESC)
                .tag(TAG)
                .parameter(parameterBuilder()
                        .name(PARAM_PAGE)
                        .description(DEFAULT_PAGE_DESC)
                        .required(false))
                .parameter(parameterBuilder()
                        .name(PARAM_SIZE)
                        .description(DEFAULT_SIZE_DESC)
                        .required(false))
                .parameter(parameterBuilder()
                        .name(PARAM_SORT_BY)
                        .description(SORT_BY_DESC)
                        .required(false))
                .parameter(parameterBuilder()
                        .name(PARAM_ORDER)
                        .description(ORDER_DESC)
                        .required(false))
                .response(responseBuilder()
                        .responseCode(OK_CODE)
                        .description(OK_DESC)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(BusinessResponse.class))));
    }

    public void deleteBootcamp(Builder builder) {
        builder
                .operationId(OPERATION_DELETE)
                .description(DELETE_DESCRIPTION)
                .tag(TAG)
                .parameter(parameterBuilder()
                        .name(DELETE_PARAM_NAME)
                        .description(DELETE_PARAM_DESC)
                        .in(ParameterIn.PATH)
                        .required(true)
                )
                .response(responseBuilder()
                        .responseCode(NO_CONTENT_CODE)
                        .description(DELETE_204_DESC)
                )
                .response(responseBuilder()
                        .responseCode(INTERNAL_ERROR_CODE)
                        .description(DELETE_500_DESC)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(BusinessResponse.class))
                        )
                )
                .response(responseBuilder()
                        .responseCode(NOT_FOUND_CODE)
                        .description(DELETE_404_DESC)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder().implementation(BusinessResponse.class))
                        )
                );
    }

}
