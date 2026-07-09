import { useState } from 'react';
import { currency } from '../utils/formatters.js';

export default function TradeModal({ stock, action, onClose, onSubmit, loading }) {
  const [quantity, setQuantity] = useState(1);

  if (!stock) {
    return null;
  }

  const total = Number(stock.currentPrice) * Number(quantity || 0);

  return (
    <div className="modalBackdrop">
      <div className="modal">
        <div className="modalHeader">
          <div>
            <span>{action}</span>
            <h2>{stock.symbol}</h2>
          </div>
          <button className="iconButton" onClick={onClose}>x</button>
        </div>

        <div className="tradeDetails">
          <p>{stock.companyName}</p>
          <strong>{currency(stock.currentPrice)}</strong>
        </div>

        <label className="field">
          Quantity
          <input
            type="number"
            min="1"
            value={quantity}
            onChange={(event) => setQuantity(event.target.value)}
          />
        </label>

        <div className="modalTotal">
          <span>Estimated Total</span>
          <strong>{currency(total)}</strong>
        </div>

        <button
          className={action === 'BUY' ? 'primaryButton fullWidth' : 'dangerButton fullWidth'}
          disabled={loading || Number(quantity) < 1}
          onClick={() => onSubmit({ stockId: stock.id, quantity: Number(quantity) })}
        >
          Confirm {action}
        </button>
      </div>
    </div>
  );
}
