import { combineReducers } from 'redux';

import {
  GET_ORDER_RATES_REQUEST,
  GET_ORDER_RATES_SUCCESS,
  GET_ORDER_RATES_FAILURE,
  UPDATE_DISTRIBUTION,
  GET_PALLET_TEMPLATES_SUCCESS,
  SAVE_PALLET_TEMPLATES_SUCCESS,
} from '../actions/orders';

import { API_SAVE_SUCCESS } from '../actions/api.js';

const rates = (state = {}, action) => {
  switch (action.type) {
    // This itercepts form change and mark the rates dirty.
    case '@@redux-form/CHANGE':
      /*
     * NOTE: There needs to be a better way than hard-coding the
     *    field name to skip marking rates dirty.
     *    When carrierId is set, that means user selected a rate
     */
      if (action.meta.field === 'carrierId') {
        return state;
      }

      // Fields that will trigger Rates dirty field
      const fieldsToWatch = [
        'shipFrom.postalCode',
        'shipTo.postalCode',
        'pallets',
        'packages',
        'accessorialServices',
        'packageTypeName',
      ];

      if (
        fieldsToWatch.filter(i =>
          action.meta.field.toLowerCase().includes(i.toLowerCase()),
        ).length > 0
      ) {
        return {
          ...state,
          [action.meta.form]: {
            ...state[action.meta.form],
            dirty: true,
            loaded: false,
          },
        };
      }

      if (action.meta.field === 'shipFrom' || action.meta.field === 'shipTo') {
        const { postalCode = false } = action.payload;

        if (postalCode !== false || action.payload === '') {
          return {
            ...state,
            [action.meta.form]: {
              ...state[action.meta.form],
              dirty: true,
              loaded: false,
            },
          };
        }
      }

      return state;

    case API_SAVE_SUCCESS:
      if (action.meta.key && action.payload.orderId) {
        return {
          ...state,
          [action.meta.key]: {
            ...(state[action.meta.key] || {}),
            orderId: action.payload.orderId,
          },
        };
      } else {
        return state;
      }

    case GET_ORDER_RATES_REQUEST:
      if (action.meta.getMoreRequest) {
        return {
          ...state,
          [action.meta.form]: {
            ...state[action.meta.form],
            fetching: true,
            error: null,
          },
        };
      } else {
        return {
          ...state,
          [action.meta.form]: {
            fetchingMore: false,
            fetching: true,
            error: null,
          },
        };
      }

    case GET_ORDER_RATES_SUCCESS:
      if (action.meta.getMoreRequest) {
        return {
          ...state,
          [action.meta.form]: {
            ...state[action.meta.form],
            ids: [...action.payload.result],
            fetching: false,
            messages: action.payload._messages,
          },
        };
      } else {
        return {
          ...state,
          [action.meta.orderForm]: {
            ids: action.payload.result,
            fetching: false,
            fetchingMore: false,
            dirty: false,
            loaded: true,
            messages: action.payload._messages,
          },
        };
      }

    case GET_ORDER_RATES_FAILURE:
      return {
        ...state,
        [action.meta.form]: {
          fetchingMore: false,
          fetching: false,
          error: action.payload.response.message,
        },
      };

    default:
      return state;
  }
};

const distributionList = (state = {}, action) => {
  switch (action.type) {
    case 'UPLOAD_SUBMIT':
      return { ...state, [action.meta.form]: { status: 'Uploading' } };

    case 'UPLOAD_SUCCESS':
      return {
        ...state,
        [action.meta.form]: { status: 'Done', ...action.payload },
      };

    case 'UPLOAD_FAILURE':
      return {
        ...state,
        [action.meta.form]: { status: 'Error', ...action.payload },
      };

    case UPDATE_DISTRIBUTION:
      return {
        ...state,
        [action.meta.form]: { status: 'Done', ...action.payload },
      };

    default:
      return state;
  }
};

const stats = (
  state = { env: 0, pak: 0, package: 0, pallet: 0, workorder: 0 },
  action,
) => {
  switch (action.type) {
    case 'LOAD_ORDER_STATS':
      return { ...action.payload };
    default:
      return state;
  }
};

const users = (state = {}, action) => {
  switch (action.type) {
    case 'ORDER_USERS_SUCCESS':
      return { ...state, [action.meta.orderId]: action.payload.result };
    default:
      return state;
  }
};

const palletTemplates = (state = {}, action) => {
  switch (action.type) {
    case GET_PALLET_TEMPLATES_SUCCESS:
    case SAVE_PALLET_TEMPLATES_SUCCESS:
      return { ...state, [action.meta.customerId]: action.payload.result };
    default:
      return state;
  }
};

export default combineReducers({
  rates,
  distributionList,
  stats,
  users,
  palletTemplates,
});
