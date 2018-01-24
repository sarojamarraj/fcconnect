# Managing App State
The application uses Redux to manage app state. This means there's only one global object (`store`) used in the entire app. It uses `react-redux` bindings to map out the state data and action creators to React component `props` using the `connect()`. function.

## Redux State tree
```
{
  notifications: [],           => Used by reapop library to show notifications
  loggedIn: {},                => Contains login status and the current user role
    role: {}
  }
  loggedInUser: {},            => Information about the current logged in user
  form: {},                    => Used by redux-form
  entities: {},                => Contains all entities from the server parsed by normalizr
  pagination: {},              => Pagination information for listing entities
  autoSuggest: {},             => Only used by AddressBook auto suggest (subject to simplification)
  tabs: {                      => Contains tab information of orders, invoices, payables (needs refactoring)
    activeTab: string          => Shipment's active tab (tab ID)
    openTabs: []               => Array of open tabs in Shipments
    invoices: {                => Tab information for Invoices
      activeTab, openTabs
    },
    payables: {                => Tab information for Payables (Payables and Commissions)
      activeTab, openTabs
    }
  },                    
  orders: {                    => Contains order information like Rates and distribution list
    rates: {},
    distributionList,
    stats: {                   => Contains the total number of orders by shipment type
      env, pak, etc...
    }
  }
}
```

## Getting data from Redux store
To get data from Redux `store`. You need to use the `connect` function from `react-redux` library.

```Javascript
import React, { Component } from 'react';
import { connect } from 'react-redux';

class MyComponent extends Component {
  render() {
    // The object returned by mapStateToProps function below makes the data accessible in `this.props.<mapStateToProps object key>`.
    return <span>{this.props.loggedInUserName}</span>;
  }
}

// The `state` parameter here will contain the entire Redux state tree.
const mapStateToProps = (state) => {
  return {
    // For best practice, use Selectors instead of directly referencing the redux state
    loggedInUserName: state.loggedInUser.login,
  }
}

export default connect(mapStateToProps)(MyComponent);
```

### Using `Selectors` inside `mapStateToProps`
A way to decouple React Components to the Redux state tree structure, is to use Selectors.

**Selectors** are just functions that accept the state tree and extract data and return the needed data. Below is an example of using a Selector.

```Javascript
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { selectLoggedInUserName } from '../selectors';

class MyComponent extends Component {
  render() {
    return <span>{this.props.loggedInUserName}</span>;
  }
}

const mapStateToProps = (state) => {
  return {
    loggedInUserName: selectLoggedInUserName(state),
  }
}

export default connect(mapStateToProps)(MyComponent);

// ---------------
// selectors.js
export const selectLoggedInUserName = (state) => {
  return state.loggedInUser.login;
}
```

## Updating Redux store data
Any changes to the Redux `store` will have to go through dispatching an **Action** and have a **Reducer** return a new updated state.

```Javascript
// ---------------
// actions/tabs.js
export const activateTab = (tabId) => {
  return {
    type: 'ACTIVATE_TAB',
    payload: { tabId },
  };
}

// ---------------
// reducers/tabs.js
const tabs = (state, action) => {
  switch (action.type) {
    case 'ACTIVATE_TAB':
      return {
        ...state,
        activeTab: action.payload.tabId,
      };

    default:
      return state;
  }
}

// ---------------
// components/MyComponent.js
import React from 'react';
import { activateTab } from '../actions/tabs';

class MyComponent extends Component {
  render() {
    return <button onClick={this.handleClick}>Activate Tab</button>;
  }
  handleClick = e => {
    this.props.activateTab(`Tab ID`);
  }
}

const mapDispatchToProps = {
  activateTab,
}

export default connect(null, mapDispatchToProps)(MyComponent);
```

### Using `redux-thunk` middleware
Redux thunk middleware allows you to dispatch a function instead of an action object.

#### Common uses of thunk
1. Access to Redux store inside action creators.

```Javascript
const someAction = (someParams) => (dispatch, getState) => {
  // this will contain the entire Redux state tree
  const reduxState = getState();

  console.log(reduxState.loggedInUser.login);

  // Manually call dispatch.
  dispatch(...);
}
```

2. Dispatching multiple actions

```Javascript
const someAction = (someParams) => (dispatch) => {

  dispatch({ type: 'ACTION_ONE' });

  if (something) {
    dispatch({ type: 'ACTION_TWO' });
  }
}

```
