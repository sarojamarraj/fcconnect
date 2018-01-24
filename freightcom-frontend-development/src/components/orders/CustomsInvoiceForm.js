import React from 'react';
import { connect } from 'react-redux';
import { FormSection, FieldArray, change } from 'redux-form';
import { Row, Col, Button } from 'react-bootstrap';
import InputText from '../forms/inputs/InputText';
import InputCheckbox from '../forms/inputs/InputCheckbox';
import InputRadio from '../forms/inputs/InputRadio';
import InputSelect from '../forms/inputs/InputSelect';

import { selectActiveTab } from '../../reducers';

import {
  fetchAddressFromPostalCode,
  formatCurrency,
  countries,
} from '../../utils';

const renderProducts = ({ fields }) => {
  return (
    <table className="table table-bordered">
      <thead>
        <tr>
          <th colSpan="7">Products</th>
        </tr>
        <tr>
          <th>Description</th>
          <th width="100">HSCODE</th>
          <th>Origin</th>
          <th width="70">Quantity</th>
          <th width="70">Unit Price</th>
          <th width="70">Amount</th>
          <th />
        </tr>
      </thead>

      <tfoot>
        <tr>
          <td colSpan="5" className="currency">
            <big>
              <b>Total</b>
            </big>
          </td>
          <td className="currency">
            <big>
              <b>
                {formatCurrency(
                  (() => {
                    const values = fields.getAll() || [];
                    return values.reduce((acc, curr) => {
                      const qty = +curr.quantity || 0;
                      const price = +curr.unitPrice || 0;
                      acc += qty * price;
                      return acc;
                    }, 0);
                  })(),
                )}
              </b>
            </big>
          </td>
          <td>
            <Button
              bsStyle="primary"
              bsSize="small"
              onClick={e => fields.push({})}
            >
              Add item
            </Button>
          </td>
        </tr>
      </tfoot>
      <tbody>
        {fields.length
          ? fields.map((product, index) => {
              return (
                <tr key={index}>
                  <td>
                    <InputText name={`${product}.description`} wrapper="div" />
                  </td>
                  <td>
                    <InputText name={`${product}.hscode`} wrapper="div" />
                  </td>
                  <td>
                    <InputSelect name={`${product}.origin`}>
                      {Object.keys(countries).map(ccode =>
                        <option key={ccode} value={ccode}>
                          {countries[ccode]}
                        </option>,
                      )}
                    </InputSelect>
                  </td>
                  <td>
                    <InputText name={`${product}.quantity`} wrapper="div" />
                  </td>
                  <td>
                    <InputText name={`${product}.unitPrice`} wrapper="div" />
                  </td>
                  <td className="currency">
                    {formatCurrency(
                      +fields.get(index).quantity *
                        +fields.get(index).unitPrice,
                    )}
                  </td>
                  <td>
                    <Button
                      bsStyle="danger"
                      bsSize="small"
                      onClick={e => fields.remove(index)}
                    >
                      Delete
                    </Button>
                  </td>
                </tr>
              );
            })
          : <tr>
              <td
                colSpan="7"
                style={{
                  textAlign: 'center',
                  fontStyle: 'italic',
                  color: '#ccc',
                }}
              >
                No item to display
              </td>
            </tr>}
      </tbody>
    </table>
  );
};

const CustomsInvoiceForm = props => {
  return (
    <Row>
      <Col className="col" sm={12} xs={12}>
        <FormSection name="customsInvoice">
          <div className="jarviswidget jarviswidget-sortable jarviswidget-color-blue">
            <header role="heading">
              <h2>Customs Invoice</h2>
            </header>
            <div>
              <div className="widget-body">
                <Row>
                  <Col className="col" sm={4}>
                    <fieldset>
                      <legend>Bill To</legend>
                      <FormSection name="billTo">
                        <InputText name="company" label="Company" />
                        <InputText name="address1" label="Address" />
                        <InputText name="address2" label="Address 2" />
                        <InputText
                          name="postalCode"
                          label="ZIP/Postal"
                          onBlur={e => {
                            fetchAddressFromPostalCode(e.target.value)
                              .then(val => {
                                if (val !== false) {
                                  props.change(
                                    props.formName,
                                    'customsInvoice.billTo.city',
                                    val.city,
                                  );
                                  props.change(
                                    props.formName,
                                    'customsInvoice.billTo.province',
                                    val.province,
                                  );
                                  props.change(
                                    props.formName,
                                    'customsInvoice.billTo.country',
                                    val.country,
                                  );
                                }
                              })
                              .catch(err => console.error('Invalid Postal/ZIP Code'));
                          }}
                        />
                        <InputText name="city" label="City" />
                        <InputText name="province" label="Province/State" />
                        <InputText name="country" label="Country" />
                      </FormSection>
                    </fieldset>
                  </Col>
                  <Col className="col" sm={4}>
                    <fieldset>
                      <legend>Duties and Taxes Bill To</legend>
                      <InputCheckbox name="dutiable" label="Dutiable" />
                      <InputRadio name="bill" value="receiver" label="Receiver" />
                      <InputRadio name="bill" value="shipper" label="Shipper" />
                      <InputRadio name="bill" value="consignee" label="Consignee" />
                      <section>
                        <InputText name="consigneeAccount" label="Consignee Account" />
                        <InputText name="sedNumber" label="SED Number" />
                      </section>
                    </fieldset>
                  </Col>
                  <Col className="col" sm={4}>
                    <legend>Please Note</legend>
                    <br />
                    <p>
                      If your shipment has a declared value of greater than $2,000 CAD
                      and is being shipped to a country other than the United States,
                      Puerto Rico or the U.S. Virgin Islands, Revenue Canada requires
                      you to complete a B13A Export Declaration. To download the manual
                      B13A form click here..
                    </p>
                    <p>
                      <a
                        href="http://www.cbsa-asfc.gc.ca/publications/forms-formulaires/b13a.pdf"
                        target="_new"
                      >
                        http://www.cbsa-asfc.gc.ca/publications/forms-formulaires/b13a.pdf
                      </a>
                    </p>
                    <p>
                      To submit the B13A form electronically to Revenue Canada click
                      here. (recommended)
                    </p>
                    <p>
                      <a
                        href="http://www.statcan.gc.ca/exp/index-eng.htm"
                        target="_new"
                      >
                        http://www.statcan.gc.ca/exp/index-eng.htm
                      </a>
                    </p>
                  </Col>
                </Row>
                <Row>
                  <Col className="col" sm={12} xs={12}>
                    <FieldArray name="products" component={renderProducts} />
                  </Col>
                </Row>

                <Row>
                  <Col className="col" sm={12} xs={12}>
                    <FormSection name="contactInfo">
                      <legend>For any questions please contact</legend>
                      <br />
                      <Row>
                        <Col className="col" sm={4}>
                          <InputText name="company" label="Company" />
                          <InputText name="name" label="Name" />
                        </Col>
                        <Col className="col" sm={4}>
                          <InputText name="brokerName" label="Broker Name" />
                          <InputText name="taxId" label="Tax ID" />
                        </Col>
                        <Col className="col" sm={4}>
                          <InputText name="phone" label="Phone" />
                          <InputText name="receiptsTaxId" label="Receipts Tax ID" />
                        </Col>
                      </Row>
                    </FormSection>
                  </Col>
                </Row>
              </div>
            </div>
          </div>
        </FormSection>
      </Col>
    </Row>
  );
};

const mapStateToProps = state => {
  return {
    formName: selectActiveTab(state).id,
  };
};

export default connect(mapStateToProps, { change })(CustomsInvoiceForm);
