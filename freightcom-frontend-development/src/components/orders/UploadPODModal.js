import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Modal, Row, Col, Button } from 'react-bootstrap';

import { uploadPODFile } from '../../actions/orders';
import strings from '../../strings';

class UploadPODModal extends Component {
  constructor(props) {
    super(props);
    this.state = {
      uploadDisabled: true,
      fileDescription: '',
    };
  }

  componentWillReceiveProps(nextProps) {
    const { showModal, orderId } = nextProps;

    if (showModal && !this.props.showModal && this.props.orderId !== orderId) {
      this.setState({
        uploadDisabled: true,
        fileDescription: 'Proof of delivery',
      });
    }
  }

  render() {
    return (
      <Modal
        show={this.props.showModal}
        onHide={() => this.props.toggleModal(false)}
      >
        <Modal.Header closeButton>
          <Modal.Title>Upload POD</Modal.Title>
        </Modal.Header>
        <Modal.Body className="smart-form">

          <Row>
            <Col className="col" sm={12}>
              <input
                type="file"
                ref="fileInput"
                onChange={this.enableUploadButton}
              />
            </Col>
          </Row>
          <Row>
            <Col className="col" sm={12}>
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
          <div>
            <Button
              bsStyle="default"
              onClick={() => this.props.toggleModal(false)}
            >
              Cancel
            </Button>
            <Button
              bsStyle="primary"
              disabled={this.state.uploadDisabled}
              onClick={e => {
                this.props.uploadPODFile({
                  orderId: this.props.orderId,
                  fileInput: this.refs.fileInput,
                  fileDescription: this.state.fileDescription,
                  callback: this.props.callback,
                });
              }}
            >
              Upload
            </Button>
          </div>
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
}

const mapDispatchToProps = {
  uploadPODFile,
};

export default connect(null, mapDispatchToProps)(UploadPODModal);
