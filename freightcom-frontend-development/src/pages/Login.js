import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Field, reduxForm } from 'redux-form';
import { withRouter } from 'react-router';
import { Modal, Row, Col } from 'react-bootstrap';
import Button from 'react-bootstrap-button-loader';

import '../styles/css/login-styles.css';
import HeaderLogo from '../styles/img/freightcom-logo.png';
import {
  loginAction,
  logoutAction,
  LOGGED_IN,
  PRE_LOGGED_IN,
  passwordResetAction,
  passwordDialog,
  setSelectedRole,
  clearStoredUrl,
} from '../actions/loginSubmit';
import { loginStoredUrl } from '../reducers/index.js';
import ResetPassword from '../components/ResetPassword';
import { formatRole } from '../utils';
const $ = window.jQuery;

class Login extends Component {
  constructor(props) {
    super(props);
    this.state = {
      showRoleModal: false,
      redirected: false,
      isSubmitting: false,
    };
  }
  componentWillMount() {
    const { loggedIn } = this.props;

    if (loggedIn.status === LOGGED_IN || loggedIn.status === PRE_LOGGED_IN) {
      this.props.logoutAction();
    }
  }

  componentDidUpdate(prevProps, prevState) {
    const {
      loggedIn,
      loggedInUser,
      router,
      setSelectedRole,
      redirectUrl,
      clearStoredUrl,
    } = this.props;

    if (
      loggedIn.status === LOGGED_IN &&
      !(loggedIn.role && loggedIn.role.id) &&
      !loggedIn.requesting_role
    ) {
      if (
        loggedInUser &&
        loggedInUser.authorities &&
        loggedInUser.authorities.length === 1 &&
        this.state.submitted
      ) {
        setSelectedRole(loggedInUser.authorities[0].id);
      }
    } else if (
      loggedIn.status === LOGGED_IN &&
      loggedIn.role &&
      loggedIn.role.id &&
      !this.state.redirected
    ) {
      // Only redirect once after role has been set (didUpdate is called several times, e.g. due to form destroy)
      this.setState({ redirected: true, haveCalledSetRole: true }, () => {
        clearStoredUrl().then(() => {
          router.replace(redirectUrl);
        });
      });
    }
  }

  switchRole = roleId => {
    this.props.setSelectedRole(roleId);
  };

  closeRoleModal = () => {
    this.props.logoutAction();
    this.setState({ showRoleModal: false });
  };

  submitHandler = values => {
    this.setState({ isSubmitting: true });
    const loginAction = this.props.loginAction;
    const $ = window.jQuery;
    const form = $('<form action="/api/login" method="POST"/>');

    $('<input type="hidden" name="username">')
      .val(values.username)
      .appendTo(form);
    $('<input type="hidden" name="password">')
      .val(values.password)
      .appendTo(form);
    $('#login-submit-frame').contents().find('body').append(form);

    return loginAction(values, $('#login-submit-frame'), form, result => {
      this.setState({ isSubmitting: false, submitted: true });
    });
  };

  resetSubmitButtonAction = e => {
    e.preventDefault();
    const submitButton = $("<input type='submit' />")
      .css('display', 'none')
      .appendTo($(e.target).closest('.modal-dialog').find('form'));

    window.setTimeout(() => {
      submitButton.click();
    });
  };

  resetPassword = values => {
    this.props.passwordResetAction(values && values.email);
  };

  open = () => {
    this.props.passwordDialog('open');
  };

  close = () => {
    this.props.passwordDialog('close');
  };

  alert = () => {
    alert('Instruction to reset password is sent to your email.');
  };

  render() {
    const {
      handleSubmit,
      submitting,
      loggedIn,
      loggedInUser: { authorities = [] },
    } = this.props;

    const iframe_styles = {
      position: 'absolute',
      top: '0',
      left: '0',
      height: '0',
      width: '0',
    };

    if (1 === LOGGED_IN) {
      return null;
    } else {
      return (
        <div id="extr-page">
          <header id="header" className="login-header">
            <div id="header-logo">
              <img
                src={HeaderLogo}
                alt="SmartAdmin"
                style={{ width: '210px' }}
              />
            </div>
            <span id="extr-page-header-space">
              <span className="hidden-mobile hidden-xs">Need an account? </span>
              <a href="register.html" className="btn btn-danger">
                Create account
              </a>
            </span>
          </header>
          <div id="main" role="main">
            <div id="" className="container">
              <div className="row">
                <div className="col-xs-12 col-sm-12 col-md-7 col-lg-8 hidden-xs hidden-sm">
                  <Row>
                    <Col xs={12}>
                      <div className="video-wrapper">
                        <iframe
                          src="//www.youtube.com/embed/JCgBd9iX58g"
                          frameBorder="0"
                        />
                      </div>
                      <br />
                    </Col>
                  </Row>
                  <Row>
                    <Col xs={12} sm={12} md={6} lg={6}>
                      <h5 className="about-heading">
                        Efficient, Cost-Effective LTL Shipping Solutions for
                        Small and Midsize Business
                      </h5>
                      <p>
                        Sed ut perspiciatis unde omnis iste natus error sit
                        voluptatem accusantium doloremque laudantium, totam rem
                        aperiam, eaque ipsa.
                      </p>
                    </Col>
                    <Col xs={12} sm={12} md={6} lg={6}>
                      <h5 className="about-heading">
                        Get The Best LTL Freight Rates Anytime, Instantly!
                      </h5>
                      <p>
                        Et harum quidem rerum facilis est et expedita
                        distinctio. Nam libero tempore, cum soluta nobis est
                        eligendi voluptatem accusantium!
                      </p>
                    </Col>
                  </Row>
                </div>
                <div className="col-xs-12 col-sm-12 col-md-5 col-lg-4">
                  <div className="no-padding">
                    <form
                      id="login-form"
                      className="smart-form client-form"
                      noValidate
                      onSubmit={handleSubmit(this.submitHandler)}
                    >
                      <header>Sign In</header>
                      <fieldset>
                        {loggedIn.reset_status !== 'open' &&
                          loggedIn.message &&
                          loggedIn.message !== 'No message available' &&
                          loggedIn.message !== 'Internal Error' &&
                          loggedIn.message !== 'Logged out'
                          ? <div
                              className={
                                loggedIn.message_class !== 'ok'
                                  ? 'alert alert-danger'
                                  : 'alert alert-success'
                              }
                            >
                              {loggedIn.message}
                            </div>
                          : ''}
                        <section>
                          <label className="label">Login</label>
                          <label className="input">
                            <i className="icon-append fa fa-user" />
                            <Field
                              component="input"
                              type="text"
                              className="wo-input"
                              name="username"
                            />
                            <b className="tooltip tooltip-top-right">
                              <i className="fa fa-user txt-color-teal" />
                              Please enter your login/username
                            </b>
                          </label>
                        </section>
                        <section>
                          <label className="label">Password</label>
                          <label className="input">
                            <i className="icon-append fa fa-lock" />
                            <Field
                              component="input"
                              type="password"
                              className="wo-input"
                              name="password"
                            />
                            <b className="tooltip tooltip-top-right">
                              <i className="fa fa-lock txt-color-teal" />
                              Enter your password
                            </b>
                          </label>
                          <div className="note">
                            <a href="#" onClick={this.open}>Forgot password?</a>
                          </div>
                        </section>
                      </fieldset>
                      <footer>
                        <Button
                          type="submit"
                          className="btn btn-primary"
                          disabled={submitting}
                          loading={this.state.isSubmitting}
                        >
                          Sign in
                        </Button>
                      </footer>
                    </form>
                  </div>
                </div>
              </div>
            </div>
            <Modal show={loggedIn.reset_status === 'open'} onHide={this.close}>
              <Modal.Header closeButton>
                <Modal.Title>Forgot Password?</Modal.Title>
              </Modal.Header>
              <Modal.Body>
                <ResetPassword resetSubmit={this.resetPassword} />
              </Modal.Body>
              <Modal.Footer>
                <Button onClick={this.close} className="btn btn-default">
                  Close
                </Button>
                <Button
                  type="button"
                  className="btn btn-primary"
                  onClick={this.resetSubmitButtonAction}
                >
                  Submit
                </Button>
              </Modal.Footer>
            </Modal>
            <Modal
              show={
                loggedIn.status === LOGGED_IN &&
                  !(loggedIn.role && loggedIn.role.id) &&
                  !loggedIn.requesting_role
              }
              onHide={this.closeRoleModal}
            >
              <Modal.Header closeButton>
                <Modal.Title>
                  Please select the role
                </Modal.Title>
              </Modal.Header>
              <Modal.Body>
                <div className="list-group">
                  {authorities &&
                    authorities.map(role => {
                      return role.customerName
                        ? <a
                            key={role.id}
                            href="#"
                            className="list-group-item"
                            onClick={() => {
                              this.switchRole(role.id);
                            }}
                            data-id={role.id}
                          >
                            {formatRole(role.roleName)} at {role.customerName}
                          </a>
                        : <a
                            key={role.id}
                            href="#"
                            className="list-group-item"
                            onClick={() => {
                              this.switchRole(role.id);
                            }}
                            data-id={role.id}
                          >
                            {formatRole(role.roleName)}
                          </a>;
                    })}
                </div>
              </Modal.Body>
            </Modal>
          </div>
          <iframe id="login-submit-frame" style={iframe_styles} />
          <footer className="footer">
            <h4>Call Today!</h4>
            <h3>1-877-335-8740</h3>
            <p><b>Business Hours: Mon to Fri: 8am to 6pm</b></p>
          </footer>
        </div>
      );
    }
  }
}

const mapStateToProps = state => {
  const loggedInUser = state.loggedInUser.id ? state.loggedInUser : {};
  const redirectUrl = loginStoredUrl(state) || '/dashboard';

  return { loggedIn: state.loggedIn, loggedInUser, redirectUrl };
};

const innerForm = withRouter(
  reduxForm({
    form: 'login-form',
    // a unique identifier for this form
    onSubmitFail: errors => console.debug(errors),
  })(Login),
);

export default connect(mapStateToProps, {
  logoutAction,
  loginAction,
  passwordResetAction,
  passwordDialog,
  setSelectedRole,
  clearStoredUrl,
})(innerForm);
