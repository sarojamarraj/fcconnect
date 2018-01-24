import React, { Component } from 'react';
import { connect } from 'react-redux';

import { Row, Col, ButtonToolbar, Button } from 'react-bootstrap';

import { getPageItems, selectAllFromEntities } from '../../reducers';

import { loadCustomerMarkup } from '../../actions/markups';
import { loadCarriers, loadServices } from '../../actions/orders';
import DataTable from '../../components/DataTable';

class CustomerMarkups extends Component {
  constructor(props) {
    super(props);
    this.state = {
      shipmentType: 'pallet',
    };

    this.columns = [
      {
        label: 'Service',
        field: 'serviceName',
      },
      {
        label: 'Carrier',
        field: 'carrierName',
      },
      {
        label: 'Type',
        field: 'carrierType',
      },
      {
        label: 'Min Markup',
        field: 'minMarkup',
      },
      {
        label: 'Markup',
        field: 'minMarkup',
      },
      {
        label: 'International',
        field: 'minMarkup',
      },
      {
        label: "Int'l Inbound",
        field: 'minMarkup',
      },
      {
        label: 'Disabled?',
      },
      {
        label: 'CA',
        field: 'minMarkup',
      },
      {
        label: 'US',
        field: 'minMarkup',
      },
    ];
  }

  componentWillMount() {
    this.props.loadCustomerMarkup();
    //this.props.loadCarriers();
    this.props.loadServices();
  }

  render() {
    return (
      <div>

        <table className="table table-bordered table-condensed smart-form">
          <tbody>
            <tr>
              <th>Courier</th>
              <td>
                <label className="radio">
                  <input
                    name="shipmentType"
                    type="radio"
                    value="env"
                    checked={this.state.shipmentType === 'env'}
                    onChange={this.changeShipmentType}
                  />
                  <i />
                  <span>Letter</span>
                </label>
              </td>
              <td>
                <label className="radio">
                  <input
                    name="shipmentType"
                    type="radio"
                    value="pak"
                    checked={this.state.shipmentType === 'pak'}
                    onChange={this.changeShipmentType}
                  />
                  <i />
                  <span>Pak</span>
                </label>
              </td>
              <td>
                <label className="radio">
                  <input
                    name="shipmentType"
                    type="radio"
                    value="package"
                    checked={this.state.shipmentType === 'package'}
                    onChange={this.changeShipmentType}
                  />
                  <i />
                  <span>Package</span>
                </label>
              </td>
            </tr>
            <tr>
              <th>Trucking</th>
              <td colSpan="3">
                <label className="radio">
                  <input
                    name="shipmentType"
                    type="radio"
                    value="pallet"
                    checked={this.state.shipmentType === 'pallet'}
                    onChange={this.changeShipmentType}
                  />
                  <i />
                  <span>LTL</span>
                </label>
              </td>
            </tr>
          </tbody>
        </table>
        <hr className="simple" />

        <div className="well smart-form">
          <div className="">

            <Row>
              <Col className="col" xs={6}>
                <section>
                  <label className="label">Service Name</label>
                  <label className="select">
                    <select className="input-xs">
                      <option key={'service-00'}>- Select Service -</option>
                      {this.props.services.map(service =>
                        <option
                          key={'service-' + service.id}
                          value={service.id}>
                          {service.name}
                        </option>,
                      )}
                    </select>
                    <i />
                  </label>
                </section>
              </Col>
              <Col className="col" xs={6}>
                <section>
                  <label className="label">Carrier Name</label>
                  <label className="select">
                    <select className="input-xs">
                      <option key={'carrier-00'}>- Select Carrier -</option>
                      {this.props.carriers.map(carrier =>
                        <option
                          key={'carrier-' + carrier.id}
                          value={carrier.id}>
                          {carrier.name}
                        </option>,
                      )}
                    </select>
                    <i />
                  </label>
                </section>
              </Col>
            </Row>

            <Row>
              <Col className="col" xs={6}>
                <section>
                  <label className="label">Status</label>
                  <label className="select">
                    <select className="input-xs">
                      <option>Enabled</option>
                      <option>Disabled</option>
                    </select>
                    <i />
                  </label>
                </section>
              </Col>
              <Col className="col" xs={6}>
                <section>
                  <label className="label">Region</label>
                  <label className="select">
                    <select className="input-xs">
                      <option>All Regions</option>
                      <option>Canada</option>
                      <option>USA</option>
                    </select>
                    <i />
                  </label>
                </section>
              </Col>
            </Row>

            <Row>
              <Col className="col" xs={3}>
                <section>
                  <label className="label">Minimum Markup</label>
                  <label className="input">
                    <input type="text" />
                  </label>
                </section>
              </Col>
              <Col className="col" xs={3}>
                <section>
                  <label className="label">Markup</label>
                  <label className="input">
                    <input type="text" />
                  </label>
                </section>
              </Col>
              <Col className="col" xs={3}>
                <section>
                  <label className="label">International</label>
                  <label className="input">
                    <input type="text" />
                  </label>
                </section>
              </Col>
              <Col className="col" xs={3}>
                <section>
                  <label className="label">International Inbound</label>
                  <label className="input">
                    <input type="text" />
                  </label>
                </section>
              </Col>
            </Row>
          </div>

          <ButtonToolbar>
            <Button bsStyle="primary" bsSize="small">Update</Button>
            <Button bsStyle="info" bsSize="small">Switch to Edit Mode</Button>
          </ButtonToolbar>
        </div>

        <br />

        <DataTable
          columns={this.columns}
          renderTableFilters={false}
          {...this.props.datatable}
        />
      </div>
    );
  }

  changeShipmentType = e => {
    const shipmentType = e.target.value;
    this.setState({ shipmentType });
  };
}

const mapStateToProps = state => {
  return {
    datatable: getPageItems(state, 'markups'),
    carriers: selectAllFromEntities(state, 'carrier'),
    services: selectAllFromEntities(state, 'service'),
  };
};

const mapDispatchToProps = { loadCustomerMarkup, loadCarriers, loadServices };

export default connect(mapStateToProps, mapDispatchToProps)(CustomerMarkups);
