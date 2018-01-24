import React, { Component } from 'react';
import { Field } from 'redux-form';
import { states, provinces, countries } from '../../utils';

class ProfileTab extends Component {
  renderInputField = (
    { input, label, type, meta: { touched, error, warning } },
  ) =>
    {
      return (
        <label className={touched && (error && 'state-error')}>
          <input {...input} placeholder={label} type={type} />
          {
            touched &&
              (error && <span className="help-block state-error">{error}</span>)
          }
        </label>
      );
    };

  renderSelectField = (
    { input, options, name, meta: { touched, error, warning } },
  ) =>
    {
      let items = [];

      for (let index in options) {
        if (options.hasOwnProperty(index)) {
          items.push(<option key={index} value={index}>{options[index]}</option>);
        }
      }

      return (
        <label
          className={'select ' + (touched && (error && 'select state-error'))}
        >
          <select {...input}>
            {items}
          </select>
          {
            touched &&
              (error && <span className="help-block state-error">{error}</span>)
          }
        </label>
      );
    };

  render() {
    return (
      <fieldset>
        <section className="col col-6">
          <label className="label">Business Name</label>
          <label className="input">
            <Field className="required-input" component="input" name="name" type="text" />
          </label>
        </section>
        <section className="col col-6">
          <label className="label">Street Address</label>
          <label className="input">
            <Field component="input" name="address" type="text" />
          </label>
        </section>
        <section className="col col-6">
          <label className="label">City</label>
          <label className="input">
            <Field className="required-input" component="input" name="city" type="text" />
          </label>
        </section>
        <section className="col col-6">
          <label className="label">Country</label>
          <label className="select">
            <Field
              component={this.renderSelectField}
              name="country"
              options={countries}
            />
            <i />
          </label>
        </section>
        <section className="col col-6">
          <label className="label">Province/State</label>
          <label className="select">
            <Field
              component={this.renderSelectField}
              name="province"
              options={
                this.props.selectedCountry
                  ? this.props.selectedCountry === 'CA' ? provinces : states
                  : provinces
              }
            />
            <i />
          </label>
        </section>
        <section className="col col-6">
          <label className="label">Postal Code/Zip Code</label>
          <label className="input">
            <Field className="required-input" component="input" name="postalCode" type="text" />
          </label>
        </section>
        <section className="col col-6">
          <label className="label">Main Phone</label>
          <label className="input">
            <Field component="input" name="phone" type="text" />
          </label>
        </section>
      </fieldset>
    );
  }
}

export default ProfileTab;
