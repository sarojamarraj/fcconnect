import React, { Component } from 'react';
import { Modal } from 'react-bootstrap';
import { connect } from 'react-redux';
import Button from 'react-bootstrap-button-loader';
import CurrencySelector from '../CurrencySelector';
import FormGroup from '../forms/FormGroup';
import CreditCardSelector from '../CreditCardSelector';

class CustomerRefundCreditModal extends Component {
  constructor(props) {
    super(props);
    this.state = {
      creditToRefund: { ccInfo: {}, amount: 100, note: 'Manually added ...' },
      ccInfo: {},
    };
  }

  render() {
    const { show, submit, customer = {} } = this.props;
    const { creditToRefund: { amount, note, currency } } = this.state;

    return (
      <Modal show={show}>
        <Modal.Header>
          <Modal.Title>Refund Credit</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div className="col-sm-12 col-xs-12">
            <form className="modalForm form-horizontal">
              <FormGroup label="Amount">
                <input
                  className="form-control"
                  type="text"
                  name="amount"
                  placeholder="Amount"
                  value={amount}
                  onChange={e => {
                    this.setState({
                      creditToRefund: {
                        ...this.state.creditToRefund,
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
                      creditToRefund: {
                        ...this.state.creditToRefund,
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
                      creditToRefund: {
                        ...this.state.creditToRefund,
                        currency: value,
                      },
                    })}
                />
              </FormGroup>
              <div>
                <CreditCardSelector
                  customer={customer}
                  onChange={ccInfo =>
                    this.setState({
                      creditToRefund: {
                        ...this.state.creditToRefund,
                        ccInfo,
                      },
                    })}
                />
              </div>

            </form>
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button onClick={this.props.cancel}>
            Cancel
          </Button>
          <Button
            onClick={e => submit(this.state.creditToRefund)}
            bsStyle="primary"
          >
            Refund Credit
          </Button>
        </Modal.Footer>
      </Modal>
    );
  }
}

export default connect(null, null)(CustomerRefundCreditModal);
