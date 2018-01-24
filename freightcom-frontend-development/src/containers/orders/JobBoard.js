import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Row, Col, ButtonToolbar, Button } from 'react-bootstrap';
import Select from 'react-select';

import { DatePicker } from '../../components/forms/inputs/InputDate';
import Icon from '../../components/Icon';
import DataTable from '../../components/DataTable';
import OrderStatusLabel from '../../components/orders/OrderStatusLabel';
import Pagination from '../../components/Pagination';

import CustomerAutoSuggest from '../../components/customers/CustomerAutoSuggest';
import AgentAutoSuggest from '../../components/agents/AgentAutoSuggest';

import { getPageItems, selectAllFromEntities } from '../../reducers';
import { loadJobBoard, viewOrder, loadCountOrders } from '../../actions/orders';

import { getAgentRoleOfUser } from '../../utils';

class JobBoard extends Component {
  constructor(props) {
    super(props);
    this.datatable = null;
    this.state = {
      showFilter: false,
      filtersInitialized: false,
      carrierFilter: [],
      orderStatusFilter: [],
      customerFilter: '',
      agentFilter: '',
      colorCodeFilter: '',
      initialLoad: this.props.active,
    };
    this.filters = {};

    this.colorCodeOptions = [
      { label: 'ORANGE' },
      { label: 'YELLOW' },
      { label: 'RED' },
      { label: 'GREEN' },
    ];
    this.columns = [
      {
        label: 'BOL #',
        field: 'bolId',
        filter: filter =>
          <input
            type="text"
            autoFocus
            className="form-control input-sm"
            onChange={e => filter({ bolId: e.target.value })}
          />,
      },
      {
        label: 'Customer',
        field: 'customerName',
        cell: order => order.customer.name,
        filter: filter => {
          return (
            <CustomerAutoSuggest
              name="customer"
              value={this.state.customerFilter}
              onChange={customer => {
                filter({
                  customerId: customer ? customer.id : '',
                });
                this.setState({ customerFilter: customer || '' });
              }}
            />
          );
        },
      },
      {
        label: 'Service',
        field: 'serviceName',
      },
      {
        label: 'Carrier',
        field: 'carrierName',
        filter: filter => {
          return (
            <Select
              value={this.state.carrierFilter}
              multi={true}
              onChange={value => {
                filter({
                  carrierName: value.map(item => item.id).join(','),
                });
                this.setState({ carrierFilter: value });
              }}
              options={this.props.carriers}
              labelKey="name"
              valueKey="id"
            />
          );
        },
      },
      {
        label: 'Delivery Date',
        field: 'deliveryDate',
        filter: filter =>
          <DatePicker
            dateFormat="yy-mm-dd"
            className="form-control input-sm"
            input={{
              onChange: value => filter({ deliveryDate: value }),
            }}
          />,
      },
      {
        label: 'Ship Date',
        field: 'shipDate',
        filter: filter =>
          <DatePicker
            dateFormat="yy-mm-dd"
            className="form-control input-sm"
            input={{
              onChange: value => filter({ shipDate: value }),
            }}
          />,
      },
      {
        label: 'Color Code',
        field: 'colorCode',
        filter: filter =>
          <Select
            value={this.state.colorCodeFilter}
            multi={true}
            onChange={value => {
              filter({ colorCode: value.map(item => item.label).join(',') });
              this.setState({ colorCodeFilter: value });
            }}
            options={this.colorCodeOptions}
            labelKey="label"
            valueKey="label"
          />,
      },
      {
        label: 'Status',
        field: 'statusName',
        filter: filter =>
          <Select
            value={this.state.orderStatusFilter}
            multi={true}
            onChange={value => {
              filter({
                statusId: value.map(item => item.id).join(','),
              });
              this.setState({ orderStatusFilter: value });
            }}
            options={this.props.orderStatuses}
            labelKey="name"
            valueKey="id"
          />,
        cell: order =>
          <OrderStatusLabel
            statusId={order.statusId}
            statusName={order.statusName}
          />,
      },
      {
        label: 'Comment',
        field: 'latestComment',
      },
      {
        label: 'Agent',
        field: 'agent',
        cell: order => {
          return order && order.agent && order.agent.name;
        },
        filter: filter =>
          <AgentAutoSuggest
            name="agent"
            value={this.state.agentFilter}
            onChange={agent => {
              filter({
                agentId: getAgentRoleOfUser(agent).id || '',
              });
              this.setState({ agentFilter: agent || '' });
            }}
          />,
      },
      {
        cell: order =>
          <Button
            bsStyle="primary"
            bsSize="xsmall"
            onClick={e => this.props.viewOrder(order.id)}
          >
            <Icon name="edit" /> Edit
          </Button>,
      },
    ];
  }

  componentWillReceiveProps(nextProps) {
    if (!this.state.initialLoad && !this.props.active && nextProps.active) {
      this.setState({ initialLoad: true }, () => {
        this.props
          .loadJobBoard()
          .then(() =>
            this.props.loadCountOrders('JOB_BOARD', this.filters, '_jobBoard'),
          );
      });
    }
  }

  componentWillMount() {
    if (this.state.initialLoad) {
      this.props
        .loadJobBoard()
        .then(() =>
          this.props.loadCountOrders('JOB_BOARD', this.filters, '_jobBoard'),
        );
    }
  }

  render() {
    return (
      <div>
        {this.state.showFilter && this.renderCustomFilters()}
        <DataTable
          className="no-margin"
          loadData={this.loadData}
          columns={this.columns}
          {...this.props.jobBoardData}
          ref={datatable => (this.datatable = datatable)}
          renderTableFilters={this.renderTableActions}
        />
      </div>
    );
  }

  loadData = (page, items, filters, sort, paginate) => {
    if (paginate) {
      this.props.loadJobBoard(page, items, filters, sort, {
        noUpdatePage: true,
      });
    } else {
      this.props
        .loadJobBoard(page, items, filters, sort)
        .then(() =>
          this.props.loadCountOrders('JOB_BOARD', this.filters, '_jobBoard'),
        );
    }
  };

  renderTableActions = () => {
    return (
      <tr>
        <td colSpan={this.columns.length}>
          <div className="pull-right" style={{ marginLeft: '10px' }}>
            <Pagination
              currentPage={this.props.jobBoardData.currentPage}
              paginate={page => this.datatable.paginate(page)}
              itemCount={this.props.jobBoardData.items.length}
              itemsPerPage={this.props.jobBoardData.itemsPerPage}
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

  showFilters = e => {
    const filtersInitialized = this.state.filtersInitialized;

    this.setState(
      {
        showFilter: true,
        filtersInitialized: true,
      },
      () => {
        if (!filtersInitialized) {
          this.props.loadFilterData();
        }
      },
    );
  };

  renderCustomFilters = () => {
    return (
      <div
        className="jarviswidget jarviswidget-sortable jarviswidget-color-blue"
        data-widget-color-button="false"
        data-widget-deletebutton="false"
      >
        <header role="heading">
          <div className="jarviswidget-ctrls" role="menu">
            <a
              href="#"
              className="button-icon jarvis-widget-delete-btn"
              rel="tooltip"
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
                                    this.addFilter({
                                      [col.field]: e.target.value,
                                    })}
                                />}
                          </div>
                        </div>
                      </Col>
                    );
                  })}
                </Row>
              </div>

              <br />
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
                  onClick={e => {
                    e.target.blur();
                    this.applyFilters();
                  }}
                >
                  Search
                </Button>
              </ButtonToolbar>
            </div>
          </div>
        </div>
      </div>
    );
  };

  addFilter = filter => {
    this.filters = Object.assign(this.filters, filter);
  };

  applyFilters = () => {
    this.datatable.filterData(this.filters);
  };

  clearFilters = e => {
    e.target.blur();
    const $ = window.jQuery;
    $('.advanced-filters input, .advanced-filters select').val('');
    this.filters = {};
    this.datatable.filters = {};
    this.datatable.filterData({});
    this.setState({
      carrierFilter: [],
      orderStatusFilter: [],
      agentFilter: '',
      customerFilter: '',
      colorCodeFilter: '',
    });
  };

  loadOrders = () => {
    //console.log('loading orders...');
  };
}

const mapStateToProps = state => {
  return {
    jobBoardData: getPageItems(state, 'jobBoard'),
    orderStatuses: selectAllFromEntities(state, 'orderStatus'),
    carriers: selectAllFromEntities(state, 'carrier'),
  };
};

const mapDispatchToProps = {
  loadJobBoard,
  viewOrder,
  loadCountOrders,
};

export default connect(mapStateToProps, mapDispatchToProps, null, {
  withRef: true,
})(JobBoard);
