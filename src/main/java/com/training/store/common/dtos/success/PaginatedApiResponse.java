package com.training.store.common.dtos.success;

import java.time.Instant;

public record PaginatedApiResponse<T>(
        String message,
        int status,
        String path,
        Instant timestamp,
        String correlationId,
        T data,
        PaginationMeta meta
) {}
