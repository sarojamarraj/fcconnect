import React, { Component } from 'react';
import { connect } from 'react-redux';
import { FieldArray, change, getFormValues } from 'redux-form';
import { Row, Col } from 'react-bootstrap';
import formatDate from 'date-fns/format';

import { selectAllFromEntities, selectCurrencies } from '../../reducers';

import Packages from './Packages';
import Pallets from './Pallets';
import ClassDensityCalculator from './ClassDensityCalculator';
import AccessorialServices from './AccessorialServices';

import InputText from '../forms/inputs/InputText';
import InputSelect from '../forms/inputs/InputSelect';
import InputDate from '../forms/inputs/InputDate';

import { SHIPMENT_TYPES } from '../../constants';

class ShipmentDetails extends Component {
  render() {
    return (
      <div
        className="jarviswidget jarviswidget-sortable jarviswidget-color-blue"
        data-widget-edit-button="false"
      >
        <header role="heading">
          <h2>Shipment Details</h2>
        </header>
        <div>
          <div className="widget-body">
            <Row>
              <Col className="col" sm={6} xs={12}>
                <section>
                  <label className="label shipmentType">Shipment Type</label>
                  <div className="inline-group shipmentType">
                    {this.props.shipmentTypes.map(
                      this.renderShipmentTypeOptions,
                    )}
                  </div>
                </section>
              </Col>
              <Col className="col" sm={3} xs={12}>
                <InputText name="referenceCode" label="Reference Number " />
              </Col>
              <Col className="col" sm={3} xs={12}>
                <InputDate
                  name="shipDate"
                  dateFormat="yy-mm-dd"
                  label="Ship Date"
                  minDate={new Date()}
                  defaultValue={formatDate(new Date(), 'YYYY-MM-DD')}
                  required
                />
              </Col>
            </Row>
            {this.props.packageTypeName === SHIPMENT_TYPES.ENVELOPE ||
              this.props.packageTypeName === SHIPMENT_TYPES.PAK ||
              this.props.packageTypeName === SHIPMENT_TYPES.PACKAGE ||
              this.props.packageTypeName === SHIPMENT_TYPES.LTL
              ? <div>
                  <section>
                    {this.renderDimensionsAndWeightInput(
                      this.props.packageTypeName,
                    )}
                  </section>
                  <section>
                    <Row>
                      {this.props.packageTypeName === SHIPMENT_TYPES.PAK
                        ? <Col className="col" sm={3}>
                            <InputSelect name="weight" label="Weight (lbs)">
                              <option>1</option>
                              <option>2</option>
                              <option>3</option>
                              <option>4</option>
                              <option>5</option>
                              <option>6</option>
                              <option>7</option>
                              <option>8</option>
                              <option>9</option>
                            </InputSelect>
                          </Col>
                        : ''}
                      {this.props.packageTypeName === SHIPMENT_TYPES.ENVELOPE ||
                        this.props.packageTypeName === SHIPMENT_TYPES.PAK ||
                        this.props.packageTypeName === SHIPMENT_TYPES.PACKAGE ||
                        this.props.packageTypeName === SHIPMENT_TYPES.LTL
                        ? <Col className="col" sm={3}>
                            <InputSelect
                              name="dangerousGoods"
                              label="Dangerous Goods"
                            >
                              <option>None</option>
                              <option>Limited Quantity</option>
                              <option>500 kg Exemption</option>
                              <option>Fully Regulated</option>
                            </InputSelect>
                          </Col>
                        : ''}
                      {this.props.packageTypeName === SHIPMENT_TYPES.ENVELOPE ||
                        this.props.packageTypeName === SHIPMENT_TYPES.PAK ||
                        this.props.packageTypeName === SHIPMENT_TYPES.PACKAGE
                        ? <Col className="col" sm={3}>
                            <InputSelect
                              name="signatureRequired"
                              label="Signature Required"
                            >
                              <option>No</option>
                              <option>Yes</option>
                            </InputSelect>
                          </Col>
                        : ''}
                      {this.props.packageTypeName === SHIPMENT_TYPES.LTL
                        ? <Col className="col" sm={3}>
                            <InputSelect name="isStackable" label="Stackable">
                              <option>Yes</option>
                              <option>No</option>
                            </InputSelect>
                          </Col>
                        : ''}
                      {this.props.packageTypeName === SHIPMENT_TYPES.PACKAGE ||
                        this.props.packageTypeName === SHIPMENT_TYPES.LTL
                        ? <Col className="col" sm={3}>
                            <InputSelect
                              name="insuranceType"
                              label="Insurance Type"
                            >
                              <option value="1">Freightcom</option>
                              <option value="2">Carriers Insurance</option>
                              <option value="0">Declined</option>
                            </InputSelect>
                          </Col>
                        : ''}
                      {this.props.packageTypeName === SHIPMENT_TYPES.PACKAGE ||
                        this.props.packageTypeName === SHIPMENT_TYPES.LTL
                        ? <Col className="col" sm={3}>
                            <InputSelect
                              name="insuranceCurrency"
                              label="Insurance Currency"
                            >
                              {this.props.currencies.map(item => (
                                <option key={item.id} value={item.id}>
                                  {item.name}
                                </option>
                              ))}
                            </InputSelect>
                          </Col>
                        : ''}
                    </Row>
                  </section>
                  <section>
                    <AccessorialServices
                      name="accessorialServices"
                      type={this.props.packageTypeName}
                    />
                  </section>
                </div>
              : ''}

          </div>
        </div>
      </div>
    );
  }

  changePackageType = e => {
    this.shipmentType = e.currentTarget.value;

    this.props.dispatch(
      change(this.props.formName, 'packageTypeName', this.shipmentType),
    );
    // this.setState(
    //   { packageTypeName: this.shipmentType },
    //   () =>
    //     this.props.dispatch(
    //       change(this.props.formName, 'packageTypeName', this.shipmentType),
    //     ),
    // );
  };

  renderDimensionsAndWeightInput = packageTypeName => {
    switch (packageTypeName) {
      case SHIPMENT_TYPES.PACKAGE:
        return (
          <div>
            <FieldArray
              formName={this.props.formName}
              name="packages"
              props={{ name: 'packages' }}
              customer={this.props.customer}
              component={Packages}
            />
          </div>
        );
      case SHIPMENT_TYPES.LTL:
        return (
          <div>
            <FieldArray
              formName={this.props.formName}
              name="pallets"
              props={{ name: 'pallets' }}
              customer={this.props.customer}
              component={Pallets}
            />
            <div style={{ display: 'none' }}>
              <ClassDensityCalculator />
            </div>
          </div>
        );
      default:
        return;
    }
  };

  renderAdditionalServices = (item, index) => {
    return (
      <div key={'additional-services-' + index} className="col-sm-3">
        <label className="wo-checkbox">
          <input type="checkbox" />
          {item}
          {item.indexOf('Tailgate') !== -1
            ? <span
                className="glyphicon glyphicon-info-sign"
                data-toggle="tooltip"
                title="Help info"
              />
            : ''}
        </label>
      </div>
    );
  };

  renderShipmentTypeOptions = item => {
    return (
      <label key={item.id} className="radio">
        <input
          type="radio"
          name="packageType"
          value={item.name}
          onChange={e => this.changePackageType(e)}
          checked={this.props.packageTypeName === item.name}
        />
        <i /> {item.description}
      </label>
    );
  };
}

const mapStateToProps = state => {
  const formName = state.tabs.activeTab;
  const formValues = getFormValues(formName)(state);
  const { packageTypeName = '' } = formValues || {};

  return {
    formName,
    shipmentTypes: selectAllFromEntities(state, 'packageType'),
    packageTypeName,
    currencies: selectCurrencies(state),
  };
};

export default connect(mapStateToProps)(ShipmentDetails);
