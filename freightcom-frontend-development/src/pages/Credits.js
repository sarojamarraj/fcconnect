import React, { Component } from 'react';
import { connect } from 'react-redux';
import Widget from '../components/Widget';
import CreditTable from '../components/credits/CreditTable';

class Credits extends Component {
  render() {
    return (
      <Widget title="Credit">
        <CreditTable />
      </Widget>
    );
  }
}

const mapStateToProps = state => {
  const { loggedIn: { role = {} } } = state;

  return { activeRole: role };
};

export default connect(mapStateToProps)(Credits);
