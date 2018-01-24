import React, { Component } from 'react';

class FormGroup extends Component {
  render() {
    const { type = false, inputRef = '', ...inputProps } = this.props;
    return (
      <div className="form-group">
        <label className="form-label">
          {this.props.label}
        </label>
        {type
          ? <input
              type={type}
              ref={inputRef}
              className="form-control"
              {...inputProps}
            />
          : this.props.children}
      </div>
    );
  }
}

export default FormGroup;
