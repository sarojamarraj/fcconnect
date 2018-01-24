import React, { Component } from 'react';
import { connect } from 'react-redux';

import Select from 'react-select';

import { selectCurrencies } from '../reducers';
import { loadCurrencyList } from '../actions/currencies';

class CurrencySelector extends Component {
  componentWillMount() {
    this.props.loadCurrencyList();
  }

  render() {
    const { value = 'Choose', onChange, currencies } = this.props;

    return (
      <Select
        value={value}
        onChange={value => onChange(value && value.name)}
        options={[{ name: 'Choose' }, ...currencies]}
        labelKey="name"
        valueKey="name"
      />
    );
  }
}

export default connect(
  state => ({
    currencies: selectCurrencies(state),
  }),
  {
    loadCurrencyList,
  },
)(CurrencySelector);
