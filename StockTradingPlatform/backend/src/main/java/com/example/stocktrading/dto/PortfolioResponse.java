package com.example.stocktrading.dto;

import java.math.BigDecimal;
import java.util.List;

public record PortfolioResponse(
        Long userId,
        String userName,
        BigDecimal cashBalance,
        BigDecimal totalInvested,
        BigDecimal currentPortfolioValue,
        BigDecimal totalAccountValue,
        BigDecimal overallProfitLoss,
        BigDecimal todayProfitLoss,
        int numberOfHoldings,
        List<HoldingResponse> holdings
) {
}
