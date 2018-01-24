import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Row, Col } from 'react-bootstrap';
import findKey from 'lodash/findKey';
import formatDate from 'date-fns/format';
import { formatCurrency, formatAddress } from '../../utils';
import { loadCommissionStatementById } from '../../actions/payables';
import { loadSettings } from '../../actions/settings';
import { selectOneFromEntities } from '../../reducers';

class CommissionStatement extends Component {
  componentWillMount() {
    this.props.loadSettings();
    this.props.loadCommissionStatementById(this.props.id);
  }

  render() {
    const {
      status,
      paidAt = '',
      agent = {},
      //toAddress = {},
      fromAddress = {},
    } = this.props.commissionStatement;
    return (
      <div>
        <div className="invoice-view">
          <div className="invoice-header">
            <h2><span>Commission Statement</span></h2>
            <div>
              {status === 'paid'
                ? <span className="label label-success">
                    PAID AT {formatDate(paidAt, 'YYYY-MM-DD')}
                  </span>
                : <span className="label label-warning">
                    UNPAID
                  </span>}
            </div>
            <hr className="simple" />
          </div>
        </div>
        <div>
          <Row className="flex invoice-fromto">
            <Col className="col" sm={6} xs={12}>
              <div className="jarviswidget jarviswidget-sortable jarviswidget-color-blue" data-widget-edit-button="false">
                <header role="heading">
                  <h2>From</h2>
                </header>
                <div>
                  <div className="widget-body">
                    <address>
                      <p className="address">
                        <strong>
                          {fromAddress.companyName ||
                            'FREIGHTCOM COMMISSION PAYMENTS'}
                        </strong>
                      </p>
                      <p className="address">
                        {formatAddress({
                          address1: fromAddress.address1,
                          address2: fromAddress.address2,
                        })}
                      </p>
                      <p className="address">
                        {formatAddress({
                          city: fromAddress.city,
                          postalCode: fromAddress.postalCode,
                          country: fromAddress.country,
                        })}
                      </p>
                    </address>
                  </div>
                </div>
              </div>
            </Col>
            <Col className="col" sm={6} xs={12}>
              <div className="jarviswidget jarviswidget-sortable jarviswidget-color-blue" data-widget-edit-button="false">
                <header role="heading">
                  <h2>To</h2>
                </header>
                <div>
                  <div className="widget-body">
                    <address>
                      <p className="address">
                        <strong>
                          {agent.name || ''}
                        </strong>
                      </p>
                      <p className="address">
                        {formatAddress({
                          address1: agent.address1,
                          address2: agent.address2,
                        })}
                      </p>
                      <p className="address">
                        {formatAddress({
                          city: agent.city,
                          postalCode: agent.postalCode,
                          country: agent.country,
                        })}
                      </p>
                      </address>
                  </div>
                </div>
              </div>
            </Col>
          </Row>
          <Row>
            <Col className="col" sm={12} xs={12}>
              <div className="jarviswidget jarviswidget-sortable jarviswidget-color-blue" data-widget-edit-button="false">
                <header role="heading">
                  <h2>Orders</h2>
                </header>
                <table className="table table-bordered table-condensed">
                  <thead>
                    <tr>
                      <th>Order ID</th>
                      <th>BOL</th>
                      <th>Customer</th>
                      <th>Ship Date</th>
                      <th className="currency">Order Charge</th>
                      <th className="currency">Commission</th>
                    </tr>
                  </thead>
                  <tbody>
                    {this.props.commissionStatement.orders &&
                      !!this.props.commissionStatement.orders.length
                      ? this.props.commissionStatement.orders.map((item, idx) => (
                          <tr key={idx}>
                            <td>{item.id}</td>
                            <td>{item.bolId}</td>
                            <td>{item.customer.name}</td>
                            <td>{formatDate(item.shipDate, 'YYYY-MM-DD')}</td>
                            <td className="currency">
                              {formatCurrency(item.totalCharge)}
                            </td>
                            <td className="currency">
                              {formatCurrency(item.commission)}
                            </td>
                          </tr>
                        ))
                      : <tr>
                          <td colSpan={5}>No Orders</td>
                        </tr>}
                  </tbody>
                </table>
              </div>
            </Col>
          </Row>
          <hr className="simple" />
          <h3>Terms</h3>
          <p>{this.props.commissionTerms}</p>

          <footer className="form-actions clearfix">
            <h3 className="pull-right">
              Total Commission Payable:
              {' '}
              <b>{formatCurrency(this.props.commissionStatement.totalAmount)}</b>
            </h3>
          </footer>
          <Row />
        </div>
      </div>
    );
  }
}

const mapStateToProps = (state, ownProps) => {
  return {
    commissionStatement: selectOneFromEntities(
      state,
      'commissionStatement',
      ownProps.id,
    ),
    commissionTerms: state.entities.systemProperties
      ? state.entities.systemProperties[
          findKey(state.entities.systemProperties, { name: 'commission_terms' })
        ].data
      : null,
  };
};

const mapDispatchToProps = {
  loadSettings,
  loadCommissionStatementById,
};

export default connect(mapStateToProps, mapDispatchToProps)(
  CommissionStatement,
);
