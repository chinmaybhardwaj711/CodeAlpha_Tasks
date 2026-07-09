package com.example.stocktrading.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TradeRequest(
        Long userId,
        @NotNull(message = "stockId is required") Long stockId,
        @Min(value = 1, message = "quantity must be at least 1") int quantity
) {
    public Long normalizedUserId() {
        return userId == null ? 1L : userId;
    }
}
