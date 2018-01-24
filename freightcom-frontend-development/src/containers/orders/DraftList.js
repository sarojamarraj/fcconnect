import React, { PureComponent } from 'react';
import { connect } from 'react-redux';
import formatDate from 'date-fns/format';
import { Row, Col, ButtonToolbar, Button, ButtonGroup } from 'react-bootstrap';
import Select from 'react-select';

import { addTab, removeTabByOrderId } from '../../actions/tabs';
import {
  loadDraftOrderById,
  loadDraftOrders,
  loadCountOrders,
  deleteOrder,
  initializeOrderForm,
  checkDirty,
} from '../../actions/orders';
import { getPageItems, selectAllFromEntities } from '../../reducers';
import {
  formatAddress,
  displayCompanyOrContactName,
  displayShipmentType,
} from '../../utils';

import {
  registerChangedInterest,
  hasChangedInterest,
  clearChangedObjects,
} from '../../reducers/changed-objects';

import Icon from '../../components/Icon';
import DataTable from '../../components/DataTable';
import { DatePicker } from '../../components/forms/inputs/InputDate';
import Pagination from '../../components/Pagination';

const DRAFT_LIST = 'draft-list';
const DRAFT_ORDER = 'draftOrder';
const ORDER = 'order';

class DraftList extends PureComponent {
  constructor(props) {
    super(props);

    registerChangedInterest(DRAFT_LIST, DRAFT_ORDER);
    registerChangedInterest(DRAFT_LIST, ORDER);

    this.datatable = null;
    this.state = {
      showFilter: false,
      filtersInitialized: false,
      packageTypeNameFilter: [],
      initialLoad: this.props.active,
    };
    this.filters = {};
    this.columns = [
      {
        label: 'Quote #',
        field: 'id',
      },
      {
        label: 'Date Created',
        field: 'createdAt',
        filter: filter =>
          <DatePicker
            dateFormat="yy-mm-dd"
            className="form-control input-sm"
            input={{
              onChange: value => filter({ createdAt: value }),
            }}
          />,
        cell: order => formatDate(order.createdAt, 'MMM DD, YYYY'),
      },
      {
        label: 'Reference #',
        field: 'referenceCode',
        filter: filter =>
          <input
            autoFocus
            type="text"
            className="form-control input-sm"
            onChange={e => filter({ referenceCode: e.target.value })}
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
        cell: order => formatDate(order.scheduledShipDate, 'MMM DD, YYYY'),
      },
      {
        label: 'Type',
        field: 'packageTypeName',
        filter: filter =>
          <Select
            ref="packageTypeName"
            className="_form-control _input-sm"
            onChange={value => {
              filter({ packageTypeName: value.map(i => i.id).join(',') });
              this.setState({ packageTypeNameFilter: value });
            }}
            options={this.props.packageTypes}
            multi={true}
            labelKey="description"
            valueKey="id"
            value={this.state.packageTypeNameFilter}
          />,
        cell: row =>
          <div>
            <span style={{ display: 'block' }}>
              {displayShipmentType(row.packageTypeName)}
            </span>
            {row.customWorkOrder &&
              <span className="label label-default">Work Order</span>}
          </div>,
      },
      {
        label: 'Ship From',
        field: 'shipFrom',
        cell: ({ shipFrom = {} }) =>
          <div>
            <b>{displayCompanyOrContactName(shipFrom)}</b>
            <br />
            <span>{formatAddress(shipFrom)}</span>
          </div>,
      },
      {
        label: 'Ship To',
        field: 'shipTo',
        cell: ({ shipTo = {} }) =>
          <div>
            <b>{displayCompanyOrContactName(shipTo)}</b>
            <br />
            <span>{formatAddress(shipTo)}</span>
          </div>,
      },
      {
        width: '120',
        cell: ({ id }) =>
          <ButtonGroup>
            <Button
              onClick={e => this.openDraftOrder(id)}
              bsStyle="primary"
              bsSize="xs"
            >
              <Icon name="edit" /> Edit
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
                    //this.props.deleteOrder(id, () => {
                    //this.props.removeTabByOrderId(id);
                    //this.loadOrders();
                    //  })}
                    this.props.deleteOrder(id, this.loadOrders);
                  }}
                >
                  <Icon name="trash-o" /> Delete
                </a>
              </li>
            </ul>
          </ButtonGroup>,
      },
    ];
  }

  componentWillReceiveProps(nextProps) {
    if (
      !this.props.active &&
      nextProps.active &&
      (!this.state.initialLoad || this.props.changedObjects)
    ) {
      this.setState({ initialLoad: true }, () => {
        this.props
          .clearChangedObjects(DRAFT_LIST)
          .then(() => this.props.loadDraftOrders())
          .then(() =>
            this.props.loadCountOrders(
              'DRAFT_ONLY',
              this.filters,
              '_draftOrder',
            ),
          );
      });
    }
  }

  componentWillMount() {
    if (this.state.initialLoad) {
      this.props
        .loadDraftOrders()
        .then(() =>
          this.props.loadCountOrders('DRAFT_ONLY', this.filters, '_draftOrder'),
        );
    }
  }

  render() {
    return (
      <div>
        {this.state.showFilter && this.renderCustomFilters()}
        <DataTable
          className="no-margin"
          columns={this.columns}
          loadData={this.loadDraftOrders}
          items={this.props.items}
          currentPage={this.props.currentPage}
          numberOfPages={this.props.numberOfPages}
          totalItems={this.props.totalItems}
          isFetching={this.props.isFetching}
          ref={datatable => (this.datatable = datatable)}
          renderTableFilters={this.renderTableActions}
        />
      </div>
    );
  }

  loadDraftOrders = (page, items, filters, sort, paginate) => {
    if (paginate) {
      this.props.loadDraftOrders(page, items, filters, sort, {
        noUpdatePage: true,
      });
    } else {
      this.props
        .loadDraftOrders(page, items, filters, sort)
        .then(() =>
          this.props.loadCountOrders('DRAFT_ONLY', this.filters, '_draftOrder'),
        );
    }
  };

  renderTableActions = () => {
    return (
      <tr>
        <td colSpan={this.columns.length}>
          <div className="pull-right" style={{ marginLeft: '10px' }}>
            <Pagination
              currentPage={this.props.currentPage}
              paginate={page => this.datatable.paginate(page)}
              itemCount={this.props.items.length}
              itemsPerPage={this.props.itemsPerPage}
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
  };

  loadOrders = () => {
    this.datatable.loadData();
  };

  openDraftOrder = orderId => {
    this.props.loadDraftOrderById(orderId, true, () => {
      this.props.initializeOrderForm(orderId);
    });
  };
}

const mapStateToProps = state => {
  return {
    ...getPageItems(state, 'draftOrder', [
      { entity: 'shippingAddress', key: 'shipFrom' },
      { entity: 'shippingAddress', key: 'shipTo' },
    ]),
    packageTypes: selectAllFromEntities(state, 'packageType'),
    changedObjects: hasChangedInterest(state, DRAFT_LIST).length > 0,
  };
};

const mapDispatchToProps = {
  loadDraftOrderById,
  loadDraftOrders,
  deleteOrder,
  initializeOrderForm,
  addTab,
  removeTabByOrderId,
  checkDirty,
  clearChangedObjects,
  loadCountOrders,
};

export default connect(mapStateToProps, mapDispatchToProps, null, {
  withRef: true,
})(DraftList);
