import { gainClass } from '../utils/formatters.js';

export default function StatCard({ label, value, trend }) {
  return (
    <section className="statCard">
      <span>{label}</span>
      <strong>{value}</strong>
      {trend !== undefined && <small className={gainClass(trend)}>{trend >= 0 ? '+' : ''}{trend}</small>}
    </section>
  );
}
