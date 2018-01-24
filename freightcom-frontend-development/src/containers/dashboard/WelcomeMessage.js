import React, { Component } from 'react';
import { connect } from 'react-redux';
import { loadSettings } from '../../actions/settings';
import { getSystemProperty } from '../../reducers';

class WelcomeMessage extends Component {
  componentWillMount() {
    this.props.loadSettings();
  }

  render() {
    const { title, body } = this.props;

    return (
      <div className="jarviswidget jarviswidget-color-blueLight" id="wid-id-3">
        <div>
          <div className="widget-body">
            <h1>{title}</h1>
            <p>
              {body}
            </p>
          </div>
        </div>
      </div>
    );
  }
}

const mapStateToProps = state => {
  const props = state.entities.systemProperties
    ? {
        title: getSystemProperty(state, 'welcome_title'),
        body: getSystemProperty(state, 'welcome_message'),
      }
    : {};

  return props;
};

export default connect(mapStateToProps, { loadSettings })(WelcomeMessage);
