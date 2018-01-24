import React, { Component } from 'react';
import { Modal } from 'react-bootstrap';
import { formatCurrency } from '../../utils';
import { connect } from 'react-redux';
import { checkRoleIfAdminOrAgent } from '../../reducers';
import { enterPayments } from '../../actions/invoices';
import { addNotification, updateNotification } from 'reapop';
import Button from 'react-bootstrap-button-loader';

class EnterPaymentsModal extends Component {
  constructor(props) {
    super(props);
    this.state = { validate: false, isSubmitting: false };
  }

  render() {
    const { show, toggleEnterPaymentsModal, invoiceList } = this.props;
    return (
      <Modal show={show} onHide={toggleEnterPaymentsModal} bsSize="large">
        <Modal.Header closeButton>
          <Modal.Title>Enter Payments</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <form
            className="modalForm form-horizontal"
            // action="/abcd"
            // method="POST"
            onSubmit={this.submit}>
            <div className="row">
              <div className="col-sm-12 col-xs-12">
                <table className="table table-bordered table-condensed">
                  <thead>
                    <tr>
                      <th>Invoice #</th>
                      <th>Customer</th>
                      <th>Due Date</th>
                      <th>Paid Amount</th>
                      <th>Total Amount</th>
                      <th>Total Payable</th>
                      <th>Payment Type</th>
                      <th>Amount</th>
                      <th>Reference #</th>
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
                            <td>{invoice.customer.name}</td>
                            <td>{invoice.dueDate}</td>
                            <td className="currency">
                              {formatCurrency(invoice.paidAmount)}
                            </td>
                            <td className="currency">
                              {formatCurrency(invoice.amount)}
                            </td>
                            <td className="currency">
                              {formatCurrency(invoice.amountRemaining)}
                            </td>
                            <td>
                              {' '}
                              <select
                                className="form-control"
                                name={`paymentType${index}`}
                                ref={`paymentType${index}`}>
                                <option value="wireTransfer">
                                  Wire Transfer
                                </option>
                                <option value="check">Check</option>
                                <option value="cash">Cash</option>
                              </select>
                            </td>
                            <td>
                              <input
                                type="text"
                                className="form-control"
                                name={`amount${index}`}
                                ref={`amount${index}`}
                                onChange={e => {
                                  e.preventDefault();
                                  this.validate(e.target.value, index);
                                }}
                                maxLength="8"
                                size="6"
                              />
                            </td>
                            <td>
                              <input
                                type="text"
                                className="form-control"
                                name={`referenceCode${index}`}
                                ref={`referenceCode${index}`}
                                maxLength="12"
                                size="10"
                              />
                            </td>
                          </tr>
                        );
                      })}
                  </tbody>
                </table>
              </div>
            </div>
            <div className="row modalForm-footer">
              <div className="col-sm-12 col-xs-12">
                <Button
                  onClick={toggleEnterPaymentsModal}
                  className="btn">
                  Cancel
                </Button>
                <Button
                  type="submit"
                  className="btn btn-primary"
                  disabled={!this.state.validate}
                  loading={this.state.isSubmitting}>
                  Pay
                </Button>
              </div>
            </div>
          </form>
        </Modal.Body>
      </Modal>
    );
  }

  validate = (value, index) => {
    const payableInvoiceList = this.props.invoiceList.filter(
      invoice => invoice.paymentStatus !== 1,
    );
    const { amountRemaining } = payableInvoiceList[index];
    value = value ? Number(value) : 0;
    this.setState({
      validate: amountRemaining - value >= 0 && value !== 0 ? true : false,
    });
  };

  calculateTotal = invoiceList => {
    let total = 0;
    for (var invoice of invoiceList) {
      total += invoice.amountRemaining;
    }
    return formatCurrency(total);
  };

  submit = event => {
    event.preventDefault();
    this.setState({ isSubmitting: true });
    const payableInvoiceList = this.props.invoiceList.filter(
      invoice => invoice.paymentStatus !== 1,
    );
    const payments = [];

    for (let index = 0; index < payableInvoiceList.length; index++) {
      if (
        this.refs[`amount${index}`].value &&
        this.refs[`amount${index}`].value > 0
      ) {
        payments.push({
          paymentType: this.refs[`paymentType${index}`].value,
          payment: Number(this.refs[`amount${index}`].value),
          invoices: [payableInvoiceList[index].id],
        });
      }
    }

    if (payments.length > 0) {
      let notif = this.props.addNotification({
        title: 'Proceeding payment as admin',
        message: 'Sending request...',
        status: 'info',
        dismissible: false,
        dismissAfter: 0,
      });
      this.props.enterPayments(payments, data => {
        notif.status = 'success';
        notif.message = 'Payment has been updated.';
        notif.dismissible = true;
        notif.dismissAfter = 3000;
        this.props.updateNotification(notif);
        this.props.resetCheckBoxes();
        this.props.loadInvoices(
          this.props.currentPage,
          this.props.itemsPerPage,
          this.props.filters,
          this.props.sortOrder,
        );
        this.setState({ isSubmitting: false });
        this.props.toggleEnterPaymentsModal();
      });
    }
  };
}

const mapStateToProps = state => {
  return { isAdminOrAgent: checkRoleIfAdminOrAgent(state) };
};

const mapDispatchToProps = {
  enterPayments,
  addNotification,
  updateNotification,
};

export default connect(mapStateToProps, mapDispatchToProps)(EnterPaymentsModal);
