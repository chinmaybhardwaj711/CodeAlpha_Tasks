# API Documentation

Base URL:

```text
http://localhost:8080
```

The backend supports both unprefixed endpoints and `/api` endpoints. For example, both `/stocks` and `/api/stocks` work.

## GET /stocks

Returns all stocks.

Query parameters:

- `search`: Optional company or symbol search
- `sort`: Optional sort value: `symbol`, `company`, `priceAsc`, `priceDesc`

Example:

```text
GET /stocks?search=apple&sort=priceDesc
```

## POST /buy

Buys shares for a user.

Request:

```json
{
  "userId": 1,
  "stockId": 1,
  "quantity": 5
}
```

Validation:

- Quantity must be at least `1`
- User must have enough cash balance

## POST /sell

Sells shares for a user.

Request:

```json
{
  "userId": 1,
  "stockId": 1,
  "quantity": 2
}
```

Validation:

- Quantity must be at least `1`
- User cannot sell more shares than owned

## GET /portfolio

Returns user cash balance, holdings, portfolio value, total invested amount, and profit/loss.

Query parameters:

- `userId`: Optional, defaults to `1`
- `filter`: Optional holding filter by company or symbol

Example:

```text
GET /portfolio?userId=1&filter=tesla
```

## GET /transactions

Returns transaction history ordered by newest first.

Query parameters:

- `userId`: Optional, defaults to `1`

Example:

```text
GET /transactions?userId=1
```

## POST /market/update

Applies a random simulated price movement between `-5%` and `+5%` to every stock.

Example:

```text
POST /market/update
```

## Error Response

```json
{
  "timestamp": "2026-07-09T08:00:00",
  "status": 400,
  "message": "Insufficient balance to buy 1000 shares of AAPL",
  "details": []
}
```
