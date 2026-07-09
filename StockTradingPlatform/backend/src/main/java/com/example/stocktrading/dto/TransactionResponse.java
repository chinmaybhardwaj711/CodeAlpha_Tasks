package com.example.stocktrading.dto;

import com.example.stocktrading.entity.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long id,
        LocalDateTime date,
        TransactionType type,
        Long stockId,
        String stock,
        String symbol,
        int quantity,
        BigDecimal price,
        BigDecimal totalAmount
) {
}
