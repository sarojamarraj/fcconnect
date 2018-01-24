import React from 'react';
import { Field } from 'redux-form';

const Select = field => {
  const {
    input,
    children,
    onChangeListener = () => {}
  } = field;

  return (
    <label
      className={
        'select' +
          (field.meta.touched && field.meta.error ? ' state-error' : '')
      }
    >
      {field.tooltip
        ? <i className="icon-prepend fa fa-question-circle" />
        : ''}
      <select
        {...input}
        onChange={e => {
          input.onChange(e);
          onChangeListener(e);
        }}
      >
        {children}
      </select>
      <i />
      {field.tooltip
        ? <b className="tooltip tooltip-top-left">
            <i className="fa fa-warning txt-color-teal" />{field.tooltip}
          </b>
        : ''}
      {field.meta.touched &&
        (field.meta.error &&
          <span className="help-block state-error">{field.meta.error}</span>)}
    </label>
  );
};

const InputSelect = (
  {
    label = false,
    tooltip = false,
    name,
    error = false,
    onChange = () => {},
    wrapper = 'section',
    children
  }
) => {
  if (label) {
    if (wrapper === 'div') {
      return (
        <div>
          {label ? <label className="label">{label}</label> : ''}
          <Field
            name={name}
            className="input-sm"
            component={Select}
            onChangeListener={onChange}
          >
            {children}
          </Field>
        </div>
      );
    }
    return (
      <section>
        {label ? <label className="label">{label}</label> : ''}
        <Field
          name={name}
          className="input-sm"
          component={Select}
          onChangeListener={onChange}
        >
          {children}
        </Field>
      </section>
    );
  }
  return (
    <Field
      name={name}
      className="input-sm"
      component={Select}
      onChangeListener={onChange}
    >
      {children}
    </Field>
  );
};

export default InputSelect;
