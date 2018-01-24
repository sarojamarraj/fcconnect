import React, { Component } from 'react';
import { connect } from 'react-redux';
import {
  Field,
  reduxForm,
  SubmissionError,
  formValueSelector,
} from 'redux-form';
import { compose } from 'redux';
import { Button } from 'react-bootstrap';
import { update, save } from '../../actions/api.js';
import { addressBookValidation } from '../../utils';
import { states, provinces } from '../../utils';

class AddressBookForm extends Component {
  constructor(props, context) {
    super(props);
    this.submit = this.submit.bind(this);
    this.sleep = this.sleep.bind(this);
  }

  componentDidMount() {
    //console.log("MOUNTED")
    if (!this.props.initialValues) {
      //console.log("NEW RECORD")
      this.props.change('country', 'CA');
      this.props.change('province', 'AB');
    } else {
      //console.log("EXISTING RECORD")
      //console.log(this.props)
      if (this.props.initialValues && this.props.initialValues.phone) {
        this.props.change(
          'phone',
          this.formatPhone(this.props.initialValues.phone),
        );
      }
    }
  }

  formatPhone = phone => {
    return phone
      .replace(/\s/g, '')
      .replace(/(\d{3})(\d{3})(\d{4})/, '$1-$2-$3');
  };

  sleep = ms => new Promise(resolve => setTimeout(resolve, ms));

  submit(values) {
    //console.log("Submitting...")
    return this.sleep(500).then(() => {
      const errors = addressBookValidation(values);

      if (Object.getOwnPropertyNames(errors).length !== 0) {
        //console.log("ERROR CONTAINED")
        throw new SubmissionError(errors);
      }

      if (!values.id) {
        //console.log("NEW ADDRESS CREATED")
        this.props
          .createAction(values, 'address-book')
          .then(() => this.props.paginate());
      } else {
        //console.log("ADDRESS "+ values.id +" UPDATED")
        this.props.updateAction(values, values.id, 'address-book');
      }

      this.props.close();
    });
  }

  renderInputField = ({
    input,
    label,
    type,
    meta: { touched, error, warning },
  }) =>
    <div>
      <label className={touched && (error && 'state-error')}>
        <input {...input} placeholder={label} type={type} />
        {touched &&
          (error && <span className="help-block state-error">{error}</span>)}
      </label>
    </div>;

  renderSelectField = ({
    input,
    options,
    name,
    meta: { touched, error, warning },
  }) => {
    let items = [];

    for (let index in options) {
      if (options.hasOwnProperty(index)) {
        items.push(<option key={index} value={index}>{options[index]}</option>);
      }
    }
    return (
      <div>
        <label className={touched && (error && 'state-error')}>
          <select {...input}>
            {items}
          </select>
          {touched &&
            (error && <span className="help-block state-error">{error}</span>)}
        </label>
      </div>
    );
  };

  render() {
    const { submitting } = this.props;

    return (
      <form
        onSubmit={this.props.handleSubmit(this.props.onSubmit)}
        className="modalForm form-horizontal">
        <div className="row">
          <div className="col-sm-12">
            <section className="form-group">
              <label className="">Company Name</label>
              <label>
                <Field
                  name="consigneeName"
                  component={this.renderInputField}
                  type="text"
                  disabled
                />
              </label>
            </section>
          </div>
        </div>
        <div className="row">
          <div className="col-sm-6">
            <section className="form-group">
              <label className="">Address Line 1</label>
              <label>
                <Field
                  name="address1"
                  component={this.renderInputField}
                  type="text"
                />
              </label>
            </section>
          </div>
          <div className="col-sm-6">
            <label className="">Address Line 2</label>
            <label>
              <Field
                name="address2"
                component={this.renderInputField}
                type="text"
              />
            </label>
          </div>
        </div>
        <div className="row">
          <div className="col-sm-6">
            <section className="form-group">
              <label className="">City</label>
              <label>
                <Field
                  name="city"
                  component={this.renderInputField}
                  type="text"
                />
              </label>
            </section>
          </div>
          <div className="col-sm-6">
            <label className="">Postal/Zip code</label>
            <label>
              <Field
                name="postalCode"
                component={this.renderInputField}
                type="text"
              />
            </label>
          </div>
        </div>
        <div className="row">
          <div className="col-sm-6">
            <section className="form-group">
              <label className="">Province</label>
              <label className="select">
                <Field
                  name="province"
                  component={this.renderSelectField}
                  options={
                    this.props.selectedCountry
                      ? this.props.selectedCountry === 'CA' ? provinces : states
                      : provinces
                  }
                />
                <i />
              </label>
            </section>
          </div>
          <div className="col-sm-6">
            <label className="">Country</label>
            <label className="select">
              <Field
                name="country"
                component={this.renderSelectField}
                options={{ CA: 'Canada', US: 'USA' }}
              />
              <i />
            </label>
          </div>
        </div>
        <div className="row">
          <div className="col-sm-12">
            <section className="form-group">
              <label className="">Contact Name</label>
              <label>
                <Field
                  name="contactName"
                  component={this.renderInputField}
                  type="text"
                />
              </label>
            </section>
          </div>
        </div>
        <div className="row">
          <div className="col-sm-6">
            <section className="form-group">
              <label className="">Phone</label>
              <label>
                <Field
                  name="phone"
                  component={this.renderInputField}
                  type="text"
                />
              </label>
            </section>
          </div>
          <div className="col-sm-6">
            <label className="">E-mail</label>
            <label>
              <Field
                name="contactEmail"
                component={this.renderInputField}
                type="text"
              />
            </label>
          </div>
        </div>
        <div className="row">
          <div className="col-sm-6">
            <section className="form-group">
              <div className="checkbox">
                <label>
                  <Field
                    component="input"
                    name="residential"
                    type="checkbox"
                    format={value => {
                      return value ? true : false;
                    }}
                    parse={value => {
                      return value ? 1 : 0;
                    }}
                  /> {' '}
                  Residential
                </label>
              </div>
            </section>
          </div>
        </div>
        <div className="row modalForm-footer">
          <div className="col-sm-12">
            <Button bsStyle="default" onClick={this.props.close}>
              Cancel
            </Button>
            <Button bsStyle="primary" type="submit" disabled={submitting}>
              Save
            </Button>
          </div>
        </div>
      </form>
    );
  }
}

const selector = formValueSelector('address-book');

const mapStateToProps = state => {
  const selectedCountry = selector(state, 'country');
  return { selectedCountry };
};

export default compose(
  connect(mapStateToProps, {
    updateAction: (values, addressBookId, key) =>
      update('addressbook', addressBookId, values, null, key),
    createAction: (values, key) => save('addressbook', values, null, key),
  }),
  reduxForm({ form: 'address-book' }),
)(AddressBookForm);
