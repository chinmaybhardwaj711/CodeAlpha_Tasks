package com.example.stocktrading.repository;

import com.example.stocktrading.entity.Holding;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HoldingRepository extends JpaRepository<Holding, Long> {
    List<Holding> findByPortfolioUserId(Long userId);

    Optional<Holding> findByPortfolioIdAndStockId(Long portfolioId, Long stockId);
}
