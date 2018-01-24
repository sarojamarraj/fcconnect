import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Row, Col, Button, ButtonToolbar } from 'react-bootstrap';
import DataTable from '../../components/DataTable';

import formatDate from 'date-fns/format';
import { getPageItems } from '../../reducers';
import {
  loadCurrencies,
  saveCurrency,
  deleteCurrency,
} from '../../actions/currencies';

class Currencies extends Component {
  constructor(props) {
    super(props);

    this.datatable = null;
    this.currencyToAdd = {};
    this.state = {
      itemsToEdit: [],
    };
    this.editRowData = {};
  }

  componentWillMount() {
    this.props.loadCurrencies();
  }

  /**
   * TODO: Need to make this more readable...
   *       Move those onclick handlers to its own functions
   */
  render() {
    return (
      <div>
        <form className="smart-form">
          <Row>
            <Col className="col" xs={4}>
              <section>
                <label className="label">Currency</label>
                <label className="input">
                  <input
                    type="text"
                    name="to"
                    onChange={e =>
                      (this.currencyToAdd[e.target.name] = e.target.value)}
                  />
                </label>
              </section>
            </Col>
            <Col className="col" xs={4}>
              <section>
                <label className="label">Rate</label>
                <label className="input">
                  <input
                    type="text"
                    name="rate"
                    onChange={e =>
                      (this.currencyToAdd[e.target.name] = e.target.value)}
                  />
                </label>
              </section>
            </Col>
          </Row>
          <Row>
            <Col className="col" xs={4}>
              <Button
                bsStyle="primary"
                bsSize="small"
                onClick={e => {
                  e.target.blur();
                  e.preventDefault();
                  this.props.saveCurrency(this.currencyToAdd, () => {
                    this.datatable.loadData();
                  });
                }}>
                Add Currency
              </Button>
            </Col>
          </Row>
        </form>
        <hr />
        <DataTable
          columns={[
            {
              label: 'Updated At',
              width: '100px',
              cell: row => formatDate(row.updatedAt, 'YYYY-MM-DD'),
            },
            {
              label: 'Currency',
              field: 'to',
              sorting: false,
              cell: row => {
                if (~this.state.itemsToEdit.indexOf(row.id)) {
                  return (
                    <input
                      type="text"
                      name="to"
                      defaultValue={row.to}
                      onChange={e => {
                        this.editRowData[row.id][e.target.name] =
                          e.target.value;
                      }}
                      className="form-control"
                    />
                  );
                } else {
                  return row.to;
                }
              },
            },
            {
              label: 'Rate',
              field: 'rate',
              sorting: false,
              cell: row => {
                if (~this.state.itemsToEdit.indexOf(row.id)) {
                  return (
                    <input
                      type="text"
                      name="rate"
                      defaultValue={row.rate}
                      onChange={e => {
                        this.editRowData[row.id][e.target.name] =
                          e.target.value;
                      }}
                      className="form-control"
                    />
                  );
                } else {
                  return row.rate;
                }
              },
            },
            {
              cell: row => {
                if (~this.state.itemsToEdit.indexOf(row.id)) {
                  return (
                    <ButtonToolbar>
                      <Button
                        bsStyle="primary"
                        bsSize="small"
                        onClick={e => {
                          e.target.blur();
                          e.preventDefault();
                          this.props.saveCurrency(
                            this.editRowData[row.id],
                            () => {
                              this.setState({
                                itemsToEdit: this.state.itemsToEdit.filter(
                                  item => item !== row.id,
                                ),
                              });
                              this.datatable.loadData();
                            },
                          );
                        }}>
                        Save
                      </Button>
                      <Button
                        bsStyle="default"
                        bsSize="small"
                        onClick={e => {
                          e.target.blur();
                          e.preventDefault();
                          delete this.editRowData[row.id];
                          this.setState({
                            itemsToEdit: this.state.itemsToEdit.filter(
                              item => item !== row.id,
                            ),
                          });
                        }}>
                        Cancel
                      </Button>
                      <Button
                        bsStyle="danger"
                        bsSize="small"
                        onClick={e => {
                          e.target.blur();
                          e.preventDefault();
                          delete this.editRowData[row.id];
                          this.props.deleteCurrency(row.id, () => {
                            this.setState({
                              itemsToEdit: this.state.itemsToEdit.filter(
                                item => item !== row.id,
                              ),
                            });
                            this.datatable.loadData();
                          });
                        }}>
                        Delete
                      </Button>
                    </ButtonToolbar>
                  );
                } else {
                  return (
                    <Button
                      bsStyle="primary"
                      bsSize="small"
                      onClick={e => {
                        e.target.blur();
                        e.preventDefault();
                        this.editRowData[row.id] = {
                          ...row,
                        };
                        this.setState({
                          itemsToEdit: [...this.state.itemsToEdit, row.id],
                        });
                      }}>
                      Edit
                    </Button>
                  );
                }
              },
            },
          ]}
          loadData={this.props.loadCurrencies}
          renderTableFilters={false}
          renderPagination={false}
          ref={dt => (this.datatable = dt)}
          {...this.props.datatable}
        />
      </div>
    );
  }

  addCurrency = () => {};
}

const mapStateToProps = state => {
  return {
    datatable: getPageItems(state, 'currency'),
  };
};

const mapDispatchToProps = {
  loadCurrencies,
  saveCurrency,
  deleteCurrency,
};

export default connect(mapStateToProps, mapDispatchToProps)(Currencies);
