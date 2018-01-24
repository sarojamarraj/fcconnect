import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Button } from 'react-bootstrap';
import DataTable from '../DataTable';
import { getPageItems } from '../../reducers';
import {
  loadCreditsByCustomer,
  addCredit,
  refundCredit,
} from '../../actions/credits';
import { loadCustomerById } from '../../actions/customers';
import { DatePicker } from '../../components/forms/inputs/InputDate';

import { formatCurrency } from '../../utils';
import { Link } from 'react-router';
import CustomerAddCreditModal from './CustomerAddCreditModal';
import CustomerRefundCreditModal from './CustomerRefundCreditModal';

const mapCreditAvailable = creditAvailable =>
  Object.entries(creditAvailable || {}).map(([key, value]) => (
    <span key={key}>&nbsp;{key.toUpperCase() + ': ' + value.toFixed(2)}</span>
  ));

class CustomerCredits extends Component {
  constructor(props) {
    super(props);
    this.creditList = null;
    this.columns = [
      {
        width: '80',
        label: 'Date Created',
        field: 'createdAt',
        filter: filter => {
          return (
            <DatePicker
              className="form-control"
              placeholder="Date Created"
              dateFormat="yy-mm-dd"
              input={{
                onChange: value => filter({ createdAt: value }),
              }}
            />
          );
        },
      },
      {
        width: '80',
        label: 'Currency',
        field: 'currency',
      },
      {
        width: '80',
        label: 'Amount',
        field: 'amount',
        className: 'currency',
        cell: credit => {
          return formatCurrency(credit.amount);
        },
      },
      {
        width: '80',
        label: 'Balance',
        field: 'amountRemaining',
        className: 'currency',
        cell: credit => formatCurrency(credit.amountRemaining),
      },
      {
        width: '80',
        label: 'Note',
        field: 'note',
        cell: credit => {
          const matches = credit.note && credit.note.match(/order #(\d+)$/i);

          if (matches) {
            return <Link to={`/orders/${matches[1]}`}>{credit.note}</Link>;
          } else {
            return credit.note;
          }
        },
      },
    ];

    this.creditToAdd = { customerId: props.customerId };
    this.creditToRefund = { customerId: props.customerId };
    this.state = {
      addCreditModal: false,
      refundCreditModal: false,
    };
  }

  componentWillMount() {
    this.props.loadCreditsByCustomer(this.props.customerId);
  }

  render() {
    const { activeRole = {}, customerId, customer = {} } = this.props;

    return (
      <div>
        <span>
          Total Credits:&nbsp;
          {mapCreditAvailable(customer.creditAvailable)}
        </span>
        {activeRole.canManageCredits &&
          <Button
            bsStyle="primary"
            style={{ marginLeft: '1em' }}
            onClick={e => {
              this.setState({ addCreditModal: true });
            }}
            disabled={this.state.addCreditModal}
          >
            Add Credit
          </Button>}
        {activeRole.canManageCredits &&
          <Button
            bsStyle="primary"
            style={{ marginLeft: '1em' }}
            onClick={e => {
              this.setState({ refundCreditModal: true });
            }}
            disabled={this.state.refundCreditModal || !customer.hasCredit}
          >
            Refund Credit
          </Button>}

        <DataTable
          columns={this.columns}
          //loadData={this.props.loadCredits}
          items={this.props.items}
          currentPage={this.props.currentPage}
          numberOfPages={this.props.numberOfPages}
          totalItems={this.props.totalItems}
          isFetching={this.props.isFetching}
          ref={datatable => (this.creditList = datatable)}
          renderTableFilters={false}
        />
        {this.renderAddCreditModal(customerId)}
        {this.renderRefundCreditModal(customerId)}
      </div>
    );
  }

  renderAddCreditModal = customerId => {
    const { addCreditModal } = this.state;

    return (
      <CustomerAddCreditModal
        show={addCreditModal}
        cancel={e => this.setState({ addCreditModal: false })}
        submit={creditToAdd =>
          this.props.addCredit({ ...creditToAdd, customerId }, () => {
            this.props.loadCustomerById(customerId);
            this.props.loadCreditsByCustomer(customerId);
            this.setState({ addCreditModal: false });
          })}
      />
    );
  };

  renderRefundCreditModal = customerId => {
    const { refundCreditModal } = this.state;

    return (
      <CustomerRefundCreditModal
        show={refundCreditModal}
        cancel={e => this.setState({ refundCreditModal: false })}
        submit={creditToRefund =>
          this.props.refundCredit({ ...creditToRefund, customerId }, () => {
            this.props.loadCustomerById(customerId);
            this.props.loadCreditsByCustomer(customerId);
            this.setState({ refundCreditModal: false });
          })}
      />
    );
  };
}

const mapStateToProps = (state, ownProps) => {
  const { loggedIn: { role = {} } } = state;
  const customer = state.entities.customer[ownProps.customerId] || null;

  return {
    ...getPageItems(state, 'credit'),
    activeRole: role,
    customer,
  };
};

const mapDispatchToProps = {
  loadCreditsByCustomer,
  addCredit,
  refundCredit,
  loadCustomerById,
};

export default connect(mapStateToProps, mapDispatchToProps)(CustomerCredits);
