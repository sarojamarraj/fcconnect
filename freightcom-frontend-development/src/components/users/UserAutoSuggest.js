import React, { Component } from 'react';
import Select from 'react-select';

class UserAutoSuggest extends Component {
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
        labelKey="login"
        valueKey="id"
        filterOption={() => true}
        loadOptions={loadOptions}
        onChange={value => {
          this.setState({ value });
          onChange(value);
        }}
        cache={false}
        valueRenderer={option => {
          const name = `${option.firstname} ${option.lastname}`.trim();
          return name;
        }}
        optionRenderer={option => {
          return `${option.firstname || ''} ${option.lastname || ''}`;
        }}
        ref={inputRef}
      />
    );
  }

  setValue = value => {
    this.setState({ value });
  };
}

export const AgentAutoSuggestV1 = props => {
  const {
    name,
    value,
    onChange,
  } = props;

  return (
    <Select.Async
      name={name}
      value={value}
      labelKey="login"
      valueKey="id"
      filterOption={() => true}
      loadOptions={loadOptions}
      onChange={onChange}
      cache={false}
      valueRenderer={option => {
        const name = `${option.firstname} ${option.lastname}`.trim();

        return name;
      }}
      optionRenderer={option => {
        return `${option.firstname || ''} ${option.lastname || ''}`;
      }}
    />
  );
};

function loadOptions(input) {
  return fetch(`/api/user?name=${input}`, {
    credentials: 'include',
    method: 'GET',
  })
    .then(response => {
      return response.json();
    })
    .then(json => {
      let options = json._embedded.user || [];
      if (!input) {
        options = [
          {
            id: 'New',
            login: '',
            firstname: 'New',
            lastname: 'User',
          },
        ].concat(options);
      }
      return {
        options: options,
      };
    });
}

export default UserAutoSuggest;
