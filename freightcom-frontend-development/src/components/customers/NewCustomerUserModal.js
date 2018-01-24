import React, { Component } from 'react';
import { Modal } from 'react-bootstrap';
import { Row, Col } from 'react-bootstrap';
import { connect } from 'react-redux';
import Button from 'react-bootstrap-button-loader';
import { save } from '../../actions/api';

class NewCustomerUserModal extends Component {
  constructor(props) {
    super(props);
    this.state = {
      firstname: '',
      lastname: '',
      cell: '',
      phone: '',
      email: '',
      login: '',
      password: '',
    };
  }
  render() {
    return (
      <Modal show={this.props.show} onHide={this.props.hideModal}>
        <Modal.Header closeButton>
          <Modal.Title>New Customer Admin</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <form
            className="modalForm form-horizontal"
            onSubmit={e => {
              e.preventDefault();
              this.handleSubmit();
            }}
          >
            <Row>
              <Col className="col" sm={6}>
                <section className="form-group">
                  <label>firstname</label>
                  <label>
                    <input type="text" name="firstname" onChange={this.store} />
                  </label>
                </section>
              </Col>
              <Col className="col" sm={6}>
                <section className="form-group">
                  <label>Lastname</label>
                  <label>
                    <input type="text" name="lastname" onChange={this.store} />
                  </label>
                </section>
              </Col>
            </Row>
            <Row>
              <Col className="col" sm={6}>
                <section className="form-group">
                  <label>Cell</label>
                  <label>
                    <input type="text" name="cell" onChange={this.store} />
                  </label>
                </section>
              </Col>
              <Col className="col" sm={6}>
                <section className="form-group">
                  <label>Office Phone</label>
                  <label>
                    <input type="text" name="phone" onChange={this.store} />
                  </label>
                </section>
              </Col>
            </Row>
            <Row>
              <Col className="col" sm={6}>
                <section className="form-group">
                  <label>Email</label>
                  <label>
                    <input type="email" name="email" onChange={this.store} />
                  </label>
                </section>
              </Col>
            </Row>
            <Row>
              <Col className="col" sm={6}>
                <section className="form-group">
                  <label>Username</label>
                  <div className="input-group">
                    <input
                      type="text"
                      className="form-control"
                      disabled
                      name="login"
                      ref="login"
                    />
                    <span className="input-group-btn">
                      <Button
                        className="btn btn-default"
                        type="button"
                        disabled={this.validateGButton()}
                        onClick={this.generateUsername}
                      >
                        Generate
                      </Button>
                    </span>
                  </div>
                </section>
              </Col>
              <Col className="col" sm={6}>
                <section className="form-group">
                  <label>Password</label>
                  <div className="input-group">
                    <input
                      type="text"
                      className="form-control"
                      disabled
                      name="password"
                      ref="password"
                    />
                    <span className="input-group-btn">
                      <Button
                        className="btn btn-default"
                        type="button"
                        disabled={this.validateGButton()}
                        onClick={this.generatePassword}
                      >
                        Generate
                      </Button>
                    </span>
                  </div>
                </section>
              </Col>
            </Row>
            <div className="row modalForm-footer">
              <div className="col-sm-12 col-xs-12">
                <Button
                  onClick={() => {
                    this.props.hideModal(true);
                  }}
                  className="btn btn-sm"
                >
                  Cancel
                </Button>
                <Button
                  type="submit"
                  className="btn btn-sm btn-primary"
                  disabled={!this.validate()}
                >
                  Submit
                </Button>
              </div>
            </div>
          </form>
        </Modal.Body>
      </Modal>
    );
  }
  validate = () => {
    const {
      firstname,
      lastname,
      email,
      phone,
      cell,
      login,
      password,
    } = this.state;
    if (
      !firstname ||
      !lastname ||
      !email ||
      !phone ||
      !cell ||
      !login ||
      !password
    ) {
      return false;
    }
    return true;
  };
  store = e => {
    this.setState({ [e.target.name]: e.target.value });
  };
  validateGButton = () => {
    if (this.state.firstname && this.state.lastname) {
      return false;
    }
    return true;
  };
  generateUsername = () => {
    const { firstname, lastname } = this.state;
    let login = '';

    if (lastname.length > 7) {
      login = firstname.substring(0, 1) + lastname.substring(0, 7);
    } else {
      login = firstname.substring(0, 1) + lastname;
    }
    login += Math.floor(Math.random() * 99999999 + 10000000)
      .toString()
      .substring(0, 10 - login.length);

    this.setState({ login: login });
    this.refs.login.value = login;
  };
  generatePassword = () => {
    let password = '';
    const chars =
      'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    for (let i = 0; i < 9; i++) {
      password += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    this.setState({ password: password });
    this.refs.password.value = password;
  };
  handleSubmit = () => {
    this.props.save('user', this.state).then(data => {
      this.props.setSelectedUser(data.payload.entity);
    });
  };
}

export default connect(null, { save })(NewCustomerUserModal);
