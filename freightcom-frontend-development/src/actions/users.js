import { getAll, save, update, getOne } from './api';
import { CALL_API } from 'redux-api-middleware';
import { UserSchema } from '../schemas';
import { camelizeKeys } from 'humps';
import { normalize, arrayOf } from 'normalizr';
import { stopSubmit } from 'redux-form';
import merge from 'lodash/merge';
import { apiCallbackHandler } from '../utils';
import { selectOneFromEntities } from '../reducers';

export const loadUsers = (
  pageToLoad = 1,
  itemsPerPage = 10,
  filters = {},
  sortOrder = { sortBy: 'id', order: 'DESC' },
) => (dispatch, getState) => {
  dispatch(
    getAll(
      'user',
      UserSchema,
      pageToLoad,
      itemsPerPage,
      filters,
      sortOrder && sortOrder.sortBy !== 'login'
        ? [sortOrder, { sortBy: 'login' }]
        : sortOrder,
      'user',
    ),
  );
};

export const loadCustomerUsers = (
  customerId = false,
  pageToLoad = 1,
  itemsPerPage = 10,
  filters = {},
  sortOrder = { sortBy: 'id', order: 'DESC' },
) => (dispatch, getState) => {
  dispatch({
    [CALL_API]: {
      credentials: 'include',
      endpoint: customerId
        ? '/api/customer-staff?customer_id=' + customerId + '&page=0&size=30'
        : '/api/customer-staff?page=0&size=30',
      method: 'GET',
      types: [
        { type: 'API_GET_ALL_REQUEST', meta: { entity: 'user' } },
        {
          type: 'API_GET_ALL_SUCCESS',
          meta: { entity: 'user' },
          payload: (action, state, res) => {
            const contentType = res.headers.get('Content-Type');
            if (contentType && ~contentType.indexOf('json')) {
              return res.json().then(json => {
                const embedded = 'user';
                const result = merge(
                  camelizeKeys(
                    normalize(
                      json._embedded[embedded] || [],
                      arrayOf(UserSchema),
                    ),
                  ),
                  {
                    currentPage: pageToLoad,
                    totalItems: json.page.totalElements,
                    itemsPerPage,
                    filters,
                    sortOrder,
                  },
                );
                return result;
              });
            }
          },
        },
        { type: 'API_GET_ALL_FAILURE', meta: { entity: 'user' } },
      ],
    },
  });
};

export const loadUserById = (userId, callbackAction = false) => (
  dispatch,
  getState,
) => {
  dispatch(getOne('user', userId, false, false, callbackAction));
};

/**
 *
 */
export const removeRole = (roleId, callbackAction = false) => ({
  [CALL_API]: {
    credentials: 'include',
    endpoint: '/api/user/role/' + roleId,
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    types: [
      'API_DELETE_REQUEST',
      {
        type: 'API_DELETE_SUCCESS',
        payload: (action, state, response) => {
          if (typeof callbackAction === 'function') {
            callbackAction();
          }
          return response.json();
        },
      },
      'API_DELETE_FAILURE',
    ],
  },
});

export const saveRole = (userId, role, reload = false) => (
  dispatch,
  getState,
) => {
  if (reload) {
    console.log(role, 'role...');
    dispatch(
      save(
        'role',
        role,
        `user/role/${role.roleName.toLowerCase()}`,
        'save',
        'POST',
        data => {
          dispatch(loadUserById(userId));
        },
      ),
    );
  } else {
    dispatch(save('role', role, `user/role/${role.roleName.toLowerCase()}`));
  }
};

export const saveRoles = (assignedRoles, userId) => (dispatch, getState) => {
  for (let index = 0; index < assignedRoles.length; index++) {
    if (assignedRoles[index].userId === 'add') {
      assignedRoles[index].userId = userId;
    }
    if (index + 1 !== assignedRoles.length) {
      dispatch(saveRole(userId, assignedRoles[index]));
    } else {
      dispatch(saveRole(userId, assignedRoles[index], true));
    }
  }
};

export const removeAndSaveRoles = (removedRoles, assignedRoles, userId) => (
  dispatch,
  getState,
) => {
  for (let index = 0; index < removedRoles.length; index++) {
    if (index + 1 !== removedRoles.length) {
      dispatch(removeRole(removedRoles[index].id));
    } else {
      dispatch(
        removeRole(removedRoles[index].id, () => {
          if (assignedRoles.length > 0) {
            dispatch(saveRoles(assignedRoles, userId));
          } else {
            dispatch(loadUserById(userId));
          }
        }),
      );
    }
  }
};

export const saveUser = (
  formName,
  values,
  assignedRoles,
  onSuccess = false,
  onError = false,
) => (dispatch, getState) => {
  dispatch(
    save('user', values, false, 'save', 'POST', data => {
      const userId = data.id;
      if (userId) {
        if (assignedRoles.length > 0) {
          dispatch(saveRoles(assignedRoles, userId));
        }
      } else {
        dispatch(stopSubmit(formName, data));
      }
    }),
  ).then(apiCallbackHandler(onSuccess, onError));
};

export const agentSaveUser = ({
  formName,
  values,
  role = false,
  customerId = false,
  onSuccess = false,
  onError = false,
}) => (dispatch, getState) => {
  dispatch(
    save(
      'user',
      values,
      `/api/user/${role}/${customerId}`,
      'save',
      'POST',
      data => {
        if (!data.id && formName) {
          dispatch(stopSubmit(formName, data));
        }
      },
    ),
  ).then(apiCallbackHandler(onSuccess, onError));
};

export const updateUser = (
  values,
  assignedRoles,
  removedRoles,
  userId,
  onSuccess = false,
  onError = false,
) => (dispatch, getState) => {
  dispatch(
    update('user', userId, values, false, 'save', 'PUT', data => {
      if (removedRoles.length > 0) {
        dispatch(removeAndSaveRoles(removedRoles, assignedRoles, userId));
      } else if (assignedRoles.length > 0) {
        dispatch(saveRoles(assignedRoles, userId));
      } else {
        dispatch(loadUserById(userId));
      }
    }),
  ).then(apiCallbackHandler(onSuccess, onError));
};

export const saveCustomerUser = (
  userData,
  customerId = false,
  onSuccess = false,
  onError = false,
) => (dispatch, getState) => {
  // Use a selector for this?
  const state = getState();
  const activeRole = state.loggedIn.role;
  if (!customerId) {
    customerId = activeRole.customerId;
  }
  const { id: userId = false, role = 'customer_staff' } = userData;

  if (userId) {
    dispatch(
      update('user', userId, userData, `user/${userId}`),
      'save',
      'PUT',
    ).then(apiCallbackHandler(onSuccess, onError));
  } else {
    dispatch(
      save(
        'user',
        userData,
        `user/${role}/${customerId}`,
        'save',
        'POST',
        data => {
          const {
            currentPage,
            itemsPerPage,
            sortOrder,
            filters,
          } = state.pagination['_user'];
          dispatch(
            loadCustomerUsers(
              customerId,
              currentPage,
              itemsPerPage,
              sortOrder,
              filters,
            ),
          );
        },
      ),
    ).then(apiCallbackHandler(onSuccess, onError));
  }
};

export const removeUserFromCustomer = (
  customerId,
  userId,
  onSuccess = false,
  onError = false,
) => (dispatch, getState) => {
  // get the role ID
  const state = getState();
  const user = selectOneFromEntities(state, 'user', userId);
  const { authorities = [] } = user;

  const customerUserRole = authorities.find(
    role => String(role.customerId) === String(customerId),
  );

  dispatch(removeRole(customerUserRole.id)).then(
    apiCallbackHandler(onSuccess, onError),
  );
};

export const updateRole = (roleName, data) => (dispatch, getState) => {
  return dispatch({
    [CALL_API]: {
      credentials: 'include',
      endpoint: `/api/user/role/${roleName.toLowerCase()}`,
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
      types: [
        { type: 'UPDATE_ROLE_REQUEST' },
        {
          type: 'UPDATE_ROLE_SUCCESS',
          payload: (action, state, response) => {
            return response.json();
          },
        },
        { type: 'UPDATE_ROLE_FAILURE' },
      ],
    },
  });
};

export const attachCustomerRoleToUser = (
  data,
  newRole,
  onSuccess = false,
  onError = false,
) => (dispatch, getState) => {
  dispatch({
    [CALL_API]: {
      credentials: 'include',
      endpoint: `/api/user/role/${newRole.toLowerCase()}/`,
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
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

export const sendNewPasswordById = userId => ({
  [CALL_API]: {
    credentials: 'include',
    endpoint: `/api/user/resetPassword/${userId}`,
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
});

export const updateProfile = (userId, values) => ({
  [CALL_API]: {
    credentials: 'include',
    endpoint: `/api/user/${userId}`,
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(values),
    types: [
      'PREFERENCE_UPDATE_REQUEST',
      {
        type: 'PREFERENCE_UPDATE_SUCCESS',
        meta: {},
        payload: (action, state, response) => {
          const contentType = response.headers.get('Content-Type');
          if (contentType && ~contentType.indexOf('json')) {
            return response.json().then(json => {
              return json;
            });
          } else {
            return { response: response };
          }
        },
      },
      'PREFERENCE_UPDATE_FAILURE',
    ],
  },
});
