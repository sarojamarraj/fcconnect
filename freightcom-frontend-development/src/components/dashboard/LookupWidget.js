import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Row, Col, Button, Modal } from 'react-bootstrap';
import Icon from '../Icon';

import { browserHistory } from 'react-router';

import { addTab } from '../../actions/tabs';

class LookupWidget extends Component {
  constructor(props) {
    super(props);

    this.state = {
      lookupResultsModal: false,
      lookupCriteria: 'orderId',
      lookupKeyword: '',
      resultsData: [],
      lookupStatus: '',
    };
  }

  render() {
    return (
      <div id="dashboard-lookup-widget" className="jarviswidget">
        <header role="heading">
          <span className="widget-icon">
            <Icon name="search" />
          </span>
          <h2>Quick Lookup Widget</h2>
        </header>
        <div>
          <div className="widget-body">
            <Row>
              <Col sm={12}>
                <div className="form-group">
                  <label>Search by:</label>
                  <select
                    className="form-control"
                    value={this.state.lookupCriteria}
                    onChange={e =>
                      this.setState({ lookupCriteria: e.target.value })}
                  >
                    <option value="orderId">Shipment ID</option>
                    <option value="bolId">Bill of Lading</option>
                    <option value="trackingNumber">Tracking Number</option>
                    <option value="referenceCode">Reference Code</option>
                    <option value="invoiceId">Invoice Number</option>
                    <option value="ccTransaction">CC Transaction #</option>
                  </select>
                </div>

                <div className="form-group">
                  <input
                    type="text"
                    className="form-control"
                    value={this.state.lookupKeyword}
                    placeholder="Enter a value"
                    onChange={e =>
                      this.setState({ lookupKeyword: e.target.value })}
                  />
                </div>

                <Button
                  bsStyle="primary"
                  onClick={this.fetchResults}
                  disabled={!this.state.lookupKeyword}
                >
                  Search
                </Button>
                {this.state.lookupStatus &&
                  <span
                    style={{
                      margin: '0 0 0 8px',
                      color: '#aaa',
                      fontStyle: 'italic',
                    }}
                  >
                    {this.state.lookupStatus}
                  </span>}
              </Col>
            </Row>

            <Modal
              show={this.state.lookupResultsModal}
              onHide={this.toggleLookupResultsModal}
              backdrop="static"
            >
              <Modal.Header closeButton>
                <Modal.Title>Lookup Search Results</Modal.Title>
              </Modal.Header>
              <Modal.Body>
                <table className="table table-bordered">
                  <tbody>
                    {this.state.resultsData.map((item, idx) =>
                      <tr key={item.id + '-' + idx}>
                        <td>
                          {item.eshipperOid ? 'Shipment' : 'Invoice'}
                        </td>
                        <td>
                          {item.id}
                        </td>

                        <td>
                          <Button
                            bsStyle="primary"
                            bsSize="xs"
                            onClick={e => {
                              e.preventDefault();
                              e.target.blur();
                              if (!item.eshipperOid) {
                                browserHistory.push('/invoices/' + item.id);
                              } else {
                                this.props.dispatch(
                                  addTab({
                                    orderId: item.id,
                                    type: 'ORDER_VIEW',
                                  }),
                                );
                                browserHistory.push('/shipments');
                              }
                            }}
                          >
                            View
                          </Button>
                        </td>
                      </tr>,
                    )}
                  </tbody>
                </table>
              </Modal.Body>
              <Modal.Footer>
                <Button
                  bsStyle="default"
                  onClick={this.toggleLookupResultsModal}
                >
                  Close
                </Button>
              </Modal.Footer>
            </Modal>
          </div>
        </div>
      </div>
    );
  }

  toggleLookupResultsModal = e => {
    if (e && e.target) {
      e.target.blur();
      e.preventDefault();
    }

    let showModal = !this.state.lookupResultsModal;
    if (typeof e === 'boolean') showModal = e;
    this.setState({ lookupResultsModal: showModal });
  };

  fetchResults = e => {
    this.setState({ lookupStatus: 'Fetching...' });

    const defaultQuery = '&page=0&size=6&sort=id,DESC';

    function buildOrderQuery() {
      const mappings = {
        orderId: 'id',
      };
      let lookupCriteria =
        mappings[this.state.lookupCriteria] || this.state.lookupCriteria;
      return (
        '/api/submitted-orders?' +
        lookupCriteria +
        '=' +
        this.state.lookupKeyword +
        defaultQuery
      );
    }

    function buildInvoiceQuery() {
      const mappings = {
        invoiceId: 'id',
      };
      let lookupCriteria =
        mappings[this.state.lookupCriteria] || this.state.lookupCriteria;
      return (
        '/api/invoice?' +
        lookupCriteria +
        '=' +
        this.state.lookupKeyword +
        defaultQuery
      );
    }

    const fetchOptions = { credentials: 'include', method: 'GET' };

    (async () => {
      try {
        const orders = await fetch(buildOrderQuery.call(this), fetchOptions)
          .then(res => res.json())
          .then(json => {
            return json._embedded ? json._embedded.customerOrders : [];
          });
        const invoices = await fetch(buildInvoiceQuery.call(this), fetchOptions)
          .then(res => res.json())
          .then(json => {
            return json._embedded ? json._embedded.invoice : [];
          });

        const data = [...orders, ...invoices];
        if (data.length) {
          this.setState({ resultsData: data, lookupStatus: '' });
          this.toggleLookupResultsModal(true);
        } else {
          this.setState({ lookupStatus: 'No results found...' });
        }
      } catch (e) {
        this.setState({ lookupStatus: 'No results found...' });
        console.error(e);
      }
    })();
  };
}

export default connect()(LookupWidget);
