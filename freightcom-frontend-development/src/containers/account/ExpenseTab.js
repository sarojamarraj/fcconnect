import React, { Component } from 'react';
import { Field } from 'redux-form';
import { booleanToInt, parseAsInt, parseAsBoolean } from '../../utils';

class ExpenseTab extends Component {
  render() {
    return (
      <fieldset>
        <section className="col col-6 hidden">
          <label className="label">Broker Name</label>
          <label className="input">
            <Field component="input" name="brokerName" type="text" />
          </label>
        </section>
        <section className="col col-6 hidden">
          <label className="label">Broker*</label>
          <label className="select">
            <Field
              name="broker"
              component="select"
              parse={parseAsInt}
              format={booleanToInt}
            >
              <option value={1}>Yes</option>
              <option value={0}>No</option>
            </Field><i />
          </label>
        </section>
        <section className="col col-6">
          <label className="label">Customer Status</label>
          <label className="select">
            <Field
              name="status"
              component="select"
              className="select"
              parse={parseAsInt}
              format={booleanToInt}
            >
              <option value={1}>Active</option>
              <option value={0}>Inactive</option>
            </Field><i />
          </label>
        </section>
        <section className="col col-6">
          <label className="label">Active</label>
          <label className="select">
            <Field
              name="active"
              component="select"
              parse={parseAsBoolean}
              format={booleanToInt}
            >
              <option value={1}>Yes</option>
              <option value={0}>No</option>
            </Field><i />
          </label>
        </section>
        <section className="col col-6 hidden">
          <label className="label">Show News?</label>
          <label className="select">
            <Field
              name="showNews"
              component="select"
              parse={parseAsInt}
              format={booleanToInt}
            >
              <option value={1}>Yes</option>
              <option value={0}>No</option>
            </Field><i />
          </label>
        </section>
        <section className="col col-6 hidden">
          <label className="label">Apply Tax</label>
          <label className="select">
            <Field
              name="applyTax"
              component="select"
              parse={parseAsInt}
              format={booleanToInt}
            >
              <option value={1}>Yes</option>
              <option value={0}>No</option>
            </Field><i />
          </label>
        </section>
        <section className="col col-6 hidden">
          <label className="label">Ref Sort Billing</label>
          <label className="select">
            <Field name="refSortBilling" component="select" parse={parseAsInt}>
              <option value={1}>Yes</option>
              <option value={0}>No</option>
            </Field><i />
          </label>
        </section>
        <section className="col col-6 hidden">
          <label className="label">Freight Class Auto Populate</label>
          <label className="select">
            <Field
              name="freightClass"
              component="select"
              parse={parseAsInt}
              format={booleanToInt}
            >
              <option value={1}>Yes</option>
              <option value={0}>No</option>
            </Field><i />
          </label>
        </section>
        <section className="col col-6 hidden">
          <label className="label">Disable Commercial Invoice</label>
          <label className="select">
            <Field
              name="disableIfUnpaidInvoices"
              component="select"
              parse={parseAsInt}
              format={booleanToInt}
            >
              <option value={1}>Yes</option>
              <option value={0}>No</option>
            </Field><i />
          </label>
        </section>
        <section className="col col-6 hidden">
          <label className="label">Operating Expense % - Couriers</label>
          <label className="input">
            <Field component="input" name="commCourierOpExpense" type="text" />
          </label>
        </section>
        <section className="col col-6 hidden">
          <label className="label">Operating Expense % - Pallets</label>
          <label className="input">
            <Field component="input" name="commPalletOpExpense" type="text" />
          </label>
        </section>
        <section className="col col-6 hidden">
          <label className="label">Operating Expense % Web Work Order</label>
          <label className="input">
            <Field component="input" name="commPalletOpExpense" type="text" />
          </label>
        </section>
        <section className="col col-6 hidden">
          <label className="label">
            Include affiliate fee in commission calculations for courier services?
          </label>
          <label className="select">
            <Field
              component="select"
              name="commCourierDeductAffiliateFee"
              parse={parseAsInt}
              format={booleanToInt}
            >
              <option value={1}>Yes</option>
              <option value={0}>No</option>
            </Field><i />
          </label>
        </section>
        <section className="col col-6 hidden">
          <label className="label">
            Include affiliate fee in commission calculations for pallet services?
          </label>
          <label className="select">
            <Field
              component="select"
              name="commPalletDeductAffiliateFee"
              parse={parseAsInt}
              format={booleanToInt}
            >
              <option value={1}>Yes</option>
              <option value={0}>No</option>
            </Field><i />
          </label>
        </section>
        <section className="col col-6 hidden">
          <label className="label">
            Include transaction fee in commission calculations for courier services?
          </label>
          <label className="select">
            <Field
              component="select"
              name="commCourierDeductTranFee"
              parse={parseAsInt}
              format={booleanToInt}
            >
              <option value={1}>Yes</option>
              <option value={0}>No</option>
            </Field><i />
          </label>
        </section>
        <section className="col col-6 hidden">
          <label className="label">
            Include transaction fee in commission calculations for pallet services?
          </label>
          <label className="select">
            <Field
              component="select"
              name="commPalletDeductTranFee"
              parse={parseAsInt}
              format={booleanToInt}
            >
              <option value={1}>Yes</option>
              <option value={0}>No</option>
            </Field><i />
          </label>
        </section>
        <section className="col col-6 hidden">
          <label className="label">Show Packaging option??</label>
          <label className="select">
            <Field
              component="select"
              name="showPackaging"
              parse={parseAsInt}
              format={booleanToInt}
            >
              <option value={1}>Yes</option>
              <option value={0}>No</option>
            </Field><i />
          </label>
        </section>
        <section className="col col-6 hidden">
          <label className="label">Apply currency exchange?</label>
          <label className="select">
            <Field
              component="select"
              name="applyCurrencyExchange"
              parse={parseAsInt}
              format={booleanToInt}
            >
              <option value={1}>Yes</option>
              <option value={0}>No</option>
            </Field><i />
          </label>
        </section>
        <section className="col col-6 hidden">
          <label className="label">Single Shipment Invoicing ?</label>
          <label className="select">
            <Field
              component="select"
              name="singleShipmentInvoicing"
              parse={parseAsInt}
              format={booleanToInt}
            >
              <option value={1}>Yes</option>
              <option value={0}>No</option>
            </Field><i />
          </label>
        </section>
        <section className="col col-6 hidden">
          <label className="label">Reference 1 Name</label>
          <label className="input">
            <Field component="input" name="reference1" type="text" />
          </label>
        </section>
        <section className="col col-6 hidden">
          <label className="label">Reference 2 Name</label>
          <label className="input">
            <Field component="input" name="reference2" type="text" />
          </label>
        </section>
        <section className="col col-6 hidden">
          <label className="label">Reference 3 Name</label>
          <label className="input">
            <Field component="input" name="reference3" type="text" />
          </label>
        </section>
        <section className="col col-6 hidden">
          <label className="label">Ref code mandatory?</label>
          <label className="select">
            <Field
              name="refCodeMandatory"
              component="select"
              parse={parseAsInt}
              format={booleanToInt}
            >
              <option value={1}>Yes</option>
              <option value={0}>No</option>
            </Field><i />
          </label>
        </section>
        <section className="col col-6 hidden">
          <label className="label">API Username</label>
          <label className="input">
            <Field component="input" name="apiUsername" type="text" />
          </label>
        </section>
        <section className="col col-6 hidden">
          <label className="label">API Password</label>
          <label className="input">
            <Field component="input" type="text" name="apiPassword" />
          </label>
        </section>
        <section className="col col-6 hidden">
          <label className="label">POD required</label>
          <label className="select">
            <Field
              name="podReqd"
              component="select"
              parse={parseAsInt}
              format={booleanToInt}
            >
              <option value={1}>Yes</option>
              <option value={0}>No</option>
            </Field><i />
          </label>
        </section>
        <section className="col col-6 hidden">
          <label className="label">Disable on Late Payments</label>
          <label className="select">
            <Field
              name="disableIfUnpaidInvoices"
              component="select"
              parse={parseAsInt}
              format={booleanToInt}
            >
              <option value={1}>Yes</option>
              <option value={0}>No</option>
            </Field><i />
          </label>
        </section>
        <section className="col col-6 hidden">
          <label className="label">Sub sales Rep</label>
          <label className="select">
            <select className="input-sm">
              <option value={0}>None</option>
              <option value={1}>Tony Blare</option>
            </select><i />
          </label>
        </section>
      </fieldset>
    );
  }
}

export default ExpenseTab;
