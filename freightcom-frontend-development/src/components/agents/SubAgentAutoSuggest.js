import React, { Component } from 'react';
import Select from 'react-select';

class SubAgentAutoSuggest extends Component {
  loadOptions = input => {
    const { value } = this.props;

    if (input) {
      const encoded = encodeURIComponent(input);

      return fetch(`/api/agent?name=${encoded}`, {
        credentials: 'include',
        method: 'GET',
      })
        .then(response => {
          return response.json();
        })
        .then(json => {
          return { options: json._embedded.agent || [] };
        });
    } else if (value) {
      return new Promise((resolve, reject) => {
        resolve({ options: [value] });
      });
    } else {
      return new Promise((resolve, reject) => {
        resolve({ options: [] });
      });
    }
  };

  render() {
    const { name, value = '', onChange, inputRef = '' } = this.props;

    return (
      <Select.Async
        name={name}
        value={value}
        labelKey="name"
        valueKey="id"
        filterOption={() => true}
        loadOptions={this.loadOptions}
        onChange={onChange}
        cache={false}
        valueRenderer={option => option.name}
        optionRenderer={option => option.name}
        ref={inputRef}
      />
    );
  }
}

export default SubAgentAutoSuggest;
