package com.example.stocktrading.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StockResponse(
        Long id,
        String companyName,
        String symbol,
        BigDecimal currentPrice,
        BigDecimal previousClose,
        BigDecimal dayChange,
        BigDecimal dayChangePercent,
        LocalDateTime updatedAt
) {
}
