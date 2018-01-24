import { initialize } from 'redux-form';
import { getAll, getOne, update, save, search } from './api';
import { CustomerSchema } from '../schemas';
import { apiCallbackHandler, submitFilter } from '../utils';
import { CALL_API } from 'redux-api-middleware';

export const loadCustomers = (
  pageToLoad = 1,
  itemsPerPage = 10,
  filters = {},
  sortOrder = { sortBy: 'createdAt', order: 'DESC' },
) => {
  const { _agent, ...sanitizedFilters } = filters;

  return getAll(
    'customer',
    CustomerSchema,
    pageToLoad,
    itemsPerPage,
    sanitizedFilters,
    sortOrder && sortOrder.sortBy !== 'name'
      ? [sortOrder, { sortBy: 'name' }]
      : sortOrder,
  );
};

export const loadCustomerById = (
  customerId = null,
  forceLoad = true,
  callback,
  meta,
) => (dispatch, getState) => {
  if (customerId) {
    const customerData = getState().entities.customer[customerId] || false;

    if (!customerData || forceLoad) {
      dispatch(
        getOne('customer', customerId, null, null, callback, null, meta),
      ).then(callback);
    }
  }
};

export const initializeCustomerForm = formName => (dispatch, getState) => {
  const { role: { customerId = false } = {} } = getState().loggedIn;

  if (customerId) {
    const url = '/api/customer/' + customerId;
    fetch(url, { credentials: 'include', method: 'GET' })
      .then(response => {
        return response.json();
      })
      .then(json => {
        dispatch(initialize(formName, json));
      })
      .catch(error => {
        console.error(error);
      });
  }
};

export const updateCustomer = (
  values,
  customerId = false,
  onSuccess = false,
  onError = false,
) => (dispatch, getState) => {
  if (!customerId) {
    customerId = getState().loggedIn.role.customerId;
  }
  dispatch(update('customer', customerId, submitFilter(values))).then(
    apiCallbackHandler(onSuccess, onError),
  );
};

export const saveCustomer = (
  form,
  values,
  onSuccess = false,
  onError = false,
) => (dispatch, getState) => {
  dispatch(save('customer', values, 'customer', 'save', 'POST')).then(
    apiCallbackHandler(onSuccess, onError),
  );
  // return formApiAdaptor(
  //   dispatch,
  //   save('customer', values, 'customer', 'save', 'POST'),
  //   onSuccess = false,
  //   onError = false,
  // );
};

export const autoSuggestCustomers = (id, input = '') => {
  return search('customer', CustomerSchema, input, { id });
};

export const updateCustomerStatus = customer => {
  if (customer.active) {
    customer.active = false;
  } else {
    customer.active = true;
  }
  return update('customer', customer.id, customer);
};

export const inviteCustomerStaff = (
  customerId,
  email,
  onSuccess = false,
  onError = false,
) => (dispatch, getState) => {
  dispatch({
    [CALL_API]: {
      credentials: 'include',
      endpoint: `/api/customer/${customerId}/invite?email=${email}`,
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      types: [
        { type: 'API_POST_REQUEST' },
        {
          type: 'API_POST_SUCCESS',
          payload: (action, state, response) => {
            return { response: response };
          },
        },
        { type: 'API_POST_FAILURE' },
      ],
    },
  }).then(apiCallbackHandler(onSuccess, onError));
};
