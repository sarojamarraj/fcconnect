import React, { PureComponent } from 'react';
import { connect } from 'react-redux';
import formatDate from 'date-fns/format';
import { Row, Col, Button, ButtonToolbar } from 'react-bootstrap';
import Select from 'react-select';
import findIndex from 'lodash/findIndex';
import { addNotification } from 'reapop';

import CustomerAutoSuggest
  from '../../components/customers/CustomerAutoSuggest';
import AgentAutoSuggest from '../../components/agents/AgentAutoSuggest';
import AlertMessage from '../../components/AlertMessage';
import DatatablePagination from '../../components/DatatablePagination';
import {
  loadSubmittedOrders,
  duplicateOrder,
  viewOrder,
  cancelOrder,
  updateStatus,
  loadOrderCount,
  loadCountOrders,
} from '../../actions/orders';
import { generateInvoice } from '../../actions/invoices';
import {
  getPageItems,
  selectAllFromEntities,
  checkRoleIfFreightcom,
  checkRoleIfFreightcomAdmin,
  checkRoleIfAdminOrAgent,
  selectOrderCountPerType,
  checkRoleIfAgent,
  selectCarrierList,
} from '../../reducers';
import {
  formatAddress,
  displayCompanyOrContactName,
  displayShipmentType,
  formatCurrency,
  getAgentRoleOfUser,
} from '../../utils';

import Icon from '../../components/Icon';
import OrderStatusLabel from '../../components/orders/OrderStatusLabel';
import DataTable from '../../components/DataTable';
import { DatePicker } from '../../components/forms/inputs/InputDate';
import {
  getStatusActions,
  shipmentListModals,
} from '../../components/orders/ShipmentMenu';

import {
  registerChangedInterest,
  hasChangedInterest,
  clearChangedObjects,
} from '../../reducers/changed-objects';

const SHIPMENT_LIST = 'shipment-list';
const ORDER = 'order';
const CLAIM = 'claim';

class ShipmentList extends PureComponent {
  constructor(props) {
    super(props);

    const initialOrderStatusFilters = '';

    registerChangedInterest(SHIPMENT_LIST, ORDER);
    registerChangedInterest(SHIPMENT_LIST, CLAIM);

    this.state = {
      shipDateFilter: 'upcoming',
      showFilter: false,
      filtersInitialized: false,
      checkedOrderList: [],
      confirmInvoiceDialog: false,
      activeOrderId: '',
      schedulePickupModal: false,
      orderStatusFilter: initialOrderStatusFilters,
      packageTypeNameFilter: [],
      carrierFilter: [],
      uploadPODModal: false,
      submitClaimModal: false,
      initialLoad: this.props.active,
    };
    this.checkboxes = [];
    this.filters = {
      statusId: initialOrderStatusFilters,
    };

    this.customerFilter = null;
    this.agentFilter = null;

    this.datatable = null;
    this.columns = [
      {
        label: 'Order',
        field: 'eshipperOid',
        sorting: false,
        cell: order => {
          return (
            <div>
              Transaction #: <b>{order.eshipperOid}</b>
              <br />
              Carrier: <b>{order.carrierName}</b>
              <br />
              Service: <b>{order.serviceName}</b>
              {order['bolId']
                ? <span>
                    <br />Bill of Lading: <b>{order.bolId}</b>
                  </span>
                : ''}
              {order['referenceCode']
                ? <span>
                    <br />Reference #: <b>{order.referenceCode}</b>
                  </span>
                : ''}
              {order['carrierPickupConf']
                ? <span>
                    <br />
                    Pickup Confirmation #: <b>{order.carrierPickupConf}</b>
                  </span>
                : ''}
            </div>
          );
        },
      },
      {
        label: 'Type',
        field: 'packageTypeName',
        sorting: true,
        cell: order => {
          let carrierLogo = order.logoPath;

          return (
            <div style={{ textAlign: 'center' }}>
              <b>
                {displayShipmentType(order.packageTypeName)}
              </b>
              {order.customWorkOrder &&
                <div>
                  <span className="label label-default">Work Order</span>
                </div>}
              <br />
              <br />
              <img
                src={`/api/${carrierLogo}.png`}
                style={{ maxWidth: '25px', margin: 'auto' }}
                alt={order.actualCarrierName}
                title={order.actualCarrierName}
                className="img-responsive"
              />
            </div>
          );
        },
      },
      {
        label: 'Ship Date',
        field: 'shipDate',
        cell: order =>
          order.shipDate ? formatDate(order.shipDate, 'MMM DD, YYYY') : '',
      },
      {
        label: 'Ship From',
        field: 'shipFrom',
        sortField: 'shipFrom.city',
        cell: ({ shipFrom = {} }) => (
          <div>
            <b>
              {displayCompanyOrContactName(shipFrom)}
            </b>
            <br />
            <span>
              {formatAddress(shipFrom)}
            </span>
          </div>
        ),
      },
      {
        label: 'Ship To',
        field: 'shipTo',
        sortField: 'shipTo.city',
        cell: ({ shipTo = {} }) => (
          <div>
            <b>
              {displayCompanyOrContactName(shipTo)}
            </b>
            <br />
            <span>
              {formatAddress(shipTo)}
            </span>
          </div>
        ),
      },
      {
        label: 'Charges',
        field: 'totalCharge',
        className: 'currency',
        cell: order => (
          <div>
            {formatCurrency(order.totalCharge)}
          </div>
        ),
      },
      {
        label: 'Status',
        field: 'statusId',
        cell: ({
          id,
          statusId = '0',
          statusName = 'Invalid Status',
          trackingUrl = null,
          masterTrackingNum = null,
          hasClaim = false,
        }) => {
          return (
            <div>
              <OrderStatusLabel statusId={statusId} statusName={statusName} />
              {hasClaim
                ? <div>
                    <span className="label label-yellow">CLAIM</span>
                  </div>
                : ''}
              <br />
              <br />
              {trackingUrl && masterTrackingNum
                ? <a href={trackingUrl} target="_blank">
                    Track It
                  </a>
                : ''}
            </div>
          );
        },
      },
      { width: '86', cell: order => getStatusActions(this, order) },
    ];
  }

  componentWillMount() {
    if (this.state.initialLoad) {
      this.props
        .loadSubmittedOrders(1, 10, this.filters)
        .then(() => this.props.loadCountOrders('SUBMITTED_ONLY', this.filters));
    }

    this.columns = this.columns.slice();

    if (this.props.isAdminOrAgent) {
      this.columns.unshift({
        label: 'Customer',
        field: 'customerId',
        cell: order => {
          return (
            <h5>
              {order.customer.name}
            </h5>
          );
        },
      });
    }

    if (this.props.isFreightcomAdmin) {
      this.columns.unshift({
        label: () => {
          return (
            <input
              type="checkbox"
              name="selectAllUnbilledOrders"
              value="1"
              checked={
                // checked should be modified later
                this.state.checkedOrderList.length === this.props.items.length
              }
              onChange={e => this.selectAllUnbilledOrders(e.target.checked)}
            />
          );
        },
        field: 'id',
        width: '20',
        sorting: false,
        filter: false,
        cell: order => {
          return (
            <input
              type="checkbox"
              name={`order[${order.id}]`}
              value={order.id}
              style={{ width: '100%', minWidth: '25px' }}
              ref={input => {
                if (
                  input &&
                  findIndex(this.checkboxes, input) === -1 &&
                  this.props.items.find(item => item.id === order.id)
                ) {
                  this.checkboxes.push(input);
                }
              }}
              checked={
                this.state.checkedOrderList.find(
                  item => String(item.id) === String(order.id),
                )
                  ? true
                  : false
              }
              onChange={e => this.addToOrderList(e.target.checked, order)}
            />
          );
        },
      });
    }
  }

  componentWillReceiveProps(nextProps) {
    if (this.props.items !== nextProps.items) {
      this.checkboxes = [];
      this.setState({ checkedOrderList: [] });
    }

    if (
      !this.props.active &&
      nextProps.active &&
      (!this.state.initialLoad || this.props.changedObjects)
    ) {
      this.setState({ initialLoad: true }, () => {
        this.props.clearChangedObjects(SHIPMENT_LIST).then(() => {
          this.props
            .loadSubmittedOrders()
            .then(() =>
              this.props.loadCountOrders('SUBMITTED_ONLY', this.filters),
            );
          this.props.loadOrderCount();
        });
      });
    }
  }

  render() {
    return (
      <div>
        {this.state.showFilter && this.renderDataFilters()}
        <DataTable
          className="no-margin"
          columns={this.columns}
          loadData={this.loadData}
          items={this.props.items}
          currentPage={this.props.currentPage}
          numberOfPages={this.props.numberOfPages}
          totalItems={this.props.totalItems}
          isFetching={this.props.isFetching}
          ref={datatable => (this.datatable = datatable)}
          renderTableFilters={this.renderTableActions}
        />
        {this.state.confirmInvoiceDialog
          ? <AlertMessage
              title="Generate Invoice(s)"
              text={this.formatInvoiceDialog()}
              buttonToolbar={
                <ButtonToolbar>
                  <Button
                    onClick={e => {
                      e.preventDefault();
                      this.setState({
                        confirmInvoiceDialog: !this.state.confirmInvoiceDialog,
                      });
                    }}
                  >
                    Cancel
                  </Button>
                  <Button
                    bsStyle="primary"
                    onClick={e => {
                      e.preventDefault();
                      this.generateInvoices();
                    }}
                  >
                    Generate
                  </Button>
                </ButtonToolbar>
              }
            />
          : null}
        {shipmentListModals(this)}
      </div>
    );
  }

  loadOrders = () => {
    this.datatable.loadData();
  };

  addFilter = filter => {
    this.filters = Object.assign(this.filters, filter);
  };

  applyFilters = () => {
    this.datatable.filterData(this.filters);
  };

  clearFilters = () => {
    //Object.keys(this.filters).forEach(key => this.refs[key].value = '');
    //Just gonna use jquery for now...
    const $ = window.jQuery;
    $('.advanced-filters input, .advanced-filters select').val('');
    this.filters = {};
    this.datatable.filters = {};
    this.datatable.filterData({});
    this.setState({
      shipDateFilter: 'today',
      orderStatusFilter: '',
      packageTypeNameFilter: [],
      carrierFilter: [],
      currencyFilter: '',
    });
    this.customerFilter && this.customerFilter.setValue('');
    this.agentFilter && this.agentFilter.setValue('');
  };

  renderTableActions = () => {
    const { currentPage, numberOfPages, totalItems, itemsPerPage } = this.props;

    return (
      <tr>
        <td colSpan={this.columns.length}>
          {this.props.isFreightcomAdmin &&
            <Button
              bsStyle="primary"
              bsSize="small"
              style={{ marginRight: '5px' }}
              onClick={e => {
                e.preventDefault();
                this.setState({
                  confirmInvoiceDialog: !this.state.confirmInvoiceDialog,
                });
              }}
              disabled={
                this.state.checkedOrderList.length > 0 &&
                  this.state.checkedOrderList.reduce((acc, order) => {
                    return acc + order.unbilledCharges;
                  }, 0) > 0
                  ? false
                  : true
              }
            >
              <Icon name="server" /> Generate Invoice
            </Button>}

          <div className="pull-right" style={{ marginLeft: '10px' }}>
            <DatatablePagination
              numberOfPages={numberOfPages}
              currentPage={currentPage}
              totalItems={totalItems}
              itemsPerPage={itemsPerPage}
              paginate={this.paginate}
            />
          </div>

          {!this.state.showFilter &&
            <Button
              bsStyle="default"
              bsSize="small"
              className="pull-right"
              onClick={this.showFilters}
            >
              <Icon name="search" /> Search
            </Button>}
        </td>
      </tr>
    );
  };

  loadData = (page = 1, itemsPerPage, filters, sortOrder, paginate) => {
    this.sortOrder = sortOrder;

    if (paginate) {
      this.props.loadSubmittedOrders(page, itemsPerPage, filters, sortOrder, {
        noUpdatePage: true,
      });
    } else {
      this.props
        .loadSubmittedOrders(page, itemsPerPage, filters, sortOrder)
        .then(() => this.props.loadCountOrders('SUBMITTED_ONLY', this.filters));
    }
  };

  paginate = (page = 1) => {
    this.props.loadSubmittedOrders(page, 10, this.filters, this.sortOrder, {
      noUpdatePage: true,
    });
  };

  showFilters = e => {
    const filtersInitialized = this.state.filtersInitialized;

    this.setState(
      {
        showFilter: true,
        filtersInitialized: true,
      },
      () => {
        this.props.loadOrderCount();

        if (!filtersInitialized) {
          this.props.loadFilterData();
        }
      },
    );
  };

  renderDataFilters = () => {
    return (
      <div
        className="jarviswidget jarviswidget-sortable jarviswidget-color-blue"
        data-widget-colorbutton="false"
        data-widget-deletebutton="false"
      >
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
          <h2 />
        </header>
        <div>
          <div className="widget-body no-padding advanced-filters">
            <div className="panel-body">
              <div
                className="form-horizontal"
                onKeyUp={e => {
                  const isCombobox =
                    e.target.getAttribute('role') === 'combobox';
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
                }}
              >
                <Row>
                  <Col sm={6}>
                    <div className="form-group">
                      <label className="control-label col-sm-4">
                        Transaction #:
                      </label>
                      <div className="col-sm-8">
                        <input
                          autoFocus
                          type="text"
                          className="form-control input-sm"
                          onChange={e => {
                            this.addFilter({ eshipperOid: e.target.value });
                          }}
                        />
                      </div>
                    </div>
                  </Col>

                  <Col sm={6}>
                    <div className="form-group">
                      <label className="control-label col-sm-4">
                        Tracking #:
                      </label>
                      <div className="col-sm-8">
                        <input
                          autoFocus
                          type="text"
                          className="form-control input-sm"
                          onChange={e => {
                            this.addFilter({ trackingNumber: e.target.value });
                          }}
                        />
                      </div>
                    </div>
                  </Col>

                  <Col sm={6}>
                    <div className="form-group">
                      <label className="control-label col-sm-4">
                        Bill of Lading #:
                      </label>
                      <div className="col-sm-8">
                        <input
                          type="text"
                          ref="bolId"
                          className="form-control input-sm"
                          onChange={e =>
                            this.addFilter({ bolId: e.target.value })}
                        />
                      </div>
                    </div>
                  </Col>

                  <Col sm={6}>
                    <div className="form-group">
                      <label className="control-label col-sm-4">
                        Reference #:
                      </label>
                      <div className="col-sm-8">
                        <input
                          type="text"
                          ref="referenceCode"
                          className="form-control input-sm"
                          onChange={e =>
                            this.addFilter({ referenceCode: e.target.value })}
                        />
                      </div>
                    </div>
                  </Col>

                  <Col sm={6}>
                    <div className="form-group">
                      <label className="control-label col-sm-4">Origin:</label>
                      <div className="col-sm-8">
                        <input
                          type="text"
                          ref="origin"
                          className="form-control input-sm"
                          placeholder="City, Zip or Country"
                          onChange={e =>
                            this.addFilter({ origin: e.target.value })}
                        />
                      </div>
                    </div>
                  </Col>

                  <Col sm={6}>
                    <div className="form-group">
                      <label className="control-label col-sm-4">
                        Destination:
                      </label>
                      <div className="col-sm-8">
                        <input
                          type="text"
                          ref="destination"
                          className="form-control input-sm"
                          placeholder="City, Zip or Country"
                          onChange={e =>
                            this.addFilter({ destination: e.target.value })}
                        />
                      </div>
                    </div>
                  </Col>

                  <Col sm={12}>
                    <Row>
                      <Col sm={6}>
                        <div className="form-group">
                          <label className="control-label col-sm-4">
                            Ship Date:
                          </label>
                          <div className="col-sm-8">
                            <select
                              ref="shipDate"
                              className="form-control input-sm"
                              onChange={e => {
                                this.addFilter({
                                  scheduledShipDate: e.target.value,
                                });
                                this.setState({
                                  shipDateFilter: e.target.value,
                                });
                              }}
                            >
                              <option value="">Show All</option>
                              <option value="today">Today</option>
                              <option value="tomorrow">Tomorrow</option>
                              <option value="upcoming">Upcoming</option>
                              <option value="past">Past</option>
                              <option value="custom">Custom range</option>
                            </select>
                          </div>
                        </div>
                      </Col>
                      {this.state.shipDateFilter === 'custom' &&
                        <Col sm={6}>
                          <div className="form-group">
                            <div className="col-sm-6">
                              <DatePicker
                                ref="shipDateStart"
                                className="form-control input-sm"
                                placeholder="Date start"
                                dateFormat="yy-mm-dd"
                                input={{
                                  onChange: value =>
                                    this.addFilter({
                                      scheduledShipDateFrom: value,
                                    }),
                                }}
                              />
                            </div>
                            <div className="col-sm-6">
                              <DatePicker
                                ref="shipDateEnd"
                                className="form-control input-sm"
                                placeholder="Date end"
                                dateFormat="yy-mm-dd"
                                input={{
                                  onChange: value =>
                                    this.addFilter({
                                      scheduledShipDateTo: value,
                                    }),
                                }}
                              />
                            </div>
                          </div>
                        </Col>}
                    </Row>
                  </Col>

                  <Col sm={6}>
                    <div className="form-group">
                      <label className="control-label col-sm-4">
                        Order Status:
                      </label>
                      <div className="col-sm-8">
                        <Select
                          ref="statusId"
                          value={this.state.orderStatusFilter}
                          multi
                          onChange={value => {
                            this.addFilter({ statusId: value });
                            this.setState({ orderStatusFilter: value });
                          }}
                          options={this.props.orderStatuses}
                          labelKey="name"
                          valueKey="id"
                          simpleValue
                        />
                      </div>
                    </div>
                  </Col>

                  {this.props.isAdminOrAgent &&
                    <Col sm={6}>
                      <div className="form-group">
                        <label className="control-label col-sm-4">
                          Invoice Status:
                        </label>
                        <div className="col-sm-8">
                          <select
                            className="form-control input-sm"
                            onChange={e => {
                              this.addFilter({ invoiceStatus: e.target.value });
                            }}
                          >
                            <option value="">Show All</option>
                            <option value="fully invoiced">
                              Fully Invoiced
                            </option>
                            <option value="unbilled charges">
                              Unbilled Charges
                            </option>
                          </select>
                        </div>
                      </div>
                    </Col>}

                  <Col sm={6}>
                    <div className="form-group">
                      <label className="control-label col-sm-4">Type:</label>
                      <div className="col-sm-8">
                        <Select
                          ref="packageTypeName"
                          value={this.state.packageTypeNameFilter}
                          multi={true}
                          onChange={value => {
                            this.addFilter({
                              packageTypeName: value
                                .map(item => item.id)
                                .join(','),
                            });
                            this.setState({ packageTypeNameFilter: value });
                          }}
                          options={this.props.packageTypes}
                          labelKey="description"
                          valueKey="id"
                          optionRenderer={item => {
                            return `${item.description} (${this.props.packageTypeCount[item.id] ? this.props.packageTypeCount[item.id].toLocaleString() : 0})`;
                          }}
                        />
                      </div>
                    </div>
                  </Col>

                  <Col sm={6}>
                    <div className="form-group">
                      <label className="control-label col-sm-4">
                        Currency:
                      </label>
                      <div className="col-sm-8">
                        <Select
                          ref="currency"
                          value={this.state.currencyFilter}
                          multi={true}
                          onChange={value => {
                            this.addFilter({
                              currency: value.map(item => item.to).join(','),
                            });
                            this.setState({ currencyFilter: value });
                          }}
                          options={this.props.currencies}
                          labelKey="to"
                          valueKey="to"
                        />
                      </div>
                    </div>
                  </Col>

                  <Col sm={6}>
                    <div className="form-group">
                      <label className="control-label col-sm-4">Carrier:</label>
                      <div className="col-sm-8">
                        <Select
                          ref="carrierName"
                          value={this.state.carrierFilter}
                          multi={true}
                          onChange={value => {
                            this.addFilter({
                              carrierId: value.map(item => item.id).join(','),
                            });
                            this.setState({ carrierFilter: value });
                          }}
                          options={this.props.carriers}
                          labelKey="name"
                          valueKey="id"
                        />
                      </div>
                    </div>
                  </Col>

                  <Col sm={6}>
                    <div className="form-group">
                      <label className="control-label col-sm-4">
                        Has Claim:
                      </label>
                      <div className="col-sm-8">
                        <select
                          className="form-control input-sm"
                          onChange={e => {
                            this.addFilter({
                              hasClaim: e.target.value,
                            });
                          }}
                        >
                          <option value="">Show All</option>
                          <option value="Yes">Yes</option>
                          <option value="No">No</option>
                        </select>
                      </div>
                    </div>
                  </Col>

                  {this.props.isAdminOrAgent &&
                    <Col sm={6}>
                      <div className="form-group">
                        <label className="control-label col-sm-4">
                          Customer:
                        </label>
                        <div className="col-sm-8">
                          <CustomerAutoSuggest
                            name="customer"
                            ref={input => (this.customerFilter = input)}
                            onChange={customer => {
                              this.addFilter({
                                customerId: customer ? customer.id : '',
                              });
                            }}
                          />
                        </div>
                      </div>
                    </Col>}

                  {this.props.isFreightcomAdmin &&
                    <Col sm={6}>
                      <div className="form-group">
                        <label className="control-label col-sm-4">Agent:</label>
                        <div className="col-sm-8">
                          <AgentAutoSuggest
                            name="agent"
                            ref={input => (this.agentFilter = input)}
                            onChange={agent => {
                              const agentRole = getAgentRoleOfUser(agent);
                              const agentId = agentRole && agentRole.id;
                              this.addFilter({
                                agentId: agentId || '',
                              });
                            }}
                          />
                        </div>
                      </div>
                    </Col>}
                </Row>
              </div>

              <br />
              <ButtonToolbar className="pull-right">
                <Button
                  bsStyle="default"
                  bsSize="small"
                  onClick={this.clearFilters}
                >
                  Clear Filters
                </Button>
                <Button
                  bsStyle="primary"
                  bsSize="small"
                  onClick={this.applyFilters}
                >
                  Apply Filters
                </Button>
              </ButtonToolbar>
            </div>
          </div>
        </div>
      </div>
    );
  };

  addToOrderList = (checked, order) => {
    const { checkedOrderList } = this.state;
    if (checked && !checkedOrderList.find(item => item.id === order.id)) {
      this.setState({ checkedOrderList: [...checkedOrderList, order] });
    } else if (
      !checked &&
      checkedOrderList.find(item => item.id === order.id)
    ) {
      const modified = checkedOrderList.filter(item => {
        return item.id !== order.id;
      });
      this.setState({ checkedOrderList: modified });
    }
  };
  //To do: detect if it's unbilled
  selectAllUnbilledOrders = checked => {
    if (checked) {
      this.setState({
        checkedOrderList: this.checkboxes.map((checkbox, index) =>
          this.props.items.find(
            order => order.id === parseInt(checkbox.value, 10),
          ),
        ),
      });
    } else {
      this.setState({ checkedOrderList: [] });
    }
  };

  formatInvoiceDialog = () => {
    const { checkedOrderList } = this.state;
    const uniqueCustomer = [];
    for (let order of checkedOrderList) {
      if (findIndex(uniqueCustomer, order.customer) === -1) {
        uniqueCustomer.push(order.customer);
      }
    }
    return `${uniqueCustomer.length} invoice(s) will be created.`;
  };

  generateInvoices = () => {
    const { checkedOrderList } = this.state;
    if (checkedOrderList.length === 1) {
      this.props.generateInvoice(
        {
          orders: [{ id: checkedOrderList[0].id }],
        },
        data => {
          this.props.addNotification({
            title: 'Invoice is generated',
            message: `<a href="/invoices/${data.id}">Invoice</a> for ${checkedOrderList[0].customer.name} has been generated...`,
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
        },
      );
    } else {
      const uniqueCustomer = [];
      for (let order of checkedOrderList) {
        if (findIndex(uniqueCustomer, order.customer) === -1) {
          uniqueCustomer.push(order.customer);
        }
      }
      for (let customer of uniqueCustomer) {
        const orders = checkedOrderList
          .filter(order => order.customer === customer)
          .map(order => {
            return { id: order.id };
          });
        this.props.generateInvoice({ orders: orders }, data => {
          this.props.addNotification({
            title: 'Invoice is generated',
            message: `<a href="/invoices/${data.id}">Invoice</a> for ${customer.name} has been generated...`,
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
        });
      }
    }
    this.setState({ confirmInvoiceDialog: false });
  };
}

const mapStateToProps = state => {
  return {
    ...getPageItems(state, 'order', [
      { entity: 'shippingAddress', key: 'shipFrom' },
      { entity: 'shippingAddress', key: 'shipTo' },
      { entity: 'customer', key: 'customer' },
    ]),
    // NOTE:  Using item.id for checking orderstatus not equal to draft, quoted or deleted
    orderStatuses: selectAllFromEntities(state, 'orderStatus').filter(
      item => item.id !== '10' && item.id !== '16' && item.id !== '999',
    ),
    packageTypes: [
      ...selectAllFromEntities(state, 'packageType'),
      { id: 'work-order', description: 'Work Order' },
    ],
    packageTypeCount: selectOrderCountPerType(state),
    carriers: selectCarrierList(state),
    currencies: selectAllFromEntities(state, 'currency'),
    isAgent: checkRoleIfAgent(state),
    isAdmin: checkRoleIfFreightcom(state),
    isAdminOrAgent: checkRoleIfAdminOrAgent(state),
    isFreightcomAdmin: checkRoleIfFreightcomAdmin(state),
    changedObjects: hasChangedInterest(state, SHIPMENT_LIST).length > 0,
  };
};

const mapDispatchToProps = {
  loadSubmittedOrders,
  duplicateOrder,
  viewOrder,
  cancelOrder,
  updateStatus,
  generateInvoice,
  addNotification,
  loadOrderCount,
  clearChangedObjects,
  loadCountOrders,
};

export default connect(mapStateToProps, mapDispatchToProps, null, {
  withRef: true,
})(ShipmentList);
