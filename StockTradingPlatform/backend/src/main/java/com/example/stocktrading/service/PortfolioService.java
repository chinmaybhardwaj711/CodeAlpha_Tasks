package com.example.stocktrading.service;

import com.example.stocktrading.dto.HoldingResponse;
import com.example.stocktrading.dto.PortfolioResponse;
import com.example.stocktrading.dto.TradeRequest;
import com.example.stocktrading.dto.TransactionResponse;
import com.example.stocktrading.entity.Holding;
import com.example.stocktrading.entity.Portfolio;
import com.example.stocktrading.entity.Stock;
import com.example.stocktrading.entity.Transaction;
import com.example.stocktrading.entity.TransactionType;
import com.example.stocktrading.entity.User;
import com.example.stocktrading.exception.ResourceNotFoundException;
import com.example.stocktrading.exception.TradingException;
import com.example.stocktrading.repository.HoldingRepository;
import com.example.stocktrading.repository.PortfolioRepository;
import com.example.stocktrading.repository.StockRepository;
import com.example.stocktrading.repository.TransactionRepository;
import com.example.stocktrading.repository.UserRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PortfolioService {
    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final PortfolioRepository portfolioRepository;
    private final HoldingRepository holdingRepository;
    private final TransactionRepository transactionRepository;

    public PortfolioService(
            UserRepository userRepository,
            StockRepository stockRepository,
            PortfolioRepository portfolioRepository,
            HoldingRepository holdingRepository,
            TransactionRepository transactionRepository
    ) {
        this.userRepository = userRepository;
        this.stockRepository = stockRepository;
        this.portfolioRepository = portfolioRepository;
        this.holdingRepository = holdingRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public PortfolioResponse buy(TradeRequest request) {
        User user = findUser(request.normalizedUserId());
        Portfolio portfolio = findPortfolio(user.getId());
        Stock stock = findStock(request.stockId());
        BigDecimal totalCost = stock.getCurrentPrice().multiply(BigDecimal.valueOf(request.quantity()));

        if (portfolio.getCashBalance().compareTo(totalCost) < 0) {
            throw new TradingException("Insufficient balance to buy " + request.quantity() + " shares of " + stock.getSymbol());
        }

        Holding holding = holdingRepository
                .findByPortfolioIdAndStockId(portfolio.getId(), stock.getId())
                .orElseGet(() -> createHolding(portfolio, stock));

        int oldQuantity = holding.getQuantity();
        BigDecimal oldValue = holding.getAverageBuyPrice().multiply(BigDecimal.valueOf(oldQuantity));
        int newQuantity = oldQuantity + request.quantity();
        BigDecimal newAveragePrice = oldValue.add(totalCost)
                .divide(BigDecimal.valueOf(newQuantity), 2, RoundingMode.HALF_UP);

        holding.setQuantity(newQuantity);
        holding.setAverageBuyPrice(newAveragePrice);
        portfolio.setCashBalance(portfolio.getCashBalance().subtract(totalCost).setScale(2, RoundingMode.HALF_UP));

        holdingRepository.save(holding);
        transactionRepository.save(createTransaction(user, stock, TransactionType.BUY, request.quantity(), stock.getCurrentPrice(), totalCost));

        return getPortfolio(user.getId(), null);
    }

    @Transactional
    public PortfolioResponse sell(TradeRequest request) {
        User user = findUser(request.normalizedUserId());
        Portfolio portfolio = findPortfolio(user.getId());
        Stock stock = findStock(request.stockId());
        Holding holding = holdingRepository
                .findByPortfolioIdAndStockId(portfolio.getId(), stock.getId())
                .orElseThrow(() -> new TradingException("No shares owned for " + stock.getSymbol()));

        if (holding.getQuantity() < request.quantity()) {
            throw new TradingException("Cannot sell more shares than owned for " + stock.getSymbol());
        }

        BigDecimal saleValue = stock.getCurrentPrice().multiply(BigDecimal.valueOf(request.quantity())).setScale(2, RoundingMode.HALF_UP);
        holding.setQuantity(holding.getQuantity() - request.quantity());
        portfolio.setCashBalance(portfolio.getCashBalance().add(saleValue).setScale(2, RoundingMode.HALF_UP));

        if (holding.getQuantity() == 0) {
            holdingRepository.delete(holding);
        } else {
            holdingRepository.save(holding);
        }

        transactionRepository.save(createTransaction(user, stock, TransactionType.SELL, request.quantity(), stock.getCurrentPrice(), saleValue));
        return getPortfolio(user.getId(), null);
    }

    @Transactional(readOnly = true)
    public PortfolioResponse getPortfolio(Long userId, String filter) {
        Long normalizedUserId = userId == null ? 1L : userId;
        User user = findUser(normalizedUserId);
        Portfolio portfolio = findPortfolio(normalizedUserId);

        List<HoldingResponse> holdings = holdingRepository.findByPortfolioUserId(normalizedUserId)
                .stream()
                .map(this::toHoldingResponse)
                .filter(holding -> filter == null || filter.isBlank()
                        || holding.companyName().toLowerCase().contains(filter.toLowerCase())
                        || holding.symbol().toLowerCase().contains(filter.toLowerCase()))
                .toList();

        BigDecimal totalInvested = sum(holdings.stream().map(HoldingResponse::investedAmount).toList());
        BigDecimal currentPortfolioValue = sum(holdings.stream().map(HoldingResponse::currentValue).toList());
        BigDecimal overallProfitLoss = currentPortfolioValue.subtract(totalInvested).setScale(2, RoundingMode.HALF_UP);
        BigDecimal todayProfitLoss = sum(holdings.stream()
                .map(holding -> holding.currentPrice()
                        .subtract(findStock(holding.stockId()).getPreviousClose())
                        .multiply(BigDecimal.valueOf(holding.quantity())))
                .toList());

        return new PortfolioResponse(
                user.getId(),
                user.getName(),
                portfolio.getCashBalance(),
                totalInvested,
                currentPortfolioValue,
                portfolio.getCashBalance().add(currentPortfolioValue).setScale(2, RoundingMode.HALF_UP),
                overallProfitLoss,
                todayProfitLoss,
                holdings.size(),
                holdings
        );
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactions(Long userId) {
        Long normalizedUserId = userId == null ? 1L : userId;
        findUser(normalizedUserId);

        return transactionRepository.findByUserIdOrderByCreatedAtDesc(normalizedUserId)
                .stream()
                .map(this::toTransactionResponse)
                .toList();
    }

    private Holding createHolding(Portfolio portfolio, Stock stock) {
        Holding holding = new Holding();
        holding.setPortfolio(portfolio);
        holding.setStock(stock);
        holding.setQuantity(0);
        holding.setAverageBuyPrice(BigDecimal.ZERO);
        return holding;
    }

    private Transaction createTransaction(User user, Stock stock, TransactionType type, int quantity, BigDecimal price, BigDecimal totalAmount) {
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setStock(stock);
        transaction.setType(type);
        transaction.setQuantity(quantity);
        transaction.setPrice(price);
        transaction.setTotalAmount(totalAmount);
        return transaction;
    }

    private HoldingResponse toHoldingResponse(Holding holding) {
        BigDecimal investedAmount = holding.getAverageBuyPrice()
                .multiply(BigDecimal.valueOf(holding.getQuantity()))
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal currentValue = holding.getStock().getCurrentPrice()
                .multiply(BigDecimal.valueOf(holding.getQuantity()))
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal profitLoss = currentValue.subtract(investedAmount).setScale(2, RoundingMode.HALF_UP);
        BigDecimal profitLossPercent = investedAmount.compareTo(BigDecimal.ZERO) == 0
                ? BigDecimal.ZERO
                : profitLoss.divide(investedAmount, 6, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP);

        return new HoldingResponse(
                holding.getId(),
                holding.getStock().getId(),
                holding.getStock().getCompanyName(),
                holding.getStock().getSymbol(),
                holding.getQuantity(),
                holding.getAverageBuyPrice(),
                holding.getStock().getCurrentPrice(),
                investedAmount,
                currentValue,
                profitLoss,
                profitLossPercent
        );
    }

    private TransactionResponse toTransactionResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getCreatedAt(),
                transaction.getType(),
                transaction.getStock().getId(),
                transaction.getStock().getCompanyName(),
                transaction.getStock().getSymbol(),
                transaction.getQuantity(),
                transaction.getPrice(),
                transaction.getTotalAmount()
        );
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }

    private Portfolio findPortfolio(Long userId) {
        return portfolioRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found for user id " + userId));
    }

    private Stock findStock(Long stockId) {
        return stockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found with id " + stockId));
    }

    private BigDecimal sum(List<BigDecimal> values) {
        return values.stream().reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
    }
}
