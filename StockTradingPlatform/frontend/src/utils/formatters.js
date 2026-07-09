export const currency = (value) =>
  Number(value || 0).toLocaleString('en-US', {
    style: 'currency',
    currency: 'USD'
  });

export const number = (value) => Number(value || 0).toLocaleString('en-US');

export const percent = (value) => `${Number(value || 0).toFixed(2)}%`;

export const dateTime = (value) => {
  if (!value) {
    return '-';
  }

  return new Date(value).toLocaleString();
};

export const gainClass = (value) => (Number(value) >= 0 ? 'positive' : 'negative');
