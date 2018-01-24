import React, { Component } from 'react';
import { Modal, Button } from 'react-bootstrap';
import { formatCurrency } from '../../utils';

class DisputeReplyModal extends Component {
  constructor(props) {
    super(props);
    this.state = {
      comment: '',
      resolveDispute: 0,
    };
  }

  render() {
    return (
      <Modal show={this.props.showModal} onHide={this.props.toggleModal}>
        <Modal.Header closeButton>
          <Modal.Title>Reply to Disputes</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div className="form-group hidden">
            <label>Charge</label>
            <select className="form-control">
              {this.props.charges.map(charge => (
                <option key={charge.id}>
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
          <div className="checkbox">
            <label>
              <input
                type="checkbox"
                name="resolveDispute"
                value="1"
                checked={!!this.state.resolveDispute}
                onChange={e =>
                  this.setState({ resolveDispute: !!e.target.checked ? 1 : 0 })}
              />
              {' '}
              Clear All Disputes
            </label>
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button bsStyle="default" onClick={this.props.toggleModal}>
            Cancel
          </Button>
          <Button bsStyle="primary" onClick={this.handleFormSubmit}>
            Submit
          </Button>
        </Modal.Footer>
      </Modal>
    );
  }

  handleInputChange = e => {
    this.setState({ [e.target.name]: e.target.value });
  };

  handleFormSubmit = () => {
    this.props.onSubmit({
      comment: this.state.comment,
      resolveDispute: this.state.resolveDispute,
    });
  };
}

export default DisputeReplyModal;
