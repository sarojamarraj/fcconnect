import React, { Component } from 'react';
import { connect } from 'react-redux';
import { browserHistory } from 'react-router';
import { Row, Col } from 'react-bootstrap';
import AccountStatus from '../../components/dashboard/AccountStatus';
import OrdersBarChart from '../../components/dashboard/OrdersBarChart';
import { addTab } from '../../actions/tabs';
import WelcomeMessage from './WelcomeMessage';
import { selectLoggedInCustomer } from '../../reducers';
import { Popover } from 'react-bootstrap';
class CustomerAdminDashboard extends Component {
  constructor(props) {
    super(props);
    this.disallowOrdersPopover = (
      <Popover id="disallowOrdersPopover" title="Attention!">
        Currently your account doesn{"'"}t allow placement of new orders.
      </Popover>
    );
  }
  render() {
    return (
      <div>
        <Row>
          <Col className="col" sm={6}>
            <h1>Customer Admin Dashboard</h1>
          </Col>
        </Row>
        <Row>
          <Col className="col" sm={6}>
            <WelcomeMessage />
          </Col>
        </Row>
        <br/>
        <Row>
          <OrdersBarChart />
          <AccountStatus />
        </Row>
      </div>
    );
  }

  openInstantRate = e => {
    e.preventDefault();
    this.props.dispatch(
      addTab({
        title: 'Quote',
        type: 'ORDER_QUOTE',
        isInstantRate: true,
      }),
    );
    browserHistory.push('/shipments');
  };

  openNewShipment = e => {
    e.preventDefault();
    this.props.dispatch(addTab({ title: 'New Shipment', type: 'ORDER_QUOTE' }));
    browserHistory.push('/shipments');
  };
}
const mapStateToProps = state => {
  return {
    allowNewOrders: selectLoggedInCustomer(state).allowNewOrders,
  };
};

export default connect(mapStateToProps)(CustomerAdminDashboard);
