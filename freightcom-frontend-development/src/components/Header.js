import React, { PureComponent } from 'react';
import { connect } from 'react-redux';
import { Modal } from 'react-bootstrap';
import Preferences from './Preferences';
import { setSelectedRole } from '../actions/loginSubmit';
import { Link, browserHistory } from 'react-router';
import { formatRole } from '../utils';
import logo from '../styles/img/connect-logo.png';

import { ButtonToolbar } from 'react-bootstrap';

import Icon from '../components/Icon';

export const ORDER_QUOTE = 'ORDER_QUOTE';

class Header extends PureComponent {
  constructor(props) {
    super(props);
    this.state = {
      preferencesModal: false,
      changeRoleModal: false,
      welcomeMsgModal: false,
      welcomeMsg: {
        welcomeTitle: 'Welcome to Freightcom',
        welcomeBody:
          'Access this system to process shipments instantly. Schedule LTL shipments from anywhere to anywhere in North America including Trans-Border. Real time, user friendly access to very competitive LTL and courier rates.',
      },
    };
  }

  componentDidMount() {
    const $ = window.jQuery;

    $("a[data-action='toggleMenu']").click(e => {
      e.preventDefault();
      $('body').toggleClass('hidden-menu');
    });
  }

  render() {
    const {
      loggedInUser: { firstname = '', lastname = '', authorities = [] },
    } = this.props;
    const contactNumber = '1.877.335.8740';

    return (
      <header id="header" className="app-header">
        <div id="logo-group">
          <span className="logo">
            <Link to="/dashboard">
              <img src={logo} alt="Freightcom" className="img-responsive" />
            </Link>
          </span>
        </div>
        <div className="pull-right" id="header-dropdown-list">
          <div id="hide-menu" className="btn-header pull-right">
            <span>
              <a href="#" data-action="toggleMenu" title="Collapse Menu">
                <i className="fa fa-reorder" />
              </a>
            </span>
          </div>
          <ul className="header-dropdown-list">
            <li className="">
              <a href="#" className="dropdown-toggle" data-toggle="dropdown">
                <span>Hello <b>{firstname}, {lastname}</b></span>
                {' '}<i className="fa fa-angle-down" />
              </a>
              <ul className="dropdown-menu pull-right">
                <li>
                  <a
                    href="#"
                    onClick={this.showPreferencesModal}
                    className="padding-10 padding-top-5 padding-bottom-5"
                  >
                    <i className="fa fa-cog fa-lg" />{' '}Preferences
                  </a>
                </li>
                {authorities.length > 1 &&
                  <li>
                    <a
                      href="#"
                      onClick={this.showChangeRoleModal}
                      className="padding-10 padding-top-5 padding-bottom-5"
                    >
                      <i className="fa fa-user-circle-o fa-lg" />
                      {' '}Switch Role
                    </a>
                  </li>}
                <li className="divider" />
                <li>
                  <a
                    href="/login"
                    className="padding-10 padding-top-5 padding-bottom-5"
                    data-action="userLogout"
                  >
                    <i className="fa fa-sign-out fa-lg" />
                    <strong>
                      {' '}Logout
                    </strong>
                  </a>
                </li>
              </ul>
            </li>
          </ul>
          <Modal
            show={this.state.preferencesModal}
            onHide={this.hidePreferencesModal}
          >
            <Modal.Header closeButton>
              <Modal.Title>Preferences</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              <Preferences close={this.hidePreferencesModal} />
            </Modal.Body>
          </Modal>
          <Modal
            show={this.state.changeRoleModal}
            onHide={this.hideChangeRoleModal}
          >
            <Modal.Header closeButton>
              <Modal.Title>Change Role</Modal.Title>
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
                          <b>{role.customerName}</b>
                          {' '}
                          <em>({formatRole(role.roleName)})</em>
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
        <div id="contact-number" className="pull-right">
          <div className="pull-right">
            <span>{contactNumber}</span>
          </div>
        </div>
        <ButtonToolbar id="quick-nav" className="pull-right">
          <button
            className="btn btn-blue"
            onClick={e => browserHistory.push('/order/new-quote')}
          >
            <Icon name="edit" /> New Quote
          </button>
          <button
            className="btn btn-black"
            onClick={e => browserHistory.push('/order/new-shipment')}
          >
            <Icon name="truck" /> New Shipment
          </button>
        </ButtonToolbar>
      </header>
    );
  }

  showPreferencesModal = () => {
    this.setState({ preferencesModal: true });
  };

  hidePreferencesModal = () => {
    this.setState({ preferencesModal: false });
  };

  showChangeRoleModal = () => {
    this.setState({ changeRoleModal: true });
  };

  hideChangeRoleModal = () => {
    this.setState({ changeRoleModal: false });
  };

  switchRole = roleId => {
    this.props.setSelectedRole(roleId);
    this.hideChangeRoleModal();
    browserHistory.push('/dashboard');
  };

  showWelcomeModal = () => {
    this.setState({ welcomeMsgModal: true });
  };

  hideWelcomeModal = () => {
    this.setState({ welcomeMsgModal: false });
  };
}

const mapStateToProps = state => {
  return { loggedInUser: state.loggedInUser };
};

export default connect(mapStateToProps, { setSelectedRole })(Header);
