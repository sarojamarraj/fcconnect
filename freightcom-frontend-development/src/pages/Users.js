import React, { Component } from 'react';
import { connect } from 'react-redux';
import Widget from '../components/Widget';
import UserTable from '../components/users/UserTable';
import CustomerUserTable from '../components/users/CustomerUserTable';

class Users extends Component {
  render() {
    const { activeRole, customerId } = this.props;
    return (
      <Widget title="Manage Users">
        {activeRole.roleName === 'ADMIN'
          ? <UserTable />
          : <CustomerUserTable customerId={customerId} />}
      </Widget>
    );
  }
}

const mapStateToProps = state => {
  const { loggedIn: { role = {} } } = state;
  return { activeRole: role, customerId: role.customerId || null };
};

export default connect(mapStateToProps)(Users);
