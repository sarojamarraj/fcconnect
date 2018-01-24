import { CurrencySchema } from '../schemas';
import { getAll } from '../actions/api';
import { CALL_API } from 'redux-api-middleware';

export const loadCurrencies = () => dispatch => {
  dispatch(
    getAll(
      'currency',
      CurrencySchema,
      1,
      9999,
      {},
      {},
      'currency-exchange',
      'currencyExchange',
    ),
  );
};

export const saveCurrency = (
  currencyToAdd,
  callback = () => {},
) => dispatch => {
  const body = JSON.stringify({
    from: 'CAD',
    to: currencyToAdd.to || '',
    rate: currencyToAdd.rate || '0.0',
  });
  fetch('/api/currency-exchange/' + (currencyToAdd.id || ''), {
    credentials: 'include',
    method: currencyToAdd.id ? 'PUT' : 'POST',
    body: body,
  })
    .then(response => response.json())
    .then(json => callback(json))
    .catch(error => console.log(error));
};

export const deleteCurrency = (currencyId, callback = () => {}) => dispatch => {
  fetch('/api/currency-exchange/' + currencyId, {
    credentials: 'include',
    method: 'DELETE',
  })
    .then(response => response.json())
    .then(json => callback(json))
    .catch(error => console.log(error));
};

export const loadCurrencyList = () => dispatch => {
  return dispatch({
    [CALL_API]: {
      credentials: 'include',
      endpoint: '/api/currencies',
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      types: [
        'LIST_REQUEST',
        {
          type: 'LIST_SUCCESS',
          meta: { list: 'currencies' },
          payload: (action, state, response) => {
            return response.json(json => ({ response: json }));
          },
        },
        'LIST_FAILURE',
      ],
    },
  });
};
