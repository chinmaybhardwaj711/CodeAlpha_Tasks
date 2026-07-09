# Stock Trading Platform

A full-stack stock trading simulation built with Spring Boot, Java 21, Maven, React, Vite, Axios, and JPA.

## Features

- View simulated stock market data
- Search stocks and sort by price or company name
- Buy and sell shares
- Prevent buying when cash balance is insufficient
- Prevent selling more shares than owned
- View portfolio holdings
- Track total invested amount, current portfolio value, and profit/loss
- View transaction history
- Update market prices using random movement between `-5%` and `+5%`
- Persist data using JPA with H2 for development or MySQL for production-style setup

## Project Structure

```text
StockTradingPlatform/
  backend/       Spring Boot REST API
  frontend/      React + Vite dashboard
  docs/          API docs, SQL schema, sample data
```

## Backend Setup

Requirements:

- Java 21

Fastest run method on Windows:

```powershell
.\start-all.bat
```

Or start backend only:

```powershell
.\run-backend.bat
```

Manual Maven method:

Run with H2:

```powershell
cd backend
mvn spring-boot:run
```

Backend URL:

```text
http://localhost:8080
```

H2 console:

```text
http://localhost:8080/h2-console
```

H2 JDBC URL:

```text
jdbc:h2:mem:tradingdb
```

## Frontend Setup

Requirements:

- Node.js
- npm

Run:

```powershell
.\run-frontend.bat
```

Manual method:

```powershell
cd frontend
npm install
npm run dev
```

Frontend URL:

```text
http://localhost:5173
```

## MySQL Setup

Create a MySQL database:

```sql
CREATE DATABASE stock_trading_platform;
```

Update `backend/src/main/resources/application-mysql.properties` with your MySQL username and password, then run:

```powershell
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

Optional SQL files are available in `docs/schema.sql` and `docs/sample-data.sql`.

## Default User

The development database starts with:

```text
User ID: 1
Name: Demo Trader
Cash Balance: $100,000
```

## API Documentation

See [docs/API.md](docs/API.md).
