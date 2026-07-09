import { currency, dateTime } from '../utils/formatters.js';

export default function Transactions({ transactions }) {
  return (
    <section className="panel">
      <div className="panelHeader">
        <div>
          <h2>Transaction History</h2>
          <p>Every buy and sell order is saved by the backend database.</p>
        </div>
      </div>

      <div className="tableWrap">
        <table>
          <thead>
            <tr>
              <th>Date</th>
              <th>Type</th>
              <th>Stock</th>
              <th>Quantity</th>
              <th>Price</th>
              <th>Total</th>
            </tr>
          </thead>
          <tbody>
            {transactions.length === 0 && (
              <tr>
                <td colSpan="6" className="emptyCell">No transactions yet.</td>
              </tr>
            )}
            {transactions.map((transaction) => (
              <tr key={transaction.id}>
                <td>{dateTime(transaction.date)}</td>
                <td>
                  <span className={transaction.type === 'BUY' ? 'typeBuy' : 'typeSell'}>{transaction.type}</span>
                </td>
                <td>
                  <strong>{transaction.symbol}</strong>
                  <span className="mutedText">{transaction.stock}</span>
                </td>
                <td>{transaction.quantity}</td>
                <td>{currency(transaction.price)}</td>
                <td>{currency(transaction.totalAmount)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </section>
  );
}
