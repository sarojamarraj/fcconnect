import React, { Component } from 'react';
import Widget from '../components/Widget';

class Loading extends Component {
  render() {
    return (
      <Widget title="Error">
        <div id="errorPage">
          <h2>
            <i className="fa fa-warning" />
            {' '} Loading
          </h2>
          <p />
        </div>
      </Widget>
    );
  }
}

export default Loading;
