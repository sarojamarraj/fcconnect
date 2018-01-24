import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Nav, NavItem } from 'react-bootstrap';
import { browserHistory } from 'react-router';
import Widget from '../components/Widget';
import Icon from '../components/Icon';
import InvoiceTable from '../components/invoices/InvoiceTable';

import {
  addInvoiceTab,
  removeInvoiceTab,
  activateInvoiceTab,
} from '../actions/tabs';

class Invoices extends Component {
  componentWillMount() {
    this.props.params['id']
      ? this.props.addInvoiceTab(this.props.params['id'])
      : this.props.activateInvoiceTab();
  }

  componentWillUpdate(nextProps) {
    nextProps.params['id']
      ? this.props.addInvoiceTab(nextProps.params['id'])
      : this.props.activateInvoiceTab();
  }

  render() {
    return (
      <Widget title="Invoices">
        <Nav
          bsStyle="tabs"
          className="nav nav-tabs bordered "
          onSelect={selectedKey => {
            if (selectedKey === 'tab-content-list') {
              browserHistory.push('/invoices');
            } else {
              browserHistory.push('/invoices/' + selectedKey);
            }
          }}
        >
          <NavItem
            active={!this.props.params['id']}
            eventKey="tab-content-list"
          >
            <Icon name="list" /> Invoice List
          </NavItem>
          {this.props.openTabs.map(item => {
            return (
              <NavItem
                eventKey={item}
                key={item}
                active={this.props.params['id'] === item}
              >
                Invoice: {item}
                <span
                  className="tab-close"
                  onClick={e => {
                    e.preventDefault();
                    e.stopPropagation();
                    if (item === this.props.params['id']) {
                      browserHistory.push('/invoices');
                    }
                    this.props.removeInvoiceTab(item);
                  }}
                >
                  <Icon name="close" />
                </span>
              </NavItem>
            );
          })}
        </Nav>
        <br />
        <div style={{ display: this.props.params['id'] ? 'none' : 'block' }}>
          <InvoiceTable />
        </div>
        {this.props.params['id'] && this.props.children}

      </Widget>
    );
  }
}

const mapStateToProps = state => {
  return {
    openTabs: state.tabs.invoices.openTabs,
  };
};

const mapDispatchToProps = {
  activateInvoiceTab,
  addInvoiceTab,
  removeInvoiceTab,
};

export default connect(mapStateToProps, mapDispatchToProps)(Invoices);
