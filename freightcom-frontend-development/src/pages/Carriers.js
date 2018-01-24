import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Modal, ButtonToolbar, Row, Col } from 'react-bootstrap';
import Icon from '../components/Icon';
import { Link } from 'react-router';
import {
  loadCarriers,
  loadCarrierById,
  updateCarrier,
} from '../actions/carriers';
import { reportPayables } from '../actions/payables';
import { getPageItems } from '../reducers';

import Widget from '../components/Widget';
import DataTable from '../components/DataTable';
import FormGroup from '../components/forms/FormGroup';
import AlertMessage from '../components/AlertMessage';
import Button from 'react-bootstrap-button-loader';
import { addNotification } from 'reapop';

class Carriers extends Component {
  dataTable = null;

  constructor(props) {
    super(props);
    this.state = {
      showEditModal: false,
      showAlertReportPayables: false,
      isSubmitting: false,
      showFilter: false,
      filters: {},
      selectedCarrier: {
        name: '',
        term: '',
      },
    };
  }

  componentWillMount() {
    this.props.loadCarriers();
  }

  render() {
    return (
      <Widget title="Manage Carriers">
        {this.state.showFilter && this.renderDataFilters()}
        <DataTable
          loadData={this.props.loadCarriers}
          columns={[
            {
              label: 'ID',
              field: 'id',
            },
            {
              label: 'Provider',
              field: 'implementingClass',
              sortField: 'carrier.implementingClass',
              cell: service =>
                service.implementingClass &&
                service.implementingClass.replace(/API$/, ''),
            },
            {
              label: 'Name',
              field: 'name',
            },
            {
              label: 'Term',
              field: 'term',
            },
            {
              width: '80',
              cell: row =>
                <div className="btn-group flex">
                  <Link
                    className="btn btn-xs btn-primary"
                    onClick={e => {
                      this.setCarrierModal(row);
                    }}
                  >
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
                    {1 === 2 &&
                      <li>
                        <Link
                          onClick={e => {
                            this.setCarrierModal(row);
                          }}
                        >
                          <Icon name="usd" /> Report Payables
                        </Link>
                      </li>}
                    <li>
                      <Link
                        onClick={e => {
                          this.setCarrierModal(row);
                        }}
                      >
                        <Icon name="pencil" /> Edit
                      </Link>
                    </li>
                  </ul>
                </div>,
            },
          ]}
          {...this.props.datatable}
          renderTableFilters={this.renderTableActions}
          ref={datatable => (this.datatable = datatable)}
        />
        {this.state.showAlertReportPayables &&
          <AlertMessage
            title="Report All Payables"
            text="Do you wish to report all payables for all carriers?"
            buttonToolbar={
              <ButtonToolbar>
                <Button
                  onClick={e => {
                    e.preventDefault();
                    this.setState({
                      showAlertReportPayables: false,
                    });
                  }}
                >
                  Cancel
                </Button>
                <Button
                  bsStyle="primary"
                  loading={this.state.isSubmitting}
                  onClick={e => {
                    e.preventDefault();
                    this.reportPayables();
                  }}
                >
                  Report
                </Button>
              </ButtonToolbar>
            }
          />}
        <Modal show={this.state.showEditModal}>
          <Modal.Header>
            <Modal.Title>Update Carrier</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <fieldset>
              <FormGroup
                label="Name"
                name="name"
                type="text"
                value={this.state.selectedCarrier.name}
                onChange={e =>
                  this.setState({
                    selectedCarrier: {
                      ...this.state.selectedCarrier,
                      name: e.target.value,
                    },
                  })}
              />
              <FormGroup label="Term">
                <select
                  name="term"
                  className="form-control"
                  value={this.state.selectedCarrier.term}
                  onChange={e =>
                    this.setState({
                      selectedCarrier: {
                        ...this.state.selectedCarrier,
                        term: e.target.value,
                      },
                    })}
                >
                  <option value="DAILY">Daily</option>
                  <option value="WEEKLY">Weekly</option>
                  <option value="BIWEEKLY">Biweekly</option>
                  <option value="MONTHLY">Monthly</option>
                </select>
              </FormGroup>
            </fieldset>
          </Modal.Body>
          <Modal.Footer>
            <Button
              bsStyle="default"
              onClick={e => this.setCarrierModal()}
            >
              Cancel
            </Button>
            <Button
              bsStyle="primary"
              onClick={e => {
                this.props.updateCarrier(this.state.selectedCarrier);
                this.setCarrierModal();
              }}
            >
              Save
            </Button>
          </Modal.Footer>
        </Modal>
      </Widget>
    );
  }

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
        <div className="panel-body">
          <div
            className="form-horizontal"
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
            }}
          >
            <Row>
              <Col sm={6}>
                <div className="form-group">
                  <label className="control-label col-sm-4">
                    Name:
                  </label>
                  <div className="col-sm-8">
                    <input
                      autoFocus
                      type="text"
                      className="form-control input-sm"
                      value={this.state.filters.name || ''}
                      onChange={e => {
                        this.setState({
                          filters: {
                            ...this.state.filters,
                            name: e.target.value,
                          },
                        });
                      }}
                    />
                  </div>
                </div>
              </Col>
            </Row>
            <Row>
              <Col sm={6}>
                <div className="form-group">
                  <label className="control-label col-sm-4">
                    Provider:
                  </label>
                  <div className="col-sm-8">
                    <input
                      type="text"
                      className="form-control input-sm"
                      value={this.state.filters.implementingClass || ''}
                      onChange={e => {
                        this.setState({
                          filters: {
                            ...this.state.filters,
                            implementingClass: e.target.value,
                          },
                        });
                      }}
                    />
                  </div>
                </div>
              </Col>
            </Row>
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
    );
  };

  applyFilters = () => {
    this.datatable.filterData(this.state.filters);
  };

  clearFilters = () => {
    this.datatable.filters = {};
    this.setState({ filters: {} }, this.applyFilters);
  };

  showFilters = () => {
    this.setState({ showFilter: true });
  };

  renderTableActions = () => {
    return (
      <tr>
        <td colSpan={5}>
          <Button
            bsStyle="primary"
            bsSize="small"
            style={{ marginRight: '5px' }}
            onClick={e => {
              e.preventDefault();
              this.setState({
                showAlertReportPayables: true,
              });
            }}
          >
            <Icon name="server" />
            {' '}
            Report All Payables
          </Button>
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

  setCarrierModal = (selectedCarrier = false) => {
    if (selectedCarrier) {
      this.setState({ showEditModal: true, selectedCarrier });
    } else {
      this.setState({
        showEditModal: false,
        selectedCarrier: { name: '', term: '' },
      });
    }
  };

  reportPayables = () => {
    this.setState({
      isSubmitting: true,
    });
    this.props.reportPayables(data => {
      this.setState({
        showAlertReportPayables: false,
        isSubmitting: false,
      });
      this.props.addNotification({
        title: 'Payables reported',
        message: `All payables have been reported.`,
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
  };
}

const mapStateToProps = state => ({
  datatable: getPageItems(state, 'carrier'),
});

const mapDispatchToProps = {
  loadCarriers,
  loadCarrierById,
  updateCarrier,
  reportPayables,
  addNotification,
};

export default connect(mapStateToProps, mapDispatchToProps)(Carriers);
