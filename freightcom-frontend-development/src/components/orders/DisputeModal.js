import React, { Component } from 'react';
import { Modal, Button } from 'react-bootstrap';
import { formatCurrency } from '../../utils';
import orderBy from 'lodash/orderBy';
import find from 'lodash/find';

class DisputeModal extends Component {
  constructor(props) {
    super(props);
    this.state = {
      comment: '',
      chargeId: 0,
    };
  }
  render() {
    return (
      <Modal show={this.props.showModal} onHide={this.props.toggleModal}>
        <Modal.Header closeButton>
          <Modal.Title>File a dispute</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div className="form-group">
            <label>Charge</label>
            <select
              className="form-control"
              name="chargeId"
              value={this.state.chargeId}
              onChange={this.handleInputChange}
            >
              <option value="0">- Select a charge -</option>
              {orderBy(this.props.charges, ['description'], [
                'asc',
              ]).map(charge => (
                <option key={charge.id} value={charge.id}>
                  {charge.description} - {formatCurrency(charge.subTotal)}
                </option>
              ))}
            </select>
          </div>
          <div className="form-group">
            <label>Comment</label>
            <textarea
              className="form-control"
              rows="5"
              name="comment"
              value={this.state.comment}
              onChange={this.handleInputChange}
            />
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button
            bsStyle="default"
            onClick={this.props.toggleModal}
          >
            Cancel
          </Button>
          <Button
            bsStyle="primary"
            onClick={this.handleFormSubmit}
            disabled={!!!this.state.chargeId}
          >
            File Dispute
          </Button>
        </Modal.Footer>
      </Modal>
    );
  }

  handleInputChange = e => {
    this.setState({ [e.target.name]: e.target.value });
  };

  handleFormSubmit = () => {
    const charge = find(this.props.charges, charge => {
      return String(charge.id) === String(this.state.chargeId);
    });
    this.props.onSubmit({
      chargeId: this.state.chargeId,
      comment: this.state.comment + ' [' + charge.description + ' - ' + charge.total + ']',
    });
  };
}

export default DisputeModal;
