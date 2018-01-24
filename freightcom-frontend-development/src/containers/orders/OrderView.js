import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router';
import {
  Row,
  Col,
  ButtonToolbar,
  ButtonGroup,
  Popover,
  Modal,
} from 'react-bootstrap';
import Button from 'react-bootstrap-button-loader';
import formatDate from 'date-fns/format';
import { getSystemProperty } from '../../reducers';
import { EditableTextField } from 'react-bootstrap-xeditable';
import { addNotification } from 'reapop';
import strings from '../../strings';
import SubmitClaimForm from '../../components/orders/SubmitClaimForm';

import {
  formatCurrency,
  formatAddress,
  displayCompanyOrContactName,
  formatPhone,
} from '../../utils';
import {
  selectStatusLogsByOrderId,
  selectOneFromEntities,
  selectActiveTab,
  checkRoleIfCustomer,
  checkRoleIfFreightcomAdmin,
  checkIfUserCanManageClaims,
  checkRoleIfAdminOrAgent,
  checkIfUserCanManageDisputes,
  checkRoleIfFreightcom,
} from '../../reducers';
import { removeOneEntity } from '../../actions';
import {
  loadOrderById,
  loadOrderLogs,
  duplicateOrder,
  updateOrder,
  deleteOrderCharge,
  reconcileAllCharges,
  disputeCharge,
  replyDisputedCharge,
  markOrderAsInTransit,
  markOrderAsDelivered,
  deleteOrderDocument,
} from '../../actions/orders';
import { generateInvoice } from '../../actions/invoices';
import { removeTab } from '../../actions/tabs';
import { loadSettings } from '../../actions/settings';

import CustomsInvoiceModal from '../../components/orders/CustomsInvoiceModal';
import DisputeModal from '../../components/orders/DisputeModal';
import DisputeReplyModal from '../../components/orders/DisputeReplyModal';

import AddCommentModal from '../../components/orders/AddCommentModal';
import UpdateOrderStatusModal
  from '../../components/orders/UpdateOrderStatusModal';
import OrderChargeModal from '../../components/orders/OrderChargeModal';
import SchedulePickupModal from '../../components/orders/SchedulePickupModal';
import AlertMessage from '../../components/AlertMessage';
import Icon from '../../components/Icon';
import OrderStatusLabel from '../../components/orders/OrderStatusLabel';
import DocumentUploadModal from '../../components/orders/DocumentUploadModal';
import ClaimsUpdateForm from '../../components/orders/ClaimsUpdateForm';

class OrderView extends Component {
  constructor(props) {
    super(props);
    this.state = {
      customsInvoiceModal: false,
      addCommentModal: false,
      schedulePickupModal: false,
      chargeModal: false,
      updateOrderStatusModal: false,
      disputeModal: false,
      disputeReplyModal: false,
      deleteCharge: null,
      isDeletingCharge: false,
      charge: 'new',
      documentUploadModal: false,
      confirmFileDelete: false,
      submitClaimModal: this.props.command === 'claim',
    };
    this.overlay = {
      reconciled: <Popover id="popover-trigger-focus">Reconciled.</Popover>,
      notReconciled: (
        <Popover id="popover-trigger-focus">Not reconciled.</Popover>
      ),
    };
  }

  componentWillMount() {
    this.props
      .loadSettings()
      .then(() => {
        this.props.loadOrderLogs(this.props.orderId);
      })
      .then(() => {
        this.props.loadOrderById(this.props.orderId, true);
      });
  }

  componentWillReceiveProps(nextProps) {
    if (
      this.props.command !== nextProps.command &&
      nextProps.command === 'claim'
    ) {
      this.setState({ submitClaimModal: true });
    }
  }

  render() {
    const { order = {}, isAdminOrAgent } = this.props;

    return (
      <div>
        <div>
          <div className="shipmentDetails-heading">
            <h2
              className="clearfix"
              style={{ paddingTop: '0', marginTop: '0' }}
            >
              <span className="pull-left">Shipment: {order.id}</span>

              {isAdminOrAgent &&
                order.customer &&
                <div>
                  <br />
                  <span className="pull-left">
                    <Link
                      to={`/customers/${order.customer && order.customer.id}`}
                    >
                      {order.customer && order.customer.name}
                    </Link>
                  </span>
                </div>}

              <ButtonToolbar className="pull-right">
                {order.hasShippingLabel &&
                  <Button
                    bsStyle="primary"
                    bsSize="xsmall"
                    href={`/api/order/shipping-label/${order.id}`}
                    download
                  >
                    Download Shipping Label
                  </Button>}
                {order.customsInvoice &&
                  <Button
                    bsStyle="default"
                    bsSize="xsmall"
                    onClick={e => this.setState({ customsInvoiceModal: true })}
                  >
                    Customs Invoice
                  </Button>}
                {order.packageTypeName !== 'pallet' &&
                  !order.scheduledPickup &&
                  order.canSchedulePickup &&
                  <Button
                    bsStyle="default"
                    bsSize="xsmall"
                    onClick={e => this.setState({ schedulePickupModal: true })}
                  >
                    Schedule Pickup
                  </Button>}
                {order.podName &&
                  <a
                    className="btn btn-default btn-xs"
                    href={`/api/order/${order.id}/pod`}
                    download={`shipment-${order.id}-pod`}
                  >
                    Download POD
                  </a>}
              </ButtonToolbar>
            </h2>
          </div>
          <hr className="simple" />
          {this.renderShippingDetails()}
          <hr className="simple" />
          <h3>Shipment Terms</h3>
          <p>
            {this.props.shipmentTerms}
          </p>
        </div>

        <footer className="form-actions">
          <Button
            bsStyle="primary"
            bsSize="large"
            onClick={e => this.props.duplicateOrder(this.props.orderId)}
          >
            Duplicate
          </Button>

          {this.props.order &&
            this.props.order.claimCanBeFiled &&
            !this.state.submitClaimModal &&
            <Button
              bsStyle="primary"
              bsSize="large"
              onClick={e => this.setState({ submitClaimModal: true })}
            >
              File Claim
            </Button>}
        </footer>

        {this.state.customsInvoiceModal &&
          <CustomsInvoiceModal
            showModal={this.state.customsInvoiceModal}
            toggleModal={() => this.setState({ customsInvoiceModal: false })}
            order={this.props.order}
          />}

        {this.state.addCommentModal &&
          <AddCommentModal
            show={this.state.addCommentModal}
            toggleAddCommentModal={() =>
              this.setState({ addCommentModal: false })}
            loadOrderLogs={this.props.loadOrderLogs}
            order={order}
          />}

        {this.state.disputeModal &&
          <DisputeModal
            showModal={this.state.disputeModal}
            toggleModal={() => this.setState({ disputeModal: false })}
            charges={this.props.order.charges}
            disputeCharge={this.props.disputeCharge}
            onSubmit={({ chargeId, ...formData }) => {
              this.props.disputeCharge(
                chargeId,
                formData,
                ({ error = false }) => {
                  if (error) {
                    this.props.addNotification({
                      title: 'Order Disputes',
                      message: `Failed to submit dispute`,
                      status: 'error',
                    });
                  } else {
                    this.props.loadOrderById(this.props.orderId, true);
                    this.props.loadOrderLogs(this.props.orderId);
                    this.props.addNotification({
                      title: 'Order Disputes',
                      message: `Submitted an order dispute`,
                      status: 'success',
                    });
                  }
                  this.setState({ disputeModal: false });
                },
              );
            }}
          />}

        {this.state.disputeReplyModal &&
          <DisputeReplyModal
            showModal={this.state.disputeReplyModal}
            toggleModal={() => this.setState({ disputeReplyModal: false })}
            charges={this.props.order.charges}
            onSubmit={formData => {
              this.props.replyDisputedCharge(
                this.props.order.id,
                formData,
                ({ error = false }) => {
                  if (error) {
                    this.props.addNotification({
                      title: 'Order Disputes',
                      message: `Failed to submit dispute reply`,
                      status: 'error',
                    });
                  } else {
                    this.props.loadOrderById(this.props.orderId, true);
                    this.props.loadOrderLogs(this.props.orderId);
                    this.props.addNotification({
                      title: 'Order Disputes',
                      message: `Responded to order disputes`,
                      status: 'success',
                    });
                  }
                  this.setState({ disputeReplyModal: false });
                },
              );
            }}
          />}

        {this.state.updateOrderStatusModal &&
          <UpdateOrderStatusModal
            showModal={this.state.updateOrderStatusModal}
            toggleModal={() => this.setState({ updateOrderStatusModal: false })}
            modalTitle={this.updateOrderStatusModal.modalTitle}
            modalActionLabel={this.updateOrderStatusModal.modalActionLabel}
            modalAction={this.updateOrderStatusModal.modalAction}
          />}

        {this.state.chargeModal &&
          <OrderChargeModal
            showModal={this.state.chargeModal}
            toggleModal={() => this.setState({ chargeModal: false })}
            orderId={this.props.orderId}
            carrierId={this.state.defaultCarrier}
            charge={this.state.charge}
            packageTypeName={this.props.order.packageTypeName}
            onSubmitCallback={data => {
              if (data.error) {
                this.props.addNotification({
                  title: 'Charge Request Failed',
                  message: `There was an error processing this request`,
                  status: 'error',
                  dismissible: false,
                  dismissAfter: 5000,
                });
              } else {
                this.props.loadOrderById(this.props.orderId, true);
                this.props.loadOrderLogs(this.props.orderId);
                this.setState({
                  chargeModal: false,
                  defaultCarrier: data.payload.entity.carrier &&
                    data.payload.entity.carrier.id,
                });
              }
            }}
            showCarrier={this.props.order.customWorkOrder}
          />}

        {this.state.schedulePickupModal &&
          <SchedulePickupModal
            showModal={this.state.schedulePickupModal}
            orderId={this.props.orderId}
            toggleModal={showModal =>
              this.setState({ schedulePickupModal: showModal })}
          />}

        {this.state.documentUploadModal &&
          <DocumentUploadModal
            showModal={this.state.documentUploadModal}
            toggleModal={() => this.setState({ documentUploadModal: false })}
            orderId={this.props.orderId}
            callback={data => {
              const { error = false } = data;
              if (error) {
                this.props.addNotification({
                  message: `Error uploading document...`,
                  status: 'error',
                });
              } else {
                this.props.loadOrderLogs(this.props.orderId).then(() => {
                  this.props.loadOrderById(this.props.orderId, true);
                });
                this.props.addNotification({
                  message: `Document Succesfully Added...`,
                  status: 'success',
                });
              }
            }}
          />}

      </div>
    );
  }

  confirmationModal = () => {
    if (this.state.reconcileModalOpen) {
      return (
        <Modal
          show={this.state.reconcileModalOpen}
          onHide={() => {
            this.setState({ reconcileModalOpen: false });
          }}
        >
          <Modal.Header>
            <Modal.Title>
              Confirm Reconciliation
            </Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <div>
              Are you sure you want to reconcile all charges for this shipment?
            </div>
          </Modal.Body>
          <Modal.Footer>
            <div>
              <Button
                bsStyle="default"
                onClick={() => {
                  this.setState({ reconcileModalOpen: false });
                }}
              >
                Cancel
              </Button>
              <Button
                bsStyle="success"
                onClick={e => {
                  e.preventDefault();
                  this.reconcileChargesConfirmed();
                  this.setState({ reconcileModalOpen: false });
                }}
              >
                Reconcile
              </Button>
            </div>
          </Modal.Footer>
        </Modal>
      );
    } else if (this.state.invoiceChargeModalOpen) {
      return (
        <Modal
          show={this.state.invoiceChargeModalOpen}
          onHide={() => {
            this.setState({ invoiceChargeModalOpen: false });
          }}
        >
          <Modal.Header>
            <Modal.Title>
              Confirm Invoicing
            </Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <div>
              Are you sure you want to invoice all charges for this shipment?
            </div>
          </Modal.Body>
          <Modal.Footer>
            <div>
              <Button
                bsStyle="default"
                onClick={e => {
                  e.preventDefault();
                  this.setState({ invoiceChargeModalOpen: false });
                }}
              >
                Cancel
              </Button>
              <Button
                bsStyle="success"
                onClick={e => {
                  e.preventDefault();
                  this.generateInvoice();
                  this.setState({ invoiceChargeModalOpen: false });
                }}
              >
                Invoice
              </Button>
            </div>
          </Modal.Footer>
        </Modal>
      );
    } else {
      return null;
    }
  };

  reconcileChargesConfirmed = () => {
    this.props.reconcileAllCharges(this.props.orderId, () => {
      this.props.addNotification({
        title: 'Charges Reconciled',
        message: `Charges have been marked reconciled`,
        status: 'success',
        dismissible: false,
        dismissAfter: 5000,
      });
      this.props.loadOrderById(this.props.orderId, true);
    });
  };

  renderShippingDetails = () => {
    const {
      order: {
        shipDate = '',
        shipFrom = {},
        shipTo = {},
        packageTypeName = '',
        dangerousGoods = 'none',
        bolId = '- not set -',
        unbilledCharges = true,
        charges = {},
      } = {},
    } = this.props;

    const { order = {} } = this.props;

    let shipmentType = 'LTL';
    if (packageTypeName === 'env') shipmentType = 'Letter';
    if (packageTypeName === 'pak') shipmentType = 'Pak';
    if (packageTypeName === 'package') shipmentType = 'Package';
    if (packageTypeName === 'Pallet') shipmentType = 'LTL';

    let unreconciled = 0;
    for (let charge in charges) {
      if (charges[charge].reconciled === false) {
        unreconciled += 1;
      }
    }

    return (
      <div className="shipmentDetails-body">
        <Row className="flex">
          <Col md={3} sm={6} xs={12} className="col">
            <div
              className="jarviswidget jarviswidget-sortable jarviswidget-color-blue"
              data-widget-edit-button="false"
            >
              <header role="heading">
                <h2 />
              </header>
              <div>
                <div className="widget-body">
                  <h5 style={{ display: 'inline-block' }}>Status:</h5>
                  {' '}
                  <OrderStatusLabel
                    statusId={order.statusId}
                    statusName={order.statusName}
                  />
                  {' '}
                  {order.hasClaim
                    ? <span className="label label-yellow">CLAIM</span>
                    : null}

                  {bolId &&
                    <h3>
                      BOL: <b>{bolId}</b>
                    </h3>}
                  <h5>
                    {strings.order.tracking}:{' '}
                    {this.props.isAdmin
                      ? <EditableTextField
                          name="masterTrackingNum"
                          value={
                            order.masterTrackingNum
                              ? order.masterTrackingNum
                              : 'Set'
                          }
                          onUpdate={this.handleUpdate}
                          placeholder="Enter a tracking number"
                        />
                      : order.masterTrackingNum}
                  </h5>
                  <h5>
                    {strings.order.referenceNumber}:{' '}
                    {this.props.isAdmin
                      ? <EditableTextField
                          name="referenceCode"
                          value={
                            order.referenceCode ? order.referenceCode : 'Set'
                          }
                          onUpdate={this.handleUpdate}
                          placeholder="Enter a reference code"
                        />
                      : order.referenceCode}
                  </h5>
                </div>
              </div>
            </div>
          </Col>
          <Col md={3} sm={6} xs={12} className="col">
            {this.renderShippingAddress('Ship From', shipFrom)}
          </Col>
          <Col md={3} sm={6} xs={12} className="col">
            {this.renderShippingAddress('Ship To', shipTo)}
          </Col>
          <Col md={3} sm={6} xs={12} className="col">
            {this.renderPickupSchedule()}
          </Col>
        </Row>
        <Row>
          <Col sm={12} xs={12} className="col">
            <div
              className="jarviswidget jarviswidget-sortable jarviswidget-color-blue"
              data-widget-edit-button="false"
            >
              <header role="heading">
                <h2>Shipment Details</h2>
              </header>
              <div>
                <div className="widget-body">
                  <Row style={{ paddingBottom: '10px' }}>
                    <Col md={6} sm={12} xs={12} className="col">
                      <div className="">
                        <table className="table-list">
                          <tbody>
                            <tr>
                              <td
                                style={{
                                  verticalAlign: 'top',
                                  paddingRight: '20px',
                                }}
                              >
                                Shipment Type
                              </td>
                              <th>
                                {shipmentType}
                              </th>
                            </tr>
                            <tr>
                              <td
                                style={{
                                  verticalAlign: 'top',
                                  paddingRight: '20px',
                                }}
                              >
                                Ship Date
                              </td>
                              <th>
                                {formatDate(shipDate, 'YYYY-MM-DD')}
                              </th>
                            </tr>
                            <tr>
                              <td
                                style={{
                                  verticalAlign: 'top',
                                  paddingRight: '20px',
                                }}
                              >
                                Dangerous Goods
                              </td>
                              <th>
                                {dangerousGoods || 'None'}
                              </th>
                            </tr>
                          </tbody>
                        </table>
                      </div>
                    </Col>
                    <Col
                      md={6}
                      sm={12}
                      xs={12}
                      className="col"
                      style={{
                        borderLeft: '1px solid #ddd',
                      }}
                    >
                      <div className="">
                        <table className="table-list">
                          <tbody>
                            <tr>
                              <td
                                style={{
                                  verticalAlign: 'top',
                                  paddingRight: '20px',
                                }}
                              >
                                Carrier
                              </td>
                              <th>
                                {order.carrierName || 'Fedex'}
                              </th>
                            </tr>
                            <tr>
                              <td
                                style={{
                                  verticalAlign: 'top',
                                  paddingRight: '20px',
                                }}
                              >
                                Service
                              </td>
                              <th>
                                {order.serviceName || 'Fedex Freight'}
                              </th>
                            </tr>
                            <tr>
                              <td
                                style={{
                                  verticalAlign: 'top',
                                  paddingRight: '20px',
                                }}
                              >
                                Insurance
                              </td>
                              <th>Freightcom Insurance</th>
                            </tr>
                          </tbody>
                        </table>
                      </div>
                    </Col>
                    <Col md={6} sm={12} xs={12} className="col">
                      <div className="">
                        <table className="table-list">
                          <tbody>
                            <tr>
                              <td
                                style={{
                                  verticalAlign: 'top',
                                  paddingRight: '20px',
                                }}
                              >
                                Total Charges
                              </td>
                              <th style={{ textAlign: 'right' }}>
                                {`${(order.totalCharge || 0)
                                  .toFixed(2)
                                  .toLocaleString()}`}
                              </th>
                            </tr>
                          </tbody>
                        </table>
                      </div>
                    </Col>
                  </Row>
                  <br />
                  {this.renderShippingType()}
                </div>
              </div>
            </div>
          </Col>
          {order.hasClaim &&
            order.claim &&
            this.props.canManageClaims &&
            <ClaimsUpdateForm
              orderId={this.props.orderId}
              claim={order.claim}
              callback={({ error = false }) => {
                if (error) {
                  this.props.addNotification({
                    title: 'Claim Status Update',
                    message: `Failed to update claim status`,
                    status: 'error',
                  });
                } else {
                  this.props.loadOrderById(this.props.orderId, true);
                  this.props.loadOrderLogs(this.props.orderId);
                  this.props.addNotification({
                    title: 'Claim Status',
                    message: `Claim status has been updated.`,
                    status: 'success',
                  });
                }
              }}
            />}
          <Col sm={12} xs={12} className="col">
            <div
              className="jarviswidget jarviswidget-sortable jarviswidget-color-blue"
              data-widget-editbutton="false"
            >
              <header role="heading">
                <h2>
                  Status Updates {' '}
                  {this.props.isAdmin &&
                    <Button
                      bsStyle="primary"
                      bsSize="xs"
                      onClick={() => this.setState({ addCommentModal: true })}
                    >
                      Add Note
                    </Button>}{' '}
                  {this.props.isAdmin &&
                    this.props.order.statusChangeable &&
                    <Button
                      bsStyle="info"
                      bsSize="xs"
                      onClick={e => {
                        e.target.blur();
                        this.updateOrderStatusModal = {
                          modalTitle: 'Mark Shipment as IN-TRANSIT',
                          modalActionLabel: 'Mark as IN-TRANSIT',
                          modalAction: values => {
                            this.props.markOrderAsInTransit(
                              this.props.orderId,
                              values,
                              ({ error = false }) => {
                                if (error) {
                                  this.props.addNotification({
                                    title: 'Order Status Update',
                                    message: `Failed to update order status`,
                                    status: 'error',
                                  });
                                } else {
                                  this.props.loadOrderById(
                                    this.props.orderId,
                                    true,
                                  );
                                  this.props.loadOrderLogs(this.props.orderId);
                                  this.props.addNotification({
                                    title: 'Order Status Update',
                                    message: `Order has been marked as IN-TRANSIT`,
                                    status: 'success',
                                  });
                                }
                              },
                            );
                          },
                        };
                        this.setState({ updateOrderStatusModal: true });
                      }}
                    >
                      Mark as IN-TRANSIT
                    </Button>}{' '}
                  {this.props.isAdmin &&
                    this.props.order.statusChangeable &&
                    <Button
                      bsStyle="info"
                      bsSize="xs"
                      onClick={e => {
                        e.target.blur();
                        this.updateOrderStatusModal = {
                          modalTitle: 'Mark Shipment as DELIVERED',
                          modalActionLabel: 'Mark as DELIVERED',
                          modalAction: values => {
                            this.props.markOrderAsDelivered(
                              this.props.orderId,
                              values,
                              ({ error = false }) => {
                                if (error) {
                                  this.props.addNotification({
                                    title: 'Order Status Update',
                                    message: `Failed to update order status`,
                                    status: 'error',
                                  });
                                } else {
                                  this.props.loadOrderById(
                                    this.props.orderId,
                                    true,
                                  );
                                  this.props.loadOrderLogs(this.props.orderId);
                                  this.props.addNotification({
                                    title: 'Order Status Update',
                                    message: `Order has been marked as DELIVERED`,
                                    status: 'success',
                                  });
                                }
                              },
                            );
                          },
                        };
                        this.setState({ updateOrderStatusModal: true });
                      }}
                    >
                      Mark as DELIVERED
                    </Button>}
                </h2>
              </header>
              {this.renderShipmentStatus()}
            </div>
          </Col>
        </Row>
        {this.state.submitClaimModal &&
          this.props.order.claimCanBeFiled &&
          <Row>
            <SubmitClaimForm
              showModal={this.state.submitClaimModal}
              isFreightcom={this.props.isFreightcom}
              toggleModal={() => this.setState({ submitClaimModal: false })}
              orderId={this.props.orderId}
              callback={({ error = false }) => {
                this.setState({ submitClaimModal: false });
                if (error) {
                  this.props.addNotification({
                    message: 'Error with claim submission',
                    status: 'error',
                  });
                } else {
                  this.props.addNotification({
                    message: 'Claim submitted.',
                    status: 'success',
                  });
                  this.props
                    .loadOrderById(this.props.orderId, true)
                    .then(() => {
                      this.props.loadOrderLogs(this.props.orderId);
                    });
                }
              }}
            />
          </Row>}
        <Row>
          <Col sm={12} xs={12} className="col">
            <div
              className="jarviswidget jarviswidget-sortable jarviswidget-color-blue"
              data-widget-edit-button="false"
            >
              <header role="heading">
                <h2>
                  Charges {' '}
                  {this.props.isAdmin &&
                    <Button
                      bsStyle="primary"
                      bsSize="xs"
                      onClick={e => {
                        e.preventDefault();
                        this.toggleChargeModal();
                      }}
                    >
                      Add
                    </Button>}{' '}
                  {this.props.isAdmin &&
                    <Button
                      bsStyle="success"
                      disabled={unreconciled === 0}
                      bsSize="xs"
                      onClick={e => {
                        // e.target.blur();
                        e.preventDefault();
                        this.setState({ reconcileModalOpen: true });
                      }}
                    >
                      Reconcile
                    </Button>}{' '}
                  {this.props.isAdmin &&
                    <Button
                      bsStyle="success"
                      disabled={!unbilledCharges}
                      bsSize="xs"
                      onClick={e => {
                        e.preventDefault();
                        this.setState({ invoiceChargeModalOpen: true });
                      }}
                    >
                      Invoice Charges
                    </Button>}{' '}
                  {this.props.canManageDisputes &&
                    <Button
                      bsStyle="info"
                      bsSize="xs"
                      onClick={() => {
                        this.setState({ disputeReplyModal: true });
                      }}
                    >
                      Reply to Disputes
                    </Button>}
                  {this.props.isCustomer &&
                    <Button
                      bsStyle="primary"
                      bsSize="xs"
                      onClick={() => {
                        this.setState({ disputeModal: true });
                      }}
                    >
                      File a dispute
                    </Button>}
                </h2>
              </header>
              {this.renderCharges()}
              {this.confirmationModal()}
            </div>
          </Col>
          <Col sm={12} xs={12} className="col">
            <div
              className="jarviswidget jarviswidget-sortable jarviswidget-color-blue"
              data-widget-edit-button="false"
            >
              <header role="heading">
                <h2>
                  Documents{' '}
                  <Button
                    bsStyle="primary"
                    bsSize="xs"
                    onClick={() => {
                      this.setState({ documentUploadModal: true });
                    }}
                  >
                    Upload
                  </Button>
                </h2>
              </header>
              <table className="table table-bordered">
                <thead>
                  <tr>
                    <th>Document</th>
                    <th>Date Added</th>
                    <th>Description</th>
                    <th />
                  </tr>
                </thead>
                <tbody>
                  {this.props.order.files && this.props.order.files.length
                    ? this.props.order.files.map(item => {
                        return (
                          <tr key={item.id}>
                            <td>
                              {item.fileName}
                            </td>
                            <td>
                              {formatDate(item.dateUploaded, 'YYYY-MM-DD')}
                            </td>
                            <td>
                              {item.description}
                            </td>
                            <td>
                              <ButtonGroup className="btn-group-xs">
                                <Button
                                  bsStyle="primary"
                                  bsSize="xsmall"
                                  href={`/api/order/file/${item.id}`}
                                  download
                                >
                                  Download
                                </Button>
                                {item.canDelete &&
                                  <Button
                                    style={{ marginLeft: '1em' }}
                                    bsStyle="danger"
                                    bsSize="xsmall"
                                    onClick={e => {
                                      this.setState({
                                        confirmFileDelete: item.id +
                                          '---|||---' +
                                          item.fileName,
                                      });
                                    }}
                                  >
                                    Delete
                                  </Button>}
                              </ButtonGroup>
                            </td>
                          </tr>
                        );
                      })
                    : <tr>
                        <td colSpan={4}>No uploaded files yet</td>
                      </tr>}
                </tbody>
              </table>

              {this.state.confirmFileDelete &&
                <AlertMessage
                  title="Delete File"
                  text={
                    <span>
                      Are you sure to delete{' '}
                      <b>
                        "{this.state.confirmFileDelete.split('---|||---')[1]}"
                      </b>?
                    </span>
                  }
                  buttonToolbar={
                    <ButtonToolbar>
                      <Button
                        onClick={e => {
                          e.preventDefault();
                          this.setState({
                            confirmFileDelete: false,
                          });
                        }}
                      >
                        Cancel
                      </Button>
                      <Button
                        bsStyle="danger"
                        onClick={e => {
                          e.preventDefault();
                          const [
                            fileId,
                            fileName,
                          ] = this.state.confirmFileDelete.split('---|||---');
                          this.props.deleteOrderDocument(fileId, () => {
                            this.props.loadOrderById(this.props.orderId, true);
                            this.props.addNotification({
                              message: fileName + ' deleted!',
                              status: 'success',
                            });
                          });
                          this.setState({ confirmFileDelete: false });
                        }}
                        loading={this.state.isDeletingCharge}
                      >
                        Delete
                      </Button>
                    </ButtonToolbar>
                  }
                />}
            </div>
          </Col>
        </Row>

        {this.state.deleteCharge &&
          <AlertMessage
            title="Delete Charge"
            text={`Are you sure to delete ${this.state.deleteCharge.description}?`}
            buttonToolbar={
              <ButtonToolbar>
                <Button
                  onClick={e => {
                    e.preventDefault();
                    this.setState({
                      deleteCharge: null,
                      isDeletingCharge: false,
                    });
                  }}
                >
                  Cancel
                </Button>
                <Button
                  bsStyle="danger"
                  onClick={e => {
                    e.preventDefault();
                    this.deleteCharge();
                  }}
                  loading={this.state.isDeletingCharge}
                >
                  Delete
                </Button>
              </ButtonToolbar>
            }
          />}
      </div>
    );
  };

  renderShippingAddress = (label, address) => {
    if (!address) {
      return null;
    }
    return (
      <div
        className="jarviswidget jarviswidget-sortable jarviswidget-color-blue"
        data-widget-edit-button="false"
      >
        <header role="heading">
          <span className="widget-icon" style={{ display: 'none' }}>
            <i className="fa fa-bar-chart-o" />
          </span>
          <h2>
            {label}
          </h2>
        </header>
        <div>
          <div className="widget-body">
            {/* <div className="well no-border"> */}
            <address>
              <p className="address">
                <strong>
                  {displayCompanyOrContactName(address)}
                </strong>
              </p>
              <p className="address">
                {formatAddress(address, 2)}
              </p>
              <p className="address">
                {formatAddress(address, 3)}
              </p>
              <p className="address">
                {address.contactPhone && <abbr title="Phone">P: </abbr>}
              </p>
              <p className="address">
                {formatPhone(address.contactPhone)}
              </p>
              <br />
              <p className="address">
                <strong>
                  {address.contactName}
                </strong>
              </p>
              {address.contactEmail
                ? <a href={`mailto:${address.contactEmail}`} title={address.contactEmail}>
                    <span className="email-address">
                      {address.contactEmail}
                    </span>
                  </a>
                : ''}
              <br />
            </address>
          </div>
        </div>
        {/* </div> */}
      </div>
    );
  };

  renderShippingType = () => {
    switch (String(this.props.order.packageTypeName).toLowerCase()) {
      case 'package':
        return (
          <table className="table table-condensed table-bordered table-striped table-standout">
            <thead>
              <tr>
                <th>Dimensions</th>
                <th>Weight</th>
                <th>Insurance Amount</th>
                <th>Description</th>
              </tr>
            </thead>
            <tbody>
              {this.props.order.packages &&
                this.props.order.packages.map(this.renderPackage)}
            </tbody>
          </table>
        );
      case 'pallet':
        return (
          <table className="table table-condensed table-bordered table-striped table-standout">
            <thead>
              <tr>
                <th>Dimensions</th>
                <th>Weight</th>
                <th>Insurance Amount</th>
                <th>Freight class</th>
                <th>NMFC Code</th>
                <th>Type</th>
                <th>Description</th>
              </tr>
            </thead>
            <tbody>
              {this.props.order.pallets &&
                this.props.order.pallets.map(this.renderPallet)}
            </tbody>
          </table>
        );
      default:
        return '';
    }
  };

  renderPackage = (packageItem, index) => {
    const {
      id = index,
      width,
      length,
      height,
      weight,
      insurance = '0.00',
      insuranceAmount,
      description,
    } = packageItem;

    return (
      <tr key={'key ' + id}>
        <td>
          {width}in x {length}in x {height}in
        </td>
        <td>
          {weight} lbs.
        </td>
        <td>
          $ {insuranceAmount || insurance}
        </td>
        <td>
          {description}
        </td>
      </tr>
    );
  };

  renderPallet = (packageItem, index) => {
    const {
      id = index,
      width,
      length,
      height,
      weight,
      type,
      palletType = 'Pallet',
      freightClass,
      insuranceAmount,
      insurance = '0.00',
      nmfcCode,
      description,
    } = packageItem;

    return (
      <tr key={'key ' + id}>
        <td>
          {width}in x {length}in x {height}in
        </td>
        <td>
          {weight} lbs.
        </td>
        <td>
          $ {insuranceAmount || insurance}
        </td>
        <td>
          {freightClass}
        </td>
        <td>
          {nmfcCode}
        </td>
        <td>
          {type || palletType}
        </td>
        <td>
          {description}
        </td>
      </tr>
    );
  };

  renderShipmentStatus = () => {
    const { loggedEvents = [] } = this.props;

    return (
      <table className="table table-condensed table-bordered table-striped table-standout">
        <thead>
          <tr>
            <th width="100">Status</th>
            {this.props.isAdmin && <th width="150">Assigned By</th>}
            <th width="100">Action</th>
            <th width="200">Date</th>
            <th>Comment</th>
          </tr>
        </thead>
        <tbody>
          {loggedEvents && loggedEvents.length
            ? loggedEvents.map((event, index) => {
                if (!this.props.isAdmin && event.private) {
                  return null;
                }
                return (
                  <tr key={'le' + index}>
                    <td>
                      <OrderStatusLabel statusName={event.message} />
                    </td>
                    {this.props.isAdmin &&
                      <td>
                        {event.user
                          ? `${event.user.firstname}, ${event.user.lastname}`
                          : 'SYSTEM'}
                      </td>}
                    <td>
                      {event.action}
                      {event.private &&
                        <span className="label label-private">Private</span>}
                    </td>
                    <td>
                      {formatDate(event.createdAt, 'D MMM, YYYY HH:mm ZZ')}
                    </td>
                    <td>
                      {this.renderStatusComment(event.comment)}
                    </td>
                  </tr>
                );
              })
            : <tr />}
        </tbody>
      </table>
    );
  };

  renderStatusComment = comment => {
    const { order = {}, isAdminOrAgent } = this.props;

    const matches = comment.match(
      /^(Claim resolved.\s*)?(Credit #\d+)\s+(.*)$/,
    );

    if (isAdminOrAgent && order.customer && matches) {
      return (
        <span>
          <Link to={`/customers/${order.customer.id}/tab-content-credits`}>
            {matches[2]}
          </Link>
          {' '}
          {matches[3]}
        </span>
      );
    } else {
      return comment;
    }
  };

  renderCharges = () => {
    const {
      order: { charges = [], chargeTotalRow: orderChargesTotalRow = {} },
    } = this.props;

    if (!charges) {
      return null;
    }

    let lastInvoiceId = false;
    let lastChargeId = '';
    let rowSpanMap = charges.reduce((acc, val) => {
      if (!lastChargeId) lastChargeId = val.id;
      if (lastInvoiceId === false) lastInvoiceId = val.invoiceId;

      if (val.invoiceId) {
        acc[val.id] = 0;

        if (lastInvoiceId === val.invoiceId) {
          acc[lastChargeId] += 1;
        } else if (lastChargeId !== val.id) {
          acc[val.id] += 1;
          lastChargeId = val.id;
          lastInvoiceId = val.invoiceId;
        } else {
          lastChargeId = val.id;
          lastInvoiceId = val.invoiceId;
        }
      } else {
        acc[val.id] = 1;
      }
      return acc;
    }, {});

    return (
      <table className="table table-condensed table-bordered table-striped table-standout">
        <thead>
          <tr>
            <th>Carrier</th>
            <th>Name</th>
            <th>Charge</th>
            {this.props.isAdmin && <th>Cost</th>}
            {this.props.isAdmin && <th>Markup</th>}
            {this.props.isAdmin && <th>Commission</th>}
            {this.props.isAdmin && <th>Status</th>}
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {charges.map((charge, index) => {
            return (
              <tr key={'le' + index}>
                <td>
                  {charge.carrier && charge.carrier.name}
                </td>
                <td>
                  {charge.name}
                  {charge.description && ` - ${charge.description}`}
                </td>
                <td className="currency">
                  {formatCurrency(charge.subTotal)}
                </td>
                {this.props.isAdmin &&
                  <td className="currency">
                    {formatCurrency(charge.cost)}{' '}
                    {charge.reconciled
                      ? <Icon name="check" style={{ color: '#1b6039' }} />
                      : <Icon name="times" style={{ color: '#b60205' }} />}
                  </td>}
                {this.props.isAdmin &&
                  <td className="currency">
                    {charge.markup || '0.00'}
                  </td>}
                {this.props.isAdmin &&
                  <td className="currency">
                    {charge.commission || '0.00'}
                  </td>}

                {this.props.isAdmin &&
                  <td>
                    {this.renderChargeStatus(charge)}
                  </td>}
                {rowSpanMap[charge.id] > 0 &&
                  <td
                    rowSpan={rowSpanMap[charge.id]}
                    style={{ verticalAlign: 'middle' }}
                  >
                    {this.renderActionButtons(charge)}
                  </td>}
              </tr>
            );
          })}
        </tbody>

        <tfoot>
          <tr>
            <td colSpan="2" className="currency">
              <b>Total</b>
            </td>
            <td className="currency">
              <b>
                {formatCurrency(orderChargesTotalRow.subTotal)}
              </b>
            </td>
            {this.props.isAdmin &&
              <td className="currency">
                <b>
                  {formatCurrency(orderChargesTotalRow.cost)}
                </b>
              </td>}
            {this.props.isAdmin &&
              <td className="currency">
                <b>
                  {formatCurrency(orderChargesTotalRow.markup)}
                </b>
              </td>}
            {this.props.isAdmin &&
              <td className="currency">
                <b>
                  {formatCurrency(orderChargesTotalRow.commission)}
                </b>
              </td>}
            {this.props.isAdmin && <td />}
            <td />
          </tr>
          {this.props.isCustomer ? null : <tr>
            <td className="status-legend">
              <div>
                <b>Status Legend</b>
              </div>
            </td>
            <td colSpan="7" className="status-legend">
              <div className="status-legend status-label">
                <span className="label label-primary">B</span> - Invoiced
              </div>
              <div className="status-legend status-label">
                <span className="label label-success">C</span>
                {' '}
                - Commission Reported
              </div>
              <div className="status-legend status-label">
                <span className="label label-success">P</span>
                {' '}
                - Payable Reported
              </div>
              <div className="status-legend status-label">
                <span className="label label-danger">D</span> - Disputed
              </div>
              <div className="status-legend status-label">
                <span className="label label-warning">Unbilled</span> - Unbilled
              </div>
            </td>
          </tr>}
        </tfoot>
      </table>
    );
  };

  renderActionButtons = charge => {
    switch (true) {
      case !charge.invoiceId &&
        !charge.payableReported &&
        !charge.commissionReported &&
        !this.props.isCustomer:
        return (
          <div>
            <Button
              bsStyle="primary"
              bsSize="xs"
              disabled={!this.props.isAdmin}
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
              disabled={!this.props.isAdmin}
              onClick={e => {
                e.preventDefault();
                this.setState({ deleteCharge: charge });
              }}
            >
              Delete
            </Button>
          </div>
        );
      case charge.invoiceId !== null:
        return (
          <Link
            className="btn btn-xs btn-primary"
            to={`/invoices/${charge.invoiceId}`}
          >
            View Invoice #: {charge.invoiceId}
          </Link>
        );
      default:
        return null;
    }
  };

  renderPickupSchedule = () => {
    const { scheduledPickup = {} } = this.props.order;

    if (!scheduledPickup) return null;
    if (scheduledPickup.id) {
      return (
        <div
          className="jarviswidget jarviswidget-sortable jarviswidget-color-blue"
          data-widget-edit-button="false"
        >
          <header role="heading">
            <h2>Pickup Schedule</h2>
          </header>
          <div>
            <div className="widget-body">
              <table className="table-list">
                <tbody>
                  {scheduledPickup.contactName &&
                    <tr>
                      <td colspan="2">
                        Contact Name: <b>{scheduledPickup.contactName}</b>
                        <br />
                        Contact Phone: {scheduledPickup.contactPhone}
                        <br />
                        Contact Email: <span className="email-address">{scheduledPickup.contactEmail}</span>
                      </td>
                    </tr>}
                  <tr>
                    <td style={{ verticalAlign: 'top', paddingRight: '10px' }}>
                      Pickup Date:{' '}
                    </td>
                    <th>
                      <b>{scheduledPickup.pickupDate}</b>
                    </th>
                  </tr>
                  <tr>
                    <td style={{ verticalAlign: 'top', paddingRight: '10px' }}>
                      Pickup Ready Time:
                    </td>
                    <th>
                      <b>{scheduledPickup.pickupReadyTime}</b>
                    </th>
                  </tr>
                  <tr>
                    <td style={{ verticalAlign: 'top', paddingRight: '10px' }}>
                      Pickup Close Time:{' '}
                    </td>
                    <th>
                      <b>{scheduledPickup.pickupCloseTime}</b>
                    </th>
                  </tr>
                  <tr>
                    <td colSpan="3">
                      Pickup Instructions: <br />
                      <br />
                      {scheduledPickup.pickupInstructions}
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      );
    } else {
      return null;
    }
  };

  toggleChargeModal = (charge = 'new') => {
    if (this.state.chargeModal) {
      this.setState({ chargeModal: !this.state.chargeModal, charge: 'new' });
    } else {
      this.setState({ chargeModal: !this.state.chargeModal, charge: charge });
    }
  };

  confirmDocumentDelete = e => {};

  handleUpdate = (name, value) => {
    const order = {
      id: this.props.orderId,
      [name]: value,
    };
    this.props.updateOrder(order.id, order, data => {
      this.props.loadOrderById(this.props.orderId, true);
    });
  };

  generateInvoice = () => {
    const { order, generateInvoice, addNotification } = this.props;
    generateInvoice(
      {
        orders: [{ id: order.id }],
      },
      data => {
        addNotification({
          title: strings.order.invoiceGeneratedTitle,
          message: `<a href="/invoices/${data.id}">Invoice</a> for ${data.customer.name} has been generated...`,
          status: 'success',
          allowHTML: true,
          dismissible: false,
          dismissAfter: 10000,
          buttons: [
            {
              name: 'OK',
              primary: true,
            },
          ],
        });

        this.props.loadOrderById(this.props.orderId, true);
      },
    );
  };

  deleteCharge = () => {
    this.setState({
      isDeletingCharge: true,
    });

    this.props.deleteOrderCharge(this.state.deleteCharge.id, () => {
      //this fix a bug where row doesn't get deleted in the frontend
      this.props.removeOneEntity('order', this.props.order.id, 'charges');

      this.props.addNotification({
        title: 'Charge deleted',
        message: `Charge has been removed`,
        status: 'success',
        dismissible: false,
        dismissAfter: 5000,
      });
      this.props.loadOrderById(this.props.order.id, true);
      this.setState({ isDeletingCharge: false, deleteCharge: null });
    });
  };

  renderChargeStatus = charge => {
    let invoiceLabel = null;
    let commissionLabel = null;
    let payableLabel = null;
    let disputeLabel = null;

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
      charge.disputed.toString() === 'true' &&
      charge.disputeResolved.toString() === 'false'
    ) {
      disputeLabel = (
        <span className="label label-danger">
          D
        </span>
      );
    }

    if (
      !charge.invoiceId &&
      !charge.commissionReported &&
      !charge.payableReported
    ) {
      return (
        <div>
          <span className="label label-warning">
            Unbilled
          </span>{' '}
          {disputeLabel}
        </div>
      );
    }
    return (
      <div>
        {invoiceLabel} {commissionLabel} {payableLabel} {disputeLabel}
      </div>
    );
  };
}

const mapStateToProps = state => {
  const { id: tabId, orderId = '', command } = selectActiveTab(state);
  const { loggedIn: { role = {} } } = state;
  return {
    tabId,
    orderId,
    command,
    order: selectOneFromEntities(state, 'order', orderId, [
      { entity: 'shippingAddress', key: 'shipFrom' },
      { entity: 'shippingAddress', key: 'shipTo' },
      { entity: 'customer', key: 'customer' },
      { entity: 'carrier', key: 'carrier' },
    ]),
    loggedEvents: selectStatusLogsByOrderId(state, orderId),
    activeRole: role,
    isFreightcom: checkRoleIfFreightcom(state),
    isAdmin: checkRoleIfFreightcomAdmin(state),
    isCustomer: checkRoleIfCustomer(state),
    isAdminOrAgent: checkRoleIfAdminOrAgent(state),
    canManageClaims: checkIfUserCanManageClaims(state),
    canManageDisputes: checkIfUserCanManageDisputes(state),
    shipmentTerms: getSystemProperty(state, 'shipment_terms'),
  };
};

const mapDispatchToProps = {
  loadOrderById,
  loadOrderLogs,
  duplicateOrder,
  removeTab,
  updateOrder,
  deleteOrderCharge,
  generateInvoice,
  addNotification,
  reconcileAllCharges,
  loadSettings,
  removeOneEntity,
  disputeCharge,
  replyDisputedCharge,
  markOrderAsInTransit,
  markOrderAsDelivered,
  deleteOrderDocument,
};

export default connect(mapStateToProps, mapDispatchToProps)(OrderView);
