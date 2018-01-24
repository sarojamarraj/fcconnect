import React, { Component } from 'react';
import { Modal } from 'react-bootstrap';
import { connect } from 'react-redux';
import Button from 'react-bootstrap-button-loader';
import CurrencySelector from '../CurrencySelector';
import FormGroup from '../forms/FormGroup';

class CustomerAddCreditModal extends Component {
  constructor(props) {
    super(props);
    this.state = { creditToAdd: { amount: 100, note: 'Manually added ...' } };
  }

  render() {
    const { show, submit } = this.props;
    const { creditToAdd: { amount, note, currency } } = this.state;

    return (
      <Modal show={show}>
        <Modal.Header>
          <Modal.Title>Add Credit</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <form className="form">
            <FormGroup label="Amount">
              <input
                className="form-control"
                type="text"
                name="amount"
                placeholder="Amount"
                value={amount}
                onChange={e => {
                  this.setState({
                    creditToAdd: {
                      ...this.state.creditToAdd,
                      amount: e.target.value,
                    },
                  });
                }}
              />
            </FormGroup>
            <FormGroup label="Note">
              <input
                className="form-control"
                type="text"
                name="note"
                placeholder="Note"
                value={note}
                onChange={e =>
                  this.setState({
                    creditToAdd: {
                      ...this.state.creditToAdd,
                      note: e.target.value,
                    },
                  })}
              />
            </FormGroup>
            <FormGroup label="Currency">
              <CurrencySelector
                value={currency}
                onChange={value =>
                  this.setState({
                    creditToAdd: {
                      ...this.state.creditToAdd,
                      currency: value,
                    },
                  })}
              />
            </FormGroup>

          </form>
        </Modal.Body>
        <Modal.Footer>
          <Button onClick={this.props.cancel}>
            Cancel
          </Button>
          <Button
            onClick={e => submit(this.state.creditToAdd)}
            bsStyle="primary"
          >
            Add Credit
          </Button>
        </Modal.Footer>
      </Modal>
    );
  }
}

export default connect(null, null)(CustomerAddCreditModal);
