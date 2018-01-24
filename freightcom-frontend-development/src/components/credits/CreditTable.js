import React, { Component } from 'react';
import { connect } from 'react-redux';
import DataTable from '../DataTable';
import { getPageItems } from '../../reducers';
import { loadCredits } from '../../actions/credits';
import CustomerAutoSuggest from '../../components/customers/CustomerAutoSuggest';
import { DatePicker } from '../../components/forms/inputs/InputDate';

class CreditTable extends Component {
  constructor(props) {
    super(props);
    this.creditList = null;
    this.columns = [
      { width: '80', label: 'Id', field: 'id' },
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
        label: 'Customer',
        field: 'customerId',
        filter: filter => {
          return (
            <CustomerAutoSuggest
              onChange={customer =>
                filter({ customerId: customer ? customer.id : '' })}
            />
          );
        },
      },
      { width: '80', label: 'Amount', field: 'amount' },
      { width: '80', label: 'Balance', field: 'amountRemaining' },
      { width: '80', label: 'Note', field: 'note' },
    ];
  }

  componentWillMount() {
    this.props.loadCredits();
  }

  render() {
    return (
      <div>
        <DataTable
          columns={this.columns}
          loadData={this.props.loadCredits}
          items={this.props.items}
          currentPage={this.props.currentPage}
          numberOfPages={this.props.numberOfPages}
          totalItems={this.props.totalItems}
          isFetching={this.props.isFetching}
          ref={datatable => (this.creditList = datatable)}
        />
      </div>
    );
  }
}

const mapStateToProps = state => {
  return getPageItems(state, 'credit');
};

const mapDispatchToProps = { loadCredits };

export default connect(mapStateToProps, mapDispatchToProps)(CreditTable);
