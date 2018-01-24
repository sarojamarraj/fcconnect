import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import Select from 'react-select';

import { autoSuggestAddressBook } from '../../actions/addressBook';

class AddressBookAutoSuggest extends Component {
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

  loadAddressBookAutoSuggest = input => {
    this.props.autoSuggestAddressBook(
      this.props.id,
      input,
      this.props.customer,
    );
  };

  selectValue = selected => {
    this.setState({ autoSuggestValue: selected });
    if (selected) {
      this.props.onSelection(this.props.addressBook[selected.value]);
    } else {
      this.props.onSelection({});
    }
  };

  render() {
    return (
      <Select
        {...this.props}
        ref={select => (this.select = select)}
        name="select-address-book"
        value={this.state.autoSuggestValue}
        onChange={this.selectValue}
        options={this.props.addressBookAutoSuggestOptions}
        isLoading={this.props.addressBookAutoSuggestIsLoading}
        autoload={false}
        cache={false}
        onBlurResetsInput={false}
        filterOption={() => true}
        onInputChange={this.loadAddressBookAutoSuggest}
      />
    );
  }
}

const mapStateToProps = (state, ownProps) => {
  const { entities: { addressBook }, autoSuggest } = state;

  if (typeof autoSuggest[ownProps.id] !== 'object') {
    return {
      addressBookAutoSuggestOptions: [],
      addressBookAutoSuggestIsLoading: false,
    };
  }
  const addressBookAutoSuggestOptions = autoSuggest[ownProps.id].ids
    .map(id => addressBook[id])
    .map(entry => {
      return { label: entry.consigneeName, value: entry.id };
    });
  const addressBookAutoSuggestIsLoading = autoSuggest[ownProps.id].isFetching;

  return {
    addressBook,
    addressBookAutoSuggestOptions,
    addressBookAutoSuggestIsLoading,
  };
};

export default connect(mapStateToProps, { autoSuggestAddressBook })(
  AddressBookAutoSuggest,
);
