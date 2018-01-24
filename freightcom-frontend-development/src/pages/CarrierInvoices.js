import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Row, Col, Button, Modal } from 'react-bootstrap';
import formatDate from 'date-fns/format';

import Widget from '../components/Widget';
import DataTable from '../components/DataTable';
import CarrierAutoSuggest from '../components/carriers/CarrierAutoSuggest';

import { loadCarrierInvoices, uploadEDIFile } from '../actions/carriers';
import { getPageItems } from '../reducers';

class CarrierInvoices extends Component {
  constructor(props) {
    super(props);

    this.state = {
      ediUploadModal: false,
    };

    this.datatable = null;

    this.columns = [
      {
        label: 'ID',
        field: 'id',
      },
      {
        label: 'Date created',
        cell: edi => formatDate(edi.createdAt, 'YYYY-MM-DD'),
      },
      {
        label: 'Carrier',
        filter: 'carrier.name',
        cell: edi => {
          return edi.carrier.name;
        },
      },
      {
        label: 'Processed',
        cell: edi => (edi.processed ? 'Yes' : 'No'),
      },
      {
        label: false,
        cell: edi => {
          return (
            <a
              className="btn btn-primary btn-xs"
              href={`/api/carrier/invoice/${edi.carrier.id}/${edi.id}`}
              download="carrier-edi"
            >
              Download
            </a>
          );
        },
      },
    ];
  }
  componentWillMount() {
    this.props.loadCarrierInvoices();
  }
  render() {
    return (
      <Widget title="Manage Carrier Invoices">
        <Button
          bsStyle="primary"
          className="pull-right"
          onClick={() => {
            this.setState({ ediUploadModal: true });
          }}
        >
          Upload Invoice
        </Button>
        <br /> <br />
        <DataTable
          columns={this.columns}
          loadData={this.props.loadCarrierInvoices}
          {...this.props.datatable}
          renderTableFilters={false}
          ref={datatable => (this.datatable = datatable)}
        />
        <Modal
          show={this.state.ediUploadModal}
          onHide={() => {
            this.setState({ ediUploadModal: false });
          }}
        >
          <Modal.Header>
            <Modal.Title>Carrier Invoice Upload</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Row>
              <Col className="col" sm={6}>
                <label>Carrier:</label>
                <CarrierAutoSuggest
                  onChange={carrier =>
                    this.setState({ carrierId: carrier ? carrier.id : '' })}
                />
              </Col>
              <Col className="col" sm={6}>
                <label>Carrier Invoice:</label>
                <input type="file" ref="fileInput" className="form-control" />
              </Col>
            </Row>
          </Modal.Body>
          <Modal.Footer>
            <div>
              <Button
                bsStyle="default"
                onClick={() => this.setState({ ediUploadModal: false })}
              >
                Cancel
              </Button>
              <Button
                bsStyle="primary"
                disabled={this.state.uploadDisabled}
                onClick={e => {
                  this.props.uploadEDIFile({
                    carrierId: this.state.carrierId,
                    fileInput: this.refs.fileInput,
                    callback: () => {
                      this.datatable.loadData();
                    },
                  });
                  this.setState({ ediUploadModal: false });
                }}
              >
                Upload
              </Button>
            </div>
          </Modal.Footer>
        </Modal>
      </Widget>
    );
  }
}

const mapStateToProps = state => {
  return {
    datatable: getPageItems(state, 'carrierInvoice'),
  };
};

const mapDispatchToProps = {
  loadCarrierInvoices,
  uploadEDIFile,
};

export default connect(mapStateToProps, mapDispatchToProps)(CarrierInvoices);
