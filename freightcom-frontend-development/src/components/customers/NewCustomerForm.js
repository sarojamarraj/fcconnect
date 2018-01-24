import React, { Component } from 'react';
import { connect } from 'react-redux';
import { reset } from 'redux-form';
import { Row, Col } from 'react-bootstrap';
import { states, provinces, countries } from '../../utils';
import { reduxForm, formValueSelector } from 'redux-form';
import { saveCustomer, saveCustomerAdmin } from '../../actions/customers';
import { saveRole } from '../../actions/users';
import { withRouter, browserHistory } from 'react-router';
import InputText from '../forms/inputs/InputText';
import InputSelect from '../forms/inputs/InputSelect';
import Button from 'react-bootstrap-button-loader';
import { addNotification } from 'reapop';
import UserAutoSuggest from '../../components/users/UserAutoSuggest';
import NewCustomerUserModal from './NewCustomerUserModal';

class NewCustomerForm extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isSubmitting: false,
      selectedUser: null,
      showModal: false,
    };
  }
  render() {
    const { selectedCountry, handleSubmit } = this.props;
    console.log(this.state.selectedUser, 'selected user...');
    return (
      <form
        id="customer-info"
        className="smart-form"
        onSubmit={handleSubmit(this.submit)}
      >
        <div className="">
          <fieldset className="padding-10">
            <div>
              <br />
              <Row>
                <Col className="col" sm={6}>
                  <InputText
                    label="Business Name"
                    name="name"
                    inputSize="input-sm"
                    wrapper="div"
                    required
                  />
                </Col>
              </Row>
              <Row>
                <Col className="col" sm={6}>
                  <InputText
                    label="Street Address"
                    name="address"
                    inputSize="input-sm required-input"
                    wrapper="div"
                  />
                </Col>
                <Col className="col" sm={6}>
                  <InputText
                    label="Unit Number"
                    name="unit"
                    inputSize="input-sm"
                    wrapper="div"
                  />
                </Col>
              </Row>
              <Row>
                <Col className="col" sm={6}>
                  <InputText
                    label="City"
                    name="city"
                    inputSize="input-sm required-input"
                    wrapper="div"
                  />
                </Col>
                <Col className="col" sm={6}>
                  <InputSelect className="required-input" label="Country" name="country" wrapper="div">
                    {Object.keys(countries).map(function(key, index) {
                      return (
                        <option key={key} value={key}>
                          {countries[key]}
                        </option>
                      );
                    })}
                  </InputSelect>
                </Col>
              </Row>
              <Row>
                <Col className="col" sm={6}>
                  <InputSelect className="required-input" label="Province" name="province" wrapper="div">
                    {selectedCountry === 'CA' || !selectedCountry
                      ? Object.keys(provinces).map(function(key, index) {
                          return (
                            <option key={key} value={key}>
                              {provinces[key]}
                            </option>
                          );
                        })
                      : Object.keys(states).map(function(key, index) {
                          return (
                            <option key={key} value={key}>
                              {states[key]}
                            </option>
                          );
                        })}
                  </InputSelect>
                </Col>
                <Col className="col" sm={6}>
                  <InputText
                    label="Postal Code"
                    name="postalCode"
                    inputSize="input-sm required-input"
                    wrapper="div"
                  />
                </Col>
              </Row>
              <Row>
                <Col className="col" sm={6}>
                  <InputText
                    label="Main Phone"
                    name="phone"
                    inputSize="input-sm"
                    wrapper="div"
                  />
                </Col>
                <Col className="col" sm={6}>
                  <InputText
                    label="Fax"
                    name="fax"
                    inputSize="input-sm"
                    wrapper="div"
                  />
                </Col>
              </Row>
              <Row>
                <Col className="col" sm={6}>
                  <label className="label">Main Contact</label>
                  <label className="select">
                    <UserAutoSuggest
                      name="customerAdmin"
                      value={this.state.selectedUser}
                      ref="customerAdmin"
                      onChange={user => {
                        if (user && user.id === 'New') {
                          this.setState({
                            selectedUser: user,
                            showModal: true,
                          });
                        } else {
                          this.setState({
                            selectedUser: user || null,
                            showModal: false,
                          });
                        }
                      }}
                    />
                  </label>
                </Col>
              </Row>
            </div>
          </fieldset>
        </div>
        {this.state.showModal &&
          <NewCustomerUserModal
            show={this.state.showModal}
            hideModal={this.hideModal}
            setSelectedUser={this.setSelectedUser}
          />}
        <hr className="simple" />
        <footer className="clearfix">
          <Button
            type="submit"
            className="btn btn-primary btn-lg"
            loading={this.state.isSubmitting}
          >
            Add Customer
          </Button>
          <button
            className="btn btn-default btn-lg"
            type="button"
            onClick={e => browserHistory.push('/customers')}
          >
            Cancel
          </button>
        </footer>
      </form>
    );
  }

  hideModal = () => {
    this.setState({ showModal: false, selectedUser: null });
  };

  setSelectedUser = user => {
    this.setState({ selectedUser: user, showModal: false });
  };

  submit = values => {
    const { saveCustomer, router, form, addNotification } = this.props;
    this.setState({ isSubmitting: true });

    saveCustomer(
      form,
      values,
      result => {
        this.setState({ isSubmitting: false });
        addNotification({
          title: 'New customer has been created',
          message: `<a href="/customers/${result.id}">${result.name}</a> has been created.`,
          status: 'success',
          allowHTML: true,
          dismissible: false,
          dismissAfter: 5000,
          buttons: [
            {
              name: 'OK',
              primary: true,
            },
          ],
        });

        this.props.saveRole(this.state.selectedUser.id, {
          roleName: 'CUSTOMER_ADMIN',
          customerId: result.payload.id,
          userId: this.state.selectedUser.id,
        });
        router.replace('/customers');
      },
      result => {
        this.setState({ isSubmitting: false });
        addNotification({
          title: 'Error occurred',
          message: `Error occured while creating new customer`,
          status: 'error',
          dismissible: true,
          dismissAfter: 5000,
          buttons: [
            {
              name: 'OK',
              primary: true,
            },
          ],
        });
      },
    );
  };
}

const mapStateToProps = state => {
  const selector = formValueSelector('new-customer-info');
  const selectedCountry = selector(state, 'country');

  return { selectedCountry };
};
const mapDispatchToProps = {
  saveCustomer,
  reset,
  saveCustomerAdmin,
  addNotification,
  saveRole,
};

const innerForm = withRouter(
  reduxForm({ form: 'new-customer-info' })(NewCustomerForm),
);

export default connect(mapStateToProps, mapDispatchToProps)(innerForm);
