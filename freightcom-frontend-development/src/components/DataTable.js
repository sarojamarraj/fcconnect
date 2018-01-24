import React, { Component } from 'react';
import PropTypes from 'prop-types';
import debounce from 'lodash/debounce';
import DatatablePagination from './DatatablePagination';
import { Button } from 'react-bootstrap';
import Icon from './Icon';

class DataTable extends Component {
  static propTypes = {
    loadData: PropTypes.func,
    columns: PropTypes.array,
    items: PropTypes.array,
    currentPage: PropTypes.oneOfType([PropTypes.string, PropTypes.number])
      .isRequired,
    numberOfPages: PropTypes.oneOfType([PropTypes.string, PropTypes.number])
      .isRequired,
    isFetching: PropTypes.bool,
  };

  constructor(props) {
    super(props);
    this.itemsPerPage = props['itemsPerPage'] || 10;
    this.pageToLoad = 1;
    this.sortOrder = { sortBy: 'id', order: 'DESC' };
    this.filters = props.filters || {};

    this.state = {
      showFilter: false,
    };
  }

  render() {
    const {
      children = false,
      isFetching = false,
      renderPagination = true,
    } = this.props;

    let tableClassName =
      'table table-striped table-bordered wo-search-results dataTable no-footer';

    if (this.props.className) {
      tableClassName += ` ${this.props.className}`;
    }

    if (isFetching) {
      tableClassName += ' table-loading';
    }

    return (
      <div className="dataTables_wrapper _form-inline dt-bootstrap no-footer">
        {this.props.groupedFilters &&
          this.state.showFilter &&
          this.renderTableFiltersGrouped()}
        <div className="table-responsive">
          {children
            ? <table className={tableClassName}>
                {children}
              </table>
            : <table className={tableClassName}>
                <thead>
                  {this.renderTableActionButtons()}
                  {!this.props.groupedFilters && this.renderTableFilters()}
                  {this.renderTableHeaders()}
                </thead>
                <tbody>
                  {this.renderTableRows()}
                </tbody>
              </table>}
          {renderPagination && this.renderTablePagination()}
        </div>
      </div>
    );
  }

  renderTablePagination = () => {
    const { numberOfPages = 1, currentPage = 1, totalItems = 1 } = this.props;

    return (
      <DatatablePagination
        numberOfPages={numberOfPages}
        currentPage={currentPage}
        totalItems={totalItems}
        itemsPerPage={this.itemsPerPage}
        paginate={this.paginate}
        showCount={true}
      />
    );
  };

  renderTableActionButtons = () => {
    if (!this.props.renderTableActionButtons && !this.props.groupedFilters) {
      return null;
    }

    if (typeof this.props.renderTableActionButtons === 'function') {
      return this.props.renderTableActionButtons();
    }

    return (
      <tr>
        <th colSpan={this.props.columns.length}>
          {this.props.actionButtons &&
            <div className="pull-left">
              {this.props.actionButtons}
            </div>}

          {this.props.renderPagination && this.renderTablePagination()}
          <div className="pull-right">
            {!this.state.showFilter &&
              <Button
                className="btn btn-default btn-sm pull-right"
                onClick={e => this.setState({ showFilter: true })}
              >
                <i className="fa fa-search" />{' '}Search
              </Button>}
          </div>
        </th>
      </tr>
    );
  };

  renderTableFilters = () => {
    if (this.props.renderTableFilters === false) {
      return null;
    }

    if (typeof this.props.renderTableFilters === 'function') {
      return this.props.renderTableFilters(this.filterData);
    }

    return (
      <tr>
        {this.props.columns.map((column, index) => {
          if (typeof column['filter'] === 'function') {
            return (
              <td key={'filter-' + index}>
                {column.filter(this.filterData)}
              </td>
            );
          }

          if (typeof column['filter'] === 'string') {
            return (
              <td key={'filter-' + index}>
                <input
                  type="text"
                  className="form-control"
                  placeholder={column.label || ''}
                  onChange={e => {
                    this.filterData({ [column.filter]: e.target.value });
                  }}
                />
              </td>
            );
          }

          if (!column.label || column['filter'] === false) {
            return <td key={'filter-' + index} />;
          }

          return (
            <td key={'filter-' + index}>
              <input
                type="text"
                className="form-control"
                placeholder={column.label}
                onChange={e => {
                  this.filterData({ [column.field]: e.target.value });
                }}
              />
            </td>
          );
        })}
      </tr>
    );
  };

  // WIP: another way of rendering filter... does not work yet
  renderTableFiltersGrouped = () => {
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
                <div className="row">
                  {this.props.columns.map((col, idx) => {
                    if (col.filter === false || !col.label) return null;
                    return (
                      <div className="col-sm-6" key={idx}>
                        <div className="form-group">
                          <label className="control-label col-sm-4">
                            {col.label}
                          </label>
                          <div className="col-sm-8">
                            {typeof col.filter === 'function'
                              ? col.filter(this.addFilter, this.filters)
                              : <input
                                  type="text"
                                  className="form-control input-sm"
                                  defaultValue={this.filters[col.field] || ''}
                                  onChange={e =>
                                    this.addFilter({
                                      [col.field]: e.target.value,
                                    })}
                                />}
                          </div>
                        </div>
                      </div>
                    );
                  })}
                </div>
                <div className="btn-toolbar pull-right">
                  <button
                    className="btn btn-default btn-sm"
                    onClick={this.clearFilters}
                  >
                    Clear
                  </button>
                  <button
                    className="btn btn-primary btn-sm"
                    onClick={e => {
                      e.target.blur();
                      this.applyFilters();
                    }}
                  >
                    Search
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  };

  showFilters = () => {
    this.setState({ showFilter: true });
  };

  hideFilters = () => {
    this.setState({ showFilter: false });
  };

  renderTableHeaders = () => {
    if (this.props.renderTableHeaders === false) {
      return null;
    }

    if (typeof this.props.renderTableHeaders === 'function') {
      return this.props.renderTableHeaders(this.sortData);
    }

    return (
      <tr>
        {this.props.columns.map((column, index) => {
          const className = column.className ? column.className : '';
          const thAttributes = {
            key: 'header-' + index,
            style: column.style || {
              width: column.width
                ? column.width.trim().replace('px', '') + 'px'
                : 'auto',
            },
            className,
          };

          if (!column.label) {
            return <th {...thAttributes} />;
          }
          if (typeof column['label'] === 'function') {
            return (
              <th {...thAttributes}>
                {column['label']()}
              </th>
            );
          }
          if (!column.field || column['sorting'] === false) {
            return (
              <th {...thAttributes}>
                {column.label}
              </th>
            );
          }

          return (
            <th
              {...thAttributes}
              className={this.getHeaderRowClassName(column)}
              onClick={e => this.sortData(column.sortField || column.field)}
            >
              {column.label}
            </th>
          );
        })}
      </tr>
    );
  };

  renderTableRows = () => {
    if (this.props.items.length === 0) {
      return (
        <tr>
          <td colSpan={this.props.columns.length} className="text-center">
            <h3>Empty Result</h3>
          </td>
        </tr>
      );
    }

    if (typeof this.props.renderTableRows === 'function') {
      return this.props.renderTableRows();
    }

    return this.props.items.map((item, itemIndex) => {
      const { id = 'item-index-' + itemIndex } = item;

      return (
        <tr key={'data-table-row-' + id}>
          {this.props.columns.map((col, index) => {
            const className = col.className ? col.className : '';
            const tdAttributes = {
              key: `dt-${id}-${index}`,
              className,
              style: col.style || {},
            };
            if (typeof col['cell'] === 'function')
              return (
                <td {...tdAttributes}>
                  {col['cell'](item)}
                </td>
              );
            if (typeof col['cell'] === 'string')
              return (
                <td {...tdAttributes}>
                  {col['cell']}
                </td>
              );

            return (
              <td {...tdAttributes}>
                {item[col.field] || ''}
              </td>
            );
          })}
        </tr>
      );
    });
  };

  getHeaderRowClassName = column => {
    const className = column.className ? ` ${column.className}` : '';
    if (this.sortOrder.sortBy !== column.field) {
      return 'sorting' + className;
    }
    return this.sortOrder.order === 'DESC'
      ? 'sorting_desc' + className
      : 'sorting_asc' + className;
  };

  paginate = (pageToLoad = 1) => {
    this.pageToLoad = pageToLoad;
    this.loadData(true);
  };

  sortData = sortBy => {
    this.pageToLoad = 1;
    const order = this.sortOrder.order === 'ASC' &&
      this.sortOrder.sortBy === sortBy
      ? 'DESC'
      : 'ASC';
    this.sortOrder = { sortBy, order };
    this.loadData();
  };

  addFilter = filter => {
    this.filters = Object.assign(this.filters, filter);

    this.setState({ filters: this.filters });
  };

  applyFilters = () => {
    this.filterData(this.filters);
  };

  clearFilters = e => {
    e.target.blur();
    const $ = window.jQuery;
    $('.advanced-filters input, .advanced-filters select').val('');

    if (typeof this.props.clearFilters === 'function') {
      this.props.clearFilters(e);
    }

    this.filters = {};
    this.filterData({});
  };

  filterData = debounce((filters = {}) => {
    this.pageToLoad = 1;
    this.filters = Object.assign(this.filters, filters);
    this.loadData();
  }, 500);

  loadData = paginate => {
    //console.log('loading data table... ');
    this.props.loadData(
      this.pageToLoad,
      this.itemsPerPage,
      this.filters,
      this.sortOrder,
      paginate,
    );
  };
}

export default DataTable;
