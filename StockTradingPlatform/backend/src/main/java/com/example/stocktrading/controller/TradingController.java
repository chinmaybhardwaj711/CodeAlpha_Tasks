package com.example.stocktrading.controller;

import com.example.stocktrading.dto.PortfolioResponse;
import com.example.stocktrading.dto.TradeRequest;
import com.example.stocktrading.dto.TransactionResponse;
import com.example.stocktrading.service.PortfolioService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class TradingController {
    private final PortfolioService portfolioService;

    public TradingController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @PostMapping({"/buy", "/api/buy"})
    public PortfolioResponse buy(@Valid @RequestBody TradeRequest request) {
        return portfolioService.buy(request);
    }

    @PostMapping({"/sell", "/api/sell"})
    public PortfolioResponse sell(@Valid @RequestBody TradeRequest request) {
        return portfolioService.sell(request);
    }

    @GetMapping({"/portfolio", "/api/portfolio"})
    public PortfolioResponse getPortfolio(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String filter
    ) {
        return portfolioService.getPortfolio(userId, filter);
    }

    @GetMapping({"/transactions", "/api/transactions"})
    public List<TransactionResponse> getTransactions(@RequestParam(required = false) Long userId) {
        return portfolioService.getTransactions(userId);
    }
}
