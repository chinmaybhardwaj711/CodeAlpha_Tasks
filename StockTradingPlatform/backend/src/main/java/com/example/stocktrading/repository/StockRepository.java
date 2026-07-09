package com.example.stocktrading.repository;

import com.example.stocktrading.entity.Stock;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findByCompanyNameContainingIgnoreCaseOrSymbolContainingIgnoreCase(String companyName, String symbol);
}
