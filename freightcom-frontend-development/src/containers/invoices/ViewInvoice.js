import React, { Component } from 'react';
import { Row, Col, Button } from 'react-bootstrap';
import formatDate from 'date-fns/format';
import { loadInvoiceById, viewInvoice } from '../../actions/invoices';
import { connect } from 'react-redux';
import { browserHistory, Link } from 'react-router';

import {
  selectOneFromEntities,
  checkRoleIfAdminOrAgent,
  checkRoleIfFreightcom,
  getSystemProperty,
} from '../../reducers';
import { formatCurrency } from '../../utils';
import InvoicePaymentModal from '../../components/invoices/InvoicePaymentModal';
import PaymentHistoryModal from '../../components/invoices/PaymentHistoryModal';
import { addTab, removeTab } from '../../actions/tabs';

import { loadSettings } from '../../actions/settings';
import { checkLoadStatus } from '../../reducers/load-status';

class ViewInvoice extends Component {
  constructor(props) {
    super(props);
    this.state = { paymentModal: false, paymentHistoryModal: false };
  }

  componentWillMount() {
    this.props.loadSettings();

    if (!this.props.isAdminOrAgent) {
      this.props.viewInvoice(this.props.params.id);
    } else {
      this.props.loadInvoiceById(this.props.params.id);
    }
  }

  render() {
    const {
      invoice: { customer: { id: customerId, name: customerName } = {} } = {},
      isAdminOrAgent,
    } = this.props;

    return (
      <div className="invoice-view">
        <div className="invoice-header">
          <h2 className="clearfix" style={{ paddingTop: '0', marginTop: '0' }}>
            {isAdminOrAgent &&
              customerId &&
              <span className="pull-left">
                <Link to={`/customers/${customerId}`}>
                  {customerName}
                </Link>
                &nbsp;&nbsp;
              </span>}
            <span>
              Invoice #: {this.props.params.id}
            </span>
          </h2>

          <hr className="simple" />
        </div>
        {this.renderContents()}
      </div>
    );
  }

  renderContents() {
    const { loadStatus } = this.props;

    if (loadStatus === 'loading') {
      return <div>Loading</div>;
    } else if (loadStatus === 'failed') {
      return <div>Loading invoice failed</div>;
    } else if (loadStatus !== 'success') {
      return <div>Invoice not available</div>;
    } else {
      return (
        <div>
          <Row className="flex">
            <Col className="col" sm={6} xs={12}>
              <div
                className="jarviswidget jarviswidget-sortable jarviswidget-color-blue"
                data-widget-edit-button="false"
              >
                <header role="heading">
                  <h2>Ship From</h2>
                </header>
                <div>
                  <div className="widget-body">
                    <address>
                      <p className="address">
                        <strong>
                          FREIGHTCOM
                        </strong>
                      </p>
                      <p className="address">
                        1234 Freightcom Rd
                      </p>
                      <p className="address">
                        Toronto, ON
                      </p>
                      <br />
                    </address>
                  </div>
                </div>
              </div>
            </Col>
            <Col className="col" sm={6} xs={12}>
              <div
                className="jarviswidget jarviswidget-sortable jarviswidget-color-blue"
                data-widget-edit-button="false"
              >
                <header role="heading">
                  <h2>Ship To</h2>
                </header>
                <div>
                  <div className="widget-body">
                    <address>
                      <p className="address">
                        <strong>
                          {this.props.invoice.customer &&
                            this.props.invoice.customer.name}
                        </strong>
                      </p>
                      <p className="address">
                        {this.props.invoice.customer &&
                          this.props.invoice.customer.address
                          ? this.props.invoice.customer.address
                          : 'Address needs to be supplied'}
                      </p>
                      <p className="address">
                        {this.props.invoice.customer &&
                          this.props.invoice.customer.city
                          ? this.props.invoice.customer.city +
                              ' ' +
                              this.props.invoice.customer.provice +
                              ' ' +
                              this.props.invoice.customer.country
                          : 'Address needs to be supplied'}
                      </p>
                      <br />
                    </address>
                  </div>
                </div>
              </div>
            </Col>
          </Row>
          <Row>
            <Col className="col" sm={12} xs={12}>
              <div
                className="jarviswidget jarviswidget-sortable jarviswidget-color-blue"
                data-widget-edit-button="false"
              >
                <header role="heading">
                  <h2>Invoice Details</h2>
                </header>
                <div>
                  <div className="widget-body">
                    <Row>
                      <Col className="col" sm={4}>
                        <div className="table-responsive">
                          <table className="table-list">
                            <tbody>
                              <tr>
                                <td
                                  style={{
                                    verticalAlign: 'top',
                                    paddingRight: '20px',
                                  }}
                                >
                                  Invoice #
                                </td>
                                <th>
                                  {this.props.invoice.id}
                                </th>
                              </tr>
                              <tr>
                                <td
                                  style={{
                                    verticalAlign: 'top',
                                    paddingRight: '20px',
                                  }}
                                >
                                  Invoice Date
                                </td>
                                <th>
                                  {this.props.invoice.dateGenerated
                                    ? this.props.invoice.dateGenerated
                                    : this.props.invoice.createdAt}
                                </th>
                              </tr>
                              <tr>
                                <td
                                  style={{
                                    verticalAlign: 'top',
                                    paddingRight: '20px',
                                  }}
                                >
                                  Due Date
                                </td>
                                <th>
                                  {this.props.invoice.dueDate}
                                </th>
                              </tr>
                            </tbody>
                          </table>
                        </div>
                      </Col>
                      <Col className="col" sm={4}>
                        <table className="table-list">
                          <tbody>
                            <tr>
                              <td
                                style={{
                                  verticalAlign: 'top',
                                  paddingRight: '20px',
                                }}
                              >
                                Status
                              </td>
                              <th>
                                {this.props.invoice.paymentStatus
                                  ? 'Paid'
                                  : 'Unpaid'}
                              </th>
                            </tr>
                            <tr>
                              <td
                                style={{
                                  verticalAlign: 'top',
                                  paddingRight: '20px',
                                }}
                              >
                                Currency
                              </td>
                              <th>
                                {this.props.invoice.currency || 'N/A'}
                              </th>
                            </tr>
                          </tbody>
                        </table>
                      </Col>
                      <Col className="col" sm={4}>
                        <table className="table-list">
                          <tbody>
                            <tr>
                              <td>Sub Total</td>
                              <td>
                                <b>
                                  {formatCurrency(this.props.invoice.subtotal)}
                                </b>
                              </td>
                            </tr>
                            {this.props.invoice.groupedTaxes &&
                              this.props.invoice.groupedTaxes.map(
                                (tax, idx) => (
                                  <tr key={idx}>
                                    <td>
                                      {tax.name}
                                    </td>
                                    <td className="text-right">
                                      {formatCurrency(tax.total)}
                                    </td>
                                  </tr>
                                ),
                              )}
                            <tr>
                              <td
                                style={{
                                  verticalAlign: 'top',
                                  paddingRight: '20px',
                                }}
                              >
                                Total Amount
                              </td>
                              <td style={{ textAlign: 'right' }}>
                                <strong>
                                  {formatCurrency(this.props.invoice.amount)}
                                </strong>
                              </td>
                            </tr>
                            <tr>
                              <td
                                style={{
                                  verticalAlign: 'top',
                                  paddingRight: '20px',
                                }}
                              >
                                Amount Paid
                              </td>
                              <td style={{ textAlign: 'right' }}>
                                <strong>
                                  {formatCurrency(
                                    this.props.invoice.paidAmount,
                                  )}
                                </strong>
                              </td>
                            </tr>
                            {this.props.invoice.creditedAmount
                              ? <tr>
                                  <td
                                    style={{
                                      verticalAlign: 'top',
                                      paddingRight: '20px',
                                    }}
                                  >
                                    Credits Used
                                  </td>
                                  <td style={{ textAlign: 'right' }}>
                                    <strong>
                                      {formatCurrency(
                                        this.props.invoice.creditedAmount,
                                      )}
                                    </strong>
                                  </td>
                                </tr>
                              : null}
                            <tr>
                              <td
                                style={{
                                  verticalAlign: 'top',
                                  paddingRight: '20px',
                                }}
                              >
                                Amount Owing
                              </td>
                              <td style={{ textAlign: 'right' }}>
                                <strong>
                                  {formatCurrency(
                                    this.props.invoice.amountRemaining,
                                  )}
                                </strong>
                              </td>
                            </tr>
                          </tbody>
                        </table>
                      </Col>
                    </Row>
                  </div>
                </div>
              </div>
            </Col>
          </Row>

          <Row className="flex">
            <Col className="col" md={4} sm={12} xs={12}>
              <div
                className="jarviswidget jarviswidget-sortable jarviswidget-color-blue"
                data-widget-edit-button="false"
              >
                <header role="heading">
                  <h2>Charges</h2>
                </header>
                <table className="table table-bordered table-condensed">
                  <thead>
                    <tr>
                      <th>Description</th>
                      <th>Charge</th>
                    </tr>
                  </thead>
                  <tbody>
                    {this.props.invoice.groupedCharges &&
                      this.props.invoice.groupedCharges.map((charge, index) => (
                        <tr key={index}>
                          <td>
                            {charge.description}
                          </td>
                          <td className="currency">
                            {formatCurrency(charge.total)}
                          </td>
                        </tr>
                      ))}
                  </tbody>
                  {this.props.invoice.groupedCharges &&
                    <tfoot>
                      <tr>
                        <th>Total</th>
                        <th className="currency">
                          {formatCurrency(this.props.invoice.subtotal)}
                        </th>
                      </tr>
                    </tfoot>}
                </table>
              </div>
            </Col>
            <Col className="col" md={8} sm={12} xs={12}>
              <div
                className="jarviswidget jarviswidget-sortable jarviswidget-color-blue"
                data-widget-edit-button="false"
              >
                <header role="heading">
                  <h2>Orders</h2>
                </header>
                <table className="table table-bordered table-condensed">
                  <thead>
                    <tr>
                      <th>Order #</th>
                      <th>Date Created</th>
                      <th>Total Charges</th>
                      {this.props.isAdmin && <th>Profit</th>}
                      <th>Status</th>
                      <th>Type</th>
                      <th />
                    </tr>
                  </thead>
                  <tbody>
                    {this.props.invoice.orders &&
                      this.props.invoice.orders.map((order, index) => (
                        <tr key={index}>
                          <td>
                            {order.bolId || order.id || 'N/A'}
                          </td>
                          <td>
                            {formatDate(order.createdAt, 'YYYY-MM-DD')}
                          </td>
                          <td className="currency">
                            {formatCurrency(order.totalCharge)}
                          </td>
                          {this.props.isAdmin &&
                            <td className="currency">{order.profit}</td>}
                          <td>
                            {order.statusName}
                          </td>
                          <td>
                            {(order.packageTypeName || '').toUpperCase()}
                          </td>
                          <td>
                            <Button
                              bsStyle="primary"
                              bsSize="xsmall"
                              onClick={e => {
                                this.props.addTab({
                                  orderId: order.id,
                                  type: 'ORDER_VIEW',
                                });
                                browserHistory.push('/shipments');
                              }}
                            >
                              View
                            </Button>
                          </td>
                        </tr>
                      ))}
                  </tbody>
                </table>
                {Object.keys(this.props.invoice).length &&
                  this.state.paymentModal &&
                  <InvoicePaymentModal
                    invoiceList={[this.props.invoice]}
                    show={this.state.paymentModal}
                    togglePaymentModal={this.togglePaymentModal}
                    reloadInvoiceView={this.props.invoice.id}
                  />}

                {this.state.paymentHistoryModal &&
                  <PaymentHistoryModal
                    showModal={this.state.paymentHistoryModal}
                    toggleModal={() =>
                      this.setState({ paymentHistoryModal: false })}
                    paymentHistory={this.props.invoice.paymentHistory}
                  />}
              </div>
            </Col>
          </Row>

          <Row>
            <Col className="col" sm={12} xs={12}>
              <hr className="simple" />
              <h3>
                <span>Terms</span>
              </h3>
              <div>
                <span>
                  {this.props.invoiceTerms}
                </span>
              </div>
            </Col>
          </Row>

          <Row>
            <Col className="col" sm={12} xs={12}>
              <hr className="simple" />
              <h3>
                <span>Notes</span>
              </h3>
              <div>
                {this.renderInvoiceOrderLogs()}
              </div>
            </Col>
          </Row>

          <footer className="form-actions">
            {this.props.invoice.paymentHistory &&
              this.props.invoice.paymentHistory.length > 0 &&
              <Button
                bsStyle="success"
                bsSize="large"
                onClick={e => this.setState({ paymentHistoryModal: true })}
              >
                Payment History
              </Button>}
            <Button bsSize="large" bsStyle="primary">
              Print
            </Button>
            {!this.props.invoice.paymentStatus &&
              <Button
                bsStyle="success"
                bsSize="large"
                onClick={this.togglePaymentModal}
              >
                Pay Now...
              </Button>}
          </footer>
        </div>
      );
    }
  }

  renderInvoiceOrderLogs = () => {
    const { invoiceOrderLogs } = this.props;
    return (
      <ul>
        {invoiceOrderLogs && invoiceOrderLogs.length
          ? invoiceOrderLogs.map((event, index) => {
              return (
                <li key={'le' + index}>
                  Order #{event.orderId}: {event.comment}
                </li>
              );
            })
          : ''}
      </ul>
    );
  };

  togglePaymentModal = () => {
    this.setState({ paymentModal: !this.state.paymentModal });
  };
}

const mapStateToProps = (state, ownProps) => {
  const invoice = selectOneFromEntities(state, 'invoice', ownProps.params.id);

  return {
    invoice,
    loadStatus: checkLoadStatus(state, 'invoice', ownProps.params.id),
    invoiceOrderLogs: invoice.messages,
    isAdminOrAgent: checkRoleIfAdminOrAgent(state),
    isAdmin: checkRoleIfFreightcom(state),
    invoiceTerms: getSystemProperty(state, 'invoice_terms'),
  };
};

const mapDispatchToProps = {
  loadInvoiceById,
  viewInvoice,
  addTab,
  removeTab,
  loadSettings,
};

export default connect(mapStateToProps, mapDispatchToProps)(ViewInvoice);
