// Creates a reducer managing pagination, given the action types to handle,
// and a function telling how to extract the key from an action.
const paginate = ({ types, mapActionToKey }) => {
  if (!Array.isArray(types) || types.length !== 3) {
    throw new Error('Expected types to be an array of three elements.');
  }
  if (!types.every(t => typeof t === 'string')) {
    throw new Error('Expected types to be strings.');
  }
  if (typeof mapActionToKey !== 'function') {
    throw new Error('Expected mapActionToKey to be a function.');
  }

  const [requestType, successType, failureType] = types;

  const updatePagination = (
    state = {
      isFetching: false,
      currentPage: '1',
      itemsPerPage: '8',
      totalItems: undefined,
      sortOrder: {},
      filters: {},
      ids: [],
    },
    action,
  ) => {
    switch (action.type) {
      case requestType:
        return {
          ...state,
          isFetching: true,
          redrawIfFetching: action.meta.redrawIfFetching,
        };
      case successType:
        const ids = action.payload['result'] || [];
        if (action.meta.noUpdatePage) {
          // Don't update totalItems if noUpdatePage
          // Used when separate page query
          return {
            ...state,
            isFetching: false,
            currentPage: action.payload.currentPage,
            ids: ids.constructor === Array ? ids : [],
          };
        }

        return {
          ...state,
          isFetching: false,
          ids: ids.constructor === Array ? ids : [],
          filters: action.payload.filters,
          sortOrder: action.payload.sortOrder,
          currentPage: action.payload.currentPage,
          itemsPerPage: action.payload.itemsPerPage,
          totalItems: action.payload.totalItems,
          redrawIfFetching: action.meta.redrawIfFetching,
        };
      case failureType:
        return { ...state, isFetching: false };
      default:
        return state;
    }
  };

  return (state = {}, action) => {
    // Update pagination by key
    switch (action.type) {
      case requestType:
      case successType:
      case failureType:
        const key = mapActionToKey(action);
        if (typeof key !== 'string') {
          throw new Error('Expected key to be a string.');
        }
        return {
          ...state,
          ['_' + key]: updatePagination(state['_' + key], action),
        };

      case 'ORDER_COUNT_SUCCESS':
        const countKey = action.meta.key;
        const data = state[countKey] || {};
        return {
          ...state,
          [countKey]: { ...data, totalItems: action.payload.count },
        };

      default:
        return state;
    }
  };
};

export default paginate;
