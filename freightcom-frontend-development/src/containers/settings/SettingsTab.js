import React, { Component } from 'react';
import { Field, reset } from 'redux-form';
import { connect } from 'react-redux';
import { reduxForm } from 'redux-form';
import { compose } from 'redux';
import { loadSettings, updateSettings } from '../../actions/settings';
import findKey from 'lodash/findKey';
import Button from 'react-bootstrap-button-loader';
import { addNotification } from 'reapop';

class SettingsTab extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isSubmitting: false,
    };
  }
  componentWillMount() {
    this.props.loadSettings();
  }
  render() {
    return (
      <form
        id="general-settings"
        className="smart-form"
        onSubmit={this.props.handleSubmit(this.submit)}>
        <fieldset>
          <section className="col col-sm-12">
            <label className="label">Welcome Message Title</label>
            <label className="input">
              <Field className="required-input" component="input" name="welcomeTitle" type="text" />
            </label>
          </section>
          <section className="col col-sm-12">
            <label className="label">Welcome Message</label>
            <label className="textarea">
              <Field className="required-input" component="textarea" name="welcomeMessage" rows="10" />
            </label>
          </section>
          <section className="col col-sm-12">
            <label className="label">Invoice Terms</label>
            <label className="textarea">
              <Field className="required-input" component="textarea" name="invoiceTerms" rows="10" />
            </label>
          </section>
          <section className="col col-sm-12">
            <label className="label">Shipment Terms</label>
            <label className="textarea">
              <Field className="required-input" component="textarea" name="shipmentTerms" rows="10" />
            </label>
          </section>
          <section className="col col-sm-12">
            <label className="label">Commission Terms</label>
            <label className="textarea">
              <Field className="required-input" component="textarea" name="commissionTerms" rows="10" />
            </label>
          </section>
        </fieldset>
        <hr className="simple" />
        <footer className="clearfix">
          <Button
            type="submit"
            className="btn btn-primary btn-lg"
            loading={this.state.isSubmitting}>
            Save
          </Button>
          <Button
            className="btn btn-default btn-lg"
            type="button"
            onClick={this.props.reset}>
            Reset
          </Button>
        </footer>
      </form>
    );
  }

  submit = values => {
    this.setState({ isSubmitting: true });
    this.props.updateSettings(
      values,
      result => {
        this.setState({ isSubmitting: false });
        this.props.addNotification({
          title: 'Settings updated',
          message: `Settings have been updated.`,
          status: 'success',
          dismissible: true,
          dismissAfter: 5000,
        });
      },
      result => {
        this.setState({ isSubmitting: false });
        this.props.addNotification({
          title: 'Setting update failure',
          message: `Error ouccured while updating settings`,
          status: 'error',
          dismissible: true,
          dismissAfter: 5000,
        });
      },
    );
  };
}

const mapStateToProps = state => {
  const initialValues = state.entities.systemProperties
    ? {
        welcomeTitle:
          state.entities.systemProperties[
            findKey(state.entities.systemProperties, { name: 'welcome_title' })
          ].data,
        welcomeMessage:
          state.entities.systemProperties[
            findKey(state.entities.systemProperties, {
              name: 'welcome_message',
            })
          ].data,
        invoiceTerms:
          state.entities.systemProperties[
            findKey(state.entities.systemProperties, { name: 'invoice_terms' })
          ].data,
        shipmentTerms:
          state.entities.systemProperties[
            findKey(state.entities.systemProperties, { name: 'shipment_terms' })
          ].data,
        commissionTerms:
          state.entities.systemProperties[
            findKey(state.entities.systemProperties, {
              name: 'commission_terms',
            })
          ].data,
      }
    : null;
  return { initialValues };
};

export default compose(
  connect(mapStateToProps, {
    loadSettings,
    reset,
    updateSettings,
    addNotification,
  }),
  reduxForm({ form: 'general-settings' }),
)(SettingsTab);
