import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { formValueSelector, change, Field } from 'redux-form';

import { removeMultipleFromArray } from '../../actions/form';
import {
  loadPalletTemplates,
  savePalletTemplates,
  loadPalletTypes,
} from '../../actions/orders';
import { orderPalletTemplates, selectPalletTypes } from '../../reducers';

import InputText from '../forms/inputs/InputText';
import InputSelect from '../forms/inputs/InputSelect';
import { inputMaskNumber, inputMaskAlphaNumeric } from '../../utils';
import SavePalletTemplateModal from './SavePalletTemplateModal';

class Pallet extends Component {
  static propTypes = {
    name: PropTypes.string.isRequired,
    templates: PropTypes.array.isRequired,
  };

  constructor(props) {
    super(props);
    this.selectedItems = [];
    this.selectedTemplate = {};

    //Hacky way of adding focus to the row...
    this.addRowFocus = false;
    this.refsArrayOfFields = {};

    this.selectTemplate = this.selectTemplate.bind(this);
    this.selectItem = this.selectItem.bind(this);
    this.deleteSelectedItems = this.deleteSelectedItems.bind(this);
    this.getItemValues = this.getItemValues.bind(this);
    this.renderRow = this.renderRow.bind(this);

    this.state = {
      showSaveTemplate: false,
    };
  }

  componentWillMount() {
    this.props.loadPalletTypes();
  }

  componentDidMount() {
    // this adds a default field
    if (!this.props.values || this.props.values.length <= 0) {
      this.props.fields.push(this.selectedTemplate);
    }
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.customer && !this.props.customer) {
      this.props.loadPalletTemplates(nextProps.customer);
    }
  }

  componentDidUpdate() {
    if (!this.props.values || this.props.values.length <= 0) {
      this.props.fields.push(this.seletedTemplate);
    }

    if (this.addRowFocus) {
      this.refsArrayOfFields[Object.keys(this.refsArrayOfFields).length - 1]
        .getRenderedComponent()
        .focus();
      this.addRowFocus = false;
    }
  }

  calculateDensity(e) {
    let length = this.props.values[e.target.dataset.index].length;
    let width = this.props.values[e.target.dataset.index].width;
    let height = this.props.values[e.target.dataset.index].height;
    let weight = this.props.values[e.target.dataset.index].weight;
    let density = 0;

    if (length && width && height && weight) {
      density = weight / (length * width * height / 1728);
      this.props.change(
        this.props.formName,
        `pallets[${e.target.dataset.index}].freightclass`,
        this.nearestOption(density),
      );
    }
  }

  nearestOption(x) {
    switch (true) {
      case x >= 50:
        return 50;
      case x >= 35:
        return 55;
      case x >= 30:
        return 60;
      case x >= 22:
        return 65;
      case x >= 15:
        return 70;
      case x >= 13:
        return 77.5;
      case x >= 12:
        return 85;
      case x >= 10:
        return 92.5;
      case x >= 9:
        return 100;
      case x >= 8:
        return 110;
      case x >= 7:
        return 125;
      case x >= 6:
        return 150;
      case x >= 5:
        return 175;
      case x >= 4:
        return 200;
      case x >= 3:
        return 250;
      case x >= 2:
        return 300;
      case x >= 1:
        return 400;
      default:
        return 500;
    }
  }

  selectItem(item) {
    this.selectedItems.push(item);
  }

  deleteSelectedItems() {
    this.props.removeMultipleFromArray(
      this.props.formName,
      this.props.name,
      this.selectedItems,
    );
    this.selectedItems = [];
  }

  getItemValues(index) {
    //NOTE:	No way to extract value from the FieldArray
    //		using index. Need to subscribe the value to
    //		the store.
    return this.props.values[index];
  }

  selectTemplate(e) {
    this.selectedTemplate = e.target.value !== 'blank'
      ? this.props.templates[e.target.value]
      : {};
  }

  renderTemplates(item, index) {
    return (
      <option key={index} value={index}>
        {item.name}
      </option>
    );
  }

  renderRow(item, index) {
    let rowClass = index % 2 === 0 ? 'even-row' : 'odd-row';
    const { palletTypes = [] } = this.props;

    return (
      <tr key={index} className={rowClass}>
        <td style={{ minWidth: '39px' }}>
          <label className="checkbox">
            <input
              key={Math.random()}
              type="checkbox"
              name={'row-checkbox-' + index}
              onChange={e => {
                this.selectItem(index);
              }}
            />
            <i />
          </label>
        </td>

        <td>
          <div className="inline-group">
            <div className="pull-left">
              <label className="input">
                <Field
                  name={`${item}.length`}
                  type="text"
                  data-index={index}
                  style={{ width: '45px' }}
                  size="5"
                  placeholder="L"
                  component="input"
                  onBlur={e => {
                    this.calculateDensity(e);
                  }}
                  withRef
                  ref={input => (this.refsArrayOfFields[index] = input)}
                  format={inputMaskNumber}
                />
              </label>
            </div>
            <div className="pull-left" style={{ fontSize: '20px' }}>
              {' '}×{' '}
            </div>
            <div className="pull-left">
              <label className="input">
                <Field
                  name={`${item}.width`}
                  type="text"
                  data-index={index}
                  style={{ width: '45px' }}
                  size="5"
                  placeholder="W"
                  component="input"
                  onBlur={e => {
                    this.calculateDensity(e);
                  }}
                  format={inputMaskNumber}
                />
              </label>
            </div>
            <div className="pull-left" style={{ fontSize: '20px' }}>
              {' '}×{' '}
            </div>
            <div className="pull-left">
              <label className="input">
                <Field
                  name={`${item}.height`}
                  type="text"
                  data-index={index}
                  style={{ width: '45px' }}
                  size="5"
                  placeholder="H"
                  component="input"
                  onBlur={e => {
                    this.calculateDensity(e);
                  }}
                  format={inputMaskNumber}
                />
              </label>
            </div>
          </div>
        </td>
        <td>
          <label className="input">
            <Field
              name={`${item}.weight`}
              size="5"
              type="text"
              data-index={index}
              placeholder="lbs."
              component="input"
              onBlur={e => {
                this.calculateDensity(e);
              }}
              format={inputMaskNumber}
            />
          </label>
        </td>
        <td>
          <InputText
            name={`${item}.insurance`}
            size="6"
            placeholder="$"
            format={inputMaskNumber}
          />
        </td>
        <td>
          <InputSelect name={`${item}.freightclass`}>
            <option>50</option>
            <option>55</option>
            <option>60</option>
            <option>65</option>
            <option>70</option>
            <option>77.5</option>
            <option>85</option>
            <option>92.5</option>
            <option>100</option>
            <option>110</option>
            <option>125</option>
            <option>150</option>
            <option>175</option>
            <option>200</option>
            <option>250</option>
            <option>300</option>
            <option>400</option>
            <option>500</option>
          </InputSelect>
        </td>
        <td>
          <InputText
            name={`${item}.nmfcCode`}
            size="4"
            placeholder=""
            format={inputMaskAlphaNumeric}
          />
        </td>
        <td>
          <InputSelect name={`${item}.palletTypeId`}>
            {palletTypes.map(type => (
              <option key={type.id} value={type.id}>{type.name}</option>
            ))}
          </InputSelect>
        </td>
        <td>
          <InputText
            name={`${item}.pieces`}
            size="4"
            placeholder=""
            format={inputMaskNumber}
          />
        </td>
        <td>
          <InputText
            name={`${item}.description`}
            placeholder=""
            style={{ width: '100%' }}
          />
        </td>
        <td className="text-center" style={{ whiteSpace: 'nowrap' }}>
          <div
            className="btn btn-default btn-xs"
            data-toggle="tooltip"
            data-placement="left"
            title="Duplicate Row"
            onClick={() =>
              this.props.fields.push({
                ...this.getItemValues(index),
                id: null,
              })}
          >
            <span className="glyphicon glyphicon-duplicate" />
          </div>
          {Object.entries(this.getItemValues(index)).length > 0 &&
            <div
              className="btn btn-default btn-xs"
              data-toggle="tooltip"
              data-placement="left"
              title="Save Template"
              onClick={() => {
                this.setState({ showSaveTemplate: index });
              }}
            >
              <span className="glyphicon glyphicon-download" />
            </div>}
        </td>
      </tr>
    );
  }

  render() {
    return (
      <div className="table-responsive">
        <table className="table table-bordered table-striped table-condensed table-hover smart-form has-tickbox">
          <thead>
            <tr>
              <th />
              <th width="175">Dimensions (inches)</th>
              <th width="80">Weight (lbs)</th>
              <th width="110">Insurance Amount</th>
              <th width="80">Freight Class</th>
              <th width="80">NMFC Code</th>
              <th width="110">Type</th>
              <th width="80">Pieces</th>
              <th>Description</th>
              <th width="20" />
            </tr>
          </thead>
          <tfoot>
            <tr>
              <td colSpan="10" style={{ textAlign: 'right' }}>
                <div className="padding-5 clearfix">
                  <button
                    className="btn btn-danger btn-sm pull-left"
                    onClick={e => {
                      e.preventDefault();
                      this.deleteSelectedItems();
                    }}
                  >
                    <span className="glyphicon glyphicon-trash" />
                    {' '}Delete Selected Row(s)
                  </button>
                  <button
                    className="btn btn-success btn-sm pull-right"
                    style={{ marginLeft: '10px' }}
                    onClick={e => {
                      e.preventDefault();
                      e.target.blur();
                      this.props.fields.push(this.selectedTemplate);
                      this.addRowFocus = true;
                    }}
                  >
                    <span className="glyphicon glyphicon-plus" />
                    {' '}Add
                  </button>
                  <label className="select pull-right">
                    <select
                      onChange={this.selectTemplate}
                      style={{ height: '33px', width: '180px' }}
                    >
                      <option value="blank">Add Blank Row</option>
                      {this.props.templates.map(this.renderTemplates)}
                    </select>
                    <i />
                  </label>
                </div>
              </td>
            </tr>
          </tfoot>
          <tbody>
            {this.props.fields.map(this.renderRow)}
          </tbody>
        </table>
        {this.state.showSaveTemplate !== false &&
          <SavePalletTemplateModal
            show={true}
            index={this.state.showSaveTemplate}
            hideModal={e => this.setState({ showSaveTemplate: false })}
            saveTemplate={this.saveTemplate}
          />}
      </div>
    );
  }

  saveTemplate = (index, templateName) => {
    this.setState({ showSaveTemplate: false }, () => {
      this.props.savePalletTemplates(this.props.customer, {
        ...this.getItemValues(index),
        name: templateName,
      });
    });
  };
}

const mapStateToProps = (state, ownProps) => {
  const selector = formValueSelector(ownProps.formName);

  return {
    values: selector(state, ownProps.name),
    templates: orderPalletTemplates(state, ownProps.customer),
    palletTypes: selectPalletTypes(state),
  };
};

export default connect(mapStateToProps, {
  loadPalletTemplates,
  removeMultipleFromArray,
  change,
  savePalletTemplates,
  loadPalletTypes,
})(Pallet);
