import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Row, Col, ButtonToolbar, Button } from 'react-bootstrap';
import { initialize } from 'redux-form';

import {
  saveCustomerUser,
  loadCustomerUsers,
  removeUserFromCustomer,
  attachCustomerRoleToUser,
} from '../../actions/users';
import { getPageItems } from '../../reducers';

import { USER_ROLES } from '../../constants';
import Icon from '../Icon';
import DataTable from '../DataTable';
import CustomerUserModal from '../../containers/users/CustomerUserModal';
import AlertMessage from '../../components/AlertMessage';

class CustomerUserTable extends Component {
  constructor(props) {
    super(props);

    this.userList = null;
    this.state = {
      showFilter: false,
      isSubmitting: false,
      confirmDeleteUser: false,
      confirmChangeRole: false,
      selectedUser: null,
      showInviteModal: false,
    };
    this.filters = {};
    this.columns = [
      {
        width: '80',
        label: 'Name',
        field: 'firstname',
        cell: user => <b>{[user.firstname, user.lastname].join(' ')}</b>,
      },
      { width: '80', label: 'Email', field: 'email' },
      { width: '80', label: 'Phone', field: 'phone' },
      {
        width: '80',
        label: 'Role',
        field: 'subType',
        filter: filter =>
          <select
            className="form-control"
            onChange={e => filter({ subType: e.target.value })}>
            <option value="">Show All</option>
            <option value="staff">Staff</option>
            <option value="admin">Admin</option>
          </select>,
        cell: user => {
          const roles = user.authorities || [];
          const currentRole = roles.find(
            role => String(role.customerId) === String(this.props.customerId),
          );
          return currentRole ? USER_ROLES[currentRole.roleName] : ' - ';
        },
      },
      {
        width: '20',
        cell: user =>
          <div>
            <Button
              className="btn btn-xs btn-primary btn-row-single-action"
              onClick={() =>
                this.setState({ confirmDeleteUser: true, selectedUser: user })}>
              Remove
            </Button>{' '}
            <Button
              className="btn btn-xs btn-primary btn-row-single-action"
              onClick={() =>
                this.setState({
                  confirmChangeRole: true,
                  selectedUser: Object.assign(user, {
                    roleName: (user.authorities || []).find(role => {
                      return (
                        String(role.customerId) ===
                        String(this.props.customerId)
                      );
                    }).roleName,
                  }),
                })}>
              Change Role
            </Button>
          </div>,
      },
    ];
  }

  componentWillMount() {
    const { customerId, loadCustomerUsers } = this.props;
    if (customerId) {
      loadCustomerUsers(customerId);
    } else {
      loadCustomerUsers();
    }
  }

  render() {
    return (
      <div>
        {this.state.showFilter && this.renderCustomFilters()}
        <DataTable
          className="no-margin"
          columns={this.columns}
          loadData={this.props.loadCustomerUsers}
          items={this.props.items}
          currentPage={this.props.currentPage}
          numberOfPages={this.props.numberOfPages}
          totalItems={this.props.totalItems}
          isFetching={this.props.isFetching}
          renderTableFilters={this.renderTableActions}
          ref={datatable => (this.userList = datatable)}
        />
        {this.state.showInviteModal &&
          <CustomerUserModal
            customerId={this.props.customerId}
            show={this.state.showInviteModal}
            hideModal={this.toggleInviteModal}
          />}
        {this.state.confirmDeleteUser &&
          <AlertMessage
            title="Remove User"
            text={`Are you sure to remove ${this.state.selectedUser.email}?`}
            buttonToolbar={
              <ButtonToolbar>
                <Button
                  onClick={e => {
                    e.preventDefault();
                    this.setState({
                      confirmDeleteUser: !this.state.confirmDeleteUser,
                      selectedUser: null,
                    });
                  }}>
                  Cancel
                </Button>
                <Button
                  bsStyle="danger"
                  onClick={e => {
                    e.preventDefault();
                    this.deactivateUser();
                  }}>
                  Remove
                </Button>
              </ButtonToolbar>
            }
          />}
        {this.state.confirmChangeRole &&
          <AlertMessage
            title="Change User Role"
            text={
              this.state.selectedUser.roleName === 'CUSTOMER_ADMIN'
                ? `Are you sure to change ${this.state.selectedUser
                    .email} to staff?`
                : `Are you sure to change ${this.state.selectedUser
                    .email} to admin?`
            }
            buttonToolbar={
              <ButtonToolbar>
                <Button
                  onClick={e => {
                    e.preventDefault();
                    this.setState({
                      confirmChangeRole: !this.state.confirmChangeRole,
                      selectedUser: null,
                    });
                  }}>
                  Cancel
                </Button>
                <Button
                  bsStyle="primary"
                  onClick={e => {
                    e.preventDefault();
                    this.changeRole();
                  }}>
                  Change Role
                </Button>
              </ButtonToolbar>
            }
          />}
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
              onClick={this.toggleInviteModal}>
              Invite User
            </Button>
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
                }}>
                <Icon name="times" />
              </a>
            </Col>
          </Row>
          <br />
          <div className="form-horizontal">
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
              onClick={this.clearFilters}>
              Clear
            </Button>
            <Button
              bsStyle="primary"
              bsSize="small"
              onClick={this.applyFilters}>
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

  toggleInviteModal = () => {
    this.setState({ showInviteModal: !this.state.showInviteModal });
  };

  toggleSubmit = () => {
    this.setState({ isSubmitting: !this.state.isSubmitting });
  };

  deactivateUser = () => {
    const { selectedUser } = this.state;
    this.setState({ confirmDeleteUser: false, selectedUser: null });
    this.props.removeUserFromCustomer(
      this.props.customerId,
      selectedUser.id,
      result => {
        this.props.loadCustomerUsers(this.props.customerId);
        // this.toggleSubmit();
      },
      result => {
        // this.toggleSubmit();
      },
    );
  };

  changeRole = () => {
    const { selectedUser } = this.state;
    console.log(selectedUser, 'selected user...');
    this.setState({ confirmChangeRole: false, selectedUser: null });

    this.props.removeUserFromCustomer(
      this.props.customerId,
      selectedUser.id,
      result => {
        this.props.attachCustomerRoleToUser(
          {
            userId: selectedUser.id,
            customerId: Number(this.props.customerId),
          },
          selectedUser.roleName === 'CUSTOMER_ADMIN'
            ? 'CUSTOMER_STAFF'
            : 'CUSTOMER_ADMIN',
          result => {
            this.props.loadCustomerUsers(this.props.customerId);
          },
        );
      },
    );
  };
}

const mapStateToProps = state => {
  const {
    //loggedInUser: { franchiseId = false },
    loggedIn: { role = {} },
  } = state;

  return {
    ...getPageItems(state, 'user'),
    //franchiseId,
    activeRole: role,
  };
};

const mapDispatchToProps = {
  initialize,
  saveCustomerUser,
  loadCustomerUsers,
  removeUserFromCustomer,
  attachCustomerRoleToUser,
};

export default connect(mapStateToProps, mapDispatchToProps)(CustomerUserTable);
