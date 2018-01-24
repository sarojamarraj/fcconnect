import React, { Component } from 'react';
import NotificationsSystem from 'reapop';
import theme from 'reapop-theme-wybo';
import Header from '../components/Header';
import Navigation from '../components/Navigation';

class App extends Component {
  render() {
    return (
      <div className="App">
        <Header />
        <NotificationsSystem theme={theme} />
        <Navigation />
        <div id="main" className="container" role="main">
          <div id="content">
            {this.props.children}
          </div>
        </div>
      </div>
    );
  }
}

export default App;
