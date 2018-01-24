import React, { Component } from 'react';
import { Nav, NavItem, Tab } from 'react-bootstrap';
import Widget from '../components/Widget';
import SettingsTab from '../containers/settings/SettingsTab';
import Currencies from '../containers/settings/Currencies';

class Settings extends Component {
  render() {
    return (
      <Widget title="Manage Settings">
        <Tab.Container
          generateChildId={(key, type) => key}
          id="setting-tabs"
          defaultActiveKey="tab-content-setting1"
        >
          <div>
            <Nav bsStyle="tabs" className="nav nav-tabs bordered">
              <NavItem eventKey="tab-content-setting1">
                <i className="fa fa-fw fa-md fa-gears" />Settings
              </NavItem>
              <NavItem eventKey="tab-content-setting2">
                <i className="fa fa-fw fa-md fa-heartbeat" />Diagnostics
              </NavItem>
              <NavItem eventKey="tab-content-currencies">
                <i className="fa fa-fw fa-md fa-dollar" /> Currencies
              </NavItem>
            </Nav>
            <Tab.Content
              id="customer-details-tabs"
              className="tab-content"
              animation
            >
              <Tab.Pane eventKey="tab-content-setting1">
                <SettingsTab />
              </Tab.Pane>
              <Tab.Pane eventKey="tab-content-setting2">
                <div>bbb</div>
              </Tab.Pane>
              <Tab.Pane eventKey="tab-content-currencies">
                <Currencies />
              </Tab.Pane>
            </Tab.Content>
          </div>
        </Tab.Container>
      </Widget>
    );
  }
}

export default Settings;
