import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Button, Row, Col } from 'react-bootstrap';

import { updateClaim } from '../../actions/orders';

import FormGroup from '../forms/FormGroup';

class ClaimsUpdateForm extends Component {
  constructor(props) {
    super(props);

    const { claim = {} } = props;

    this.state = {
      status: claim.status,
      comment: '',
      amount: claim.amount,
      resolving: false,
      creditAmount: '',
    };
  }

  componentWillReceiveProps(nextProps) {
    const { claim } = nextProps;

    if (this.props.claim !== claim) {
      this.state = {
        status: claim.status,
        comment: '',
        amount: claim.amount,
        creditAmount: '',
      };
    }
  }

  handleInputChange = e => {
    this.setState({ [e.target.name]: e.target.value });
  };

  handleStatusChange = e => {
    this.setState({
      status: e.target.value,
      resolving: e.target.value === 'CLAIM_SETTLED',
    });
  };

  render() {
    const { status, comment, amount, resolving, creditAmount } = this.state;

    return (
      <Col className="col claims-section " sm={12} xs={12}>
        <div
          className="jarviswidget jarviswidget-sortable jarviswidget-color-red"
          data-widget-edit-button="false"
        >
          <header role="heading">
            <h2>
              Pending Order Claim
            </h2>
          </header>
          <div>
            <div className="widget-body">
              <Row>
                <Col className="col" sm={2}>
                  <FormGroup label="Status">
                    <select
                      className="form-control"
                      name="status"
                      defaultValue={status}
                      onChange={this.handleStatusChange}
                    >
                      <option value="NEW_CLAIM">NEW</option>
                      <option value="CLAIM_REVIEWED">REVIEWED</option>
                      <option value="CLAIM_IN_PROCESS">IN-PROCESS</option>
                      <option disabled>──────────</option>
                      <option value="CLAIM_DENIED">DENIED</option>
                      <option value="CLAIM_SETTLED">SETTLED</option>
                    </select>
                  </FormGroup>
                </Col>
                <Col className="col" sm={5}>
                  <FormGroup
                    label="Comment"
                    type="text"
                    name="comment"
                    value={comment}
                    onChange={this.handleInputChange}
                  />
                </Col>
                <Col className="col" sm={1}>
                  <FormGroup
                    label="Amount"
                    type="text"
                    name="amount"
                    value={amount}
                    disabled={true}
                  />
                </Col>
                {resolving &&
                  <Col className="col" sm={2}>
                    <FormGroup
                      label="Create credit"
                      type="text"
                      name="creditAmount"
                      value={creditAmount}
                      onChange={this.handleInputChange}
                    />
                  </Col>}
                <Col className="col" sm={2} style={{ paddingTop: '25px' }}>
                  <Button
                    bsStyle="primary"
                    bsSize="small"
                    onClick={this.updateHandler}
                  >
                    Update Claim Status
                  </Button>
                </Col>
              </Row>
            </div>
          </div>
        </div>
      </Col>
    );
  }

  updateHandler = e => {
    const { updateClaim, orderId, callback } = this.props;

    updateClaim(orderId, { ...this.state }, callback).then(() =>
      this.setState({ comment: '', resolving: false }),
    );
  };
}

export default connect(null, { updateClaim })(ClaimsUpdateForm);
