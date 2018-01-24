import React, { Component } from 'react';
import { Modal, Row, Col, Button } from 'react-bootstrap';
import { connect } from 'react-redux';
import strings from '../../strings';

import { uploadOrderDocument } from '../../actions/orders';

class DocumentUploadModal extends Component {
  constructor(props) {
    super(props);
    this.state = {
      uploadDisabled: true,
      fileDescription: '',
    };
  }

  render() {
    return (
      <Modal show={this.props.showModal} onHide={this.props.toggleModal}>
        <Modal.Header closeButton>
          <Modal.Title>Upload a Document</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Row>
            <Col className="col" sm={12} xs={12}>
              <input
                type="file"
                ref="fileInput"
                onChange={this.enableUploadButton}
              />
            </Col>
          </Row>
          <Row>
            <Col className="col" sm={12} xs={12}>
              <label style={{ width: '100%' }}>
                {strings.order.uploadDescriptionPrompt}
                <textarea
                  className="form-control"
                  value={this.state.fileDescription}
                  onChange={this.formChange}
                />
              </label>
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
            disabled={this.state.uploadDisabled}
            onClick={this.submitHandler}
          >
            Upload
          </Button>
        </Modal.Footer>
      </Modal>
    );
  }

  formChange = e => {
    this.setState({ fileDescription: e.target.value });
  };

  enableUploadButton = e => {
    this.setState({ uploadDisabled: e.target.files.length === 0 });
  };

  submitHandler = e => {
    e.preventDefault();
    this.props.uploadOrderDocument({
      orderId: this.props.orderId,
      fileInput: this.refs.fileInput,
      fileDescription: this.state.fileDescription,
      callback: this.props.callback,
    });
    this.props.toggleModal(false);
  };
}

const mapDispatchToProps = {
  uploadOrderDocument,
};
export default connect(null, mapDispatchToProps)(DocumentUploadModal);
