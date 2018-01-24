import React from 'react';
import { Button } from 'react-bootstrap';
import { Link } from 'react-router';
import Icon from '../../components/Icon';
import { SHIPMENT_TYPES } from '../../constants';
import SchedulePickupModal from './SchedulePickupModal';
import SubmitClaimModal from './SubmitClaimModal';
import UploadPODModal from './UploadPODModal';
import UpdateOrderStatusModal from './UpdateOrderStatusModal';

export const getStatusActions = (container, order) => {
  const {
    id = '',
    statusId = '',
    trackingUrl = null,
    cancellable,
    trackable,
    claimCanBeFiled,
  } = order;
  const orderStatusId = String(statusId);

  return (
    <div className="btn-group" style={{ width: '85px' }}>
      <Button
        bsStyle="primary"
        bsSize="xs"
        onClick={e => container.props.viewOrder(id)}
      >
        <Icon name="file" /> View
      </Button>
      <Button
        bsStyle="primary"
        bsSize="xs"
        className="dropdown-toggle"
        data-toggle="dropdown"
        aria-haspopup="true"
        aria-expanded="false"
      >
        <span className="caret" />
        <span className="sr-only">Toggle Dropdown</span>
      </Button>
      <ul className="dropdown-menu dropdown-menu-right">
        <li>
          <a onClick={e => container.props.viewOrder(id)}>
            <Icon name="file-o" /> View
          </a>
        </li>
        <li>
          <a onClick={e => container.props.duplicateOrder(id)}>
            <Icon name="files-o" /> Duplicate
          </a>
        </li>
        {container.props.isFreightcomAdmin &&
          order.unbilledCharges > 0 &&
          <li>
            <a
              onClick={e => {
                container.setState({
                  checkedOrderList: [order],
                  confirmInvoiceDialog: !container.state.confirmInvoiceDialog,
                });
              }}
            >
              <Icon name="server" /> Generate Invoice
            </a>
          </li>}

        {container.props.isAdmin &&
          <li>
            <a
              onClick={e => {
                container.setState({
                  uploadPODModal: true,
                  activeOrderId: id,
                });
              }}
            >
              <Icon name="file-pdf-o" /> Upload POD
            </a>
          </li>}

        {order.packageTypeName !== SHIPMENT_TYPES.LTL && !order.scheduledPickup
          ? <li>
              <a
                href="#"
                onClick={e => {
                  e.preventDefault();
                  container.setState({
                    activeOrderId: id,
                    schedulePickupModal: true,
                  });
                }}
              >
                <Icon name="calendar-o" /> Schedule Pickup
              </a>
            </li>
          : ''}
        {cancellable
          ? <li>
              <a
                href="#"
                onClick={e => {
                  e.preventDefault();
                  container.props.cancelOrder(id, result => {
                    container.datatable.loadData();
                  });
                }}
              >
                <Icon name="ban" /> Cancel
              </a>
            </li>
          : ''}
        {trackable
          ? <li>
              <a href={trackingUrl} target="_blank">
                <Icon name="globe" /> Track It
              </a>
            </li>
          : ''}
        {claimCanBeFiled
          ? <li>
              {' '}
              <Link to={`/orders/${id}/claim`}>
                {' '}<Icon name="flag-o" /> Submit Claim{' '}
              </Link>
              {' '}
            </li>
          : ''}
        {orderStatusId === '9'
          ? <li>
              <Link to={'/order/claim/' + id}>
                <Icon name="calendar-o" /> Reschedule
              </Link>
            </li>
          : ''}
        {orderStatusId === '15'
          ? <li>
              <Link to={'/order/dispute/' + id}>
                <Icon name="flag-o" /> Dispute
              </Link>
            </li>
          : ''}
        {orderStatusId === '999'
          ? <li>
              <a
                onClick={e => container.props.duplicateOrder(id)}
                className="text-danger"
              >
                <span
                  className="glyphicon glyphicon-trash"
                  aria-hidden="true"
                />
                {' '}
                Delete Order
              </a>
            </li>
          : ''}
      </ul>
    </div>
  );
};

export const shipmentListModals = container => {
  return [
    <SchedulePickupModal
      key="SchedulePickupModal"
      orderId={container.state.activeOrderId}
      showModal={container.state.schedulePickupModal}
      toggleModal={showModal =>
        container.setState({
          activeOrderId: '',
          schedulePickupModal: showModal,
        })}
    />,
    <UploadPODModal
      key="UploadPODModal"
      orderId={container.state.activeOrderId}
      showModal={container.state.uploadPODModal}
      reloadOrders={container.loadOrders}
      toggleModal={showModal =>
        container.setState({
          activeOrderId: '',
          uploadPODModal: showModal,
        })}
      callback={data => {
        const { error = false } = data;
        if (error) {
          container.props.addNotification({
            message: `Error uploading POD...`,
            status: 'error',
          });
        } else {
          container.loadOrders();
          container.props.addNotification({
            message: 'POD Uploaded.',
            status: 'success',
          });
        }

        container.setState({
          activeOrderId: '',
          uploadPODModal: false,
        });
      }}
    />,
    <SubmitClaimModal
      key="SubmitClaimModal"
      showModal={container.state.submitClaimModal}
      isFreightcom={container.props.isAdmin}
      toggleModal={() =>
        container.setState({ submitClaimModal: false, activeOrderId: '' })}
      orderId={container.state.activeOrderId}
      callback={({ error = false }) => {
        container.setState({ submitClaimModal: false, activeOrderId: '' });
        if (error) {
          container.props.addNotification({
            message: 'Error with claim submission',
            status: 'error',
          });
        } else {
          container.props.addNotification({
            message: 'Claim submitted.',
            status: 'success',
          });
        }
        container.paginate(container.props.currentPage);
      }}
    />,
    container.state.updateOrderStatusModal &&
      <UpdateOrderStatusModal
        key="UpdateOrderStatusModal"
        showModal={container.state.updateOrderStatusModal}
        toggleModal={() =>
          container.setState({ updateOrderStatusModal: false })}
        modalTitle={container.updateOrderStatusModal.modalTitle}
        modalActionLabel={container.updateOrderStatusModal.modalActionLabel}
        modalAction={container.updateOrderStatusModal.modalAction}
      />,
  ];
};
