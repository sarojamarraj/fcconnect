import React, { Component } from 'react';
import { Modal, Button } from 'react-bootstrap';
import CustomsInvoice from './CustomsInvoice';

class CustomsInvoiceModal extends Component {
  render() {
    return (
      <Modal show={this.props.showModal} onHide={this.props.toggleModal}>
        <Modal.Header closeButton>
          <Modal.Title>Customs Invoice</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <CustomsInvoice />
        </Modal.Body>
        <Modal.Footer>
          <Button
            bsStyle="default"
            bsSize="small"
            onClick={this.props.toggleModal}
          >
            Close
          </Button>
          <Button
            bsStyle="primary"
            onClick={e => {
              console.log('Print...');
            }}
          >
            Print
          </Button>
        </Modal.Footer>
      </Modal>
    );
  }
}

export default CustomsInvoiceModal;
