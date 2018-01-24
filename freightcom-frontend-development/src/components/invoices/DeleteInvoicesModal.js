import React, { Component } from 'react';
import { Modal } from 'react-bootstrap';
import { formatCurrency } from '../../utils';
import { reduxForm } from 'redux-form';
import { connect } from 'react-redux';
import { compose } from 'redux';
import { deleteInvoices } from '../../actions/invoices';
import { addNotification, updateNotification } from 'reapop';
import { checkRoleIfAdminOrAgent } from '../../reducers';
import Button from 'react-bootstrap-button-loader';

class DeleteInvoicesModal extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isSubmitting: false,
    };
  }
  render() {
    const { show, toggleDeleteModal, invoiceList } = this.props;
    return (
      <Modal show={show} onHide={toggleDeleteModal} bsSize="large">
        <Modal.Header closeButton>
          <Modal.Title>Cancel Invoices</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <form
            className="modalForm form-horizontal"
            onSubmit={this.props.handleSubmit(this.submit)}>
            <div className="row">
              <div className="col-sm-12 col-xs-12">
                <table className="table table-bordered table-condensed">
                  <thead>
                    <tr>
                      <th>Invoice #</th>
                      <th>Invoice Date</th>
                      <th>Customer</th>
                      <th>Total Charges</th>
                      <th>Refund Amount</th>
                    </tr>
                  </thead>
                  <tbody>
                    {invoiceList &&
                      invoiceList.map((invoice, index) => {
                        return (
                          <tr key={index}>
                            <td>{invoice.id}</td>
                            <td>
                              {invoice.dateGenerated
                                ? invoice.dateGenerated
                                : invoice.createdAt}
                            </td>
                            <td>{invoice.customer.name}</td>
                            <td className="currency">
                              {formatCurrency(invoice.amount)}
                            </td>
                            <td className="currency">
                              {formatCurrency(invoice.creditedAmount + invoice.paidAmount)}
                            </td>
                          </tr>
                        );
                      })}
                  </tbody>
                </table>
              </div>
            </div>
            <div className="row">
              <div className="col-sm-12 col-xs-12">
                Deleting these invoices will reset related charges to unbilled
                and remove the invoices from the Customer dashboard. Any payable
                commission will be cancelled and payments made towards these
                invoices will be converted to customer credit.
              </div>
            </div>
            <div className="row modalForm-footer">
              <div className="col-sm-12 col-xs-12">
                <Button onClick={toggleDeleteModal} className="btn">
                  Cancel
                </Button>
                <Button
                  type="submit"
                  className="btn btn-danger"
                  loading={this.state.isSubmitting}>
                  Cancel Invoice
                </Button>
              </div>
            </div>
          </form>
        </Modal.Body>
      </Modal>
    );
  }

  renderTable = () => {};

  calculateTotal = invoiceList => {
    let total = 0;
    for (var invoice of invoiceList) {
      total += invoice.paidAmount;
    }
    return formatCurrency(total);
  };

  submit = values => {
    this.setState({ isSubmitting: true });
    const {
      invoiceList,
      deleteInvoices,
      toggleDeleteModal,
      addNotification,
      updateNotification,
    } = this.props;
    let notif = addNotification({
      title: 'Cancel Invoices',
      message: 'Sending request...',
      status: 'info',
      dismissible: false,
      dismissAfter: 0,
    });
    deleteInvoices(
      {
        invoices: invoiceList.map(invoice => {
          return invoice.id;
        }),
      },
      data => {
        notif.title = 'Invoices cancelled';
        notif.status = 'success';
        notif.message =
          'Selected invoices have been cancelled. Relevant charges have been reset to unbilled. ';
        notif.dismissible = true;
        notif.dismissAfter = 3000;
        updateNotification(notif);

        this.props.loadInvoices(
          this.props.currentPage,
          this.props.itemsPerPage,
          this.props.filters,
          this.props.sortOrder,
        );
        this.setState({ isSubmitting: false });
        this.props.resetCheckBoxes();
        toggleDeleteModal();
      },
    );
  };
}

const mapStateToProps = state => {
  return { isAdminOrAgent: checkRoleIfAdminOrAgent(state) };
};

const mapDispatchToProps = {
  deleteInvoices,
  addNotification,
  updateNotification,
};

export default compose(
  connect(mapStateToProps, mapDispatchToProps),
  reduxForm({ form: 'delete-invoices' }),
)(DeleteInvoicesModal);
