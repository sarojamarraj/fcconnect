import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Button, ButtonToolbar } from 'react-bootstrap';
import DataTable from '../DataTable';
import Icon from '../Icon';
import {
  getPageItems,
  checkRoleIfFreightcom,
  checkRoleIfAdminOrAgent,
} from '../../reducers';
import { loadInvoices } from '../../actions/invoices';
import { DatePicker } from '../forms/inputs/InputDate';
import ApplyCreditsModal from './ApplyCreditsModal';
import EnterPaymentsModal from './EnterPaymentsModal';
import InvoicePaymentModal from './InvoicePaymentModal';
import DeleteInvoicesModal from './DeleteInvoicesModal';
import { formatCurrency } from '../../utils';
import CustomerAutoSuggest from '../customers/CustomerAutoSuggest';
// import findIndex from 'lodash/findIndex';
import arrow from '../../styles/img/custom/arrow.png';
import { Link } from 'react-router';

import differenceInDays from 'date-fns/difference_in_days';

class InvoiceTable extends Component {
  constructor(props) {
    super(props);
    this.state = {
      checkedInvoiceList: [],
      applyCreditsModal: false,
      enterPaymentsModal: false,
      paymentModal: false,
      deleteModal: false,
      dateGeneratedFilter: '',
      dueDateFilter: '',
      showFilter: false,
    };
    this.checkboxes = [];
    this.filters = {};
    this.invoiceList = null;
    this.customerFilter = null;
    this.columns = [
      {
        width: '40',
        label: () => {
          return (
            <input
              type="checkbox"
              name="selectAllUnpaidInvoice"
              value="1"
              checked={
                this.state.checkedInvoiceList.length === this.props.items.length
              }
              onChange={e => this.selectAllUnpaidInvoice(e.target.checked)}
            />
          );
        },
        // label: '',
        sorting: false,
        filter: false,
        field: 'paymentStatus',
        cell: invoice => {
          return (
            <input
              type="checkbox"
              name={`invoice[${invoice.id}]`}
              style={{ width: '100%', minWidth: '25px' }}
              value={invoice.id}
              // ref={input => {
              //   if (
              //     input &&
              //     findIndex(this.checkboxes, input) === -1 &&
              //     this.props.items.find(item => item.id === invoice.id)
              //   ) {
              //     this.checkboxes.push(input);
              //   }
              // }}
              checked={
                this.state.checkedInvoiceList.find(
                  item => String(item.id) === String(invoice.id),
                )
                  ? true
                  : false
              }
              onChange={e =>
                this.addToCheckedInvoiceList(e.target.checked, invoice)}
            />
          );
        },
      },
      { width: '60', label: 'Invoice Id', field: 'id' },
      {
        width: '80',
        label: 'Date Created',
        field: 'createdAt',
        className: 'hidden-xs hidden-sm',
        cell: invoice => invoice.dateGenerated,
        filter: filter =>
          <DatePicker
            className="form-control"
            placeholder="Date Created"
            dateFormat="yy-mm-dd"
            input={{ onChange: value => filter({ dateGenerated: value }) }}
          />,
      },
      {
        width: '80',
        label: 'Total Amount',
        field: 'amount',
        filter: false,
        className: 'currency',
        cell: invoice => formatCurrency(invoice.amount),
      },
      {
        width: '80',
        label: 'Paid Amount',
        field: 'paidAmount',
        filter: false,
        className: 'currency',
        cell: invoice => formatCurrency(invoice.paidAmount),
      },
      {
        width: '80',
        label: 'Credit Usage',
        field: 'creditedAmount',
        filter: false,
        className: 'currency',
        cell: invoice => formatCurrency(invoice.creditedAmount),
      },
      {
        width: '80',
        label: 'Paid Status',
        field: 'paymentStatus',
        filter: filter =>
          <select
            className="form-control"
            onChange={e => filter({ paymentStatus: e.target.value })}>
            <option value="">Show All</option>
            <option value={true}>Paid</option>
            <option value={false}>Unpaid</option>
          </select>,
        cell: invoice => {
          const pastDue = differenceInDays(Date.now(), invoice.dueDate);
          return (
            <div>

              {invoice.paymentStatus
                ? <span className="label label-success">Paid</span>
                : <span className="label label-warning">Unpaid</span>}
              {' '}
              {!invoice.paymentStatus &&
                pastDue > 0 &&
                <span className="label label-danger">{pastDue}D Overdue</span>}
            </div>
          );
        },
      },
      {
        width: '80',
        label: 'Due Date',
        field: 'dueAt',
        cell: invoice =>
          <div>
            {invoice.dueDate}
            {invoice.viewedAt &&
              this.props.isAdmin &&
              <span className="label label-warning">
                <Icon name="eye" />  {invoice.viewedAt}
              </span>}
          </div>,
        filter: filter =>
          <DatePicker
            className="form-control"
            placeholder="Due Date"
            dateFormat="yy-mm-dd"
            input={{ onChange: value => filter({ dueDate: value }) }}
          />,
      },
      {
        width: '70',
        cell: invoice =>
          <div className="btn-group flex">
            <Link
              className="btn btn-xs btn-primary"
              to={'/invoices/' + invoice.id}>
              <Icon name="file-o" /> View
            </Link>
            <Button
              bsStyle="primary"
              bsSize="xs"
              className="dropdown-toggle"
              data-toggle="dropdown"
              aria-haspopup="true"
              aria-expanded="false">
              <span className="caret" />
              <span className="sr-only">Toggle Dropdown</span>
            </Button>
            <ul className="dropdown-menu dropdown-menu-right">
              <li>
                <Link to={'/invoices/' + invoice.id}>
                  <Icon name="file-o" /> View
                </Link>
              </li>
              {this.props.isAdmin &&
                <li>
                  <a
                    href="#"
                    onClick={e => {
                      e.preventDefault();
                      this.setState({
                        checkedInvoiceList: [invoice],
                        deleteModal: !this.state.deleteModal,
                      });
                    }}>
                    <Icon name="trash" /> Cancel
                  </a>
                </li>}
              {this.props.isAdmin &&
                !invoice.paymentStatus &&
                <li>
                  <a
                    href="#"
                    onClick={e => {
                      e.preventDefault();
                      this.setState({
                        checkedInvoiceList: [invoice],
                        applyCreditsModal: !this.state.applyCreditsModal,
                      });
                    }}>
                    <Icon name="usd" /> Apply Credits
                  </a>
                </li>}
            </ul>
          </div>,
      },
    ];
  }

  componentWillMount() {
    this.props.loadInvoices();
  }

  componentWillReceiveProps(nextProps) {
    if (this.props.currentPage !== nextProps.currentPage) {
      this.resetCheckBoxes();
    }
  }

  render() {
    let columns = this.columns.slice();
    if (this.props.isAdminOrAgent) {
      columns.splice(3, 0, {
        width: '80',
        label: 'Customer',
        field: 'customer.name',
        cell: invoice => {
          return invoice.customer.name;
        },
      });
    }

    return (
      <div>
        {this.state.showFilter && this.renderCustomFilters(this.filters)}
        <DataTable
          className="no-margin"
          columns={columns}
          loadData={this.props.loadInvoices}
          items={this.props.items}
          currentPage={this.props.currentPage}
          numberOfPages={this.props.numberOfPages}
          totalItems={this.props.totalItems}
          isFetching={this.props.isFetching}
          renderTableFilters={this.renderTableActionButtons}
          ref={datatable => (this.invoiceList = datatable)}
        />
        {this.state.applyCreditsModal &&
          <ApplyCreditsModal
            invoiceList={this.state.checkedInvoiceList}
            show={this.state.applyCreditsModal}
            toggleApplyCreditsModal={this.toggleApplyCreditsModal}
            loadInvoices={this.props.loadInvoices}
            currentPage={this.props.currentPage}
            sortOrder={this.props.sortOrder}
            filters={this.filters}
            itemsPerPage={this.props.itemsPerPage}
            resetCheckBoxes={this.resetCheckBoxes}
          />}
        {this.state.enterPaymentsModal &&
          <EnterPaymentsModal
            invoiceList={this.state.checkedInvoiceList}
            show={this.state.enterPaymentsModal}
            toggleEnterPaymentsModal={this.toggleEnterPaymentsModal}
            loadInvoices={this.props.loadInvoices}
            currentPage={this.props.currentPage}
            sortOrder={this.props.sortOrder}
            filters={this.filters}
            itemsPerPage={this.props.itemsPerPage}
            resetCheckBoxes={this.resetCheckBoxes}
          />}
        {this.state.paymentModal &&
          <InvoicePaymentModal
            invoiceList={this.state.checkedInvoiceList}
            show={this.state.paymentModal}
            togglePaymentModal={this.togglePaymentModal}
            loadInvoices={this.props.loadInvoices}
            currentPage={this.props.currentPage}
            sortOrder={this.props.sortOrder}
            filters={this.filters}
            itemsPerPage={this.props.itemsPerPage}
            resetCheckBoxes={this.resetCheckBoxes}
          />}
        {this.state.deleteModal &&
          <DeleteInvoicesModal
            invoiceList={this.state.checkedInvoiceList}
            show={this.state.deleteModal}
            toggleDeleteModal={this.toggleDeleteModal}
            loadInvoices={this.props.loadInvoices}
            currentPage={this.props.currentPage}
            sortOrder={this.props.sortOrder}
            filters={this.filters}
            itemsPerPage={this.props.itemsPerPage}
            resetCheckBoxes={this.resetCheckBoxes}
          />}
      </div>
    );
  }

  renderCustomFilters = filters => {
    return (
      <div className="jarviswidget jarviswidget-sortable jarviswidget-color-blue"
            data-widget-colorbutton="false"
            data-widget-deletebutton="false">
        <header role="heading">
          <div className="jarviswidget-ctrls" role="menu">
            <a
              href="#"
              className="button-icon jarviswidget-delete-btn"
              rel="tooltip"
              title=""
              data-placement="bottom"
              data-original-title="Delete"
              onClick={e => {
                e.preventDefault();
                this.setState({ showFilter: false });
              }}
            >
              <Icon name="times" />
            </a>
          </div>
          <h2></h2>
        </header>
        <div>
          <div className="widget-body no-padding advanced-filters">
            <div
              className="panel-body"
              onKeyUp={e => {
                const isCombobox = e.target.getAttribute('role') === 'combobox';
                if (!isCombobox) {
                  switch (e.key) {
                    case 'Enter':
                      this.applyFilters();
                      break;
                    case 'Escape':
                      this.setState({ showFilter: false });
                      break;
                    default:
                  }
                }
              }}>
              <div className="form-horizontal">
                <div className="row">
                  <div className="col-sm-6">
                    <div className="form-group">
                      <label className="control-label col-sm-4">
                        Invoice Id:
                      </label>
                      <div className="col-sm-8">
                        <input
                          ref="id"
                          className="form-control input-sm"
                          defaultValue={filters.id}
                          onChange={e => {
                            this.addFilter({ id: e.target.value });
                          }}
                          type="text"
                          autoFocus
                        />
                      </div>
                    </div>
                  </div>
                  <div className="col-sm-6">
                    <div className="form-group">
                      <label className="control-label col-sm-4">
                        Paid/Unpaid:
                      </label>
                      <div className="col-sm-8">
                        <select
                          className="form-control input-sm"
                          ref="paymentStatus"
                          defaultValue={filters.paymentStatus}
                          onChange={e =>
                            this.addFilter({ paymentStatus: e.target.value })}>
                          <option value="">Show All</option>
                          <option value={1}>Paid</option>
                          <option value={0}>Unpaid</option>
                        </select>
                      </div>
                    </div>
                  </div>
                </div>
                <div className="row">
                  <div className="col-sm-6">
                    <div className="form-group">
                      <label className="control-label col-sm-4">
                        BOL Id:
                      </label>
                      <div className="col-sm-8">
                        <input
                          ref="id"
                          className="form-control input-sm"
                          defaultValue={filters.bolId}
                          onChange={e => {
                            this.addFilter({ bolId: e.target.value });
                          }}
                          type="text"
                        />
                      </div>
                    </div>
                  </div>
                  <div className="col-sm-6">
                    <div className="form-group">
                      <label className="control-label col-sm-4">
                        Tracking Number:
                      </label>
                      <div className="col-sm-8">
                        <input
                          ref="trackingNumber"
                          className="form-control input-sm"
                          defaultValue={filters.trackingNumber}
                          onChange={e => {
                            this.addFilter({ trackingNumber: e.target.value });
                          }}
                          type="text"
                        />
                      </div>
                    </div>
                  </div>
                </div>
                {this.props.isAdminOrAgent
                  ? <div className="row">
                      <div className="col-sm-6">
                        <div className="form-group">
                          <label className="control-label col-sm-4">
                            Customer:
                          </label>
                          <div className="col-sm-8">
                            <CustomerAutoSuggest
                              name="customer"
                              defaultValue={filters.customerId}
                              ref={input => (this.customerFilter = input)}
                              onChange={customer => {
                                this.addFilter({
                                  customerId: customer ? customer.id : '',
                                });
                              }}
                            />
                          </div>
                        </div>
                      </div>
                      <div className="col-sm-6" />
                    </div>
                  : <div className="row">
                      <div className="col-sm-6" />
                    </div>}
                <div className="row">
                  <div className="col-sm-6">
                    <div className="form-group">
                      <label className="control-label col-sm-4">
                        Date Created:
                      </label>
                      <div className="col-sm-8">
                        <select
                          className="form-control input-sm"
                          ref="dateGenerated"
                          defaultValue={filters.createdAt}
                          onChange={e => {
                            this.addFilter({ createdAt: e.target.value });
                            this.setState({ dateGeneratedFilter: e.target.value });
                          }}>
                          <option value="">Show All</option>
                          <option value="today">Today</option>
                          <option value="custom">Custom range</option>
                        </select>
                      </div>
                    </div>
                  </div>
                  {this.state.dateGeneratedFilter === 'custom' &&
                    <div>
                      <div className="col-sm-3">
                        <div className="col-sm-12">
                          <div className="form-group">
                            <DatePicker
                              ref="dateGeneratedFrom"
                              className="form-control input-sm"
                              placeholder="From"
                              dateFormat="yy-mm-dd"
                              defaultValue={filters.createdAtFrom}
                              input={{
                                onChange: value =>
                                  this.addFilter({ createdAtFrom: value }),
                              }}
                            />
                          </div>
                        </div>
                      </div>
                      <div className="col-sm-3">
                        <div className="col-sm-12">
                          <div className="form-group">
                            <DatePicker
                              ref="dateGeneratedTo"
                              className="form-control input-sm"
                              placeholder="To"
                              dateFormat="yy-mm-dd"
                              defaultValue={filters.createdAtTo}
                              input={{
                                onChange: value =>
                                  this.addFilter({ createdAtTo: value }),
                              }}
                            />
                          </div>
                        </div>
                      </div>
                    </div>}
                </div>
                <div className="row">
                  <div className="col-sm-6">
                    <div className="form-group">
                      <label className="control-label col-sm-4">
                        Due Date:
                      </label>
                      <div className="col-sm-8">
                        <select
                          ref="dueDate"
                          className="form-control input-sm"
                          defaultValue={filters.dueDate}
                          onChange={e => {
                            this.addFilter({ dueDate: e.target.value });
                            this.setState({ dueDateFilter: e.target.value });
                          }}>
                          <option value="">Show All</option>
                          <option value="today">Today</option>
                          <option value="tomorrow">Tomorrow</option>
                          <option value="upcoming">Upcoming</option>
                          <option value="past">Past</option>
                          <option value="custom">Custom range</option>
                        </select>
                      </div>
                    </div>
                  </div>
                  {this.state.dueDateFilter === 'custom' &&
                    <div>
                      <div className="col-sm-3">
                        <div className="col-sm-12">
                          <div className="form-group">
                            <DatePicker
                              ref="dueDateFrom"
                              className="form-control input-sm"
                              placeholder="From"
                              dateFormat="yy-mm-dd"
                              defaultValue={filters.dueDateFrom}
                              input={{
                                onChange: value =>
                                  this.addFilter({ dueDateFrom: value }),
                              }}
                            />
                          </div>
                        </div>
                      </div>
                      <div className="col-sm-3">
                        <div className="col-sm-12">
                          <div className="form-group">
                            <DatePicker
                              ref="dueDateTo"
                              className="form-control input-sm"
                              placeholder="To"
                              dateFormat="yy-mm-dd"
                              defaultValue={filters.dueDateTo}
                              input={{
                                onChange: value =>
                                  this.addFilter({ dueDateTo: value }),
                              }}
                            />
                          </div>
                        </div>
                      </div>
                    </div>}
                </div>
                <div className="row">
                  <div className="col-sm-12">
                    <ButtonToolbar>
                      <Button
                        bsStyle="primary"
                        bsSize="small"
                        className="pull-right"
                        onClick={this.applyFilters}>
                        Search
                      </Button>
                      <Button
                        bsStyle="default"
                        bsSize="small"
                        className="pull-right"
                        onClick={this.clearFilters}>
                        Clear
                      </Button>
                    </ButtonToolbar>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  };

  addFilter = filter => {
    this.filters = Object.assign(this.filters, filter);
  };

  clearFilters = () => {
    const $ = window.jQuery;
    $('.advanced-filters input, .advanced-filters select').val('');
    this.filters = {};
    this.invoiceList.filters = {};
    this.setState({ dateGeneratedFilter: '', dueDateFilter: '' });
    this.invoiceList.filterData({});
    this.customerFilter && this.customerFilter.setValue('');
  };

  applyFilters = () => {
    this.invoiceList.filterData(this.filters);
    this.resetCheckBoxes();
  };

  resetCheckBoxes = () => {
    this.setState({ checkedInvoiceList: [] });
  };

  addToCheckedInvoiceList = (checked, invoice) => {
    const { checkedInvoiceList } = this.state;
    const newInvoiceList = this.state.checkedInvoiceList.slice();
    if (checked && !newInvoiceList.find(item => item.id === invoice.id)) {
      newInvoiceList.push(invoice);
      this.setState({ checkedInvoiceList: newInvoiceList });
    } else if (
      !checked &&
      checkedInvoiceList.find(item => item.id === invoice.id)
    ) {
      const modified = checkedInvoiceList.filter(item => {
        return item.id !== invoice.id;
      });
      this.setState({ checkedInvoiceList: modified });
    }
  };

  selectAllUnpaidInvoice = checked => {
    if (checked) {
      this.setState({ checkedInvoiceList: this.props.items });
    } else {
      this.setState({ checkedInvoiceList: [] });
    }
  };

  toggleApplyCreditsModal = () => {
    this.setState({ applyCreditsModal: !this.state.applyCreditsModal });
  };

  toggleEnterPaymentsModal = () => {
    this.setState({ enterPaymentsModal: !this.state.enterPaymentsModal });
  };

  togglePaymentModal = () => {
    this.setState({ paymentModal: !this.state.paymentModal });
  };

  toggleDeleteModal = () => {
    this.setState({ deleteModal: !this.state.deleteModal });
  };

  renderTableActionButtons = () => {
    const columnLength = this.props.isAdminOrAgent
      ? this.columns.length + 1
      : this.columns.length;

    return (
      <tr>
        <th colSpan={columnLength}>
          <img
            src={arrow}
            width="50px"
            style={{ paddingTop: '5px' }}
            alt="Arrow"
          />
          {this.props.isAdmin &&
            <Button
              className="btn btn-primary btn-sm"
              disabled={
                this.state.checkedInvoiceList.filter(
                  item => !item.paymentStatus,
                ).length > 0
                  ? false
                  : true
              }
              onClick={this.toggleEnterPaymentsModal}>
              <i className="fa fa-usd" />{' '}Enter Payments
            </Button>}
          {' '}
          {}
          <Button
            className="btn btn-primary btn-sm"
            disabled={
              this.state.checkedInvoiceList.filter(item => !item.paymentStatus)
                .length > 0
                ? false
                : true
            }
            onClick={this.togglePaymentModal}>
            <i className="fa fa-credit-card" />{' '}Pay Now
          </Button>
          {' '}
          {this.props.isAdmin &&
            <Button
              className="btn btn-danger btn-sm"
              disabled={this.state.checkedInvoiceList.length > 0 ? false : true}
              onClick={this.toggleDeleteModal}>
              <i className="fa fa-trash" />{' '}Cancel
            </Button>}
          {!this.state.showFilter &&
            <Button
              className="btn btn-default pull-right btn-sm"
              onClick={e => {
                e.preventDefault();
                this.setState({ showFilter: true });
              }}>
              <i className="fa fa-search" />{' '}Search
            </Button>}
        </th>
      </tr>
    );
  };
}

const mapStateToProps = state => {
  const { loggedIn: { role = {} } } = state;

  return {
    ...getPageItems(state, 'invoice'),
    activeRole: role,
    isAdmin: checkRoleIfFreightcom(state),
    isAdminOrAgent: checkRoleIfAdminOrAgent(state),
  };
};

const mapDispatchToProps = { loadInvoices };

export default connect(mapStateToProps, mapDispatchToProps)(InvoiceTable);
