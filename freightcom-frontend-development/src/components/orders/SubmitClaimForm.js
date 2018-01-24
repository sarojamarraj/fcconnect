import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Button } from 'react-bootstrap';

import { submitClaim, loadOrderCustomers } from '../../actions/orders';
import { loadCurrencyList } from '../../actions/currencies';
import { orderUserOptions, selectCurrencies } from '../../reducers';
import { Col, Row } from 'react-bootstrap';
import Select from 'react-select';

import FormGroup from '../forms/FormGroup';
import strings from '../../strings';

class SubmitClaimForm extends Component {
  constructor(props) {
    super(props);
    this.state = {
      userRoleId: null,
      orderId: props.orderId,
      amount: null,
      comment: null,
    };
  }

  componentWillMount() {
    const {
      showModal,
      isFreightcom,
      orderId,
      loadOrderCustomers,
      loadCurrencyList,
    } = this.props;

    if (showModal && isFreightcom) {
      loadOrderCustomers(orderId);
      loadCurrencyList();
    }
  }

  componentWillReceiveProps(nextProps) {
    const { orderId, showModal, isFreightcom, userOptions } = nextProps;

    if (showModal && !this.props.showModal) {
      // Reopened
      this.setState(
        {
          userRoleId: null,
          orderId: orderId,
          amount: '',
          comment: '',
        },
        () => {
          if (isFreightcom) {
            this.props.loadOrderCustomers(this.state.orderId);
          }
        },
      );
    } else if (isFreightcom && this.props.userOptions !== userOptions) {
      // Load done, new user options
      if (userOptions.length === 1) {
        this.setState({
          userRoleId: userOptions[0].id,
        });
      }
    }
  }

  render() {
    return (
      <Col sm={12} xs={12} className="col">
        <div
          className="jarviswidget jarviswidget-sortable jarviswidget-color-red"
          data-widget-edit-button="false"
        >
          <header role="heading" className="danger">
            <h2>Submit Claim</h2>
          </header>

          <form>
            <FormGroup label={strings.claim.submitComment}>
              <textarea
                className="form-control"
                defaultValue={this.state.comment}
                onChange={e => this.setState({ comment: e.target.value })}
              />
            </FormGroup>
            <FormGroup>
              <Row>
                <Col sm={4} xs={4} className="col">
                  <label style={{ width: '50%' }}>
                    Currency
                    <Select
                      value={this.state.currency}
                      multi={false}
                      onChange={value => {
                        this.setState({ currency: value && value.name });
                      }}
                      options={this.props.currencies}
                      labelKey="name"
                      valueKey="name"
                    />

                  </label>
                </Col>
                <Col sm={4} xs={4} className="col">
                  <label style={{ width: '90%' }}>
                    Claimed Amount
                    <input
                      type="text"
                      className="form-control"
                      defaultValue={this.state.amount}
                      onChange={e => this.setState({ amount: e.target.value })}
                    />
                  </label>
                </Col>
                <Col sm={4} xs={4} className="col">
                  <label style={{ width: '90%' }}>
                    Reason for claim
                    <select
                      className="form-control"
                      defaultValue={this.state.reason}
                      onChange={e => this.setState({ reason: e.target.value })}
                    >
                      <option />
                      <option value="DAMAGED">DAMAGED</option>
                      <option value="MISSING">MISSING</option>
                      <option value="SERVICE_FAILURE">SERVICE FAILURE</option>
                      <option value="NO_SHIP">DID NOT SHIP/CANCEL</option>
                    </select>
                  </label>
                </Col>
              </Row>
            </FormGroup>
            {this.props.isFreightcom &&
              this.props.userOptions.length === 1 &&
              <FormGroup>
                <label>
                  {strings.claim.userPrompt}
                  {this.props.userOptions[0].text}
                </label>
              </FormGroup>}
            {this.props.isFreightcom &&
              this.props.userOptions.length > 1 &&
              <FormGroup>
                <label>
                  {strings.claim.userPrompt}
                  <select
                    name="userRoleId"
                    className="form-control"
                    onChange={e =>
                      this.setState({ userRoleId: e.target.value })}
                    defaultValue={this.state.userRoleId}
                  >
                    <option key={99999} value="">- choose a user -</option>
                    {this.props.userOptions.map(item => (
                      <option key={item.id} value={item.id}>
                        {item.text}
                      </option>
                    ))}
                  </select>
                </label>
              </FormGroup>}

            <FormGroup>
              <Button
                bsStyle="default"
                bsSize="small"
                onClick={this.props.toggleModal}
              >
                Cancel
              </Button>
              <Button
                bsStyle="primary"
                bsSize="small"
                onClick={e =>
                  this.props.submitClaim(
                    this.props.orderId,
                    {
                      comment: this.state.comment,
                      amount: this.state.amount,
                      userRoleId: this.state.userRoleId,
                      reason: this.state.reason,
                      currency: this.state.currency,
                    },
                    this.props.callback,
                  )}
              >
                Submit Claim
              </Button>
            </FormGroup>

          </form>
        </div>
      </Col>
    );
  }
}

const mapStateToProps = (state, nextProps) => {
  return {
    userOptions: orderUserOptions(state, nextProps.orderId),
    currencies: selectCurrencies(state),
  };
};

export default connect(mapStateToProps, {
  submitClaim,
  loadOrderCustomers,
  loadCurrencyList,
})(SubmitClaimForm);
