import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Row, Col } from 'react-bootstrap';

import formatDate from 'date-fns/format';
import { formatAddress, formatCurrency } from '../../utils';
import { loadPayableStatementById } from '../../actions/payables';
import { selectOneFromEntities } from '../../reducers';

class PayableStatement extends Component {
  componentWillMount() {
    this.props.loadPayableStatementById(this.props.id);
  }

  render() {
    const {
      status,
      paidAt = '',
      carrier = {},
      toAddress = {},
      fromAddress = {},
    } = this.props.payableStatement;
    return (
      <div>
        <div className="invoice-view">
          <div className="invoice-header">
            <h2><span>Payable Statement</span></h2>
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
                          {fromAddress.companyName || 'FREIGHTCOM'}
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
                          {carrier.name || ''}
                        </strong>
                      </p>
                      <p className="address">
                      {formatAddress({
                        address1: toAddress.address1,
                        address2: toAddress.address2,
                      })}
                      </p>
                      <p className="address">
                        {formatAddress({
                          city: toAddress.city,
                          postalCode: toAddress.postalCode,
                          country: toAddress.country,
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
                      <th className="currency">Amount</th>
                    </tr>
                  </thead>
                  <tbody>
                    {this.props.payableStatement.orders &&
                      !!this.props.payableStatement.orders.length
                      ? this.props.payableStatement.orders.map((item, idx) => (
                          <tr key={idx}>
                            <td>{item.id}</td>
                            <td>{item.bolId}</td>
                            <td>{item.customer.name || ''}</td>
                            <td>{formatDate(item.shipDate, 'YYYY-MM-DD')}</td>
                            <td className="currency">
                              {formatCurrency(item.totalCost)}
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
          <Row>
            <Col className="col" sm={12} xs={12}>
              <div className="jarviswidget jarviswidget-sortable jarviswidget-color-blue" data-widget-edit-button="false">
                <header role="heading">
                  <h2>Total Charges by Type</h2>
                </header>
                <table className="table table-bordered table-condensed">
                  <thead>
                    <tr>
                      <th>Description</th>
                      <th>Cost</th>
                    </tr>
                  </thead>
                  <tbody>
                    {this.props.payableStatement.groupedCharges &&
                      !!this.props.payableStatement.groupedCharges.length
                      ? this.props.payableStatement.groupedCharges.map((item, idx) => (
                          <tr key={idx}>
                            <td>{item.description}</td>
                            <td className="currency">{formatCurrency(item.cost)}</td>
                          </tr>
                        ))
                      : <tr><td colSpan="2">No Charges</td></tr>}
                  </tbody>
                </table>
              </div>
            </Col>
          </Row>
        
          <footer className="form-actions clearfix">
            <h3 className="pull-right">
              Total Payables:
              {' '}
              <b>{formatCurrency(this.props.payableStatement.totalAmount)}</b>
            </h3>
            <div className="invoice-totals row" />
          </footer>
        </div>
      </div>
    );
  }
}

const mapStateToProps = (state, ownProps) => {
  return {
    payableStatement: selectOneFromEntities(
      state,
      'payableStatement',
      ownProps.id,
    ),
  };
};

const mapDispatchToProps = {
  loadPayableStatementById,
};

export default connect(mapStateToProps, mapDispatchToProps)(PayableStatement);
