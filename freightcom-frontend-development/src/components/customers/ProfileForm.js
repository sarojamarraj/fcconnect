import React, { Component } from 'react';
import { connect } from 'react-redux';
import { reduxForm, formValueSelector } from 'redux-form';
import { compose } from 'redux';
import { browserHistory } from 'react-router';
import { updateCustomer } from '../../actions/customers';
import ProfileTab from '../../containers/account/ProfileTab';
import Button from 'react-bootstrap-button-loader';
import { addNotification } from 'reapop';

class ProfileForm extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isSubmitting: false,
    };
  }
  render() {
    const { selectedCountry } = this.props;
    return (
      <form
        id="customer-info"
        className="smart-form"
        onSubmit={this.props.handleSubmit(this.submit)}
      >
        <ProfileTab selectedCountry={selectedCountry} />
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
            (this.props.activeRole.roleName !== 'CUSTOMER_STAFF' &&
              <Button
                className="btn btn-default btn-lg"
                type="button"
                onClick={e => browserHistory.push('/customers')}
              >
                Back to Customer List
              </Button>)}
        </footer>
      </form>
    );
  }

  submit = values => {
    const { updateCustomer, customerId, version } = this.props;
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
  const selector = formValueSelector('customer-profile');
  const selectedCountry = selector(state, 'country');
  const { entities: { customer }, loggedIn: { role = {} } } = state;
  const initialValues = customer[ownProps.customerId] || {};

  return {
    selectedCountry,
    initialValues,
    activeRole: role,
    version: initialValues.version,
  };
};

export default compose(
  connect(mapStateToProps, { updateCustomer, addNotification }),
  reduxForm({ form: 'customer-profile' }),
)(ProfileForm);
