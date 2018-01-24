import React, { Component } from 'react';
import { connect } from 'react-redux';
import { reduxForm } from 'redux-form';
import { Row, Col, Modal } from 'react-bootstrap';
import some from 'lodash/some';
import { Link } from 'react-router';

import RolePreferences from '../../components/users/RolePreferences';

import {
  saveUser,
  loadUserById,
  updateUser,
  sendNewPasswordById,
  updateRole,
  removeRole,
} from '../../actions/users';
import { formatRole, getAgentRoleOfUser } from '../../utils';
import Widget from '../../components/Widget';
import CustomerAutoSuggest from '../../components/customers/CustomerAutoSuggest';
import AgentAutoSuggest from '../../components/agents/AgentAutoSuggest';
import InputText from '../../components/forms/inputs/InputText';
import Button from 'react-bootstrap-button-loader';
import { addNotification } from 'reapop';
import { selectEntity } from '../../reducers/index';
import { checkLoadStatus } from '../../reducers/load-status';

const EMPTY_VALUES = { x: true };

class UserForm extends Component {
  constructor(props) {
    super(props);
    this.state = {
      selectedNewRole: '',
      selectedCustomer: '',
      selectedAgent: '',
      assignedRoles: [],
      username: '',
      isSubmitting: false,
    };
  }

  componentWillMount() {
    if (!this.props.isNewUser) {
      this.props.loadUserById(this.props.userId);
    }
  }

  componentWillReceiveProps(nextProps) {
    const { dataValues } = nextProps;

    if (this.props.dataValues !== dataValues) {
      const name = `${dataValues.firstname || ''} ${dataValues.lastname || ''}`;
      this.setState({
        assignedRoles: this.parseRoles(dataValues.authorities),
        username: name.trim() ? name : dataValues.login,
      });

      // This seems to be needed on refreshes
      this.props.initialize(this.state.newUser || dataValues);
    }
  }

  render() {
    const newUser = this.props.isNewUser && !this.state.newUser;

    const pageTitle = newUser
      ? 'Add New User'
      : <span>Edit User: <b>{this.state.username}</b></span>;
    return (
      <Widget title={pageTitle}>
        <form
          id="customer-info"
          className="smart-form"
          onSubmit={this.props.handleSubmit(this.onSubmit)}
        >
          <fieldset className="padding-10">
            <div>
              <Row>
                <Col className="col" sm={12} xs={12}>
                  <div className="jarviswidget jarviswidget-sortable jarviswidget-color-blue" data-widget-edit-button="false">
                    <header role="heading">
                      <h2>{(pageTitle === 'Add New User') ? 'Add New User' : `Edit User: ${this.state.username}`}</h2>
                    </header>
                    <div>
                      <div className="widget-body">
                        <Row>
                          <Col className="col" sm={6}>
                            <InputText label="Username" name="login" />
                          </Col>
                          <Col className="col" sm={6}>
                            <InputText label="Email" name="email" />
                          </Col>
                        </Row>
                        <Row>
                          <Col className="col" sm={6}>
                            <InputText label="First Name" name="firstname" />
                          </Col>
                          <Col className="col" sm={6}>
                            <InputText label="Last Name" name="lastname" />
                          </Col>
                        </Row>
                        <Row>
                          <Col className="col" sm={6}>
                            <InputText label="Phone" name="phone" />
                          </Col>
                          <Col className="col" sm={6}>
                            <InputText label="Cell" name="cell" />
                          </Col>
                        </Row>
                      </div>
                    </div>
                  </div>
                </Col>
              </Row>
              {this.showRoles(newUser)}
            </div>
          </fieldset>
          <hr className="simple" />
          <footer className="form-actions">
            <Button
              type="submit"
              bsStyle="success"
              bsSize="large"
              loading={this.state.isSubmitting}
            >
              {newUser ? 'Add User' : 'Update User'}
            </Button>
            {!newUser &&
              <Button
                bsStyle="primary"
                bsSize="large"
                type="button"
                onClick={e => {
                  e.preventDefault();
                  this.sendNewPassword();
                }}
              >
                Reset Password
              </Button>}
            <Link to="/users" className="btn btn-primary btn-lg">
              Back to User List
            </Link>
          </footer>
        </form>
        {this.possiblyShowModal()}
      </Widget>
    );
  }

  showRoles = newUser => {
    if (newUser) {
      return null;
    } else {
      return (
        <div>
          <Row>
            <Col className="col" sm={12} xs={12}>
              <div className="jarviswidget jarviswidget-sortable jarviswidget-color-blue" data-widget-edit-button="false">
                <header role="heading">
                  <h2>Roles</h2>
                </header>
                <div>
                  <div className="widget-body">
                    <Row>
                      <Col className="col" sm={6} xs={12}>
                        <section>
                          <label className="label">Assign New Role</label>
                          <label className="select">
                            <select
                              value={this.state.selectedNewRole}
                              onChange={e =>
                                this.setState({
                                  selectedNewRole: e.target.value,
                                })}
                            >
                              <option value="">- Select a role -</option>
                              {!Boolean(
                                this.state.assignedRoles.find(
                                  role => role.roleName === 'ADMIN',
                                ),
                              ) && <option value="ADMIN">Freightcom Admin</option>}
                              {!Boolean(
                                this.state.assignedRoles.find(
                                  role => role.roleName === 'FREIGHTCOM_STAFF',
                                ),
                              ) &&
                                <option value="FREIGHTCOM_STAFF">
                                  Freightcom Staff
                                </option>}
                              <option value="AGENT">Agent</option>
                              <option value="CUSTOMER_ADMIN">
                                Customer Admin
                              </option>
                              <option value="CUSTOMER_STAFF">
                                Customer Staff
                              </option>
                            </select><i />
                          </label>
                        </section>
                      </Col>
                      <Col className="col" sm={6} xs={12}>
                        {this.renderAdditionalRowOptions()}
                      </Col>
                      <Col className="col" xs={12}>
                        <button
                          type="button"
                          className="btn btn-blue btn-sm"
                          disabled={this.shouldAddRoleBeDisabled()}
                          onClick={this.addRole}
                        >
                          Add Role
                        </button>
                      </Col>
                    </Row>
                    <br />
                    <Row>
                      <Col className="col" sm={12}>
                        <label className="label">Assigned Roles</label>
                        <table className="table table-bordered table-striped table-condensed">
                          <thead>
                            <tr>
                              <th>Roles</th>
                              <th style={{ width: '50px' }} />
                            </tr>
                          </thead>
                          <tbody>
                            {this.state.assignedRoles.map(this.renderRoleRow)}
                          </tbody>
                        </table>
                      </Col>
                    </Row>
                  </div>
                </div>
              </div>
            </Col>
          </Row>
        </div>
      );
    }
  };

  possiblyShowModal = () => {
    const { assignedRoles, preferencesIndex, removeRoleIndex } = this.state;
    const editedRole = assignedRoles[preferencesIndex];
    const deleteRole = assignedRoles[removeRoleIndex];

    if (this.state.preferencesOpen) {
      return (
        <Modal
          show={this.state.preferencesOpen}
          onHide={() => {
            this.setState({ preferencesOpen: false });
          }}
        >
          <Modal.Header>
            <Modal.Title>
              Edit Preferences: {this.roleDisplayText(editedRole)}
            </Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <RolePreferences
              index={preferencesIndex}
              changeProperty={this.rolePropertyChanged}
              role={editedRole}
              value="1"
            />
          </Modal.Body>
          <Modal.Footer>
            <div>
              <Button
                bsStyle="default"
                onClick={() => this.setState({ preferencesOpen: false })}
              >
                Cancel
              </Button>
              <Button bsStyle="primary" onClick={this.updatePreferences}>
                Update Preferences
              </Button>
            </div>
          </Modal.Footer>
        </Modal>
      );
    }
    else if (this.state.removeRoleModalOpen) {
      return (
        <Modal
          show={this.state.removeRoleModalOpen}
          onHide={() => {
            this.setState({ removeRoleModalOpen: false });
          }}
        >
          <Modal.Header>
            <Modal.Title>
              Remove Role: {this.roleDisplayText(deleteRole)}
            </Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <div>
              {'Are you sure you want to remove the role of '}
              <b> {this.roleDisplayText(deleteRole)} </b> ?
            </div>
          </Modal.Body>
          <Modal.Footer>
            <div>
              <Button
                bsStyle="default"
                onClick={() => this.setState({ removeRoleModalOpen: false })}
              >
                Cancel
              </Button>
              <Button bsStyle="danger"
                onClick={e => {
                  e.preventDefault();
                  this.removeRole(removeRoleIndex);
                  this.setState({ removeRoleModalOpen: false });
                }}
              >
                Remove Role
              </Button>
            </div>
          </Modal.Footer>
        </Modal>
      );
    }
    else {
      return null;
    }
  };

  updatePreferences = () => {
    const { assignedRoles, preferencesIndex } = this.state;
    const editedRole = assignedRoles[preferencesIndex];

    this.props.updateRole(editedRole.roleName, editedRole).then(arg => {
      this.setState({ preferencesOpen: false });
    });
  };

  rolePropertyChanged = (property, value, index) => {
    const { assignedRoles } = this.state;
    const editedRole = assignedRoles[index];
    const newRoles = [...assignedRoles];

    newRoles[index] = { ...editedRole, [property]: value };

    this.setState({
      assignedRoles: newRoles,
    });
  };

  renderAdditionalRowOptions = () => {
    switch (this.state.selectedNewRole) {
      case 'CUSTOMER_ADMIN':
      case 'CUSTOMER_STAFF':
        return (
          <section>
            <label className="label">Customer</label>
            <label className="select">
              <CustomerAutoSuggest
                name="customer"
                value={this.state.selectedCustomer}
                onChange={customer => {
                  this.setState({ selectedCustomer: customer || '' });
                }}
              />
            </label>
          </section>
        );

      case 'AGENT':
        return (
          <section>
            <label className="label">Parent Agent</label>
            <label className="select">
              <AgentAutoSuggest
                name="agent"
                value={this.state.selectedAgent}
                onChange={agent => {
                  this.setState({ selectedAgent: agent || '' });
                }}
              />
            </label>
          </section>
        );
      default:
        return null;
    }
  };

  roleDisplayText = role => {
    const displayText = formatRole(role.roleName);

    if (~role.roleName.indexOf('CUSTOMER')) {
      return displayText + ' at ' + role.customerName;
    }

    if (~role.roleName.indexOf('AGENT')) {
      return displayText + ' - ' + (role.parentSalesAgentName || 'Freightcom');
    }

    return displayText;
  };

  renderRoleRow = (role, index) => {
    const displayText = this.roleDisplayText(role);

    return (
      <tr key={index}>
        <td>{displayText}</td>
        <td style={{ whiteSpace: 'nowrap' }}>
          <Button
            bsStyle="primary"
            bsSize="xs"
            onClick={e => {
              e.preventDefault();
              this.editPreferences(index);
            }}
          >
            Edit Prefs
          </Button>
          &nbsp;
          <Button
            bsStyle="danger"
            bsSize="xs"
            onClick={e => {
              e.preventDefault();
              this.confirmRoleRemoval(index);
            }}
          >
            Remove
          </Button>
        </td>
      </tr>
    );
  };

  editPreferences = index => {
    this.setState({
      preferencesOpen: true,
      preferencesIndex: index,
    });
  };

  confirmRoleRemoval = index => {
    this.setState({
      removeRoleModalOpen: true,
      removeRoleIndex: index,
    });
  };

  sendNewPassword = () => {
    const { sendNewPasswordById, userId, addNotification } = this.props;

    sendNewPasswordById(
      this.state.newUser ? this.state.newUser.id : userId,
    ).then(apiPayload => {
      console.log(apiPayload);
      if (!apiPayload.error) {
        addNotification({
          message: `A new password has been emailed to ${this.state.username}.`,
          status: 'success',
        });
      } else {
        addNotification({
          message: `Error occurred while resetting password.`,
          status: 'error',
        });
      }
    });
  };

  parseRoles = authorities => {
    const formattedRoles = [];
    if (authorities && authorities.length > 0) {
      for (let value of authorities) {
        formattedRoles.push({
          id: value.id,
          roleName: value.roleName,
          customerId: value.customerId,
          customerName: value.customerName,
          parentSalesAgentId: value.parentSalesAgentId,
          parentSalesAgentName: value.parentSalesAgentName,
          userId: this.state.newUser
            ? this.state.newUser.id
            : this.props.userId,
          canManageClaims: value.canManageClaims,
          canManageDisputes: value.canManageDisputes,
          canEnterPayments: value.canEnterPayments,
          canGenerateInvoices: value.canGenerateInvoices,
        });
      }
    }
    return formattedRoles;
  };

  checkDuplicateRole = () => {
    const {
      selectedNewRole,
      assignedRoles,
      selectedCustomer,
      selectedAgent,
    } = this.state;

    if (~selectedNewRole.indexOf('CUSTOMER')) {
      if (
        some(assignedRoles, {
          roleName: selectedNewRole,
          customerId: selectedCustomer.id,
        })
      ) {
        return true;
      }
    }

    if (selectedNewRole === 'AGENT') {
      const agentRole = getAgentRoleOfUser(selectedAgent);
      if (selectedAgent) {
        if (
          some(assignedRoles, {
            roleName: 'AGENT',
            parentSalesAgentId: agentRole.id,
          })
        ) {
          return true;
        }
      } else {
        if (
          some(assignedRoles, {
            roleName: 'AGENT',
            parentSalesAgentId: null,
          })
        ) {
          return true;
        }
      }
    }
    return false;
  };

  shouldAddRoleBeDisabled = () => {
    const { selectedNewRole, selectedCustomer } = this.state;

    if (!selectedNewRole) {
      return true;
    }
    if (this.checkDuplicateRole()) {
      return true;
    }
    if (~selectedNewRole.indexOf('CUSTOMER') && !selectedCustomer) {
      return true;
    }

    return false;
  };

  addRole = e => {
    e.preventDefault();
    e.target.blur();

    const { selectedNewRole, selectedCustomer, selectedAgent } = this.state;

    let roleToAdd = {};

    switch (selectedNewRole) {
      case 'CUSTOMER_ADMIN':
      case 'CUSTOMER_STAFF':
        roleToAdd = {
          customerId: selectedCustomer.id,
          customerName: selectedCustomer.name,
        };
        break;
      case 'AGENT':
        // get agent role id
        const agentRole = getAgentRoleOfUser(selectedAgent);
        const agentRoleId = agentRole ? agentRole['id'] : '';
        const agentRoleName = `${selectedAgent.firstname} ${selectedAgent.lastname}`;
        roleToAdd = {
          parentSalesAgentId: agentRoleId || null,
          parentSalesAgentName: !!agentRoleId ? agentRoleName : 'Freightcom',
        };
        break;
      default:
        break;
    }

    this.props
      .updateRole(this.state.selectedNewRole, {
        ...roleToAdd,
        id: null,
        roleName: this.state.selectedNewRole,
        userId: this.state.newUser ? this.state.newUser.id : this.props.userId,
      })
      .then(arg => {
        this.setState({
          selectedNewRole: '',
          selectedCustomer: '',
          selectedAgent: '',
          assignedRoles: [
            ...this.state.assignedRoles,
            {
              ...arg.payload.authorities[arg.payload.authorities.length - 1],
              userId: this.state.newUser
                ? this.state.newUser.id
                : this.props.userId,
            },
          ],
        });
      });
  };

  removeRole = index => {
    const roleToRemove = [];
    const { assignedRoles } = this.state;

    if (assignedRoles[index].id) {
      roleToRemove.push(assignedRoles[index]);
    }

    this.props.removeRole(assignedRoles[index].id, arg => {
      this.setState({
        assignedRoles: [
          ...assignedRoles.slice(0, index),
          ...assignedRoles.slice(index + 1),
        ],
      });
    });
  };

  onSubmit = values => {
    const { userId, saveUser, updateUser, isNewUser, form } = this.props;
    const { newUser } = this.state;

    this.setState({ isSubmitting: true });
    if (isNewUser && !newUser) {
      saveUser(
        form,
        values,
        [],
        result => {
          const newUser = result.payload.entity;
          const name = `${newUser.firstname || ''} ${newUser.lastname || ''}`;

          this.setState({
            isSubmitting: false,
            newUser,
            assignedRoles: this.parseRoles(newUser.authorities),
            username: name.trim() ? name : newUser.login,
          });
          this.props.addNotification({
            message: `New user has been added.`,
            status: 'success',
          });
        },
        result => {
          this.setState({ isSubmitting: false });
          this.props.addNotification({
            message: `Error occurred while creating new user.`,
            status: 'error',
          });
        },
      );
    } else {
      const userValues = {
        id: newUser ? newUser.id : values.id,
        login: values.login,
        firstname: values.firstname,
        lastname: values.lastname,
        email: values.email,
        phone: values.phone,
        cell: values.cell,
        enabled: values.enabled,
      };
      updateUser(
        userValues,
        [],
        [],
        newUser ? newUser.id : userId,
        result => {
          this.setState({ isSubmitting: false });
          this.props.addNotification({
            message: `User information has been updated.`,
            status: 'success',
          });
        },
        result => {
          this.setState({ isSubmitting: false });
          this.props.addNotification({
            message: `Error occurred while updating user infomration.`,
            status: 'error',
          });
        },
      );
    }
  };
}

const mapStateToProps = (state, ownProps) => {
  const isNewUser = ownProps.params['id'] === 'add' || !ownProps.params['id'];
  const userId = !isNewUser ? ownProps.params['id'] : '';

  return {
    isNewUser,
    userId,
    loadStatus: checkLoadStatus(state, 'user', userId),
    dataValues: userId ? selectEntity(state, 'user', userId) : EMPTY_VALUES,
  };
};

const mapDispatchToProps = {
  saveUser,
  loadUserById,
  updateUser,
  sendNewPasswordById,
  addNotification,
  updateRole,
  removeRole,
};

export default connect(mapStateToProps, mapDispatchToProps)(
  reduxForm({ form: 'edit-user' })(UserForm),
);
