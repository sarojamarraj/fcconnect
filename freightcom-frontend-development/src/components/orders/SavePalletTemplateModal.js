import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Button, Modal } from 'react-bootstrap';

class SavePalletTemplateModal extends Component {
  constructor(props) {
    super(props);

    this.state = {
      templateName: '',
    };
  }

  saveTemplate = e => {
    if (e) {
      e.preventDefault();
    }

    this.props.saveTemplate(this.props.index, this.state.templateName);
  };

  render() {
    return (
      <Modal show={this.props.show} onHide={this.props.hideModal}>
        <form onSubmit={this.saveTemplate}>
          <Modal.Header>
            <Modal.Title>Save Pallet Template</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <label style={{ width: '100%' }}>
              Name of template<br />
              <input
                autoFocus
                type="text"
                className="col-sm-12 col-xs-12"
                value={this.state.templateName}
                onChange={e => this.setState({ templateName: e.target.value })}
              />
            </label>
          </Modal.Body>
          <Modal.Footer>
            <Button bsStyle="default" onClick={this.props.hideModal}>
              Cancel
            </Button>
            <Button bsStyle="primary" type="submit">
              Save Template
            </Button>
          </Modal.Footer>
        </form>
      </Modal>
    );
  }
}

export default connect(null, null)(SavePalletTemplateModal);
