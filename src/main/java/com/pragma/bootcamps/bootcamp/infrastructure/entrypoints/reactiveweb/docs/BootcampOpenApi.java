package com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.docs;

import com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.dtos.requests.BootcampRequest;
import com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.dtos.responses.BusinessResponse;
import lombok.experimental.UtilityClass;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;


@UtilityClass
public class BootcampOpenApi {

    private static final String TAG = "Bootcamp";

    private static final String CREATED_CODE = String.valueOf(HttpStatus.CREATED.value());
    private static final String BAD_REQUEST_CODE = String.valueOf(HttpStatus.BAD_REQUEST.value());
    private static final String CONFLICT_CODE = String.valueOf(HttpStatus.CONFLICT.value());

    private static final String CREATED_DESC = "Bootcamp created successfully";
    private static final String BAD_REQUEST_DESC = "Invalid request data";
    private static final String CONFLICT_DESC = "Bootcamp name already exists";

    private static final String OPERATION_SAVE = "saveBootcamp";
    private static final String OPERATION_DESC = "Creates a new bootcamp";

    public void saveBootcamp(Builder builder) {
        builder
                .operationId(OPERATION_SAVE)
                .description(OPERATION_DESC)
                .tag(TAG)
                // Request body
                .requestBody(requestBodyBuilder()
                        .required(true)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(BootcampRequest.class))))
                // 201 Created
                .response(responseBuilder()
                        .responseCode(CREATED_CODE)
                        .description(CREATED_DESC)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(BusinessResponse.class))))
                // 400 Bad Request
                .response(responseBuilder()
                        .responseCode(BAD_REQUEST_CODE)
                        .description(BAD_REQUEST_DESC)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(BusinessResponse.class))))
                // 409 Conflict
                .response(responseBuilder()
                        .responseCode(CONFLICT_CODE)
                        .description(CONFLICT_DESC)
                        .content(contentBuilder()
                                .mediaType(MediaType.APPLICATION_JSON_VALUE)
                                .schema(schemaBuilder()
                                        .implementation(BusinessResponse.class))));
    }

}
