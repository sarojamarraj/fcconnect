import React, { PureComponent } from 'react';

class OrderStatusLabel extends PureComponent {
  render() {
    const { statusName = 'Invalid' } = this.props;

    let label = 'label-default';
    switch (String(statusName).toUpperCase()) {
      case 'DELIVERED':
        label = 'label-primary';
        break;

      case 'IN TRANSIT':
      case 'PREDISPATCHED':
        label = 'label-info';
        break;

      case 'READY FOR SHIPPING':
      case 'READY FOR DISPATCH':
        label = 'label-success';
        break;

      case 'CANCELLED':
      case 'DELETED':
      case 'DISPUTED':
      case 'EXCEPTION':
      label = 'label-warning';
      break;
      
      case 'WAITING BORDER':
      case 'MISSED PICKUP':
        label = 'label-danger';
        break;

      case 'BOOKED':
      case 'SCHEDULED':
        label = 'label-pending';
        break;

      case 'QUOTED':
      case 'DRAFT':
        label = 'label-gray';
        break;

      case 'CUSTOMER COMMENT':
        label = 'label-comment';
        break;

      default:
        label = 'label-default';
        break;
    }

    return (
      <span className={'label ' + label}>
        {statusName && statusName.toUpperCase()}
      </span>
    );
  }
}

export default OrderStatusLabel;
