const navItems = [
  { id: 'dashboard', label: 'Dashboard' },
  { id: 'market', label: 'Market' },
  { id: 'portfolio', label: 'Portfolio' },
  { id: 'transactions', label: 'Transactions' }
];

export default function Sidebar({ activePage, onNavigate }) {
  return (
    <aside className="sidebar">
      <div className="brand">
        <div className="brandMark">ST</div>
        <div>
          <strong>StockTrade</strong>
          <span>Simulation</span>
        </div>
      </div>

      <nav>
        {navItems.map((item) => (
          <button
            key={item.id}
            className={activePage === item.id ? 'active' : ''}
            onClick={() => onNavigate(item.id)}
          >
            {item.label}
          </button>
        ))}
      </nav>
    </aside>
  );
}
