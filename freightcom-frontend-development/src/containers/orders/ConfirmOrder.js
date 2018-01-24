import React, { Component } from 'react';
import { findDOMNode } from 'react-dom';
import { compose } from 'redux';
import { connect } from 'react-redux';
import { reduxForm, FormSection } from 'redux-form';
import startCase from 'lodash/startCase';
import { Row, Col, ButtonToolbar, Button } from 'react-bootstrap';
import formatDate from 'date-fns/format';
import { formatAddress } from '../../utils';
import { SHIPMENT_TYPES } from '../../constants';

import ShipmentWizardRibbon from '../../components/orders/ShipmentWizardRibbon';
import CustomsInvoiceForm from '../../components/orders/CustomsInvoiceForm';
import PayShipmentModal from '../../components/orders/PayShipmentModal';
import AlertMessage from '../../components/AlertMessage';
import InputDate from '../../components/forms/inputs/InputDate';
import InputTime from '../../components/forms/inputs/InputTime';
import InputTextArea from '../../components/forms/inputs/InputTextArea';
import InputText from '../../components/forms/inputs/InputText';

import {
  selectDistributionList,
  selectActiveTab,
  selectOneFromEntities,
  checkIfReadyToBookOrder,
  checkRoleIfFreightcom,
} from '../../reducers';
import { shipmentFormState } from '../../reducers/shipment-form';
import {
  bookOrder,
  updateOrderForBook,
  clearFormErrors,
  clearOrderForBook,
} from '../../actions/orders';
import { updateTab, removeTab } from '../../actions/tabs';
import { loadCustomerById } from '../../actions/customers';

class ConfirmOrder extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isNewContactPerson: false,
    };
  }

  componentWillMount() {
    this.props.loadCustomerById(this.props.order.customer.id, true);
  }

  componentDidMount() {
    const node = findDOMNode(this);
    window.scrollTo(0, node.offsetTop);
  }

  render() {
    const {
      payNow,
      confirm,
      saveCustomsPickupError,
      order: {
        shipFrom = {},
        shipTo = {},
        packageTypeName = '',
        referenceCode = '',
        shipDate = '',
        selectedQuote = false,
      } = {},
      formState: { updateBookData },
    } = this.props;

    try {
      return (
        <div>
          <ShipmentWizardRibbon step="2" orderId={this.props.orderId} />

          <form className="smart-form">
            <Row className="flex">
              <Col className="col" md={3} sm={6} xs={12}>
                {this.renderShipmentAddress('Ship From', shipFrom)}
              </Col>
              <Col className="col" md={3} sm={6} xs={12}>
                {this.renderShipmentAddress('Ship To', shipTo)}
              </Col>
              <Col className="col" md={6} sm={12} xs={12}>
                <div className="jarviswidget jarviswidget-sortable jarviswidget-color-blue shipment-details" data-widget-edit-button="false">
                  <header role="heading">
                    <h2>Shipment Details</h2>
                  </header>
                  <div>
                    <div className="widget-body">
                      <Row style={{ paddingBottom: '10px' }}>
                        <Col md={12} sm={12} xs={12} className="col">
                          <div>
                            <table className="table-list">
                              <tbody>
                                <tr>
                                  <td style={{ verticalAlign: 'top', paddingRight: '20px' }}>
                                    Shipment Type
                                  </td>
                                  <th>
                                    {packageTypeName === SHIPMENT_TYPES.ENVELOPE
                                      ? 'LETTER'
                                      : packageTypeName === SHIPMENT_TYPES.LTL
                                          ? 'LTL'
                                          : packageTypeName.toUpperCase()}
                                  </th>
                                </tr>
                                <tr>
                                  <td style={ {verticalAlign: 'top', paddingRight: '20px' }}>
                                    Shipment Date
                                  </td>
                                  <th>
                                    {formatDate(shipDate, 'YYYY-MM-DD')}
                                  </th>
                                </tr>
                                <tr>
                                  <td style={{ verticalAlign: 'top', paddingRight: '20px' }}>
                                    Reference Number
                                  </td>
                                  <th>
                                    {referenceCode}
                                  </th>
                                </tr>
                                <tr>
                                <td style={{ verticalAlign: 'top', paddingRight: '20px' }}>
                                    Additional Services
                                  </td>
                                  <th>
                                    {this.renderAccessorialServices()}
                                  </th>
                                </tr>
                              </tbody>
                            </table>
                          </div>
                        </Col>
                      </Row>
                      {this.renderShipmentItems()}
                    </div>
                  </div>
                </div>
              </Col>
            </Row>
            <Row>
              <Col className="col" sm={12} xs={12}>
                {this.renderSelectedCarrier(selectedQuote)}
              </Col>
            </Row>
            {this.props.order.customsSurveyFormRequired && <CustomsInvoiceForm />}
            <FormSection name="scheduledPickup">
            <Row>
              <Col className="col" sm={12} xs={12}>
                  <div className="jarviswidget jarviswidget-sortable jarviswidget-color-blue" data-widget-edit-button="false">
                    <header role="heading">
                      <h2>Schedule Pickup</h2>
                    </header>
                    <div>
                      <div className="widget-body">
                        {this.props.order.packageTypeName !==
                          SHIPMENT_TYPES.LTL &&
                          <Row>
                            <Col className="col" sm={6}>
                              <InputDate
                                name="pickupDate"
                                label="Pickup Date"
                                dateFormat="yy-mm-dd"
                                minDate={new Date()}
                                defaultValue={this.props.order.shipDate}
                              />
                            </Col>
                          </Row>}
                        <Row>
                          <Col className="col" sm={4}>
                            <InputTime
                              name="pickupReadyTime"
                              label="Pickup Ready Time"
                              defaultValue="9:00"
                            />
                          </Col>
                          <Col className="col" sm={4}>
                            <InputTime
                              name="pickupCloseTime"
                              label="Pickup Close Time"
                              defaultValue="17:00"
                            />
                          </Col>
                          <Col className="col" sm={4}>
                            <InputTime
                              name="deliveryCloseTime"
                              label="Delivery Close Time"
                              defaultValue="17:00"
                            />
                          </Col>
                        </Row>
                        <Row>
                          <Col className="col" sm={6}>
                            <InputTextArea
                              name="pickupInstructions"
                              label="Additional Pickup Instructions"
                            />
                          </Col>
                          <Col className="col" sm={6}>
                            <InputTextArea
                              name="deliveryInstructions"
                              label="Additional Delivery Instructions"
                            />
                          </Col>
                        </Row>
                        {this.props.order.packageTypeName !==
                          SHIPMENT_TYPES.LTL &&
                          <div>
                            <Row>
                              <Col className="col" sm={6}>
                                <section>
                                  <label className="label">
                                    Contact Person
                                  </label>
                                  <label className="checkbox">
                                    <input
                                      type="checkbox"
                                      defaultChecked="true"
                                      onChange={() =>
                                        this.setState({
                                          isNewContactPerson: !this.state
                                            .isNewContactPerson,
                                        })}
                                    />
                                    <i />
                                    <span>
                                      {' '}Same as ship from contact{' '}
                                      <b>{this.renderContactPersonInfo()}</b>
                                    </span>
                                  </label>
                                </section>
                              </Col>
                            </Row>
                            {this.state.isNewContactPerson &&
                              <Row>
                                <Col className="col" sm={4}>
                                  <InputText
                                    name="contactName"
                                    label="Contact Name"
                                  />
                                </Col>
                                <Col className="col" sm={4}>
                                  <InputText
                                    name="contactPhone"
                                    label="Contact Phone"
                                  />
                                </Col>
                                <Col className="col" sm={4}>
                                  <InputText
                                    name="contactEmail"
                                    label="Contact Email"
                                  />
                                </Col>
                              </Row>}
                          </div>}
                      </div>
                    </div>
                  </div>
                </Col>
              </Row>
            </FormSection>
            <br />
            <footer className="form-actions">
              <Button
                type="submit"
                bsStyle="primary"
                bsSize="large"
                onClick={this.showConfirmDialog}
              >
                Book Shipment
              </Button>
              {}
              <Button
                bsStyle="primary"
                bsSize="large"
                onClick={e => {
                  e.preventDefault();
                  this.props.updateTab(this.props.form, {
                    title: 'Edit Shipment',
                    type: 'ORDER_QUOTE',
                  });
                }}
              >
                Edit Shipment
              </Button>
            </footer>
          </form>
          {payNow &&
            <PayShipmentModal
              orderData={updateBookData}
              show={payNow}
              hideModal={() => this.props.clearOrderForBook(this.props.form)}
              order={this.props.order}
              submitForm={this.submitForm}
            />}

          {confirm
            ? <AlertMessage
                title="Book Shipment"
                text={this.generateConfirmMessage()}
                buttonToolbar={
                  <ButtonToolbar>
                    <Button onClick={this.cancelSubmit}>Cancel</Button>
                    <Button
                      bsStyle="primary"
                      onClick={this.submitForm}
                      disabled={!this.props.enableBookOrder}
                    >
                      Book Now
                    </Button>
                  </ButtonToolbar>
                }
              />
            : null}

          {saveCustomsPickupError
            ? <AlertMessage
                title="Error Saving"
                text={saveCustomsPickupError}
                buttonToolbar={
                  <ButtonToolbar>
                    <Button onClick={this.cancelSubmit}>Close</Button>
                  </ButtonToolbar>
                }
              />
            : null}
        </div>
      );
    } catch (error) {
      console.error('ERROR', error);
      return <div>{error}</div>;
    }
  }

  renderSelectedCarrier(item) {
    if (!item) {
      return null;
    }

    const {
      carrier: { name: carrierName = '' } = {},
      id = 0,
      logo = false,
      transitDays = '0',
      serviceName: service = '',
      charges = {},
      totalCharges = '0',
      description = '',
      currency = 'xxx',
    } = item;

    return (
      <div
        key={'carrier-' + id}
        className="jarviswidget jarviswidget-sortable jarviswidget-color-green"
        data-widget-editbutton="false"
        data-widget-attstyle="jarviswidget-color-green"
      >
        <header role="heading">
          <h2>Selected Rate</h2>
        </header>
        <div>
          <div className="widget-body">
            <div className="selected-carrier carrier-rate">
              <Row className="flex" style={{ alignItems: 'center' }}>
                <Col className="col" sm={2} xs={12}>
                  <Row className="padding-10">
                    <div className="carrier carrier-logo text-center">
                      <h4 style={{ display: 'none' }}>
                        {carrierName}
                      </h4>
                      <img
                        src={logo ? logo : '//placehold.it/120x60'}
                        alt=""
                        className="img-responsive"
                        style={{ width: '30%' }}
                      />
                    </div>
                  </Row>
                  <Row className="padding-10">
                    <div className="service-name">
                      <h4>
                        {service}
                      </h4>
                    </div>
                  </Row>
                </Col>
                <Col className="col text-center" sm={5} xs={12}>
                  <div className="carrier-description">
                    {description}
                  </div>
                  <div className="timeframe" style={{ overflow: 'hidden' }}>
                    <h5 className="cal-head">Est Transit Days</h5>
                    <div className="cal-body">
                      <h3 className="days">
                        {Number(transitDays) > 1
                          ? transitDays + ' days'
                          : !transitDays ? '0 days' : transitDays + ' day'}
                      </h3>
                      <div className="ring-left" />
                      <div className="ring-right" />
                    </div>
                  </div>
                </Col>
                <Col className="col" sm={3} xs={12}>
                  <div className="fees">
                    <table className="table table-bordered table-condensed">
                      <tbody>
                        {Object.keys(charges).map(key => (
                          <tr key={key}>
                            <td>
                              {startCase(key)}
                            </td>
                            <td>
                              {charges[key]}
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                </Col>
                <Col className="col text-center" sm={2} xs={12}>
                  <div className="total">
                    <h4>Total Amount</h4>
                    <h3 className="amount" style={{ textAlign: 'center' }}>
                      {`${(totalCharges || 0).toFixed(2).toLocaleString()}`}
                      <span>
                        {' '} {currency}
                      </span>
                    </h3>
                  </div>
                </Col>
              </Row>
            </div>
          </div>
        </div>
      </div>
    );
  }

  renderShipmentAddress = (title, address) => {
    // TODO: Figure out how to retrieve distribution list data
    if (address === null) {
      return (
        <div className="well well-sm well-light padding-10">
          <h3 className="well-title">
            {title}
          </h3>
          <h4>
            Distribution List:{' '}
            <a href="#">{this.props.distributionList.processed} recipients</a>
          </h4>
        </div>
      );
    }

    const {
      company,
      companyName,
      consigneeName = '',
      attention,
      email,
      phone,
      contactName = '',
      contactEmail = '',
      contactPhone = '',
    } = address;

    const displayEmail = contactEmail || email || '';
    const displayPhone = contactPhone || phone || '';

    return (
      <div
        className="jarviswidget jarviswidget-sortable jarviswidget-color-blue"
        data-widget-edit-button="false"
      >
        <header role="heading">
          <h2>
            {title}
          </h2>
        </header>
        <div>
          <div className="widget-body">
            <h4 style={{ color: '#444', fontWeight: 'bold' }}>
              {company || companyName || consigneeName || '-'}
            </h4>
            <address style={{ fontStyle: 'italic' }}>
              {formatAddress(address)}
            </address>
            <hr className="simple" />
            <h4 style={{ fontSize: '16px', color: '#333' }}>
              {contactName || attention || '-'}
            </h4>
            {displayEmail &&
              <address>
                <a href={`mailto:${displayEmail}`} title={displayEmail}>
                  <span className="email-address">
                    {displayEmail}
                  </span>
                </a>
              </address>}
            {displayPhone &&
              <address>
                {displayPhone}
              </address>}
          </div>
        </div>
      </div>
    );
  };

  renderAccessorialServices = () => {
    const { accessorialServiceNames = [] } = this.props.order;
    return (
      <div style={{paddingLeft: '10px'}}>
        <ul style={{paddingLeft: '10px'}}>
          {accessorialServiceNames.map((item, index) => {
            return (
              <li key={index}>
                {item}
              </li>
            );
          })}
        </ul>
      </div>
    );
  };

  renderShipmentItems = () => {
    const { packageTypeName = '' } = this.props.order || {};
    switch (packageTypeName) {
      case SHIPMENT_TYPES.LTL:
        return (
          <table className="table table-bordered">
            <thead>
              <tr>
                <th>Dimension</th>
                <th>Weight</th>
                <th>Insurance Amount</th>
                <th>Freight Class</th>
                <th>NMFC Code</th>
                <th>Type</th>
                <th>Pieces</th>
                <th>Description</th>
              </tr>
            </thead>
            <tbody>
              {this.props.order.pallets.map((item, index) => {
                const {
                  id = index,
                  length = 0,
                  width = 0,
                  height = 0,
                  weight = 0,
                  insurance = 0,
                  freightClass = '500',
                  nmfcCode = '1111',
                  palletType = 'Pallet',
                  pieces = '1',
                  description = 'no description',
                } = item;

                return (
                  <tr key={id}>
                    <td>
                      {`${length} in \u00d7 ${width} in \u00d7 ${height} in`}
                    </td>
                    <td>{`${weight} lbs`}</td>
                    <td>
                      ${`${insurance}`}
                    </td>
                    <td>{`${freightClass}`}</td>
                    <td>{`${nmfcCode}`}</td>
                    <td>{`${palletType}`}</td>
                    <td>{`${pieces}`}</td>
                    <td>{`${description}`}</td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        );

      case SHIPMENT_TYPES.PACKAGE:
        return (
          <table className="table table-bordered">
            <thead>
              <tr>
                <th>Dimension</th>
                <th>Weight</th>
                <th>Insurance Amount</th>
                <th>Description</th>
              </tr>
            </thead>
            <tbody>
              {this.props.order.packages.map((item, index) => {
                const {
                  id = index,
                  length = 0,
                  width = 0,
                  height = 0,
                  weight = 0,
                  insurance = 0,
                  description = 0,
                } = item;

                return (
                  <tr key={id}>
                    <td>
                      {`${length} in \u00d7 ${width} in \u00d7 ${height} in`}
                    </td>
                    <td>{`${weight} lbs`}</td>
                    <td>
                      ${`${insurance}`}
                    </td>
                    <td>{`${description}`}</td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        );

      case SHIPMENT_TYPES.PAK:
        const { weight = 0 } = this.props.order;
        return (
          <table className="table table-bordered">
            <tbody>
              <tr>
                <td>
                  Weight: <b>{weight} lbs</b>
                </td>
              </tr>
            </tbody>
          </table>
        );
      default:
    }
  };

  renderContactPersonInfo = () => {
    if (
      this.props.order.shipFrom.contactName &&
      this.props.order.shipFrom.contactPhone
    ) {
      return (
        this.props.order.shipFrom.contactName +
        ', ' +
        (this.props.order.shipFrom.contactPhone || '')
      );
    }
    if (this.props.order.shipFrom.contactName) {
      return this.props.order.shipFrom.contactName;
    }
    return null;
  };

  showConfirmDialog = e => {
    e.preventDefault();
    const { form, updateOrderForBook } = this.props;
    updateOrderForBook(form);
  };

  submitForm = e => {
    if (e && e.preventDefault) {
      e.preventDefault();
    }

    this.props.bookOrder(this.props.form, () => true);
  };

  cancelSubmit = e => {
    if (e && e.preventDefault) {
      e.preventDefault();
    }
    this.props.clearOrderForBook(this.props.form);
  };

  generateConfirmMessage = () => {
    if (this.props.isAdmin) {
      return 'This will submit the Order for processing. The Customer will be notified';
    }

    return 'This will submit your order for processing. You can still cancel the order until 24h before the scheduled pickup time (TO BE ADJUSTED)';
  };
}

const formatBookError = response => {
  if (!response) {
    return 'An error has occurred saving the pickup time or customs invoice';
  }

  return Object.entries(response)
    .map(([variable, value]) => `${variable}: ${value}`)
    .join('<br>');
};

const mapStateToProps = state => {
  const activeTab = selectActiveTab(state);
  const form = activeTab.id;
  const orderId = activeTab.orderId;
  const order = selectOneFromEntities(state, 'draftOrder', orderId, [
    { entity: 'shippingAddress', key: 'shipFrom' },
    { entity: 'shippingAddress', key: 'shipTo' },
    { entity: 'customer', key: 'customer' },
  ]) || {};

  const distributionList = selectDistributionList(state, form) || {};
  const {
    updateBook,
    updateBookData = {},
    updateBookError: { response: saveCustomsPickupError } = {},
  } = shipmentFormState(state, form) || {};

  return {
    form,
    orderId,
    order,
    distributionList,
    enableBookOrder: checkIfReadyToBookOrder(state, form),
    isAdmin: checkRoleIfFreightcom(state),
    formState: shipmentFormState(state, form),
    payNow: updateBook === 'success' && updateBookData.payNow,
    confirm: updateBook === 'success' && !updateBookData.payNow,
    saveCustomsPickupError: updateBook === 'failed' &&
      formatBookError(saveCustomsPickupError),
  };
};

const onChange = function(props, dispatch) {
  dispatch(clearFormErrors(this.form));
};

const mapDispatchToProps = {
  bookOrder,
  updateOrderForBook,
  updateTab,
  removeTab,
  loadCustomerById,
  clearOrderForBook,
};

export default compose(
  connect(mapStateToProps, mapDispatchToProps),
  reduxForm({ destroyOnUnmount: false, onChange }),
)(ConfirmOrder);
