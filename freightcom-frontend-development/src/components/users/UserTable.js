import React, { Component } from 'react';
import { connect } from 'react-redux';
import { initialize } from 'redux-form';
import { Link, browserHistory } from 'react-router';
import { Row, Col, ButtonToolbar, Button } from 'react-bootstrap';

import { saveCustomerUser, loadUsers } from '../../actions/users';
import { getPageItems } from '../../reducers';
import Icon from '../Icon';
import DataTable from '../DataTable';

class UserTable extends Component {
  constructor(props) {
    super(props);

    this.userList = null;
    this.state = {
      showFilter: false,
    };
    this.filters = {};
    this.columns = [
      {
        width: '80',
        label: 'Username',
        field: 'login',
        filter: filter => (
          <input
            type="text"
            className="form-control input-sm"
            autoFocus
            onChange={e => {
              const value = e.target.value;
              filter({ login: value });
            }}
          />
        ),
      },
      {
        width: '80',
        label: 'Name',
        field: 'name',
        cell: user => <b>{[user.firstname, user.lastname].join(' ')}</b>,
      },
      { width: '80', label: 'Email', field: 'email' },
      { width: '80', label: 'Phone', field: 'phone' },
      {
        width: '80',
        label: 'Status',
        field: 'enabled',
        filter: filter => (
          <select
            className="form-control"
            onChange={e => filter({ enabled: e.target.value })}
          >
            <option value="">Show All</option>
            <option value={true}>Active</option>
            <option value={false}>Inactive</option>
          </select>
        ),
        cell: user =>
          user.enabled
            ? <span className="label label-success">Active</span>
            : <span className="label label-warning">Inactive</span>,
      },
      {
        width: '80',
        label: 'Role',
        field: 'roles',
        filter: filter => (
          <select
            className="form-control"
            onChange={e => filter({ role: e.target.value })}
          >
            <option value="">Show All</option>
            <option value="ADMIN">Admin</option>
            <option value="FREIGHTCOM_STAFF">Staff</option>
            <option value="CUSTOMER_ADMIN">Customer Admin</option>
            <option value="CUSTOMER_STAFF">Customer Staff</option>
            <option value="AGENT">Agent</option>
          </select>
        ),
        cell: user => (
          <div
            style={{
              textOverflow: 'ellipsis',
              overflow: 'hidden',
              whiteSpace: 'nowrap',
              maxWidth: 76,
            }}
            title={user.roles}
          >
            {user.roles}
          </div>
        ),
      },
      {
        width: '20',
        cell: user => (
          <Link
            to={'/users/' + user.id}
            className="btn btn-xs btn-primary btn-row-single-action"
          >
            Edit
          </Link>
        ),
      },
    ];
  }

  componentWillMount() {
    this.props.loadUsers();
  }

  render() {
    return (
      <div>
        {this.state.showFilter && this.renderCustomFilters()}
        <DataTable
          className="no-margin"
          columns={this.columns}
          loadData={this.props.loadUsers}
          items={this.props.items}
          currentPage={this.props.currentPage}
          numberOfPages={this.props.numberOfPages}
          totalItems={this.props.totalItems}
          isFetching={this.props.isFetching}
          _renderTableFilters={this.renderTableActions}
          groupedFilters
          ref={datatable => (this.userList = datatable)}
          actionButtons={
            <Button
              bsStyle="primary"
              bsSize="small"
              className="pull-right"
              onClick={e => browserHistory.push('/users/add')}
            >
              <Icon name="plus" /> Add new user
            </Button>
          }
        />
      </div>
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
              onClick={e => browserHistory.push('/users/add')}
            >
              <Icon name="plus" /> Add new user
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
    this.userList.filterData(this.filters);
  };

  clearFilters = e => {
    e.target.blur();
    const $ = window.jQuery;
    $('.advanced-filters input, .advanced-filters select').val('');
    this.filters = {};
    this.userList.filters = {};
    this.userList.filterData({});
  };
}

const mapStateToProps = state => {
  const {
    loggedInUser: { franchiseId = false },
    loggedIn: { role = {} },
  } = state;

  return { ...getPageItems(state, 'user'), franchiseId, activeRole: role };
};

const mapDispatchToProps = { initialize, saveCustomerUser, loadUsers };

export default connect(mapStateToProps, mapDispatchToProps)(UserTable);
