package com.example.stocktrading.controller;

import com.example.stocktrading.dto.StockResponse;
import com.example.stocktrading.service.StockService;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping({"/stocks", "/api/stocks"})
    public List<StockResponse> getStocks(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sort
    ) {
        return stockService.getStocks(search, sort);
    }

    @PostMapping({"/market/update", "/api/market/update"})
    public List<StockResponse> updateMarket() {
        return stockService.updateMarket();
    }
}
