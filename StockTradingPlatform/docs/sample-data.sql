INSERT INTO users (id, name, email) VALUES
(1, 'Demo Trader', 'demo.trader@example.com');

INSERT INTO portfolios (id, user_id, cash_balance) VALUES
(1, 1, 100000.00);

INSERT INTO stocks (id, company_name, symbol, current_price, previous_close, updated_at) VALUES
(1, 'Apple Inc.', 'AAPL', 214.35, 211.90, CURRENT_TIMESTAMP),
(2, 'Microsoft Corporation', 'MSFT', 486.20, 481.75, CURRENT_TIMESTAMP),
(3, 'Tesla Inc.', 'TSLA', 278.40, 285.10, CURRENT_TIMESTAMP),
(4, 'Amazon.com Inc.', 'AMZN', 196.80, 193.55, CURRENT_TIMESTAMP),
(5, 'Alphabet Inc.', 'GOOGL', 181.25, 179.90, CURRENT_TIMESTAMP),
(6, 'NVIDIA Corporation', 'NVDA', 126.70, 123.10, CURRENT_TIMESTAMP),
(7, 'Meta Platforms Inc.', 'META', 512.45, 518.30, CURRENT_TIMESTAMP),
(8, 'Netflix Inc.', 'NFLX', 691.10, 684.00, CURRENT_TIMESTAMP),
(9, 'JPMorgan Chase & Co.', 'JPM', 221.85, 219.60, CURRENT_TIMESTAMP),
(10, 'Walmart Inc.', 'WMT', 67.50, 66.90, CURRENT_TIMESTAMP);
