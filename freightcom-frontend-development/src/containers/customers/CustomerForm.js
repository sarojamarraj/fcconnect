import React, { Component } from 'react';
import Widget from '../../components/Widget';
import NewCustomerForm from '../../components/customers/NewCustomerForm';

class CustomerForm extends Component {
  render() {
    return (
      <Widget title="New Customer">
        <NewCustomerForm />
      </Widget>
    );
  }
}

export default CustomerForm;
