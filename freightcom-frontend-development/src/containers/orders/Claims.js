import React, { Component } from 'react';
import { connect } from 'react-redux';

import { Button } from 'react-bootstrap';

import Icon from '../../components/Icon';

import DataTable from '../../components/DataTable';

import { formatCurrency } from '../../utils';
import { getPageItems } from '../../reducers';
import { loadClaims, viewOrder } from '../../actions/orders';

class ClaimList extends Component {
  constructor(props) {
    super(props);

    this.state = {
      initialLoad: this.props.active
    };

    this.datatable = null;
    this.columns = [
      {
        label: 'Order',
        field: 'id',
      },
      {
        label: 'Ship Date',
        field: 'shipDate',
      },
      {
        label: 'Customer',
        field: 'customer',
        cell: order => order.customer.name,
      },
      {
        label: 'Claim Status',
        field: 'claimStatus',
        cell: order => {
          return (
            <div>
              {(order.claimStatus.toUpperCase() === 'NEW_CLAIM') 
                ? <span className="label label-pending">
                  {'NEW CLAIM'}
                  </span> : 
                  (order.claimStatus.toUpperCase() === 'IN-PROCESS')
                ? <span className="label label-info">
                  {'IN PROCESS'}
                  </span> :
                  (order.claimStatus.toUpperCase() === 'SETTLED')
                ? <span className="label label-primary">
                  {'SETTLED'}
                  </span> :
                  (order.claimStatus.toUpperCase() === 'CLAIM_DENIED')
                ? <span className="label label-danger">
                  {'CLAIM DENIED'}
                  </span> :
                <span className="label label-default">
                  {order.claimStatus.replace('_', ' ')}
                </span>}
            </div>
          );
        },
      },
      {
        label: 'Age of Claim',
        field: 'claimAgeInDays',
      },
      {
        label: 'Amount Claimed',
        field: 'claimAmount',
        cell: order => formatCurrency(order.claimAmount),
        className: 'currency',
      },
      {
        label: 'Recent Comments',
        field: 'claimLatestComment',
      },
      {
        cell: order =>
          <Button
            bsStyle="primary"
            bsSize="xsmall"
            onClick={e => this.props.viewOrder(order.id)}
          >
            <Icon name="file" /> View
          </Button>,
      },
    ];
  }

  componentWillReceiveProps(nextProps) {
    if (!this.state.initialLoad && !this.props.active && nextProps.active) {
      this.setState({ initialLoad: true }, () => {
        this.props.loadClaims();
      });
    }
  }

  componentWillMount() {
    if (this.state.initialLoad) {
      this.props.loadClaims();
    }
  }

  render() {
    return (
      <DataTable
        className="no-margin"
        loadData={this.props.loadClaims}
        columns={this.columns}
        ref={dt => (this.datatable = dt)}
        renderTableFilters={false}
        renderPagination={false}
        {...this.props.datatableData}
      />
    );
  }
}

const mapStateToProps = state => ({
  datatableData: getPageItems(state, 'claimList'),
});

const mapDispatchToProps = { loadClaims, viewOrder };

export default connect(mapStateToProps, mapDispatchToProps)(ClaimList);
