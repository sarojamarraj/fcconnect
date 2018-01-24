import React, { Component } from 'react';
import { Row, Col } from 'react-bootstrap';
import AccountStatus from '../../components/dashboard/AccountStatus';
import OrdersBarChart from '../../components/dashboard/OrdersBarChart';
import LookupWidget from '../../components/dashboard/LookupWidget';

class StaffDashboard extends Component {
  render() {
    return (
      <Row>
        <Col sm={12}>
          <h1>Freightcom Staff Dashboard</h1>
        </Col>
        <Col sm={6}>
          <LookupWidget />
        </Col>
        <AccountStatus />
        <OrdersBarChart />
      </Row>
    );
  }
}

export default StaffDashboard;
