import React, { Component } from 'react';
import { connect } from 'react-redux';
import { reduxForm } from 'redux-form';
import { compose, reset } from 'redux';
import { updateCustomer } from '../../actions/customers';
import ExpenseTab from '../../containers/account/ExpenseTab';
import Button from 'react-bootstrap-button-loader';
import { addNotification } from 'reapop';

class ExpensesForm extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isSubmitting: false,
    };
  }
  render() {
    const { activeRole } = this.props;
    return (
      <form
        id="customer-info"
        className="smart-form"
        onSubmit={this.props.handleSubmit(this.submit)}
      >
        <ExpenseTab activeRole={activeRole} />
        <hr className="simple" />
        <footer className="clearfix">
          <Button
            type="submit"
            className="btn btn-primary btn-lg"
            loading={this.state.isSubmitting}
          >
            Save
          </Button>
          <button
            className="btn btn-default btn-lg"
            type="button"
            onClick={this.props.reset}
          >
            Cancel
          </button>
        </footer>
      </form>
    );
  }

  submit = values => {
    const {
      updateCustomer,
      customerId,
      intialValues: { version } = {},
    } = this.props;
    this.setState({ isSubmitting: true });
    updateCustomer(
      { ...values, version },
      customerId,
      result => {
        this.setState({ isSubmitting: false });
        this.props.addNotification({
          title: 'Saved',
          message: `Customer information has been saved.`,
          status: 'success',
          dismissible: true,
          dismissAfter: 5000,
        });
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
  reduxForm({ form: 'customer-expenses' }),
)(ExpensesForm);
