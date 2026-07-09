package com.example.stocktrading.service;

import com.example.stocktrading.dto.StockResponse;
import com.example.stocktrading.entity.Stock;
import com.example.stocktrading.repository.StockRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional(readOnly = true)
    public List<StockResponse> getStocks(String search, String sort) {
        List<Stock> stocks = search == null || search.isBlank()
                ? stockRepository.findAll()
                : stockRepository.findByCompanyNameContainingIgnoreCaseOrSymbolContainingIgnoreCase(search, search);

        Comparator<Stock> comparator = Comparator.comparing(Stock::getSymbol);
        if ("priceAsc".equalsIgnoreCase(sort)) {
            comparator = Comparator.comparing(Stock::getCurrentPrice);
        } else if ("priceDesc".equalsIgnoreCase(sort)) {
            comparator = Comparator.comparing(Stock::getCurrentPrice).reversed();
        } else if ("company".equalsIgnoreCase(sort)) {
            comparator = Comparator.comparing(stock -> stock.getCompanyName().toLowerCase(Locale.ROOT));
        }

        return stocks.stream().sorted(comparator).map(this::toResponse).toList();
    }

    @Transactional
    public List<StockResponse> updateMarket() {
        List<Stock> stocks = stockRepository.findAll();

        for (Stock stock : stocks) {
            BigDecimal oldPrice = stock.getCurrentPrice();
            BigDecimal movementPercent = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(-5.0, 5.0));
            BigDecimal multiplier = BigDecimal.ONE.add(movementPercent.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP));
            BigDecimal newPrice = oldPrice.multiply(multiplier).max(BigDecimal.ONE).setScale(2, RoundingMode.HALF_UP);

            stock.setPreviousClose(oldPrice);
            stock.setCurrentPrice(newPrice);
        }

        return stockRepository.saveAll(stocks).stream().map(this::toResponse).toList();
    }

    public StockResponse toResponse(Stock stock) {
        BigDecimal dayChange = stock.getCurrentPrice().subtract(stock.getPreviousClose()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal dayChangePercent = BigDecimal.ZERO;

        if (stock.getPreviousClose().compareTo(BigDecimal.ZERO) > 0) {
            dayChangePercent = dayChange
                    .divide(stock.getPreviousClose(), 6, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        return new StockResponse(
                stock.getId(),
                stock.getCompanyName(),
                stock.getSymbol(),
                stock.getCurrentPrice(),
                stock.getPreviousClose(),
                dayChange,
                dayChangePercent,
                stock.getUpdatedAt()
        );
    }
}
