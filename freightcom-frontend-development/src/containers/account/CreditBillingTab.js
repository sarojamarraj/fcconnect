import React, { Component } from 'react';
import { Field } from 'redux-form';
import { connect } from 'react-redux';
import { selectOneFromEntities } from '../../reducers';
import { updateCustomer, loadCustomerById } from '../../actions/customers';

class CreditBilling extends Component {
  componentWillMount() {
    if (this.props.activeRoleCustomerId) {
      this.props.loadCustomerById(this.props.activeRoleCustomerId);
    }
  }
  render() {
    const { activeRole } = this.props;

    return (
      <fieldset>
        <FieldSection
          activeRole={activeRole.roleName}
          authorizedRole={[
            'ADMIN',
            'AGENT',
            'CUSTOMER_ADMIN',
            'CUSTOMER_STAFF',
            'FREIGHTCOM_STAFF',
          ]}
          colSize="col col-sm-12"
        >
          <div className="table-responsive">
            <table className="table table-bordered table-condensed">
              <thead>
                <tr>
                  <th>Type</th>
                  <th>Name on Card</th>
                  <th>Number</th>
                  <th>Expiry Date</th>
                  <th>CVC</th>
                  <th />
                </tr>
              </thead>
              {this.renderCCList()}
            </table>
          </div>
        </FieldSection>
        <FieldSection
          activeRole={activeRole.roleName}
          authorizedRole={[
            'ADMIN',
            'AGENT',
            'CUSTOMER_ADMIN',
            'CUSTOMER_STAFF',
            'FREIGHTCOM_STAFF',
          ]}
        >
          <label className="label">Receive transaction receipts?</label>
          <label className="select">
            <Field component="select" name="ccReceipt">
              <option value={true}>Yes</option>
              <option value={false}>No</option>
            </Field>
            <i />
          </label>
        </FieldSection>
      </fieldset>
    );
  }

  renderCCList = () => {
    const { customer } = this.props;

    if (!customer.creditCards || !customer.creditCards.length) {
      return (
        <tbody><tr><td colSpan="6">No credit card found...</td></tr></tbody>
      );
    }

    return (
      <tbody>
        {customer.creditCards &&
          customer.creditCards.length &&
          customer.creditCards.map((cc, index) => {
            return (
              <tr key={'cc' + index}>
                <td><i className="fa fa-cc-visa fa-2x" /></td>
                <td>{cc.name}</td>
                <td>{cc.number}</td>
                <td>{cc.expiryMonth}/{cc.expiryYear}</td>
                <td>***</td>
                <td>
                  {cc.isDefault
                    ? '[Default]'
                    : <a
                        href="#"
                        onClick={e => {
                          e.preventDefault();
                          this.makeDefault(index);
                        }}
                      >
                        [Make Default]
                      </a>}
                  {' '}{' '}
                  <a
                    href="#"
                    onClick={e => {
                      e.preventDefault();
                      this.removeCC(index);
                    }}
                  >
                    {' '}
                    [Remove]
                    {' '}
                  </a>
                </td>
              </tr>
            );
          })}
      </tbody>
    );
  };

  makeDefault = index => {
    const creditCards = this.props.customer.creditCards;
    if (index > -1) {
      for (let i = 0; i < creditCards.length; i++) {
        creditCards[i].isDefault = i === index ? true : false;
      }
    }
    this.props.updateCustomer(
      { creditCards: creditCards, version: this.props.customer.version },
      this.props.customerId,
    );
  };

  removeCC = index => {
    const creditCards = this.props.customer.creditCards;

    if (index > -1) {
      creditCards.splice(index, 1);
    }

    this.props.updateCustomer(
      { creditCards: creditCards, version: this.props.customer.version },
      this.props.customerId,
    );
  };
}

const FieldSection = ({
  children,
  activeRole,
  authorizedRole,
  colSize = 'col col-6',
}) => {
  if (~authorizedRole.indexOf(activeRole)) {
    return <section className={colSize}>{children}</section>;
  }
  return null;
};

const mapStateToProps = (state, ownProps) => {
  let customer = null;
  let activeRoleCustomerId = false;

  if (ownProps.activeRole.roleName === 'CUSTOMER_ADMIN') {
    activeRoleCustomerId = ownProps.activeRole.customerId;
    customer = selectOneFromEntities(state, 'customer', activeRoleCustomerId);
  } else {
    customer = selectOneFromEntities(state, 'customer', ownProps.customerId);
  }

  return { customer, activeRoleCustomerId };
};

export default connect(mapStateToProps, { updateCustomer, loadCustomerById })(
  CreditBilling,
);
