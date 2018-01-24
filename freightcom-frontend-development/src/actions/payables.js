import { CALL_API } from 'redux-api-middleware';

import { getAll, getOne, update, deleteEntity } from './api';
import { PayableStatementSchema, CommissionStatementSchema } from '../schemas';
import { addNotification } from 'reapop';

export const loadPayableStatements = (
  pageToLoad = 1,
  itemsPerPage = 10,
  filters = {},
  sortOrder = { sortBy: 'id', order: 'DESC' },
) => dispatch => {
  dispatch(
    getAll(
      'payableStatement',
      PayableStatementSchema,
      pageToLoad,
      itemsPerPage,
      filters,
      sortOrder,
      'payablestatement',
      'payable',
    ),
  );
};

export const loadCommissionStatements = (
  pageToLoad = 1,
  itemsPerPage = 10,
  filters = {},
  sortOrder = { sortBy: 'id', order: 'DESC' },
) => dispatch => {
  dispatch(
    getAll(
      'commissionStatement',
      CommissionStatementSchema,
      pageToLoad,
      itemsPerPage,
      filters,
      sortOrder,
      'commissionstatement',
      'commissionPayable',
    ),
  );
};

export const loadPayableStatementById = payableId => dispatch => {
  dispatch(
    getOne(
      'payableStatement',
      payableId,
      'payable',
      `payablestatement/${payableId}`,
    ),
  );
};

export const loadCommissionStatementById = commissionId => dispatch => {
  dispatch(
    getOne(
      'commissionStatement',
      commissionId,
      'commissionPayable',
      `commissionstatement/${commissionId}`,
    ),
  );
};

export const reportPayables = callback => (dispatch, getState) => {
  dispatch(update('payables', null, {}, 'payables/generate')).then(callback);
};

export const markPayableStatementPaid = (payableId, callback) => dispatch => {
  // call the endpoint
  dispatch(
    update(
      'payableStatement',
      payableId,
      {},
      `payablestatement/${payableId}/paid`,
    ),
  ).then(apiPayload => {
    if (!apiPayload.error) {
      callback(apiPayload.payload);
      dispatch(
        addNotification({
          message: 'Payable Statement marked paid',
          status: 'success',
        }),
      );
    } else {
      dispatch(
        addNotification({
          message: apiPayload.payload.message,
          status: 'error',
        }),
      );
    }
  });
};

export const markCommissionStatementPaid = (
  commissionId,
  callback,
) => dispatch => {
  // call the endpoint
  dispatch(
    update(
      'commissionStatement',
      commissionId,
      {},
      `commissionstatement/${commissionId}/paid`,
    ),
  ).then(apiPayload => {
    if (!apiPayload.error) {
      callback(apiPayload.payload);
      dispatch(
        addNotification({
          message: 'Commission Statement marked paid',
          status: 'success',
        }),
      );
    } else {
      dispatch(
        addNotification({
          title: apiPayload.payload.statusText,
          message: apiPayload.payload.message,
          status: 'error',
        }),
      );
    }
  });
};

export const deletePayableStatement = (payableId, callback) => dispatch => {
  // call the endpoint
  dispatch(deleteEntity('payableStatement', payableId)).then(apiPayload => {
    if (!apiPayload.error) {
      dispatch(
        addNotification({
          message: 'Payable statement deleted',
          status: 'success',
        }),
      );
      callback(apiPayload.payload);
    } else {
      dispatch(
        addNotification({
          message: apiPayload.payload.message,
          status: 'error',
        }),
      );
    }
  });
};

export const printCheques = (chequeType, data) => dispatch => {
  return dispatch({
    [CALL_API]: {
      credentials: 'include',
      endpoint: `/api/payables/print-cheques`,
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
      types: [
        { type: 'PRINT_CHEQUES_REQUEST', meta: { chequeType } },
        {
          type: 'PRINT_CHEQUES_SUCCESS',
          meta: { chequeType },
          payload: (action, state, response) => {
            return response.json(json => ({ response: json }));
          },
        },
        { type: 'PRINT_CHEQUES_FAILURE', meta: { chequeType } },
      ],
    },
  });
};

export const deleteCommissionStatement = (
  commissionId,
  callback,
) => dispatch => {
  // call the endpoint
  dispatch(
    deleteEntity('commissionStatement', commissionId),
  ).then(apiPayload => {
    if (!apiPayload.error) {
      dispatch(
        addNotification({
          message: 'Commission statement deleted',
          status: 'success',
        }),
      );
      callback(apiPayload.payload);
    } else {
      dispatch(
        addNotification({
          message: apiPayload.payload.message,
          status: 'error',
        }),
      );
    }
  });
};
