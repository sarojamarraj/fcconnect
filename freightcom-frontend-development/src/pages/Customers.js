import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link, browserHistory } from 'react-router';

import { Row, Col, ButtonToolbar, Button } from 'react-bootstrap';

import Icon from '../components/Icon';

import { loadCustomers, updateCustomerStatus } from '../actions/customers';
import { getPageItems } from '../reducers';
import { formatAddress } from '../utils';

import SubAgentAutoSuggest from '../components/agents/SubAgentAutoSuggest';

import Widget from '../components/Widget';
import DataTable from '../components/DataTable';

const mapCreditAvailable = creditAvailable =>
  Object.entries(creditAvailable || {}).map(([key, value]) => (
    <span key={key}>&nbsp;{key.toUpperCase() + ': ' + value.toFixed(2)}</span>
  ));

class Customers extends Component {
  constructor(props) {
    super(props);
    this.customerList = null;
    this.state = {
      showFilter: false,
    };
    this.filters = { active: 1 };
    this.columns = [
      { width: '30', label: 'ID', field: 'id', filter: false },
      {
        width: '160',
        label: 'Company',
        field: 'name',
        filter: filter => (
          <input
            type="text"
            className="form-control input-sm"
            autoFocus
            onChange={e => filter({ name: e.target.value })}
          />
        ),
        cell: item => <b>{item.name}</b>,
      },
      { label: 'Address', field: 'address', cell: item => formatAddress(item) },
      {
        width: '160',
        label: 'Agent',
        field: 'agentName',
        filter: (filter, filterValue) => (
          <SubAgentAutoSuggest
            value={filterValue._agent}
            onChange={value =>
              filter({ _agent: value, agentId: value && value.id })}
          />
        ),
      },
      {
        label: 'Status',
        field: 'active',
        filter: (filter, filterValues) => (
          <select
            className="form-control"
            value={filterValues.active || ''}
            onChange={e => filter({ active: e.target.value })}
          >
            <option value="">All</option>
            <option value="1">Active</option>
            <option value="0">Inactive</option>
          </select>
        ),
        cell: item =>
          item.active
            ? <span className="label label-success">
                Active
              </span>
            : <span className="label label-warning">
                Inactive
              </span>,
      },
      {
        label: 'Available Credit',
        cell: item => mapCreditAvailable(item.creditAvailable),
        filter: false,
      },
      {
        width: '10',
        cell: item => (
          <Link
            to={'/customers/' + item.id}
            className="btn btn-xs btn-primary btn-row-single-action"
          >
            Edit
          </Link>
        ),
      },
    ];
  }

  componentWillMount() {
    this.props.loadCustomers(1, 10, this.filters);
  }

  render() {
    return (
      <Widget title="Manage Customers">
        {this.state.showFilter && this.renderCustomFilters()}
        <DataTable
          className="no-margin"
          filters={this.filters}
          columns={this.columns}
          loadData={this.props.loadCustomers}
          items={this.props.items}
          currentPage={this.props.currentPage}
          numberOfPages={this.props.numberOfPages}
          totalItems={this.props.totalItems}
          isFetching={this.props.isFetching}
          ref={datatable => (this.customerList = datatable)}
          _renderTableFilters={this.renderTableActions}
          groupedFilters
          actionButtons={
            <Button
              bsStyle="primary"
              bsSize="small"
              className="pull-right"
              onClick={e => browserHistory.push('/customers/add')}
            >
              <Icon name="plus" /> New Customer
            </Button>
          }
        />
      </Widget>
    );
  }

  renderTableActions = () => {
    return (
      <tr>
        <td colSpan={this.columns.length}>
          <ButtonToolbar>
            <Button
              bsStyle="primary"
              bsSize="small"
              className="pull-right"
              onClick={e => browserHistory.push('/customers/add')}
            >
              <Icon name="plus" /> New Customer
            </Button>

            {!this.state.showFilter &&
              <Button
                bsStyle="default"
                bsSize="small"
                className="pull-right"
                onClick={e => this.setState({ showFilter: true })}
              >
                <Icon name="search" /> Search
              </Button>}
          </ButtonToolbar>
        </td>
      </tr>
    );
  };

  renderCustomFilters = () => {
    return (
      <div className="panel panel-primary advanced-filters">
        <div className="panel-body">
          <Row>
            <Col sm={12}>
              <a
                href="#"
                className="pull-right"
                onClick={e => {
                  e.preventDefault();
                  this.setState({ showFilter: false });
                }}
              >
                <Icon name="times" />
              </a>
            </Col>
          </Row>
          <br />

          <div
            className="form-horizontal"
            onKeyUp={e => {
              const isCombobox = e.target.getAttribute('role') === 'combobox';
              if (!isCombobox) {
                switch (e.key) {
                  case 'Enter':
                    this.applyFilters(e);
                    break;
                  case 'Escape':
                    this.setState({ showFilter: false });
                    break;
                  default:
                }
              }
            }}
          >
            <Row>
              {this.columns.map((col, idx) => {
                if (col.filter === false || !col.label) return null;
                return (
                  <Col key={idx} sm={6}>
                    <div className="form-group">
                      <label className="control-label col-sm-4">
                        {col.label}
                      </label>
                      <div className="col-sm-8">
                        {typeof col.filter === 'function'
                          ? col.filter(this.addFilter)
                          : <input
                              type="text"
                              className="form-control input-sm"
                              onChange={e =>
                                this.addFilter({ [col.field]: e.target.value })}
                            />}
                      </div>
                    </div>
                  </Col>
                );
              })}
            </Row>

          </div>

          <ButtonToolbar className="pull-right">
            <Button
              bsStyle="default"
              bsSize="small"
              onClick={this.clearFilters}
            >
              Clear
            </Button>
            <Button
              bsStyle="primary"
              bsSize="small"
              onClick={this.applyFilters}
            >
              Search
            </Button>

          </ButtonToolbar>
        </div>
      </div>
    );
  };

  addFilter = filter => {
    this.filters = Object.assign(this.filters, filter);
  };

  applyFilters = e => {
    e.target.blur();
    this.customerList.filterData(this.filters);
  };

  clearFilters = e => {
    e.target.blur();
    const $ = window.jQuery;
    $('.advanced-filters input, .advanced-filters select').val('');
    this.filters = {};
    this.customerList.filters = {};
    this.customerList.filterData({});
  };
}

const mapStateToProps = state => {
  return { ...getPageItems(state, 'customer') };
};

const mapDispatchToProps = { loadCustomers, updateCustomerStatus };

export default connect(mapStateToProps, mapDispatchToProps)(Customers);
