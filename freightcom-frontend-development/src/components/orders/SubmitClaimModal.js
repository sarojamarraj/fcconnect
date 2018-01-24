import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Modal, Button } from 'react-bootstrap';

import { submitClaim, loadOrderCustomers } from '../../actions/orders';
import { orderUserOptions } from '../../reducers';

import FormGroup from '../forms/FormGroup';
import strings from '../../strings';

class SubmitClaimModal extends Component {
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
    const { showModal, isFreightcom, orderId, loadOrderCustomers } = this.props;

    if (showModal && isFreightcom) {
      loadOrderCustomers(orderId);
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
      <Modal show={this.props.showModal} onHide={this.props.toggleModal}>
        <Modal.Header closeButton>
          <Modal.Title>Submit Claim</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <FormGroup label={strings.claim.submitComment}>
            <textarea
              className="form-control"
              defaultValue={this.state.comment}
              onChange={e => this.setState({ comment: e.target.value })}
            />
          </FormGroup>
          <FormGroup>
            <label>
              Claimed Amount
              <input
                type="text"
                className="form-control"
                defaultValue={this.state.amount}
                onChange={e => this.setState({ amount: e.target.value })}
              />
            </label>
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
                  onChange={e => this.setState({ userRoleId: e.target.value })}
                  defaultValue={this.state.userRoleId}
                >
                  <option key={99999} value="">- choose a user -</option>
                  {this.props.userOptions.map(item => (
                    <option key={item.id} value={item.id}>{item.text}</option>
                  ))}
                </select>
              </label>
            </FormGroup>}
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
            onClick={e =>
              this.props.submitClaim(
                this.props.orderId,
                {
                  comment: this.state.comment,
                  amount: this.state.amount,
                  userRoleId: this.state.userRoleId,
                },
                this.props.callback,
              )}
          >
            Submit Claim
          </Button>
        </Modal.Footer>
      </Modal>
    );
  }
}

const mapStateToProps = (state, nextProps) => {
  return {
    userOptions: orderUserOptions(state, nextProps.orderId),
  };
};

export default connect(mapStateToProps, { submitClaim, loadOrderCustomers })(
  SubmitClaimModal,
);
