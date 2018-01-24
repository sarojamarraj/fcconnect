import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Link, browserHistory } from 'react-router';
// import logo from '../styles/img/logo.png';

class Navigation extends Component {
  render() {
    const { activeRole } = this.props;
    return (
      <aside id="left-panel">
        <nav>
          <ul>
            <NavItem
              activeRole={activeRole.roleName}
              authorizedRole={[
                'ADMIN',
                'AGENT',
                'CUSTOMER_ADMIN',
                'CUSTOMER_STAFF',
                'FREIGHTCOM_STAFF',
              ]}
              url="/dashboard"
            >
              <Link to="/dashboard">
                <i className="fa fa-lg fa-fw fa-home" />
                <span className="menu-item-parent"> Dashboard</span>
              </Link>
            </NavItem>
            <NavItem
              activeRole={activeRole.roleName}
              authorizedRole={[
                'ADMIN',
                'AGENT',
                'FREIGHTCOM_STAFF',
                'CUSTOMER_ADMIN',
                'CUSTOMER_STAFF',
              ]}
              url="/shipments"
            >
              <Link to="/shipments">
                <i className="fa fa-lg fa-fw fa-truck" />
                <span className="menu-item-parent"> Shipments</span>
              </Link>
            </NavItem>
            <NavItem
              activeRole={activeRole.roleName}
              authorizedRole={[
                'ADMIN',
                'AGENT',
                'CUSTOMER_ADMIN',
                'CUSTOMER_STAFF',
                'FREIGHTCOM_STAFF',
              ]}
              url="/invoices"
            >
              <Link to="/invoices">
                <i className="fa fa-lg fa-fw fa-file-o" />
                <span className="menu-item-parent"> Invoices</span>
              </Link>
            </NavItem>
            <NavItem
              activeRole={activeRole.roleName}
              authorizedRole={['ADMIN', 'AGENT', 'FREIGHTCOM_STAFF']}
              url="/customers"
            >
              <Link to="/customers">
                <i className="fa fa-lg fa-fw fa-users" />
                <span className="menu-item-parent"> Customers</span>
              </Link>
            </NavItem>

            <NavItem
              activeRole={activeRole.roleName}
              authorizedRole={['ADMIN', 'FREIGHTCOM_STAFF', 'AGENT']}
              url="/agents"
            >
              <Link to="/agents">
                <i className="fa fa-lg fa-fw fa-user-secret" />
                <span className="menu-item-parent"> Agents</span>
              </Link>
            </NavItem>
            <NavItem
              activeRole={activeRole.roleName}
              authorizedRole={['ADMIN']}
              url="/users"
            >
              <Link to="/users">
                <i className="fa fa-lg fa-fw fa-users" />
                <span className="menu-item-parent"> Users</span>
              </Link>
            </NavItem>
            <NavItem
              activeRole={activeRole.roleName}
              authorizedRole={['ADMIN']}
              url="/carriers"
            >
              <Link to="/carriers">
                <i className="fa fa-lg fa-fw fa-truck" />
                <span className="menu-item-parent"> Carriers</span>
              </Link>
            </NavItem>
            <NavItem
              activeRole={activeRole.roleName}
              authorizedRole={['ADMIN']}
              url="/edi"
            >
              <Link to="/edi">
                <i className="fa fa-lg fa-fw fa-truck" />
                <span className="menu-item-parent"> Carrier EDI</span>
              </Link>
            </NavItem>
            <NavItem
              activeRole={activeRole.roleName}
              authorizedRole={['ADMIN']}
              url="/settings"
            >
              <Link to="/settings">
                <i className="fa fa-lg fa-fw fa-gears" />
                <span className="menu-item-parent"> Settings</span>
              </Link>
            </NavItem>
            <NavItem
              activeRole={activeRole.roleName}
              authorizedRole={['CUSTOMER_ADMIN', 'CUSTOMER_STAFF']}
              url="/address-book"
            >
              <Link to="/address-book">
                <i className="fa fa-lg fa-fw fa-address-book-o" />
                <span className="menu-item-parent"> Address Book</span>
              </Link>
            </NavItem>
            <NavItem
              activeRole={activeRole.roleName}
              authorizedRole={['CUSTOMER_ADMIN']}
              url="/accounts"
            >
              <Link to="/accounts">
                <i className="fa fa-lg fa-fw fa-cogs" />
                <span className="menu-item-parent"> Account</span>
              </Link>
            </NavItem>
            {false &&
              <NavItem
                activeRole={activeRole.roleName}
                authorizedRole={['CUSTOMER_ADMIN']}
              >
                <a href="#" ref={el => this.attachSubNav(el)}>
                  <i className="fa fa-lg fa-fw fa-cogs" />
                  <span className="menu-item-parent"> Account</span>
                  <b className="collapse-sign">
                    <em className="fa fa-plus-square-o" />
                  </b>
                </a>
                <ul>
                  <li>
                    <Link to="/accounts/company-profile">Company Profile</Link>
                  </li>
                  <li>
                    <Link to="/accounts/general-settings">
                      General Settings
                    </Link>
                  </li>
                  <li>
                    <Link to="/accounts/credit-billing">Credit & Billing</Link>
                  </li>
                  <li>
                    <Link to="#/shipment-settings">Shipment Preferences</Link>
                  </li>
                  <li><Link to="/users">Manage Users</Link></li>
                </ul>
              </NavItem>}

            <NavItem
              activeRole={activeRole.roleName}
              authorizedRole={['ADMIN']}
              url="/payable-statement"
            >
              <Link to="/payables">
                <i className="fa fa-lg fa-fw fa-address-book-o" />
                <span className="menu-item-parent"> Payables</span>
              </Link>
            </NavItem>
          </ul>
        </nav>
      </aside>
    );
  }

  attachSubNav = element => {
    const $ = window.jQuery;
    $(element).off();
    $(element).click(() => {
      $(element).parent().find('ul:first').slideToggle(() => {
        if ($(element).parent().find('ul:first').is(':visible')) {
          $(element)
            .find('.collapse-sign')
            .html('<em class="fa fa-minus-square-o"></em>');
        } else {
          $(element)
            .find('.collapse-sign')
            .html('<em class="fa fa-plus-square-o"></em>');
        }
      });
      return false;
    });
  };
}

const NavItem = connect(state => ({
  activeURL: browserHistory.getCurrentLocation().pathname,
}))(({ children, authorizedRole, activeRole, url, activeURL }) => {
  if (~authorizedRole.indexOf(activeRole)) {
    return <li className={url === activeURL ? 'active' : ''}>{children}</li>;
  }
  return null;
});

const mapStateToProps = state => {
  const { loggedIn: { role = {} } } = state;
  return {
    activeRole: role,
  };
};

export default connect(mapStateToProps)(Navigation);
