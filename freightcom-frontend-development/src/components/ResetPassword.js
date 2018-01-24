import React from 'react';
import { compose } from 'redux';
import { connect } from 'react-redux';
import { reduxForm, Field } from 'redux-form';

class ResetPassword extends React.Component {
  static propTypes = {};

  render() {
    const handleSubmit = this.props.handleSubmit;

    return (
      <div className="form-group reset-password-group">
        <div className="row">
          <div className="col-sm-12">
            <label>Let us send a new temporary password to your inbox:</label>
          </div>
        </div>
        <br />
        <form
          className="smart-form client-form reset-password-form"
          onSubmit={handleSubmit(this.props.resetSubmit)}>
          <section>
            <label className="">Enter your email address</label>
            <label className="input">
              {' '}<i className="icon-append fa fa-envelope" />
              <Field component="input" type="email" name="email" />
              <b className="tooltip tooltip-top-right">
                <i className="fa fa-envelope txt-color-teal" /> Please enter
                email address for password reset
              </b>
            </label>
          </section>
          {this.props.message
            ? <div className="alert alert-danger">{this.props.message}</div>
            : ''}
        </form>

      </div>
    );
  }
}

const mapStateToProps = state => {
  return {
    message: state.loggedIn.reset_message || state.loggedIn.message,
  };
};

export default compose(
  connect(mapStateToProps),
  reduxForm({ form: 'reset-password' }),
)(ResetPassword);
