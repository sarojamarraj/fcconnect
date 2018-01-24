import React from 'react';
import { Router, Route, IndexRedirect, IndexRoute } from 'react-router';

import { addNotification } from 'reapop';

import App from './containers/App';

import Dashboard from './pages/Dashboard';
import Shipments from './pages/Shipments';
import AddressBook from './pages/AddressBook';
import Settings from './pages/Settings';
import Accounts from './pages/Accounts';

import Agents from './pages/Agents';
import EditAgent from './containers/agents/EditAgent';
import Customers from './pages/Customers';
import CustomerForm from './containers/customers/CustomerForm';
import EditCustomer from './containers/customers/EditCustomer';

import Carriers from './pages/Carriers';
import CarrierInvoices from './pages/CarrierInvoices';
import Invoices from './pages/Invoices';
import ViewInvoice from './containers/invoices/ViewInvoice';
import Claims from './pages/Claims';
import Users from './pages/Users';
import UserForm from './containers/users/UserForm';

import Currencies from './pages/Currencies';
import CustomerShipments from './pages/CustomerShipments';
import CustomerMarkups from './pages/CustomerMarkups';
import Credits from './pages/Credits';

import Login from './pages/Login';
import Logout from './pages/Logout';

import Payables from './pages/Payables';

import EnsureLoggedIn from './components/EnsureLoggedIn';
import ErrorPage from './pages/ErrorPage';
import Loading from './pages/Loading';
import Cheques from './pages/Cheques';

import OrderRedirect from './pages/OrderRedirect';

//import { getLoggedInRole } from './reducers';

const routeError = () => {
  Router.transitionTo('/login');
};

const configureRoutes = store => {
  function ensureLoggedIn(nextState, replace, callback) {
    if (store.getState().loggedIn.role && store.getState().loggedIn.role.id) {
      callback();
      return;
    }

    store.dispatch({
      type: 'SELECT_ROLE_REQUEST',
    });

    fetch('/api/session-role', { credentials: 'include' })
      .then(response => response.json())
      .then(json => {
        store.dispatch({
          type: 'SELECT_ROLE_SUCCESS',
          payload: json,
        });
        callback();
      })
      .catch(error => {
        console.error(error);
      });
  }

  function checkAccess(roles = []) {
    return (nextState, replace) => {
      let roleName = store.getState().loggedIn.role.roleName || '';
      if (!~roles.indexOf(roleName)) {
        replace('/dashboard');
        // Need to lift it up the call stack since <Notification is inside app
        // it's possible that the App component isn't ready yet.
        setTimeout(() => {
          // For some reason, this gets dismissed when dashboard page loads...
          // Still figuring this out...
          store.dispatch(
            addNotification({
              title: 'Page Error',
              message: "The page you are trying to access is invalid or you don't have permission to",
              status: 'error',
              dismissible: true,
              dismissAfter: 0,
            }),
          );
        }, 0);
      }
    };
  }

  return (
    <Route onError={() => routeError()}>
      <Route component={EnsureLoggedIn}>
        <Route path="/" component={App} onEnter={ensureLoggedIn}>
          <IndexRedirect to="/dashboard" />
          <IndexRoute component={Dashboard} />

          <Route path="address-book" component={AddressBook} />
          <Route path="accounts" component={Accounts} />
          <Route
            path="agents"
            component={Agents}
            onEnter={checkAccess(['ADMIN', 'FREIGHTCOM_STAFF', 'AGENT'])}
          />
          <Route
            path="agents/:id"
            component={EditAgent}
            onEnter={checkAccess(['ADMIN', 'FREIGHTCOM_STAFF', 'AGENT'])}
          />
          <Route
            path="carriers"
            component={Carriers}
            onEnter={checkAccess(['ADMIN', 'FREIGHTCOM_STAFF'])}
          />
          <Route path="claims" component={Claims} />
          <Route
            path="currencies"
            component={Currencies}
            onEnter={checkAccess(['ADMIN'])}
          />
          <Route
            path="customers"
            component={Customers}
            onEnter={checkAccess(['ADMIN', 'FREIGHTCOM_STAFF', 'AGENT'])}
          />
          <Route
            path="customers/add"
            component={CustomerForm}
            onEnter={checkAccess(['ADMIN', 'FREIGHTCOM_STAFF', 'AGENT'])}
          />
          <Route
            path="customers/:id"
            component={EditCustomer}
            onEnter={checkAccess(['ADMIN', 'FREIGHTCOM_STAFF', 'AGENT'])}
          />
          <Route
            path="customers/:id/:tab"
            component={EditCustomer}
            onEnter={checkAccess(['ADMIN', 'FREIGHTCOM_STAFF', 'AGENT'])}
          />
          <Route
            path="customer-shipments"
            component={CustomerShipments}
            onEnter={checkAccess(['ADMIN', 'FREIGHTCOM_STAFF', 'AGENT'])}
          />
          <Route
            path="customer-markup"
            component={CustomerMarkups}
            onEnter={checkAccess(['ADMIN'])}
          />
          <Route path="dashboard" component={Dashboard} />
          <Route path="edi" component={CarrierInvoices} />
          <Route path="invoices" component={Invoices}>
            <Route path=":id" component={ViewInvoice} />
          </Route>

          <Route path="order/:id" component={OrderRedirect} />
          <Route path="orders/:id" component={OrderRedirect} />
          <Route path="orders/:id/:command" component={OrderRedirect} />
          <Route path="payables" component={Payables} />

          <Route path="shipments" component={Shipments} />
          <Route path="settings" component={Settings} />
          <Route
            path="users"
            component={Users}
            onEnter={checkAccess([
              'ADMIN',
              'FREIGHTCOM_STAFF',
              'AGENT',
              'CUSTOMER_ADMIN',
            ])}
          />
          <Route
            path="users/add"
            component={UserForm}
            onEnter={checkAccess(['ADMIN', 'FREIGHTCOM_STAFF', 'AGENT'])}
          />
          <Route
            path="users/:id"
            component={UserForm}
            onEnter={checkAccess(['ADMIN', 'FREIGHTCOM_STAFF', 'AGENT'])}
          />
          <Route path="credits" component={Credits} />
          <Route path="loading" component={Loading} />
          <Route path="cheques" component={Cheques} />
        </Route>
      </Route>
      <Route path="login" component={Login} />
      <Route path="logout" component={Logout} />
      <Route path="/" component={App} onEnter={ensureLoggedIn}>
        <Route path="*" component={ErrorPage} />
      </Route>
    </Route>
  );
};

export default configureRoutes;
