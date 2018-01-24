import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { formValueSelector, Field } from 'redux-form';
//import { OverlayTrigger, Tooltip, Glyphicon } from 'react-bootstrap';

import { removeMultipleFromArray } from '../../actions/form';
import { loadPackageTemplates } from '../../actions/orders';
import { selectAllFromEntities } from '../../reducers';

import InputText from '../forms/inputs/InputText';
//import InputSelect from '../forms/inputs/InputSelect';
import { inputMaskNumber } from '../../utils';
class Package extends Component {
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
  }

  componentWillMount() {
    this.props.loadPackageTemplates();
  }

  componentDidMount() {
    if (!this.props.values || this.props.values.length <= 0) {
      this.props.fields.push({});
    }
  }

  componentDidUpdate() {
    if (!this.props.values || this.props.values.length <= 0) {
      this.props.fields.push({});
    }

    if (this.addRowFocus) {
      this.refsArrayOfFields[Object.keys(this.refsArrayOfFields).length - 1]
        .getRenderedComponent()
        .focus();
      this.addRowFocus = false;
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
    //console.log(this.props.templates);
    this.selectedTemplate = e.target.value !== 'blank'
      ? this.props.templates[e.target.value].values
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
                  component="input"
                  type="text"
                  name={`${item}.length`}
                  style={{ width: '45px' }}
                  size="5"
                  placeholder="L"
                  format={inputMaskNumber}
                  withRef
                  ref={input => (this.refsArrayOfFields[index] = input)}
                />
              </label>
            </div>
            <div className="pull-left" style={{ fontSize: '20px' }}>
              {' '}×{' '}
            </div>
            <div className="pull-left">
              <label className="input">
                <Field
                  component="input"
                  type="text"
                  name={`${item}.width`}
                  style={{ width: '45px' }}
                  size="5"
                  placeholder="W"
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
                  component="input"
                  type="text"
                  name={`${item}.height`}
                  style={{ width: '45px' }}
                  size="5"
                  placeholder="H"
                  format={inputMaskNumber}
                />
              </label>
            </div>
          </div>
        </td>
        <td>
          <InputText
            name={`${item}.weight`}
            size="5"
            placeholder="lbs."
            format={inputMaskNumber}
          />
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
          <InputText
            name={`${item}.description`}
            placeholder=""
            style={{ width: '100%' }}
          />
        </td>
        <td className="text-center">
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
              <th width="20" />
              <th width="175">Dimensions (inches)</th>
              <th width="80">Weight (lbs)</th>
              <th width="110">Insurance Amount</th>
              <th>Description</th>
              <th width="20" />
            </tr>
          </thead>
          <tfoot>
            <tr>
              <td colSpan="6" style={{ textAlign: 'right' }}>
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
      </div>
    );
  }
}
const mapStateToProps = (state, ownProps) => {
  const selector = formValueSelector(ownProps.formName);
  return {
    values: selector(state, ownProps.name),
    templates: selectAllFromEntities(state, 'packageTemplate'),
  };
};

export default connect(mapStateToProps, {
  loadPackageTemplates,
  removeMultipleFromArray,
})(Package);
