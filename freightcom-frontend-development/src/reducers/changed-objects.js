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

import { API_SAVE_SUCCESS } from '../actions/api.js';

const CLEAR_SAVED_OBJECTS = 'clear-saved-objects';

let registered = {};

export const registerChangedInterest = (key, entity) => {
  if (!registered[key]) {
    registered[key] = [entity];
  } else {
    registered[key].push(entity);
  }
};

export const clearChangedObjects = key => ({
  type: CLEAR_SAVED_OBJECTS,
  meta: key,
});

const changedObjects = (state = {}, action) => {
  if (action.type === API_SAVE_SUCCESS) {
    let updates = {};

    for (let key in registered) {
      for (let index in registered[key]) {
        if (
          action.payload &&
          (action.payload.result || action.payload.entity) &&
          action.meta &&
          registered[key][index] === action.meta.entity
        ) {
          if (!updates[key]) {
            if (state[key]) {
              updates[key] = [...state[key]];
            } else {
              updates[key] = [];
            }
          }

          updates[key].push(action.payload.result);
        }
      }
    }

    return { ...state, ...updates };
  } else if (action.type === CLEAR_SAVED_OBJECTS) {
    const updates = state.updates || {};

    return { ...state, ...{ ...updates, [action.meta]: [] } };
  }

  return state;
};

export const hasChangedInterest = (state, key) =>
  state.changedObjects ? state.changedObjects[key] || [] : [];

export default changedObjects;
