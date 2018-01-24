import React, { Component } from 'react';
import { Modal, Button } from 'react-bootstrap';
import { getAgentRoleOfUser } from '../../utils';
import AgentAutoSuggest from '../../components/agents/AgentAutoSuggest';

class DeactivateAgentModal extends Component {
  constructor(props) {
    super(props);

    this.state = {
      selectedAgent: 0,
      selectedRoleAgentId: 0,
    };
  }

  render() {
    return (
      <Modal show={this.props.show}>
        <Modal.Header>
          <Modal.Title>Deactivate Agent</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          Transfer customers owned by this agent to:
          <br /><br />
          <AgentAutoSuggest
            name="agent"
            value={this.state.selectedAgent}
            onChange={agent => {
              this.setState({
                selectedAgent: agent && agent.id,
                selectedRoleAgentId: getAgentRoleOfUser(agent).id,
              });
            }}
          />
        </Modal.Body>
        <Modal.Footer>
          <Button onClick={this.props.hide}>Cancel</Button>
          <Button
            onClick={e =>
              this.props.deactivateAgent(this.state.selectedRoleAgentId)}
            bsStyle="danger"
          >
            Deactivate
          </Button>
        </Modal.Footer>
      </Modal>
    );
  }
}

export default DeactivateAgentModal;
