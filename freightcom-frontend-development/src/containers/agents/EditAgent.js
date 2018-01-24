import React, { Component } from 'react';
import { compose } from 'redux';
import { connect } from 'react-redux';
import { reduxForm, Field, change } from 'redux-form';
import Widget from '../../components/Widget';
import { selectOneFromEntities } from '../../reducers';
import { loadAgentById, updateAgent } from '../../actions/agents';
import { Row, Col } from 'react-bootstrap';
import InputText from '../../components/forms/inputs/InputText';
import InputSelect from '../../components/forms/inputs/InputSelect';
import SubAgentAutoSuggest from '../../components/agents/SubAgentAutoSuggest';
import { Link } from 'react-router';
import Button from 'react-bootstrap-button-loader';
import { addNotification } from 'reapop';
import { checkLoadStatus } from '../../reducers/load-status';

class EditAgent extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isLoaded: false,
      isSubmitting: false,
    };
  }

  componentWillMount() {
    this.props.loadAgentById(this.props.agentId, true);
  }

  initializeForm = (agent, parentAgent) => {
    this.setState({ isLoaded: true }, () => {
      this.props.initialize({ ...agent, parentAgent: parentAgent });
    });
  };

  componentWillReceiveProps(nextProps) {
    const { agentId, agent, parentAgent } = nextProps;

    if (agentId !== this.props.agentId) {
      this.props.loadAgentById(agentId, true);
    } else if (
      agent !== this.props.agent &&
      agent.parentSalesAgentId &&
      parentAgent.id !== agent.parentSalesAgentId
    ) {
      this.props.loadAgentById(agent.parentSalesAgentId, true);
    } else if (agent !== this.props.agent && !agent.parentSalesAgentId) {
      this.initializeForm(agent, parentAgent);
    } else if (parentAgent !== this.props.parentAgent) {
      this.initializeForm(agent, parentAgent);
    } else if (!this.state.isLoaded) {
      this.initializeForm(agent, parentAgent);
    }
  }

  autoSuggestParent = field => {
    return <SubAgentAutoSuggest {...field.input} useAgentId={true} />;
  };

  render() {
    const pageTitle = <span>Edit Agent: <b>{this.props.agent.name}</b></span>;

    if (!this.props.agent) {
      return null;
    }

    return (
      <Widget title={pageTitle}>
        <form
          id="customer-info"
          className="smart-form"
          onSubmit={this.props.handleSubmit(this.onSubmit)}
        >
          <fieldset className="padding-10">
            <div>
              <Row>
                <Col className="col" sm={12} xs={12}>
                  <div className="jarviswidget jarviswidget-sortable jarviswidget-color-blue"
                    data-widget-edit-button="false">
                    <header role="heading">
                      <h2>Edit Agent:</h2>
                    </header>
                    <div>
                      <div classname="widget-body">
                        <Row>
                          <Col className="col" sm={6}>
                            <InputText label="Agent Name" name="name" />
                          </Col>
                        </Row>
                        <Row>
                          <Col className="col" sm={6}>
                            <section>
                              <label className="label">Parent Agent</label>
                              <label className="select">
                                <Field
                                  name="parentAgent"
                                  component={this.autoSuggestParent}
                                />
                              </label>
                            </section>
                          </Col>
                          <Col className="col" sm={6}>
                            <InputText label="% Commission" name="commissionPercent" />
                          </Col>
                        </Row>
                        <Row>
                          <Col className="col" sm={6}>
                            <InputSelect
                              label="Automatically report commissions"
                              name="term"
                            >
                              <option value="NEVER">Never</option>
                              <option value="WEEKLY">Weekly</option>
                              <option value="BIWEEKLY">Biweekly</option>
                              <option value="MONTHLY">Monthly</option>
                            </InputSelect>
                          </Col>
                          <Col className="col" sm={3}>
                            <section>
                              <label className="label">Allow New Orders</label>
                              <label className="toggle pull-left">
                                <Field
                                  component="input"
                                  name="allowNewOrders"
                                  type="checkbox"
                                />
                                <i data-swchon-text="ON" data-swchoff-text="OFF" />
                              </label>
                            </section>
                          </Col>
                          <Col className="col" sm={3}>
                            <section>
                              <label className="label">View Invoice</label>
                              <label className="toggle pull-left">
                                <Field
                                  component="input"
                                  name="viewInvoices"
                                  type="checkbox"
                                />
                                <i data-swchon-text="ON" data-swchoff-text="OFF" />
                              </label>
                            </section>
                          </Col>
                        </Row>
                      </div>
                    </div>
                  </div>
                </Col>
              </Row>
            </div>
          </fieldset>

          <footer className="form-actions">
            <Button
              type="submit"
              bsStyle="success"
              bsSize="large"
              loading={this.state.isSubmitting}
            >
              Save
            </Button>
            <Link to="/agents" className="btn btn-lg btn-primary">
              Cancel
            </Link>
          </footer>
        </form>
      </Widget>
    );
  }

  onSubmit = values => {
    this.setState({ isSubmitting: true });

    const {
      name,
      commissionPercent,
      allowNewOrders,
      viewInvoices,
      term,
      parentAgent,
    } = values;
    const formattedValues = {
      agentName: name,
      commissionPercent: commissionPercent,
      allowNewOrders: allowNewOrders,
      viewInvoices: viewInvoices,
      term: term,
      parentSalesAgentId: parentAgent.id,
    };
    this.props.updateAgent(this.props.agentId, formattedValues, result => {
      this.setState({ isSubmitting: false });
      this.props.addNotification({
        title: 'Agent updated',
        message: `Agent information was updated.`,
        status: 'success',
        allowHTML: true,
        dismissible: false,
        dismissAfter: 10000,
        buttons: [
          {
            name: 'OK',
            primary: true,
          },
        ],
      });
    });
  };
}

const mapStateToProps = (state, ownProps) => {
  const agentId = ownProps.params['id'] || 0;
  const agent = selectOneFromEntities(state, 'agent', agentId, null);

  return {
    agentId,
    agent,
    parentAgent: selectOneFromEntities(
      state,
      'agent',
      agent ? agent.parentSalesAgentId : null,
      null,
    ),
    loadStatus: checkLoadStatus(state, 'agent', agentId),
  };
};

const mapDispatchToProps = {
  loadAgentById,
  change,
  updateAgent,
  addNotification,
};

export default compose(
  connect(mapStateToProps, mapDispatchToProps),
  reduxForm({ form: 'edit-agent' }),
)(EditAgent);
