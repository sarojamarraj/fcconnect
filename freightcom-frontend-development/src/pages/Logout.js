import { Component } from 'react';
import { connect } from 'react-redux';
import { browserHistory } from 'react-router';
import { logoutAction, LOGGED_IN } from '../actions/loginSubmit';

class Logout extends Component {
  componentWillMount() {
    this.props.logoutAction();
  }

  componentDidUpdate() {
    const { loggedIn } = this.props;
    if (loggedIn.status !== LOGGED_IN) {
      browserHistory.replace('/login');
    }
  }

  render() {
    return null;
  }
}

const mapStateToProps = state => {
  return {
    loggedIn: state.loggedIn,
  };
};

export default connect(mapStateToProps, {
  logoutAction,
})(Logout);
