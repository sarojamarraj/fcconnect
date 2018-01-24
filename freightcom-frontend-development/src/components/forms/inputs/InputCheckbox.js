import React from 'react';
import { Field } from 'redux-form';

const InputCheckbox = ({ name, label }) => {
  return (
    <label className="checkbox">
      <Field name={name} component="input" type="checkbox" />
      <i />
      <span>{label}</span>
    </label>
  );
};

export default InputCheckbox;
