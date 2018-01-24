import React, { Component } from 'react';
import { connect } from 'react-redux';

import { Button } from 'react-bootstrap';

import Icon from '../../components/Icon';

import DataTable from '../../components/DataTable';

import { formatCurrency } from '../../utils';
import { getPageItems } from '../../reducers';
import { loadDisputeList, viewOrder } from '../../actions/orders';

import {
  registerChangedInterest,
  hasChangedInterest,
  clearChangedObjects,
} from '../../reducers/changed-objects';

const DISPUTE_LIST = 'dispute-list';
const DISPUTE = 'dispute';

class DisputeList extends Component {
  constructor(props) {
    super(props);

    this.state = {
      initialLoad: this.props.active,
    };

    registerChangedInterest(DISPUTE_LIST, DISPUTE);

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
        label: 'Age of Dispute',
        field: 'disputeAgeInDays',
      },
      {
        label: 'Amount Disputed',
        field: 'disputeAmount',
        cell: order => formatCurrency(order.disputeAmount),
        className: 'currency',
      },
      {
        label: 'Recent Comments',
        field: 'disputeLatestComment',
      },
      {
        cell: order => (
          <Button
            bsStyle="primary"
            bsSize="xsmall"
            onClick={e => this.props.viewOrder(order.id)}
          >
            <Icon name="file" /> View
          </Button>
        ),
      },
    ];
  }

  componentWillReceiveProps(nextProps) {
    if (
      !this.props.active &&
      nextProps.active &&
      (!this.state.initialLoad || this.props.changedObjects)
    ) {
      this.setState({ initialLoad: true }, () =>
        this.props
          .clearChangedObjects(DISPUTE_LIST)
          .then(() => this.props.loadDisputeList()),
      );
    }
  }

  componentWillMount() {
    if (this.state.initialLoad) {
      this.props.loadDisputeList();
    }
  }

  render() {
    return (
      <DataTable
        className="no-margin"
        loadData={this.props.loadDisputeList}
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
  datatableData: getPageItems(state, 'disputeList'),
  changedObjects: hasChangedInterest(state, DISPUTE_LIST).length > 0,
});

const mapDispatchToProps = { loadDisputeList, viewOrder, clearChangedObjects };

export default connect(mapStateToProps, mapDispatchToProps)(DisputeList);
