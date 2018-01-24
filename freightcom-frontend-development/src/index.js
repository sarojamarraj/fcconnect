import React from 'react';
import ReactDOM from 'react-dom';
import { Router, browserHistory } from 'react-router';
import { Provider } from 'react-redux';

import 'bootstrap/dist/css/bootstrap.min.css';
import 'font-awesome/css/font-awesome.min.css';
import './styles/css/bootstrap-editable.css';
import './styles/css/plugins.min.css';
import './styles/css/base.min.css';
import './styles/css/skins.min.css';
import './styles/css/rtl.min.css';
import './styles/css/styles.css';
import './styles/css/react-select.css';

import configureRoutes from './routes';
import configureStore from './store/configureStore';

const store = configureStore();
const routes = configureRoutes(store);

ReactDOM.render(
  <Provider store={store}>
    <Router history={browserHistory} routes={routes} />
  </Provider>,
  document.getElementById('root'),
);
