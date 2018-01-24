// Creates a reducer managing pagination, given the action types to handle,
// and a function telling how to extract the key from an action.
const autoSuggest = ({ types, mapActionToKey }) => {
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

  const updateAutoSuggestion = (
    state = {
      requestId: 0,
      isFetching: false,
      keyword: '',
      totalItems: 0,
      ids: [],
    },
    action,
  ) => {
    switch (action.type) {
      case requestType:
        return {
          ...state,
          requestId: action.payload.requestId,
          isFetching: true,
        };
      case successType:
        if (action.payload.requestId === state.requestId) {
          //console.log('last request processed - ' + action.payload.requestId);
          return {
            ...state,
            isFetching: false,
            ids: action.payload.result,
            keyword: action.payload.keyword,
            totalItems: action.payload.totalItems,
          };
        } else {
          if (action.error) {
            return {
              ...state,
              isFetching: false,
            };
          }
          return {
            ...state,
            isFetching: true,
          };
        }

      case failureType:
        return {
          ...state,
          isFetching: false,
        };
      default:
        return state;
    }
  };

  return (state = {}, action) => {
    // Update autoSuggest by key
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
          [key]: updateAutoSuggestion(state[key], action),
        };
      default:
        return state;
    }
  };
};

export default autoSuggest;
