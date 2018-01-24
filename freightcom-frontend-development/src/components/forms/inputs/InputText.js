import React from 'react';
import { Field } from 'redux-form';

const renderField = field => (
  <label
    className={
      'input' + (field.meta.touched && field.meta.error ? ' state-error' : '')
    }
  >
    {field.tooltip ? <i className="icon-prepend fa fa-question-circle" /> : ''}
    <input {...field.input} type="text" className={field.className} />
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

//

const InputText = ({
  label = false,
  tooltip = false,
  error = false,
  name,
  validate = (value, allValues, props) => undefined,
  inputSize = false,
  wrapper = 'section',
  required = false,
  ...props
}) => {
  if (wrapper === 'div') {
    return (
      <div>
        {label ? <label className="label">{label}</label> : ''}
        <Field
          name={name}
          className={
            (inputSize ? inputSize : 'input-xs') +
              (required ? ' required-input' : '')
          }
          component={renderField}
          validate={validate}
          tooltip={tooltip}
          {...props}
        />
      </div>
    );
  }
  return (
    <section>
      {label ? <label className="label">{label}</label> : ''}
      <Field
        name={name}
        className={
          (inputSize ? inputSize : 'input-xs') +
            (required ? ' required-input' : '')
        }
        component={renderField}
        validate={validate}
        tooltip={tooltip}
        {...props}
      />
    </section>
  );
};

export default InputText;
