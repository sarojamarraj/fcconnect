import { getAll, getOne, save } from './api';
import { InvoiceSchema } from '../schemas';
import { CALL_API } from 'redux-api-middleware';
import { removeInvoiceTab } from './tabs';
export const loadInvoices = (
  pageToLoad = 1,
  itemsPerPage = 10,
  filters = {},
  sortOrder = { sortBy: 'createdAt', order: 'DESC' },
) => {
  return getAll(
    'invoice',
    InvoiceSchema,
    pageToLoad,
    itemsPerPage,
    filters,
    sortOrder,
  );
};

export const INVOICE_CALCULATIONS_REQUEST = 'INVOICE_CALCULATIONS_REQUEST';
export const INVOICE_CALCULATIONS_FAILURE = 'INVOICE_CALCULATIONS_FAILURE';
export const INVOICE_CALCULATIONS_SUCCESS = 'INVOICE_CALCULATIONS_SUCCESS';

export const loadInvoiceCalculation = (invoices, meta = {}) => ({
  [CALL_API]: {
    credentials: 'include',
    endpoint:
      '/api/invoice-charges/' + invoices.map(invoice => invoice.id).join(','),
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    types: [
      INVOICE_CALCULATIONS_REQUEST,
      {
        type: INVOICE_CALCULATIONS_SUCCESS,
        meta: meta,
        payload: (action, state, response) => {
          return response.json();
        },
      },
      INVOICE_CALCULATIONS_FAILURE,
    ],
  },
});

export const loadInvoiceById = (invoiceId, callbackAction = false) => (
  dispatch,
  getState,
) => {
  dispatch(
    getOne('invoice', invoiceId, false, false, callbackAction, '/invoices'),
  ).then(
    result => {
      if (result.type === 'API_GET_ONE_FAILURE') {
        dispatch(removeInvoiceTab(invoiceId));
      }
    },
    error => {
      console.log(error, 'error');
    },
  );
};

export const viewInvoice = (invoiceId, callbackAction = false) => (
  dispatch,
  getState,
) => {
  dispatch(
    getOne(
      'invoice',
      invoiceId,
      false,
      `invoice/view/${invoiceId}`,
      callbackAction,
      '/invoices',
      { method: 'POST' },
    ),
  ).then(
    result => {
      if (result.type === 'API_GET_ONE_FAILURE') {
        dispatch(removeInvoiceTab(invoiceId));
      }
    },
    error => {
      console.log(error, 'error');
    },
  );
};

export const generateInvoice = (orders, callbackAction = false) => (
  dispatch,
  getState,
) => {
  dispatch(
    save('invoice', orders, 'generate-invoice', 'save', 'POST', callbackAction),
  );
};

export const deleteInvoices = (invoices, callbackAction = false) => ({
  [CALL_API]: {
    credentials: 'include',
    endpoint: '/api/invoice',
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(invoices),
    types: [
      'API_DELETE_REQUEST',
      {
        type: 'API_DELETE_SUCCESS',
        payload: (action, state, response) => {
          if (typeof callbackAction === 'function') {
            callbackAction();
          }
          return response.json().then(json => ({
            response: json,
          }));
        },
      },
      'API_DELETE_FAILURE',
    ],
  },
});

export const enterPayment = (invoice, callbackAction = false) => ({
  [CALL_API]: {
    credentials: 'include',
    endpoint: '/api/invoice/pay',
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(invoice),
    types: [
      'API_POST_REQUEST',
      {
        type: 'API_POST_SUCCESS',
        payload: (action, state, response) => {
          if (typeof callbackAction === 'function') {
            callbackAction();
          }
          return response.json().then(json => ({
            response: json,
          }));
        },
      },
      'API_POST_FAILURE',
    ],
  },
});

export const payNow = (invoice, callbackAction = false) => ({
  [CALL_API]: {
    credentials: 'include',
    endpoint: '/api/invoice/pay-now',
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(invoice),
    types: [
      'API_POST_REQUEST',
      {
        type: 'API_POST_SUCCESS',
        payload: (action, state, response) => {
          if (typeof callbackAction === 'function') {
            callbackAction();
          }
          return response.json().then(json => ({
            response: json,
          }));
        },
      },
      'API_POST_FAILURE',
    ],
  },
});

export const enterPayments = (payments, callbackAction = false) => (
  dispatch,
  getState,
) => {
  for (var index = 0; index < payments.length; index++) {
    if (index + 1 !== payments.length) {
      dispatch(enterPayment(payments[index], false));
    } else {
      dispatch(enterPayment(payments[index], callbackAction));
    }
  }
};

export const applyCredit = (invoice, callbackAction = false) => ({
  [CALL_API]: {
    credentials: 'include',
    endpoint: '/api/apply-credit',
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(invoice),
    types: [
      'API_POST_REQUEST',
      {
        type: 'API_POST_SUCCESS',
        payload: (action, state, response) => {
          return response.json().then(json => {
            if (typeof callbackAction === 'function') {
              callbackAction(json);
            }

            return {
              response: json,
            };
          });
        },
      },
      'API_POST_FAILURE',
    ],
  },
});

export const loadCreditAppliedView = invoice => dispatch =>
  new Promise((resolve, fail) =>
    dispatch({
      [CALL_API]: {
        credentials: 'include',
        endpoint: `/api/view-apply-credit/${invoice.id}`,
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        types: [
          'INVOICE_CREDIT_REQUEST',
          {
            type: 'INVOICE_CREDIT_SUCCESS',
            payload: (action, state, response) => {
              return response.json().then(json => {
                resolve(json);

                return json;
              });
            },
          },
          'INVOICE_CREDIT_FAILURE',
        ],
      },
    }),
  );
