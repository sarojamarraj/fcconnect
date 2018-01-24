import { getAll, search, save, update } from './api';
import { CALL_API } from 'redux-api-middleware';
import { AddressBookSchema } from '../schemas';
import { apiCallbackHandler } from '../utils';

export const loadAddressBook = (
  pageToLoad = 1,
  itemsPerPage = 10,
  filters = {},
  sortOrder = {},
) => {
  return getAll(
    'addressBook',
    AddressBookSchema,
    pageToLoad,
    itemsPerPage,
    filters,
    sortOrder,
    'address_book',
    'addressBooks',
  );
};

export const autoSuggestAddressBook = (id, input = '', customer = false) => {
  return search('address_book', AddressBookSchema, input, { id, customer });
};

export const saveAddressBookEntry = (data, id = '') => {
  if (id) {
    return update('addressBook', id, data, `address_book/${id}`);
  } else {
    return save('addressBook', data, 'address_book');
  }
};

export const deleteAddressBookEntry = (
  addressBookId,
  onSuccess = false,
  onError = false,
) => (dispatch, getState) => {
  dispatch({
    [CALL_API]: {
      credentials: 'include',
      endpoint: `/api/address_book/${addressBookId}`,
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json' },
      types: [
        { type: 'API_DELETE_REQUEST' },
        {
          type: 'API_DELETE_SUCCESS',
          payload: (action, state, response) => {
            return { response: response };
          },
        },
        { type: 'API_DELETE_FAILURE' },
      ],
    },
  }).then(apiCallbackHandler(onSuccess, onError));
};

export const checkAddressBook = customer => (dispatch, getState) => {
  return dispatch({
    [CALL_API]: {
      credentials: 'include',
      endpoint: '/api/hasAddressBook' + (customer ? `/${customer.id}` : ''),
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      types: [
        'API_CHECK_ADDRESS_BOOK_REQUEST',
        {
          type: 'API_CHECK_ADDRESS_BOOK_SUCCESS',
          meta: { customer: customer },
          payload: (action, state, response) => {
            return response.json().then(json => {
              return json;
            });
          },
        },
        'API_CHECK_ADDRESS_BOOK_FAILURE',
      ],
    },
  });
};
