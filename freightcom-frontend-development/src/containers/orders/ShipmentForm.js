import React, { Component } from 'react';
import { findDOMNode } from 'react-dom';
import { connect } from 'react-redux';
import { compose } from 'redux';
import { reduxForm, formValueSelector } from 'redux-form';
import { Row, Col, Button } from 'react-bootstrap';

import CustomerAutoSuggest
  from '../../components/customers/CustomerAutoSuggest';
import {
  getShipmentRates,
  confirmOrder,
  saveOrderAsDraft,
  loadDuplicate,
} from '../../actions/orders';
import { checkAddressBook } from '../../actions/addressBook';
import { updateTab, removeTab } from '../../actions/tabs';
import {
  selectActiveTab,
  checkRoleIfAdminOrAgent,
  checkIfRatesSelected,
  checkIfRatesLoaded,
  checkIfReadyToGetRates,
  selectOneFromEntities,
  selectLoggedInCustomer,
  checkIfShippingAddressIsValid,
  checkIfRatesFetching,
  hasAddressBook,
  selectEntity,
} from '../../reducers';

import ShipmentWizardRibbon from '../../components/orders/ShipmentWizardRibbon';
import ShipmentAddress from '../../components/orders/ShipmentAddress';
import ShipmentDetails from '../../components/orders/ShipmentDetails';
import ShipmentRates from '../../components/orders/Rates';

class ShipmentForm extends Component {
  componentWillReceiveProps(nextProps) {
    const { duplicate, lifecycle } = nextProps.formState;

    if (duplicate && lifecycle === 'new') {
      this.props.loadDuplicate(duplicate, nextProps.form);
    } else if (duplicate && lifecycle === 'fetching') {
    } else if (
      nextProps.orderTemplate &&
      duplicate &&
      lifecycle === 'done' &&
      this.props.formState &&
      this.props.formState.lifecycle !== 'done'
    ) {
      this.props.initialize({
        ...nextProps.orderTemplate,
        id: null,
      });
    }
  }

  render() {
    return (
      <div>
        {!this.props.isInstantRate &&
          <ShipmentWizardRibbon step="1" orderId={this.props.orderId} />}

        <form
          onSubmit={e => this.props.handleSubmit(this.props.onSubmit)}
          className="smart-form"
        >
          {this.props.isAdminOrAgent
            ? <Row>
                <Col className="col" sm={12} xs={12}>
                  <div
                    className="jarviswidget jarviswidget-sortable jarviswidget-color-blue"
                    data-widget-edit-button="false"
                  >
                    <header role="heading">
                      <h2>Customer</h2>
                    </header>
                    <div>
                      <div className="widget-body">
                        <CustomerAutoSuggest
                          name="customerId"
                          value={this.props.customer}
                          onChange={customer => {
                            this.props.change(
                              'customerId',
                              customer ? customer.id : '',
                            );
                            this.props.change('customer', customer);
                            this.props.checkAddressBook(customer);
                          }}
                          required
                        />
                      </div>
                    </div>
                  </div>
                </Col>
              </Row>
            : ''}

          <Row className="flex">
            <Col className="col" sm={6} xs={12}>
              <ShipmentAddress
                id="ship-from-widget"
                title="Ship From"
                name="shipFrom"
                customer={this.props.customer}
                hasAddressBookData={this.props.hasAddressBookData}
              />
            </Col>
            <Col className="col" sm={6} xs={12}>
              <ShipmentAddress
                id="ship-to-widget"
                title="Ship To"
                name="shipTo"
                showDistributionListOption
                customer={this.props.customer}
                hasAddressBookData={this.props.hasAddressBookData}
              />
            </Col>
          </Row>
          <Row>
            <Col className="col" sm={12} xs={12}>
              <ShipmentDetails
                name="shipmentDetails"
                customer={this.props.customer}
              />
            </Col>
          </Row>
          <ShipmentRates />

          {this.props.isWorkOrder
            ? <footer className="form-actions">
                <Button
                  bsStyle="primary"
                  bsSize="large"
                  onClick={this.continueWorkOrder}
                >
                  Continue Work Order
                </Button>
              </footer>
            : <footer className="form-actions">
                {this.props.isRatesLoaded
                  ? <Button
                      bsStyle="primary"
                      bsSize="large"
                      disabled={!this.validateToContinue()}
                      onClick={this.continueOrder}
                    >
                      Continue
                    </Button>
                  : <Button
                      bsStyle="primary"
                      bsSize="large"
                      disabled={!this.props.enableGetRates}
                      onClick={this.getRates}
                    >
                      Get Rates
                    </Button>}
                <Button
                  bsStyle="success"
                  bsSize="large"
                  onClick={this.saveOrder}
                >
                  Save
                </Button>
                {this.props.isAdminOrAgent &&
                  <Button
                    bsStyle="primary"
                    bsSize="large"
                    disabled={!this.props.enableGetRates}
                    onClick={this.continueWorkOrder}
                  >
                    Work Order
                  </Button>}
              </footer>}
        </form>
      </div>
    );
  }

  saveOrder = e => {
    this.props.saveOrderAsDraft(this.props.form, () => {});
  };

  getRates = e => {
    e.target.disabled = true;
    this.props.getShipmentRates(this.props.form);
  };

  continueOrder = e => {
    // If submitting order from an instant rate...
    if (this.props.isInstantRate) {
      e.preventDefault();
      this.props.updateTab(this.props.form, { isInstantRate: false });
      const node = findDOMNode(this);
      window.scrollTo(0, node.offsetTop);
    } else {
      this.props.confirmOrder(this.props.form, () => {});
    }
  };

  continueWorkOrder = e => {
    e.target.disabled = true;
    //Using the getRate function but actually it saves an order only
    this.props.getShipmentRates(this.props.form, true);
  };

  validateToContinue = () => {
    if (
      (this.props.isAdminOrAgent || this.props.allowNewOrders) &&
      this.props.isRatesSelected &&
      (this.props.isValidShippingAddress || this.props.isInstantRate)
    ) {
      return true;
    }
    return false;
  };
}

const mapStateToProps = (state, nextProps) => {
  const activeTab = selectActiveTab(state);
  const formName = activeTab.id;
  const { isInstantRate = false } = activeTab;
  const customerId = formValueSelector(formName)(state, 'customerId') || '';
  // customer data is used by the CustomerAutoSuggest. It needs an object with
  // `id` and `name` property.
  // first, we check if customer data is in the form data,
  // this means it's been set by the user
  // if not available, check the state.entities.customer for the data.
  // usually that means orders was a duplicated from the existing orders
  const customerData = formValueSelector(formName)(state, 'customer');
  const customer = customerData && customerData.id
    ? customerData
    : selectOneFromEntities(state, 'customer', customerId);

  return {
    // `form` is required to initialize the reduxForm
    form: formName,
    orderId: activeTab.orderId || false,
    isWorkOrder: !!activeTab.workOrder,
    orderTemplate: nextProps.formState
      ? selectEntity(state, 'orderTemplate', nextProps.formState.duplicate)
      : null,
    isInstantRate,
    isRatesSelected: checkIfRatesSelected(state, formName),
    isRatesLoaded: checkIfRatesLoaded(state, formName),
    enableGetRates: checkIfReadyToGetRates(state, formName) &&
      !checkIfRatesFetching(state, formName),
    isAdminOrAgent: checkRoleIfAdminOrAgent(state),
    customer: customer.id ? customer : '',
    allowNewOrders: selectLoggedInCustomer(state).allowNewOrders,
    isValidShippingAddress: checkIfShippingAddressIsValid(state, formName),
    // Only here to force a render
    hasAddressBookData: hasAddressBook(state)(
      checkRoleIfAdminOrAgent(state) ? customer : null,
    ),
  };
};

const mapDispatchToProps = {
  getShipmentRates,
  confirmOrder,
  updateTab,
  removeTab,
  saveOrderAsDraft,
  loadDuplicate,
  checkAddressBook,
};

export default compose(
  connect(mapStateToProps, mapDispatchToProps),
  reduxForm({ destroyOnUnmount: false }),
)(ShipmentForm);
