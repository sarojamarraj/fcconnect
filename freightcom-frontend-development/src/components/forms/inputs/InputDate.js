import React, { PureComponent } from 'react';
import { Field } from 'redux-form';

export class DatePicker extends PureComponent {

  componentWillMount() {
    if (this.props.defaultValue && !this.props.input.value) {
      this.props.input.onChange(this.props.defaultValue);
    }
  }

  componentDidMount() {
    const $ = window.jQuery;
    const element = $(this.input);

    let onSelectCallbacks = [];
    const {
      minDate,
      maxDate,
      numberOfMonths,
      dateFormat,
      defaultDate,
      changeMonth,
    } = this.props;

    // Let others know about the changes to the data field
    onSelectCallbacks.push(selectedDate => {
      element.triggerHandler('change');
      let form = element.closest('form');

      if (typeof form.bootstrapValidator === 'function') {
        try {
          form.bootstrapValidator('revalidateField', element);
        } catch (e) {
          console.log(e.message);
        }
      }
    });

    onSelectCallbacks.push(selectedDate => {
      if (this.props.input && this.props.input.onChange) {
        this.props.input.onChange(selectedDate);
      }
    });

    let options = {
      prevText: '<i class="fa fa-chevron-left"></i>',
      nextText: '<i class="fa fa-chevron-right"></i>',
      onSelect: selectedDate =>
        onSelectCallbacks.map(
          callback => callback.call(callback, selectedDate),
        ),
    };

    if (minDate)
      options.minDate = minDate;
    if (maxDate)
      options.maxDate = maxDate;
    if (numberOfMonths)
      options.numberOfMonths = numberOfMonths;
    if (dateFormat)
      options.dateFormat = dateFormat;
    if (defaultDate)
      options.defaultDate = defaultDate;
    if (changeMonth)
      options.changeMonth = changeMonth;

    element.datepicker(options);
  }

  render() {
    const { className = '', placeholder = '', input } = this.props;
    return (
      <input
        type="text"
        className={className}
        placeholder={placeholder}
        ref={input => {
            this.input = input;
          }}
        {...input}
      />
    );
  }
}

const InputDate = props => {
  const {
    label = false,
    error = false,
    name,
    placeholder = '',
    dateformat = 'mm/dd/yy',
    required = false,
    ...otherProps,
  } = props;

  if (label) {
    return (
      <section>
        <label className="label">{label}</label>
        <label className={error ? 'input state-error' : 'input'}>
          <i className="icon-append fa fa-calendar"></i>
          <Field
            name={name}
            className={'input-xs' + (required ? ' required-input' : '')}
            component={DatePicker}
            dateFormat={dateformat}
            placeholder={placeholder}
            {...otherProps}
          />
        </label>
      </section>
    );
  }

  return (
    <label className="input">
      <i className="icon-append fa fa-calendar"></i>
      <Field
        name={name}
            className={'input-xs' + (required ? ' required-input' : '')}

        component={DatePicker}
        type="text"
        placeholder={placeholder}
        {...otherProps}
      />
    </label>
  );
};

export default InputDate;
