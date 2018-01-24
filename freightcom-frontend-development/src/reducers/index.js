import has from 'lodash/has';

import { reducer as form, getFormValues } from 'redux-form';
//import { routerReducer as routing } from 'react-router-redux';
import { combineReducers } from 'redux';
import { reducer as notificationsReducer } from 'reapop';

import paginate from './paginate';
import autoSuggestReducer from './autoSuggest';
import tabs from './tabs';
import orders from './orders';
import changedObjects from './changed-objects';
import { loadStatus } from './load-status';
import { shipmentForm } from './shipment-form';

import {
  SUBMIT_LOGOUT_SUCCESS,
  SUBMIT_LOGIN_SUCCESS,
  SUBMIT_LOGIN_FAILURE,
  CHECK_LOGGED_IN_SUCCESS,
  CHECK_LOGGED_IN_FAILURE,
  USER_INFO_SUCCESS,
  NOT_LOGGED_IN,
  LOGGED_IN,
  PRE_LOGGED_IN,
  CLEAR_LOGIN_MESSAGE,
  RESET_PASSWORD_FAILURE,
  RESET_PASSWORD_SUCCESS,
  SELECT_ROLE_REQUEST,
  SELECT_ROLE_SUCCESS,
  SELECT_ROLE_FAILURE,
  GET_ROLE_REQUEST,
  GET_ROLE_SUCCESS,
  GET_ROLE_FAILURE,
  STORE_URL,
  CLEAR_STORED_URL,
} from '../actions/loginSubmit.js';

import {
  API_GET_ALL_REQUEST,
  API_GET_ALL_SUCCESS,
  API_GET_ALL_FAILURE,
  API_SEARCH_REQUEST,
  API_SEARCH_SUCCESS,
  API_SEARCH_FAILURE,
  API_GET_ONE_SUCCESS,
  API_GET_ONE_FAILURE,
  API_GET_ONE_REQUEST,
} from '../actions/api';

import { FORM_MULTIPLE_REMOVE_FIELDARRAY } from '../actions/form';

import { SHIPMENT_TYPES } from '../constants';

// Updates logged in status
const loggedIn = (
  state = { status: PRE_LOGGED_IN, message: '', reset_message: '' },
  action,
) => {
  if (action.type === STORE_URL) {
    return { ...state, storedUrl: action.url };
  } else if (action.type === CLEAR_STORED_URL) {
    return { ...state, storedUrl: null };
  } else if (
    action.type === SUBMIT_LOGIN_SUCCESS ||
    action.type === CHECK_LOGGED_IN_SUCCESS
  ) {
    return { ...state, status: LOGGED_IN, message: '' };
  } else if (action.type === CHECK_LOGGED_IN_FAILURE) {
    return { ...state, status: NOT_LOGGED_IN, message: '' };
  } else if (action.type === SUBMIT_LOGOUT_SUCCESS) {
    return { ...state, status: NOT_LOGGED_IN, message: 'Logged out' };
  } else if (action.type === SUBMIT_LOGIN_FAILURE) {
    return {
      ...state,
      status: NOT_LOGGED_IN,
      message: 'Invalid user name or password',
    };
  } else if (action.type === RESET_PASSWORD_SUCCESS) {
    return Object.assign({}, state, action.payload);
  } else if (action.type === RESET_PASSWORD_FAILURE) {
    return Object.assign({}, state, action.payload);
  } else if (action.type === CLEAR_LOGIN_MESSAGE) {
    return Object.assign({}, state, action.payload);
  } else if (
    action.type === SELECT_ROLE_REQUEST ||
    action.type === GET_ROLE_REQUEST
  ) {
    return {
      ...state,
      requesting_role: true,
      customer_loaded: false,
      role: false,
      customerLoadFailed: false,
    };
  } else if (
    action.type === SELECT_ROLE_SUCCESS ||
    action.type === GET_ROLE_SUCCESS
  ) {
    return {
      ...state,
      role: action.payload,
      requesting_role: false,
    };
  } else if (
    action.type === SELECT_ROLE_FAILURE ||
    action.type === GET_ROLE_FAILURE
  ) {
    return Object.assign({}, state, {
      role: null,
      message: action.payload.message,
      requesting_role: false,
    });
  } else if (
    action.type === API_GET_ONE_REQUEST &&
    action.meta.loginLoadCustomer
  ) {
    return { ...state, customerLoading: true };
  } else if (
    action.type === API_GET_ONE_SUCCESS &&
    action.meta.loginLoadCustomer
  ) {
    return {
      ...state,
      customer_loaded: true,
      customerLoading: false,
    };
  } else if (
    action.type === API_GET_ONE_FAILURE &&
    action.meta.loginLoadCustomer
  ) {
    return { ...state, customerLoadFailed: true, customerLoading: false };
  } else if (
    action.payload &&
    action.payload.status &&
    action.payload.status === 401 &&
    action.payload.response &&
    action.payload.response.event === 'session-expired'
  ) {
    return { status: NOT_LOGGED_IN, message: 'Session timed out' };
  } else {
    return state;
  }
};

// Updates logged in user
const loggedInUser = (state = { id: null }, action) => {
  if (
    action.type === USER_INFO_SUCCESS ||
    action.type === CHECK_LOGGED_IN_SUCCESS ||
    action.type === 'PREFERENCE_UPDATE_SUCCESS'
  ) {
    return { ...state, ...(action.payload ? action.payload : {}) };
  } else if (
    action.type === 'API_CHECK_ADDRESS_BOOK_SUCCESS' &&
    !action.meta.customer
  ) {
    return {
      ...state,
      hasAddressBook: action.payload.hasAddressBook ? true : false,
    };
  } else {
    return state;
  }
};

const mergeEntityMap = (existing, values) => {
  return {
    ...existing,
    ...Object.entries(values)
      .map(([key, value]) => [key, { ...(existing[key] || {}), ...value }])
      .reduce(
        (accumulate, pair) => ({
          ...accumulate,
          [pair[0]]: pair[1],
        }),
        {},
      ),
  };
};

/**
 * Immutably merge a payload's entities into existing entities in state
 */
const mergeEntities = (state, entities) => {
  const existingEntities = state || {};

  return {
    ...state,
    ...Object.entries(entities)
      .map(([key, values]) => [
        key,
        {
          ...(existingEntities[key] || {}),
          ...mergeEntityMap(existingEntities[key] || {}, values),
        },
      ])
      .reduce(
        (accumulate, pair) => ({
          ...accumulate,
          [pair[0]]: pair[1],
        }),
        {},
      ),
  };
};

// Updates an entity cache in response to any action with response.entities.
const entities = (
  state = {
    customer: {},
    addressBook: {},
    user: {},
    order: {},
    draftOrder: {},
    service: {},
    carrier: {},
  },
  action,
) => {
  if (action.type === 'REMOVE_ITEM_FROM_ENTITY') {
    const entity = Object.assign(
      {},
      state[action.payload.entity][action.payload.id],
    );
    delete entity[action.payload.fieldToDelete];
    return {
      ...state,
      [action.payload.entity]: {
        ...state[action.payload.entity],
        [action.payload.id]: entity,
      },
    };
  }

  if (action.type === 'REMOVE_ONE_FROM_ENTITIES') {
    const entities = Object.assign({}, state[action.payload.entity]);
    delete entities[action.payload.id];
    return {
      ...state,
      [action.payload.entity]: entities,
    };
  }

  if (
    action.type === 'API_CHECK_ADDRESS_BOOK_SUCCESS' &&
    action.meta.customer
  ) {
    return {
      ...state,
      customer: {
        ...state.customer,
        [action.meta.customer.id]: {
          ...state.customer[action.meta.customer.id],
          ...action.meta.customer,
          hasAddressBook: action.payload.hasAddressBook ? true : false,
        },
      },
    };
  }

  if (has(action, 'payload.entities')) {
    return mergeEntities(state, action.payload.entities);
  } else if (has(action, 'payload.entity')) {
    return {
      ...state,
      [action.meta.entity]: {
        ...state[action.meta.entity],
        [action.payload.id]: action.payload.entity,
      },
    };
  }

  return state;
};

// Updates the pagination data for different actions.
const pagination = paginate({
  mapActionToKey: action => action.meta.entity,
  types: [API_GET_ALL_REQUEST, API_GET_ALL_SUCCESS, API_GET_ALL_FAILURE],
});

const autoSuggest = autoSuggestReducer({
  mapActionToKey: action => action.meta.id,
  types: [API_SEARCH_REQUEST, API_SEARCH_SUCCESS, API_SEARCH_FAILURE],
});

const formReducer = (state = {}, action) => {
  switch (action.type) {
    case FORM_MULTIPLE_REMOVE_FIELDARRAY:
      //Removes multiple items from FieldArray
      const { [action.payload.form]: { values = {} } = {} } = state;
      const field = values[action.payload.field] || [];
      return {
        ...state,
        [action.payload.form]: {
          ...state[action.payload.form],
          values: {
            ...values,
            [action.payload.field]: field.filter(
              (item, index) => action.payload.indexes.indexOf(index) === -1,
            ),
          },
        },
      };
    case 'FORM_FORCE_FIELD_UPDATE':
      const {
        form: formName,
        field: fieldName,
        value: fieldValue,
      } = action.payload;
      const { values: formValues = {} } = state[formName] || {};

      return {
        ...state,
        [formName]: {
          ...state[formName],
          values: {
            ...formValues,
            [fieldName]: fieldValue,
          },
        },
      };
    default:
      return form(state, action);
  }
};

const lists = (state = { currencies: [] }, action) => {
  if (action.type === 'LIST_SUCCESS') {
    return { ...state, [action.meta.list]: action.payload };
  }

  return state;
};

const cheques = (state = {}, action) => {
  if (action.type === 'PRINT_CHEQUES_SUCCESS') {
    return { ...state, [action.meta.chequeType]: action.payload };
  }

  return state;
};

const appReducers = combineReducers({
  notifications: notificationsReducer(),
  loggedIn,
  loggedInUser,
  form: formReducer,
  entities,
  pagination,
  autoSuggest,
  tabs,
  orders,
  changedObjects,
  loadStatus,
  lists,
  shipmentForm,
  cheques,
});

const rootReducer = (state, action) => {
  if (action.type === SUBMIT_LOGOUT_SUCCESS) {
    return appReducers(undefined, action);
  } else if (action.type === SELECT_ROLE_REQUEST) {
    const { loggedIn, loggedInUser } = state;
    return appReducers(
      { loggedInUser, loggedIn: { ...loggedIn, role: {} } },
      action,
    );
  }

  return appReducers(state, action);
};

export default rootReducer;

/******************* ALL SELECTORS ********************************************/
/**
 * Selectors for Entity-Pagination
 *
 * Use this selector for displaying items that are retreived from the server
 * with `getAll` method
 *
 * How to use:
 * const mapStateToProps = (state, ownProps) => {
 *   return getPageItems(state, 'entity_name');
 * }
 *
 * If you need other properties from redux store, you can use the spread
 * const mapStateToProps = (state, ownProps) => {
 *   return {
 *      something: state.something,
 *      ...getPageItems(state, 'entity_name'),
 *   };
 * }
 *
 * props that will be available to the component
 * - this.props.items
 * - this.props.currentPage
 * - this.props.numberOfPages
 * - this.props.isFetching
 * - this.props.sortOrder
 *
 * @param {object} state Redux state object
 * @param {string} entity Entity name from state.entities
 * @param {array} related related entities that will be merged to the item
 *        objects. Should be an array of object with `entity` and `key` property
 *        e.g. [{entity: 'shippingAddress', key: 'shipFrom'}]
 */
export const getPageItems = (state, entity, related = []) => {
  const {
    entities: { [entity]: entityItems = {} },
    pagination: {
      ['_' + entity]: {
        ids = [],
        currentPage = 1,
        isFetching = false,
        sortOrder = { sortBy: 'createdAt', order: 'DESC' },
        ...meta
      } = {},
    },
  } = state;

  const items = ids.map(id => {
    const item = entityItems[id];
    if (related.length === 0) {
      return item;
    }
    return Object.assign(
      {},
      item,
      related.reduce((relatedItems, relatedObj) => {
        const relatedItem =
          state.entities[relatedObj.entity][item[relatedObj.key]];
        return Object.assign(relatedItems, { [relatedObj.key]: relatedItem });
      }, {}),
    );
  });

  return {
    currentPage,
    isFetching,
    sortOrder,
    items,
    totalItems: meta.totalItems,
    numberOfPages: Math.ceil(
      parseInt(meta.totalItems, 10) / parseInt(meta.itemsPerPage, 10),
    ),
  };
};

export const getEntities = (state, entity) => {
  const { entities: { [entity]: entityItems = {} } } = state;

  return entityItems;
};

export const getPageIds = (state, entity) => {
  const { pagination: { ['_' + entity]: { ids = [] } = {} } } = state;

  return ids;
};

export const getPageEntities = (() => {
  const pageCache = {};

  const pageCacheLookup = (state, entity) => {
    if (!pageCache[String(entity)]) {
      pageCache[String(entity)] = {};
    }

    return pageCache[String(entity)];
  };

  return (state, entity) => {
    const entities = getEntities(state, entity);
    const pageIds = getPageIds(state, entity);
    const cacheBucket = pageCacheLookup(state, entity);

    if (
      cacheBucket.entities &&
      cacheBucket.entities === entities &&
      cacheBucket.pageIds === pageIds
    ) {
      return cacheBucket.values;
    } else {
      const values = pageIds.map(id => entities[id]);

      pageCache[String(entity)] = { entities, pageIds, values };

      return values;
    }
  };
})();

//NOTE: This can get some unexpected results since it's overwriting the `id`
//      attribute with the key if it uses a different one
export const selectAllFromEntities = (state, entity, filters = []) => {
  const { [entity]: entities = {} } = state.entities;

  if (filters.length > 0) {
    const filteredData = [];
    Object.keys(entities).forEach(id => {
      const obj = entities[id];
      if (
        filters.filter(i => String(obj[i.key]) === String(i.value)).length > 0
      ) {
        filteredData.push(obj);
      }
    });
    return filteredData;
  } else {
    return Object.keys(entities).map(id =>
      Object.assign({}, entities[id], { id }),
    );
  }
};

/**
 * Returns version from state
 */
export const selectEntity = (state, entity, entityId) => {
  const { [entity]: { [entityId]: item = {} } = {} } = state.entities || {};

  return item;
};

export const selectOneFromEntities = (
  state,
  entity,
  entityId,
  related = [],
) => {
  const { [entity]: { [entityId]: item = {} } = {} } = state.entities;

  if (related) {
    return Object.assign(
      {},
      item,
      related.reduce((relatedItems, relatedObj) => {
        const relatedItemKey = item[relatedObj.key] || false;
        if (relatedItemKey) {
          const relatedItem = state.entities[relatedObj.entity][
            relatedItemKey
          ] || {};
          return Object.assign(relatedItems, {
            [relatedObj.key]: relatedItem,
          });
        }
        return relatedItems;
      }, {}),
    );
  } else {
    return item;
  }
};

/**
 * @deprecated Use `getPageItems`
 */
export const getEntitiesPage = (state, entity) => {
  const {
    entities: { [entity]: items = {} },
    pagination: { ['_' + entity]: { ids = [] } = {} },
  } = state;
  return ids.map(id => items[id]);
};

export const selectUsers = state => {
  const {
    entities: { user = {} },
    pagination: { _customerStaff: { ids = [], currentPage = 1, ...meta } = {} },
  } = state;
  const numberOfPages = Math.ceil(
    parseInt(meta.totalItems, 10) / parseInt(meta.itemsPerPage, 10),
  );

  return {
    items: ids.map(id => user[id]),
    ...meta,
    numberOfPages,
    currentPage: parseInt(currentPage, 10),
  };
};

/**
 * @deprecated Use `getPageItems`
 */
export const selectAddressBook = state => {
  const {
    entities: { addressBook = {} },
    pagination: { _addressBook: { ids = [], currentPage = 1, ...meta } = {} },
  } = state;
  const numberOfPages = Math.ceil(
    parseInt(meta.totalItems, 10) / parseInt(meta.itemsPerPage, 10),
  );

  return {
    items: ids.map(id => addressBook[id]),
    ...meta,
    numberOfPages,
    currentPage: parseInt(currentPage, 10),
  };
};

/**
 * Selectors for login user and role
 */
export const getLoggedInUser = state => state.loggedInUser;

export const hasAddressBook = state => forCustomer => {
  const { entities: { customer = {} } = {} } = state;

  return forCustomer && forCustomer.id
    ? customer[forCustomer.id] && customer[forCustomer.id].hasAddressBook
    : state.loggedInUser && state.loggedInUser.hasAddressBook;
};

export const getLoggedInRole = state =>
  state.loggedIn ? state.loggedIn.role : null;

export const selectPackageTypes = state => {
  const { entities: { packageTypes = {} } } = state;
  return Object.keys(packageTypes).map(id => packageTypes[id]);
};

export const selectOrderStatuses = state => {
  const { entities: { packageTypes = {} } } = state;
  return Object.keys(packageTypes).map(id => packageTypes[id]);
};

export const selectOrderCountPerType = state => {
  const { stats: { workorder, ...otherStats } } = state.orders;
  // Replace `workorder` with `work-order`
  const orderStats = {
    'work-order': workorder,
    ...otherStats,
  };
  return orderStats;
};

export const selectActiveTab = (state, section = 'orders') => {
  if (section === 'orders') {
    const { activeTab, openTabs } = state.tabs;
    return openTabs.find(tab => tab.id === activeTab);
  }

  const { [section]: { activeTab, openTabs } = {} } = state.tabs;
  return openTabs.find(tab => tab.id === activeTab);
};

export const selectTabByOrderId = (state, orderId) => {
  const { openTabs = [] } = state.tabs;
  return openTabs.find(tab => tab.orderId === orderId);
};

export const loginStoredUrl = state => {
  const { storedUrl } = state.loggedIn ? state.loggedIn : {};
  return storedUrl;
};

export const loggedInCustomer = state => {
  const {
    entities: { customers = {} } = {},
    loggedIn: { role = {} } = {},
  } = state;

  return customers[role.customerId];
};

/**
 * SELECTORS IMPORTED FROM reducers/orders.js
 * TODO: Check for simplification and reuse of generic selectors
 */
export const selectDistributionList = (state, form) => {
  const {
    [form]: list = { id: '', status: 'Waiting' },
  } = state.orders.distributionList;

  return list;
};

// Orders Selectors
export const getOrdersSelector = state => {
  if (state.entities && state.pagination && state.pagination._order) {
    const {
      entities: { order = {}, customer = {}, shippingAddress = {} },
      pagination: { _order: { ids = [] } },
    } = state;

    const ordersArray = ids
      .map(id => order[id])
      .map(order =>
        Object.assign(
          order,
          { customer: customer[order.customer] },
          { shipTo: shippingAddress[order.shipTo] },
          { shipFrom: shippingAddress[order.shipFrom] },
        ),
      );

    return ordersArray;
  } else {
    return [];
  }
};

export const getAccessorialServices = (state, filter) => {
  const { entities: { accessorialServices = {} } } = state;

  return Object.keys(accessorialServices)
    .filter(
      id =>
        accessorialServices[id].type === filter ? filter.toLowerCase() : '',
    )
    .map(id => accessorialServices[id]);
};

export const getRatesSelector = (state, formName) => {
  const {
    entities: { rates = {} } = {},
    orders: { rates: { [formName]: { ids = [] } = {} } },
  } = state;
  return ids.map(id => rates[id]);
};

export const selectOrderRates = (state, formName) => {
  const {
    entities: { orderRate = {} } = {},
    orders: { rates: { [formName]: { ids = [] } = {} } },
  } = state;
  return ids.map(id => orderRate[id]);
};

export const selectOrderMessages = (state, formName) => {
  const { orders: { rates: { [formName]: { messages = [] } = {} } } } = state;
  return messages;
};

export const checkRoleIfCustomer = state => {
  const role = state.loggedIn ? state.loggedIn.role : {};
  const { roleName = false } = role;
  return roleName === 'CUSTOMER_ADMIN' || roleName === 'CUSTOMER_STAFF';
};

export const checkRoleIfCustomerAdmin = state => {
  const role = state.loggedIn ? state.loggedIn.role : {};
  const { roleName = false } = role;
  return roleName === 'CUSTOMER_ADMIN';
};

export const checkRoleIfCustomerStaff = state => {
  const role = state.loggedIn ? state.loggedIn.role : {};
  const { roleName = false } = role;
  return roleName === 'CUSTOMER_STAFF';
};

export const checkRoleIfFreightcomStaff = state => {
  const role = state.loggedIn ? state.loggedIn.role : {};
  const { roleName = false } = role;
  return roleName === 'FREIGHTCOM_STAFF';
};

export const checkRoleIfFreightcomAdmin = state => {
  const role = state.loggedIn ? state.loggedIn.role : {};
  const { roleName = false } = role;
  return roleName === 'ADMIN';
};

export const checkRoleIfFreightcom = state => {
  const role = state.loggedIn ? state.loggedIn.role : {};
  const { roleName = false } = role;
  return roleName === 'ADMIN' || roleName === 'FREIGHTCOM_STAFF';
};

export const checkRoleIfAgent = state => {
  const role = state.loggedIn ? state.loggedIn.role : {};
  const { roleName = false } = role;
  return roleName === 'AGENT';
};

export const checkRoleIfAdminOrAgent = state => {
  const role = state.loggedIn ? state.loggedIn.role : {};
  const { roleName = false } = role;
  return (
    roleName === 'ADMIN' ||
    roleName === 'FREIGHTCOM_STAFF' ||
    roleName === 'AGENT'
  );
};

export const checkIfRatesError = (state, orderFormName) => {
  const { [orderFormName]: { error = null } = {} } = state.orders.rates;
  return error;
};

export const checkIfRatesFetching = (state, orderFormName) => {
  const { [orderFormName]: { fetching = false } = {} } = state.orders.rates;
  return fetching;
};

export const checkIfRatesFetchingMore = (state, orderFormName) => {
  const { [orderFormName]: { fetchingMore = false } = {} } = state.orders.rates;
  return fetchingMore;
};

export const checkIfRatesDirty = (state, orderFormName) => {
  const { [orderFormName]: { dirty = false } = {} } = state.orders.rates;
  return dirty;
};

export const checkIfRatesSelected = (state, formName) => {
  const formData = getFormValues(formName)(state);
  const { carrierId = false } = formData || {};
  return Boolean(carrierId);
};

export const checkIfRatesLoaded = (state, formName) => {
  const { [formName]: { loaded = false } = {} } = state.orders.rates;
  return loaded;
};

export const checkIfReadyToGetRates = (state, formName) => {
  //Check form fields if they are all populated
  const formData = getFormValues(formName)(state);
  const isAdminOrAgent = checkRoleIfAdminOrAgent(state);

  if (!formData) {
    return false;
  }
  const {
    customerId = false,
    shipFrom = false,
    shipTo = false,
    shipDate = false,
    packageTypeName = false,
    pallets = false,
    packages = false,
    customer = {},
  } = formData;

  //Only check for customerId if it's admin or agent
  //customerId is populated on submit if customer admin/staff
  if (isAdminOrAgent) {
    if (!customerId && !customer.id) return false;
  }

  if (!shipFrom || !shipTo || !shipDate || !packageTypeName) {
    return false;
  }
  if (!shipFrom['postalCode']) {
    return false;
  }
  if (!shipTo['postalCode'] && !shipTo['distributionListId']) {
    return false;
  }
  if (
    packageTypeName === SHIPMENT_TYPES.LTL &&
    (!pallets ||
      pallets.length <= 0 ||
      !pallets[0]['length'] ||
      !pallets[0]['width'] ||
      !pallets[0]['height'] ||
      !pallets[0]['weight'])
  ) {
    return false;
  }
  if (
    packageTypeName === SHIPMENT_TYPES.PACKAGE &&
    (!packages ||
      packages.length <= 0 ||
      !packages[0]['length'] ||
      !packages[0]['width'] ||
      !packages[0]['height'] ||
      !packages[0]['weight'])
  ) {
    return false;
  }
  return true;
};

export const checkIfShippingAddressIsValid = (state, formName) => {
  const formData = getFormValues(formName)(state);
  if (!formData) {
    return false;
  }

  const { shipFrom = false, shipTo = false } = formData;

  if (!shipFrom || !shipTo) {
    return false;
  }

  const {
    consigneeName: shipFromCompanyName = false,
    address1: shipFromLine1 = false,
    city: shipFromCity = false,
    postalCode: shipFromPostalCode = false,
    province: shipFromProvince = false,
    country: shipFromCountry = false,
    contactName: shipFromContactName = false,
    phone: shipFromContactPhone = false,
  } = shipFrom;

  const {
    consigneeName: shipToCompanyName = false,
    address1: shipToLine1 = false,
    city: shipToCity = false,
    postalCode: shipToPostalCode = false,
    province: shipToProvince = false,
    country: shipToCountry = false,
    contactName: shipToContactName = false,
    phone: shipToContactPhone = false,
    distributionListId = false,
  } = shipTo;

  if (
    !!shipFromCompanyName &&
    !!shipFromLine1 &&
    !!shipFromCity &&
    !!shipFromPostalCode &&
    !!shipFromProvince &&
    !!shipFromCountry &&
    !!shipFromContactName &&
    !!shipFromContactPhone &&
    ((!!shipToCompanyName &&
      !!shipToLine1 &&
      !!shipToCity &&
      !!shipToPostalCode &&
      !!shipToProvince &&
      !!shipToCountry &&
      !!shipToContactName &&
      !!shipToContactPhone) ||
      distributionListId)
  ) {
    return true;
  }

  return false;
};

/**
 * Placeholder function for book order validation.
 */
export const checkIfReadyToBookOrder = (state, orderFormName) => {
  // const formData = getFormValues(orderFormName)(state);
  // const isAdminOrAgent = checkRoleIfAdminOrAgent(state);
  //
  // if (!formData) {
  //   return false;
  // }
  // const {
  //   packageTypeName,
  // } = formData;

  //for now return true
  return true;
};

export const selectCustomerTerm = state => {
  const isCustomer = checkRoleIfCustomer(state);

  if (isCustomer) {
    // eslint-disable-next-line
    const customer = selectLoggedInCustomer(state);
    return customer.autoInvoice;
  } else {
    return false;
  }
};

export const isRatesDirtySelector = (state, orderFormName) => {
  if (state.orders && state.orders.rates && state.orders.rates[orderFormName]) {
    const {
      orders: { rates: { [orderFormName]: { isDirty = true } = {} } },
    } = state;

    return isDirty;
  } else {
    return false;
  }
};

export const getSelectedCarrier = (state, orderFormName) => {
  return (
    state.form &&
    state.form[orderFormName] &&
    state.form[orderFormName].values &&
    state.form[orderFormName].values.carrierId
  );
};

export const selectShipmentTypeOptions = state => {
  if (state.entities && state.entities.packageTypes) {
    const { entities: { packageTypes = {} } } = state;

    return Object.keys(packageTypes).map(id => packageTypes[id]);
  } else {
    return [];
  }
};

export const selectOrderById = (state, orderId) => {
  if (state.entities && state.entities.orders) {
    const {
      entities: {
        orders: { [orderId]: order = {} } = {},
        customer = {},
        shippingAddress = {},
        orderStatuses = {},
      },
    } = state;

    const values =
      state.form && state.form[orderId] && state.form[orderId].values;

    if (Object.keys(order).length === 0 && order.constructor === Object) {
      return values;
    }

    return Object.assign(
      {},
      order,
      { customer: customer[order.customer] },
      { shipTo: shippingAddress[order.shipTo] },
      { shipFrom: shippingAddress[order.shipFrom] },
      { orderStatuses: orderStatuses[order.orderStatus] },
    );
  } else {
    return { customer: {}, shipTo: {}, shipFrom: {}, orderStatuses: null };
  }
};

export const selectStatusLogsByOrderId = (() => {
  let cache = {};
  let cachedState = null;

  const memoize = (state, key, fn) => {
    if (cachedState !== state) {
      cachedState = state;
      cache = { [String(key)]: fn() };
    } else if (!cache[String(key)]) {
      cache[String(key)] = fn();
    }

    return cache[String(key)];
  };

  return (state, orderId) => {
    return memoize(state.entities.orderLog, orderId, () =>
      selectAllFromEntities(state, 'orderLog').filter(
        log => String(log.entityId) === String(orderId),
      ),
    );
  };
})();

export const selectLoggedInCustomer = state => {
  const customerId = state.loggedIn.role.customerId || '';
  return selectOneFromEntities(state, 'customer', customerId);
};

export const selectSubAgents = (state, agentId) => {
  const agents = selectAllFromEntities(state, 'subAgent');
  return agents.filter(
    agent => String(agent.parentSalesAgentId) === String(agentId),
  );
};

export const checkIfUserIsAllowedToOrder = state => {
  //check role of logged in user
  const role = state.loggedIn.role.roleName;
  if (role === 'CUSTOMER_ADMIN' || role === 'CUSTOMER_STAFF') {
    return selectLoggedInCustomer(state).allowNewOrders;
  }
  return true;
};

export const checkIfUserCanManageClaims = state => {
  const role = state.loggedIn.role;
  if (role.roleName === 'ADMIN') return true;
  const { canManageClaims = 'false' } = role;
  if (role.roleName === 'FREIGHTCOM_STAFF')
    return canManageClaims.toString() === 'true';
  return false;
};

export const orderUserOptions = (state, orderId) => {
  const { orders: { users = {} } = {} } = state;

  return users[orderId] || [];
};

export const orderPalletTemplates = (state, customer) => {
  const {
    loggedIn: { role: { customerId: loggedInCustomerId } = {} } = {},
  } = state;
  const {
    orders: {
      palletTemplates: {
        [(customer ? customer.id : false) ||
          loggedInCustomerId]: palletTemplates = [],
      },
    },
  } = state;

  return palletTemplates;
};

export const checkIfUserCanManageDisputes = state => {
  const role = state.loggedIn.role;

  if (role.roleName === 'ADMIN') {
    return true;
  }

  const { canManageDisputes = 'false' } = role;

  if (role.roleName === 'FREIGHTCOM_STAFF') {
    return canManageDisputes.toString() === 'true';
  }

  return false;
};

export const getSystemProperty = (() => {
  let cachedProperties = null;
  let cache = {};

  return (state, name) => {
    const { entities: { systemProperties = {} } = {} } = state;

    if (cachedProperties !== systemProperties) {
      cachedProperties = systemProperties;
      cache = {};
    }

    if (!cache.hasOwnProperty(name)) {
      const match = Object.entries(systemProperties).find(
        ([key, entry]) => entry.name === name,
      );
      cache[name] = match ? match[1].data : '';
    }

    return cache[name];
  };
})();

export const selectCurrencies = state => {
  const { lists: { currencies = [] } = {} } = state;

  return currencies;
};

export const selectCarrierList = state => {
  const { lists: { carriers = [] } = {} } = state;

  return carriers;
};

export const selectPalletTypes = state => {
  const { lists: { palletTypes = [] } = {} } = state;

  return palletTypes;
};

export const selectCheque = (state, key) => {
  const { cheques: { [key]: value } = {} } = state;

  return value;
};
