import React, { Component } from 'react';
import { Nav, NavItem, Tab } from 'react-bootstrap';
import { connect } from 'react-redux';

import ProfileForm from './ProfileForm';
import GeneralSettingsForm from './GeneralSettingsForm';
import CreditBillingForm from './CreditBillingForm';
import ExpensesForm from './ExpensesForm';
import CustomerCredits from './CustomerCredits';
import UserTab from '../../containers/account/UserTab';

class EditCustomerForm extends Component {
  render() {
    const { customerId, tab = false } = this.props;
    if (!customerId) {
      return null;
    }
    return (
      <Tab.Container
        generateChildId={(key, type) => key}
        id="customer-details-tabs"
        defaultActiveKey={tab || 'tab-content-profile'}
      >
        <div>
          <Nav bsStyle="tabs" className="nav nav-tabs bordered">
            <NavItem eventKey="tab-content-profile">
              <i className="fa fa-fw fa-md fa-building" />Company Profile
            </NavItem>
            <NavItem eventKey="tab-content-settings">
              <i className="fa fa-fw fa-md fa-gears" />General Settings
            </NavItem>
            <NavItem eventKey="tab-content-billing">
              <i className="fa fa-fw fa-md fa-credit-card" />Credit and Billing
            </NavItem>
            <NavItem eventKey="tab-content-expenses" className="hidden">
              <i className="fa fa-fw fa-md fa-money" />Legacy Settings
            </NavItem>
            <NavItem eventKey="tab-content-users">
              <i className="fa fa-fw fa-md fa-users" />Users
            </NavItem>
            <NavItem eventKey="tab-content-credits">
              <i className="fa fa-fw fa-md fa-dollar" /> Credits
            </NavItem>
          </Nav>
          <Tab.Content
            id="customer-details-tabs"
            className="tab-content"
            animation
          >
            <Tab.Pane eventKey="tab-content-profile">
              <ProfileForm customerId={customerId} />
            </Tab.Pane>
            <Tab.Pane eventKey="tab-content-settings">
              <GeneralSettingsForm customerId={customerId} />
            </Tab.Pane>
            <Tab.Pane eventKey="tab-content-billing">
              <CreditBillingForm customerId={customerId} />
            </Tab.Pane>
            <Tab.Pane eventKey="tab-content-expenses">
              <ExpensesForm customerId={customerId} />
            </Tab.Pane>
            <Tab.Pane eventKey="tab-content-users">
              <UserTab customerId={customerId} />
            </Tab.Pane>
            <Tab.Pane eventKey="tab-content-credits">
              <CustomerCredits customerId={customerId} />
            </Tab.Pane>
          </Tab.Content>
        </div>
      </Tab.Container>
    );
  }
}

export default connect(null, null)(EditCustomerForm);
