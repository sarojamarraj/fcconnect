import React, { Component } from 'react';
import { Modal, Button } from 'react-bootstrap';

class UpdateOrderStatusModal extends Component {
  constructor(props) {
    super(props);
    this.form = {};
  }

  render() {
    return (
      <Modal show={this.props.showModal} onHide={this.props.toggleModal}>
        <Modal.Header closeButton>
          <Modal.Title>
            {this.props.modalTitle}
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div className="form-group">
            <label>Comment</label>
            <textarea
              className="form-control"
              rows="5"
              name="comment"
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
          >
            {this.props.modalActionLabel}
          </Button>
        </Modal.Footer>
      </Modal>
    );
  }

  handleInputChange = e => {
    this.form = {
      ...this.form,
      [e.target.name]: e.target.value,
    };
  };

  handleFormSubmit = e => {
    e.preventDefault();
    this.props.modalAction(this.form);
    this.props.toggleModal();
  };
}

export default UpdateOrderStatusModal;
