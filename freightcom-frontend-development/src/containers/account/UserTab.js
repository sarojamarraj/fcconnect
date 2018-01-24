import React, { Component } from 'react';
import CustomerUserTable from '../../components/users/CustomerUserTable';

class UserTab extends Component {
  render() {
    return (
      <CustomerUserTable
        renderTableFilters={false}
        customerId={this.props.customerId}
      />
    );
  }
}

export default UserTab;
