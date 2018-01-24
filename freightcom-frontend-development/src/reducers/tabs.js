import {
  ADD_TAB,
  REMOVE_TAB,
  UPDATE_TAB,
  ACTIVATE_TAB,
  UPDATE_TAB_TYPE,
  UPDATE_TAB_DATA,
} from '../actions/tabs';

const activeTab = (state, action) => {
  switch (action.type) {
    case ACTIVATE_TAB:
      if (action.payload.id) {
        return action.payload.id;
      }
      return 'tab-content-search';
    default:
      return state;
  }
};

const possiblyChangeTab = (activeTab, action) => {
  if (action.type === REMOVE_TAB) {
    if (action.payload.activeTab === action.payload.id) {
      // Can't use deleted tab
      const filtered = (action.payload.previousTab || [])
        .filter(id => id !== action.payload.id);

      if (filtered.length > 0) {
        return filtered[filtered.length - 1];
      } else {
        return 'tab-content-search';
      }
    } else {
      return activeTab;
    }
  } else {
    return activeTab;
  }
};

const possiblyChangePreviousTab = (previousTab, action) => {
  if (action.type === REMOVE_TAB) {
    // Filter out the deleted tab from the list
    return (previousTab || []).filter(id => id !== action.payload.id);
  } else {
    return previousTab;
  }
};

const openTabs = (state, action) => {
  switch (action.type) {
    case ADD_TAB:
      const { id, title, orderId = false } = action.payload.tab;
      if (typeof id !== 'string' || id.length < 1) {
        return state;
      }
      if (typeof title !== 'string' || title.length < 1) {
        return state;
      }
      if (state.filter(tab => tab.id === id).length > 0) {
        return state;
      }
      if (state.filter(tab => orderId && tab.orderId === orderId).length > 0) {
        return state;
      }
      return [...state, action.payload.tab];
    case REMOVE_TAB:
      return state.filter(tab => tab.id !== action.payload.id);
    case UPDATE_TAB:
      return state.map(tab => {
        if (tab.id === action.payload.id) {
          return { ...tab, ...action.payload.data, id: action.payload.id };
        }
        return tab;
      });
    case UPDATE_TAB_TYPE:
      return state.map(tab => {
        if (tab.id === action.payload.id) {
          return {
            ...tab,
            title: 'Confirm Shipment',
            type: action.payload.value,
          };
        }
        return tab;
      });
    case UPDATE_TAB_DATA:
      return state.map(tab => {
        if (tab.id === action.payload.id) {
          tab[action.payload.key] = action.payload.value;
        }
        return tab;
      });
    default:
      return state;
  }
};

const invoiceTabReducer = (state, action) => {
  switch (action.type) {
    case 'INVOICE_ACTIVATE_TAB':
      return {
        ...state,
        activeTab: action.payload.id,
      };
    case 'INVOICE_ADD_TAB':
      if (state.openTabs.filter(tab => tab === action.payload.id).length > 0) {
        return state;
      }
      return {
        ...state,
        openTabs: [...state.openTabs, action.payload.id],
      };
    case 'INVOICE_REMOVE_TAB':
      return {
        ...state,
        openTabs: state.openTabs.filter(id => id !== action.payload.id),
      };
    default:
      return state;
  }
};

const payableTabReducer = (state, action) => {
  switch (action.type) {
    case 'PAYABLE_ACTIVATE_TAB':
      return {
        ...state,
        activeTab: action.payload.id,
      };
    case 'PAYABLE_ADD_TAB':
      if (
        state.openTabs.filter(tab => tab.id === action.payload.id).length > 0
      ) {
        return state;
      }
      return {
        ...state,
        openTabs: [...state.openTabs, action.payload],
      };
    case 'PAYABLE_REMOVE_TAB':
      return {
        ...state,
        openTabs: state.openTabs.filter(tab => tab.id !== action.payload.id),
      };
    default:
      return state;
  }
};

const tabs = (
  state = {
    activeTab: 'tab-content-search',
    openTabs: [],
    invoices: { activeTab: 'tab-content-list', openTabs: [] },
    payables: { activeTab: 'tab-payable-statement', openTabs: [] },
  },
  action,
) => {
  // Added tab navigation to invoice.
  // Ideally orders and invoices have their own object structure
  // and not mixed like this.
  // Refactor when have time.
  // tabs = { orders: { activeTab, openTabs }, invoices: { activeTab, openTabs } }
  if (~action.type.toString().indexOf('INVOICE')) {
    return {
      ...state,
      invoices: invoiceTabReducer(state.invoices, action),
    };
  }

  if (~action.type.toString().indexOf('PAYABLE')) {
    return {
      ...state,
      payables: payableTabReducer(state.payables, action),
    };
  }

  switch (action.type) {
    case ADD_TAB:
    case REMOVE_TAB:
    case UPDATE_TAB:
    case UPDATE_TAB_TYPE:
    case UPDATE_TAB_DATA:
      return Object.assign({}, state, {
        openTabs: openTabs(state.openTabs, action),
        activeTab: possiblyChangeTab(state.activeTab, action),
        previousTab: possiblyChangePreviousTab(state.previousTab, action),
      });
    case ACTIVATE_TAB:
      return Object.assign({}, state, {
        previousTab: [...(state.previousTab || []), state.activeTab],
        activeTab: activeTab(state.activeTab, action),
      });
    default:
      return state;
  }
};

export default tabs;
