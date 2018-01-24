import React, { Component } from 'react';
import { connect } from 'react-redux';
import Widget from '../components/Widget';
import EditCustomerForm from '../components/customers/EditCustomerForm';

import { selectLoggedInCustomer } from '../reducers';

class Accounts extends Component {
  render() {
    return (
      <Widget title="Account Information">
        <EditCustomerForm customerId={this.props.customerId} />
      </Widget>
    );
  }
}

const mapStateToProps = state => {
  return {
    customerId: selectLoggedInCustomer(state).id,
  };
};

export default connect(mapStateToProps)(Accounts);
