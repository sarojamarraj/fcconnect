import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Button } from 'react-bootstrap';
import { Field, reduxForm, SubmissionError } from 'redux-form';
import { compose } from 'redux';
import { getOne } from '../actions/api';
import { updateProfile } from '../actions/users';

class Preferences extends Component {
  constructor(props, context) {
    super(props);

    this.submit = this.submit.bind(this);
    this.sleep = this.sleep.bind(this);
  }

  componentWillMount() {}

  componentWillReceiveProps(nextProps) {}

  renderField = ({ input, label, type, meta: { touched, error, warning } }) => {
    return (
      <div>
        <label className={touched && (error && 'state-error')}>
          <input {...input} placeholder={label} type={type} />
          {touched &&
            (error && <span className="help-block state-error">{error}</span>)}
        </label>
      </div>
    );
  };

  sleep = ms => new Promise(resolve => setTimeout(resolve, ms));

  submit(values) {
    //console.log('submitting....')
    return this.sleep(500).then(() => {
      let errors = {};

      if (!values.email) {
        errors['email'] = 'Please enter your email address.';
      } else if (
        !/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(values.email)
      ) {
        errors['email'] = 'Please enter a valid email address.';
      }

      if (!values.firstname) {
        errors['firstname'] = 'Please enter your first name.';
      }

      if (!values.lastname) {
        errors['lastname'] = 'Please enter your last name.';
      }

      if (!values.phone) {
        errors['phone'] = 'Please enter your phone number.';
      } else if (
        !/^(\()?\d{3}(\))?(-|\s)?\d{3}(-|\s)\d{4}$/.test(values.phone)
      ) {
        errors['phone'] = 'e.g) (xxx)xxx-xxxx or xxx-xxx-xxxx.';
      }

      if (!values.cell) {
        errors['cell'] = 'Please enter your cell number.';
      } else if (
        !/^(\()?\d{3}(\))?(-|\s)?\d{3}(-|\s)\d{4}$/.test(values.cell)
      ) {
        errors['cell'] = 'e.g) (xxx)xxx-xxxx or xxx-xxx-xxxx.';
      }

      if (values.password && values.password.length < 6) {
        errors['password'] =
          'Weak password. Please enter at least 6 characters.';
      }
      if (
        values.confirmPassword &&
        values.password !== values.confirmPassword
      ) {
        errors['confirmPassword'] =
          'Please enter the same password twice for confirmation.';
      }

      if (Object.getOwnPropertyNames(errors).length > 0) {
        throw new SubmissionError(errors);
      }

      // window.alert(`You submitted:\n\n${JSON.stringify(values, null, 2)}`);
      this.props.close();
      const { userId, updateProfile } = this.props;
      const userValues = {
        id: values.id,
        login: values.login,
        firstname: values.firstname,
        lastname: values.lastname,
        email: values.email,
        phone: values.phone,
        cell: values.cell,
        password: values.password,
        enabled: values.enabled,
      };
      console.log(userValues);
      // submitAction(userValues, userId)
      updateProfile(userId, userValues);
    });
  }
  render() {
    const { submitting } = this.props;

    return (
      <form
        onSubmit={this.props.handleSubmit(this.submit)}
        className="modalForm form-horizontal">
        <div className="row">
          <div className="col-sm-6">
            <section className="form-group">
              <label className="">Username</label>
              <label>
                <Field name="login" component="input" type="text" disabled />
              </label>
            </section>
          </div>
          <div className="col-sm-6">
            <label className="">Email for password retrieval:</label>
            <label>
              <Field name="email" component={this.renderField} type="email" />
            </label>
          </div>
        </div>
        <div className="row">
          <div className="col-sm-6">
            <section className="form-group">
              <label className="">First Name</label>
              <label>
                <Field
                  name="firstname"
                  component={this.renderField}
                  type="text"
                />
              </label>
            </section>
          </div>
          <div className="col-sm-6">
            <label className="">Last Name</label>
            <label>
              <Field name="lastname" component={this.renderField} type="text" />
            </label>
          </div>
        </div>
        <div className="row">
          <div className="col-sm-6">
            <section className="form-group">
              <label className="">Phone</label>
              <label>
                <Field name="phone" component={this.renderField} type="text" />
              </label>
            </section>
          </div>
          <div className="col-sm-6">
            <label className="">Cell</label>
            <label>
              <Field name="cell" component={this.renderField} type="text" />
            </label>
          </div>
        </div>
        <div className="row">
          <div className="col-sm-6">
            <section className="form-group">
              <label className="">Change Password:</label>
              <label>
                <Field
                  name="password"
                  component={this.renderField}
                  type="password"
                />
              </label>
            </section>
          </div>
          <div className="col-sm-6">
            <label className="">Confirm Password</label>
            <label>
              <Field
                name="confirmPassword"
                component={this.renderField}
                type="password"
              />
            </label>
          </div>
        </div>
        <div className="row modalForm-footer">
          <div className="col-sm-12">
            <Button onClick={this.props.close} className="btn">
              Cancel
            </Button>
            <Button
              type="submit"
              className="btn btn-primary"
              disabled={submitting}>
              Save
            </Button>
          </div>
        </div>
      </form>
    );
  }
}

const mapStateToProps = state => {
  const initialValues = state.loggedInUser;
  const userId = state.loggedInUser.id;

  return { initialValues, userId };
};

export default compose(
  connect(mapStateToProps, {
    updateProfile,
    loadUser: id => getOne('user', id),
  }),
  reduxForm({ form: 'account-info' }),
)(Preferences);
