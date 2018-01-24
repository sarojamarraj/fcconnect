import { update } from './api';
import { SystemPropertySchema } from '../schemas';
import { CALL_API } from 'redux-api-middleware';
import { normalize, arrayOf } from 'normalizr';
import { camelize, camelizeKeys, decamelize } from 'humps';
import merge from 'lodash/merge';
import { apiCallbackHandler } from '../utils';

export const loadSettings = () => (dispatch, getState) => {
  const { entities } = getState();

  if (entities && entities.systemProperties) {
    return new Promise(resolve => resolve());
  } else {
    return dispatch({
      [CALL_API]: {
        credentials: 'include',
        endpoint: '/api/system_property',
        method: 'GET',
        types: [
          {
            type: 'API_GET_ALL_REQUEST',
            meta: { entity: camelize('systemProperties') },
          },
          {
            type: 'API_GET_ALL_SUCCESS',
            meta: { entity: camelize('systemProperties') },
            payload: (action, state, response) => {
              const contentType = response.headers.get('Content-Type');
              if (contentType && ~contentType.indexOf('json')) {
                return response.json().then(json => {
                  const result = merge(
                    camelizeKeys(
                      normalize(
                        json._embedded['systemProperties'] || [],
                        arrayOf(SystemPropertySchema),
                      ),
                    ),
                  );
                  return result;
                });
              }
            },
          },
          {
            type: 'API_GET_ALL_FAILURE',
            meta: { entity: camelize('systemProperties') },
          },
        ],
      },
    });
  }
};

export const updateSetting = (
  settingName,
  settingData,
  onSuccess = false,
  onError = false,
) => (dispatch, getState) => {
  dispatch(
    update(
      'systemProperties',
      settingName,
      settingData,
      `system_property/${settingName}`,
      'save',
      'PUT',
    ),
  ).then(apiCallbackHandler(onSuccess, onError));
};

export const updateSettings = (values, onSuccess = false, onError = false) => (
  dispatch,
  getState,
) => {
  var i = 1;
  for (var setting of Object.keys(values)) {
    i++;
    if (Object.keys(values).length === i) {
      dispatch(
        updateSetting(
          decamelize(setting, { separator: '_' }),
          {
            data: values[setting],
          },
          onSuccess,
          onError,
        ),
      );
    } else {
      dispatch(
        updateSetting(decamelize(setting, { separator: '_' }), {
          data: values[setting],
        }),
      );
    }
  }
};
