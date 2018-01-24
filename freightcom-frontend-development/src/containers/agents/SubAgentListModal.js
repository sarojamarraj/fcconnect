import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Modal, Row, Col } from 'react-bootstrap';
import Button from 'react-bootstrap-button-loader';
import { loadSubAgents } from '../../actions/agents';
import { selectSubAgents } from '../../reducers';

class SubAgentListModal extends Component {
  componentWillMount() {
    this.props.loadSubAgents(this.props.agent.id);
  }
  render() {
    return (
      <Modal show={this.props.show} onHide={this.props.hide} bsSize="large">
        <Modal.Header closeButton>
          <Modal.Title>Sub Agents under {this.props.agent.name}</Modal.Title>
        </Modal.Header>
        <Modal.Body className="smart-form">
          <Row>
            <Col className="col" sm={12}>
              {this.renderSubAgentList()}
            </Col>
          </Row>
        </Modal.Body>
        <Modal.Footer>
          <div>
            <Button bsStyle="default" onClick={this.props.hide}>
              Close
            </Button>
          </div>
        </Modal.Footer>
      </Modal>
    );
  }

  renderSubAgentList = () => {
    const { subAgents } = this.props;
    return (
      <div className="table-responsive">
        <table className="table table-condensed table-bordered table-striped table-standout">
          <thead>
            <tr>
              <th>ID</th>
              <th>Agent Name</th>
              <th>Phone</th>
              <th># of Customers</th>
              <th>Total Commission</th>
              <th>Unreported Commission</th>
            </tr>
          </thead>
          <tbody>
            {subAgents.map(agent => {
              return (
                <tr key={agent.id}>
                  <td>{agent.id}</td>
                  <td>{agent.name}</td>
                  <td>{agent.phone}</td>
                  <td>{agent.customerCount}</td>
                  <td>{agent.commissionPercent}</td>
                  <td className="currency">{agent.unpaidCommission}</td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    );
  };
}

const mapStateToProps = (state, ownProps) => {
  const subAgents = selectSubAgents(state, ownProps.agent.id);
  return {
    subAgents,
  };
};

const mapDispatchToProps = { loadSubAgents };

export default connect(mapStateToProps, mapDispatchToProps)(SubAgentListModal);
