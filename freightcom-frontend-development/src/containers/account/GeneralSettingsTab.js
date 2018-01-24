import React, { Component } from 'react';
import { Field, change } from 'redux-form';
import { connect } from 'react-redux';
import { Row, Col } from 'react-bootstrap';
import { booleanToInt, parseAsInt, getAgentRoleOfUser } from '../../utils';
import AgentAutoSuggest from '../../components/agents/AgentAutoSuggest';
import CarrierDropdown from '../../components/carriers/CarrierDropdown';
import {
  checkRoleIfAdminOrAgent,
  selectLoggedInCustomer,
} from '../../reducers';

class GeneralSettingsTab extends Component {
  constructor(props) {
    super(props);
    this.state = {
      selectedAgent: null,
    };
  }

  render() {
    const { activeRole, isCustomer } = this.props;
    return (
      <fieldset>
        <Row className="row flex">
          <Col className="col" sm={6} xs={12}>
            <div className="jarviswidget jarviswidget-sortable jarviswidget-color-blue" data-widget-edit-button="false">
              <header role="heading">
                <h2>General</h2>
              </header>
              <div>
                <div className="widget-body">
                  <Row>
                    <FieldSection
                      activeRole={activeRole.roleName}
                      authorizedRole={[
                        'ADMIN',
                        'AGENT',
                        'CUSTOMER_ADMIN',
                        'CUSTOMER_STAFF',
                        'FREIGHTCOM_STAFF',
                      ]}
                    >
                      <label className="label">Agent</label>
                      <label className="select">
                        {!isCustomer
                          ? <Field
                              name="salesAgent"
                              component={props => {
                                const agentId =
                                  props.input.value.userId || this.state.selectedAgent;

                                return (
                                  <AgentAutoSuggest
                                    name={props.input.name}
                                    value={agentId}
                                    onChange={agent => {
                                      this.props
                                        .change(this.props.formName, 'salesAgent', {
                                          id: getAgentRoleOfUser(agent).id,
                                        })
                                        .then(data => {
                                          this.setState({ selectedAgent: agent });
                                        });
                                    }}
                                  />
                                );
                              }}
                            />
                          : <span>{this.props.agent}</span>}
                      </label>

                    </FieldSection>
                    <FieldSection
                      activeRole={activeRole.roleName}
                      authorizedRole={['ADMIN', 'AGENT', 'FREIGHTCOM_STAFF']}
                      colSize="col col-3"
                    >
                      <label className="label">Allow New Orders</label>
                      <label className="toggle pull-left">
                        <Field component="input" name="allowNewOrders" type="checkbox" />
                        <i data-swchon-text="ON" data-swchoff-text="OFF" />
                      </label>
                    </FieldSection>
                    <FieldSection
                      activeRole={activeRole.roleName}
                      authorizedRole={['ADMIN', 'AGENT', 'FREIGHTCOM_STAFF']}
                      colSize="col col-3"
                    >
                      <label className="label">Suspend all activity</label>
                      <label className="toggle pull-left">
                        <Field component="input" name="suspended" type="checkbox" />
                        <i data-swchon-text="ON" data-swchoff-text="OFF" />
                      </label>
                    </FieldSection>
                  </Row>
                </div>
              </div>
            </div>
          </Col>

          <Col className="col" sm={6} xs={12}>
            <div className="jarviswidget jarviswidget-sortable jarviswidget-color-blue" data-widget-edit-button="false">
              <header role="heading">
                <h2>Invoicing Terms</h2>
              </header>
              <div>
                <div className="widget-body" style={{paddingBottom: '0'}}>
                  <Row>
                    <FieldSection
                      activeRole={activeRole.roleName}
                      authorizedRole={['ADMIN', 'AGENT', 'FREIGHTCOM_STAFF']}
                    >
                      <label className="label">Auto-generate new invoices</label>
                      <label className="select">
                        <Field name="autoInvoice" component="select">
                          <option value="ON_BOOKING">On Booking</option>
                          <option value="ON_DELIVERY">On Delivery</option>
                          <option value="WHEN_RECONCILED">When Reconciled</option>
                          <option value="DAILY">Daily</option>
                          <option value="WEEKLY">Weekly</option>
                          <option value="BIWEEKLY">Biweekly</option>
                          <option value="MONTHLY">Monthly</option>
                          <option value="NEVER">Never</option>
                        </Field>
                        <i />
                      </label>
                    </FieldSection>
                    <FieldSection
                      activeRole={activeRole.roleName}
                      authorizedRole={['ADMIN', 'AGENT', 'FREIGHTCOM_STAFF']}
                    >
                      <label className="label">Auto-charge credit cards</label>
                      <label className="select">
                        <Field name="autoCharge" component="select">
                          <option value="NEVER">Never</option>
                          <option value="IMMEDIATELY">Immediately</option>
                          <option value="ON_DUE_DATE">On Due Date</option>
                          <option value="DAILY">Daily</option>
                          <option value="WEEKLY">Weekly</option>
                          <option value="BIWEEKLY">Biweekly</option>
                          <option value="MONTHLY">Monthly</option>
                        </Field>
                        <i />
                      </label>
                    </FieldSection>
                  </Row>
                  <br />
                  <Row>
                    <FieldSection
                      activeRole={activeRole.roleName}
                      authorizedRole={['ADMIN', 'AGENT', 'FREIGHTCOM_STAFF']}
                    >
                      <label className="label">
                        Automatically set due term to (days)
                      </label>
                      <label className="input">
                        <Field component="input" name="invoiceTerm" type="text" />
                      </label>
                    </FieldSection>
                    <FieldSection
                      activeRole={activeRole.roleName}
                      authorizedRole={['ADMIN', 'AGENT', 'FREIGHTCOM_STAFF']}
                    >
                      <label className="label">Send due notification after (days)</label>
                      <label className="input">
                        <Field component="input" name="invoiceTermWarning" type="text" />
                      </label>
                    </FieldSection>
                  </Row>
                  <Row>
                    <FieldSection
                      activeRole={activeRole.roleName}
                      authorizedRole={[
                        'ADMIN',
                        'AGENT',
                        'CUSTOMER_ADMIN',
                        'CUSTOMER_STAFF',
                        'FREIGHTCOM_STAFF',
                      ]}
                    >
                      <label className="label">Send invoices to (email)</label>
                      <label className="input">
                        <Field component="input" name="invoiceEmail" type="text" />
                      </label>
                    </FieldSection>
                    <FieldSection
                      activeRole={activeRole.roleName}
                      authorizedRole={['ADMIN', 'AGENT', 'FREIGHTCOM_STAFF']}
                    >
                      <label className="label">On past due</label>
                      <label className="select">
                        <Field
                          name="pastDueAction"
                          component="select"
                          parse={parseAsInt}
                          format={booleanToInt}
                        >
                          <option value={0}>Do nothing and alert agent</option>
                          <option value={1}>Disallow new orders and alert agent</option>
                        </Field>
                        <i />
                      </label>
                    </FieldSection>
                  </Row>
                  <Row>
                    <FieldSection
                      activeRole={activeRole.roleName}
                      authorizedRole={[
                        'ADMIN',
                        'AGENT',
                        'CUSTOMER_ADMIN',
                        'CUSTOMER_STAFF',
                        'FREIGHTCOM_STAFF',
                      ]}
                    >
                      <label className="label">Invoice Currency</label>
                      <label className="select">
                        <Field component="select" name="invoiceCurrency">
                          <option value="CAD">CAD</option>
                          <option value="USD">USD</option>
                          <option value="AUD">AUD</option>
                          <option value="SGD">SGD</option>
                          <option value="GBP">GBP</option>
                        </Field>
                        <i />
                      </label>
                    </FieldSection>
                  </Row>
                </div>
              </div>
            </div>
          </Col>
        </Row>

        <Row>
          <Col className="col" sm={12} xs={12}>
            <div className="jarviswidget jarviswidget-sortable jarviswidget-color-blue" data-widget-edit-button="false">
              <header role="heading">
                <h2>Shipping Preferences</h2>
              </header>
              <div>
                <div className="widget-body">
                  <Row>
                    <FieldSection
                      activeRole={activeRole.roleName}
                      authorizedRole={[
                        'ADMIN',
                        'AGENT',
                        'CUSTOMER_ADMIN',
                        'CUSTOMER_STAFF',
                        'FREIGHTCOM_STAFF',
                      ]}
                    >
                      <label className="label">
                        Proof of Delivery is required on each shipment
                      </label>
                      <label className="toggle pull-left">
                        <Field
                          component="input"
                          name="shippingPODRequired"
                          type="checkbox"
                        />
                        <i data-swchon-text="ON" data-swchoff-text="OFF" />
                      </label>
                    </FieldSection>
                    <FieldSection
                      activeRole={activeRole.roleName}
                      authorizedRole={[
                        'ADMIN',
                        'AGENT',
                        'CUSTOMER_ADMIN',
                        'CUSTOMER_STAFF',
                        'FREIGHTCOM_STAFF',
                      ]}
                    >
                      <label className="label">
                        NMFC Code is required on each shipment
                      </label>
                      <label className="toggle pull-left">
                        <Field
                          component="input"
                          name="shippingNMFCRequired"
                          type="checkbox"
                        />
                        <i data-swchon-text="ON" data-swchoff-text="OFF" />
                      </label>
                    </FieldSection>
                  </Row>
                  <br />
                  <Row>
                    <FieldSection
                      activeRole={activeRole.roleName}
                      authorizedRole={[
                        'ADMIN',
                        'AGENT',
                        'CUSTOMER_ADMIN',
                        'CUSTOMER_STAFF',
                        'FREIGHTCOM_STAFF',
                      ]}
                    >
                      <label className="label">Excluded Services:</label>
                      <label className="select">
                        <Field
                          name="excludedServicesIds"
                          component={props => {
                            return (
                              <CarrierDropdown
                                name={props.input.name}
                                defaultValue={
                                  Array.isArray(props.input.value)
                                    ? props.input.value.join(',')
                                    : ''
                                }
                                onChange={values => {
                                  this.props.change(
                                    this.props.formName,
                                    'excludedServiceIds',
                                    values,
                                  );

                                  this.props.change(
                                    this.props.formName,
                                    'excludedServices',
                                    null,
                                  );
                                }}
                                clearable={false}
                                multi
                                simpleValue
                              />
                            );
                          }}
                        />
                      </label>

                    </FieldSection>
                  </Row>
                </div>
              </div>
            </div>
          </Col>
        </Row>
      </fieldset>
    );
  }
}

const FieldSection = ({
  children,
  activeRole,
  authorizedRole,
  colSize = 'col col-6',
}) => {
  if (~authorizedRole.indexOf(activeRole)) {
    return <section className={colSize}>{children}</section>;
  }
  return null;
};
const mapStateToProps = state => {
  const isCustomer = !checkRoleIfAdminOrAgent(state);
  if (isCustomer) {
    const customer = selectLoggedInCustomer(state);
    const salesAgent = customer.salesAgent && customer.salesAgent.name;
    return {
      agent: salesAgent || 'Not Applicable',
      isCustomer: true,
    };
  } else {
    return {
      isCustomer: false,
    };
  }
};
const mapDispatchToProps = {
  change,
};
export default connect(mapStateToProps, mapDispatchToProps)(GeneralSettingsTab);
