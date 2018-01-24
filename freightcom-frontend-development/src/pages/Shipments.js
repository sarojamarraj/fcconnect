import React, { Component } from 'react';
import { Nav, NavItem, Tab, Popover } from 'react-bootstrap';
import { connect } from 'react-redux';

import ShipmentList from '../containers/orders/ShipmentList';
import DraftList from '../containers/orders/DraftList';
import JobBoard from '../containers/orders/JobBoard';
import DisputeList from '../containers/orders/DisputeList';
import ShipmentForm from '../containers/orders/ShipmentForm';
import ConfirmOrder from '../containers/orders/ConfirmOrder';
import ConfirmWorkOrder from '../containers/orders/ConfirmWorkOrder';
import OrderView from '../containers/orders/OrderView';
import ClaimList from '../containers/orders/Claims';

import { shipmentFormState } from '../reducers/shipment-form.js';

import Widget from '../components/Widget';
import Icon from '../components/Icon';

import { addTab, removeTab, activateTab, updateTabType } from '../actions/tabs';

import {
  checkRoleIfFreightcom,
  checkIfUserIsAllowedToOrder,
  checkIfUserCanManageClaims,
  checkIfUserCanManageDisputes,
  hasAddressBook,
} from '../reducers';

import {
  loadPackageTypes,
  loadOrderStatuses,
  loadCarrierList,
} from '../actions/orders';

import { checkAddressBook } from '../actions/addressBook';

import { loadCurrencyList } from '../actions/currencies';

export const ORDER_QUOTE = 'ORDER_QUOTE';
export const ORDER_RATES = 'ORDER_RATES';
export const ORDER_CONFIRM = 'ORDER_CONFIRM';
export const WORK_ORDER_CONFIRM = 'WORK_ORDER_CONFIRM';
export const ORDER_VIEW = 'ORDER_VIEW';
export const UPDATE_STATUS = 'UPDATE_STATUS';

class Shipments extends Component {
  constructor(props) {
    super(props);

    this.state = {
      filterDataLoaded: false,
    };

    this.shipmentList = null;
    this.draftList = null;
    this.jobBoard = null;
    this.disputeList = null;
    this.claimList = null;
    this.disallowOrdersPopover = (
      <Popover id="disallowOrdersPopover" title="Attention!">
        Currently your account doesn{"'"}t allow placement of new orders.
      </Popover>
    );
  }

  componentWillMount() {
    this.props.checkAddressBook();
    this.props.loadPackageTypes();
    this.props.loadOrderStatuses();
    this.props.loadCurrencyList();
  }

  loadFilterData = () => {
    if (!this.state.filterDataLoaded) {
      this.setState(
        {
          filterDataLoaded: true,
        },
        () => {
          this.props.loadCarrierList();
        },
      );
    }
  };

  render() {
    const { activeTab, openTabs, activateTab } = this.props;

    return (
      <Widget title="Shipments">
        <Tab.Container
          generateChildId={(key, type) => key}
          id="customer-order-tabs"
          activeKey={activeTab}
          onSelect={activateTab}
        >
          <div>
            <Nav bsStyle="tabs" className="nav nav-tabs bordered">
              <NavItem eventKey="tab-content-search">
                <Icon name="truck" /> Shipments
              </NavItem>
              <NavItem eventKey="tab-content-drafts">
                <Icon name="edit" /> Quotes
              </NavItem>
              {this.props.isFreightcom &&
                <NavItem eventKey="tab-content-jobs">
                  <Icon name="clipboard" /> Job Board
                </NavItem>}
              {this.props.canManageClaims &&
                <NavItem eventKey="tab-content-claims">
                  <Icon name="file" /> Claims
                </NavItem>}
              {this.props.canManageDisputes &&
                <NavItem eventKey="tab-content-dispute">
                  <Icon name="gavel" /> Disputes
                </NavItem>}
              {openTabs.map(this.renderTabLink)}
            </Nav>
            <Tab.Content
              id="customer-order-tabs-content"
              className="tab-content"
              animation
            >
              <Tab.Pane eventKey="tab-content-search">
                <ShipmentList
                  active={this.props.activeTab === 'tab-content-search'}
                  ref={datatable => (this.shipmentList = datatable)}
                  loadFilterData={this.loadFilterData}
                />
              </Tab.Pane>

              <Tab.Pane eventKey="tab-content-drafts">
                <DraftList
                  active={this.props.activeTab === 'tab-content-drafts'}
                  ref={datatable => (this.draftList = datatable)}
                  loadFilterData={this.loadFilterData}
                />
              </Tab.Pane>

              {this.props.isFreightcom &&
                <Tab.Pane eventKey="tab-content-jobs">
                  <JobBoard
                    active={this.props.activeTab === 'tab-content-jobs'}
                    ref={datatable => (this.jobBoard = datatable)}
                    loadFilterData={this.loadFilterData}
                  />
                </Tab.Pane>}

              {this.props.canManageClaims &&
                <Tab.Pane eventKey="tab-content-claims">
                  <ClaimList
                    active={this.props.activeTab === 'tab-content-claims'}
                    ref={datatable => (this.claimList = datatable)}
                  />
                </Tab.Pane>}

              {this.props.canManageDisputes &&
                <Tab.Pane eventKey="tab-content-dispute">
                  <DisputeList
                    active={this.props.activeTab === 'tab-content-dispute'}
                    ref={datatable => (this.disputeList = datatable)}
                    loadFilterData={this.loadFilterData}
                  />
                </Tab.Pane>}

              {openTabs.map(this.renderTabContent)}
            </Tab.Content>
          </div>
        </Tab.Container>
      </Widget>
    );
  }

  renderTabLink = (tab, index) => {
    return (
      <NavItem key={'tab-link-' + tab.id} eventKey={tab.id}>
        {tab.title}
        <span
          className="tab-close"
          onClick={e => {
            e.preventDefault();
            e.stopPropagation();
            this.props.removeTab(tab.id);
          }}
        >
          <Icon name="close" />
        </span>
      </NavItem>
    );
  };

  renderTabContent = (tab, index) => {
    if (tab.id !== this.props.activeTab) {
      return false;
    }

    switch (tab.type) {
      case ORDER_QUOTE:
        return (
          <Tab.Pane key={tab.id} eventKey={tab.id}>
            <ShipmentForm
              loadOrders={this.loadOrders}
              formState={this.props.formState}
            />
          </Tab.Pane>
        );
      case ORDER_CONFIRM:
        return (
          <Tab.Pane key={tab.id} eventKey={tab.id}>
            <ConfirmOrder loadOrders={this.loadOrders} />
          </Tab.Pane>
        );

      case ORDER_VIEW:
        return (
          <Tab.Pane key={tab.id} eventKey={tab.id}>
            <OrderView loadOrders={this.loadOrders} />
          </Tab.Pane>
        );
      case WORK_ORDER_CONFIRM:
        return (
          <Tab.Pane key={tab.id} eventKey={tab.id}>
            <ConfirmWorkOrder loadOrders={this.loadOrders} />
          </Tab.Pane>
        );

      default:
        return (
          <Tab.Pane key={tab.id} eventKey={tab.id}>
            <div />
          </Tab.Pane>
        );
    }
  };

  loadOrders = () => {
    this.shipmentList.getWrappedInstance().loadOrders();
    this.draftList.getWrappedInstance().loadOrders();
    if (this.jobBoard) {
      this.jobBoard.getWrappedInstance().loadOrders();
    }
  };
}

const mapStateToProps = (state, nextProps) => {
  const { tabs: { activeTab, openTabs = [] } } = state;

  return {
    activeTab,
    openTabs,
    formState: shipmentFormState(state, activeTab),
    isFreightcom: checkRoleIfFreightcom(state),
    allowNewOrders: checkIfUserIsAllowedToOrder(state),
    canManageClaims: checkIfUserCanManageClaims(state),
    canManageDisputes: checkIfUserCanManageDisputes(state),
    // Only here to force a render
    hasAddressBookData: hasAddressBook(state),
  };
};

const mapDispatchToProps = {
  addTab,
  removeTab,
  activateTab,
  updateTabType,
  loadPackageTypes,
  loadOrderStatuses,
  loadCurrencyList,
  loadCarrierList,
  checkAddressBook,
};

export default connect(mapStateToProps, mapDispatchToProps)(Shipments);
