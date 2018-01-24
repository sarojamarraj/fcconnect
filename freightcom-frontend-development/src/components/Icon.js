import React, { Component } from 'react';

class Icon extends Component {
  render() {
    const { name, ...otherProps } = this.props;
    const classNames = 'fa fa-fw fa-md fa-' + name;
    return <span className={classNames} {...otherProps} />;
  }
}

export default Icon;
