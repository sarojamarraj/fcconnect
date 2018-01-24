import React, { Component } from 'react';
import { connect } from 'react-redux';
import { loadCustomerById } from '../../actions/customers';
import { selectOneFromEntities } from '../../reducers';

class AccountStatus extends Component {
  render() {
    return (
      <div className="col-sm-6">
        <div
          className="jarviswidget jarviswidget-sortable jarviswidget-color-blue"
          id="wid-id-1"
          data-widget-editbutton="false"
        >
          <header role="heading">
            <span className="widget-icon">
              <i className="fa fa-bar-chart-o" />
            </span>
            <h2>Account Status</h2>
            <span className="jarviswidget-loader">
              <i className="fa fa-refresh fa-spin" />
            </span>
          </header>
          <div>
            <div className="widget-body no-padding">
              <div className="well no-border">
                {this.renderCreditBar()}
                <h3 style={{ margin: '0px' }}>User Seats</h3>
                <div className="bar-holder">
                  <div className="progress">
                    <div
                      className="progress-bar bg-color-redLight"
                      aria-valuenow="100"
                      style={{ width: '80%' }}
                    >
                      User Seats
                    </div>
                  </div>
                  <p className="pull-right">Total Seats: 10</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }

  renderCreditBar = () => {
    let width = '100%';

    const {
      customer: { creditLimit = 0, creditAvailable: { CAD = 0 } = {} },
    } = !this.props.customer ? { ...this.props, customer: {} } : this.props;

    switch (true) {
      case !CAD || !creditLimit:
        width = '100%';
        break;
      case !isNaN(CAD / creditLimit):
        width = parseInt(CAD / creditLimit * 100, 10) + '%';
        break;
      default:
        width = '100%';
    }

    return (
      <div>
        <h3 style={{ margin: '0px' }}>Credit</h3>
        <div className="bar-holder">
          <div className="progress">
            <div
              className="progress-bar bg-color-blueLight"
              style={{ width: width }}
            >
              ${CAD}
            </div>
          </div>
          <p className="pull-right">
            Available Credit: $
            {CAD}
            {' '}
            {' / '}
            {' '}
            Credit Limit: $
            {creditLimit}
          </p>
        </div>
      </div>
    );
  };
}

const mapStateToProps = state => {
  let customer = null;
  let activeRoleCustomerId = false;
  if (
    state.loggedIn.role.roleName === 'CUSTOMER_ADMIN' ||
    state.loggedIn.role.roleName === 'CUSTOMER_STAFF'
  ) {
    activeRoleCustomerId = state.loggedIn.role.customerId;
    customer = selectOneFromEntities(state, 'customer', activeRoleCustomerId);
  }

  return { customer, activeRoleCustomerId };
};
export default connect(mapStateToProps, { loadCustomerById })(AccountStatus);
