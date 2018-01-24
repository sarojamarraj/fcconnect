import React, { Component } from 'react';
import { Modal } from 'react-bootstrap';
import { formatCurrency } from '../../utils';
import { connect } from 'react-redux';
import { checkRoleIfFreightcom } from '../../reducers';
import {
  payNow,
  loadInvoiceById,
  loadInvoiceCalculation,
} from '../../actions/invoices';
import { loadCustomerById, updateCustomer } from '../../actions/customers';
import { addNotification } from 'reapop';
import { getPaymentInformation } from '../../reducers/load-status';
import Button from 'react-bootstrap-button-loader';
import CreditCardSelector from '../CreditCardSelector';

class InvoicePaymentModal extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isSubmitting: false,
      isPayable: false,
      ccInfo: {},
    };
  }

  componentWillMount() {
    this.props.loadInvoiceCalculation(this.props.invoiceList);
  }

  render() {
    const { show, togglePaymentModal } = this.props;

    return (
      <Modal show={show} onHide={togglePaymentModal}>
        <Modal.Header closeButton>
          <Modal.Title>Pay Now</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {this.renderForm()}
        </Modal.Body>
      </Modal>
    );
  }

  renderForm = () => {
    const { customer = {}, paymentInformation, message } = this.props;

    if (!customer) {
      return <div>Thinking</div>;
    }

    if (message) {
      return <div>{message}</div>;
    }

    const subTotal = paymentInformation.subTotal;
    const totalCredit = paymentInformation.credit;
    const balance = paymentInformation.balance;

    return (
      <form
        className="modalForm form-horizontal"
        onSubmit={e => {
          e.preventDefault();
          this.submit(e, paymentInformation.invoices);
        }}
      >
        <div>
          <h5>{customer.name}</h5>
          <div className="row">
            {this.renderTable(
              paymentInformation.invoices,
              subTotal,
              totalCredit,
              balance,
            )}
          </div>
          {balance > 0 &&
            <CreditCardSelector
              customer={customer}
              onChange={ccInfo => this.setState({ ccInfo })}
            />}
          <br />
        </div>
        <div className="row modalForm-footer">
          <div className="col-sm-12 col-xs-12">
            <Button
              onClick={this.props.togglePaymentModal}
              className="btn"
            >
              Cancel
            </Button>
            <Button
              type="submit"
              className="btn btn-primary"
              loading={this.state.isSubmitting}
              disabled={!this.validate()}
            >
              {subTotal - totalCredit > 0 ? 'Pay Now' : 'Continue'}
            </Button>
          </div>
        </div>
      </form>
    );
  };

  renderTable = (invoiceList, total, credit, balance) => {
    return (
      <div className="col-sm-12 col-xs-12">
        <table className="table table-bordered table-condensed">
          <thead>
            <tr>
              <th>Invoice #</th>
              <th>Date Created</th>
              <th>Due Date</th>
              <th>Paid Amount</th>
              <th>Total Amount</th>
              <th>Total Payable</th>
            </tr>
          </thead>
          <tbody>
            {invoiceList &&
              invoiceList.map((invoice, index) => {
                if (invoice.paymentStatus) {
                  return null;
                }
                return (
                  <tr key={index}>
                    <td>{invoice.id}</td>
                    <td style={{ whiteSpace: 'nowrap' }}>
                      {invoice.dateGenerated}
                    </td>
                    <td style={{ whiteSpace: 'nowrap' }}>{invoice.dueDate}</td>
                    <td className="currency">
                      {formatCurrency(invoice.paidAmount)}
                    </td>
                    <td className="currency">
                      {formatCurrency(invoice.amount)}
                    </td>
                    <td className="currency">
                      {formatCurrency(invoice.amountRemaining)}
                    </td>
                  </tr>
                );
              })}
          </tbody>
          <tfoot>
            <tr>
              <th colSpan="5">Total Payable</th>
              <th style={{ textAlign: 'right' }}>
                {formatCurrency(total)}
              </th>
            </tr>
            {credit > 0 || this.props.isAdmin
              ? <tr>
                  <th colSpan="5">Less Available Credits</th>
                  <th style={{ textAlign: 'right' }}>
                    {credit > 0 ? '- ' + formatCurrency(credit) : '0.00'}
                  </th>
                </tr>
              : null}
            <tr>
              <th colSpan="5">Actual Payable</th>
              <th style={{ textAlign: 'right' }}>
                {formatCurrency(balance)}
              </th>
            </tr>
          </tfoot>
        </table>
      </div>
    );
  };

  submit = (event, invoiceList) => {
    const { paymentInformation: { balance, invoices } } = this.props;

    let error = false;

    let paymentDetails = {
      invoices: invoices.map(invoice => invoice.id),
    };

    if (balance > 0) {
      if (event.target.useNew.value === 'new') {
        paymentDetails.cardType = event.target.ccType.value;
        paymentDetails.creditCardNumber = event.target.creditCardNumber.value;
        paymentDetails.cvs = event.target.cvs.value;
        paymentDetails.expiryMonth = event.target.expiryMonth.value;
        paymentDetails.expiryYear = event.target.expiryYear.value;
        paymentDetails.nameOnCard = event.target.nameOnCard.value;
      } else {
        const cardIndex = event.target.cardIndex.value;

        if (cardIndex) {
          paymentDetails.cardType = this.props.customer.creditCards[
            cardIndex
          ].type;
          paymentDetails.creditCardNumber = this.props.customer.creditCards[
            cardIndex
          ].number;
          paymentDetails.cvs = this.props.customer.creditCards[cardIndex].cvc;
          paymentDetails.expiryMonth = this.props.customer.creditCards[
            cardIndex
          ].expiryMonth;
          paymentDetails.expiryYear = this.props.customer.creditCards[
            cardIndex
          ].expiryYear;
          paymentDetails.nameOnCard = this.props.customer.creditCards[
            cardIndex
          ].name;
        } else {
          error = 'No card selected';
        }
      }
    }

    if (error) {
      this.props.addNotification({
        title: 'Error',
        message: error,
        status: 'error',
        dismissible: false,
        dismissAfter: 10000,
        buttons: [
          {
            name: 'OK',
            primary: true,
          },
        ],
      });
    } else {
      this.setState({ isSubmitting: true });

      this.props.payNow(paymentDetails, data => {
        this.setState({ isSubmitting: false });
        this.props.addNotification({
          title: 'Thank you for your payment',
          message: `Your payment was received.`,
          status: 'success',
          allowHTML: true,
          dismissible: false,
          dismissAfter: 10000,
          buttons: [
            {
              name: 'OK',
              primary: true,
            },
          ],
        });
        if (this.props.reloadInvoiceView) {
          this.props.loadInvoiceById(this.props.reloadInvoiceView);
        }
        this.props.togglePaymentModal();
        this.props.resetCheckBoxes();
        this.props.loadInvoices(
          this.props.currentPage,
          this.props.itemsPerPage,
          this.props.filters,
          this.props.sortOrder,
        );
      });
    }
  };

  validate = () => {
    const { paymentInformation: { subTotal, credit } } = this.props;
    const { ccInfo } = this.state;

    if (subTotal <= credit) {
      return true;
    }

    if (ccInfo.useNew === 'new') {
      if (
        ccInfo.creditCardNumber &&
        ccInfo.cvs &&
        ccInfo.expiryMonth &&
        ccInfo.expiryYear &&
        ccInfo.nameOnCard
      ) {
        return true;
      }
      return false;
    }

    return ccInfo.cardIndex ? true : false;
  };
}

const mapStateToProps = (state, ownProps) => {
  const { status, data = {}, message } = getPaymentInformation(state);

  return {
    isAdmin: checkRoleIfFreightcom(state),
    customer: data.customer,
    status,
    paymentInformation: data,
    message,
  };
};

const mapDispatchToProps = {
  payNow,
  addNotification,
  loadCustomerById,
  updateCustomer,
  loadInvoiceById,
  loadInvoiceCalculation,
};

export default connect(mapStateToProps, mapDispatchToProps)(
  InvoicePaymentModal,
);
