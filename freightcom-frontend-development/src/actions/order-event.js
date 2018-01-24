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

let handlers = {};

/**
 * signal an order event
 */
export const signalOrderEvent = event => (dispatch, getState) => {
  for (let key in handlers) {
    if (handlers[key]) {
      handlers[key](event, dispatch, getState);
    }
  }
};

export const registerOrderEventHandler = (key, handler) => {
  handlers[key] = handler;
};
