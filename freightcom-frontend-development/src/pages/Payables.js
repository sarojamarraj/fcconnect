import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Button, ButtonGroup, Tab, Nav, NavItem } from 'react-bootstrap';
import Select from 'react-select';
import formatDate from 'date-fns/format';

import PayableStatementView from '../containers/payables/PayableStatementView';
import CommissionStatementView
  from '../containers/payables/CommissionStatementView';

import PrintChequesModal from '../components/PrintChequesModal';
import Widget from '../components/Widget';
import { DatePicker } from '../components/forms/inputs/InputDate';
import DataTable from '../components/DataTable';
import Icon from '../components/Icon';

import {
  loadPayableStatements,
  loadCommissionStatements,
  markPayableStatementPaid,
  deletePayableStatement,
  markCommissionStatementPaid,
  deleteCommissionStatement,
  printCheques,
} from '../actions/payables';

import {
  activatePayableTab,
  addPayableTab,
  removePayableTab,
} from '../actions/tabs';

import { loadCarriers } from '../actions/carriers';

import { getPageItems, selectAllFromEntities, selectCheque } from '../reducers';

import { formatCurrency } from '../utils';

const PAYABLES = 'payables';

const CellCheckbox = props => <input type="checkbox" {...props} />;

class Payables extends Component {
  constructor(props) {
    super(props);

    this.state = {
      carrierFilter: '',
      agentFilter: '',
      checkedPayables: {},
      checkedCommissions: {},
    };

    this.payableDatatable = null;
    this.commissionDatatable = null;

    this.payableColumns = [
      {
        label: () => {
          return (
            <CellCheckbox
              name="selectAllPaybalbes"
              value="1"
              checked={this.allPayablesChecked()}
              onChange={this.checkAllPayables}
            />
          );
        },
        field: 'id',
        width: '20',
        sorting: false,
        filter: false,
        cell: payable => {
          return (
            <CellCheckbox
              name={`payable[${payable.id}]`}
              value={payable.id}
              disabled={payable.status === 'paid'}
              style={{ width: '100%', minWidth: '25px' }}
              checked={this.state.checkedPayables[String(payable.id)] || false}
              onChange={e => {
                this.setState({
                  checkedPayables: {
                    ...this.state.checkedPayables,
                    [String(payable.id)]: e.target.checked,
                  },
                });
              }}
            />
          );
        },
      },
      {
        label: 'ID',
        field: 'id',
      },
      {
        label: 'Generated At',
        field: 'createdAt',
        filter: filter => (
          <DatePicker
            dateFormat="yy-mm-dd"
            className="form-control input-sm"
            input={{
              onChange: value => filter({ createdAt: value }),
            }}
          />
        ),
        cell: row => formatDate(row.createdAt, 'MMM DD, YYYY'),
      },
      {
        label: 'Due At',
        field: 'dueAt',
        filter: filter => (
          <DatePicker
            dateFormat="yy-mm-dd"
            className="form-control input-sm"
            input={{
              onChange: value => filter({ dueAt: value }),
            }}
          />
        ),
        cell: row => (row.dueAt ? formatDate(row.dueAt, 'MMM DD, YYYY') : ''),
      },
      {
        label: 'Paid At',
        field: 'paidAt',
        filter: filter => (
          <DatePicker
            dateFormat="yy-mm-dd"
            className="form-control input-sm"
            input={{
              onChange: value => filter({ paidAt: value }),
            }}
          />
        ),
        cell: row => (row.paidAt ? formatDate(row.paidAt, 'MMM DD, YYYY') : ''),
      },
      {
        label: 'Carrier',
        field: 'carrier.name',
        filter: filter => (
          <Select
            value={this.state.carrierFilter}
            labelKey="name"
            valueKey="id"
            onChange={value => {
              filter({
                'carrier.id': value.map(item => item.id).join(','),
              });
              this.setState({ carrierFilter: value });
            }}
            options={this.props.carriers}
            multi
            ref={el => (this.carrierSelect = el)}
          />
        ),
        cell: row => row.carrierName,
      },
      {
        label: 'Total Amount',
        field: 'totalAmount',
        filter: false,
        className: 'currency',
        cell: payable => formatCurrency(payable.totalAmount),
      },
      {
        label: 'Charge Count',
        field: 'chargeCount',
        filter: false,
      },
      {
        label: 'Order Count',
        field: 'orderCount',
        filter: false,
      },
      {
        label: 'Status',
        field: 'status',
        filter: (filter, filterValues) => (
          <select
            className="form-control"
            value={filterValues.status || ''}
            onChange={e => filter({ status: e.target.value })}
          >
            <option value="">Show All</option>
            <option value="unpaid">Unpaid</option>
            <option value="paid">Paid</option>
          </select>
        ),
      },
      {
        width: '80px',
        cell: row => (
          <ButtonGroup>
            <Button
              bsStyle="primary"
              bsSize="xs"
              onClick={e => {
                this.props.addPayableTab({
                  id: `tab-view-payable-${row.id}`,
                  title: `Payable Statement: ${row.id}`,
                  type: 'payable',
                  payableId: row.id,
                });
              }}
            >
              View
            </Button>
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
              <li>
                <a
                  onClick={e => {
                    this.props.addPayableTab({
                      id: `tab-view-payable-${row.id}`,
                      title: `Payable Statement: ${row.id}`,
                      type: 'payable',
                      payableId: row.id,
                    });
                  }}
                >
                  {' '}View
                </a>
              </li>
              {row.status !== 'paid' &&
                <li>
                  <a
                    onClick={e => {
                      this.props.markPayableStatementPaid(row.id, () => {
                        //this.payableDatatable.loadData();
                      });
                    }}
                  >
                    {' '}Mark as Paid
                  </a>
                </li>}
              <li>
                <a
                  onClick={e => {
                    this.props.deletePayableStatement(row.id, () => {
                      this.payableDatatable.loadData();
                    });
                  }}
                >
                  {' '}Delete
                </a>
              </li>
            </ul>
          </ButtonGroup>
        ),
      },
    ];

    this.commissionColumns = [
      {
        label: () => {
          return (
            <CellCheckbox
              name="selectAllCommissions"
              value="1"
              checked={this.allCommissionsChecked()}
              onChange={this.checkAllCommissions}
            />
          );
        },
        field: 'id',
        width: '20',
        sorting: false,
        filter: false,
        cell: payable => {
          return (
            <CellCheckbox
              name={`commission[${payable.id}]`}
              disabled={payable.status === 'paid'}
              value={payable.id}
              style={{ width: '100%', minWidth: '25px' }}
              checked={
                this.state.checkedCommissions[String(payable.id)] || false
              }
              onChange={e => {
                this.setState({
                  checkedCommissions: {
                    ...this.state.checkedCommissions,
                    [String(payable.id)]: e.target.checked,
                  },
                });
              }}
            />
          );
        },
      },
      {
        label: 'ID',
        field: 'id',
      },
      {
        label: 'Created At',
        field: 'createdAt',
        filter: filter => (
          <DatePicker
            dateFormat="yy-mm-dd"
            className="form-control input-sm"
            input={{
              onChange: value => filter({ createdAt: value }),
            }}
          />
        ),
        cell: row => formatDate(row.createdAt, 'MMM DD, YYYY'),
      },
      {
        label: 'Due At',
        field: 'dueAt',
        filter: filter => (
          <DatePicker
            dateFormat="yy-mm-dd"
            className="form-control input-sm"
            input={{
              onChange: value => filter({ dueAt: value }),
            }}
          />
        ),
        cell: row => (row.dueAt ? formatDate(row.dueAt, 'MMM DD, YYYY') : ''),
      },
      {
        label: 'Paid At',
        field: 'paidAt',
        filter: filter => (
          <DatePicker
            dateFormat="yy-mm-dd"
            className="form-control input-sm"
            input={{
              onChange: value => filter({ paidAt: value }),
            }}
          />
        ),
        cell: row => (row.paidAt ? formatDate(row.paidAt, 'MMM DD, YYYY') : ''),
      },
      {
        label: 'Agent',
        field: 'agentName',
      },
      {
        label: 'Total Amount',
        field: 'totalAmount',
        filter: false,
        className: 'currency',
        cell: payable => formatCurrency(payable.totalAmount),
      },
      {
        label: 'Charge Count',
        field: 'chargeCount',
        filter: false,
      },
      {
        label: 'Order Count',
        field: 'orderCount',
        filter: false,
      },
      {
        label: 'Status',
        field: 'status',
        filter: (filter, filterValues) => (
          <select
            value={filterValues.status || ''}
            className="form-control -sm"
            onChange={e => filter({ status: e.target.value })}
          >
            <option value="">Show All</option>
            <option value="unpaid">Unpaid</option>
            <option value="paid">Paid</option>
          </select>
        ),
      },
      {
        width: '80px',
        cell: row => (
          <ButtonGroup>
            <Button
              bsStyle="primary"
              bsSize="xs"
              onClick={e => {
                this.props.addPayableTab({
                  id: `tab-view-commission-${row.id}`,
                  title: `Commission Statement: ${row.id}`,
                  type: 'commission',
                  commissionId: row.id,
                });
              }}
            >
              View
            </Button>
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
              <li>
                <a
                  onClick={e => {
                    this.props.addPayableTab({
                      id: `tab-view-commission-${row.id}`,
                      title: `Commission Statement: ${row.id}`,
                      type: 'commission',
                      commissionId: row.id,
                    });
                  }}
                >
                  {' '}View
                </a>
              </li>
              {row.status !== 'paid' &&
                <li>
                  <a
                    onClick={e => {
                      this.props.markCommissionStatementPaid(row.id, () => {
                        //this.commissionDatatable.loadData();
                      });
                    }}
                  >
                    {' '}Mark as Paid
                  </a>
                </li>}
              <li>
                <a
                  onClick={e => {
                    this.props.deleteCommissionStatement(row.id, () => {
                      this.commissionDatatable.loadData();
                    });
                  }}
                >
                  {' '}Delete
                </a>
              </li>
            </ul>
          </ButtonGroup>
        ),
      },
    ];
  }

  allPayablesChecked = () =>
    this.props.payableStatementList.items.every(
      payable =>
        payable.status === 'paid' ||
        this.state.checkedPayables[String(payable.id)],
    );

  somePayablesChecked = () =>
    this.props.payableStatementList.items.some(
      payable =>
        payable.status !== 'paid' &&
        this.state.checkedPayables[String(payable.id)],
    );

  checkAllPayables = e => {
    if (this.allPayablesChecked()) {
      this.setState({
        checkedPayables: {},
      });
    } else {
      this.setState({
        checkedPayables: this.props.payableStatementList.items.reduce(
          (map, { id, status }) =>
            status === 'paid' ? map : { ...map, [String(id)]: true },
          {},
        ),
      });
    }
  };

  allCommissionsChecked = () =>
    this.props.commissionStatementList.items.every(
      commission =>
        commission.status === 'paid' ||
        this.state.checkedCommissions[String(commission.id)],
    );

  someCommissionsChecked = () =>
    this.props.commissionStatementList.items.some(
      commission =>
        commission.status !== 'paid' &&
        this.state.checkedCommissions[String(commission.id)],
    );

  checkAllCommissions = e => {
    if (this.allCommissionsChecked()) {
      this.setState({
        checkedCommissions: {},
      });
    } else {
      this.setState({
        checkedCommissions: this.props.commissionStatementList.items.reduce(
          (map, { id, status }) =>
            status === 'paid' ? map : { ...map, [String(id)]: true },
          {},
        ),
      });
    }
  };

  componentWillMount() {
    this.props.loadPayableStatements(1, 10, { status: 'unpaid' });
    this.props.loadCommissionStatements(1, 10, { status: 'unpaid' });
    this.props.loadCarriers(1, 99999);
  }

  componentWillReceiveProps(nextProps) {
    const { payableStatementList, commissionStatementList, cheque } = nextProps;
    const {
      payableStatementList: prevPayabaleStatementList,
      commissionStatementList: prevCommissionStatementList,
      cheque: prevCheque,
    } = this.props;

    if (payableStatementList !== prevPayabaleStatementList) {
      this.setState({
        checkedPayables: {},
      });
    }

    if (commissionStatementList !== prevCommissionStatementList) {
      this.setState({
        checkedCommissions: {},
      });
    }

    if (cheque && cheque !== prevCheque && this.popup) {
      console.debug('NEW CHEQUE ' + cheque);
      this.props.loadPayableStatements();
      this.props.loadCommissionStatements();
      this.popup.location.href = '/cheques';
    }
  }

  render() {
    const { showPrint = false } = this.state;

    return (
      <div>
        {showPrint === 'payables' &&
          <PrintChequesModal
            title="Payables"
            cancel={this.closePrintDialog}
            submit={this.printPayables}
          />}
        {showPrint === 'commissions' &&
          <PrintChequesModal
            title="Commission"
            cancel={this.closePrintDialog}
            submit={this.printCommissions}
          />}
        <Widget>
          <Tab.Container
            generateChildId={(key, type) => key}
            id="payables-tabs"
            activeKey={this.props.activeTab}
            onSelect={this.props.activatePayableTab}
          >
            <div>
              <Nav bsStyle="tabs" className="nav nav-tabs bordered">
                <NavItem eventKey="tab-payable-statement">
                  Payables
                </NavItem>
                <NavItem eventKey="tab-commission-statement">
                  Commissions
                </NavItem>
                {this.props.openTabs.map(tab => (
                  <NavItem key={`${tab.id}`} eventKey={`${tab.id}`}>
                    {tab.title}

                    <span
                      className="tab-close"
                      onClick={e => {
                        e.preventDefault();
                        e.stopPropagation();
                        this.props.removePayableTab(tab.id);
                      }}
                    >
                      <Icon name="close" />
                    </span>
                  </NavItem>
                ))}
              </Nav>
              <Tab.Content>
                <Tab.Pane eventKey="tab-payable-statement">
                  <DataTable
                    loadData={this.props.loadPayableStatements}
                    columns={this.payableColumns}
                    className="no-margin"
                    filters={{ status: 'unpaid' }}
                    groupedFilters
                    ref={dt => (this.payableDatatable = dt)}
                    clearFilters={e => {
                      this.carrierSelect.clearValue(e);
                    }}
                    actionButtons={[
                      <Button
                        key="A"
                        className="btn btn-primary btn-sm pull-right"
                        disabled={!this.somePayablesChecked()}
                        onClick={e => this.setState({ showPrint: 'payables' })}
                      >
                        <i className="fa fa-search" />{' '}Print Cheques ...
                      </Button>,
                    ]}
                    {...this.props.payableStatementList}
                  />
                </Tab.Pane>
                <Tab.Pane eventKey="tab-commission-statement">
                  <DataTable
                    loadData={this.props.loadCommissionStatements}
                    columns={this.commissionColumns}
                    className="no-margin"
                    filters={{ status: 'unpaid' }}
                    groupedFilters
                    ref={dt => (this.commissionDatatable = dt)}
                    actionButtons={[
                      <Button
                        key="B"
                        className="btn btn-primary btn-sm pull-right"
                        disabled={!this.someCommissionsChecked()}
                        onClick={e =>
                          this.setState({ showPrint: 'commissions' })}
                      >
                        <i className="fa fa-search" />{' '}Print Cheques ...
                      </Button>,
                    ]}
                    {...this.props.commissionStatementList}
                  />
                </Tab.Pane>
                {this.props.openTabs.map(tab => (
                  <Tab.Pane key={`${tab.id}`} eventKey={`${tab.id}`}>
                    {tab.type === 'payable' &&
                      <PayableStatementView id={tab.payableId} />}
                    {tab.type === 'commission' &&
                      <CommissionStatementView id={tab.commissionId} />}
                  </Tab.Pane>
                ))}
              </Tab.Content>
            </div>
          </Tab.Container>

        </Widget>
      </div>
    );
  }

  closePrintDialog = continuation =>
    this.setState({ showPrint: false }, continuation);

  printPayables = startingChequeNumber =>
    this.closePrintDialog(() => {
      this.popup = window.open('/loading');

      console.debug('OPENED WINDOW', this.popup);

      this.props.printCheques(PAYABLES, {
        type: 'PAYABLES',
        startingChequeNumber,
        ids: Object.keys(this.state.checkedPayables),
      });
    });

  printCommissions = startingChequeNumber =>
    this.closePrintDialog(() => {
      this.popup = window.open('/loading');

      console.debug('OPENED WINDOW', this.popup);

      this.props.printCheques(PAYABLES, {
        type: 'COMMISSIONS',
        startingChequeNumber,
        ids: Object.keys(this.state.checkedCommissions),
      });
    });
}

const mapStateToProps = state => {
  return {
    activeTab: state.tabs.payables.activeTab,
    openTabs: state.tabs.payables.openTabs,
    payableStatementList: getPageItems(state, 'payableStatement'),
    commissionStatementList: getPageItems(state, 'commissionStatement'),
    carriers: selectAllFromEntities(state, 'carrier'),
    cheque: selectCheque(state, PAYABLES),
  };
};

const mapDispatchToProps = {
  activatePayableTab,
  addPayableTab,
  loadCarriers,
  removePayableTab,
  loadPayableStatements,
  loadCommissionStatements,
  markPayableStatementPaid,
  deletePayableStatement,
  markCommissionStatementPaid,
  deleteCommissionStatement,
  printCheques,
};

export default connect(mapStateToProps, mapDispatchToProps)(Payables);
