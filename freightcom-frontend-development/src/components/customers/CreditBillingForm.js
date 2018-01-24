import React, { Component } from 'react';
import { connect } from 'react-redux';
import { reduxForm } from 'redux-form';
import { compose, reset } from 'redux';
import { updateCustomer } from '../../actions/customers';
import CreditBillingTab from '../../containers/account/CreditBillingTab';
import Button from 'react-bootstrap-button-loader';
import { addNotification } from 'reapop';

class CreditBillingForm extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isSubmitting: false,
    };
  }
  render() {
    const { activeRole, customerId } = this.props;
    return (
      <form
        id="customer-info"
        className="smart-form"
        onSubmit={this.props.handleSubmit(this.submit)}
      >
        <CreditBillingTab activeRole={activeRole} customerId={customerId} />
        <hr className="simple" />
        <footer className="clearfix">
          <Button
            type="submit"
            className="btn btn-primary btn-lg"
            loading={this.state.isSubmitting}
          >
            Save
          </Button>
          {this.props.activeRole.roleName !== 'CUSTOMER_ADMIN' &&
            this.props.activeRole.roleName !== 'CUSTOMER_STAFF' &&
            <button
              className="btn btn-default btn-lg"
              type="button"
              onClick={this.props.reset}
            >
              Cancel
            </button>}
        </footer>
      </form>
    );
  }

  notifySaveSuccess = () => {
    this.props.addNotification({
      title: 'Saved',
      message: `Customer information has been saved.`,
      status: 'success',
      dismissible: true,
      dismissAfter: 5000,
    });
  };

  submit = values => {
    const { updateCustomer, customerId } = this.props;

    this.setState({ isSubmitting: true });
    updateCustomer(
      {
        ccReceipt: values.ccReceipt,
        version: this.props.initialValues.version,
      },
      customerId,
      result => {
        this.setState({ isSubmitting: false });
        this.notifySaveSuccess();
      },
      result => {
        this.setState({ isSubmitting: false });
        this.props.addNotification({
          title: 'Error',
          message: `Error occurred while saving customer information. Please try again`,
          status: 'error',
          dismissible: true,
          dismissAfter: 5000,
        });
      },
    );
  };
}

const mapStateToProps = (state, ownProps) => {
  const { entities: { customer }, loggedIn: { role = {} } } = state;
  const initialValues = customer[ownProps.customerId];

  return { initialValues, activeRole: role };
};

export default compose(
  connect(mapStateToProps, { updateCustomer, reset, addNotification }),
  reduxForm({ form: 'customer-credit-billing' }),
)(CreditBillingForm);
