import React, { Component } from 'react';
import { compose } from 'redux';
import { connect } from 'react-redux';
import { reduxForm, FormSection } from 'redux-form';
import { Row, Col, ButtonToolbar, Button } from 'react-bootstrap';
import formatDate from 'date-fns/format';
import { addNotification } from 'reapop';
import { formatAddress, formatCurrency } from '../../utils';
import { SHIPMENT_TYPES } from '../../constants';

import AlertMessage from '../../components/AlertMessage';
import InputDate from '../../components/forms/inputs/InputDate';
import InputTime from '../../components/forms/inputs/InputTime';
import InputTextArea from '../../components/forms/inputs/InputTextArea';
import InputText from '../../components/forms/inputs/InputText';

import {
  selectDistributionList,
  selectActiveTab,
  selectOneFromEntities,
  selectAllFromEntities,
  checkIfReadyToBookOrder,
  checkRoleIfFreightcom,
  getPageItems,
} from '../../reducers';
import { removeOneEntity } from '../../actions';
import {
  bookOrder,
  sendQuote,
  loadDraftOrderById,
  deleteOrderCharge,
} from '../../actions/orders';
import { updateTab, removeTab } from '../../actions/tabs';
import OrderChargeModal from '../../components/orders/OrderChargeModal';

class ConfirmCustomWorkOrder extends Component {
  constructor(props) {
    super(props);
    this.state = {
      confirmDialog: false,
      isNewContactPerson: false,
      chargeModal: false,
      charge: 'new',
      selectedOrderCharge: {},
      defaultCarrier: '',
    };
  }
  componentWillMount() {}

  render() {
    const {
      shipFrom = {},
      shipTo = {},
      packageTypeName = '',
      referenceCode = '',
      shipDate = '',
    } = this.props.order;

    return (
      <div>
        <form className="smart-form">
          <Row className="flex">
            <Col className="col" sm={6} xs={12}>
              {this.renderShipmentAddress('Ship From', shipFrom)}
            </Col>
            <Col className="col" sm={6} xs={12}>
              {this.renderShipmentAddress('Ship To', shipTo)}
            </Col>
          </Row>
          <Row>
            <Col className="col" sm={12} xs={12}>
              {this.renderCharges()}
            </Col>
          </Row>
          <Row>
            <Col className="col" sm={12} xs={12}>
              <div className="jarviswidget jarviswidget-sortable jarviswidget-color-blue" data-widget-edit-button="false">
                <header role="heading">
                  <h2>Shipment Details</h2>
                </header>
                <div>
                  <div className="widget-body">
                    <Row style={{ paddingBottom: '10px' }}>
                      <Col sm={12} xs={12} className="col">
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
                                <td style={{ verticalAlign: 'top', paddingRight: '20px' }}>
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
          <FormSection name="scheduledPickup">
            <Row>
              <Col className="col" sm={12} xs={12}>
                <div className="jarviswidget jarviswidget-sortable jarviswidget-color-blue" data-widget-edit-button="false">
                  <header role="heading">
                    <h2>Schedule Pickup</h2>
                  </header>
                  <div>
                    <div className="widget-body">
                      <fieldset>
                        {this.props.order.packageTypeName !== SHIPMENT_TYPES.LTL &&
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
                        {this.props.order.packageTypeName !== SHIPMENT_TYPES.LTL &&
                          <div>
                            <Row>
                              <Col className="col" sm={6}>
                                <section>
                                  <label className="label">Contact Person</label>
                                  <label className="checkbox">
                                    <input
                                      type="checkbox"
                                      defaultChecked="true"
                                      onChange={() => this.setState({
                                        isNewContactPerson: !this.state.isNewContactPerson,
                                      })}
                                    />
                                    <i />
                                    <span>
                                      {' '}
                                      Same as ship from contact
                                      {' '}
                                      <b>
                                        {
                                          this.props.order.shipFrom.contactName +
                                            ', ' +
                                            (this.props.order.shipFrom.contactPhone ||
                                              '')
                                        }
                                      </b>
                                    </span>
                                  </label>
                                </section>
                              </Col>
                            </Row>
                            {this.state.isNewContactPerson &&
                              <Row>
                                <Col className="col" sm={4}>
                                  <InputText name="contactName" label="Contact Name" />
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
                      </fieldset>
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
            <Button
              bsStyle="success"
              bsSize="large"
              onClick={e => {
                e.preventDefault();
                this.props.sendQuote(this.props.form, () => {
                  this.props.removeTab(this.props.form);
                  this.props.addNotification({
                    title: 'Send Quote Status',
                    message: 'Order quote will be sent to Customer contact',
                    status: 'success',
                  });
                });
              }}
            >
              Send Quote
            </Button>
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

        {this.state.confirmDialog &&
          <AlertMessage
            title="Book Shipment"
            text={this.generateConfirmMessage()}
            buttonToolbar={
              (
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
              )
            }
          />}
        {this.state.chargeModal &&
          <OrderChargeModal
            showModal={this.state.chargeModal}
            toggleModal={() => this.setState({ chargeModal: false })}
            orderId={this.props.orderId}
            carrierId={this.state.defaultCarrier}
            charge={this.state.selectedOrderCharge}
            packageTypeName={this.props.order.packageTypeName}
            onSubmitCallback={data => {
              this.props.loadDraftOrderById(this.props.orderId, true);
              //this.setState({ chargeModal: false, defaultCarrier: data.carrier && data.carrier.id });
              this.setState({
                chargeModal: false,
                defaultCarrier: data.payload.entity.carrier &&
                  data.payload.entity.carrier.id,
              });
            }}
            showCarrier
          />}
      </div>
    );
  }

  renderShipmentAddress = (title, address) => {
    const {
      distributionListId = false,
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

    if (distributionListId) {
      return (
        <div className="jarviswidget jarviswidget-sortable jarviswidget-color-blue" data-widget-edit-button="false">
          <header role="heading">
            <h2>{title}</h2>
          </header>
          <div>
            <div className="widget-body">
              <h4>
                Distribution List:{' '}
                <a href="#">{this.props.distributionList.processed} recipients</a>
              </h4>
            </div>
          </div>
        </div>
      );
    }

    return (
      <div className="jarviswidget jarviswidget-sortable jarviswidget-color-blue" data-widget-edit-button="false">
        <header role="heading">
          <h2>{title}</h2>
        </header>
        <div>
          <div className="widget-body">
            <h4>{company || companyName || consigneeName || '-'}</h4>
            <address>{formatAddress(address)}</address>
            <h4>{contactName || attention || '-'}</h4>
            <address>
              {contactPhone || phone || ''}
              <span className="email-address">
                {contactEmail || email || ''}
              </span>
            </address>
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
            return <li key={index}>{item}</li>;
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
                    <td>${`${insurance}`}</td>
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
                    <td>${`${insurance}`}</td>
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
                <td>Weight: <b>{weight} lbs</b></td>
              </tr>
            </tbody>
          </table>
        );
      default:
    }
  };

  showConfirmDialog = e => {
    e.preventDefault();
    this.setState({ confirmDialog: true });
  };

  submitForm = () => {
    this.setState({ confirmDialog: false });
    this.props.bookOrder(this.props.form, this.props.loadOrders);
  };

  cancelSubmit = () => {
    this.setState({ confirmDialog: false });
  };

  renderCharges = () => {
    const { order: { charges = [] } } = this.props;
    if (!charges) {
      return null;
    }
    return (
      <div className="jarviswidget jarviswidget-sortable jarviswidget-color-blue" data-widget-edit-button="false">
        <header role="heading">
          <h2>
            Custom Services {' '}
            {this.props.isAdmin &&
              <Button
                bsStyle="primary"
                bsSize="xs"
                onClick={e => {
                  e.preventDefault();
                  this.toggleChargeModal({});
                }}
              >
                Add
              </Button>}
          </h2>
        </header>
        <table
          className="table table-condensed table-bordered table-striped table-standout"
        >
          <thead>
            <tr>
              <th>Carrier</th>
              <th>Name</th>
              <th>Charge</th>
              {this.props.isAdmin && <th>Cost</th>}
              {this.props.isAdmin && <th>Markup</th>}
              {this.props.isAdmin && <th>Commission</th>}
              <th>Status</th>
              {this.props.isAdmin && <th>Action</th>}
            </tr>
          </thead>
          <tbody>
            {charges.map((charge, index) => (
              <tr key={`le-${index}-${charge.id}`}>
                <td>{(charge.carrier && charge.carrier.name) || ''}</td>
                <td>
                  <b style={{ display: 'block' }}>{charge.name}</b>
                  {charge.description}
                </td>
                <td className="currency">
                  {formatCurrency(charge.total)}
                </td>
                {this.props.isAdmin &&
                  <td className="currency">
                    {formatCurrency(charge.cost)}
                  </td>}
                {this.props.isAdmin && <td>N/A</td>}
                {this.props.isAdmin && <td>N/A</td>}
                <td>
                  {this.renderChargeStatus(charge)}
                </td>
                {this.props.isAdmin &&
                  <td>
                    {!charge.invoiceId &&
                      !charge.payableReported &&
                      !charge.commissionReported &&
                      <div>
                        <Button
                          className="btn btn-xs btn-primary"
                          onClick={e => {
                            e.preventDefault();
                            this.toggleChargeModal(charge);
                          }}
                        >
                          Edit
                        </Button>{' '}
                        <Button
                          bsStyle="danger"
                          bsSize="xs"
                          onClick={e => {
                            e.preventDefault();
                            this.props.deleteOrderCharge(charge.id, () => {
                              this.props.removeOneEntity(
                                'draftOrder',
                                this.props.orderId,
                                'charges',
                              );
                              this.props.loadDraftOrderById(
                                this.props.orderId,
                                true,
                              );
                            });
                          }}
                        >
                          Delete
                        </Button>
                      </div>}
                  </td>}
              </tr>
            ))}
          </tbody>
          <tfoot>
            <tr>
              <th colSpan="2" style={{ textAlign: 'right' }}>Total</th>
              <th className="currency">
                {charges
                  .reduce(
                    (acc, item) => {
                      return Number((acc + item.total).toFixed(2));
                    },
                    0,
                  )
                  .toFixed(2)}
              </th>
              {this.props.isAdmin &&
                <th className="currency">
                  {charges
                    .reduce(
                      (acc, item) => {
                        return Number((acc + item.cost).toFixed(2));
                      },
                      0,
                    )
                    .toFixed(2)}
                </th>}
              <th colSpan={this.props.isAdmin ? '4' : '1'} />
            </tr>
          </tfoot>
        </table>
      </div>
    );
  };

  renderChargeStatus = charge => {
    let invoiceLabel = null;
    let commissionLabel = null;
    let payableLabel = null;
    if (charge.invoiceId) {
      invoiceLabel = (
        <span className="label label-primary">
          B
        </span>
      );
    }
    if (charge.commissionReported) {
      commissionLabel = (
        <span className="label label-success">
          C
        </span>
      );
    }
    if (charge.payableReported) {
      payableLabel = (
        <span className="label label-success">
          P
        </span>
      );
    }
    if (
      !charge.invoiceId && !charge.commissionReported && !charge.payableReported
    ) {
      return (
        <span className="label label-warning">
          Unbilled
        </span>
      );
    }
    return <div>{invoiceLabel}{' '}{commissionLabel}{' '}{payableLabel}</div>;
  };

  toggleChargeModal = charge => {
    this.setState({
      chargeModal: !this.state.chargeModal,
      selectedOrderCharge: charge,
    });
  };

  generateConfirmMessage = () => {
    if (this.props.isAdmin) {
      return 'This will submit the Order for processing. The Customer will be notified';
    }
    return 'This will submit your order for processing. You can still cancel the order until 24h before the scheduled pickup time (TO BE ADJUSTED)';
  };
}

const mapStateToProps = state => {
  const activeTab = selectActiveTab(state);
  const form = activeTab.id;
  const orderId = activeTab.orderId;
  const order = selectOneFromEntities(state, 'draftOrder', orderId, [
    { entity: 'shippingAddress', key: 'shipFrom' },
    { entity: 'shippingAddress', key: 'shipTo' },
  ]) || {};

  const distributionList = selectDistributionList(state, form) || {};
  return {
    form,
    orderId,
    order,
    distributionList,
    enableBookOrder: checkIfReadyToBookOrder(state, form),
    accessorialServices: selectAllFromEntities(state, 'accessorialServices'),
    carriers: getPageItems(state, 'carrier'),
    isAdmin: checkRoleIfFreightcom(state),
  };
};

const mapDispatchToProps = {
  bookOrder,
  sendQuote,
  updateTab,
  removeTab,
  loadDraftOrderById,
  deleteOrderCharge,
  addNotification,
  removeOneEntity,
};

export default compose(
  connect(mapStateToProps, mapDispatchToProps),
  reduxForm({ destroyOnUnmount: false }),
)(ConfirmCustomWorkOrder);
