import { CALL_API } from 'redux-api-middleware';
import { loadAgentById } from './agents';
import { loadCustomerById } from './customers';

export const SUBMIT_LOGOUT_REQUEST = 'SUBMIT_LOGOUT_REQUEST';
export const SUBMIT_LOGOUT_SUCCESS = 'SUBMIT_LOGOUT_SUCCESS';
export const SUBMIT_LOGOUT_FAILURE = 'SUBMIT_LOGOUT_FAILURE';

export const RESET_PASSWORD_REQUEST = 'RESET_PASSWORD_REQUEST';
export const RESET_PASSWORD_SUCCESS = 'RESET_PASSWORD_SUCCESS';
export const RESET_PASSWORD_FAILURE = 'RESET_PASSWORD_FAILURE';

export const SUBMIT_LOGIN_REQUEST = 'SUBMIT_LOGIN_REQUEST';
export const SUBMIT_LOGIN_SUCCESS = 'SUBMIT_LOGIN_SUCCESS';
export const SUBMIT_LOGIN_FAILURE = 'SUBMIT_LOGIN_FAILURE';

export const USER_INFO_REQUEST = 'USER_INFO_REQUEST';
export const USER_INFO_SUCCESS = 'USER_INFO_SUCCESS';
export const USER_INFO_FAILURE = 'USER_INFO_FAILURE';

export const CHECK_LOGGED_IN_REQUEST = 'CHECK_LOGGED_IN_REQUEST';
export const CHECK_LOGGED_IN_SUCCESS = 'CHECK_LOGGED_IN_SUCCESS';
export const CHECK_LOGGED_IN_FAILURE = 'CHECK_LOGGED_IN_FAILURE';

export const SELECT_ROLE_REQUEST = 'SELECT_ROLE_REQUEST';
export const SELECT_ROLE_SUCCESS = 'SELECT_ROLE_SUCCESS';
export const SELECT_ROLE_FAILURE = 'SELECT_ROLE_FAILURE';

export const GET_ROLE_REQUEST = 'GET_ROLE_REQUEST';
export const GET_ROLE_SUCCESS = 'GET_ROLE_SUCCESS';
export const GET_ROLE_FAILURE = 'GET_ROLE_FAILURE';

export const STORE_URL = Symbol('store url');
export const CLEAR_STORED_URL = Symbol('clear stored url');

export const CLEAR_LOGIN_MESSAGE = Symbol('clear-login-message');

export const NOT_LOGGED_IN = 'not logged in';
export const LOGGED_IN = Symbol('logged in');
export const PRE_LOGGED_IN = Symbol('login not checked');

const $ = window.jQuery;

/**
 * Cause state to store url parameter
 */
export const storeUrl = url => (dispatch, getState) => {
  return dispatch({
    type: STORE_URL,
    url,
  });
};

/**
 * Cause state to clear stored url parameter
 */
export const clearStoredUrl = () => (dispatch, getState) => {
  return dispatch({
    type: CLEAR_STORED_URL,
  });
};

/**
 * Fetch user information after a successful login
 */
const getUserInfo = id => (dispatch, getState) => {
  return dispatch({
    [CALL_API]: {
      credentials: 'include',
      endpoint: `api/user/${id}`,
      method: 'GET',
      headers: {
        Accept: 'application/json',
      },
      types: [
        USER_INFO_REQUEST,
        {
          type: USER_INFO_SUCCESS,
          payload: (action, state, response) => {
            return response.json().then(json => {
              dispatch({
                type: SUBMIT_LOGIN_SUCCESS,
                payload: json,
              });

              return json;
            });
          },
        },
        USER_INFO_FAILURE,
      ],
    },
  });
};

/**
 * Request logged in user info to check if logged in
 */
export const checkLoggedIn = () => {
  return {
    [CALL_API]: {
      credentials: 'include',
      endpoint: '/api/logged-in-user',
      method: 'GET',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
        'Access-Control-Max-Age': '3600',
      },
      types: [
        CHECK_LOGGED_IN_REQUEST,
        {
          type: CHECK_LOGGED_IN_SUCCESS,
          meta: {},
          payload: (action, state, response) => {
            const contentType = response.headers.get('Content-Type');

            if (contentType && ~contentType.indexOf('json')) {
              return response.json().then(json => {
                return json;
              });
            }
          },
        },
        {
          type: CHECK_LOGGED_IN_FAILURE,
          payload: (action, state, response) => {
            const contentType = response.headers.get('Content-Type');

            if (contentType && ~contentType.indexOf('json')) {
              return response.json().then(json => {
                return json;
              });
            }
          },
        },
      ],
    },
  };
};

// Submits a login request
export const loginAction = (values, frame, form, callbackAction = false) => (
  dispatch,
  getState,
) => {
  //console.debug("SUBMIT LOGIN")

  dispatch({
    type: SUBMIT_LOGIN_REQUEST,
  });

  frame.on('load', e => {
    const id = $('#login-submit-frame')
      .contents()
      .find('input[name="user-id"]')
      .val();

    //console.debug("ONLOAD " + id)

    if (id) {
      dispatch(getUserInfo(id)).then(result => {
        if (typeof callbackAction === 'function') {
          callbackAction(result);
        }
      });
    } else {
      dispatch({
        type: SUBMIT_LOGIN_FAILURE,
      }).then(result => {
        if (typeof callbackAction === 'function') {
          callbackAction(result);
        }
      });
    }
  });

  //console.debug("SUBMITTING")
  form.submit();
};

/**
 * Submits a logout request to the server
 */
export const logoutAction = () => {
  return {
    [CALL_API]: {
      credentials: 'include',
      endpoint: `api/logout`,
      method: 'GET',
      headers: {
        Accept: 'application/json',
      },
      types: [
        SUBMIT_LOGOUT_REQUEST,
        {
          type: SUBMIT_LOGOUT_SUCCESS,
          payload: (action, state, response) => {
            return {
              'logout-message': 'logged out',
            };
          },
        },
        SUBMIT_LOGOUT_FAILURE,
      ],
    },
  };
};

/**
 * Submits a password reset request
 */
export const passwordDialog = setStatus => (dispatch, getState) => {
  return dispatch({
    type: CLEAR_LOGIN_MESSAGE,
    payload: {
      message: '',
      reset_message: '',
      reset_status: setStatus,
    },
  });
};

/**
 * Submits a password reset request
 */
export const passwordResetAction = email => (dispatch, getState) => {
  return dispatch({
    type: CLEAR_LOGIN_MESSAGE,
    payload: {
      message: '',
      reset_message: '',
      message_class: '',
      reset_status: 'open',
    },
  }).then(() => {
    if (!email) {
      return dispatch({
        type: RESET_PASSWORD_FAILURE,
        payload: {
          reset_message: 'Please enter an email address',
        },
      });
    } else {
      return dispatch({
        [CALL_API]: {
          credentials: 'include',
          endpoint: `api/user/resetPasswordByEmail`,
          method: 'POST',
          body: JSON.stringify({ email }),
          headers: {
            Accept: 'application/json',
            'content-type': 'application/json',
          },
          types: [
            RESET_PASSWORD_REQUEST,
            {
              type: RESET_PASSWORD_SUCCESS,
              payload: (action, state, response) => {
                return response.json().then(json => {
                  const [result] = json;

                  if (result === 'failed') {
                    return {
                      reset_message: 'Unable to reset password',
                      reset_status: 'open',
                      message_class: '',
                    };
                  } else if (result === 'error') {
                    return {
                      reset_message: 'Error resetting password',
                      reset_status: 'open',
                      message_class: '',
                    };
                  } else {
                    return {
                      message: 'An email has been sent',
                      reset_status: 'closed',
                      message_class: 'ok',
                    };
                  }
                });
              },
            },
            RESET_PASSWORD_FAILURE,
          ],
        },
      });
    }
  });
};

/**
 * Set the user role in the backend
 */
export const setSelectedRole = roleId => (dispatch, getState) => {
  return dispatch({
    [CALL_API]: {
      credentials: 'include',
      endpoint: '/api/set-session-role/' + roleId,
      method: 'POST',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
        'Access-Control-Max-Age': '3600',
      },
      types: [
        SELECT_ROLE_REQUEST,
        {
          type: SELECT_ROLE_SUCCESS,
          payload: (action, state, response) => {
            const contentType = response.headers.get('Content-Type');

            if (contentType && ~contentType.indexOf('json')) {
              return response.json().then(json => {
                return json;
              });
            }
          },
        },
        {
          type: SELECT_ROLE_FAILURE,
          payload: (action, state, response) => {
            const contentType = response.headers.get('Content-Type');

            if (contentType && ~contentType.indexOf('json')) {
              return response.json().then(json => {
                return json;
              });
            }
          },
        },
      ],
    },
  });
};

export const loadLoggedInCustomer = loginInfo => (dispatch, getState) => {
  return dispatch(
    loadCustomerById(loginInfo.role.customerId, true, null, {
      loginLoadCustomer: true,
    }),
  );
};

export const getLoginRole = () => (dispatch, getState) => {
  const { loggedIn: { role = null } } = getState();

  (role
    ? new Promise((resolve, reject) =>
        resolve({
          type: 'role is fine then',
          payload: {
            roleName: role.roleName,
            customerId: role.customerId,
            id: role.id,
          },
        }),
      )
    : dispatch({
        [CALL_API]: {
          credentials: 'include',
          endpoint: '/api/session-role',
          method: 'GET',
          headers: {
            Accept: 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Max-Age': '3600',
          },
          types: [
            SELECT_ROLE_REQUEST,
            {
              type: SELECT_ROLE_SUCCESS,
              meta: {},
              payload: (action, state, response) => {
                const contentType = response.headers.get('Content-Type');

                if (contentType && ~contentType.indexOf('json')) {
                  return response.json().then(json => {
                    return json;
                  });
                }
              },
            },
            {
              type: SELECT_ROLE_FAILURE,
              payload: (action, state, response) => {
                const contentType = response.headers.get('Content-Type');

                if (contentType && ~contentType.indexOf('json')) {
                  return response.json().then(json => {
                    return json;
                  });
                }
              },
            },
          ],
        },
      })).then(data => {
    switch (data.type === SELECT_ROLE_SUCCESS) {
      case data.payload.roleName === 'CUSTOMER_ADMIN':
        dispatch(loadCustomerById(data.payload.customerId));
        break;
      case data.payload.roleName === 'CUSTOMER_STAFF':
        dispatch(loadCustomerById(data.payload.customerId));
        break;
      case data.payload.roleName === 'AGENT':
        dispatch(loadAgentById(data.payload.id));
        break;
      default:
        break;
    }
  });
};
