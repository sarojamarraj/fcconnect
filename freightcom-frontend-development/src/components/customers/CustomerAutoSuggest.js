import React, { Component } from 'react';
import Select from 'react-select';
import { Field } from 'redux-form';

/**
 * This component is experimental. Do not use...
 */
export class ReduxCustomerAutoSuggest extends Component {
  constructor(props, context) {
    super(props, context);
    this.state = {
      value: props.value,
    };
    this.reduxField = null;
  }
  render() {
    const { name, onChange = () => {} } = this.props;

    const dispatch = this.reduxField.context._reduxForm.dispatch;
    const change = this.reduxField.context._reduxForm.change;

    // HACK:  Having issues getting the react-select and redux-form to work.
    //        The problem is that onChange hook of react-select pass the actual
    //        value instead of the synthetic event of react which ReduxForm Field
    //        accept to trigger the change action.
    //        The current solution is to create a different invisible redux Field
    //        on call its change method to update the store when changes on the
    //        react-select is triggered.

    return (
      <div>
        <Select.Async
          name={name}
          value={this.state.value}
          labelKey="name"
          valueKey="id"
          filterOption={() => true}
          loadOptions={loadOptions}
          onChange={value => {
            this.setState({ value });
            dispatch(change(name, value.id));
            onChange(value);
          }}
          valueRenderer={option => {
            return option.name || '';
          }}
          optionRenderer={option => {
            return option.name || '';
          }}
        />
        <Field
          name={this.props.name}
          component="input"
          type="hidden"
          withRef
          ref={input => (this.reduxField = input)}
        />
      </div>
    );
  }
}

function loadOptions(input) {
  if (input) {
    const encoded = encodeURIComponent(input);

    return fetch(
      `/api/customer/search/byName?name=${encoded}&active=yes&sort=name,asc`,
      {
        credentials: 'include',
        method: 'GET',
      },
    )
      .then(response => {
        return response.json();
      })
      .then(json => {
        return { options: json._embedded.customer || [] };
      });
  } else {
    return new Promise((resolve, reject) => {
      resolve({
        options: [],
      });
    });
  }
}

class CustomerAutoSuggest extends Component {
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
      required = false,
    } = this.props;

    return (
      <Select.Async
        name={name}
        value={value}
        labelKey="name"
        valueKey="id"
        filterOption={() => true}
        loadOptions={loadOptions}
        onChange={value => {
          this.setState({ value });
          onChange(value);
        }}
        cache={false}
        valueRenderer={option => {
          return option.name || '';
        }}
        optionRenderer={option => {
          return option.name || '';
        }}
        ref={inputRef}
        className={required ? 'required-input' : ''}
      />
    );
  }

  setValue = value => {
    this.setState({ value });
  };
}

export default CustomerAutoSuggest;
