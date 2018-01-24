import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import Select from 'react-select';

import { autoSuggestCustomers } from '../../actions/customers';

class CustomerAutoSuggest extends Component {
  static propTypes = {
    id: PropTypes.string.isRequired,
    onSelection: PropTypes.func.isRequired,
  };

  constructor(props) {
    super(props);
    this.state = { autoSuggestValue: '' };
    this.select = {};
  }

  componentWillReceiveProps(nextProps) {
    const { id } = nextProps.value;
    this.setState({ autoSuggestValue: id });
  }

  loadCustomersAutoSuggest = input => {
    this.props.autoSuggestCustomers(this.props.id, input);
  };

  selectValue = selected => {
    this.setState({ autoSuggestValue: selected });
    if (selected) {
      this.props.onSelection(this.props.customer[selected.value]);
    } else {
      this.props.onSelection({});
    }
  };

  render() {
    return (
      <Select
        {...this.props}
        ref={select => (this.select = select)}
        name="select-customers"
        value={this.state.autoSuggestValue}
        onChange={this.selectValue}
        options={this.props.customersAutoSuggestOptions}
        isLoading={this.props.customersAutoSuggestIsLoading}
        autoload={false}
        cache={false}
        filterOption={() => true}
        onInputChange={this.loadCustomersAutoSuggest}
      />
    );
  }
}

const mapStateToProps = (state, ownProps) => {
  const { entities: { customer }, autoSuggest } = state;

  if (typeof autoSuggest[ownProps.id] !== 'object') {
    return {
      customersAutoSuggestOptions: [],
      customersAutoSuggestIsLoading: false,
    };
  }
  const customersAutoSuggestOptions = autoSuggest[ownProps.id].ids
    .map(id => customer[id])
    .map(entry => {
      return { label: entry.name, value: entry.id };
    });
  const customersAutoSuggestIsLoading = autoSuggest[ownProps.id].isFetching;

  return {
    customer,
    customersAutoSuggestOptions,
    customersAutoSuggestIsLoading,
  };
};

export default connect(mapStateToProps, { autoSuggestCustomers })(
  CustomerAutoSuggest,
);
