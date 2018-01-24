import React, { Component } from 'react';
import { connect } from 'react-redux';
import { browserHistory } from 'react-router';
import { Row, Col, ButtonToolbar, Button } from 'react-bootstrap';

import {
  loadAgents,
  transferAgentCustomerToOtherAgent,
  reportCommissions,
} from '../actions/agents';
import { getPageItems, checkRoleIfFreightcom } from '../reducers';
import { formatPhone } from '../utils';

import Icon from '../components/Icon';
import Widget from '../components/Widget';
import DataTable from '../components/DataTable';
import { Link } from 'react-router';
import AlertMessage from '../components/AlertMessage';
import { addNotification } from 'reapop';
import DeactivateAgentModal from '../containers/agents/DeactivateAgentModal';

class Agents extends Component {
  constructor(props) {
    super(props);
    this.state = {
      showFilter: false,
      selectedAgent: null,
      showSubAgentModal: false,
      showCommisionAlert: false,
      deactivateAgentModal: false,
      agentToDeactivate: null,
    };
    this.filters = {};
    this.agentList = null;
    this.columns = [
      {
        width: '30',
        label: 'ID',
        field: 'id',
        filter: filter => (
          <input
            type="text"
            className="form-control input-sm"
            autoFocus
            onChange={e => filter({ id: e.target.value })}
          />
        ),
      },
      {
        width: '160',
        label: 'Agent Name',
        field: 'name',
      },
      {
        width: '160',
        label: 'Phone',
        field: 'phone',
        cell: item => formatPhone(item.phone),
        filter: false,
      },
      {
        width: '160',
        label: 'Parent Agent Name',
        field: 'parentSalesAgentName',
        filter: (filter, appliedFilters) => {
          return (
            <input
              type="text"
              className="form-control input-sm"
              onChange={e => filter({ parentName: e.target.value })}
              defaultValue={appliedFilters['parentName'] || ''}
            />
          );
        },
      },
      {
        width: '50',
        label: '# of Customers',
        field: 'customerCount',
        filter: false,
      },
      {
        width: '50',
        label: 'Commission %',
        field: 'commissionPercent',
        filter: false,
      },
      {
        width: '50',
        label: 'Total Commission',
        field: 'totalCommission',
        filter: false,
      },
      {
        width: '50',
        label: 'Pending commission',
        field: 'unpaidCommission',
        filter: false,
      },
      {
        label: 'Status',
        field: 'status',
        filter: filter => (
          <select
            className="form-control input-sm"
            onChange={e => {
              filter({ status: e.target.value });
            }}
          >
            <option value="">Show all</option>
            <option value="active">Active</option>
            <option value="inactive">Inactive</option>
          </select>
        ),
      },
      {
        width: '10',
        cell: item => (
          <div className="btn-group flex">
            <Link to={`/agents/${item.id}`} className="btn btn-primary btn-xs">
              <Icon name="pencil" /> Edit
            </Link>
            <Button
              bsStyle="primary"
              bsSize="xs"
              className="dropdown-toggle"
              data-toggle="dropdown"
              aria-haspopup="true"
              aria-expanded="false"
            >
              <span className="caret" />
              <span className="sr-only">Toggle Dropdown</span>
            </Button>
            <ul className="dropdown-menu dropdown-menu-right">
              {this.props.isAdmin &&
                <li>
                  <Link to={'/agents/' + item.id}>
                    <Icon name="pencil" /> Edit
                  </Link>
                </li>}
              <li>
                <a
                  href="#"
                  onClick={e => {
                    this.agentList.hideFilters();
                    this.agentList.filterData({
                      parentId: item.id,
                      parentName: item.name,
                    });
                    //  this.setState({
                    //    showFilter: false,
                    //  });
                    //  this.props.loadAgents(1, 10, { parentId: item.id });
                  }}
                >
                  <Icon name="user-secret" /> Sub Agents
                </a>
              </li>
              {this.props.isAdmin &&
                <li>
                  <a
                    href="#"
                    onClick={e => {
                      this.setState({
                        selectedAgent: item,
                        showCommisionAlert: true,
                      });
                    }}
                  >
                    <Icon name="usd" /> Report Commissions
                  </a>
                </li>}
              {this.props.isAdmin &&
                <li>
                  <a
                    href="#"
                    onClick={e => {
                      e.preventDefault();
                      this.showDeactivateAgentModal(item.id);
                    }}
                  >
                    <Icon name="mail-forward" /> Deactivate
                  </a>
                </li>}
            </ul>
          </div>
        ),
      },
    ];
  }

  componentWillMount() {
    this.props.loadAgents();
  }

  render() {
    return (
      <Widget title="Agent List">
        {this.state.showFilter && this.renderCustomFilters()}
        <DataTable
          className="no-margin"
          columns={this.columns}
          loadData={this.props.loadAgents}
          items={this.props.items}
          currentPage={this.props.currentPage}
          numberOfPages={this.props.numberOfPages}
          totalItems={this.props.totalItems}
          isFetching={this.props.isFetching}
          ref={datatable => (this.agentList = datatable)}
          _renderTableFilters={this.renderTableActions}
          groupedFilters
        />
        {this.state.deactivateAgentModal &&
          <DeactivateAgentModal
            agents={this.props.items}
            deactivateAgent={this.deactivateAgent}
            show={this.state.deactivateAgentModal}
            hide={this.hideDeactivateAgentModal}
          />}
        {this.state.selectedAgent &&
          this.state.showCommisionAlert &&
          <AlertMessage
            title="Report Commissions"
            text={`Do you wish to report any unreported commissions for ${this.state.selectedAgent.name}?`}
            buttonToolbar={
              <ButtonToolbar>
                <Button
                  onClick={e => {
                    e.preventDefault();
                    this.hideCommisionAlert();
                  }}
                >
                  Cancel
                </Button>
                <Button
                  bsStyle="primary"
                  onClick={e => {
                    e.preventDefault();
                    this.reportCommissions();
                  }}
                >
                  Report
                </Button>
              </ButtonToolbar>
            }
          />}
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
              className="pull-right hidden"
              onClick={e => browserHistory.push('/agents/add')}
            >
              <Icon name="plus" /> Add an Agent
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
    this.agentList.filterData(this.filters);
  };

  clearFilters = e => {
    e.target.blur();
    const $ = window.jQuery;
    $('.advanced-filters input, .advanced-filters select').val('');
    this.filters = {};
    this.agentList.filters = {};
    this.agentList.filterData({});
  };

  hideCommisionAlert = () => {
    this.setState({ selectedAgent: null, showCommisionAlert: false });
  };

  hideSubAgentModal = () => {
    this.setState({ selectedAgent: null, showSubAgentModal: false });
  };

  reportCommissions = () => {
    this.props.reportCommissions(
      this.state.selectedAgent.id,
      data => {
        this.hideCommisionAlert();
        if (data.payload.results.length > 0) {
          this.props.addNotification({
            title: 'Commission reported',
            message: `Any unreported commssion for ${this.state.selectedAgent.name} is now reported.`,
            status: 'success',
            allowHTML: true,
            dismissible: false,
            dismissAfter: 10000,
            buttons: [
              {
                name: 'OK',
                primary: true,
              },
            ],
          });
        } else {
          this.props.addNotification({
            title: 'No Commissions Reported',
            message: 'There are no commission to report',
            status: 'warning',
          });
        }
      },
      data => {
        this.hideCommisionAlert();
        this.props.addNotification({
          title: 'Error Reporting Commissions',
          message: data.payload.message,
          status: 'error',
        });
      },
    );
  };

  deactivateAgent = agentRoleIdTransferTo => {
    const { agentToDeactivate } = this.state;

    this.props.transferAgentCustomerToOtherAgent(
      agentToDeactivate,
      agentRoleIdTransferTo,
      this.agentList.loadData,
    );
    this.hideDeactivateAgentModal();
  };

  showDeactivateAgentModal = agentToDeactivate => {
    this.setState({ agentToDeactivate, deactivateAgentModal: true });
  };

  hideDeactivateAgentModal = () => {
    this.setState({ agentToDeactivate: null, deactivateAgentModal: false });
  };
}

const mapStateToProps = state => {
  return {
    ...getPageItems(state, 'agent'),
    isAdmin: checkRoleIfFreightcom(state),
  };
};

const mapDispatchToProps = {
  loadAgents,
  transferAgentCustomerToOtherAgent,
  reportCommissions,
  addNotification,
};

export default connect(mapStateToProps, mapDispatchToProps)(Agents);
