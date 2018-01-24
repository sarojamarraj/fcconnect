import React from 'react';
import { connect } from 'react-redux';
import { browserHistory } from 'react-router';

class CreditRedirect extends React.Component {
  componentWillMount() {
    setTimeout(() => {
      browserHistory.replace(`/customers/${this.props.routeParams.id}`);
    }, 0);
  }

  render() {
    return null;
  }
}

const mapDispatchToProps = {};

export default connect(null, mapDispatchToProps)(CreditRedirect);
