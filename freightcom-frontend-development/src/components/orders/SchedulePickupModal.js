import React, { Component } from 'react';
import { reduxForm, FormSection } from 'redux-form';
import { Modal, Row, Col, Button } from 'react-bootstrap';
import InputDate from '../forms/inputs/InputDate';
import InputTime from '../forms/inputs/InputTime';
import InputText from '../forms/inputs/InputText';
import InputTextArea from '../forms/inputs/InputTextArea';
import { addNotification, updateNotification } from 'reapop';
import { updateScheduledPickup } from '../../actions/orders';

class SchedulePickupModal extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isNewContactPerson: false,
    };
  }

  componentWillMount() {
    this.props.change('id', this.props.orderId);
  }

  render() {
    return (
      <Modal
        show={this.props.showModal}
        onHide={() => this.props.toggleModal(false)}
      >
        <Modal.Header closeButton>
          <Modal.Title>Schedule Pickup</Modal.Title>
        </Modal.Header>
        <form onSubmit={this.props.handleSubmit(this.onSubmit)}>
          <Modal.Body className="smart-form">
            <FormSection name="scheduledPickup">

              <Row>
                <Col className="col" sm={6}>
                  <InputDate
                    name="pickupDate"
                    label="Pickup Date"
                    dateFormat="yy-mm-dd"
                    minDate={new Date()}
                  />
                </Col>
                <Col className="col" sm={3}>
                  <InputTime
                    name="pickupReadyTime"
                    label="Pickup Ready Time"
                    defaultValue="9:00"
                  />
                </Col>
                <Col className="col" sm={3}>
                  <InputTime
                    name="pickupCloseTime"
                    label="Pickup Close Time"
                    defaultValue="17:00"
                  />
                </Col>

              </Row>
              <Row>
                <Col className="col" sm={12}>
                  <InputTextArea
                    name="pickupInstructions"
                    label="Additional Pickup Instructions"
                  />
                </Col>

              </Row>

              <Row>
                <Col className="col" sm={6}>
                  <section>
                    <label className="label">Contact Person</label>
                    <label className="checkbox">
                      <input
                        type="checkbox"
                        defaultChecked="true"
                        onChange={() =>
                          this.setState({
                            isNewContactPerson: !this.state.isNewContactPerson,
                          })}
                      />
                      <i />
                      <span> Same as ship from contact</span>
                    </label>
                  </section>
                </Col>
              </Row>
              {this.state.isNewContactPerson &&
                <Row>
                  <Col className="col" sm={4}>
                    <InputText name="contactName" label="Contact Name" />
                  </Col>
                  <Col className="col" sm={4}>
                    <InputText name="contactPhone" label="Contact Phone" />
                  </Col>
                  <Col className="col" sm={4}>
                    <InputText name="contactEmail" label="Contact Email" />
                  </Col>
                </Row>}

            </FormSection>
          </Modal.Body>
          <Modal.Footer>
            <div>
              <Button
                bsStyle="default"
                onClick={() => this.props.toggleModal(false)}
              >
                Cancel
              </Button>
              <Button bsStyle="primary" type="submit">Update</Button>
            </div>
          </Modal.Footer>
        </form>
      </Modal>
    );
  }

  onSubmit = values => {
    const notification = this.props.dispatch(
      addNotification({
        title: 'Pickup Scheduled Success',
        message: 'Dispatching pickup schedule...',
        status: 'info',
        dismissible: false,
        dismissAfter: 5000,
      }),
    );
    this.props.toggleModal(false);
    this.props.dispatch(
      updateScheduledPickup(this.props.orderId, values, data => {
        this.props.dispatch(updateNotification(notification));
      }),
    );
  };
}

export default reduxForm({
  form: 'schedulePickupOrder',
})(SchedulePickupModal);
