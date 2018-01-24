import React, { Component } from 'react';
import { Row, Col, Modal } from 'react-bootstrap';
import Button from 'react-bootstrap-button-loader';
import { connect } from 'react-redux';
import { inviteCustomerStaff } from '../../actions/customers';
import { addNotification } from 'reapop';

class CustomerUserModal extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isSubmitting: false,
      email: false,
    };
  }
  render() {
    return (
      <div>
        <Modal show={this.props.show} onHide={this.props.hideModal}>
          <Modal.Header closeButton>
            <Modal.Title>Invite User</Modal.Title>
          </Modal.Header>
          <form
            onSubmit={this.handleSubmit}
            className="modalForm form-horizontal">
            <Modal.Body>
              <Row>
                <Col className="col" sm={12}>
                  Please enter an email address of the user you would like to
                  invite. The user will be prompted to supply personal details,
                  such as contact information, and password.
                </Col>
                <Col className="col" sm={12}>
                  <section className="form-group" style={{ marginBottom: '0' }}>
                    <label />
                    <label>
                      <input
                        type="email"
                        name="email"
                        placeholder="Enter an email"
                        onChange={e => {
                          this.setState({ email: e.target.value });
                        }}
                      />
                    </label>
                  </section>
                </Col>
              </Row>
            </Modal.Body>
            <Modal.Footer>
              <Button
                bsStyle="primary"
                type="submit"
                disabled={!this.state.email}
                loading={this.state.isSubmitting}>
                Invite
              </Button>
            </Modal.Footer>
          </form>
        </Modal>
      </div>
    );
  }
  handleSubmit = e => {
    e.preventDefault();
    this.setState({ isSubmitting: true });
    this.props.inviteCustomerStaff(
      this.props.customerId,
      this.state.email,
      data => {
        this.setState({ isSubmitting: false });
        this.props.addNotification({
          title: 'Invitation Sent',
          message: `Your invitation was sent to ${this.state
            .email}.`,
          status: 'success',
          allowHTML: true,
          dismissible: false,
          dismissAfter: 10000,
          buttons: [
            {
              name: 'OK',
              primary: true,
            },
          ],
        });
        this.props.hideModal();
      },
    );
  };
}

const mapDispatchToProps = { inviteCustomerStaff, addNotification };

export default connect(null, mapDispatchToProps)(CustomerUserModal);
