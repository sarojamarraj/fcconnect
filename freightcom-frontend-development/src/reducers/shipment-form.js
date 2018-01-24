import { ADD_TAB } from '../actions/tabs';
import {
  API_GET_ONE_REQUEST,
  API_GET_ONE_SUCCESS,
  API_SAVE_FAILURE,
} from '../actions/api';

import {
  UPDATE_ORDER_FOR_BOOK_REQUEST,
  UPDATE_ORDER_FOR_BOOK_SUCCESS,
  UPDATE_ORDER_FOR_BOOK_FAILURE,
  UPDATE_ORDER_FOR_BOOK_CLEAR,
} from '../actions/orders';

const addTab = (state, { type, payload: { tab: { id, duplicate } } = {} }) => {
  if (duplicate) {
    return { ...state, [id]: { duplicate, lifecycle: 'new' } };
  }

  return state;
};

const requestingDuplicate = (
  state,
  { meta: { id, duplicate, formName } = {}, payload },
) => {
  if (duplicate && formName) {
    return {
      ...state,
      [formName]: { ...(state[formName] || {}), lifecycle: 'fetching' },
    };
  }

  return state;
};

const receivedDuplicate = (
  state,
  { meta: { id, duplicate, formName } = {}, payload },
) => {
  if (duplicate && formName) {
    return {
      ...state,
      [formName]: { ...(state[formName] || {}), lifecycle: 'done' },
    };
  }

  return state;
};

const saveFailure = (
  state,
  { meta: { formName } = {}, payload: { response = {} } },
) => {
  if (formName) {
    return {
      ...state,
      [formName]: {
        ...(state[formName] || {}),
        error: true,
        message: response,
      },
    };
  }

  return state;
};

const clearFormErrors = (state, { formName }) => {
  if (formName) {
    const { error, message, ...rest } = state[formName] || {};
    return {
      ...state,
      [formName]: { ...rest },
    };
  }

  return state;
};

export const shipmentFormState = ({ shipmentForm }, formName) => {
  return shipmentForm[formName] || {};
};

const dispatchAction = (action, methods) => (state, action) => {
  if (methods[action.type]) {
    return methods[action.type](state, action);
  }

  return state;
};

const updateBookRequest = (state, { meta: { formName } = {} }) => {
  if (formName) {
    return {
      ...state,
      [formName]: { ...(state[formName] || {}), updateBook: 'requesting' },
    };
  }

  return state;
};

const updateBookSuccess = (state, { payload, meta: { formName } = {} }) => {
  if (formName) {
    return {
      ...state,
      [formName]: {
        ...(state[formName] || {}),
        updateBook: 'success',
        updateBookData: payload,
      },
    };
  }

  return state;
};

const updateBookFailure = (state, { payload, meta: { formName } = {} }) => {
  if (formName) {
    return {
      ...state,
      [formName]: {
        ...(state[formName] || {}),
        updateBook: 'failed',
        updateBookError: payload,
      },
    };
  }

  return state;
};

const updateBookClear = (state, { payload, meta: { formName } = {} }) => {
  if (formName) {
    const { updateBook, updateBookError, ...rest } = state[formName] || {};
    return {
      ...state,
      [formName]: {
        ...rest,
      },
    };
  }

  return state;
};

export const shipmentForm = (state = {}, action) =>
  dispatchAction(action, {
    [ADD_TAB]: addTab,
    [API_GET_ONE_REQUEST]: requestingDuplicate,
    [API_GET_ONE_SUCCESS]: receivedDuplicate,
    [API_SAVE_FAILURE]: saveFailure,
    CLEAR_FORM_ERRORS: clearFormErrors,
    [UPDATE_ORDER_FOR_BOOK_REQUEST]: updateBookRequest,
    [UPDATE_ORDER_FOR_BOOK_SUCCESS]: updateBookSuccess,
    [UPDATE_ORDER_FOR_BOOK_FAILURE]: updateBookFailure,
    [UPDATE_ORDER_FOR_BOOK_CLEAR]: updateBookClear,
  })(state, action);
