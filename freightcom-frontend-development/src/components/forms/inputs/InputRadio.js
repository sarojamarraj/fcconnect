import React from 'react';
import { Field } from 'redux-form';

const InputRadio = props => {
  const { label = '', inline = false, name, value } = props;

  if (inline) {
    return (
      <label className="radio">
        <Field type="radio" component="input" name={name} value={value} /><i />
        {label}
      </label>
    );
  } else {
    return (
      <section>
        <label className="radio">
          <Field type="radio" component="input" name={name} value={value} />
          <i />
          {label}
        </label>

      </section>
    );
  }
};

export default InputRadio;
