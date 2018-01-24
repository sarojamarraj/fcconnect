import React, { Component } from 'react';
import Select from 'react-select';

class AgentAutoSuggest extends Component {
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
      useAgentId = false,
    } = this.props;
    if (useAgentId) {
      return (
        <Select.Async
          name={name}
          value={value}
          labelKey="name"
          valueKey="id"
          filterOption={() => true}
          loadOptions={loadOptionsv2}
          onChange={value => {
            this.setState({ value });
            onChange(value);
          }}
          cache={false}
          valueRenderer={option => option.name}
          optionRenderer={option => option.name}
          ref={inputRef}
        />
      );
    }
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
          return name || option.login;
        }}
        optionRenderer={option => {
          return `(${option.login}) ${option.firstname || ''} ${option.lastname || ''}`;
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
  const { name, value, onChange } = props;

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

        return name || option.login;
      }}
      optionRenderer={option => {
        return `(${option.login}) ${option.firstname || ''} ${option.lastname || ''}`;
      }}
    />
  );
};

function loadOptions(input) {
  if (input) {
    const encoded = encodeURIComponent(input);

    return fetch(`/api/user?role=agent&name=${encoded}`, {
      credentials: 'include',
      method: 'GET',
    })
      .then(response => {
        return response.json();
      })
      .then(json => {
        return { options: json._embedded.user || [] };
      });
  } else {
    return new Promise((resolve, reject) => {
      resolve({ options: [] });
    });
  }
}

function loadOptionsv2(input) {
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
  } else {
    return new Promise((resolve, reject) => {
      resolve({ options: [] });
    });
  }
}

export default AgentAutoSuggest;
