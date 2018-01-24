import React, { Component } from 'react';
import { Row, Col, Modal, Button } from 'react-bootstrap';

//import { formatCurrency } from '../../utils';

import FormGroup from '../forms/FormGroup';

class WorkOrderAddChargeModal extends Component {
  constructor(props) {
    super(props);
    this.chargeData = {};
  }

  render() {
    return (
      <Modal show={this.props.showModal} onHide={this.props.toggleModal}>
        <Modal.Header closeButton>
          <Modal.Title>
            Add a Charge
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Row>
            <Col sm={4}>
              <FormGroup label="Carrier">
                <select
                  name="carrier"
                  className="form-control"
                  onChange={this.handleInputChanges}
                >
                  {this.props.carriers.map(item => (
                    <option key={item.id} value={item.id}>{item.name}</option>
                  ))}
                </select>
              </FormGroup>
            </Col>
            <Col sm={4}>
              <FormGroup label="Service">
                <select
                  name="service"
                  className="form-control"
                  onChange={this.handleInputChanges}
                >
                  {this.props.services.map(item => (
                    <option key={item.id} value={item.id}>{item.name}</option>
                  ))}
                </select>
              </FormGroup>
            </Col>
            <Col sm={2}>
              <FormGroup
                label="Charge"
                name="charge"
                type="text"
                onChange={this.handleInputChanges}
              />
            </Col>
            <Col sm={2}>
              <FormGroup
                label="Cost"
                name="cost"
                type="text"
                onChange={this.handleInputChanges}
              />
            </Col>
          </Row>
        </Modal.Body>
        <Modal.Footer>
          <Button
            bsStyle="default"
            onClick={this.props.toggleModal}
          >
            Cancel
          </Button>
          <Button
            bsStyle="primary"
            onClick={this.handleFormSubmit}
          >
            Add Charge
          </Button>
        </Modal.Footer>
      </Modal>
    );
  }

  handleInputChanges = e => {
    this.chargeData = {
      ...this.chargeData,
      [e.target.name]: e.target.value,
    };
  };

  handleFormSubmit = e => {
    console.log(this.chargeData);
    this.props.toggleModal();
  };
}

export default WorkOrderAddChargeModal;
