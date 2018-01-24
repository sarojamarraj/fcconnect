import React, { Component } from 'react';
import Select from 'react-select';

class CarrierAutoSuggest extends Component {
  constructor(props) {
    super(props);
    this.state = {
      value: props.value || props.defaultValue || '',
    };
  }

  render() {
    const {
      name,
      value = this.state.value,
      onChange,
      inputRef = '',
    } = this.props;

    return (
      <Select.Async
        name={name}
        value={value}
        label="name"
        valueKey="id"
        filterOption={() => true}
        loadOptions={loadOptions}
        onChange={value => {
          this.setState({ value });
          onChange(value);
        }}
        cache={false}
        valueRenderer={option => {
          return option.name;
        }}
        optionRenderer={option => {
          return option.name;
        }}
        ref={inputRef}
      />
    );
  }

  setValue = value => {
    this.setState({ value });
  };
}

function loadOptions(input) {
  return fetch(`/api/carrier?name=${input}&sort=name,asc`, {
    credentials: 'include',
    method: 'GET',
  })
    .then(response => response.json())
    .then(json => ({ options: json._embedded.carrier || [] }));
}

export default CarrierAutoSuggest;
