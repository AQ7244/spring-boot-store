package com.training.store.common.dtos.success;

import java.time.Instant;

public record ApiResponse<T>(
        int status,
        String message,
        Instant timestamp,
        String correlationId,
        T data
) {}
