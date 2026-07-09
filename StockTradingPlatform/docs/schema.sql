CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE portfolios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE,
    cash_balance DECIMAL(14, 2) NOT NULL,
    CONSTRAINT fk_portfolios_users FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE stocks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    company_name VARCHAR(255) NOT NULL,
    symbol VARCHAR(12) NOT NULL UNIQUE,
    current_price DECIMAL(12, 2) NOT NULL,
    previous_close DECIMAL(12, 2) NOT NULL,
    updated_at DATETIME NOT NULL
);

CREATE TABLE holdings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    portfolio_id BIGINT NOT NULL,
    stock_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    average_buy_price DECIMAL(12, 2) NOT NULL,
    CONSTRAINT fk_holdings_portfolios FOREIGN KEY (portfolio_id) REFERENCES portfolios(id),
    CONSTRAINT fk_holdings_stocks FOREIGN KEY (stock_id) REFERENCES stocks(id),
    CONSTRAINT uk_holding_portfolio_stock UNIQUE (portfolio_id, stock_id)
);

CREATE TABLE transactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    stock_id BIGINT NOT NULL,
    type VARCHAR(20) NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(12, 2) NOT NULL,
    total_amount DECIMAL(14, 2) NOT NULL,
    created_at DATETIME NOT NULL,
    CONSTRAINT fk_transactions_users FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_transactions_stocks FOREIGN KEY (stock_id) REFERENCES stocks(id)
);
