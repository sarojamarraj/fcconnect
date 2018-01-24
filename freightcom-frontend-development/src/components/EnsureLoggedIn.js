import { Component } from 'react';
import { connect } from 'react-redux';
import {
  checkLoggedIn,
  getLoginRole,
  NOT_LOGGED_IN,
  LOGGED_IN,
  PRE_LOGGED_IN,
  storeUrl,
  clearStoredUrl,
} from '../actions/loginSubmit';
import { withRouter } from 'react-router';
import { loginStoredUrl, loggedInCustomer } from '../reducers/index.js';
import { loadCustomerById } from '../actions/customers';
import { checkLoadStatus } from '../reducers/load-status';

class EnsureLoggedIn extends Component {
  componentWillMount() {
    const {
      currentURL,
      loggedIn,
      router,
      checkLogin,
      getLoginRole,
      storedURL,
      storeUrl,
      loadCustomerById,
      roleCustomer,
    } = this.props;

    if (loggedIn.status === NOT_LOGGED_IN) {
      // set the current url/path for future redirection (we use a Redux action)
      // then redirect (we use a React Router method)
      router.replace('/login');
      storeUrl(currentURL);
    } else if (loggedIn.status === PRE_LOGGED_IN) {
      // No value for logged in, check fist
      checkLogin().then(() => getLoginRole());
    } else if (!roleCustomer && loggedIn.role.customerId) {
      loadCustomerById(loggedIn.role.customerId);
    } else if (storedURL) {
      clearStoredUrl();
    }
  }

  componentWillReceiveProps(nextProps) {
    const {
      currentURL,
      loggedIn,
      router,
      storedURL,
      storeUrl,
      loadCustomerById,
      roleCustomer,
      loadingCustomer,
    } = nextProps;

    if (loggedIn.status === NOT_LOGGED_IN) {
      // set the current url/path for future redirection (we use a Redux action)
      // then redirect (we use a React Router method)
      router.replace('/login');
      storeUrl(currentURL);
    } else if (!roleCustomer && loggedIn.role.customerId && !loadingCustomer) {
      loadCustomerById(loggedIn.role.customerId);
    } else if (storedURL) {
      clearStoredUrl();
    }
  }

  render() {
    if (this.props.loggedIn.status === LOGGED_IN && this.props.loggedIn.role) {
      return this.props.children;
    } else {
      return null;
    }
  }
}

// Grab a reference to the current URL. If this is a web app and you are
// using React Router, you can use `ownProps` to find the URL. Other
// platforms (Native) or routing libraries have similar ways to find
// the current position in the app.
function mapStateToProps(state, ownProps) {
  return {
    loggedIn: state.loggedIn,
    currentURL: ownProps.location.pathname,
    storedURL: loginStoredUrl(state),
    roleCustomer: loggedInCustomer(state),
    loadingCustomer: state.loggedIn &&
      state.loggedIn.role &&
      checkLoadStatus(state, 'customer', state.loggedIn.role.customerId),
  };
}

export default withRouter(
  connect(mapStateToProps, {
    checkLogin: () => checkLoggedIn(),
    getLoginRole,
    storeUrl,
    clearStoredUrl,
    loadCustomerById,
  })(EnsureLoggedIn),
);
