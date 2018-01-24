import { v4 } from 'uuid';
import { selectTabByOrderId } from '../reducers';

export const ADD_TAB = 'ADD_TAB';
export const REMOVE_TAB = 'REMOVE_TAB';
export const UPDATE_TAB = 'UPDATE_TAB';
export const ACTIVATE_TAB = 'ACTIVATE_TAB';
export const UPDATE_TAB_TYPE = 'UPDATE_TAB_TYPE';
export const UPDATE_TAB_DATA = 'UPDATE_TAB_DATA';

export const activateTab = id => ({ type: ACTIVATE_TAB, payload: { id } });

export const addTab = tabMeta => (dispatch, getState) => {
  const tabTitle = tabMeta.orderId
    ? `Shipment: ${tabMeta.orderId}`
    : `New Shipment`;
  const tab = { id: v4(), title: tabTitle, ...tabMeta };

  let tabIdOfExistingOrderId = false;

  getState().tabs.openTabs.forEach(item => {
    if (tab['orderId'] && tab['orderId'] === item.orderId) {
      // orderId found
      tabIdOfExistingOrderId = item.id;
    }
  });

  if (tabIdOfExistingOrderId) {
    dispatch(activateTab(tabIdOfExistingOrderId));
  } else {
    dispatch({ type: ADD_TAB, payload: { tab } });
    dispatch(activateTab(tab.id));
  }
};

export const removeTab = (id, activateDefaultTab = true) => (
  dispatch,
  getState,
) => {
  const { tabs: { activeTab = null, previousTab = null } } = getState();

  if (id) {
    dispatch({
      type: REMOVE_TAB,
      payload: { id, activateDefaultTab, activeTab, previousTab },
    });
  }
};

export const removeTabByOrderId = orderId => (dispatch, getState) => {
  const tab = selectTabByOrderId(getState(), orderId);
  dispatch(removeTab(tab.id, false));
};

export const updateTab = (id, data) => {
  return { type: UPDATE_TAB, payload: { id, data } };
};

export const updateTabType = (id, value, ...data) => {
  return { type: UPDATE_TAB_TYPE, payload: { id, value, ...data } };
};

export const updateTabData = (id, key, value) => {
  return { type: UPDATE_TAB_DATA, payload: { id, key, value } };
};

export const INVOICE_ADD_TAB = 'INVOICE_ADD_TAB';
export const INVOICE_REMOVE_TAB = 'INVOICE_REMOVE_TAB';
export const INVOICE_ACTIVATE_TAB = 'INVOICE_ACTIVATE_TAB';

export const activateInvoiceTab = (invoiceId = 'tab-content-list') => {
  return {
    type: INVOICE_ACTIVATE_TAB,
    payload: { id: invoiceId },
  };
};

export const addInvoiceTab = invoiceId => dispatch => {
  dispatch({
    type: INVOICE_ADD_TAB,
    payload: { id: invoiceId },
  });
  dispatch(activateInvoiceTab(invoiceId));
};

export const removeInvoiceTab = invoiceId => dispatch => {
  dispatch({
    type: INVOICE_REMOVE_TAB,
    payload: { id: invoiceId },
  });
  dispatch(activateInvoiceTab());
};

export const PAYABLE_ADD_TAB = 'PAYABLE_ADD_TAB';
export const PAYABLE_REMOVE_TAB = 'PAYABLE_REMOVE_TAB';
export const PAYABLE_ACTIVATE_TAB = 'PAYABLE_ACTIVATE_TAB';

export const activatePayableTab = (itemId = 'tab-payable-statement') => {
  return {
    type: PAYABLE_ACTIVATE_TAB,
    payload: { id: itemId },
  };
};

export const addPayableTab = item => dispatch => {
  dispatch({
    type: PAYABLE_ADD_TAB,
    payload: { ...item },
  });
  dispatch(activatePayableTab(item.id));
};

export const removePayableTab = itemId => dispatch => {
  dispatch({
    type: PAYABLE_REMOVE_TAB,
    payload: { id: itemId },
  });
  dispatch(activatePayableTab());
};
