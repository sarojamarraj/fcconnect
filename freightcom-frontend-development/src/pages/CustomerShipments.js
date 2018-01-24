import React, { Component } from 'react';
import { connect } from 'react-redux';

import Widget from '../components/Widget';
import DataTable from '../components/DataTable';

import { loadSubmittedOrders } from '../actions/orders';
import { getPageItems, selectAllFromEntities } from '../reducers';

import { displayCompanyOrContactName, formatAddress } from '../utils';
import OrderStatusLabel from '../components/orders/OrderStatusLabel';

class CustomerShipments extends Component {
  constructor(props) {
    super(props);
    this.filters = {};
    this.datatable = null;
    this.columns = [
      { label: 'Order #', field: 'id', width: '50px' },
      {
        label: 'Customer',
        field: 'customer',
        cell: order => {
          return <h5>{order.customer.name}</h5>;
        },
      },
      {
        label: 'Ship From',
        field: 'origin',
        cell: order => {
          return (
            <div>
              <h4>{displayCompanyOrContactName(order.shipFrom)}</h4>
              <span>{formatAddress(order.shipFrom)}</span>
            </div>
          );
        },
      },
      {
        label: 'Ship To',
        field: 'destination',
        cell: order => {
          return (
            <div>
              <h4>{displayCompanyOrContactName(order.shipTo)}</h4>
              <span>{formatAddress(order.shipTo)}</span>
            </div>
          );
        },
      },
      {
        label: 'Status',
        field: 'statusId',
        cell: order => (
          <OrderStatusLabel
            statusId={order.statusId}
            statusName={order.statusName}
          />
        ),
      },
    ];
  }

  componentWillMount() {
    this.props.loadSubmittedOrders();
  }

  render() {
    return (
      <Widget title="Customer's Shipments">
        <DataTable
          columns={this.columns}
          loadData={this.props.loadSubmittedOrders}
          items={this.props.items}
          currentPage={this.props.currentPage}
          numberOfPages={this.props.numberOfPages}
          totalItems={this.props.totalItems}
          isFetching={this.props.isFetching}
          ref={datatable => (this.datatable = datatable)}
          renderTableFilters={false}
        />
      </Widget>
    );
  }

  renderTableFilters = () => {};
}

const mapStateToProps = state => {
  return {
    ...getPageItems(state, 'order', [
      { entity: 'shippingAddress', key: 'shipFrom' },
      { entity: 'shippingAddress', key: 'shipTo' },
      //{ entity: 'orderStatus', key: 'orderStatus' },
      { entity: 'customer', key: 'customer' },
    ]),
    orderStatuses: selectAllFromEntities(state, 'orderStatus'),
    packageTypes: selectAllFromEntities(state, 'packageType'),
  };
};

const mapDispatchToProps = { loadSubmittedOrders };

export default connect(mapStateToProps, mapDispatchToProps)(CustomerShipments);
