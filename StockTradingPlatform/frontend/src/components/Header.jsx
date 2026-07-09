export default function Header({ title, subtitle, onRefresh, onMarketUpdate, loading }) {
  return (
    <header className="topbar">
      <div>
        <h1>{title}</h1>
        <p>{subtitle}</p>
      </div>

      <div className="headerActions">
        <button className="ghostButton" onClick={onRefresh} disabled={loading}>
          Refresh
        </button>
        <button className="primaryButton" onClick={onMarketUpdate} disabled={loading}>
          Update Market
        </button>
      </div>
    </header>
  );
}
