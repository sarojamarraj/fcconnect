import React, { Component } from 'react';

import { connect } from 'react-redux';
import AdminDashboard from '../containers/dashboard/AdminDashboard';
import AgentDashboard from '../containers/dashboard/AgentDashboard';
import StaffDashboard from '../containers/dashboard/StaffDashboard';
import CustomerAdminDashboard
  from '../containers/dashboard/CustomerAdminDashboard';
import CustomerStaffDashboard
  from '../containers/dashboard/CustomerStaffDashboard';

class Dashboard extends Component {
  render() {
    return (
      <div>
        {this.renderDashboard()}
      </div>
    );
  }

  renderDashboard() {
    const { activeRole } = this.props;

    let Dashboard;
    switch (true) {
      case activeRole.roleName === 'ADMIN':
        //console.log('ADMIN');
        Dashboard = AdminDashboard;
        break;
      case activeRole.roleName === 'AGENT':
        //console.log('AGENT');
        Dashboard = AgentDashboard;
        break;
      case activeRole.roleName === 'FREIGHTCOM_STAFF':
        //console.log('FREIGHTCOM_STAFF');
        Dashboard = StaffDashboard;
        break;
      case activeRole.roleName === 'CUSTOMER_ADMIN':
        //console.log('CUSTOMER_ADMIN');
        Dashboard = CustomerAdminDashboard;
        break;
      case activeRole.roleName === 'CUSTOMER_STAFF':
        //console.log('CUSTOMER_STAFF');
        Dashboard = CustomerStaffDashboard;
        break;
      default:
        //console.log('CUSTOMER_STAFF_AS_DEFAULT');
        Dashboard = CustomerStaffDashboard;
    }

    return <Dashboard />;
  }
}

const mapStateToProps = state => {
  const { loggedIn: { role = {} } } = state;

  return { activeRole: role };
};

export default connect(mapStateToProps)(Dashboard);
