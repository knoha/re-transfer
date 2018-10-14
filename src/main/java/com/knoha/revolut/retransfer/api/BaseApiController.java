package com.knoha.revolut.retransfer.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.knoha.revolut.retransfer.Definitions;
import com.knoha.revolut.retransfer.exceptions.MarshallingException;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.Collections;

public abstract class BaseApiController {

    private static final String INFO = "info";
    private static final String ERROR = "error";

    private final Request request;
    private final Response response;

    protected BaseApiController(final Request request, final Response response) {
        this.request = request;
        this.response = response;

        getResponse().type(Definitions.Type.JSON);
    }

    public abstract Object handle();

    protected Request getRequest() {
        return request;
    }

    protected Response getResponse() {
        return response;
    }

    protected ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    protected <T> T readPayload(Class<T> payloadType) throws MarshallingException {
        try {
            return getObjectMapper().readValue(getRequest().body(), payloadType);
        } catch (final IOException e) {
            throw new MarshallingException(e);
        }
    }

    protected String ok(final String message) {
        return ok(Collections.singletonMap(INFO, message));
    }

    protected String ok(final Object object) {
        getResponse().status(200);
        return new Gson().toJson(object);
    }

    protected String badRequest(final String message) {
        getResponse().status(400);
        return new Gson().toJson(Collections.singletonMap(ERROR, message));
    }

    protected String notFound(final String message) {
        getResponse().status(404);
        return new Gson().toJson(Collections.singletonMap(ERROR, message));
    }

    protected String serverError(final String message) {
        getResponse().status(500);
        return new Gson().toJson(Collections.singletonMap(ERROR, message));
    }
}
