import { currency, gainClass, percent } from '../utils/formatters.js';

export default function Portfolio({ portfolio, filter, onFilter }) {
  const holdings = portfolio?.holdings ?? [];

  return (
    <section className="panel">
      <div className="panelHeader">
        <div>
          <h2>Portfolio</h2>
          <p>Track total invested amount, current value, and profit/loss per holding.</p>
        </div>
        <input
          className="searchInput"
          placeholder="Filter holdings"
          value={filter}
          onChange={(event) => onFilter(event.target.value)}
        />
      </div>

      <div className="summaryStrip">
        <div>
          <span>Total Invested</span>
          <strong>{currency(portfolio?.totalInvested)}</strong>
        </div>
        <div>
          <span>Current Portfolio Value</span>
          <strong>{currency(portfolio?.currentPortfolioValue)}</strong>
        </div>
        <div>
          <span>Overall Profit/Loss</span>
          <strong className={gainClass(portfolio?.overallProfitLoss)}>{currency(portfolio?.overallProfitLoss)}</strong>
        </div>
      </div>

      <div className="tableWrap">
        <table>
          <thead>
            <tr>
              <th>Stock</th>
              <th>Quantity</th>
              <th>Buy Price</th>
              <th>Current Price</th>
              <th>Invested</th>
              <th>Current Value</th>
              <th>Profit/Loss</th>
            </tr>
          </thead>
          <tbody>
            {holdings.length === 0 && (
              <tr>
                <td colSpan="7" className="emptyCell">No holdings match this filter.</td>
              </tr>
            )}
            {holdings.map((holding) => (
              <tr key={holding.holdingId}>
                <td>
                  <strong>{holding.symbol}</strong>
                  <span className="mutedText">{holding.companyName}</span>
                </td>
                <td>{holding.quantity}</td>
                <td>{currency(holding.averageBuyPrice)}</td>
                <td>{currency(holding.currentPrice)}</td>
                <td>{currency(holding.investedAmount)}</td>
                <td>{currency(holding.currentValue)}</td>
                <td className={gainClass(holding.profitLoss)}>
                  {currency(holding.profitLoss)} ({percent(holding.profitLossPercent)})
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </section>
  );
}
