import React, { Component } from 'react';
import { connect } from 'react-redux';
import Select from 'react-select';

import { loadCarriers } from '../../actions/carriers';
import { selectAllFromEntities } from '../../reducers';

class CarrierDropdown extends Component {
  constructor(props) {
    super(props);
    this.state = {
      selectedValue: '',
    };
  }

  componentWillMount() {
    this.props.loadCarriers(1, 99999);
    if (this.props.defaultValue) {
      this.setState({ selectedValue: this.props.defaultValue });
    }
  }

  render() {
    const {
      name,
      carriers,
      onChange,
      loadCarriers,
      ...selectProps,
    } = this.props;
    return (
      <Select
        name={name}
        value={this.state.selectedValue}
        options={carriers}
        labelKey="name"
        valueKey="id"
        onChange={val => {
          onChange(val);
          this.setState({
            selectedValue: val,
          });
        }}
        {...selectProps}
      />
    );
  }
}

const mapStateToProps = state => {
  return {
    carriers: selectAllFromEntities(state, 'carrier'),
  };
};

const mapDispatchToProps = {
  loadCarriers,
};

export default connect(mapStateToProps, mapDispatchToProps)(CarrierDropdown);
