import React from 'react';
import { connect } from 'react-redux';
import { browserHistory } from 'react-router';

import { viewOrder } from '../actions/orders';

class OrderRedirect extends React.Component {
  componentWillMount() {
    const { routeParams = {} } = this.props;

    setTimeout(() => {
      this.props.viewOrder(routeParams.id, routeParams.command);
      browserHistory.replace('/shipments');
    }, 0);
  }

  render() {
    return null;
  }
}

const mapDispatchToProps = {
  viewOrder,
};

export default connect(null, mapDispatchToProps)(OrderRedirect);
