import React, { Component } from 'react';
import { Field } from 'redux-form';

class TimePicker extends Component {
  constructor(props) {
    super(props);
    const [hourNow, minuteNow] = props.defaultValue &&
      props.defaultValue.split(':');

    this.state = {
      hour: hourNow < 10 ? '0' + parseInt(hourNow, 10) : hourNow,
      minute: minuteNow < 10 ? '0' + parseInt(minuteNow, 10) : minuteNow,
    };
  }

  componentDidMount() {
    this.props.input.onChange(`${this.state.hour}:${this.state.minute}`);
  }

  onChange(e, clock) {
    let value = e.target.value;

    this.setState(
      {
        [clock]: value,
      },
      () => {
        this.props.input.onChange(`${this.state.hour}:${this.state.minute}`);
      },
    );
  }

  render() {
    const props = this.props;
    const hours = [];

    for (let i = 1; i < 24; i++) {
      let str = '00' + i.toString();
      hours.push(<option key={i}>{str.substr(str.length - 2)}</option>);
    }

    const mins = [];

    for (let i = 0; i < 60; i += 15) {
      let str = '00' + i.toString();
      mins.push(<option key={i}>{str.substr(str.length - 2)}</option>);
    }

    return (
      <section className="clearfix">
        <label className="label">{props.label}</label>
        <label
          className={
            this.props.error
              ? 'select pull-left state-error'
              : 'select pull-left'
          }
          style={{ width: '48%' }}
        >
          <select
            className="input-sm"
            value={this.state.hour}
            onChange={e => this.onChange(e, 'hour')}
          >
            {hours}
          </select>
          <i />
        </label>
        <span className="wo-input-separator"> : </span>
        <label
          className={
            this.props.error
              ? 'select pull-right state-error'
              : 'select pull-right'
          }
          style={{ width: '48%' }}
        >
          <select
            className="input-sm"
            value={this.state.minute}
            onChange={e => this.onChange(e, 'minute')}
          >
            {mins}
          </select>
          <i />
        </label>
      </section>
    );
  }
}

const InputTime = props => {
  const {
    name,
    label,
    defaultValue = '9:00',
    error = false,
  } = props;

  return (
    <Field
      name={name}
      label={label}
      error={error}
      component={TimePicker}
      defaultValue={defaultValue}
    />
  );
};

export default InputTime;
