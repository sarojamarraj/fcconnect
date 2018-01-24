/******************************************************************
              WebPal Product Suite Framework Libraries
-------------------------------------------------------------------
(c) 2002-present: all copyrights are with Palomino System Innovations Inc.
(Palomino Inc.) of Toronto, Canada

Unauthorized reproduction, licensing or disclosure of this source
code will be prosecuted. WebPal is a registered trademark of
Palomino System Innovations Inc. To report misuse please contact
info@palominosys.com or call +1 416 964 7333.
*******************************************************************/

import {
  API_GET_ONE_REQUEST,
  API_GET_ONE_SUCCESS,
  API_GET_ONE_FAILURE,
} from '../actions/api.js';

import {
  INVOICE_CALCULATIONS_REQUEST,
  INVOICE_CALCULATIONS_SUCCESS,
  INVOICE_CALCULATIONS_FAILURE,
} from '../actions/invoices.js';

import { camelize } from 'humps';

export const loadStatus = (state = { paymentInformation: {} }, action) => {
  if (action.type === API_GET_ONE_REQUEST) {
    return {
      ...state,
      [action.meta.entity]: {
        ...(state[action.meta.entity] || {}),
        [action.meta.id]: 'loading',
      },
    };
  } else if (action.type === API_GET_ONE_SUCCESS) {
    return {
      ...state,
      [action.meta.entity]: {
        ...(state[action.meta.entity] || {}),
        [action.meta.id]: 'success',
      },
    };
  } else if (action.type === API_GET_ONE_FAILURE) {
    return {
      ...state,
      [action.meta.entity]: {
        ...(state[action.meta.entity] || {}),
        [action.meta.id]: 'failed',
      },
    };
  } else if (action.type === INVOICE_CALCULATIONS_REQUEST) {
    return { ...state, paymentInformation: { status: 'requesting' } };
  } else if (action.type === INVOICE_CALCULATIONS_SUCCESS) {
    return {
      ...state,
      paymentInformation: { status: 'success', data: action.payload },
    };
  } else if (action.type === INVOICE_CALCULATIONS_FAILURE) {
    const { payload: { response: { message = null } = {} } = {} } = action;
    return {
      ...state,
      paymentInformation: { status: 'failed', message },
    };
  }

  return state;
};

export const checkLoadStatus = (state, entity, id) => {
  const key = camelize(entity);

  return state.loadStatus && state.loadStatus[key]
    ? state.loadStatus[key][id]
    : null;
};

export const getPaymentInformation = state => {
  const { loadStatus: { paymentInformation = {} } = {} } = state;
  return paymentInformation;
};
