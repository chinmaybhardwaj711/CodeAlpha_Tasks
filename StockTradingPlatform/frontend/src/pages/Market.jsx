import { currency, gainClass, percent } from '../utils/formatters.js';

export default function Market({ stocks, search, sort, onSearch, onSort, onTrade }) {
  return (
    <section className="panel">
      <div className="panelHeader">
        <div>
          <h2>Market</h2>
          <p>Search stocks, sort by price, and place simulated buy or sell orders.</p>
        </div>
        <div className="toolbar">
          <input
            className="searchInput"
            placeholder="Search company or symbol"
            value={search}
            onChange={(event) => onSearch(event.target.value)}
          />
          <select value={sort} onChange={(event) => onSort(event.target.value)}>
            <option value="symbol">Symbol</option>
            <option value="company">Company</option>
            <option value="priceAsc">Price: Low to High</option>
            <option value="priceDesc">Price: High to Low</option>
          </select>
        </div>
      </div>

      <div className="tableWrap">
        <table>
          <thead>
            <tr>
              <th>Company Name</th>
              <th>Symbol</th>
              <th>Current Price</th>
              <th>Day Change</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {stocks.map((stock) => (
              <tr key={stock.id}>
                <td>{stock.companyName}</td>
                <td><strong>{stock.symbol}</strong></td>
                <td>{currency(stock.currentPrice)}</td>
                <td className={gainClass(stock.dayChange)}>
                  {currency(stock.dayChange)} ({percent(stock.dayChangePercent)})
                </td>
                <td>
                  <div className="rowActions">
                    <button className="buyButton" onClick={() => onTrade(stock, 'BUY')}>Buy</button>
                    <button className="sellButton" onClick={() => onTrade(stock, 'SELL')}>Sell</button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </section>
  );
}
