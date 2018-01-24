import React, { PureComponent } from 'react';

class AlertMessage extends PureComponent {
  render() {
    const { title = '', text = '', buttonToolbar = null } = this.props;
    return (
      <div className="divMessageBox animated fadeIn fast">
        <div className="MessageBoxContainer animated fadeIn fast">
          <div className="MessageBoxMiddle">
            <span className="MsgTitle">{title}</span>
            <p className="pText">
              {text}
            </p>
            <div className="MessageBoxButtonSection">
              {buttonToolbar}
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default AlertMessage;
