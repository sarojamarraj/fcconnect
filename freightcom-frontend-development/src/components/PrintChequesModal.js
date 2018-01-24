import React, { Component } from 'react';
import { Modal, Button } from 'react-bootstrap';

class PrintChequesModal extends Component {
  constructor(props) {
    super(props);
    this.state = {
      chequeNumber: '',
    };
  }

  validChequeNumber = () => {
    const chequeNumber =
      String(this.state.chequeNumber).match(/^\s*\d+\s*$/) &&
      Number.parseInt(this.state.chequeNumber, 10);

    return Number.isInteger(chequeNumber) && chequeNumber > 0;
  };

  submit = e => {
    e.preventDefault();

    if (this.validChequeNumber()) {
      this.props.submit(Number.parseInt(this.state.chequeNumber, 10));
    }
  };

  render() {
    const { title, cancel } = this.props;

    return (
      <Modal show={true} onHide={this.props.toggleModal}>
        <form onSubmit={this.submit}>
          <Modal.Header closeButton>
            <Modal.Title>Print {title} Cheques</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <label style={{ width: '50%' }}>
              First Cheque Number<br />
              <input
                autoFocus
                type="text"
                className="col-sm-12 col-xs-12"
                value={this.state.chequeNumber}
                onChange={e => this.setState({ chequeNumber: e.target.value })}
              />
            </label>
          </Modal.Body>
          <Modal.Footer>
            <Button bsStyle="default" onClick={cancel}>
              Cancel
            </Button>
            <Button
              bsStyle="primary"
              type="submit"
              disabled={!this.validChequeNumber()}
            >
              Print Cheques Now
            </Button>
          </Modal.Footer>
        </form>
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

export default PrintChequesModal;
