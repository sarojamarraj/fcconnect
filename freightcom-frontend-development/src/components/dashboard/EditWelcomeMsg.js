import React, { Component } from 'react';
import { Modal } from 'react-bootstrap';
import { Field, reduxForm } from 'redux-form';
import { Button } from 'react-bootstrap';

class EditWelcomeMsg extends Component {
  render() {
    return (
      <Modal show={this.props.show} onHide={this.props.close}>
        <Modal.Header closeButton>
          <Modal.Title>Edit Welcome Message</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <form className="modalForm form-horizontal">
            <div className="row">
              <div className="col-sm-12 col-xs-12">
                <section className="form-group">
                  <label className="">Title</label>
                  <label>
                    <Field name="welcomeTitle" component="input" type="text" />
                  </label>
                </section>
              </div>
            </div>
            <div className="row">
              <div className="col-sm-12 col-xs-12">
                <section className="form-group">
                  <label className="">Body</label>
                  <label>
                    <Field
                      name="welcomeBody"
                      rows="5"
                      component="textarea"
                      style={{ resize: 'none', width: '100%' }}
                    />
                  </label>
                </section>
              </div>
            </div>
            <div className="row modalForm-footer">
              <div className="col-sm-12 col-xs-12">
                <Button onClick={this.props.close}>
                  Close
                </Button>
                <Button
                  type="button"
                  onClick={this.props.close}
                  className="btn-primary">
                  Submit
                </Button>
              </div>
            </div>
          </form>
        </Modal.Body>
      </Modal>
    );
  }
}

export default reduxForm({ form: 'edit-welcome-message' })(EditWelcomeMsg);
