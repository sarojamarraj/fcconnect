import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Button, Modal, Row, Col } from 'react-bootstrap';
import { formatCurrency } from '../../utils';

import { addCreditFromCard } from '../../actions/credits';

class PayShipmentModal extends Component {
  constructor(props) {
    super(props);

    this.ccInfo = {};
    this.state = {
      isNewCreditCard: true,
    };
  }
  render() {
    const {
      orderData: {
        creditAvailable: credits = 0,
        orderTotal: shipmentCharges = 0,
        amountPayable: totalPayable = 0,
      } = {},
    } = this.props;

    return (
      <Modal show={this.props.show} onHide={this.props.hideModal}>
        <Modal.Header>
          <Modal.Title>Enter Payments</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <table className="table table-bordered table-condensed">
            <tbody>
              <tr>
                <td>Charges for this Shipment</td>
                <td className="currency">{formatCurrency(shipmentCharges)}</td>
              </tr>
              <tr>
                <td>Less: Credits on record</td>
                <td className="currency">({formatCurrency(credits)})</td>
              </tr>
              <tr>
                <th>Total Payable Now</th>
                <th className="currency">{formatCurrency(totalPayable)}</th>
              </tr>
            </tbody>
          </table>
          {totalPayable > 0 &&
            <div className="form">
              <Row>
                <Col className="col" sm={6}>
                  <section className="radio">
                    <label>
                      <input
                        type="radio"
                        checked={this.state.isNewCreditCard}
                        name="paymentMethod"
                        value="new-credit-card"
                        onChange={e => this.setState({ isNewCreditCard: true })}
                      />
                      <span>Use new credit card</span>
                    </label>
                  </section>
                </Col>
                {this.props.order.customer.creditCards.length > 0 &&
                  <Col className="col" sm={6}>
                    <section className="radio">
                      <label>
                        <input
                          type="radio"
                          checked={!this.state.isNewCreditCard}
                          name="paymentMethod"
                          value="existing-credit-card"
                          onChange={e =>
                            this.setState({ isNewCreditCard: false })}
                        />
                        <span>Select a credit card from the vault</span>
                      </label>
                    </section>
                  </Col>}
              </Row>
              {this.state.isNewCreditCard
                ? <div>
                    <Row>
                      <Col className="col" sm={6}>
                        <section className="form-group">
                          <label>Card Type</label>
                          <select
                            name="ccType"
                            onChange={this.updateCCInfo}
                            className="form-control"
                          >
                            <option />
                            <option value="visa">Visa</option>
                            <option value="visa">Master Card</option>
                          </select>
                        </section>
                      </Col>
                      <Col className="col" sm={6}>
                        <section className="form-group">
                          <label>Name on card</label>
                          <input
                            type="text"
                            name="nameOnCard"
                            onChange={this.updateCCInfo}
                            className="form-control"
                          />
                        </section>
                      </Col>
                    </Row>
                    <br />
                    <Row>
                      <Col className="col" sm={6}>
                        <section className="form-group">
                          <label>Card Number</label>
                          <input
                            type="text"
                            name="creditCardNumber"
                            onChange={this.updateCCInfo}
                            className="form-control"
                          />
                        </section>
                      </Col>
                      <Col className="col" sm={2}>
                        <section className="form-group">
                          <label>Expiration</label>
                          <select
                            name="expiryMonth"
                            onChange={this.updateCCInfo}
                            className="form-control"
                          >
                            <option />
                            <option value="01">01</option>
                            <option value="02">02</option>
                            <option value="03">03</option>
                            <option value="04">04</option>
                            <option value="05">05</option>
                            <option value="06">06</option>
                            <option value="07">07</option>
                            <option value="08">08</option>
                            <option value="09">09</option>
                            <option value="10">10</option>
                            <option value="11">11</option>
                            <option value="12">12</option>
                          </select>
                        </section>
                      </Col>
                      <Col className="col" sm={2}>
                        <section className="form-group">
                          <label>
                            <span style={{ color: 'white' }}>Year</span>
                          </label>
                          <select
                            name="expiryYear"
                            onChange={this.updateCCInfo}
                            className="form-control"
                          >
                            <option />
                            <option value="17">2017</option>
                            <option value="18">2018</option>
                            <option value="19">2019</option>
                            <option value="20">2020</option>
                            <option value="21">2021</option>
                            <option value="22">2022</option>
                            <option value="23">2023</option>
                            <option value="24">2024</option>
                            <option value="25">2025</option>
                            <option value="26">2026</option>
                          </select>
                        </section>
                      </Col>

                      <Col className="col" sm={2}>
                        <section className="form-group">
                          <label>CVS</label>
                          <input
                            type="text"
                            name="cvs"
                            onChange={this.updateCCInfo}
                            className="form-control"
                          />
                        </section>
                      </Col>
                    </Row>
                  </div>
                : <div>
                    <ul style={{ listStyleType: 'none' }}>
                      {this.props.order.customer.creditCards &&
                        this.props.order.customer.creditCards.map((cc, idx) => {
                          return (
                            <li key={idx} style={{ listStyle: 'none' }}>
                              <div className="radio">
                                <label>
                                  <input
                                    type="radio"
                                    name="select-cc"
                                    value={'cc-' + idx}
                                    onChange={e => this.selectCCValues(cc)}
                                  />
                                  {cc.number}
                                </label>
                              </div>
                            </li>
                          );
                        })}
                    </ul>
                  </div>}
            </div>}
        </Modal.Body>
        <Modal.Footer>
          <Button bsStyle="default" onClick={this.props.hideModal}>
            Cancel
          </Button>
          <Button
            bsStyle="primary"
            onClick={
              totalPayable > 0 ? this.processPayment : this.props.submitForm
            }
          >
            {totalPayable > 0 ? 'Pay' : 'Continue'}
          </Button>
        </Modal.Footer>
      </Modal>
    );
  }

  selectCCValues = cc => {
    this.ccInfo = {
      creditCardId: cc.id,
      ccType: cc.type,
      creditCardNumber: cc.number,
      cvs: cc.cvs,
      expiryMonth: cc.expiryMonth,
      expiryYear: cc.expiryYear,
      nameOnCard: cc.name,
      payment: (this.ccInfo ? this.ccInfo.payment : 0) || 0,
    };
  };

  updateCCInfo = e => {
    this.ccInfo = {
      ...this.ccInfo,
      [e.target.name]: e.target.value,
    };
  };

  validateCCFields = () => {
    const keysToCheck = [
      'ccType',
      'creditCardNumber',
      'cvs',
      'expiryMonth',
      'expiryYear',
      'nameOnCard',
    ];
    //eslint-disable-next-line
    for (let key in keysToCheck) {
      return !!this.ccInfo[key];
    }
    return true;
  };

  processPayment = e => {
    e.preventDefault();
    this.props.addCreditFromCard(this.ccInfo, () => {
      this.props.submitForm();
    });
  };
}

export default connect(null, { addCreditFromCard })(PayShipmentModal);
