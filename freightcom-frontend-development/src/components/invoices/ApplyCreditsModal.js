import React, { Component } from 'react';
import { Modal } from 'react-bootstrap';
import { formatCurrency } from '../../utils';
import { connect } from 'react-redux';
import { checkRoleIfAdminOrAgent } from '../../reducers';
import {
  applyCredit,
  loadCreditAppliedView,
  loadInvoiceById,
} from '../../actions/invoices';
import { addNotification } from 'reapop';
import Button from 'react-bootstrap-button-loader';

class ApplyCreditsModal extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isSubmitting: false,
    };
  }

  componentWillMount() {
    const { invoiceList: [invoice] = [] } = this.props;

    if (invoice) {
      this.loadView(invoice);
    }
  }

  componentWillReceiveProps(nextProps) {
    const { invoiceList: [invoice] = [] } = nextProps;
    const { invoiceList: [prevInvoice] = [] } = this.props;

    if (invoice !== prevInvoice && invoice) {
      this.loadView(invoice);
    }
  }

  loadView = invoice =>
    this.props.loadCreditAppliedView(invoice).then(json => this.setState(json));

  render() {
    const { show, toggleApplyCreditsModal, invoiceList } = this.props;
    const {
      amount,
      amountRemaining,
      creditAvailable,
      newAmountDue,
      totalApplied,
    } = this.state;

    return (
      <Modal show={show} onHide={toggleApplyCreditsModal} bsSize="large">
        <Modal.Header closeButton>
          <Modal.Title>Apply Credits</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <form
            className="modalForm form-horizontal"
            action="/abcd"
            method="POST"
            onSubmit={this.submit}
          >
            <div className="row">
              <div className="col-sm-12 col-xs-12">
                <table className="table table-bordered table-condensed">
                  <thead>
                    <tr>
                      <th>Invoice #</th>
                      <th>Customer</th>
                      <th>Due Date</th>
                      <th>Total Amount</th>
                      <th>Paid Amount</th>
                      <th>Total Payable</th>
                      <th>Available Credit</th>
                      <th>New Amount Due</th>
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
                              {invoice.customer.name}
                            </td>
                            <td style={{ whiteSpace: 'nowrap' }}>
                              {invoice.dueDate}
                            </td>
                            <td className="currency">
                              {formatCurrency(amount)}
                            </td>
                            <td className="currency">
                              {formatCurrency(totalApplied)}
                            </td>
                            <td className="currency">
                              {formatCurrency(amountRemaining)}
                            </td>
                            <td className="currency">
                              {formatCurrency(creditAvailable)}
                            </td>
                            <td className="currency">
                              {formatCurrency(newAmountDue)}
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
                  onClick={toggleApplyCreditsModal}
                  className="btn"
                >
                  Cancel
                </Button>
                <Button
                  type="submit"
                  className="btn btn-primary"
                  disabled={creditAvailable <= 0}
                  loading={this.state.isSubmitting}
                >
                  Apply Credits
                </Button>
              </div>
            </div>
          </form>
        </Modal.Body>
      </Modal>
    );
  }

  calculateTotal = invoiceList => {
    let total = 0;
    for (var invoice of invoiceList) {
      total += invoice.amountRemaining;
    }
    return formatCurrency(total);
  };

  submit = event => {
    const {
      applyCredit,
      loadInvoiceById,
      addNotification,
      invoiceList,
      resetCheckBoxes,
      toggleApplyCreditsModal,
    } = this.props;

    event.preventDefault();
    this.setState({ isSubmitting: true });
    applyCredit({ invoice: invoiceList[0].id }, data => {
      loadInvoiceById(invoiceList[0].id);
      addNotification({
        title: 'Credit has been applied',
        message: `A ${formatCurrency(
          (data && data.length && data[0].creditedAmount) || 0,
        )} credit has been applied on invoice, ${invoiceList[0].id}`,
        status: 'success',
        dismissible: false,
        dismissAfter: 5000,
      });
      this.setState({ isSubmitting: false });
      resetCheckBoxes();
      toggleApplyCreditsModal();
    });
  };
}

const mapStateToProps = state => {
  return { isAdminOrAgent: checkRoleIfAdminOrAgent(state) };
};

const mapDispatchToProps = {
  applyCredit,
  addNotification,
  loadCreditAppliedView,
  loadInvoiceById,
};

export default connect(mapStateToProps, mapDispatchToProps)(ApplyCreditsModal);
