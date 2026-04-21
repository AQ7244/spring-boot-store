package com.training.store.common.wrappers;

import com.training.store.common.dtos.success.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.time.Instant;
import java.util.Set;

@RestControllerAdvice
public class GlobalResponseWrapper implements ResponseBodyAdvice<Object> {

    private final HttpServletRequest request;

    // Endpoints that should NOT be wrapped
    private static final Set<String> EXCLUDED_PATHS = Set.of(
            "/webhook",
            "/actuator",
            "/swagger-ui",
            "/v3/api-docs"
    );

    public GlobalResponseWrapper(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class selectedConverterType,
            ServerHttpRequest serverHttpRequest,
            ServerHttpResponse serverHttpResponse
    ) {

        String path = request.getRequestURI();

        // ----------------------------------------------------
        // 1. Skip excluded endpoints
        // ----------------------------------------------------
        if (isExcludedPath(path)) {
            return body;
        }

        // ----------------------------------------------------
        // 2. Skip Swagger / OpenAPI / docs explicitly
        // ----------------------------------------------------
        if (isDocumentationRequest(path)) {
            return body;
        }

        // ----------------------------------------------------
        // 3. Skip file downloads / streaming responses
        // ----------------------------------------------------
        if (isFileResponse(selectedContentType)) {
            return body;
        }

        // ----------------------------------------------------
        // 4. Avoid double wrapping
        // ----------------------------------------------------
        if (body instanceof ApiResponse) {
            return body;
        }

        // ----------------------------------------------------
        // 5. Skip error responses (handled by @ControllerAdvice)
        // ----------------------------------------------------
        int status = 200;
        if (serverHttpResponse instanceof ServletServerHttpResponse servletResponse) {
            status = servletResponse.getServletResponse().getStatus();
        }

        if (status >= 400) {
            return body;
        }

        // ----------------------------------------------------
        // 6. API version-aware handling (optional hook)
        // ----------------------------------------------------
        String apiVersion = extractApiVersion(path);

        // ----------------------------------------------------
        // 7. Wrap success response
        // ----------------------------------------------------
        return new ApiResponse<>(
                status,
                "Success",
                Instant.now(),
                MDC.get("correlationId"),
                body
        );
    }

    // ========================================================
    // Helper methods
    // ========================================================

    private boolean isExcludedPath(String path) {
        return EXCLUDED_PATHS.stream().anyMatch(path::startsWith);
    }

    private boolean isDocumentationRequest(String path) {
        return path.contains("/swagger")
                || path.contains("/v3/api-docs")
                || path.contains("/swagger-ui");
    }

    private boolean isFileResponse(MediaType mediaType) {
        if (mediaType == null) return false;

        return mediaType.includes(MediaType.APPLICATION_OCTET_STREAM)
                || mediaType.includes(MediaType.APPLICATION_PDF)
                || mediaType.includes(MediaType.IMAGE_JPEG)
                || mediaType.includes(MediaType.IMAGE_PNG);
    }

    private String extractApiVersion(String path) {
        // Example: /api/v1/users → v1
        if (path.contains("/v1/")) return "v1";
        if (path.contains("/v2/")) return "v2";
        return "v1"; // default
    }
}
