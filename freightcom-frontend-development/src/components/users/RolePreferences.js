import React, { Component } from 'react';

import { Row, Col } from 'react-bootstrap';

class RolePreferences extends Component {
  render = () => {
    return <form>{this.renderOptions()}</form>;
  };

  renderOptions = () => {
    const { role, changeProperty, index, value } = this.props;

    if (role.roleName === 'FREIGHTCOM_STAFF') {
      return (
        <div>
          <Row>
            <Col className="col" sm={12}>
              <label className="xxx">
                <span>Can Manage Claims:&nbsp;</span>
                <input
                  name="canManageClaims"
                  type="checkbox"
                  checked={role.canManageClaims ? true : false}
                  value={value}
                  onChange={e => {
                    changeProperty('canManageClaims', e.target.checked, index);
                  }}
                />&nbsp;
              </label>
            </Col>
          </Row>
          <Row>
            <Col className="col" sm={12}>
              <label className="xxxx">
                <span>Can Manage Disputes:&nbsp;</span>
                <input
                  name="canManageDisputes"
                  type="checkbox"
                  checked={role.canManageDisputes ? true : false}
                  value={value}
                  onChange={e => {
                    changeProperty(
                      'canManageDisputes',
                      e.target.checked,
                      index,
                    );
                  }}
                />
              </label>
            </Col>
          </Row>
          <Row>
            <Col className="col" sm={12}>
              <label className="xxxx">
                <span>Can Enter Payments:&nbsp;</span>
                <input
                  name="canEnterPayments"
                  type="checkbox"
                  checked={role.canEnterPayments ? true : false}
                  value={value}
                  onChange={e => {
                    changeProperty('canEnterPayments', e.target.checked, index);
                  }}
                />
              </label>
            </Col>
          </Row>
          <Row>
            <Col className="col" sm={12}>
              <label className="xxxx">
                <span>Can Generate Invoices:&nbsp;</span>
                <input
                  name="canGenerateInvoices"
                  type="checkbox"
                  checked={role.canGenerateInvoices ? true : false}
                  value={value}
                  onChange={e => {
                    changeProperty(
                      'canGenerateInvoices',
                      e.target.checked,
                      index,
                    );
                  }}
                />
              </label>
            </Col>
          </Row>
        </div>
      );
    } else {
      return (
        <Row>
          <Col className="col" sm={6}>
            No preferences to edit.
          </Col>
        </Row>
      );
    }
  };
}

export default RolePreferences;
