import React, { Component } from 'react';
import { Row, Col } from 'react-bootstrap';
import AccountStatus from '../../components/dashboard/AccountStatus';
import OrdersBarChart from '../../components/dashboard/OrdersBarChart';

class AgentDashboad extends Component {
  render() {
    return (
      <div>
        <Row>
          <Col className="col" sm={6}>
            <h1>Agent Dashboard</h1>
          </Col>
        </Row>
        <Row>
          <Col className="col" sm={6}>
            <div
              className="jarviswidget jarviswidget-color-blueLight"
              id="wid-id-3"
            >
              Placeholder A
            </div>
          </Col>
          <Col className="col" sm={6}>
            <div
              className="jarviswidget jarviswidget-color-blueLight"
              id="wid-id-4"
            >
              Placeholder B
            </div>
          </Col>
        </Row>
        <Row>
          <OrdersBarChart />
          <AccountStatus />
        </Row>
      </div>
    );
  }
}

export default AgentDashboad;
