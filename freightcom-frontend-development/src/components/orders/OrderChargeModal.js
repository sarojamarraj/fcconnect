import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Modal, Button } from 'react-bootstrap';

import { formatCurrency } from '../../utils';
import {
  addOrderCharge,
  editOrderCharge,
  loadAccessorialServices,
} from '../../actions/orders';
import { getEntitiesPage, getPageItems } from '../../reducers';

class OrderChargeModal extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isNew: !!!this.props.charge.id,
      validate: false,
    };
    this.chargeData = {};
  }

  componentWillMount() {
    this.props.loadAccessorialServices();
    this.chargeData = this.props.charge;

    if (this.chargeData !== 'new' && this.props.carrierid) {
      this.chargeData['carrierId'] = this.props.carrierId;
    }
  }

  render() {
    return (
      <Modal show={this.props.showModal} onHide={this.props.toggleModal}>
        <Modal.Header closeButton>
          <Modal.Title>
            {this.state.isNew ? 'Add Charge' : 'Edit Charge'}
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {this.renderTable()}
        </Modal.Body>
        <Modal.Footer>
          <Button
            bsStyle="default"
            onClick={this.props.toggleModal}>
            Cancel
          </Button>
          <Button
            bsStyle="primary"
            onClick={this.handleFormSubmit}
            disabled={!this.state.validate && this.state.isNew}>
            {this.state.isNew ? 'Add' : 'Update'}
          </Button>
        </Modal.Footer>
      </Modal>
    );
  }

  renderTable = () => {
    const {
      charge: {
        carrier = false,
        accessorialId = '',
        description = '',
        cost = '',
        charge = '',
        applyCommission = 0,
      } = {},
      showCarrier = false,
    } = this.props;
    const carrierId = carrier && carrier.id;
    return (
      <table className="table table-bordered table-condensed">
        <thead>
          <tr>
            <th colSpan={showCarrier ? 2 : 1}>Name</th>
            <th>Charge</th>
            <th>Cost</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            {showCarrier &&
              this.state.isNew &&
              <td>
                <select
                  name="carrierId"
                  className="form-control"
                  onChange={this.handleInputChange}
                  defaultValue={carrierId || this.props.carrierId}>
                  <option key={99999} value="">- choose a provider -</option>
                  {this.props.carriers.map(item =>
                    <option key={item.id} value={item.id}>{item.name}</option>,
                  )}
                </select>
              </td>}
            <td colSpan={this.state.isNew || !showCarrier ? 1 : 2}>
              {this.state.isNew
                ? <select
                    name="accessorialId"
                    className="form-control"
                    onChange={this.handleInputChange}
                    defaultValue={accessorialId}>
                    <option key={99999} value="">- choose a service -</option>
                    {this.props.accessorialServices.map(item =>
                      <option key={item.id} value={item.id}>
                        {item.name}
                      </option>,
                    )}
                  </select>
                : description}
            </td>
            <td>
              <input
                type="text"
                className="form-control currency"
                name="charge"
                defaultValue={formatCurrency(charge)}
                maxLength="8"
                size="4"
                onChange={this.handleInputChange}
                onFocus={this.handleOnFocus}
              />
            </td>
            <td>
              <input
                type="text"
                className="form-control currency"
                name="cost"
                defaultValue={formatCurrency(cost)}
                maxLength="8"
                size="4"
                onChange={this.handleInputChange}
                onFocus={this.handleOnFocus}
              />
            </td>
          </tr>
          {this.state.isNew &&
            <tr>
              <td colSpan={showCarrier ? 4 : 3}>
                <div className="form-group">
                  <label>Description:</label>
                  <textarea
                    type="text"
                    name="description"
                    className="form-control"
                    defaultValue={description}
                    onChange={this.handleInputChange}
                  />
                </div>
                <div>
                  <label>
                    <input
                      type="checkbox"
                      name="applyCommission"
                      value="1"
                      defaultChecked={applyCommission}
                      ref={i => (this.applyCommissionInput = i)}
                    />
                    {' '}
                    Apply Commission
                  </label>
                </div>
              </td>
            </tr>}
          <tr>
            <td colSpan={showCarrier ? 4 : 3}>
              <label>
                <input
                  type="checkbox"
                  name="notifyCustomer"
                  value="1"
                  ref={i => (this.notifyCustomerInput = i)}
                />
                {' '}
                Notify Customer
              </label>
            </td>
          </tr>
        </tbody>
      </table>
    );
  };

  handleOnFocus = e => {
    if (e.target.value === '0.00') e.target.select();
  }

  handleInputChange = e => {
    this.chargeData = {
      ...this.chargeData,
      [e.target.name]: e.target.value,
    };

    this.setState({
      validate: this.validateChargeData(),
    });
  };

  validateChargeData = () => {
    let status = false;
    if (this.chargeData.accessorialId) {
      status = true;
      if (
        this.chargeData.accessorialId === '-1' &&
        !this.chargeData.description
      ) {
        status = false;
      }
    }
    return status;
  };

  handleFormSubmit = e => {
    e.preventDefault();
    const chargeData = {
      accessorialId: this.chargeData.accessorialId,
      carrierId: this.chargeData.carrierId,
      cost: this.chargeData.cost,
      charge: this.chargeData.charge,
      description: this.chargeData.description,
      applyCommission:
        this.applyCommissionInput &&
          (this.applyCommissionInput.checked ? 1 : 0),
      notifyCustomer:
        this.notifyCustomerInput && (this.notifyCustomerInput.checked ? 1 : 0),
    };

    if (!chargeData.carrierId) {
      delete chargeData['carrierId'];
    }

    if (this.chargeData.id) {
      this.props.editOrderCharge(this.chargeData.id, chargeData, data =>
        this.props.onSubmitCallback(data),
      );
    } else {
      this.props.addOrderCharge(this.props.orderId, chargeData, data =>
        this.props.onSubmitCallback(data),
      );
    }
  };
}

const mapStateToProps = (state, ownProps) => {
  const { packageTypeName } = ownProps;

  return {
    accessorialServices: getEntitiesPage(state, 'accessorialServices').filter(
      service => !service.type || service.type === packageTypeName,
    ),
    carriers: getPageItems(state, 'carrier').items,
  };
};

const mapDispatchToProps = {
  loadAccessorialServices,
  addOrderCharge,
  editOrderCharge,
};

export default connect(mapStateToProps, mapDispatchToProps)(OrderChargeModal);
