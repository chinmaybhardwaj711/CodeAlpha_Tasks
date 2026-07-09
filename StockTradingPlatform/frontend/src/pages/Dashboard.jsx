import StatCard from '../components/StatCard.jsx';
import { currency, gainClass } from '../utils/formatters.js';

export default function Dashboard({ portfolio, stocks, onNavigate }) {
  const movers = [...stocks]
    .sort((a, b) => Math.abs(Number(b.dayChangePercent)) - Math.abs(Number(a.dayChangePercent)))
    .slice(0, 5);

  return (
    <div className="pageStack">
      <div className="statsGrid">
        <StatCard label="Total Account Value" value={currency(portfolio?.totalAccountValue)} />
        <StatCard label="Cash Balance" value={currency(portfolio?.cashBalance)} />
        <StatCard label="Today's Profit/Loss" value={currency(portfolio?.todayProfitLoss)} />
        <StatCard label="Holdings" value={portfolio?.numberOfHoldings ?? 0} />
      </div>

      <section className="panel">
        <div className="panelHeader">
          <div>
            <h2>Portfolio Snapshot</h2>
            <p>Invested capital, market value, and overall profit/loss.</p>
          </div>
          <button className="ghostButton" onClick={() => onNavigate('portfolio')}>Open Portfolio</button>
        </div>

        <div className="summaryStrip">
          <div>
            <span>Total Invested</span>
            <strong>{currency(portfolio?.totalInvested)}</strong>
          </div>
          <div>
            <span>Current Value</span>
            <strong>{currency(portfolio?.currentPortfolioValue)}</strong>
          </div>
          <div>
            <span>Overall P/L</span>
            <strong className={gainClass(portfolio?.overallProfitLoss)}>{currency(portfolio?.overallProfitLoss)}</strong>
          </div>
        </div>
      </section>

      <section className="panel">
        <div className="panelHeader">
          <div>
            <h2>Market Movers</h2>
            <p>Largest simulated price changes after the latest market update.</p>
          </div>
          <button className="ghostButton" onClick={() => onNavigate('market')}>Open Market</button>
        </div>

        <div className="tableWrap">
          <table>
            <thead>
              <tr>
                <th>Symbol</th>
                <th>Company</th>
                <th>Price</th>
                <th>Change</th>
              </tr>
            </thead>
            <tbody>
              {movers.map((stock) => (
                <tr key={stock.id}>
                  <td><strong>{stock.symbol}</strong></td>
                  <td>{stock.companyName}</td>
                  <td>{currency(stock.currentPrice)}</td>
                  <td className={gainClass(stock.dayChange)}>{currency(stock.dayChange)} ({stock.dayChangePercent}%)</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </section>
    </div>
  );
}
