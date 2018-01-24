import React, { Component } from 'react';
import { Row, Col } from 'react-bootstrap';
import AccountStatus from '../../components/dashboard/AccountStatus';
import OrdersBarChart from '../../components/dashboard/OrdersBarChart';
import EasyPieChartContainer from '../../components/dashboard/EasyPieChartContainer';

class AdminDashboard extends Component {
  render() {
    return (
      <div className="dashboard">
        <Row>
          <Col className="col" sm={6}>
            <h1>Freightcom Admin Dashboard</h1>
          </Col>
        </Row>
        <Row>
          <Col className="col" sm={6}>
            <div className="jarviswidget jarviswidget-sortable jarviswidget-color-blue" id="wid-id-3">
              <header role="heading">
                <span className="widget-icon">
                  <i className="fa fa-pie-chart" />
                </span>
                <h2>Account Status</h2>
                <span className="jarviswidget-loader">
                  <i className="fa fa-refresh fa-spin" />
                </span>
              </header>
              <div>
                <div className="widget-body no-padding">
                  <div className="well no-border">
                    <Row>
                      <Col className="col" sm={6}>
                        <EasyPieChartContainer>
                          <div
                            className="easy-pie-chart txt-color-orangeDark"
                            data-percent="33"
                            data-pie-size="100"
                          >
                            <span className="percent percent-sign">35</span>
                          </div>
                          <span className="easy-pie-title">
                            {' '}Claims{' '}
                            <i className="fa fa-caret-up icon-color-bad" />
                            {' '}
                          </span>
                        </EasyPieChartContainer>
                      </Col>
                      <Col className="col" sm={6}>
                        <EasyPieChartContainer>
                          <div
                            className="easy-pie-chart txt-color-greenLight"
                            data-percent="78.9"
                            data-pie-size="100"
                          >
                            <span className="percent percent-sign">78.9 </span>
                          </div>
                          <span className="easy-pie-title">
                            {' '}Audit{' '}
                            <i className="fa fa-caret-down icon-color-good" />
                          </span>
                        </EasyPieChartContainer>
                      </Col>
                    </Row>
                  </div>
                </div>
              </div>
            </div>
          </Col>
          <Col className="col" sm={6}>
            <div className="jarviswidget jarviswidget-sortable jarviswidget-color-blue" id="wid-id-4">
              <header role="heading">
                <span className="widget-icon">
                  <i className="fa fa-pie-chart" />
                </span>
                <h2>Cost Analytics</h2>
                <span className="jarviswidget-loader">
                  <i className="fa fa-refresh fa-spin" />
                </span>
              </header>
              <div>
                <div className="widget-body no-padding">
                  <div className="well no-border">
                    <Row>
                      <Col className="col" sm={6}>
                        <span>Cost/Pound</span>
                        <h2>
                          {' '}$0.16{' '}
                          <i className="fa fa-caret-up icon-color-bad" />
                          {' '}
                        </h2>
                        <span>(Last 30 Days)</span>
                      </Col>
                      <Col className="col" sm={6}>
                        <span>Cost/Mile</span>
                        <h2>
                          {' '}$0.00{' '}
                          <i className="fa fa-caret-down icon-color-good" />
                        </h2>
                        <span>(Last 30 Days)</span>
                      </Col>
                    </Row>
                  </div>
                </div>
              </div>
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

export default AdminDashboard;
