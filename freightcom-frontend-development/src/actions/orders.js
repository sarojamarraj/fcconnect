import { CALL_API } from 'redux-api-middleware';
import { normalize, arrayOf } from 'normalizr';
import { camelizeKeys } from 'humps';
import { getFormValues, getFormMeta } from 'redux-form';
import formatDate from 'date-fns/format';
import { v4 } from 'uuid';
import { queryString } from '../utils';
import { change, initialize } from 'redux-form';
import { addNotification } from 'reapop';
import { selectOneFromEntities, selectActiveTab } from '../reducers';
import { getAll, getOne, save, update, getAllOptions } from './api';
import { addTab, updateTab } from './tabs';
import {
  DraftOrderSchema,
  OrderSchema,
  JobBoardSchema,
  DisputeListSchema,
  ClaimListSchema,
  OrderRateSchema,
  OrderStatusSchema,
  PackageTypeSchema,
  OrderLogSchema,
  RatesSchema,
  AccesorialServicesSchema,
  PackageTemplateSchema,
  CarrierSchema,
  ServiceSchema,
} from '../schemas';

export const UPDATE_ORDER_FOR_BOOK_REQUEST = 'UPDATE_ORDER_FOR_BOOK_REQUEST';
export const UPDATE_ORDER_FOR_BOOK_SUCCESS = 'UPDATE_ORDER_FOR_BOOK_SUCCESS';
export const UPDATE_ORDER_FOR_BOOK_FAILURE = 'UPDATE_ORDER_FOR_BOOK_FAILURE';
export const UPDATE_ORDER_FOR_BOOK_CLEAR = 'UPDATE_ORDER_FOR_BOOK_CLEAR';

/**
 * Load an order by id
 */
export const loadOrderById = (orderId, forceFetch = false) => (
  dispatch,
  getState,
) => {
  const state = getState();
  const orderData = selectOneFromEntities(state, 'order', orderId);

  if (!orderData['id'] || forceFetch) {
    return dispatch(getOne('order', orderId));
  } else {
    return new Promise(resolve => resolve(orderData));
  }
};

export const loadDraftOrderById = (
  orderId,
  forceFetch = false,
  callback = () => {},
) => (dispatch, getState) => {
  const state = getState();
  const orderData = selectOneFromEntities(state, 'draftOrder', orderId);

  if (!orderData['id'] || forceFetch) {
    dispatch(getOne('draftOrder', orderId, false, `order/${orderId}`)).then(
      callback,
    );
  }
};

/**
 * Use for listing orders
 */
export const loadOrders = (
  pageToLoad = 1,
  itemsPerPage = 10,
  filters = {},
  sortOrder = { sortBy: 'id', order: 'DESC' },
) => (dispatch, getState) => {
  return dispatch(
    getAll(
      'order',
      OrderSchema,
      pageToLoad,
      itemsPerPage,
      filters,
      sortOrder,
      'order',
      'customerOrders',
    ),
  );
};

export const loadCountOrders = (
  mode,
  filters,
  entity = '_order',
) => dispatch => {
  return dispatch({
    [CALL_API]: {
      credentials: 'include',
      endpoint: `/api/order/count/${mode}?` + queryString(filters),
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      types: [
        'ORDER_COUNT_REQUEST',
        {
          type: 'ORDER_COUNT_SUCCESS',
          meta: { key: entity },
          payload: (action, state, response) => {
            return response.json(json => ({ response: json }));
          },
        },
        'ORDER_COUNT_FAILURE',
      ],
    },
  });
};

export const loadSubmittedOrders = (
  pageToLoad = 1,
  itemsPerPage = 10,
  filters = {},
  sortOrder = { sortBy: 'id', order: 'DESC' },
  meta = {},
) => {
  return getAll(
    'order',
    OrderSchema,
    pageToLoad,
    itemsPerPage,
    filters,
    sortOrder,
    'submitted-orders',
    'customerOrders',
    false,
    meta,
  );
};

export const loadDraftOrders = (
  pageToLoad = 1,
  itemsPerPage = 10,
  filters = {},
  sortOrder = { sortBy: 'id', order: 'DESC' },
  meta = {},
) => {
  return getAll(
    'draftOrder',
    DraftOrderSchema,
    pageToLoad,
    itemsPerPage,
    filters,
    sortOrder,
    'draft-orders',
    'customerOrders',
    false,
    meta,
  );
};

export const loadJobBoard = (
  pageToLoad = 1,
  itemsPerPage = 10,
  filters = {},
  sortOrder = { sortBy: 'id', order: 'DESC' },
  meta = {},
) => {
  return getAll(
    'jobBoard',
    JobBoardSchema,
    pageToLoad,
    itemsPerPage,
    filters,
    sortOrder,
    'job-board',
    'customerOrders',
    false,
    meta,
  );
};

export const loadClaims = (
  pageToLoad = 1,
  itemsPerPage = 10,
  filters = {},
  sortOrder = { sortBy: 'id', order: 'DESC' },
) => {
  return getAll(
    'claimList',
    ClaimListSchema,
    pageToLoad,
    itemsPerPage,
    filters,
    sortOrder,
    'orders-with-claims',
    'customerOrders',
  );
};

export const loadDisputeList = (
  pageToLoad = 1,
  itemsPerPage = 10,
  filters = {},
  sortOrder = { sortBy: 'id', order: 'DESC' },
) => {
  return getAll(
    'disputeList',
    DisputeListSchema,
    pageToLoad,
    itemsPerPage,
    filters,
    sortOrder,
    'orders-with-disputes',
    'customerOrders',
  );
};

export const loadOrderCount = () => dispatch => {
  fetch('/api/order/stats-per-type', { credentials: 'include' })
    .then(res => res.json())
    .then(json => {
      dispatch({
        type: 'LOAD_ORDER_STATS',
        payload: { ...json },
      });
    })
    .catch(err => {
      console.error(err);
    });
};

/**
 * This function isn't currently in use.
 * TODO: Make this a reusable function for all saving of orders
 *
 * This should accept formName and callback.
 * It should determine if order is a new order or update order
 */
export const saveOrder = (name, data) => (dispatch, getState) => {
  if (!data.id) {
    dispatch(save('order', data, 'order/create', name));
  } else {
    dispatch(update('order', data.id, data, 'order/update', name));
  }
};

export const updateOrder = (orderId, orderData, callback) => dispatch => {
  dispatch(update('order', orderId, orderData)).then(callback);
};

export const updateScheduledPickup = (
  orderId,
  orderData,
  callback,
) => dispatch => {
  dispatch(
    update(
      'order',
      orderId,
      { scheduledPickup: orderData.scheduledPickup },
      `order/schedule-pickup/{orderId}`,
    ),
  ).then(callback);
};

export const updateOrderCharge = (
  orderId,
  chargeData,
  callback,
) => dispatch => {
  dispatch(
    update(
      'charge',
      orderId,
      chargeData,
      `/order/${orderId}/charge`,
      'save',
      'POST',
    ),
  ).then(callback);
};

export const deleteCharge = (chargeId, callback) => dispatch => {
  dispatch({
    [CALL_API]: {
      credentials: 'include',
      endpoint: '/api/order/charge/' + chargeId,
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json' },
      types: [
        'API_DELETE_REQUEST',
        {
          type: 'API_DELETE_SUCCESS',
          payload: (action, state, response) => {
            return response.json(json => ({ response: json }));
          },
        },
        'API_DELETE_FAILURE',
      ],
    },
  }).then(callback);
};

const orderFormUpdates = (dispatch, formName, data) => {
  dispatch({
    type: 'FORM_FORCE_FIELD_UPDATE',
    payload: {
      form: formName,
      field: 'pallets',
      value: data.pallets,
    },
  });
  dispatch({
    type: 'FORM_FORCE_FIELD_UPDATE',
    payload: {
      form: formName,
      field: 'packages',
      value: data.packages,
    },
  });
  dispatch(
    updateTab(formName, {
      orderId: data.id,
      title: `Quote: ${data.id}`,
    }),
  );
  dispatch(change(formName, 'id', data.id));
};

/**
 * This dispatched when user hits the "Get Rates" button in the shipment form
 *
 * XXX: `isWorkOrder` processing should not be here, Ty!
 */
export const getShipmentRates = (formName, isWorkOrder = false) => (
  dispatch,
  getState,
) => {
  // NOTE:  This is a copy from the saveOrderAsDraft since backend
  //        requires orders to be saved first before retreiving carrier
  //        rates.
  const state = getState();
  const formData = getFormValues(formName)(state);

  // console.debug('META');
  // console.debug(getFormMeta(formName)(state));
  getFormMeta(formName)(state);
  // console.debug(Object.entries(getFormMeta(formName)(state)));
  // console.debug(
  //   Object.entries(getFormMeta(formName)(state))
  //     .filter(([key, value]) => value.touched)
  //     .map(([key, value]) => key),
  //  );

  // TODO:  - Add customerId
  //        - Check if distribution Id,
  //        - Set order id
  const customerId = formData['customerId'] || state.loggedIn.role.customerId;

  const orderId = formData['id'] || false;

  // Address book company name is different from shipping address company name
  const shipFrom = formData['shipFrom'] || {};
  const { consigneeName: shipFromCompany = '' } = shipFrom;

  const shipTo = formData['shipTo'] || {};
  const {
    consigneeName: shipToCompany = '',
    distributionListId = false,
  } = shipTo;

  const distributionList = distributionListId
    ? { distributionGroupId: distributionListId, shipTo: null }
    : {};
  const formDataToSave = Object.assign({}, formData, {
    customerId,
    customer: null,
    shipFrom: Object.assign({}, shipFrom, {
      company: shipFromCompany,
      companyName: shipFromCompany,
    }),
    shipTo: Object.assign({}, shipTo, {
      company: shipToCompany,
      companyName: shipToCompany,
    }),

    // XXX: For work order
    customWorkOrder: isWorkOrder,

    ...distributionList,
  });

  delete formDataToSave['orderStatus'];
  delete formDataToSave['selectedQuote'];
  delete formDataToSave['charges'];

  dispatch({ type: 'GET_ORDER_RATES_REQUEST', meta: { form: formName } });

  if (orderId) {
    dispatch(
      update(
        'draftOrder',
        orderId,
        formDataToSave,
        `order/${orderId}`,
        'save',
        'PUT',
        data => {
          if (!isWorkOrder) {
            dispatch(
              updateTab(formName, {
                orderId: data.id,
                title: `Quote: ${data.id}`,
              }),
            );
            // eslint-disable-next-line
            dispatch(requestShipmentRates(formName, data.id));
          } else {
            // eslint-disable-next-line
            dispatch(confirmOrder(formName, loadOrders, isWorkOrder));
          }
        },
      ),
    );
  } else {
    dispatch(
      save('draftOrder', formDataToSave, 'order', 'save', 'POST', data => {
        if (data.id) {
          orderFormUpdates(dispatch, formName, data);

          if (!isWorkOrder) {
            dispatch(
              updateTab(formName, {
                orderId: data.id,
                title: `Quote: ${data.id}`,
              }),
            );
            // eslint-disable-next-line
            dispatch(requestShipmentRates(formName, data.id));
          } else {
            // eslint-disable-next-line
            dispatch(confirmOrder(formName, loadOrders, isWorkOrder));
          }
        } else {
        }
      }),
    );
  }
};

export const requestShipmentRates = (formName, orderId, getMoreRequest) => (
  dispatch,
  getState,
) => {
  const endpoint = getMoreRequest
    ? `/api/carrier_rates/${orderId}/refresh`
    : `/api/carrier_rates/${orderId}`;

  dispatch({
    [CALL_API]: {
      credentials: 'include',
      endpoint: endpoint,
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      types: [
        {
          type: 'GET_ORDER_RATES_REQUEST',
          meta: { form: formName, getMoreRequest },
        },
        {
          type: 'GET_ORDER_RATES_SUCCESS',
          meta: { orderForm: formName, form: formName, getMoreRequest },
          payload: (action, state, res) => {
            const contentType = res.headers.get('Content-Type');
            if (contentType && ~contentType.indexOf('json')) {
              return res.json().then(json => {
                if (json._content) {
                  return {
                    _messages: json.messages,
                    ...camelizeKeys(
                      normalize(json._content, arrayOf(OrderRateSchema)),
                    ),
                  };
                } else {
                  return { result: [] };
                }
              });
            }
          },
        },
        {
          type: 'GET_ORDER_RATES_FAILURE',
          meta: { form: formName, getMoreRequest },
        },
      ],
    },
  });
};

/**
 * This expects order to be already saved and have orderId
 */
export const confirmOrder = (
  formName,
  loadShipmentList,
  isWorkOrder = false,
) => (dispatch, getState) => {
  const state = getState();
  const formData = getFormValues(formName)(state);
  const customerId =
    formData['customerId'] || state.loggedIn.role.customerId || '0';

  const orderId = formData['id'] || false;
  const carrierId = formData['carrierId'] || false;

  // Address book company name is different from shipping address company name
  const shipFrom = formData['shipFrom'] || {};
  const { consigneeName: shipFromCompany = '' } = shipFrom;

  const shipTo = formData['shipTo'] || {};
  const {
    consigneeName: shipToCompany = '',
    distributionListId = false,
  } = shipTo;

  const distributionList = distributionListId
    ? { distributionGroupId: distributionListId, shipTo: null }
    : {};
  const formDataToSave = Object.assign({}, formData, {
    customerId,
    customer: null,
    shipFrom: Object.assign({}, shipFrom, {
      company: shipFromCompany,
      companyName: shipFromCompany,
    }),
    shipTo: Object.assign({}, shipTo, {
      company: shipToCompany,
      companyName: shipToCompany,
    }),
    ...distributionList,
  });

  delete formDataToSave['latestComment'];
  delete formDataToSave['selectedQuote'];
  delete formDataToSave['orderStatus'];
  delete formDataToSave['pallets'];
  delete formDataToSave['packages'];

  if (orderId && carrierId) {
    dispatch(
      update(
        'draftOrder',
        orderId,
        formDataToSave,
        `order/select-rate/${orderId}/${carrierId}`,
        'save',
        'POST',
        data => {
          dispatch({
            type: 'SHIPMENT_UPDATE',
            payload: camelizeKeys(normalize(data, DraftOrderSchema)),
            meta: { entity: 'draftOrder' },
          });

          dispatch(
            updateTab(formName, {
              //orderId: data.id,
              title: `Confirm: ${data.id}`,
              type: 'ORDER_CONFIRM',
            }),
          );

          loadShipmentList();
        },
      ),
    );
  } else if (orderId && isWorkOrder) {
    loadShipmentList();

    setTimeout(() => {
      dispatch(
        updateTab(formName, {
          title: `Work Order: ${orderId}`,
          type: 'WORK_ORDER_CONFIRM',
          orderId: orderId,
          workOrder: true,
        }),
      );
    }, 2000);
  } else {
    // This shouldn't be allowed. dispatch error
    // this doesn't do anything right now...
    dispatch({
      type: 'SHIPMENT_ERROR',
    });
  }
};

export const sendQuote = (formName, callback) => (dispatch, getState) => {
  const state = getState();
  const orderId = selectActiveTab(state).orderId;
  const orderData = getFormValues(formName)(state);
  const sanitizedOrderData = {
    id: orderId,
    scheduledPickup: orderData.sheduledPickup || {},
  };

  return dispatch(update('order', orderId, sanitizedOrderData)).then(() => {
    dispatch(
      update(
        'order',
        orderId,
        sanitizedOrderData,
        `order/send-quote/${orderId}`,
        'save',
        'POST',
      ),
    ).then(callback);
  });
};

export const updateOrderForBook = formName => (dispatch, getState) => {
  const state = getState();
  const orderId = selectActiveTab(state).orderId;
  const orderData = getFormValues(formName)(state);

  return dispatch({
    [CALL_API]: {
      credentials: 'include',
      endpoint: `/api/order/save-pickup-and-customs/${orderId}`,
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        scheduledPickup: orderData.scheduledPickup,
        customsInvoice: orderData.customsInvoice,
      }),
      types: [
        {
          type: UPDATE_ORDER_FOR_BOOK_REQUEST,
          meta: { formName },
        },
        {
          type: UPDATE_ORDER_FOR_BOOK_SUCCESS,
          meta: { formName },
          payload: (action, state, response) => {
            return response.json(json => ({ response: json }));
          },
        },
        {
          type: UPDATE_ORDER_FOR_BOOK_FAILURE,
          meta: { formName },
        },
      ],
    },
  });
};

export const clearOrderForBook = formName => ({
  type: UPDATE_ORDER_FOR_BOOK_CLEAR,
  meta: { formName },
});

export const bookOrder = (formName, loadShipmentList) => (
  dispatch,
  getState,
) => {
  const state = getState();
  const orderId = selectActiveTab(state).orderId;
  const orderData = getFormValues(formName)(state);
  const sanitizedOrderData = {
    id: orderId,
    scheduledPickup: orderData.scheduledPickup || {},
    customsInvoice: orderData.customsInvoice || {},
  };

  dispatch(
    update(
      'order',
      orderId,
      sanitizedOrderData,
      `order/book/${orderId}`,
      'save',
      'POST',
      data => {
        dispatch(
          updateTab(formName, {
            title: `Shipment: ${orderId}`,
            type: 'ORDER_VIEW',
          }),
        );
        // eslint-disable-next-line
        dispatch(loadOrderLogs(orderId));
        loadShipmentList();
      },
    ),
  );
};

export const cancelOrder = (orderId, callback) => dispatch => {
  fetch(`/api/order/${orderId}/cancel`, {
    credentials: 'include',
    method: 'PUT',
  })
    .then(res => {
      if (!res.ok) {
        res.json().then(json => {
          dispatch(
            addNotification({
              title: 'Error on Cancel Order',
              message: json.status || json._,
              status: 'error',
            }),
          );
        });
        throw new Error('Something wrong...');
      }

      callback();
    })
    .catch(err => console.error(err));
};

export const initializeOrderForm = orderId => {
  return (dispatch, getState) => {
    const formName = v4();

    const {
      draftOrder: { [orderId]: orderData = false },
      //customer = {},
      shippingAddress = {},
    } = getState().entities;

    if (orderData) {
      const shipDate = orderData.shipDate
        ? formatDate(orderData.shipDate, 'YYYY-MM-DD')
        : '';

      const accessorialServices = orderData.accessorialServiceNames
        ? orderData.accessorialServiceNames.reduce((acc, val) => {
            acc[val] = true;
            return acc;
          }, {})
        : {};

      //const formData = Object.assign({}, orderData, {
      const formData = {
        id: orderId,
        customerId: orderData.customer,
        shipFrom: shippingAddress[orderData.shipFrom],
        shipTo: shippingAddress[orderData.shipTo],
        shipDate: shipDate,
        pickupDate: shipDate,

        customWorkOrder: orderData.customWorkOrder,

        referenceCode: orderData.referenceCode || '',

        accessorialServices,
        packageTypeName: orderData.packageTypeName,

        pallets: orderData.pallets || [],
        packages: orderData.packages || [],

        weight: orderData.weight,
        dangerousGoods: orderData.dangerousGoods || 'None',
        signatureRequired: orderData.signatureRequired,
        insuranceType: orderData.insuranceType,
        insuranceCurrency: orderData.insuranceCurrency,

        selectedQuote: orderData.selectedQuote || null,
      };

      dispatch(initialize(formName, formData));

      if (formData.customWorkOrder) {
        dispatch(
          addTab({
            id: formName,
            title: 'Work Order: ' + orderId,
            orderId: orderId,
            type: 'WORK_ORDER_CONFIRM',
            workOrder: true,
          }),
        );
      } else if (formData.selectedQuote) {
        dispatch(
          addTab({
            id: formName,
            title: 'Confirm: ' + orderId,
            orderId: orderId,
            type: 'ORDER_CONFIRM',
          }),
        );
      } else {
        dispatch(
          addTab({
            id: formName,
            title: 'Quote: ' + orderId,
            orderId: orderId,
            type: 'ORDER_QUOTE',
          }),
        );
      }
    } else {
      // fetch order data
      dispatch(loadOrderById(orderId));
    }
  };
};

export const viewOrder = (orderId, command) => {
  if (orderId === 'new-shipment') {
    return addTab({
      title: 'New Shipment',
      type: 'ORDER_QUOTE',
    });
  } else if (orderId === 'new-quote') {
    return addTab({
      title: 'New Quote',
      type: 'ORDER_QUOTE',
      isInstantRate: true,
    });
  } else {
    return addTab({
      title: `Shipment: ${orderId}`,
      orderId: orderId,
      type: 'ORDER_VIEW',
      command,
    });
  }
};

export const updateStatus = orderId => {
  return addTab({
    id: v4() + '-update-status',
    orderId: orderId,
    title: 'Update Status',
    type: 'UPDATE_STATUS',
  });
};

export const duplicateOrder = orderId => {
  return (dispatch, getState) => {
    dispatch(
      addTab({
        id: v4(),
        title: 'New Shipment',
        type: 'ORDER_QUOTE',
        duplicate: orderId,
      }),
    );
  };
};

export const loadDuplicate = (orderId, formName) =>
  getOne(
    'orderTemplate',
    orderId,
    false,
    `order/duplicate/${orderId}`,
    null,
    null,
    {
      duplicate: orderId,
      formName,
    },
  );

export const saveOrderAsDraft = (formName, callback = () => {}) => (
  dispatch,
  getState,
) => {
  const state = getState();
  const formData = getFormValues(formName)(state) || {};

  // TODO:  - Add customerId
  //        - Check if distribution Id,
  //        - Set order id
  const customerId =
    formData['customerId'] ||
    (formData.customer && formData.customer.id) ||
    state.loggedIn.role.customerId;

  const orderId = formData['id'] || false;

  // Address book company name is different from shipping address company name
  const shipFrom = formData['shipFrom'] || {};
  const { consigneeName: shipFromCompany = '' } = shipFrom;

  const shipTo = formData['shipTo'] || {};
  const {
    consigneeName: shipToCompany = '',
    distributionListId = false,
  } = shipTo;

  const distributionList = distributionListId
    ? { distributionGroupId: distributionListId, shipTo: null }
    : {};
  const formDataToSave = Object.assign({}, formData, {
    customerId,
    customer: null,
    shipFrom: Object.assign({}, shipFrom, {
      company: shipFromCompany,
      companyName: shipFromCompany,
    }),
    shipTo: Object.assign({}, shipTo, {
      company: shipToCompany,
      companyName: shipToCompany,
    }),
    ...distributionList,
  });

  delete formDataToSave['orderStatus'];
  delete formDataToSave['selectedQuote'];
  delete formDataToSave['charges'];

  if (orderId) {
    dispatch(
      update(
        'draftOrder',
        orderId,
        formDataToSave,
        `order/${orderId}`,
        'save',
        'PUT',
        data => {
          dispatch({
            type: 'FORM_FORCE_FIELD_UPDATE',
            payload: {
              form: formName,
              field: 'pallets',
              value: data.pallets,
            },
          });
          dispatch({
            type: 'FORM_FORCE_FIELD_UPDATE',
            payload: {
              form: formName,
              field: 'packages',
              value: data.packages,
            },
          });
          dispatch(
            updateTab(formName, {
              orderId: data.id,
              title: `Quote: ${data.id}`,
            }),
          );

          callback(data);
        },
      ),
    ).then(apiPayload => {
      if (apiPayload.error) {
        dispatch(
          addNotification({
            message: 'Shipment data failed to save!',
            status: 'error',
          }),
        );
      } else {
        dispatch(
          addNotification({
            message: 'Shipment data saved!',
            status: 'success',
          }),
        );
      }
    });
  } else {
    dispatch(
      save('draftOrder', formDataToSave, 'order', 'save', 'POST', data => {
        //don't do anything if there's an error
        if (!data.id) return;
        orderFormUpdates(dispatch, formName, data);
        callback(data);
      }),
    ).then(apiPayload => {
      if (apiPayload.error) {
        dispatch(
          addNotification({
            message: 'Shipment data failed to save!',
            status: 'error',
          }),
        );
      } else {
        dispatch(
          addNotification({
            message: 'Shipment data saved!',
            status: 'success',
          }),
        );
      }
    });
  }
};

/**
 * This doesn't work yet... needs backend endpoint
 */
export const deleteOrder = (orderId, callback = () => {}) => (
  dispatch,
  getState,
) => {
  const state = getState();
  const orderData = selectOneFromEntities(state, 'draftOrder', orderId);
  delete orderData['orderStatus'];
  dispatch(
    update(
      'draftOrder',
      orderId,
      Object.assign({}, orderData, { statusId: '999' }),
      `order/delete/${orderId}`,
      'save',
      'PUT',
      data => callback(data),
    ),
  );
};

export const loadOrderLogs = orderId => {
  return getAllOptions({
    entity: 'loggedEvents',
    schema: OrderLogSchema,
    itemsPerPage: 1000,
    overrideUrl: `/api/order/${orderId}/status-messages`,
  });
};

export const loadPackageTypes = () => (dispatch, getState) => {
  const { entities } = getState();

  if (entities && entities.packageType) {
    return new Promise(resolve => resolve());
  } else {
    const pageToLoad = 1;
    const itemsPerPage = 1000;
    return dispatch(
      getAll(
        'packageType',
        PackageTypeSchema,
        pageToLoad,
        itemsPerPage,
        {},
        {},
        'packageTypes',
        'packageTypes',
      ),
    );
  }
};

export const loadOrderStatuses = () => (dispatch, getState) => {
  const { entities } = getState();

  if (entities && entities.orderStatus) {
    return new Promise(resolve => resolve());
  } else {
    const pageToLoad = 1;
    const itemsPerPage = 1000;
    return dispatch(
      getAll(
        'orderStatus',
        OrderStatusSchema,
        pageToLoad,
        itemsPerPage,
        {},
        {},
        'orderStatuses',
        'orderStatuses',
      ),
    );
  }
};

export const loadPackageTemplates = () => (dispatch, getState) => {
  const { packageTemplate = false } = getState().entities;
  const pageToLoad = 1, itemsPerPage = 9999, filters = {}, sortOrder = {};
  if (!packageTemplate) {
    dispatch(
      getAll(
        'packageTemplate',
        PackageTemplateSchema,
        pageToLoad,
        itemsPerPage,
        filters,
        sortOrder,
        'package_templates',
      ),
    );
  }
};

export const loadPalletTypes = () => ({
  [CALL_API]: {
    credentials: 'include',
    endpoint: `/api/order/pallet_types`,
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    types: [
      { type: 'LIST_REQUEST' },
      {
        type: 'LIST_SUCCESS',
        meta: { list: 'palletTypes' },
        payload: (action, state, response) => {
          return response.json().then(json => json);
        },
      },
      {
        type: 'LIST_FAILURE',
      },
    ],
  },
});

export const GET_PALLET_TEMPLATES_REQUEST = 'GET_PALLET_TEMPLATES_REQUEST';
export const GET_PALLET_TEMPLATES_SUCCESS = 'GET_PALLET_TEMPLATES_SUCCESS';
export const GET_PALLET_TEMPLATES_FAILURE = 'GET_PALLET_TEMPLATES_FAILURE';

export const loadPalletTemplates = customer => (dispatch, getState) => {
  const customerId = customer
    ? customer.id
    : getState().loggedIn.role.customerId;

  return dispatch({
    [CALL_API]: {
      credentials: 'include',
      endpoint: customerId
        ? `/api/order/pallet_templates?customer_id=${customerId}`
        : `/api/order/pallet_templates`,
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      types: [
        { type: GET_PALLET_TEMPLATES_REQUEST, meta: { customerId } },
        {
          type: GET_PALLET_TEMPLATES_SUCCESS,
          meta: { customerId },
          payload: (action, state, response) => {
            return response.json().then(json => ({
              result: json,
            }));
          },
        },
        {
          type: GET_PALLET_TEMPLATES_FAILURE,
          meta: { customerId },
        },
      ],
    },
  });
};

export const SAVE_PALLET_TEMPLATES_REQUEST = 'SAVE_PALLET_TEMPLATES_REQUEST';
export const SAVE_PALLET_TEMPLATES_SUCCESS = 'SAVE_PALLET_TEMPLATES_SUCCESS';
export const SAVE_PALLET_TEMPLATES_FAILURE = 'SAVE_PALLET_TEMPLATES_FAILURE';

export const savePalletTemplates = (customer, data = {}) => (
  dispatch,
  getState,
) => {
  const customerId = customer
    ? customer.id
    : getState().loggedIn.role.customerId;
  const { palletType, ...templateData } = data;

  return dispatch({
    [CALL_API]: {
      credentials: 'include',
      endpoint: customerId
        ? `/api/order/pallet_templates?customer_id=${customerId}`
        : `/api/order/pallet_templates`,
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(templateData),
      types: [
        { type: SAVE_PALLET_TEMPLATES_REQUEST, meta: { customerId } },
        {
          type: SAVE_PALLET_TEMPLATES_SUCCESS,
          meta: { customerId },
          payload: (action, state, response) => {
            return response.json().then(json => ({
              result: json,
            }));
          },
        },
        {
          type: SAVE_PALLET_TEMPLATES_FAILURE,
          meta: { customerId },
        },
      ],
    },
  });
};

export const GET_ORDER_RATES_REQUEST = 'GET_ORDER_RATES_REQUEST';
export const GET_ORDER_RATES_SUCCESS = 'GET_ORDER_RATES_SUCCESS';
export const GET_ORDER_RATES_FAILURE = 'GET_ORDER_RATES_FAILURE';

export const getOrderRates = data => {
  //check if there's order form id
  if (!data.id) {
    return { type: 'ERROR', payload: { message: 'Need Order Form ID' } };
  }

  return {
    [CALL_API]: {
      credentials: 'include',
      endpoint: '/api/carrier_services',
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      types: [
        { type: GET_ORDER_RATES_REQUEST, meta: { form: data.id } },
        {
          type: GET_ORDER_RATES_SUCCESS,
          meta: { orderForm: data.id, form: data.id },
          payload: (action, state, res) => {
            const contentType = res.headers.get('Content-Type');

            if (contentType && ~contentType.indexOf('json')) {
              return res.json().then(json => {
                return camelizeKeys(normalize(json, arrayOf(RatesSchema)));
              });
            }
          },
        },
        { type: GET_ORDER_RATES_FAILURE },
      ],
    },
  };
};

//NOTE:	Might be better to do api call directly intead of using api.getAll and
//		save it to it's own orders slice in the redux state or create another
//		function in api action to handle response and not go through the
//		entities and paginate reducer
// TODO: Refactor this code...
export const loadAccessorialServices = (type = false) => {
  const pageToLoad = 1,
    itemsPerPage = 9999,
    filters = {},
    sortOrder = { sortBy: 'name' };
  return (dispatch, getState) => {
    const { accessorialServices = false } = getState().entities;

    if (!accessorialServices) {
      dispatch(
        getAll(
          'accessorial_services',
          AccesorialServicesSchema,
          pageToLoad,
          itemsPerPage,
          filters,
          sortOrder,
          type ? `search/findByType?type=${type}` : false,
        ),
      );
    }
  };
  //return getAll('accessorial_services', AccesorialServicesSchema, pageToLoad, itemsPerPage, filters, sortOrder);
};

export const loadCustomerMarkup = () => {
  fetch('/api/markup', { credentials: 'include' })
    .then(response => response.json())
    .then(json => {
      //console.log('json...');
      console.log(json);
    })
    .catch(error => {
      console.log('error...');
      console.log(error);
    });

  return {
    type: 'LOAD_CUSTOMER_MARKUP',
  };
};

export const UPDATE_DISTRIBUTION = 'UPDATE_DISTRIBUTION';

export const updateDistribution = (form, id) => {
  return { type: UPDATE_DISTRIBUTION, meta: { form }, payload: { id } };
};

export const uploadDistributionListFile = (form, fileInput) => {
  const formData = new FormData();
  formData.append('file', fileInput.files[0]);

  return (dispatch, getState) => {
    dispatch({ type: 'UPLOAD_SUBMIT', meta: { form } });

    let hasError = false;

    fetch('/api/order/upload-distribution-list', {
      method: 'POST',
      credentials: 'include',
      body: formData,
    })
      .then(res => {
        if (res.ok) return res.json();
        else {
          hasError = true;
          return res.json();
        }
      })
      .then(json => {
        console.log(json);
        if (!hasError) {
          dispatch({
            type: 'UPLOAD_SUCCESS',
            meta: { form },
            payload: { ...json },
          });
          dispatch(change(form, 'shipTo.distributionListId', json.id));
        } else {
          dispatch({
            type: 'UPLOAD_FAILURE',
            meta: { form },
            payload: { ...json },
            error: true,
          });
        }
      })
      .catch(err => {
        console.error('Error upoading distribution list');
        dispatch({
          type: 'UPLOAD_FAILURE',
          meta: { form },
          payload: { ...err },
          error: true,
        });
      });
  };
};

export const loadCarriers = () => {
  return getAll(
    'carrier',
    CarrierSchema,
    1,
    9999,
    {},
    { sortBy: 'name', order: 'ASC' },
  );
};

export const loadServices = () => {
  return getAll('service', ServiceSchema, 1, 9999, {}, {}, false, 'services');
};

export const saveOrderLog = (orderId, values, callbackAction = false) => {
  return save(
    'orderLog',
    values,
    `order/${orderId}/update`,
    'save',
    'PUT',
    callbackAction,
  );
};

export const loadCarrierList = () => dispatch => {
  return dispatch({
    [CALL_API]: {
      credentials: 'include',
      endpoint: '/api/carrier-dropdown',
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      types: [
        'LIST_REQUEST',
        {
          type: 'LIST_SUCCESS',
          meta: { list: 'carriers' },
          payload: (action, state, response) => {
            return response.json(json => ({ response: json }));
          },
        },
        'LIST_FAILURE',
      ],
    },
  });
};

export const uploadPODFile = ({
  orderId,
  form = '',
  fileDescription,
  fileInput,
  callback,
}) => dispatch => {
  const formData = new window.FormData();
  formData.append('file', fileInput.files[0]);
  formData.append('description', fileDescription);

  dispatch({ type: 'POD_UPLOAD_SUBMIT', meta: { form } });

  fetch(`/api/order/${orderId}/upload-pod`, {
    method: 'POST',
    credentials: 'include',
    body: formData,
  })
    .then(response => response.json())
    .then(json => {
      dispatch({
        type: 'POD_UPLOAD_SUCCESS',
        meta: { form },
        payload: { ...json },
      });
      callback(json);
      //console.log(json);
    })
    .catch(error => {
      callback({ error: true, message: 'failed to upload' });
    });
};

export const loadOrderCustomers = orderId => ({
  [CALL_API]: {
    credentials: 'include',
    endpoint: '/api/order/customer-users/' + orderId,
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    types: [
      'ORDER_USERS_REQUEST',
      {
        type: 'ORDER_USERS_SUCCESS',
        meta: { orderId },
        payload: (action, state, response) => {
          return response.json().then(json => ({
            result: json,
          }));
        },
      },
      'ORDER_USERS_FAILURE',
    ],
  },
});

export const addOrderCharge = (orderId, chargeData, callback) => dispatch => {
  dispatch(save('charge', chargeData, `order/${orderId}/charge`)).then(
    callback,
  );
};

export const editOrderCharge = (chargeId, chargeData, callback) => dispatch => {
  dispatch(
    update('charge', chargeId, chargeData, `order/charge/${chargeId}`),
  ).then(callback);
};

export const deleteOrderCharge = (chargeId, callback) => dispatch => {
  dispatch(
    update(
      'charge',
      chargeId,
      {},
      `order/charge/${chargeId}`,
      'delete',
      'DELETE',
    ),
  ).then(callback);
};

export const reconcileAllCharges = (orderId, callback) => dispatch => {
  dispatch(
    update('order', orderId, {}, `order/mark-reconciled/${orderId}`),
  ).then(callback);
};

export const disputeCharge = (
  chargeId,
  disputeChargeData,
  callback,
) => dispatch => {
  dispatch(
    save('dispute', disputeChargeData, `order/charge/${chargeId}/dispute`),
  ).then(callback);
};

export const replyDisputedCharge = (
  orderId,
  disputeReplyData,
  callback,
) => dispatch => {
  dispatch(
    save('dispute', disputeReplyData, `order/${orderId}/respond-to-dispute`),
  ).then(callback);
};

export const submitClaim = (orderId, claimData, callback) => dispatch => {
  dispatch(save('claim', claimData, `order/${orderId}/submit-claim`)).then(
    callback,
  );
};

export const updateClaim = (orderId, claimData, callback) => dispatch =>
  dispatch(
    update('order', orderId, claimData, `order/${orderId}/update-claim`),
  ).then(callback);

export const uploadOrderDocument = ({
  orderId,
  form = '',
  fileInput,
  fileDescription,
  callback,
}) => dispatch => {
  const formData = new window.FormData();
  formData.append('file', fileInput.files[0]);
  formData.append('description', fileDescription);

  dispatch({ type: 'DOCUMENT_UPLOAD_SUBMIT', meta: { form } });

  //console.log(formData);
  fetch(`/api/order/${orderId}/upload`, {
    method: 'POST',
    credentials: 'include',
    body: formData,
  })
    .then(response => {
      if (response.ok) {
        return response.json();
      }

      throw new Error('Upload failed');
    })
    .then(json => {
      dispatch({
        type: 'DOCUMENT_UPLOAD_SUCCESS',
        meta: { form },
        payload: { ...json },
      });
      callback(json);
    })
    .catch(error => {
      console.debug('UPLOAD CATCH ERROR', error);
      callback({ error: true });
    });
};

export const deleteOrderDocument = (fileId, callback) => dispatch => {
  dispatch(
    update('file', fileId, {}, `order/file/${fileId}`, 'delete', 'DELETE'),
  ).then(callback);
};

export const markOrderAsInTransit = (
  orderId,
  orderData,
  callback,
) => dispatch => {
  dispatch(
    save('order', orderData, `order/${orderId}/mark-as-in-transit`),
  ).then(callback);
};

export const markOrderAsDelivered = (
  orderId,
  orderData,
  callback,
) => dispatch => {
  dispatch(save('order', orderData, `order/${orderId}/mark-as-delivered`)).then(
    callback,
  );
};

export const clearFormErrors = form => (dispatch, getState) =>
  dispatch({ type: 'CLEAR_FORM_ERRORS', formName: form });

export const SAVE_PALLET_TEMPLATE_REQUEST = 'SAVE_PALLET_TEMPLATE_REQUEST';
export const SAVE_PALLET_TEMPLATE_SUCCESS = 'SAVE_PALLET_TEMPLATE_SUCCESS';
export const SAVE_PALLET_TEMPLATE_FAILURE = 'SAVE_PALLET_TEMPLATE_FAILURE';
export const SAVE_PALLET_TEMPLATE_CLEAR = 'SAVE_PALLET_TEMPLATE_CLEAR';

export const savePalletTemplate = (formName, values) => dispatch => {
  return dispatch({
    [CALL_API]: {
      credentials: 'include',
      endpoint: `/api/order/pallet_templates`,
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(values),
      types: [
        {
          type: SAVE_PALLET_TEMPLATE_REQUEST,
          meta: { formName },
        },
        {
          type: SAVE_PALLET_TEMPLATE_SUCCESS,
          meta: { formName },
          payload: (action, state, response) => {
            return response.json(json => ({ response: json }));
          },
        },
        {
          type: SAVE_PALLET_TEMPLATE_FAILURE,
          meta: { formName },
        },
      ],
    },
  });
};
