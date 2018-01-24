import { getAll, save } from './api';
import { CreditSchema } from '../schemas';

export const loadCredits = (
  pageToLoad = 1,
  itemsPerPage = 10,
  filters = {},
  sortOrder = { sortBy: 'createdAt', order: 'DESC' },
) => {
  return getAll(
    'credit',
    CreditSchema,
    pageToLoad,
    itemsPerPage,
    filters,
    sortOrder,
  );
};

export const loadCreditsByCustomer = customerId => {
  const pageToLoad = 1;
  const itemsPerPage = 9999;
  const filters = {
    customerId,
  };
  const sortOrder = { sortBy: 'createdAt', order: 'DESC' };

  return getAll(
    'credit',
    CreditSchema,
    pageToLoad,
    itemsPerPage,
    filters,
    sortOrder,
  );
};

export const addCredit = (data, callback) => (dispatch, getState) => {
  const dataToSave = {
    customerId: getState().loggedIn.role.customerId || '',
    createdByUserId: getState().loggedInUser.id,
    amount: '100',
    currency: 'CAD',
    note: 'Manually added...',
    ...data,
  };

  dispatch(save('credit', dataToSave, false, 'save', 'POST', callback));
};

export const refundCredit = (data, callback) => (dispatch, getState) => {
  console.debug('REFUND', data);

  const dataToSave = {
    customerId: getState().loggedIn.role.customerId || '',
    createdByUserId: getState().loggedInUser.id,
    amount: '100',
    currency: 'CAD',
    note: 'Manually added...',
    ...data,
  };

  dispatch(save('refund-credit', dataToSave, false, 'save', 'POST', callback));
};

export const addCreditFromCard = (
  paymentData,
  callback = () => {},
) => dispatch => {
  console.log(paymentData);
  fetch('/api/credit/add-from-card', {
    credentials: 'include',
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(paymentData),
  })
    .then(res => {
      if (res.ok) {
        return res.json();
      } else {
        throw new Error('Something went wrong with the request');
      }
    })
    .then(json => {
      callback(json);
    })
    .catch(err => {
      console.error(err);
    });
};
