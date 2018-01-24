import { getAll, getOne, update } from './api';
import { AgentSchema, SubAgentSchema } from '../schemas';
import { normalize, arrayOf } from 'normalizr';
import { apiCallbackHandler } from '../utils';
import { CALL_API } from 'redux-api-middleware';
// import { getAll, getOne, update, save, search } from './api';

export const loadAgents = (
  pageToLoad = 1,
  itemsPerPage = 10,
  filters = {},
  sortOrder = { sortBy: 'id', order: 'DESC' },
) => {
  return getAll(
    'agent',
    AgentSchema,
    pageToLoad,
    itemsPerPage,
    filters,
    sortOrder && sortOrder.sortBy !== 'name'
      ? [sortOrder, { sortBy: 'name' }]
      : sortOrder,
  );
};

export const loadSubAgents = parentId => {
  return getAll(
    'subAgent',
    SubAgentSchema,
    1,
    9999,
    { parentid: parentId },
    {
      sortBy: 'id',
      order: 'DESC',
    },
    'agent',
    'agent',
  );
};

export const transferAgentCustomerToOtherAgent = (
  agentRoleIdFrom,
  agentRoleIdTo,
  loadData,
) => (dispatch, getState) => {
  if (agentRoleIdFrom ** agentRoleIdTo) {
    fetch(`/api/user/role/${agentRoleIdFrom}?reassignment=${agentRoleIdTo}`, {
      credentials: 'include',
      method: 'DELETE',
    }).then(response => {
      if (response.ok) {
        loadData();
      }
    });
  }
};

/**
 * Not being used...
 */
export const agentsAutosuggest = keyword => dispatch => {
  dispatch({
    type: 'API_SEARCH_REQUEST',
    meta: { entity: 'agent' },
    payload: { requestId: keyword },
  });
  fetch(`/api/user?role=agent&name=${keyword}`, {
    credentials: 'include',
    method: 'GET',
  })
    .then(response => {
      const contentType = response.headers.get('Content-Type');
      if (contentType && ~contentType.indexOf('json')) {
        return response.json();
      } else {
        throw Error('Not JSON format');
      }
    })
    .then(json => {
      dispatch({
        type: 'API_SEARCH_SUCCESS',
        payload: {
          ...normalize(json._embedded['user'], arrayOf(AgentSchema)),
          keyword,
          requestId: keyword,
          totalItems: json.page.totalElements,
        },
        meta: {
          entity: 'agent',
        },
      });
    })
    .catch(error => {
      console.log('ERROR: ' + error);
    });
};

export const reportCommissions = (
  agentId,
  onSuccess = false,
  onError = false,
) => (dispatch, getState) => {
  dispatch({
    [CALL_API]: {
      credentials: 'include',
      endpoint: `/api/agent/${agentId}/report-commission`,
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      types: [
        { type: 'API_POST_REQUEST' },
        {
          type: 'API_POST_SUCCESS',
          payload: (action, state, response) => {
            return response.json().then(json => ({ results: json }));
          },
        },
        {
          type: 'API_POST_FAILURE',
          payload: (action, state, response) => {
            return response.json().then(json => ({ ...json }));
          },
        },
      ],
    },
  }).then(apiCallbackHandler(onSuccess, onError));
};

export const loadAgentById = (
  agentId = null,
  forceLoad = true,
  onSuccess = false,
  onError = false,
) => (dispatch, getState) => {
  if (agentId) {
    const agentData = getState().entities.customer[agentId] || false;

    if (!agentData || forceLoad) {
      dispatch(getOne('agent', agentId)).then(
        apiCallbackHandler(onSuccess, onError),
      );
    }
  }
};

export const updateAgent = (
  agentId,
  agentData,
  onSuccess = false,
  onError = false,
) => (dispatch, getState) => {
  dispatch(update('agent', agentId, agentData)).then(
    apiCallbackHandler(onSuccess, onError),
  );
};
