package com.example.stocktrading.dto;

import java.math.BigDecimal;

public record HoldingResponse(
        Long holdingId,
        Long stockId,
        String companyName,
        String symbol,
        int quantity,
        BigDecimal averageBuyPrice,
        BigDecimal currentPrice,
        BigDecimal investedAmount,
        BigDecimal currentValue,
        BigDecimal profitLoss,
        BigDecimal profitLossPercent
) {
}
