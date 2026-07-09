import { useEffect, useMemo, useState } from 'react';
import Header from './components/Header.jsx';
import Sidebar from './components/Sidebar.jsx';
import TradeModal from './components/TradeModal.jsx';
import Dashboard from './pages/Dashboard.jsx';
import Market from './pages/Market.jsx';
import Portfolio from './pages/Portfolio.jsx';
import Transactions from './pages/Transactions.jsx';
import {
  buyStock,
  fetchPortfolio,
  fetchStocks,
  fetchTransactions,
  getApiError,
  sellStock,
  updateMarket
} from './services/api.js';

const pageTitles = {
  dashboard: ['Dashboard', 'A live view of cash, holdings, and simulated market performance.'],
  market: ['Market', 'Browse simulated market data and place trades.'],
  portfolio: ['Portfolio', 'Review holdings, investment value, and profit/loss.'],
  transactions: ['Transaction History', 'Audit every trade saved in the database.']
};

export default function App() {
  const [activePage, setActivePage] = useState('dashboard');
  const [stocks, setStocks] = useState([]);
  const [portfolio, setPortfolio] = useState(null);
  const [transactions, setTransactions] = useState([]);
  const [search, setSearch] = useState('');
  const [sort, setSort] = useState('symbol');
  const [portfolioFilter, setPortfolioFilter] = useState('');
  const [trade, setTrade] = useState(null);
  const [loading, setLoading] = useState(false);
  const [notice, setNotice] = useState('');
  const [error, setError] = useState('');

  const [title, subtitle] = pageTitles[activePage];

  const loadAll = async () => {
    setLoading(true);
    setError('');

    try {
      const [stockData, portfolioData, transactionData] = await Promise.all([
        fetchStocks({ search, sort }),
        fetchPortfolio({ filter: portfolioFilter }),
        fetchTransactions()
      ]);
      setStocks(stockData);
      setPortfolio(portfolioData);
      setTransactions(transactionData);
    } catch (apiError) {
      setError(getApiError(apiError));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadAll();
  }, [search, sort, portfolioFilter]);

  const handleMarketUpdate = async () => {
    setLoading(true);
    setError('');
    setNotice('');

    try {
      await updateMarket();
      await loadAll();
      setNotice('Market prices updated with simulated fluctuations.');
    } catch (apiError) {
      setError(getApiError(apiError));
    } finally {
      setLoading(false);
    }
  };

  const handleTradeSubmit = async ({ stockId, quantity }) => {
    setLoading(true);
    setError('');
    setNotice('');

    try {
      if (trade.action === 'BUY') {
        await buyStock({ userId: 1, stockId, quantity });
      } else {
        await sellStock({ userId: 1, stockId, quantity });
      }

      setTrade(null);
      await loadAll();
      setNotice(`${trade.action} order completed successfully.`);
    } catch (apiError) {
      setError(getApiError(apiError));
    } finally {
      setLoading(false);
    }
  };

  const page = useMemo(() => {
    if (activePage === 'market') {
      return (
        <Market
          stocks={stocks}
          search={search}
          sort={sort}
          onSearch={setSearch}
          onSort={setSort}
          onTrade={(stock, action) => setTrade({ stock, action })}
        />
      );
    }

    if (activePage === 'portfolio') {
      return <Portfolio portfolio={portfolio} filter={portfolioFilter} onFilter={setPortfolioFilter} />;
    }

    if (activePage === 'transactions') {
      return <Transactions transactions={transactions} />;
    }

    return <Dashboard portfolio={portfolio} stocks={stocks} onNavigate={setActivePage} />;
  }, [activePage, stocks, search, sort, portfolio, transactions, portfolioFilter]);

  return (
    <div className="appShell">
      <Sidebar activePage={activePage} onNavigate={setActivePage} />

      <main className="mainArea">
        <Header
          title={title}
          subtitle={subtitle}
          onRefresh={loadAll}
          onMarketUpdate={handleMarketUpdate}
          loading={loading}
        />

        {notice && <div className="notice success">{notice}</div>}
        {error && <div className="notice error">{error}</div>}
        {loading && <div className="loadingBar" />}

        {page}
      </main>

      <TradeModal
        stock={trade?.stock}
        action={trade?.action}
        loading={loading}
        onClose={() => setTrade(null)}
        onSubmit={handleTradeSubmit}
      />
    </div>
  );
}
