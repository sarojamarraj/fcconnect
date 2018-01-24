import React, { Component } from 'react';
import { Modal, Button } from 'react-bootstrap';

import { formatCurrency } from '../../utils';

class PaymentHistoryModal extends Component {
  render() {
    return (
      <Modal show={this.props.showModal} onHide={this.props.toggleModal}>
        <Modal.Header closeButton>
          <Modal.Title>Payment History</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <table className="table table-bordered">
            <thead>
              <tr>
                <th>Date</th>
                <th>Transaction #</th>
                <th>Payment Type</th>
                <th>Amount</th>
              </tr>
            </thead>
            <tbody>
              {this.props.paymentHistory.map(item => {
                return (
                  <tr key={item.id}>
                    <td>
                      {item.paymentDate}
                    </td>
                    <td>
                      {item.id}
                    </td>
                    <td>
                      {item.paymentType} - {item.description}
                    </td>
                    <td className="currency">
                      {formatCurrency(item.paymentAmount)}
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </Modal.Body>
        <Modal.Footer>
          <Button
            bsStyle="default"
            onClick={this.props.toggleModal}
          >
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    );
  }
}

export default PaymentHistoryModal;
