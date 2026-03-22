export const maskTelefone = (value: string) => {
  const numbers = value.replace(/\D/g, '');

  const telefone = numbers.startsWith('55') ? numbers.slice(2) : numbers;

  if (telefone.length <= 10) {
    return telefone.replace(/^(\d{2})(\d)/, '($1) $2').replace(/(\d{4})(\d)/, '$1-$2');
  }

  return telefone.replace(/^(\d{2})(\d)/, '($1) $2').replace(/(\d{5})(\d)/, '$1-$2');
};

export const maskData = (value: string) => {
  const numbers = value.replace(/\D/g, '');

  const limited = numbers.slice(0, 8);

  const year = limited.slice(0, 4);
  const month = limited.slice(4, 6);
  const day = limited.slice(6, 8);

  let formatted = day;
  if (month) {
    formatted += `/${month}`;
  }
  if (year) {
    formatted += `/${year}`;
  }

  return formatted;
};

export const maskISBN = (value: string) => {
  const numbers = value.replace(/\D/g, '');

  if (numbers.length === 10) {
    return numbers.replace(/^(\d{1})(\d{3})(\d{5})(\d{1})$/, '$1-$2-$3-$4');
  }

  if (numbers.length === 13) {
    return numbers.replace(/^(\d{3})(\d{3})(\d{5})(\d{1})$/, '$1-$2-$3-$4');
  }

  return value;
};
